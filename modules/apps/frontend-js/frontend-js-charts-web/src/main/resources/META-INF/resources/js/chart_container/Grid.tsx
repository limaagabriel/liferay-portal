/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {useChartContainer} from './ChartContainerContext';
import {getNumericAxisKey} from './plot/scale';

import '../../css/ChartGrid.scss';

/**
 * Gridlines perpendicular to the numeric axis, one per shared scale tick,
 * rendered as a `<g>` fragment inside the shared `ChartPlot` svg. Mirrors
 * `LineChartGridlines`'s gridline rendering; the matching tick-value text
 * lives in `Axis` instead, since gridlines and axis labels are separate
 * composable children here.
 *
 * The vertical chart (numeric on Y) draws horizontal lines spanning the
 * plot width; the horizontal chart (numeric on X, per
 * `getNumericAxisKey`) draws vertical lines spanning the plot height.
 */
export default function Grid() {
	const {scale, xAxis, yAxis} = useChartContainer();

	const isHorizontal = getNumericAxisKey(xAxis, yAxis) === 'x';

	return (
		<g aria-hidden="true" className="charts-grid">
			{scale.ticks.map((tick) =>
				isHorizontal ? (
					<line
						className="charts-grid__line"
						key={tick.value}
						x1={tick.position}
						x2={tick.position}
						y1={scale.plot.y}
						y2={scale.plot.y + scale.plot.height}
					/>
				) : (
					<line
						className="charts-grid__line"
						key={tick.value}
						x1={scale.plot.x}
						x2={scale.plot.x + scale.plot.width}
						y1={tick.position}
						y2={tick.position}
					/>
				)
			)}
		</g>
	);
}
