/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {createContext, useContext} from 'react';

import type {ChartPlotPadding, ChartScale} from './plot/scale';
import type {
	ChartCategoricalAxisConfig,
	ChartNumericAxisConfig,
	ChartScheme,
} from './types';

export interface ChartContainerDims {
	height: number;
	padding: ChartPlotPadding;
	width: number;
}

/** The value-extent a registered series contributes to the unified Y domain. */
export interface ChartSeriesExtent {
	max: number;
	min: number;
}

/** The active tab stop: one registered series plus a position within it. */
export interface ChartSeriesFocus {
	index: number;
	seriesId: string;
}

/**
 * A registered series' identity: what the Legend needs to list it (label,
 * color) plus the value-extent it contributes to the unified Y domain.
 */
export interface ChartSeriesMeta {
	color: string;
	extent: ChartSeriesExtent;
	id: string;
	label: string;
}

export interface ChartContainerContextValue<T> {
	categories: string[];
	data: readonly T[];
	dims: ChartContainerDims;
	focus: ChartSeriesFocus | null;

	/**
	 * Registers a series' identity and value-extent so the container can
	 * derive the unified Y domain and list it in the Legend, and returns the
	 * unregister callback for the series' cleanup.
	 */
	registerSeries: (meta: ChartSeriesMeta) => () => void;
	scale: ChartScale;
	scheme: ChartScheme;
	series: ChartSeriesMeta[];
	setFocus: (focus: ChartSeriesFocus | null) => void;
	xAxis: ChartCategoricalAxisConfig;
	yAxis: ChartNumericAxisConfig;
}

export const ChartContainerContext =
	createContext<ChartContainerContextValue<unknown> | null>(null);

export function useChartContainer<T>(): ChartContainerContextValue<T> {
	const context = useContext(ChartContainerContext);

	if (!context) {
		throw new Error(
			'useChartContainer must be called within a ChartContainer'
		);
	}

	return context as ChartContainerContextValue<T>;
}
