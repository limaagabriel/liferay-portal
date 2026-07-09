/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useEffect, useMemo, useRef} from 'react';

import {getCategoricalColors} from '../../palette';
import {useChartContainer} from '../ChartContainerContext';
import {getNumericAxisKey} from '../plot/scale';

import '../../../css/BarSeries.scss';

import type {ChartSeriesExtent} from '../ChartContainerContext';
import type {ChartNumericAxisKey, ChartSymmetricScale} from '../plot/scale';
import type {ChartScheme} from '../types';

const MIN_BAR_WIDTH = 4;
const BAR_WIDTH_RATIO = 0.6;
const BAR_RX = 2;
const INLINE_BAR_THICKNESS = 8;
const VALUE_LABEL_OFFSET = 6;
const VALUE_LABEL_TEXT_HEIGHT = 12;
const BAR_REVEAL_DELAY_STEP = 60;

/**
 * Shades of blue for the `blue` scheme, mirroring `LineSeries`'s own
 * `BLUE_SHADES`. Duplicated rather than shared for the same reason: each
 * composable series resolves its own color independently, with no central
 * series registry to key a shared palette off of.
 */
const BLUE_SHADES = [
	'var(--primary, light-dark(#0b5fff, #6198ff))',
	'var(--blue-d2, light-dark(#005fcc, #94c4ff))',
	'var(--blue-l2, light-dark(#66abff, #006be6))',
	'var(--blue-d1, light-dark(#006be6, #70b1ff))',
	'var(--blue-l3, light-dark(#97c5ff, #0056b8))',
];

export interface BarSeriesProps<T> {

	/** Overrides the cycled hue (categorical scheme) or blue shade. */
	color?: string;

	/**
	 * Gives each bar its own hue from `getCategoricalColors`, indexed by the
	 * bar's position, instead of one color for the whole series. Also
	 * switches legend registration from a single series row to one row per
	 * bar (category + color + value). Default `false`.
	 */
	colorByCategory?: boolean;

	/**
	 * Position in the series' render order, driving the cycled color when
	 * `color` is not set.
	 */
	colorIndex?: number;

	/** Formats a value for the per-bar `aria-label`. Defaults to `String`. */
	format?: (value: number) => string;

	/** Stable identity used for series registration and focus tracking. */
	id: string;

	label: string;

	/** Rounds the bar into a pill (radius equal to half its thickness). */
	rounded?: boolean;

	/**
	 * Draws each bar's formatted value as decorative `<text>` near the bar's
	 * tip. `aria-hidden`, since the same value already ships in the bar's
	 * `aria-label`. Default `false`.
	 */
	showValues?: boolean;

	/**
	 * Bar thickness preset. `default` bars fill `BAR_WIDTH_RATIO` of their
	 * band; `inline` flattens every bar to `INLINE_BAR_THICKNESS`.
	 */
	size?: 'default' | 'inline';

	/**
	 * Draws a faint track rect behind each bar, spanning the full plot
	 * extent along the numeric axis, so the bar reads as "progress out of
	 * total". Default `false`.
	 */
	track?: boolean;

	x: (item: T) => string;
	y: (item: T) => number;
}

interface BarSeriesBar {
	category: string;
	height: number;
	index: number;
	value: number;
	width: number;
	x: number;
	y: number;
}

interface BarSeriesValueLabel {
	anchor: 'end' | 'middle' | 'start';
	x: number;
	y: number;
}

interface BarSeriesTrackRect {
	height: number;
	width: number;
	x: number;
	y: number;
}

function computeExtent<T>(
	data: readonly T[],
	y: (item: T) => number
): ChartSeriesExtent {
	const values = data.map(y).filter((value) => Number.isFinite(value));

	if (!values.length) {
		return {max: 0, min: 0};
	}

	return {max: Math.max(...values), min: Math.min(...values)};
}

/**
 * Resolves the shared bar thickness for the whole series: `inline` flattens
 * every bar to `INLINE_BAR_THICKNESS` (the old progress-bar-row behavior),
 * `default` keeps the current band-ratio width.
 */
