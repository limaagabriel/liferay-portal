/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {getFrontendTokens} from '../../../src/main/resources/META-INF/resources/js/style-book-editor/utils/getFrontendTokens';

const frontendTokenDefinitions = [
	{
		frontendTokenCategories: [
			{
				frontendTokenSets: [
					{
						frontendTokens: [
							{
								defaultValue: '#000',
								label: 'Theme Token',
								mappings: [
									{type: 'cssVariable', value: 'theme-token'},
								],
								name: 'themeToken',
								type: 'color',
							},
						],
						label: 'Set 1',
						name: 'set1',
					},
				],
				label: 'Category 1',
				name: 'category1',
			},
		],
		id: 'theme',
		name: 'Theme Tokens',
	},
	{
		frontendTokenCategories: [
			{
				frontendTokenSets: [
					{
						frontendTokens: [
							{
								defaultValue: '#fff',
								label: 'Clay Token',
								mappings: [
									{type: 'cssVariable', value: 'clay-token'},
								],
								name: 'clayToken',
								type: 'color',
							},
						],
						label: 'Clay Set',
						name: 'claySet',
					},
				],
				label: 'Clay Category',
				name: 'clayCategory',
			},
		],
		id: 'clay',
		name: 'Clay Tokens',
	},
];

describe('getFrontendTokens', () => {
	it('namespaces every token by its definition id', () => {
		const frontendTokens = getFrontendTokens(
			frontendTokenDefinitions,
			'theme'
		);

		expect(frontendTokens['theme:themeToken']).toBeDefined();
		expect(frontendTokens['theme:themeToken'].value).toBe('#000');

		expect(frontendTokens['clay:clayToken']).toBeDefined();
		expect(frontendTokens['clay:clayToken'].value).toBe('#fff');
	});

	it('also keys the theme definition tokens by their bare name', () => {
		const frontendTokens = getFrontendTokens(
			frontendTokenDefinitions,
			'theme'
		);

		expect(frontendTokens.themeToken).toBeDefined();
		expect(frontendTokens.themeToken.name).toBe('themeToken');
		expect(frontendTokens.themeToken.value).toBe('#000');

		expect(frontendTokens.clayToken).toBeUndefined();
	});
});
