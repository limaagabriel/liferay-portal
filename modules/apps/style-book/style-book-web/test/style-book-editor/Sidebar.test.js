/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {fireEvent, render, screen} from '@testing-library/react';
import React from 'react';

import Sidebar from '../../src/main/resources/META-INF/resources/js/style-book-editor/Sidebar';
import {SET_FRONTEND_TOKEN_DEFINITIONS} from '../../src/main/resources/META-INF/resources/js/style-book-editor/constants/actionTypes';
import {
	StyleBookEditorContextProvider,
	useDispatch,
} from '../../src/main/resources/META-INF/resources/js/style-book-editor/contexts/StyleBookEditorContext';
import saveDraft from '../../src/main/resources/META-INF/resources/js/style-book-editor/saveDraft';

jest.mock(
	'../../src/main/resources/META-INF/resources/js/style-book-editor/config',
	() => ({
		config: {
			customTokenDefinitionId: 'custom',
			sortFrontendTokenValues: (frontendTokensValues) =>
				Object.values(frontendTokensValues),
			themeFrontendTokenDefinitionId: 'theme',
			themeName: 'Classic',
		},
	})
);

jest.mock(
	'../../src/main/resources/META-INF/resources/js/style-book-editor/saveDraft',
	() => jest.fn(() => Promise.resolve())
);

global.Liferay = {
	Language: {
		get: jest.fn((key) => key),
	},
};