function resolveBarThickness(
	size: 'default' | 'inline',
	bandSize: number
): number {
	if (size === 'inline') {
		return INLINE_BAR_THICKNESS;
	}

	return Math.max(MIN_BAR_WIDTH, bandSize * BAR_WIDTH_RATIO);
}

function resolveBarRx(rounded: boolean, barThickness: number): number {
	return rounded ? barThickness / 2 : BAR_RX;
}

/**
 * Positions a bar's decorative value label at its tip (the end farthest
 * from the baseline), offset away from the bar so it never overlaps it.
 * Negative bars grow the other direction, so the label flips sides with
 * the bar's sign instead of always sitting above/right of it.
 */
function resolveValueLabel(
	bar: BarSeriesBar,
	numericAxisKey: ChartNumericAxisKey
): BarSeriesValueLabel {
	const isPositive = bar.value >= 0;

	if (numericAxisKey === 'x') {
		return {
			anchor: isPositive ? 'start' : 'end',
			x: isPositive
				? bar.x + bar.width + VALUE_LABEL_OFFSET
				: bar.x - VALUE_LABEL_OFFSET,
			y: bar.y + bar.height / 2,
		};
	}

	return {
		anchor: 'middle',
		x: bar.x + bar.width / 2,
		y: isPositive
			? bar.y - VALUE_LABEL_OFFSET
			: bar.y + bar.height + VALUE_LABEL_TEXT_HEIGHT,
	};
}

/**
 * Sizes a bar's background track to span the full plot extent along the
 * numeric axis while matching the bar's own position and thickness along
 * the categorical axis, so it reads as "progress out of total" in either
 * orientation.
 */
function resolveTrackRect(
	bar: BarSeriesBar,
	scale: ChartSymmetricScale,
	numericAxisKey: ChartNumericAxisKey,
	barThickness: number
): BarSeriesTrackRect {
	if (numericAxisKey === 'x') {
		return {
			height: barThickness,
			width: scale.plot.width,
			x: scale.plot.x,
			y: bar.y,
		};
	}

	return {
		height: scale.plot.height,
		width: barThickness,
		x: bar.x,
		y: scale.plot.y,
	};
}

/**
 * Projects `data` onto the container's shared `scale`: a band center on the
 * categorical axis and the bar's span from the numeric axis' baseline to the
 * value's projected position on the numeric axis. Negative values land on
 * the other side of the baseline instead of being clamped, since the unified
 * domain already carries them. Bars with a non-finite value are dropped.
 *
 * `numericAxisKey` picks which axis grows the bar: `'y'` produces the
 * vertical bars `BarChart` has always rendered (band on X, height on Y);
 * `'x'` produces horizontal bars (band on Y, width on X) for a chart whose
 * `xAxis` is numeric and `yAxis` categorical.
 */
function computeGeometry<T>(
	data: readonly T[],
	x: (item: T) => string,
	y: (item: T) => number,
	scale: ChartSymmetricScale,
	numericAxisKey: ChartNumericAxisKey,
	barThickness: number
): Array<BarSeriesBar | null> {
	return data.map((item, index): BarSeriesBar | null => {
		const value = y(item);

		if (!Number.isFinite(value)) {
			return null;
		}

		const category = x(item);

		if (numericAxisKey === 'x') {
			const bandCenter = scale.yPosition(index);
			const valueX = scale.xPosition(value);

			return {
				category,
				height: barThickness,
				index,
				value,
				width: Math.abs(valueX - scale.baseline),
				x: Math.min(valueX, scale.baseline),
				y: bandCenter - barThickness / 2,
			};
		}

		const bandCenter = scale.xPosition(index);
		const valueY = scale.yPosition(value);

		return {
			category,
			height: Math.abs(valueY - scale.baseline),
			index,
			value,
			width: barThickness,
			x: bandCenter - barThickness / 2,
			y: Math.min(valueY, scale.baseline),
		};
	});
}

/**
 * Anchors the active datum's position at the bar's tip (the end farthest
 * from the baseline) — the point a tooltip would later attach to.
 */
