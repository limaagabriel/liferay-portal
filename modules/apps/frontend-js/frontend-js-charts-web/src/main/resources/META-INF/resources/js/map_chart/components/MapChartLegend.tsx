/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {MapDatum} from '../types/MapDatum';
import MapChartLegendList from './MapChartLegendList';
import MapChartLegendScale from './MapChartLegendScale';
import MapChartLegendTable from './MapChartLegendTable';

interface MapChartLegendProps {
	activeIndex: number | null;
	bucketCount: number;
	colors: string[];
	data: MapDatum[];
	legend: 'list' | 'none' | 'scale' | 'table';
	onFocus: (index: number) => void;
	onHover: (index: number) => void;
	onHoverEnd: () => void;
	scheme: 'blue' | 'categorical';
	titleId: string;
	total: number;
}

export default function MapChartLegend({
	bucketCount,
	legend,
	scheme,
	titleId,
	...props
}: MapChartLegendProps) {
	if (legend === 'list') {
		return <MapChartLegendList {...props} />;
	}

	if (legend === 'table') {
		return <MapChartLegendTable {...props} titleId={titleId} />;
	}

	if (legend === 'scale') {
		return (
			<MapChartLegendScale bucketCount={bucketCount} scheme={scheme} />
		);
	}

	return null;
}
