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

interface Props<T> extends ChartStateProps {
	children: React.ReactNode;
	data: readonly T[];
	dims?: Partial<ChartContainerDims>;
	scheme?: ChartScheme;
	xAxis: ChartCategoricalAxisConfig;
	yAxis: ChartNumericAxisConfig;
}

function unifyExtents(
	extentsById: Record<string, ChartSeriesExtent>
): ChartSeriesExtent {
	const extents = Object.values(extentsById);

	if (!extents.length) {
		return EMPTY_EXTENT;
	}

	return {
		max: Math.max(...extents.map((extent) => extent.max)),
		min: Math.min(...extents.map((extent) => extent.min)),
	};
}

export default function ChartContainer<T>({
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
}: Props<T>) {
	const [seriesExtents, setSeriesExtents] = useState<
		Record<string, ChartSeriesExtent>
	>({});
	const [focus, setFocus] = useState<ChartSeriesFocus | null>(null);

	const dims = useMemo<ChartContainerDims>(
		() => ({
			height: dimsProp?.height ?? DEFAULT_DIMS.height,
			padding: {...DEFAULT_DIMS.padding, ...dimsProp?.padding},
			width: dimsProp?.width ?? DEFAULT_DIMS.width,
		}),
		[dimsProp]
	);

	const valueExtent = useMemo(
		() => unifyExtents(seriesExtents),
		[seriesExtents]
	);

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

	const registerSeries = useCallback(
		(id: string, extent: ChartSeriesExtent) => {
			setSeriesExtents((current) => ({...current, [id]: extent}));

			return () => {
				setSeriesExtents((current) => {
					const next = {...current};

					delete next[id];

					return next;
				});
			};
		},
		[]
	);

	const value = useMemo<ChartContainerContextValue<T>>(
		() => ({
			data,
			dims,
			focus,
			registerSeries,
			scale,
			scheme,
			setFocus,
			xAxis,
			yAxis,
		}),
		[data, dims, focus, registerSeries, scale, scheme, xAxis, yAxis]
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
