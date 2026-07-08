/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {useChartContainer} from './ChartContainerContext';

import '../../css/ChartAxis.scss';

const CATEGORY_LABEL_OFFSET = 12;
const TICK_LABEL_GAP = 8;
const TICK_LABEL_BASELINE_OFFSET = 4;

export interface AxisProps {

	/** Formats a Y tick value for its label. Defaults to `String`. */
	yFormat?: (value: number) => string;
}

/**
 * Y tick labels and X category labels, rendered as a `<g>` fragment inside
 * the shared `ChartPlot` svg. Mirrors `LineChartAxis` (the baseline) and
 * `LineChartCategoryLabels` (the per-category text), plus the Y tick-value
 * text `LineChartGridlines` renders alongside its lines — split out here
 * since `Grid` owns only the gridlines themselves.
 *
 * Category labels read `categories` from the container: the X axis is
 * shared across every series, so the label set is a container-level concern
 * rather than something any one series provides.
 */
export default function Axis({yFormat = String}: AxisProps) {
	const {categories, dims, scale} = useChartContainer();

	return (
		<g aria-hidden="true" className="charts-axis">
			<line
				className="charts-axis__line"
				x1={scale.plot.x}
				x2={scale.plot.x + scale.plot.width}
				y1={scale.plot.y + scale.plot.height}
				y2={scale.plot.y + scale.plot.height}
			/>

			{scale.ticks.map((tick) => (
				<text
					className="charts-axis__tick-label"
					key={tick.value}
					textAnchor="end"
					x={scale.plot.x - TICK_LABEL_GAP}
					y={tick.y + TICK_LABEL_BASELINE_OFFSET}
				>
					{yFormat(tick.value)}
				</text>
			))}

			{categories.map((category, index) => (
				<text
					className="charts-axis__category-label"
					key={`${category}-${index}`}
					textAnchor="middle"
					x={scale.xForIndex(index)}
					y={dims.height - CATEGORY_LABEL_OFFSET}
				>
					{category}
				</text>
			))}
		</g>
	);
}
