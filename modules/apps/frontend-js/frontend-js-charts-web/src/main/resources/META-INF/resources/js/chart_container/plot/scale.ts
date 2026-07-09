/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import type {
	ChartAxisConfig,
	ChartCategoricalAxisConfig,
	ChartNumericAxisConfig,
} from '../types';

export interface ChartPlotPadding {
	bottom: number;
	left: number;
	right: number;
	top: number;
}

export interface ChartAxisGeometry {
	extent: number;
	offset: number;
	reverse: boolean;
}

export interface ChartAxisTick {
	position: number;
	value: number;
}

export interface ChartCategoricalAxisPosition {
	bandSize: number;
	categoryPositions: number[];
	forValue: (index: number) => number;
	type: 'categorical';
}

export interface ChartNumericAxisPosition {
	baseline: number;
	forValue: (value: number) => number;
	ticks: ChartAxisTick[];
	type: 'numeric';
}

export type ChartAxisPosition =
	| ChartCategoricalAxisPosition
	| ChartNumericAxisPosition;

export interface ChartSymmetricScale {
	bandSize: number;
	baseline: number;
	categoryPositions: number[];
	plot: {height: number; width: number; x: number; y: number};
	ticks: ChartAxisTick[];
	xPosition: (value: number) => number;
	yPosition: (value: number) => number;
}

interface SymmetricOptions {
	height: number;
	padding: ChartPlotPadding;
	valueMax: number;
	valueMin: number;
	width: number;
	xAxis: ChartAxisConfig;
	yAxis: ChartAxisConfig;
}

interface NumericDomain {
	domainMax: number;
	domainMin: number;
	domainRange: number;
	step: number;
}

/**
 * "Nice" tick step from the {1, 2, 2.5, 5} x 10^k family, so axis labels land
 * on readable values (5, 10, 25, 50) rather than arbitrary fractions.
 */
export function niceTickStep(range: number, count: number): number {
	if (range <= 0 || count <= 0) {
		return 1;
	}

	const rough = range / count;
	const magnitude = Math.pow(10, Math.floor(Math.log10(rough)));
	const normalized = rough / magnitude;

	let nice;

	if (normalized < 1.5) {
		nice = 1;
	}
	else if (normalized < 3) {
		nice = 2;
	}
	else if (normalized < 4) {
		nice = 2.5;
	}
	else if (normalized < 7) {
		nice = 5;
	}
	else {
		nice = 10;
	}

	return nice * magnitude;
}

/**
 * Resolves the numeric domain a value range settles into once it is
 * widened to the nearest nice tick step, so bar/line/area scales and the
 * symmetric per-axis scale derive ticks from one shared computation.
 */
function getNumericDomain(
	valueMin: number,
	valueMax: number,
	tickCount: number
): NumericDomain {
	const step = niceTickStep(
		Math.max(valueMax - valueMin, valueMax, 1),
		tickCount
	);

	const domainMin = valueMin >= 0 ? 0 : Math.floor(valueMin / step) * step;
	const domainMax = Math.max(
		domainMin + step,
		Math.ceil(valueMax / step) * step
	);

	return {
		domainMax,
		domainMin,
		domainRange: domainMax - domainMin,
		step,
	};
}

function getCategoricalAxisPosition(
	axis: ChartCategoricalAxisConfig,
	geometry: ChartAxisGeometry
): ChartCategoricalAxisPosition {
	const bandSize = geometry.extent / Math.max(1, axis.categoryCount);

	const forValue = (index: number) =>
		geometry.offset + index * bandSize + bandSize / 2;

	const categoryPositions = Array.from(
		{length: axis.categoryCount},
		(_, index) => forValue(index)
	);

	return {bandSize, categoryPositions, forValue, type: 'categorical'};
}

function getNumericAxisPosition(
	axis: ChartNumericAxisConfig,
	geometry: ChartAxisGeometry,
	valueMin: number,
	valueMax: number
): ChartNumericAxisPosition {
	const {domainMax, domainMin, domainRange, step} = getNumericDomain(
		valueMin,
		valueMax,
		axis.tickCount
	);

	const forValue = (value: number) => {
		const ratio = (value - domainMin) / domainRange;

		return geometry.reverse
			? geometry.offset + geometry.extent * (1 - ratio)
			: geometry.offset + geometry.extent * ratio;
	};

	const ticks: ChartAxisTick[] = [];

	for (let value = domainMin; value <= domainMax + step / 2; value += step) {
		ticks.push({position: forValue(value), value});
	}

	return {baseline: forValue(0), forValue, ticks, type: 'numeric'};
}

