/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import classNames from 'classnames';
import React, {useMemo} from 'react';

import {MapDatum} from '../types/MapDatum';
import {toPercent} from '../utils/percent';

interface MapChartLegendTableProps {
	activeIndex: number | null;
	colors: string[];
	data: MapDatum[];
	onFocus: (index: number) => void;
	onHover: (index: number) => void;
	onHoverEnd: () => void;
	titleId: string;
	total: number;
}

export default function MapChartLegendTable({
	activeIndex,
	colors,
	data,
	onFocus,
	onHover,
	onHoverEnd,
	titleId,
	total,
}: MapChartLegendTableProps) {
	const rows = useMemo(
		() =>
			data
				.map((datum, dataIndex) => ({
					color: colors[dataIndex],
					dataIndex,
					datum,
				}))
				.sort((a, b) => b.datum.value - a.datum.value)
				.map((row, sortedIndex) => ({...row, rank: sortedIndex + 1})),
		[data, colors]
	);

	return (
		<table aria-labelledby={titleId} className="chart-map-legend-table">
			<thead>
				<tr>
					<th scope="col">#</th>

					<th scope="col">
						<span className="sr-only">
							{Liferay.Language.get('color')}
						</span>
					</th>

					<th scope="col">{Liferay.Language.get('country')}</th>

					<th scope="col">{Liferay.Language.get('value')}</th>

					<th scope="col">{Liferay.Language.get('share')}</th>
				</tr>
			</thead>

			<tbody>
				{rows.map((row) => (
					<tr
						className={classNames('chart-map-legend-table-row', {
							'is-active': activeIndex === row.dataIndex,
						})}
						key={row.dataIndex}
						onClick={() => onFocus(row.dataIndex)}
						onMouseEnter={() => onHover(row.dataIndex)}
						onMouseLeave={onHoverEnd}
					>
						<td>{row.rank}</td>

						<td>
							<span
								className="chart-map-legend-table-swatch"
								style={{background: row.color}}
							/>
						</td>

						<th scope="row">{row.datum.label}</th>

						<td>{row.datum.value}</td>

						<td>{toPercent(row.datum.value, total)}%</td>
					</tr>
				))}
			</tbody>
		</table>
	);
}
