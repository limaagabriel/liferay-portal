/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {render, screen} from '@testing-library/react';
import React from 'react';

import '@testing-library/jest-dom';

// eslint-disable-next-line @liferay/portal/no-cross-module-deep-import
import {checkAccessibility} from '@liferay/layout-js-components-web/test/__lib__/index';

import {BarChart} from '../../src/main/resources/META-INF/resources/js';

const DATA = [
	{label: 'Jan', value: 12},
	{label: 'Feb', value: 18},
	{label: 'Mar', value: 9},
];

describe('BarChart', () => {
	it('renders one accessible bar per datum', () => {
		render(<BarChart data={DATA} title="Monthly visits" />);

		const bars = screen.getAllByRole('img');

		// `BarChart` maps its data onto one composable `BarSeries`, whose
		// per-bar `aria-label` is `${seriesLabel}, ${category}: ${value}`; the
		// facade uses the chart `title` as that series label.

		expect(bars).toHaveLength(DATA.length);
		expect(bars[0]).toHaveAttribute(
			'aria-label',
			'Monthly visits, Jan: 12'
		);
		expect(bars[1]).toHaveAttribute(
			'aria-label',
			'Monthly visits, Feb: 18'
		);
	});

	it('exposes the title as the chart accessible name', () => {
		render(<BarChart data={DATA} title="Monthly visits" />);

		expect(screen.getByRole('figure')).toHaveAccessibleName(
			'Monthly visits'
		);
	});

	it('applies the orientation and size modifiers', () => {
		render(
			<BarChart
				data={DATA}
				orientation="horizontal"
				size="inline"
				title="Monthly visits"
			/>
		);

		const figure = screen.getByRole('figure');

		expect(figure).toHaveClass('charts-bar-chart--horizontal');
		expect(figure).toHaveClass('charts-bar-chart--size-inline');
	});

	it('omits the motion modifier when animated is false', () => {
		render(
			<BarChart animated={false} data={DATA} title="Monthly visits" />
		);

		expect(screen.getByRole('figure')).not.toHaveClass(
			'charts-bar-chart--motion'
		);
	});

	it('resolves one categorical color for the whole bar series', () => {

		// `BarChart` maps its data onto a single composable `BarSeries`, which
		// resolves one color for the whole series (`--charts-bar-color` on the
		// series `<g>`) rather than one hue per bar, so every bar now renders
		// the same categorical shade instead of a distinct one.

		const {container} = render(
			<BarChart data={DATA} scheme="categorical" title="Monthly visits" />
		);

		expect(container.querySelector('.charts-bar-series')).toHaveStyle({
			'--charts-bar-color':
				'var(--primary-l0, light-dark(#5791ff, #0f62ff))',
		});
	});

	it('renders a semantic detail table for legend="table"', () => {
		render(<BarChart data={DATA} legend="table" title="Monthly visits" />);

		// The shared `Legend` lists one row per registered series; since this
		// facade registers a single `BarSeries` for the whole chart, the table
		// has one row (named after the chart title) and no per-bar value/share
		// columns, unlike the old `BarChartLegend`.

		expect(screen.getAllByRole('columnheader')).toHaveLength(3);
		expect(screen.getByRole('table')).toBeInTheDocument();
		expect(
			screen.getByRole('rowheader', {name: 'Monthly visits'})
		).toBeInTheDocument();
	});

	it('has no accessibility violations', async () => {
		const {container} = render(
			<BarChart data={DATA} legend="list" title="Monthly visits" />
		);

		await checkAccessibility({bestPractices: true, context: container});
	});
});
