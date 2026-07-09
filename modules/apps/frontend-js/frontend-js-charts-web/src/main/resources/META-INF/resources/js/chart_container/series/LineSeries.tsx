/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useEffect, useMemo, useRef} from 'react';

import {
	dashPatternFor,
	markerShapeFor,
	renderMarker,
} from '../../line_chart/plot/markers';
import {getCategoricalColors} from '../../palette';
import {useChartContainer} from '../ChartContainerContext';

import '../../../css/LineSeries.scss';

import type {LineMarkerShape} from '../../line_chart/plot/markers';
import type {ChartSeriesExtent} from '../ChartContainerContext';
import type {ChartSymmetricScale} from '../plot/scale';
import type {ChartScheme} from '../types';

const MARKER_SIZE = 4;
const MARKER_SIZE_FOCUSED = 6;
const HALO_SIZE = 8;
const POINT_HIT_RADIUS = 10;
const MARKER_REVEAL_DELAY_STEP = 40;

/**
 * Shades of blue for the `blue` scheme, mirroring `LineChart`'s `BLUE_SHADES`.
 * Duplicated here (rather than shared) because each composable `LineSeries`
 * resolves its own color independently, with no central series registry to
 * key a shared palette off of.
 */
const BLUE_SHADES = [
	'var(--primary, light-dark(#0b5fff, #6198ff))',
	'var(--blue-d2, light-dark(#005fcc, #94c4ff))',
	'var(--blue-l2, light-dark(#66abff, #006be6))',
	'var(--blue-d1, light-dark(#006be6, #70b1ff))',
	'var(--blue-l3, light-dark(#97c5ff, #0056b8))',
];

export interface LineSeriesProps<T> {

	/** Overrides the cycled hue (categorical scheme) or blue shade. */
	color?: string;

	/**
	 * Position in the series' render order, driving the cycled color, dash
	 * pattern, and marker shape when the matching override is not set.
	 */
	colorIndex?: number;

	/** Overrides the cycled `stroke-dasharray`. */
	dasharray?: string;

	/** Formats a value for the per-point `aria-label`. Defaults to `String`. */
	format?: (value: number) => string;

	/** Stable identity used for series registration and focus tracking. */
	id: string;

	label: string;

	/** Overrides the cycled marker shape. */
	marker?: LineMarkerShape;

	x: (item: T) => string;
	y: (item: T) => number;
}

interface LineSeriesPoint {
	category: string;
	index: number;
	value: number;
	x: number;
	y: number;
}

