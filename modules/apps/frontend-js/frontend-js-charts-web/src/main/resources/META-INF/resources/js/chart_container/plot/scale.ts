/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import type {
	ChartCategoricalAxisConfig,
	ChartNumericAxisConfig,
} from '../types';

export interface ChartPlotPadding {
	bottom: number;
	left: number;
	right: number;
	top: number;
}

export interface ChartScaleTick {
	value: number;
	y: number;
}

export interface ChartScale {
	plot: {height: number; width: number; x: number; y: number};
	ticks: ChartScaleTick[];
	xForIndex: (index: number) => number;
	yDomain: {max: number; min: number};
	yForValue: (value: number) => number;
}

interface Options {
	height: number;
	padding: ChartPlotPadding;
	valueMax: number;
	valueMin: number;
	width: number;
	xAxis: ChartCategoricalAxisConfig;
	yAxis: ChartNumericAxisConfig;
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
 * Computes the band-x / numeric-y coordinate scale every chart type projects
 * its geometry onto: x positions center each category on its band (unlike an
 * edge-anchored point scale), and y spans a single numeric domain that
 * extends below zero for negative data instead of clamping at a baseline.
 */
export function getChartScale({
	height,
	padding,
	valueMax,
	valueMin,
	width,
	xAxis,
	yAxis,
}: Options): ChartScale {
	const plotWidth = Math.max(0, width - padding.left - padding.right);
	const plotHeight = Math.max(0, height - padding.top - padding.bottom);

	const bandSize = plotWidth / Math.max(1, xAxis.categoryCount);

	const xForIndex = (index: number) =>
		padding.left + index * bandSize + bandSize / 2;

	const step = niceTickStep(
		Math.max(valueMax - valueMin, valueMax, 1),
		yAxis.tickCount
	);

	const domainMin = valueMin >= 0 ? 0 : Math.floor(valueMin / step) * step;
	const domainMax = Math.max(
		domainMin + step,
		Math.ceil(valueMax / step) * step
	);
	const domainRange = domainMax - domainMin;

	const yForValue = (value: number) =>
		padding.top + plotHeight * (1 - (value - domainMin) / domainRange);

	const ticks: ChartScaleTick[] = [];

	for (let value = domainMin; value <= domainMax + step / 2; value += step) {
		ticks.push({value, y: yForValue(value)});
	}

	return {
		plot: {
			height: plotHeight,
			width: plotWidth,
			x: padding.left,
			y: padding.top,
		},
		ticks,
		xForIndex,
		yDomain: {max: domainMax, min: domainMin},
		yForValue,
	};
}