const FRONTEND_TOKEN_DEFINITIONS = [
	{
		frontendTokenCategories: [
			{
				frontendTokenSets: [
					{
						frontendTokens: [
							{
								defaultValue: '#000',
								label: 'Token 1',
								mappings: [
									{
										type: 'cssVariable',
										value: 'token-1',
									},
								],
								name: 'token1',
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
									{
										type: 'cssVariable',
										value: 'clay-token',
									},
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

const THEME_DEFINITION_WITH_TWO_CATEGORIES = {
	frontendTokenCategories: [
		{
			frontendTokenSets: [
				{
					frontendTokens: [
						{
							defaultValue: '#000',
							label: 'Token 1',
							mappings: [{type: 'cssVariable', value: 'token-1'}],
							name: 'token1',
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
		{
			frontendTokenSets: [
				{
					frontendTokens: [
						{
							defaultValue: '#111',
							label: 'Token 2',
							mappings: [{type: 'cssVariable', value: 'token-2'}],
							name: 'token2',
							type: 'color',
						},
					],
					label: 'Set 2',
					name: 'set2',
				},
			],
			label: 'Category 2',
			name: 'category2',
		},
	],
	id: 'theme',
	name: 'Theme Tokens',
};

const THEME_DEFINITION_WITH_NEW_TOKEN = {
	...THEME_DEFINITION_WITH_TWO_CATEGORIES,
	frontendTokenCategories: [
		THEME_DEFINITION_WITH_TWO_CATEGORIES.frontendTokenCategories[0],
		{
			frontendTokenSets: [
				{
					frontendTokens: [
						{
							defaultValue: '#111',
							label: 'Token 2',
							mappings: [{type: 'cssVariable', value: 'token-2'}],
							name: 'token2',
							type: 'color',
						},
						{
							defaultValue: '#222',
							label: 'New Token',
							mappings: [
								{type: 'cssVariable', value: 'new-token'},
							],
							name: 'newToken',
							type: 'color',
						},
					],
					label: 'Set 2',
					name: 'set2',
				},
			],
			label: 'Category 2',
			name: 'category2',
		},
	],
};

const THEME_DEFINITION_WITH_CUSTOM_TOKEN = {
	frontendTokenCategories: [
		{
			frontendTokenSets: [
				{
					frontendTokens: [
						{
							defaultValue: '#000',
							label: 'Token 1',
							mappings: [{type: 'cssVariable', value: 'token-1'}],
							name: 'token1',
							type: 'color',
						},
						{
							defaultValue: '#fff',
							label: 'Custom Token 1',
							mappings: [
								{
									type: 'cssVariable',
									value: 'custom-token-1',
								},
							],
							name: 'customToken1',
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
};

const CUSTOM_TOKEN_FRONTEND_TOKENS_VALUES = {
	'custom:customToken1': {
		cssVariableMapping: 'custom-token-1',
		tokenDefinitionId: 'custom',
		value: '#fff',
	},
};

const renderComponent = () => {
	render(
		<StyleBookEditorContextProvider
			initialState={{
				frontendTokenDefinitions: FRONTEND_TOKEN_DEFINITIONS,
				frontendTokensValues: {},
			}}
		>
			<Sidebar />
		</StyleBookEditorContextProvider>
	);
};

function DispatchFrontendTokenDefinitions({frontendTokenDefinitions}) {
	const dispatch = useDispatch();

	return (
		<button
			onClick={() =>
				dispatch({
					frontendTokenDefinitions,
					type: SET_FRONTEND_TOKEN_DEFINITIONS,
				})
			}
			type="button"
		>
			update definitions
		</button>
	);
}

describe('Sidebar', () => {
	beforeEach(() => {
		saveDraft.mockClear();
	});

	it("stamps a custom token's saved value with the custom definition id while keeping its themeId:name key", () => {
		render(
			<StyleBookEditorContextProvider
				initialState={{
					frontendTokenDefinitions: [
						THEME_DEFINITION_WITH_CUSTOM_TOKEN,
					],
					frontendTokensValues: CUSTOM_TOKEN_FRONTEND_TOKENS_VALUES,
				}}
			>
				<Sidebar />
			</StyleBookEditorContextProvider>
		);

		const input = screen.getByLabelText('Custom Token 1');

		fireEvent.change(input, {target: {value: '#123456'}});
		fireEvent.blur(input);

		expect(saveDraft).toBeCalledWith({
			...CUSTOM_TOKEN_FRONTEND_TOKENS_VALUES,
			'theme:customToken1': {
				cssVariableMapping: 'custom-token-1',
				name: undefined,
				tokenDefinitionId: 'custom',
				value: '#123456',
			},
		});
	});

	it("keeps a theme token's saved value stamped with the theme definition id", () => {
		render(
			<StyleBookEditorContextProvider
				initialState={{
					frontendTokenDefinitions: [
						THEME_DEFINITION_WITH_CUSTOM_TOKEN,
					],
					frontendTokensValues: CUSTOM_TOKEN_FRONTEND_TOKENS_VALUES,
				}}
			>
				<Sidebar />
			</StyleBookEditorContextProvider>
		);

		const input = screen.getByLabelText('Token 1');

		fireEvent.change(input, {target: {value: '#654321'}});
		fireEvent.blur(input);

		expect(saveDraft).toBeCalledWith({
			...CUSTOM_TOKEN_FRONTEND_TOKENS_VALUES,
			'theme:token1': {
				cssVariableMapping: 'token-1',
				name: undefined,
				tokenDefinitionId: 'theme',
				value: '#654321',
			},
		});
	});

	it('renders Sidebar with definition selector when multiple definitions are present', () => {
		renderComponent();

		expect(
			screen.getByText('frontend-token-definition-provided-by')
		).toBeInTheDocument();
		expect(screen.getAllByText('Classic')[0]).toBeInTheDocument();
	});

	it('switches between definitions using the dropdown', () => {
		renderComponent();

		const triggers = screen.getAllByText('Classic');
		fireEvent.click(triggers[0]);

		const clayOption = screen.getByText('Clay Tokens');
		fireEvent.click(clayOption);

		expect(screen.getAllByText('Clay Tokens')[0]).toBeInTheDocument();
		expect(screen.getAllByText('Clay Category')[0]).toBeInTheDocument();
		expect(screen.getByText('Clay Set')).toBeInTheDocument();
		expect(screen.getByText('Clay Token')).toBeInTheDocument();
	});

	it('resets selected category when switching definitions', () => {
		renderComponent();

		// Initially in Classic, Category 1 is selected

		expect(screen.getAllByText('Category 1')[0]).toBeInTheDocument();

		// Switch to Clay Tokens

		fireEvent.click(screen.getAllByText('Classic')[0]);
		fireEvent.click(screen.getByText('Clay Tokens'));

		// Should show Clay Category now

		expect(screen.getAllByText('Clay Category')[0]).toBeInTheDocument();
		expect(screen.queryByText('Category 1')).not.toBeInTheDocument();
	});

	it('keeps the selected category when frontend token definitions are updated', () => {
		render(
			<StyleBookEditorContextProvider
				initialState={{
					frontendTokenDefinitions: [
						THEME_DEFINITION_WITH_TWO_CATEGORIES,
					],
					frontendTokensValues: {},
				}}
			>
				<DispatchFrontendTokenDefinitions
					frontendTokenDefinitions={[THEME_DEFINITION_WITH_NEW_TOKEN]}
				/>

				<Sidebar />
			</StyleBookEditorContextProvider>
		);

		fireEvent.click(screen.getAllByText('Category 1')[0]);
		fireEvent.click(screen.getByText('Category 2'));

		expect(screen.getByText('Token 2')).toBeInTheDocument();

		fireEvent.click(screen.getByText('update definitions'));

		expect(screen.getByText('New Token')).toBeInTheDocument();
		expect(screen.queryByText('Token 1')).not.toBeInTheDocument();
	});
});
