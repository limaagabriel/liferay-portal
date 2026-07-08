/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

/**
 * Color scheme shared by every chart.
 *
 * - `blue` (default): every series/bar uses a shade of `--primary`.
 * - `categorical`: each series/bar gets a distinct hue from the Clay chart
 *   palette via `getCategoricalColors(count)`.
 */
export type ChartScheme = 'blue' | 'categorical';

/**
 * Axis kind driving the coordinate scale.
 *
 * - `categorical`: a band scale, one band per discrete category.
 * - `numeric`: a continuous numeric domain with negative-value support.
 */
export type ChartAxisType = 'categorical' | 'numeric';

/** A categorical (band) axis: one band per category. */
export interface ChartCategoricalAxisConfig {
	categoryCount: number;
	type: 'categorical';
}

/** A numeric axis: a continuous domain with an approximate tick count. */
export interface ChartNumericAxisConfig {
	tickCount: number;
	type: 'numeric';
}

export type ChartAxisConfig =
	| ChartCategoricalAxisConfig
	| ChartNumericAxisConfig;