function resolveBarAnchor(
	bar: BarSeriesBar,
	numericAxisKey: ChartNumericAxisKey
): {x: number; y: number} {
	const isPositive = bar.value >= 0;

	if (numericAxisKey === 'x') {
		return {
			x: isPositive ? bar.x + bar.width : bar.x,
			y: bar.y + bar.height / 2,
		};
	}

	return {
		x: bar.x + bar.width / 2,
		y: isPositive ? bar.y : bar.y + bar.height,
	};
}

/**
 * Resolves the CSS `transform-origin` a bar's reveal-grow animation scales
 * from, so it always grows away from the numeric axis' baseline rather than
 * from its own bounding box. A positive bar's rect already sits with one
 * edge on the baseline (`resolveBarAnchor`'s "tip" is the far edge instead),
 * so a negative bar — whose rect sits on the opposite edge — must flip the
 * origin to the other side, in both orientations.
 */
function resolveBarTransformOrigin(
	bar: BarSeriesBar,
	numericAxisKey: ChartNumericAxisKey
): string {
	const isPositive = bar.value >= 0;

	if (numericAxisKey === 'x') {
		return isPositive ? 'left center' : 'right center';
	}

	return isPositive ? 'center bottom' : 'center top';
}

/**
 * Resolves a bar rect's inline style: its per-index reveal delay and
 * baseline-relative transform-origin (always set, since neither does
 * anything without the `is-animated` class), plus the `colorByCategory`
 * override when present.
 */
function resolveBarStyle(
	bar: BarSeriesBar,
	numericAxisKey: ChartNumericAxisKey,
	barColor: string | undefined
): React.CSSProperties {
	return {
		'--charts-bar-color': barColor,
		'--charts-bar-delay': `${bar.index * BAR_REVEAL_DELAY_STEP}ms`,
		'transformOrigin': resolveBarTransformOrigin(bar, numericAxisKey),
	} as React.CSSProperties;
}

/**
 * Resolves the id a bar's active datum publishes: in `colorByCategory` mode
 * it must match the per-bar registered legend entry (`${id}-${index}`)
 * rather than the whole series' `id`, so the Legend row highlighted by a
 * hovered/focused bar is the same row that bar itself registered.
 */
function resolveActiveSeriesId(
	id: string,
	colorByCategory: boolean,
	index: number
): string {
	return colorByCategory ? `${id}-${index}` : id;
}

function resolveColor(
	scheme: ChartScheme,
	colorIndex: number,
	color: string | undefined
): string {
	if (color) {
		return color;
	}

	if (scheme === 'categorical') {
		return getCategoricalColors(colorIndex + 1)[colorIndex];
	}

	return BLUE_SHADES[colorIndex % BLUE_SHADES.length];
}

/**
 * A single accessor-based bar series, rendered as a `<g>` fragment inside
 * the shared `ChartPlot` svg. Registers its y-extent with the container so
 * the unified Y domain reflects it, and acts as one keyboard tab stop: arrow
 * keys rove focus among this series' own bars, wired to the provider's
 * per-series `focus`/`setFocus`. Does not navigate across series.
 *
 * The per-bar rect mirrors `BarChartBar`'s a11y idiom (`role="img"`,
 * `aria-label`, roving `tabIndex`) rather than importing it: `BarChartBar`
 * couples the rect to `BarLayout`'s track/label/value satellites, which this
 * series recreates directly (`track`, `showValues`) instead of reimporting.
 *
 * Per-bar category text is deliberately not offered alongside `showValues`:
 * the shared `Axis` already renders one category label per band, so a
 * second copy on every bar would just duplicate it.
 */