/**
 * Maps a single axis's config (categorical or numeric) onto pixel positions
 * along the given geometry, so the X and Y axes of a chart can each declare
 * their own type independently instead of X always being the band axis.
 */
export function getAxisPosition(
	axis: ChartAxisConfig,
	geometry: ChartAxisGeometry,
	valueMin: number,
	valueMax: number
): ChartAxisPosition {
	if (axis.type === 'categorical') {
		return getCategoricalAxisPosition(axis, geometry);
	}

	return getNumericAxisPosition(axis, geometry, valueMin, valueMax);
}

export type ChartNumericAxisKey = 'x' | 'y';

/**
 * Reports which of the two axis configs carries the numeric domain, so
 * `BarSeries`, `Axis`, and `Grid` derive orientation from the same source
 * instead of each guessing at a hardcoded axis or a separate orientation
 * prop. `'y'` is the vertical chart (bars grow up); `'x'` is horizontal
 * (bars grow right).
 */
export function getNumericAxisKey(
	xAxis: ChartAxisConfig,
	yAxis: ChartAxisConfig
): ChartNumericAxisKey {
	if (xAxis.type === 'numeric' && yAxis.type === 'categorical') {
		return 'x';
	}

	if (yAxis.type === 'numeric' && xAxis.type === 'categorical') {
		return 'y';
	}

	throw new Error(
		'Expected exactly one numeric axis and one categorical axis'
	);
}

function pickNumericAxisPosition(
	xAxisPosition: ChartAxisPosition,
	yAxisPosition: ChartAxisPosition
): ChartNumericAxisPosition {
	if (xAxisPosition.type === 'numeric') {
		return xAxisPosition;
	}

	if (yAxisPosition.type === 'numeric') {
		return yAxisPosition;
	}

	throw new Error('Expected exactly one numeric axis');
}

function pickCategoricalAxisPosition(
	xAxisPosition: ChartAxisPosition,
	yAxisPosition: ChartAxisPosition
): ChartCategoricalAxisPosition {
	if (xAxisPosition.type === 'categorical') {
		return xAxisPosition;
	}

	if (yAxisPosition.type === 'categorical') {
		return yAxisPosition;
	}

	throw new Error('Expected exactly one categorical axis');
}

/**
 * Computes a coordinate scale whose X and Y axes each resolve their own
 * position independently from their own config, so a vertical chart
 * (X-categorical/Y-numeric) and a horizontal chart (X-numeric/Y-categorical)
 * share the same math instead of X being hardcoded to the band axis.
 *
 * The Y geometry reverses (high value -> low pixel) to match screen-space
 * top-down pixels against bottom-up data values; X does not, so a numeric
 * X axis grows left-to-right like the data itself.
 */
export function getSymmetricChartScale({
	height,
	padding,
	valueMax,
	valueMin,
	width,
	xAxis,
	yAxis,
}: SymmetricOptions): ChartSymmetricScale {
	const plotWidth = Math.max(0, width - padding.left - padding.right);
	const plotHeight = Math.max(0, height - padding.top - padding.bottom);

	const xAxisPosition = getAxisPosition(
		xAxis,
		{extent: plotWidth, offset: padding.left, reverse: false},
		valueMin,
		valueMax
	);

	const yAxisPosition = getAxisPosition(
		yAxis,
		{extent: plotHeight, offset: padding.top, reverse: true},
		valueMin,
		valueMax
	);

	const numericAxisPosition = pickNumericAxisPosition(
		xAxisPosition,
		yAxisPosition
	);
	const categoricalAxisPosition = pickCategoricalAxisPosition(
		xAxisPosition,
		yAxisPosition
	);

	return {
		bandSize: categoricalAxisPosition.bandSize,
		baseline: numericAxisPosition.baseline,
		categoryPositions: categoricalAxisPosition.categoryPositions,
		plot: {
			height: plotHeight,
			width: plotWidth,
			x: padding.left,
			y: padding.top,
		},
		ticks: numericAxisPosition.ticks,
		xPosition: xAxisPosition.forValue,
		yPosition: yAxisPosition.forValue,
	};
}
