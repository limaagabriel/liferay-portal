/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import classNames from 'classnames';
import React, {useCallback, useMemo} from 'react';

import Axis from '../chart_container/Axis';
import ChartContainer from '../chart_container/ChartContainer';
import ChartPlot from '../chart_container/ChartPlot';
import Grid from '../chart_container/Grid';
import Legend from '../chart_container/Legend';
import ComposableLineSeries from '../chart_container/series/LineSeries';

import type {LineChartProps} from './types';

/**
 * Thin facade over the composable chart primitives: translates the legacy
 * `series`/`categories` prop shape into an index-keyed data array plus one
 * accessor-based `LineSeries` per line, so existing consumers keep their
 * current prop API while rendering goes through the shared pipeline.
 *
 * `animated` and `pointTooltip` still drive their legacy BEM modifier
 * classes on the root element (kept for prop/CSS compatibility), but no
 * primitive in the composable pipeline implements reveal animation or a
 * value tooltip yet, so both are effectively no-ops today.
 */
export default function LineChart({
	animated = true,
	categories,
	className,
	description,
	height = 320,
	legend = 'list',
	pointTooltip = 'popover',
	scheme = 'blue',
	series,
	title,
	width = 640,
	yFormat,
	yTicks = 5,
}: LineChartProps) {
	const format = useMemo(() => yFormat ?? String, [yFormat]);

	const data = useMemo(
		() => categories.map((_, index) => index),
		[categories]
	);

	const categoryAt = useCallback(
		(index: number) => categories[index] ?? '',
		[categories]
	);

	const valueAccessors = useMemo(
		() =>
			series.map((line) => (index: number) => {
				const value = line.values[index];

				return value === null ? NaN : value;
			}),
		[series]
	);

	const resolvedDescription = useMemo(() => {
		if (description) {
			return description;
		}

		return series.map((line) => line.description ?? line.label).join('. ');
	}, [description, series]);

	return (
		<ChartContainer
			categories={categories}
			data={data}
			dims={{height, width}}
			scheme={scheme}
			xAxis={{categoryCount: categories.length, type: 'categorical'}}
			yAxis={{tickCount: yTicks, type: 'numeric'}}
		>
			<ChartPlot
				className={classNames(
					'charts-line-chart',
					`charts-line-chart--${scheme}`,
					`charts-line-chart--legend-${legend}`,
					`charts-line-chart--tooltip-${pointTooltip}`,
					{
						'charts-line-chart--motion': animated,
					},
					className
				)}
				description={resolvedDescription}
				style={{maxWidth: width}}
				title={title}
			>
				<Grid />

				<Axis yFormat={format} />

				{series.map((line, index) => (
					<ComposableLineSeries
						color={line.color}
						colorIndex={index}
						dasharray={line.dasharray}
						format={format}
						id={`line-${index}`}
						key={`${line.label}-${index}`}
						label={line.label}
						marker={line.marker}
						x={categoryAt}
						y={valueAccessors[index]}
					/>
				))}
			</ChartPlot>

			<Legend layout={legend} />
		</ChartContainer>
	);
}