export default function BarSeries<T>({
	color,
	colorByCategory = false,
	colorIndex = 0,
	format = String,
	id,
	label,
	rounded = false,
	showValues = false,
	size = 'default',
	track = false,
	x,
	y,
}: BarSeriesProps<T>) {
	const {
		active,
		animated,
		data,
		focus,
		registerSeries,
		scale,
		scheme,
		setActive,
		setFocus,
		xAxis,
		yAxis,
	} = useChartContainer<T>();

	const barRefs = useRef<Array<SVGRectElement | null>>([]);

	const resolvedColor = resolveColor(scheme, colorIndex, color);
	const numericAxisKey = getNumericAxisKey(xAxis, yAxis);
	const barThickness = resolveBarThickness(size, scale.bandSize);
	const barRx = resolveBarRx(rounded, barThickness);

	/**
	 * Memo key: `data`, the `x`/`y` accessor references, `scale`, and
	 * `barThickness` (derived from `size` and `scale.bandSize`). Pass stable
	 * accessor identities from the consumer, or this recomputes every render.
	 */
	const bars = useMemo(
		() => computeGeometry(data, x, y, scale, numericAxisKey, barThickness),
		[data, x, y, scale, numericAxisKey, barThickness]
	);

	const categoricalColors = useMemo(
		() => (colorByCategory ? getCategoricalColors(data.length) : null),
		[colorByCategory, data.length]
	);

	/**
	 * Non-`colorByCategory` mode registers the series as a single legend
	 * row, unchanged from before. `colorByCategory` mode instead registers
	 * one row per finite datum (`${id}-${index}`), each carrying its own
	 * category/value in `label` and its own hue in `color`, so the shared
	 * `Legend` lists one entry per bar. The registration channel itself
	 * (`registerSeries`, keyed by `id` in a `Map`) already supports several
	 * entries from one series without any change to it: calling it more
	 * than once, and unregistering every call on cleanup, is enough.
	 *
	 * Deliberately derived from `data`/`x`/`y` here, not from the rendered
	 * `bars` (which come from `scale`): `scale` is itself derived from every
	 * registered series' extent, so an effect keyed on `bars` would
	 * re-register on every `scale` change that registering causes, looping
	 * forever.
	 */
	useEffect(() => {
		if (!colorByCategory || !categoricalColors) {
			return registerSeries({
				color: resolvedColor,
				extent: computeExtent(data, y),
				id,
				label,
			});
		}

		const unregisters = data.reduce<Array<() => void>>(
			(accumulator, item, index) => {
				const value = y(item);

				if (!Number.isFinite(value)) {
					return accumulator;
				}

				accumulator.push(
					registerSeries({
						color: categoricalColors[index] ?? resolvedColor,
						extent: {max: value, min: value},
						id: `${id}-${index}`,
						label: `${x(item)}: ${format(value)}`,
					})
				);

				return accumulator;
			},
			[]
		);

		return () => {
			for (const unregister of unregisters) {
				unregister();
			}
		};
	}, [
		categoricalColors,
		colorByCategory,
		data,
		format,
		id,
		label,
		registerSeries,
		resolvedColor,
		x,
		y,
	]);

	const finiteIndexes = useMemo(
		() =>
			bars.reduce<number[]>((accumulator, bar, index) => {
				if (bar) {
					accumulator.push(index);
				}

				return accumulator;
			}, []),
		[bars]
	);

	const focusedIndex = focus?.seriesId === id ? focus.index : null;
	const tabbableIndex = focusedIndex ?? finiteIndexes[0] ?? null;

	const setBarRef = useCallback(
		(index: number, element: SVGRectElement | null) => {
			barRefs.current[index] = element;
		},
		[]
	);

	const focusBarAt = useCallback((index: number) => {
		barRefs.current[index]?.focus();
	}, []);

	const onFocusBar = useCallback(
		(index: number) => setFocus({index, seriesId: id}),
		[id, setFocus]
	);

	const onBlurBar = useCallback(
		(index: number) => {
			if (focus?.seriesId === id && focus?.index === index) {
				setFocus(null);
			}
		},
		[focus, id, setFocus]
	);

	const activateBar = useCallback(
		(bar: BarSeriesBar) => {
			setActive({
				category: bar.category,
				index: bar.index,
				label,
				position: resolveBarAnchor(bar, numericAxisKey),
				seriesId: resolveActiveSeriesId(id, colorByCategory, bar.index),
				value: bar.value,
			});
		},
		[colorByCategory, id, label, numericAxisKey, setActive]
	);

	const deactivateBar = useCallback(
		(index: number) => {
			const seriesId = resolveActiveSeriesId(id, colorByCategory, index);

			if (active?.seriesId === seriesId && active?.index === index) {
				setActive(null);
			}
		},
		[active, colorByCategory, id, setActive]
	);

	/**
	 * Clears the active datum on pointer leave only when the bar is not also
	 * keyboard-focused, so moving the mouse off a bar the user is still
	 * focused on keeps the highlight the focus ring is showing.
	 */
	const onMouseLeaveBar = useCallback(
		(index: number) => {
			if (barRefs.current[index] === document.activeElement) {
				return;
			}

			deactivateBar(index);
		},
		[deactivateBar]
	);

	const onKeyDownBar = useCallback(
		(index: number, event: React.KeyboardEvent) => {
			const position = finiteIndexes.indexOf(index);

			let handled = true;

			switch (event.key) {
				case 'ArrowDown':
				case 'ArrowRight':
					focusBarAt(
						finiteIndexes[
							Math.min(position + 1, finiteIndexes.length - 1)
						]
					);
					break;
				case 'ArrowLeft':
				case 'ArrowUp':
					focusBarAt(finiteIndexes[Math.max(position - 1, 0)]);
					break;
				case 'Home':
					focusBarAt(finiteIndexes[0]);
					break;
				case 'End':
					focusBarAt(finiteIndexes[finiteIndexes.length - 1]);
					break;
				default:
					handled = false;
			}

			if (handled) {
				event.preventDefault();
			}
		},
		[finiteIndexes, focusBarAt]
	);

	return (
		<g
			className={`charts-bar-series${
				numericAxisKey === 'x' ? ' charts-bar-series--horizontal' : ''
			}${animated ? ' is-animated' : ''}`}
			style={{'--charts-bar-color': resolvedColor} as React.CSSProperties}
		>
			{bars.map((bar) => {
				if (!bar) {
					return null;
				}

				const isFocused = focusedIndex === bar.index;
				const isTabbable = tabbableIndex === bar.index;
				const barColor = categoricalColors?.[bar.index];
				const valueLabel = showValues
					? resolveValueLabel(bar, numericAxisKey)
					: null;
				const trackRect = track
					? resolveTrackRect(bar, scale, numericAxisKey, barThickness)
					: null;

				return (
					<g key={bar.index}>
						{trackRect && (
							<rect
								aria-hidden="true"
								className="charts-bar-series__bar-track"
								height={trackRect.height}
								rx={barRx}
								width={trackRect.width}
								x={trackRect.x}
								y={trackRect.y}
							/>
						)}

						<rect
							aria-label={`${label}, ${bar.category}: ${format(
								bar.value
							)}`}
							className={`charts-bar-series__bar${
								isFocused ? ' is-focused' : ''
							}`}
							height={bar.height}
							onBlur={() => {
								onBlurBar(bar.index);
								deactivateBar(bar.index);
							}}
							onFocus={() => {
								onFocusBar(bar.index);
								activateBar(bar);
							}}
							onKeyDown={(event) =>
								onKeyDownBar(bar.index, event)
							}
							onMouseEnter={() => activateBar(bar)}
							onMouseLeave={() => onMouseLeaveBar(bar.index)}
							ref={(element) => setBarRef(bar.index, element)}
							role="img"
							rx={barRx}
							style={resolveBarStyle(
								bar,
								numericAxisKey,
								barColor
							)}
							tabIndex={isTabbable ? 0 : -1}
							width={bar.width}
							x={bar.x}
							y={bar.y}
						/>

						{valueLabel && (
							<text
								aria-hidden="true"
								className="charts-bar-series__value"
								textAnchor={valueLabel.anchor}
								x={valueLabel.x}
								y={valueLabel.y}
							>
								{format(bar.value)}
							</text>
						)}
					</g>
				);
			})}
		</g>
	);
}
