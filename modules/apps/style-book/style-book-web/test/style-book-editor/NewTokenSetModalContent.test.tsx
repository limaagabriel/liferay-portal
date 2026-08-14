/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';

// eslint-disable-next-line @liferay/portal/no-cross-module-deep-import
import {checkAccessibility} from '@liferay/layout-js-components-web/test/__lib__/index';
import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';

import NewTokenSetModalContent from '../../src/main/resources/META-INF/resources/js/style-book-editor/NewTokenSetModalContent';

const NEW_TOKEN_SET_MODAL_PROPS = {
	closeModal: jest.fn(),
	existingTokenSetNames: ['Set 1', 'Set 2'],
	namespace: '_stylebook_',
	onSuccess: jest.fn(),
};

describe('NewTokenSetModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('renders the Name field', () => {
		render(<NewTokenSetModalContent {...NEW_TOKEN_SET_MODAL_PROPS} />);

		expect(screen.getByLabelText(/name/i)).toBeInTheDocument();
	});

	it('rejects an empty or whitespace-only name', async () => {
		render(<NewTokenSetModalContent {...NEW_TOKEN_SET_MODAL_PROPS} />);

		await userEvent.type(screen.getByLabelText(/name/i), '   ');
		await userEvent.click(screen.getByText('create-token-set'));

		expect(screen.getByText('this-field-is-required')).toBeInTheDocument();
		expect(NEW_TOKEN_SET_MODAL_PROPS.onSuccess).not.toHaveBeenCalled();
		expect(NEW_TOKEN_SET_MODAL_PROPS.closeModal).not.toHaveBeenCalled();
	});

	it('rejects a name that already exists', async () => {
		render(<NewTokenSetModalContent {...NEW_TOKEN_SET_MODAL_PROPS} />);

		await userEvent.type(screen.getByLabelText(/name/i), 'Set 1');
		await userEvent.click(screen.getByText('create-token-set'));

		expect(
			screen.getByText('a-token-set-with-that-name-already-exists')
		).toBeInTheDocument();
		expect(NEW_TOKEN_SET_MODAL_PROPS.onSuccess).not.toHaveBeenCalled();
		expect(NEW_TOKEN_SET_MODAL_PROPS.closeModal).not.toHaveBeenCalled();
	});

	it('passes the typed name through without camelCase derivation, then closes the modal', async () => {
		render(<NewTokenSetModalContent {...NEW_TOKEN_SET_MODAL_PROPS} />);

		await userEvent.type(screen.getByLabelText(/name/i), 'My Token Set');
		await userEvent.click(screen.getByText('create-token-set'));

		expect(NEW_TOKEN_SET_MODAL_PROPS.onSuccess).toHaveBeenCalledWith({
			name: 'My Token Set',
		});
		expect(NEW_TOKEN_SET_MODAL_PROPS.closeModal).toHaveBeenCalled();
	});

	it('has no accessibility violations', async () => {
		const {container} = render(
			<NewTokenSetModalContent {...NEW_TOKEN_SET_MODAL_PROPS} />
		);

		await checkAccessibility({bestPractices: true, context: container});
	});
});
