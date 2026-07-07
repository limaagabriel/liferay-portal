/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import classNames from 'classnames';
import React from 'react';

import {MapDatum} from '../types/MapDatum';

interface MapChartLegendListProps {
	activeIndex: number | null;
	colors: string[];
	data: MapDatum[];
	onFocus: (index: number) => void;
	onHover: (index: number) => void;
	onHoverEnd: () => void;
}

export default function MapChartLegendList({
	activeIndex,
	colors,
	data,
	onFocus,
	onHover,
	onHoverEnd,
}: MapChartLegendListProps) {
	return (
		<ul aria-hidden="true" className="chart-map-legend-list">
			{data.map((datum, dataIndex) => (
				<li
					className={classNames('chart-map-legend-list-item', {
						'is-active': activeIndex === dataIndex,
					})}
					key={dataIndex}
					onClick={() => onFocus(dataIndex)}
					onMouseEnter={() => onHover(dataIndex)}
					onMouseLeave={onHoverEnd}
				>
					<span
						className="chart-map-legend-list-swatch"
						style={{background: colors[dataIndex]}}
					/>

					<span className="chart-map-legend-list-label">
						{datum.label}
					</span>

					<span className="chart-map-legend-list-value">
						{datum.value}
					</span>
				</li>
			))}
		</ul>
	);
}
