/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

// eslint-disable-next-line @liferay/portal/no-cross-module-deep-import
import {checkAccessibility} from '@liferay/layout-js-components-web/test/__lib__/index';

import '@testing-library/jest-dom';
import {fireEvent, render, screen} from '@testing-library/react';
import {openToast} from 'frontend-js-components-web';
import React from 'react';

import FrontendTokenSet from '../../src/main/resources/META-INF/resources/js/style-book-editor/FrontendTokenSet';
import {StyleBookEditorContextProvider} from '../../src/main/resources/META-INF/resources/js/style-book-editor/contexts/StyleBookEditorContext';
import saveDraft from '../../src/main/resources/META-INF/resources/js/style-book-editor/saveDraft';

jest.mock('frontend-js-components-web', () => ({
	...jest.requireActual('frontend-js-components-web'),
	openToast: jest.fn(),
}));

jest.mock(
	'../../src/main/resources/META-INF/resources/js/style-book-editor/saveDraft',
	() => jest.fn(() => Promise.resolve())
);

const CUSTOM_TOKEN = {
	custom: true,
	defaultValue: '#fff',
	label: 'Custom Token',
	mappings: [{type: 'cssVariable', value: 'custom-token'}],
	name: 'theme:customToken',
	tokenDefinitionId: 'custom',
	type: 'color',
};

const THEME_TOKEN = {
	defaultValue: '#000',
	label: 'Theme Token',
	mappings: [{type: 'cssVariable', value: 'theme-token'}],
	name: 'theme:themeToken',
	tokenDefinitionId: 'theme',
	type: 'color',
};

const TOKEN_WITHOUT_CSS_VARIABLE_MAPPING = {
	defaultValue: '#000',
	label: 'Unmapped Token',
	mappings: [],
	name: 'theme:unmappedToken',
	tokenDefinitionId: 'theme',
	type: 'color',
};

const renderFrontendTokenSet = (frontendTokens) => {
	return render(
		<StyleBookEditorContextProvider
			initialState={{
				frontendTokensValues: {},
			}}
		>
			<FrontendTokenSet
				frontendTokens={frontendTokens}
				label="Set 1"
				open
				tokenValues={{}}
			/>
		</StyleBookEditorContextProvider>
	);
};

describe('FrontendTokenSet', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('shows the custom token marker for a token marked as custom', () => {
		renderFrontendTokenSet([CUSTOM_TOKEN]);

		expect(
			screen.getByRole('img', {name: 'style-book-custom-token'})
		).toBeInTheDocument();
	});

	it('does not show the custom token marker for a theme token', () => {
		renderFrontendTokenSet([THEME_TOKEN]);

		expect(
			screen.queryByRole('img', {name: 'style-book-custom-token'})
		).not.toBeInTheDocument();
	});

	it('does not throw and warns the user instead of saving a token without a cssVariable mapping', () => {
		renderFrontendTokenSet([TOKEN_WITHOUT_CSS_VARIABLE_MAPPING]);

		const input = screen.getByRole('textbox', {name: 'Unmapped Token'});

		expect(() => {
			fireEvent.change(input, {target: {value: '#123456'}});
			fireEvent.blur(input);
		}).not.toThrow();

		expect(saveDraft).not.toBeCalled();

		expect(openToast).toBeCalledWith({
			message: Liferay.Language.get(
				'unable-to-save-due-to-invalid-or-missing-configuration-values'
			),
			type: 'danger',
		});
	});

	it('has no accessibility violations', async () => {
		const {container} = renderFrontendTokenSet([CUSTOM_TOKEN, THEME_TOKEN]);

		await checkAccessibility({bestPractices: true, context: container});
	});
});
