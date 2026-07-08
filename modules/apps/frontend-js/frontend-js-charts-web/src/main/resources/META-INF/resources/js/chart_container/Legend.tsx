/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useCallback, useMemo} from 'react';

import ChartLegend from '../chart_legend/ChartLegend';
import {useChartContainer} from './ChartContainerContext';

import type {ChartLegendItem, ChartLegendLayout} from '../chart_legend/types';

interface Props {
	layout: ChartLegendLayout;

	/**
	 * The id of the chart's visible title, used for the `table` layout's
	 * `aria-labelledby`. When the composing consumer has no shared title id to
	 * pass (Legend is a `ChartPlot` sibling, not a child, so it cannot read
	 * `ChartPlot`'s own generated title id), the table renders without
	 * `aria-labelledby` rather than pointing at an id nothing carries.
	 * Sharing a title id between `ChartPlot` and `Legend` through context is a
	 * deliberate follow-up, not built here.
	 */
	titleId?: string;
}

function renderSwatch(color: string) {
	return (
		<span
			aria-hidden="true"
			className="charts-legend__swatch"
			style={{background: color}}
		/>
	);
}

/**
 * The composable Legend: an HTML sibling of `ChartPlot` (not an svg child),
 * reading the registered series metadata straight from context instead of
 * being prop-drilled a series list. Delegates all rendering to the shared
 * `chart_legend/ChartLegend`, so the `list`/`table` layouts and their
 * active/hover styling stay in one place across every chart.
 *
 * No cross-series hover/activate highlighting yet: the container currently
 * tracks only a single keyboard-focus tab stop (`focus`/`setFocus`), not a
 * hovered/active series concept, so `onActivate`/`onDeactivate`/`onSelect`
 * are no-ops and every item renders `active: false`.
 */
export default function Legend({layout, titleId}: Props) {
	const {series} = useChartContainer();

	const noop = useCallback(() => {}, []);

	const items = useMemo<ChartLegendItem[]>(
		() =>
			series.map((meta, index) => ({
				active: false,
				id: index,
				label: meta.label,
				sortValue: index,
				visual: renderSwatch(meta.color),
			})),
		[series]
	);

	if (layout === 'none') {
		return null;
	}

	return (
		<ChartLegend
			columns={[]}
			items={items}
			layout={layout}
			onActivate={noop}
			onDeactivate={noop}
			onSelect={noop}
			titleId={titleId}
		/>
	);
}
