/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	Axis,
	BarSeries,
	ChartContainer,
	ChartPlot,
	Grid,
} from '@liferay/frontend-js-charts-web';
import React from 'react';

import {SampleContainer} from './SampleContainer';

const HORIZONTAL_BAR_DATA = [
	{department: 'Engineering', headcount: 42},
	{department: 'Sales', headcount: -18},
	{department: 'Support', headcount: 26},
	{department: 'Marketing', headcount: 15},
	{department: 'Finance', headcount: 9},
];

const CATEGORIES = HORIZONTAL_BAR_DATA.map((datum) => datum.department);

const DIMS = {
	padding: {bottom: 40, left: 96, right: 24, top: 16},
};

function horizontalCategory(datum) {
	return datum.department;
}

function horizontalValue(datum) {
	return datum.headcount;
}

// Declaring `xAxis` numeric and `yAxis` categorical (the mirror of the
// vertical default) is enough to flip BarSeries/Axis/Grid into a horizontal
// layout: bars grow rightward from the numeric baseline, numeric ticks sit
// on the bottom edge, and category labels sit on the left. No orientation
// prop exists — the axis config alone drives it.

export function HorizontalBarChartSamples() {
	return (
		<SampleContainer label="Horizontal bars (numeric X, categorical Y)">
			<ChartContainer
				categories={CATEGORIES}
				data={HORIZONTAL_BAR_DATA}
				dims={DIMS}
				scheme="categorical"
				xAxis={{tickCount: 5, type: 'numeric'}}
				yAxis={{
					categoryCount: HORIZONTAL_BAR_DATA.length,
					type: 'categorical',
				}}
			>
				<ChartPlot
					className="mx-auto"
					description="Headcount change by department"
					style={{maxWidth: 640}}
					title="Headcount change by department"
				>
					<Grid />

					<Axis />

					<BarSeries
						colorIndex={0}
						id="headcount"
						label="Headcount change"
						x={horizontalCategory}
						y={horizontalValue}
					/>
				</ChartPlot>
			</ChartContainer>
		</SampleContainer>
	);
}
