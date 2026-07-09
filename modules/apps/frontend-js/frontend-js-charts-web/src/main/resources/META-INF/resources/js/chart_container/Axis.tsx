/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {useChartContainer} from './ChartContainerContext';
import {getNumericAxisKey} from './plot/scale';

import '../../css/ChartAxis.scss';

const CATEGORY_LABEL_OFFSET = 12;
const TICK_LABEL_GAP = 8;
const TICK_LABEL_BASELINE_OFFSET = 4;

export interface AxisProps {

	/** Formats a numeric tick value for its label. Defaults to `String`. */
	yFormat?: (value: number) => string;
}

/**
 * Numeric tick labels and category labels, rendered as a `<g>` fragment
 * inside the shared `ChartPlot` svg. Mirrors `LineChartAxis` (the baseline)
 * and `LineChartCategoryLabels` (the per-category text), plus the tick-value
 * text `LineChartGridlines` renders alongside its lines — split out here
 * since `Grid` owns only the gridlines themselves.
 *
 * Placement follows `getNumericAxisKey`: the vertical chart (numeric on Y)
 * keeps ticks on the left edge and categories along the bottom; the
 * horizontal chart (numeric on X) puts ticks on the bottom edge and
 * categories along the left, so a numeric-X/categorical-Y `ChartContainer`
 * renders a correct horizontal axis with no separate orientation prop.
 *
 * Category labels read `categories` from the container: the categorical
 * axis is shared across every series, so the label set is a container-level
 * concern rather than something any one series provides.
 */
export default function Axis({yFormat = String}: AxisProps) {
	const {categories, dims, scale, xAxis, yAxis} = useChartContainer();

	const isHorizontal = getNumericAxisKey(xAxis, yAxis) === 'x';

	return (
		<g aria-hidden="true" className="charts-axis">
			{isHorizontal ? (
				<line
					className="charts-axis__line"
					x1={scale.plot.x}
					x2={scale.plot.x}
					y1={scale.plot.y}
					y2={scale.plot.y + scale.plot.height}
				/>
			) : (
				<line
					className="charts-axis__line"
					x1={scale.plot.x}
					x2={scale.plot.x + scale.plot.width}
					y1={scale.plot.y + scale.plot.height}
					y2={scale.plot.y + scale.plot.height}
				/>
			)}

			{scale.ticks.map((tick) =>
				isHorizontal ? (
					<text
						className="charts-axis__tick-label"
						key={tick.value}
						textAnchor="middle"
						x={tick.position}
						y={
							scale.plot.y +
							scale.plot.height +
							TICK_LABEL_GAP +
							TICK_LABEL_BASELINE_OFFSET
						}
					>
						{yFormat(tick.value)}
					</text>
				) : (
					<text
						className="charts-axis__tick-label"
						key={tick.value}
						textAnchor="end"
						x={scale.plot.x - TICK_LABEL_GAP}
						y={tick.position + TICK_LABEL_BASELINE_OFFSET}
					>
						{yFormat(tick.value)}
					</text>
				)
			)}

			{categories.map((category, index) =>
				isHorizontal ? (
					<text
						className="charts-axis__category-label"
						key={`${category}-${index}`}
						textAnchor="end"
						x={scale.plot.x - CATEGORY_LABEL_OFFSET}
						y={scale.yPosition(index) + TICK_LABEL_BASELINE_OFFSET}
					>
						{category}
					</text>
				) : (
					<text
						className="charts-axis__category-label"
						key={`${category}-${index}`}
						textAnchor="middle"
						x={scale.xPosition(index)}
						y={dims.height - CATEGORY_LABEL_OFFSET}
					>
						{category}
					</text>
				)
			)}
		</g>
	);
}
