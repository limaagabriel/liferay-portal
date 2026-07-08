/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	Axis,
	BarSeries,
	ChartContainer,
	ChartLineSeries,
	ChartPlot,
	Grid,
	Legend,
} from '@liferay/frontend-js-charts-web';
import React from 'react';

import {SampleContainer} from './SampleContainer';

const COMBO_DATA = [
	{label: 'Jan', revenue: 4200, target: 4000},
	{label: 'Feb', revenue: 3900, target: 4000},
	{label: 'Mar', revenue: 4600, target: 4200},
	{label: 'Apr', revenue: 5100, target: 4200},
	{label: 'May', revenue: 4800, target: 4500},
	{label: 'Jun', revenue: 5400, target: 4500},
];

const CATEGORIES = COMBO_DATA.map((datum) => datum.label);

function comboCategory(datum) {
	return datum.label;
}

function comboRevenue(datum) {
	return datum.revenue;
}

function comboTarget(datum) {
	return datum.target;
}

function ComboPlot() {
	return (
		<ChartPlot
			className="mx-auto"
			description="Monthly revenue bars against the monthly target line"
			style={{maxWidth: 640}}
			title="Revenue vs. target"
		>
			<Grid />

			<Axis />

			<BarSeries
				colorIndex={0}
				id="revenue"
				label="Revenue"
				x={comboCategory}
				y={comboRevenue}
			/>

			<ChartLineSeries
				colorIndex={1}
				id="target"
				label="Target"
				x={comboCategory}
				y={comboTarget}
			/>
		</ChartPlot>
	);
}

// The flagship composability demo: one BarSeries and one ChartLineSeries
// share the same ChartContainer data and axes inside a single ChartPlot svg,
// something the monolithic LineChart/BarChart facades cannot express since
// each owns its own container and axis pair.

export function ComboChartSamples() {
	return (
		<>
			<SampleContainer label="Bar + line series sharing one plot and axes">
				<ChartContainer
					categories={CATEGORIES}
					data={COMBO_DATA}
					scheme="categorical"
					xAxis={{
						categoryCount: COMBO_DATA.length,
						type: 'categorical',
					}}
					yAxis={{tickCount: 5, type: 'numeric'}}
				>
					<ComboPlot />

					<Legend layout="list" />
				</ChartContainer>
			</SampleContainer>

			<SampleContainer label="Controlled error (error prop set)">
				<ChartContainer
					categories={CATEGORIES}
					data={COMBO_DATA}
					error="Unable to load chart data"
					scheme="categorical"
					xAxis={{
						categoryCount: COMBO_DATA.length,
						type: 'categorical',
					}}
					yAxis={{tickCount: 5, type: 'numeric'}}
				>
					<ComboPlot />

					<Legend layout="list" />
				</ChartContainer>
			</SampleContainer>

			<SampleContainer label="Uncontrolled empty (derived from data length)">
				<ChartContainer
					categories={[]}
					data={[]}
					scheme="categorical"
					xAxis={{categoryCount: 0, type: 'categorical'}}
					yAxis={{tickCount: 5, type: 'numeric'}}
				>
					<ComboPlot />

					<Legend layout="list" />
				</ChartContainer>
			</SampleContainer>
		</>
	);
}
