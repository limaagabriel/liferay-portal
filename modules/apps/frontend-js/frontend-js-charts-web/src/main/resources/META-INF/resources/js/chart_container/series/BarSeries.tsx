/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useEffect, useMemo, useRef} from 'react';

import {getCategoricalColors} from '../../palette';
import {useChartContainer} from '../ChartContainerContext';

import '../../../css/BarSeries.scss';

import type {ChartSeriesExtent} from '../ChartContainerContext';
import type {ChartScale} from '../plot/scale';
import type {ChartScheme} from '../types';

const MIN_BAR_WIDTH = 4;
const BAR_WIDTH_RATIO = 0.6;
const BAR_RX = 2;

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
	 * Position in the series' render order, driving the cycled color when
	 * `color` is not set.
	 */
	colorIndex?: number;

	/** Formats a value for the per-bar `aria-label`. Defaults to `String`. */
	format?: (value: number) => string;

	/** Stable identity used for series registration and focus tracking. */
	id: string;

	label: string;

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
 * Projects `data` onto the container's shared `scale`: x per index (band
 * center) and the bar's vertical span from the y=0 baseline to the value's
 * projected y. Negative values land on the other side of the baseline
 * instead of being clamped, since the unified Y domain already carries them.
 * Bars with a non-finite `y` are dropped.
 */
function computeGeometry<T>(
	data: readonly T[],
	x: (item: T) => string,
	y: (item: T) => number,
	scale: ChartScale,
	categoryCount: number
): Array<BarSeriesBar | null> {
	const bandSize = scale.plot.width / Math.max(1, categoryCount);
	const barWidth = Math.max(MIN_BAR_WIDTH, bandSize * BAR_WIDTH_RATIO);
	const baseline = scale.yForValue(0);

	return data.map((item, index): BarSeriesBar | null => {
		const value = y(item);

		if (!Number.isFinite(value)) {
			return null;
		}

		const centerX = scale.xForIndex(index);
		const valueY = scale.yForValue(value);

		return {
			category: x(item),
			height: Math.abs(valueY - baseline),
			index,
			value,
			width: barWidth,
			x: centerX - barWidth / 2,
			y: Math.min(valueY, baseline),
		};
	});
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
 * series excludes (axis and category labels ship in a later step).
 */
export default function BarSeries<T>({
	color,
	colorIndex = 0,
	format = String,
	id,
	label,
	x,
	y,
}: BarSeriesProps<T>) {
	const {data, focus, registerSeries, scale, scheme, setFocus, xAxis} =
		useChartContainer<T>();

	const barRefs = useRef<Array<SVGRectElement | null>>([]);

	useEffect(
		() => registerSeries(id, computeExtent(data, y)),
		[data, id, registerSeries, y]
	);

	/**
	 * Memo key: `data`, the `x`/`y` accessor references, `scale`, and the
	 * shared category count (band width depends on it). Pass stable accessor
	 * identities from the consumer, or this recomputes every render.
	 */
	const bars = useMemo(
		() => computeGeometry(data, x, y, scale, xAxis.categoryCount),
		[data, x, y, scale, xAxis.categoryCount]
	);

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

	const resolvedColor = resolveColor(scheme, colorIndex, color);

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

	const onKeyDownBar = useCallback(
		(index: number, event: React.KeyboardEvent) => {
			const position = finiteIndexes.indexOf(index);

			let handled = true;

			switch (event.key) {
				case 'ArrowRight':
					focusBarAt(
						finiteIndexes[
							Math.min(position + 1, finiteIndexes.length - 1)
						]
					);
					break;
				case 'ArrowLeft':
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
			className="charts-bar-series"
			style={{'--charts-bar-color': resolvedColor} as React.CSSProperties}
		>
			{bars.map((bar) => {
				if (!bar) {
					return null;
				}

				const isFocused = focusedIndex === bar.index;
				const isTabbable = tabbableIndex === bar.index;

				return (
					<rect
						aria-label={`${label}, ${bar.category}: ${format(
							bar.value
						)}`}
						className={`charts-bar-series__bar${
							isFocused ? ' is-focused' : ''
						}`}
						height={bar.height}
						key={bar.index}
						onBlur={() => onBlurBar(bar.index)}
						onFocus={() => onFocusBar(bar.index)}
						onKeyDown={(event) => onKeyDownBar(bar.index, event)}
						ref={(element) => setBarRef(bar.index, element)}
						role="img"
						rx={BAR_RX}
						tabIndex={isTabbable ? 0 : -1}
						width={bar.width}
						x={bar.x}
						y={bar.y}
					/>
				);
			})}
		</g>
	);
}
