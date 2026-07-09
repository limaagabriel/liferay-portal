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

import type {ChartAxisConfig} from '../chart_container/types';
import type {BarChartProps, BarDatum} from './types';

const NUMERIC_TICK_COUNT = 5;

function barCategory(datum: BarDatum): string {
	return datum.label;
}

function barValue(datum: BarDatum): number {
	return datum.value;
}

/**
 * Resolves the container's axis pair from `orientation`: `horizontal` puts
 * the numeric domain on X and the band domain on Y (the mirror of the
 * `vertical` default), matching `getNumericAxisKey`'s contract so
 * `BarSeries`/`Axis`/`Grid` orient themselves with no separate prop of
 * their own.
 */
function resolveAxes(
	orientation: 'horizontal' | 'vertical',
	categoryCount: number
): {xAxis: ChartAxisConfig; yAxis: ChartAxisConfig} {
	const numericAxis: ChartAxisConfig = {
		tickCount: NUMERIC_TICK_COUNT,
		type: 'numeric',
	};
	const categoricalAxis: ChartAxisConfig = {
		categoryCount,
		type: 'categorical',
	};

	if (orientation === 'horizontal') {
		return {xAxis: numericAxis, yAxis: categoricalAxis};
	}

	return {xAxis: categoricalAxis, yAxis: numericAxis};
}

/**
 * Thin facade over the composable chart primitives: maps the single
 * `data: BarDatum[]` array onto one composable `BarSeries`.
 *
 * `orientation` drives the container's axis pair (see `resolveAxes`);
 * `rounded`, `track`, and `size` pass straight through to `BarSeries`;
 * `scheme: 'categorical'` maps to `colorByCategory`, giving each bar its own
 * hue (and its own `Legend` row) instead of one color for the whole series,
 * matching the old `BarChartLegend`'s per-bar rows; `showValues` is always
 * on, restoring the old `BarChartBar`'s unconditional per-bar value text.
 *
 * Two legacy behaviors stay unwired:
 *
 * - Per-bar category `<text>` labels (the old `BarChartBar` drew one
 *   alongside its value) are deliberately not restored: the shared `Axis`
 *   already renders one category label per band, so a per-bar copy would
 *   just duplicate it.
 * - Hovering/selecting a `Legend` row no longer highlights or focuses the
 *   matching bar (the old `BarChartLegend`'s `onActivate`/`onSelect`).
 *   `Legend` itself only wires the chart-to-legend direction so far; the
 *   reverse is deferred there, not something this facade can add on its own.
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

	const {xAxis, yAxis} = useMemo(
		() => resolveAxes(orientation, data.length),
		[orientation, data.length]
	);

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
			animated={animated}
			categories={categories}
			data={data}
			dims={{height, width}}
			scheme={scheme}
			xAxis={xAxis}
			yAxis={yAxis}
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
					colorByCategory={scheme === 'categorical'}
					colorIndex={0}
					id="bar-series"
					label={title}
					rounded={rounded}
					showValues
					size={size}
					track={track}
					x={barCategory}
					y={barValue}
				/>
			</ChartPlot>

			<Legend layout={legend} />
		</ChartContainer>
	);
}
