/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useMemo, useState} from 'react';

import ChartState from '../chart_state_wrapper/ChartState';
import {
	ChartActiveDatum,
	ChartContainerContext,
	ChartContainerContextValue,
	ChartContainerDims,
	ChartSeriesExtent,
	ChartSeriesFocus,
	ChartSeriesMeta,
} from './ChartContainerContext';
import {getSymmetricChartScale} from './plot/scale';

import type {ChartStateProps} from '../chart_state_wrapper/ChartState';
import type {ChartAxisConfig, ChartScheme} from './types';

const DEFAULT_DIMS: ChartContainerDims = {
	height: 320,
	padding: {bottom: 32, left: 48, right: 24, top: 16},
	width: 640,
};

const DEFAULT_Y_TICK_COUNT = 5;

const DEFAULT_Y_AXIS: ChartAxisConfig = {
	tickCount: DEFAULT_Y_TICK_COUNT,
	type: 'numeric',
};

const EMPTY_EXTENT: ChartSeriesExtent = {max: 0, min: 0};

const REDUCED_MOTION_BODY_CLASS = 'c-prefers-reduced-motion';

/**
 * Reads the portal's reduced-motion body class once (no live subscription):
 * good enough for a PoC, since the class is set before React mounts and this
 * container never needs to react to a mid-session toggle.
 */
function isReducedMotionRequested(): boolean {
	if (typeof document === 'undefined') {
		return false;
	}

	return document.body.classList.contains(REDUCED_MOTION_BODY_CLASS);
}

export interface ChartContainerProps<T> extends ChartStateProps {

	/** Reveal-stagger opt-out; suppressed regardless when reduced-motion is active. */
	animated?: boolean;
	categories?: string[];
	children: React.ReactNode;
	data: readonly T[];
	dims?: Partial<ChartContainerDims>;
	scheme?: ChartScheme;
	xAxis?: ChartAxisConfig;
	yAxis?: ChartAxisConfig;
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
	animated = true,
	categories = [],
	children,
	data,
	dims: dimsProp,
	emptyStateMessage,
	error,
	fallbackError,
	loading,
	scheme = 'blue',
	xAxis: xAxisProp,
	yAxis: yAxisProp,
}: ChartContainerProps<T>) {
	const [seriesById, setSeriesById] = useState<Map<string, ChartSeriesMeta>>(
		() => new Map()
	);
	const [focus, setFocus] = useState<ChartSeriesFocus | null>(null);
	const [active, setActive] = useState<ChartActiveDatum | null>(null);
	const [reducedMotion] = useState<boolean>(isReducedMotionRequested);

	const animatedEffective = animated && !reducedMotion;

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

	const xAxis = useMemo<ChartAxisConfig>(
		() =>
			xAxisProp ?? {
				categoryCount: categories.length,
				type: 'categorical',
			},
		[xAxisProp, categories.length]
	);

	const yAxis = useMemo<ChartAxisConfig>(
		() => yAxisProp ?? DEFAULT_Y_AXIS,
		[yAxisProp]
	);

	const scale = useMemo(
		() =>
			getSymmetricChartScale({
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
			active,
			animated: animatedEffective,
			categories,
			data,
			dims,
			focus,
			registerSeries,
			scale,
			scheme,
			series,
			setActive,
			setFocus,
			xAxis,
			yAxis,
		}),
		[
			active,
			animatedEffective,
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
