/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {useChartContainer} from './ChartContainerContext';

import '../../css/ChartGrid.scss';

/**
 * Horizontal Y gridlines, one per shared scale tick, rendered as a `<g>`
 * fragment inside the shared `ChartPlot` svg. Mirrors `LineChartGridlines`'s
 * gridline rendering; the matching tick-value text lives in `Axis` instead,
 * since gridlines and axis labels are separate composable children here.
 */
export default function Grid() {
	const {scale} = useChartContainer();

	return (
		<g aria-hidden="true" className="charts-grid">
			{scale.ticks.map((tick) => (
				<line
					className="charts-grid__line"
					key={tick.value}
					x1={scale.plot.x}
					x2={scale.plot.x + scale.plot.width}
					y1={tick.y}
					y2={tick.y}
				/>
			))}
		</g>
	);
}
