/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import type {ChartScheme} from '../chart_container/types';
import type {ChartLegendLayout} from '../chart_legend/types';

export interface BarDatum {

	/** Optional descriptive text read by screen readers. Defaults to `${label}: ${value}`. */
	description?: string;
	label: string;
	value: number;
}

export interface BarChartProps {

	/** Enable bar reveal animations (default `true`). */
	animated?: boolean;

	/** Optional class name for the root `<figure>`. */
	className?: string;

	data: BarDatum[];

	/** Optional accessible long description for the chart. */
	description?: string;

	/** Height of the SVG viewport. */
	height?: number;

	/** Legend layout. Default `none`. */
	legend?: ChartLegendLayout;

	/** Layout direction. `vertical` is the default (bars rise upward). */
	orientation?: 'horizontal' | 'vertical';

	/** Round the bar (and matching track) into a pill. */
	rounded?: boolean;

	/** Color scheme. Default `blue`. */
	scheme?: ChartScheme;

	/**
	 * Bar thickness preset. `default` bars fill ~60% of their band; `inline`
	 * flattens every bar to 8px (the progress-bar row).
	 */
	size?: 'default' | 'inline';

	/** Accessible name for the chart as a whole. */
	title: string;

	/** Show a light-gray track behind each bar spanning the full plot. */
	track?: boolean;

	/** Width of the SVG viewport. */
	width?: number;
}
