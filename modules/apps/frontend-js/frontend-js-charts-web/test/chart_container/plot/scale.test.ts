/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	getChartScale,
	niceTickStep,
} from '../../../src/main/resources/META-INF/resources/js/chart_container/plot/scale';

describe('getChartScale', () => {
	it('centers each category on its band instead of anchoring to the band edge', () => {
		const scale = getChartScale({
			height: 100,
			padding: {bottom: 0, left: 20, right: 0, top: 0},
			valueMax: 10,
			valueMin: 0,
			width: 220,
			xAxis: {categoryCount: 4, type: 'categorical'},
			yAxis: {tickCount: 5, type: 'numeric'},
		});

		const bandSize = scale.plot.width / 4;

		expect(scale.xForIndex(0)).toBeCloseTo(20 + bandSize / 2);
		expect(scale.xForIndex(0)).not.toBeCloseTo(20);
	});

	it('extends the y domain below zero when the data has negative values', () => {
		const scale = getChartScale({
			height: 100,
			padding: {bottom: 0, left: 0, right: 0, top: 0},
			valueMax: 10,
			valueMin: -5,
			width: 100,
			xAxis: {categoryCount: 3, type: 'categorical'},
			yAxis: {tickCount: 5, type: 'numeric'},
		});

		expect(scale.yDomain.min).toBeLessThan(0);
	});
});

describe('niceTickStep', () => {
	it('lands on a value from the {1, 2, 2.5, 5} x 10^k family', () => {
		expect(niceTickStep(100, 5)).toBe(20);
	});
});
