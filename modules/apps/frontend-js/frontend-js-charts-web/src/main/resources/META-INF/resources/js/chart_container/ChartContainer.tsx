/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useMemo, useState} from 'react';

import ChartState from '../chart_state_wrapper/ChartState';
import {
	ChartContainerContext,
	ChartContainerContextValue,
	ChartContainerDims,
	ChartSeriesExtent,
	ChartSeriesFocus,
	ChartSeriesMeta,
} from './ChartContainerContext';
import {getChartScale} from './plot/scale';

import type {ChartStateProps} from '../chart_state_wrapper/ChartState';
import type {
	ChartCategoricalAxisConfig,
	ChartNumericAxisConfig,
	ChartScheme,
} from './types';

const DEFAULT_DIMS: ChartContainerDims = {
	height: 320,
	padding: {bottom: 32, left: 48, right: 24, top: 16},
	width: 640,
};

const EMPTY_EXTENT: ChartSeriesExtent = {max: 0, min: 0};

export interface ChartContainerProps<T> extends ChartStateProps {
	categories?: string[];
	children: React.ReactNode;
	data: readonly T[];
	dims?: Partial<ChartContainerDims>;
	scheme?: ChartScheme;
	xAxis: ChartCategoricalAxisConfig;
	yAxis: ChartNumericAxisConfig;
}

function unifyExtents(
	seriesById: Map<string, ChartSeriesMeta>
): ChartSeriesExtent {
	const extents = Array.from(seriesById.values()).map((meta) => meta.extent);

	if (!extents.length) {
		return EMPTY_EXTENT;
	}

	return {
		max: Math.max(...extents.map((extent) => extent.max)),
		min: Math.min(...extents.map((extent) => extent.min)),
	};
}

export default function ChartContainer<T>({
	categories = [],
	children,
	data,
	dims: dimsProp,
	emptyStateMessage,
	error,
	fallbackError,
	loading,
	scheme = 'blue',
	xAxis,
	yAxis,
}: ChartContainerProps<T>) {
	const [seriesById, setSeriesById] = useState<Map<string, ChartSeriesMeta>>(
		() => new Map()
	);
	const [focus, setFocus] = useState<ChartSeriesFocus | null>(null);

	const dims = useMemo<ChartContainerDims>(
		() => ({
			height: dimsProp?.height ?? DEFAULT_DIMS.height,
			padding: {...DEFAULT_DIMS.padding, ...dimsProp?.padding},
			width: dimsProp?.width ?? DEFAULT_DIMS.width,
		}),
		[dimsProp]
	);

	const valueExtent = useMemo(() => unifyExtents(seriesById), [seriesById]);

	const series = useMemo(() => Array.from(seriesById.values()), [seriesById]);

	const scale = useMemo(
		() =>
			getChartScale({
				height: dims.height,
				padding: dims.padding,
				valueMax: valueExtent.max,
				valueMin: valueExtent.min,
				width: dims.width,
				xAxis,
				yAxis,
			}),
		[dims, valueExtent, xAxis, yAxis]
	);

	const registerSeries = useCallback((meta: ChartSeriesMeta) => {
		setSeriesById((current) => new Map(current).set(meta.id, meta));

		return () => {
			setSeriesById((current) => {
				const next = new Map(current);

				next.delete(meta.id);

				return next;
			});
		};
	}, []);

	const value = useMemo<ChartContainerContextValue<T>>(
		() => ({
			categories,
			data,
			dims,
			focus,
			registerSeries,
			scale,
			scheme,
			series,
			setFocus,
			xAxis,
			yAxis,
		}),
		[
			categories,
			data,
			dims,
			focus,
			registerSeries,
			scale,
			scheme,
			series,
			xAxis,
			yAxis,
		]
	);

	return (
		<ChartState
			empty={!data.length}
			emptyStateMessage={emptyStateMessage}
			error={error}
			fallbackError={fallbackError}
			height={dims.height}
			loading={loading}
			width={dims.width}
		>
			<ChartContainerContext.Provider
				value={value as ChartContainerContextValue<unknown>}
			>
				{children}
			</ChartContainerContext.Provider>
		</ChartState>
	);
}
