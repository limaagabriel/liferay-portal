/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {createContext, useContext} from 'react';

import type {ChartPlotPadding, ChartSymmetricScale} from './plot/scale';
import type {ChartAxisConfig, ChartScheme} from './types';

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
 * The resolved datum behind the container's unified hover-or-focus signal:
 * which registered series/index it belongs to, its category/value, and the
 * projected position downstream chrome (Legend now, Tooltip in a later step)
 * anchors to.
 */
export interface ChartActiveDatum {
	category: string;
	index: number;

	/**
	 * The publishing series' own display name. Distinct from a per-bar
	 * `ChartSeriesMeta.label`, which `colorByCategory` bars overload with
	 * `category: value` for the Legend — so a tooltip must read the name from
	 * here, not from the matched series metadata.
	 */
	label: string;

	position: {x: number; y: number};
	seriesId: string;
	value: number;
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

	/** The unified hover-or-focus signal; additive to `focus`, never replaces it. */
	active: ChartActiveDatum | null;

	/**
	 * The resolved motion policy: the `animated` prop AND-ed with NOT
	 * reduced-motion (the `c-prefers-reduced-motion` body class, read once at
	 * mount). Series read this single flag instead of the raw prop plus
	 * their own reduced-motion detection.
	 */
	animated: boolean;
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
	scale: ChartSymmetricScale;
	scheme: ChartScheme;
	series: ChartSeriesMeta[];
	setActive: (active: ChartActiveDatum | null) => void;
	setFocus: (focus: ChartSeriesFocus | null) => void;
	xAxis: ChartAxisConfig;
	yAxis: ChartAxisConfig;
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
