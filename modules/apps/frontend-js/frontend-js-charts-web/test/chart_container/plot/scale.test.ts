/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	getSymmetricChartScale,
	niceTickStep,
} from '../../../src/main/resources/META-INF/resources/js/chart_container/plot/scale';

describe('niceTickStep', () => {
	it('lands on a value from the {1, 2, 2.5, 5} x 10^k family', () => {
		expect(niceTickStep(100, 5)).toBe(20);
	});
});

describe('getSymmetricChartScale', () => {
	const verticalOptions = {
		height: 100,
		padding: {bottom: 0, left: 20, right: 0, top: 0},
		valueMax: 10,
		valueMin: 0,
		width: 220,
		xAxis: {categoryCount: 4, type: 'categorical'} as const,
		yAxis: {tickCount: 5, type: 'numeric'} as const,
	};

	it('centers each category on its band instead of anchoring to the band edge', () => {
		const scale = getSymmetricChartScale(verticalOptions);
		const bandSize = scale.plot.width / 4;

		expect(scale.xPosition(0)).toBeCloseTo(20 + bandSize / 2);
		expect(scale.xPosition(0)).not.toBeCloseTo(20);
	});

	it('extends the y domain below zero when the data has negative values', () => {
		const scale = getSymmetricChartScale({
			...verticalOptions,
			padding: {bottom: 0, left: 0, right: 0, top: 0},
			valueMin: -5,
			width: 100,
			xAxis: {categoryCount: 3, type: 'categorical'},
		});

		expect(scale.ticks[0].value).toBeLessThan(0);
	});

	it('supports a swapped configuration with a numeric x axis and categorical y axis', () => {
		const horizontalOptions = {
			height: 220,
			padding: {bottom: 0, left: 0, right: 0, top: 20},
			valueMax: 10,
			valueMin: -5,
			width: 100,
			xAxis: {tickCount: 5, type: 'numeric'} as const,
			yAxis: {categoryCount: 4, type: 'categorical'} as const,
		};

		const symmetricScale = getSymmetricChartScale(horizontalOptions);
		const yBandSize =
			symmetricScale.plot.height / horizontalOptions.yAxis.categoryCount;

		expect(symmetricScale.bandSize).toBeCloseTo(yBandSize);
		expect(symmetricScale.categoryPositions[0]).toBeCloseTo(
			horizontalOptions.padding.top + yBandSize / 2
		);
		expect(symmetricScale.xPosition(10)).toBeGreaterThan(
			symmetricScale.xPosition(-5)
		);

		const step = niceTickStep(
			Math.max(
				horizontalOptions.valueMax - horizontalOptions.valueMin,
				horizontalOptions.valueMax,
				1
			),
			horizontalOptions.xAxis.tickCount
		);
		const domainMin =
			horizontalOptions.valueMin >= 0
				? 0
				: Math.floor(horizontalOptions.valueMin / step) * step;
		const domainMax = Math.max(
			domainMin + step,
			Math.ceil(horizontalOptions.valueMax / step) * step
		);
		const domainRange = domainMax - domainMin;
		const expectedBaseline =
			horizontalOptions.padding.left +
			symmetricScale.plot.width * ((0 - domainMin) / domainRange);

		expect(symmetricScale.xPosition(0)).toBeCloseTo(expectedBaseline);
	});
});