interface LineSeriesGeometry {
	paths: string[];
	points: Array<LineSeriesPoint | null>;
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

function buildPaths(points: Array<LineSeriesPoint | null>): string[] {
	const paths: string[] = [];

	let current: LineSeriesPoint[] = [];

	const flush = () => {
		if (current.length > 1) {
			paths.push(
				current
					.map(
						(point, index) =>
							`${index === 0 ? 'M' : 'L'} ${point.x} ${point.y}`
					)
					.join(' ')
			);
		}

		current = [];
	};

	for (const point of points) {
		if (point) {
			current.push(point);
		}
		else {
			flush();
		}
	}

	flush();

	return paths;
}

/**
 * Projects `data` onto the container's shared `scale`: x per index (band
 * center) and y per value (unified domain). Points with a non-finite `y`
 * become gaps, splitting the line into more than one path segment.
 */
function computeGeometry<T>(
	data: readonly T[],
	x: (item: T) => string,
	y: (item: T) => number,
	scale: ChartSymmetricScale
): LineSeriesGeometry {
	const points = data.map((item, index): LineSeriesPoint | null => {
		const value = y(item);

		if (!Number.isFinite(value)) {
			return null;
		}

		return {
			category: x(item),
			index,
			value,
			x: scale.xPosition(index),
			y: scale.yPosition(value),
		};
	});

	return {paths: buildPaths(points), points};
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
 * A single accessor-based line series, rendered as a `<g>` fragment inside
 * the shared `ChartPlot` svg. Registers its y-extent with the container so
 * the unified Y domain reflects it, and acts as one keyboard tab stop: arrow
 * keys rove focus among this series' own points, wired to the provider's
 * per-series `focus`/`setFocus`. Does not navigate across series.
 */
export default function LineSeries<T>({
	color,
	colorIndex = 0,
	dasharray,
	format = String,
	id,
	label,
	marker,
	x,
	y,
}: LineSeriesProps<T>) {
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
	} = useChartContainer<T>();

	const pointRefs = useRef<Array<SVGCircleElement | null>>([]);

	const resolvedColor = resolveColor(scheme, colorIndex, color);

	useEffect(
		() =>
			registerSeries({
				color: resolvedColor,
				extent: computeExtent(data, y),
				id,
				label,
			}),
		[data, id, label, registerSeries, resolvedColor, y]
	);

	/**
	 * Memo key: `data`, the `x`/`y` accessor references, and `scale`. Pass
	 * stable accessor identities (module-level functions or memoized
	 * callbacks) from the consumer, or this recomputes every render.
	 */
	const geometry = useMemo(
		() => computeGeometry(data, x, y, scale),
		[data, x, y, scale]
	);

	const finiteIndexes = useMemo(
		() =>
			geometry.points.reduce<number[]>((accumulator, point, index) => {
				if (point) {
					accumulator.push(index);
				}

				return accumulator;
			}, []),
		[geometry]
	);

	const focusedIndex = focus?.seriesId === id ? focus.index : null;
	const tabbableIndex = focusedIndex ?? finiteIndexes[0] ?? null;

	const resolvedDasharray = dasharray ?? dashPatternFor(colorIndex);
	const resolvedMarker = marker ?? markerShapeFor(colorIndex);

	const setPointRef = useCallback(
		(index: number, element: SVGCircleElement | null) => {
			pointRefs.current[index] = element;
		},
		[]
	);

	const focusPointAt = useCallback((index: number) => {
		pointRefs.current[index]?.focus();
	}, []);

	const onFocusPoint = useCallback(
		(index: number) => setFocus({index, seriesId: id}),
		[id, setFocus]
	);

	const onBlurPoint = useCallback(
		(index: number) => {
			if (focus?.seriesId === id && focus?.index === index) {
				setFocus(null);
			}
		},
		[focus, id, setFocus]
	);

	/**
	 * Publishes the point's marker position (`point.x`/`point.y`) as the
	 * active datum's anchor — the point a tooltip would later attach to.
	 */
	const activatePoint = useCallback(
		(point: LineSeriesPoint) => {
			setActive({
				category: point.category,
				index: point.index,
				label,
				position: {x: point.x, y: point.y},
				seriesId: id,
				value: point.value,
			});
		},
		[id, label, setActive]
	);

	const deactivatePoint = useCallback(
		(index: number) => {
			if (active?.seriesId === id && active?.index === index) {
				setActive(null);
			}
		},
		[active, id, setActive]
	);

	/**
	 * Clears the active datum on pointer leave only when the point is not also
	 * keyboard-focused, so moving the mouse off a point the user is still
	 * focused on keeps the highlight the focus ring is showing.
	 */
	const onMouseLeavePoint = useCallback(
		(index: number) => {
			if (pointRefs.current[index] === document.activeElement) {
				return;
			}

			deactivatePoint(index);
		},
		[deactivatePoint]
	);

	const onKeyDownPoint = useCallback(
		(index: number, event: React.KeyboardEvent) => {
			const position = finiteIndexes.indexOf(index);

			let handled = true;

			switch (event.key) {
				case 'ArrowRight':
					focusPointAt(
						finiteIndexes[
							Math.min(position + 1, finiteIndexes.length - 1)
						]
					);
					break;
				case 'ArrowLeft':
					focusPointAt(finiteIndexes[Math.max(position - 1, 0)]);
					break;
				case 'Home':
					focusPointAt(finiteIndexes[0]);
					break;
				case 'End':
					focusPointAt(finiteIndexes[finiteIndexes.length - 1]);
					break;
				default:
					handled = false;
			}

			if (handled) {
				event.preventDefault();
			}
		},
		[finiteIndexes, focusPointAt]
	);

	return (
		<g
			className={`charts-line-series${animated ? ' is-animated' : ''}`}
			style={
				{'--charts-line-color': resolvedColor} as React.CSSProperties
			}
		>
			{geometry.paths.map((path, index) => (
				<path
					className="charts-line-series__line"
					d={path}
					key={index}
					style={{strokeDasharray: resolvedDasharray}}
				/>
			))}

			{geometry.points.map((point) => {
				if (!point) {
					return null;
				}

				const isFocused = focusedIndex === point.index;
				const isTabbable = tabbableIndex === point.index;

				return (
					<g
						className={`charts-line-series__point-group${
							isFocused ? ' is-focused' : ''
						}`}
						key={point.index}
					>
						{isFocused && (
							<g
								aria-hidden="true"
								className="charts-line-series__halo"
								transform={`translate(${point.x} ${point.y})`}
							>
								{renderMarker(resolvedMarker, HALO_SIZE)}
							</g>
						)}

						<g
							aria-hidden="true"
							className="charts-line-series__marker"
							transform={`translate(${point.x} ${point.y})`}
						>
							<g
								className="charts-line-series__marker-inner"
								style={
									{
										'--charts-marker-delay': `${
											point.index *
											MARKER_REVEAL_DELAY_STEP
										}ms`,
									} as React.CSSProperties
								}
							>
								{renderMarker(
									resolvedMarker,
									isFocused
										? MARKER_SIZE_FOCUSED
										: MARKER_SIZE
								)}
							</g>
						</g>

						<circle
							aria-label={`${label}, ${point.category}: ${format(
								point.value
							)}`}
							className="charts-line-series__point"
							cx={point.x}
							cy={point.y}
							onBlur={() => {
								onBlurPoint(point.index);
								deactivatePoint(point.index);
							}}
							onFocus={() => {
								onFocusPoint(point.index);
								activatePoint(point);
							}}
							onKeyDown={(event) =>
								onKeyDownPoint(point.index, event)
							}
							onMouseEnter={() => activatePoint(point)}
							onMouseLeave={() => onMouseLeavePoint(point.index)}
							r={POINT_HIT_RADIUS}
							ref={(element) => setPointRef(point.index, element)}
							role="img"
							tabIndex={isTabbable ? 0 : -1}
						/>
					</g>
				);
			})}
		</g>
	);
}
