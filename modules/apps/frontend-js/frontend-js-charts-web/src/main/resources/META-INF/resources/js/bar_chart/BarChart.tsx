/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import classNames from 'classnames';
import React, {useMemo} from 'react';

import Axis from '../chart_container/Axis';
import ChartContainer from '../chart_container/ChartContainer';
import ChartPlot from '../chart_container/ChartPlot';
import Grid from '../chart_container/Grid';
import Legend from '../chart_container/Legend';
import ComposableBarSeries from '../chart_container/series/BarSeries';

import type {BarChartProps, BarDatum} from './types';

const Y_TICK_COUNT = 5;

function barCategory(datum: BarDatum): string {
	return datum.label;
}

function barValue(datum: BarDatum): number {
	return datum.value;
}

/**
 * Thin facade over the composable chart primitives: maps the single
 * `data: BarDatum[]` array onto one composable `BarSeries`.
 *
 * Several legacy behaviors are not wired through the composable pipeline:
 *
 * - `orientation: 'horizontal'` is unsupported; the shared scale is
 *   band-x/numeric-y only.
 * - `rounded` and `track` are unsupported; `BarSeries` always renders a
 *   fixed corner radius and never draws a background track.
 * - `size: 'inline'` still suppresses the axis/grid as before, but no
 *   longer flattens every bar to a fixed thickness (band-ratio width only).
 * - `scheme: 'categorical'` no longer gives each bar its own hue: a single
 *   `BarSeries` resolves one color for the whole series, so every bar
 *   renders the same categorical shade.
 * - The shared `Legend` lists one entry per registered series. Since this
 *   facade registers a single `BarSeries` for the whole chart, `legend`
 *   can no longer list one row per bar (with per-bar value/share columns
 *   and focus-on-select) the way the old `BarChartLegend` did.
 * - Per-bar value/category `<text>` labels (drawn on/above each bar by the
 *   old `BarChartBar`) are gone; category labels now come from the shared
 *   `Axis` and values are exposed only through each bar's `aria-label`.
 *
 * These are kept on `BarChartProps` for prop-API compatibility and
 * documented here rather than silently dropped.
 */
export default function BarChart({
	animated = true,
	className,
	data,
	description,
	height = 280,
	legend = 'none',
	orientation = 'vertical',
	rounded = false,
	scheme = 'blue',
	size = 'default',
	title,
	track = false,
	width = 480,
}: BarChartProps) {
	const categories = useMemo(() => data.map((datum) => datum.label), [data]);

	const resolvedDescription = useMemo(() => {
		if (description) {
			return description;
		}

		return data
			.map(
				(datum) => datum.description ?? `${datum.label}: ${datum.value}`
			)
			.join('. ');
	}, [data, description]);

	const showAxis = size !== 'inline';

	return (
		<ChartContainer
			categories={categories}
			data={data}
			dims={{height, width}}
			scheme={scheme}
			xAxis={{categoryCount: data.length, type: 'categorical'}}
			yAxis={{tickCount: Y_TICK_COUNT, type: 'numeric'}}
		>
			<ChartPlot
				className={classNames(
					'charts-bar-chart',
					`charts-bar-chart--${orientation}`,
					`charts-bar-chart--${scheme}`,
					`charts-bar-chart--legend-${legend}`,
					`charts-bar-chart--size-${size}`,
					{
						'charts-bar-chart--motion': animated,
						'charts-bar-chart--rounded': rounded,
						'charts-bar-chart--track': track,
					},
					className
				)}
				description={resolvedDescription}
				style={{maxWidth: width}}
				title={title}
			>
				{showAxis && <Grid />}

				{showAxis && <Axis />}

				<ComposableBarSeries
					colorIndex={0}
					id="bar-series"
					label={title}
					x={barCategory}
					y={barValue}
				/>
			</ChartPlot>

			<Legend layout={legend} />
		</ChartContainer>
	);
}
