/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';

// eslint-disable-next-line @liferay/portal/no-cross-module-deep-import
import {checkAccessibility} from '@liferay/layout-js-components-web/test/__lib__/index';
import {fireEvent, render, screen} from '@testing-library/react';
import React from 'react';

import NewTokenModalContent from '../../src/main/resources/META-INF/resources/js/style-book-editor/NewTokenModalContent';

const NEW_TOKEN_MODAL_PROPS = {
	addFrontendTokenURL: '/add-frontend-token',
	categoryName: 'category1',
	closeModal: jest.fn(),
	namespace: '_stylebook_',
	onSuccess: jest.fn(),
	styleBookEntryId: 1,
	tokenSets: [
		{label: 'Set 1', name: 'set1'},
		{label: 'Set 2', name: 'set2'},
	],
};

function mockFetchJSON(json) {
	global.fetch.mockReturnValue(
		Promise.resolve({json: () => Promise.resolve(json)})
	);
}

describe('NewTokenModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();

		Liferay.Util.ns.mockImplementation((namespace, object) => object);
	});

	afterEach(() => {
		Liferay.Util.ns.mockImplementation(() => ({}));
	});

	it('posts the expected fields to the addFrontendTokenURL', () => {
		mockFetchJSON({frontendTokenDefinitions: []});

		render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

		fireEvent.change(screen.getByLabelText(/token-name/), {
			target: {value: 'My Token'},
		});

		fireEvent.click(screen.getByText('create-token'));

		const [url, {body}] = global.fetch.mock.calls[0];

		expect(url).toBe('/add-frontend-token');
		expect(Object.fromEntries(body.entries())).toEqual({
			categoryName: 'category1',
			description: '',
			editorType: 'Default',
			label: 'My Token',
			styleBookEntryId: '1',
			tokenSetName: 'set1',
			value: '',
		});
	});

	it('displays the server rejection of an invalid label in the form', async () => {
		mockFetchJSON({error: 'Please enter a valid name.'});

		render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

		fireEvent.change(screen.getByLabelText(/token-name/), {
			target: {value: ':::'},
		});

		fireEvent.click(screen.getByText('create-token'));

		expect(
			await screen.findByText('Please enter a valid name.')
		).toBeInTheDocument();
	});

	it('has no accessibility violations', async () => {
		const {container} = render(
			<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />
		);

		await checkAccessibility({bestPractices: true, context: container});
	});
});
