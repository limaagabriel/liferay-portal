/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';

// eslint-disable-next-line @liferay/portal/no-cross-module-deep-import
import {checkAccessibility} from '@liferay/layout-js-components-web/test/__lib__/index';
import {
	fireEvent,
	render,
	screen,
	waitFor,
	within,
} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {openModal} from 'frontend-js-components-web';
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

async function openNewTokenSetModal() {
	await userEvent.click(screen.getByRole('button', {name: 'new-token-set'}));

	const dialog = await screen.findByRole('dialog');

	await within(dialog).findByRole('heading', {name: 'new-token-set'});

	return dialog;
}

describe('NewTokenModalContent', () => {
	beforeEach(() => {
		jest.clearAllMocks();

		Liferay.Util.ns.mockImplementation((namespace, object) => object);
		Liferay.on.mockReturnValue({detach: jest.fn()});
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

	describe('the "+ New Token Set" trigger', () => {
		it('renders beside the Token Set picker', () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			expect(
				screen.getByRole('button', {name: 'new-token-set'})
			).toBeInTheDocument();
		});

		it('keeps the first modal mounted with its state intact', async () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			fireEvent.change(screen.getByLabelText(/token-name/), {
				target: {value: 'My Token'},
			});

			const dialog = await openNewTokenSetModal();

			expect(screen.getByLabelText(/token-name/)).toHaveValue('My Token');

			fireEvent.click(within(dialog).getByText('cancel'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});
		});

		it('appends and selects the new token set, closing only the second modal and keeping the first intact', async () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			fireEvent.change(screen.getByLabelText(/token-name/), {
				target: {value: 'My Token'},
			});

			const dialog = await openNewTokenSetModal();

			fireEvent.change(within(dialog).getByLabelText(/name/i), {
				target: {value: 'My Set'},
			});

			fireEvent.click(within(dialog).getByText('create-token-set'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});

			expect(screen.getByLabelText(/token-set/i)).toHaveTextContent(
				'My Set'
			);
			expect(screen.getByLabelText(/token-name/)).toHaveValue('My Token');
		});

		it('rides the new token set along on the token POST body', async () => {
			mockFetchJSON({frontendTokenDefinitions: []});

			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			const dialog = await openNewTokenSetModal();

			fireEvent.change(within(dialog).getByLabelText(/name/i), {
				target: {value: 'My Set'},
			});

			fireEvent.click(within(dialog).getByText('create-token-set'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});

			fireEvent.change(screen.getByLabelText(/token-name/), {
				target: {value: 'My Token'},
			});

			fireEvent.click(screen.getByText('create-token'));

			const [, {body}] = global.fetch.mock.calls[0];

			expect(Object.fromEntries(body.entries())).toMatchObject({
				tokenSetName: 'My Set',
			});
		});

		it('uses the pre-existing set when reselected after creating a provisional set', async () => {
			mockFetchJSON({frontendTokenDefinitions: []});

			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			const dialog = await openNewTokenSetModal();

			fireEvent.change(within(dialog).getByLabelText(/name/i), {
				target: {value: 'My Set'},
			});

			fireEvent.click(within(dialog).getByText('create-token-set'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});

			await userEvent.click(screen.getByLabelText(/token-set/i));
			await userEvent.click(screen.getByRole('option', {name: /set 2/i}));

			fireEvent.change(screen.getByLabelText(/token-name/), {
				target: {value: 'My Token'},
			});

			fireEvent.click(screen.getByText('create-token'));

			const [, {body}] = global.fetch.mock.calls[0];

			expect(Object.fromEntries(body.entries())).toMatchObject({
				tokenSetName: 'set2',
			});
		});

		it('rejects a name that collides with a server-provided set', async () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			const dialog = await openNewTokenSetModal();

			fireEvent.change(within(dialog).getByLabelText(/name/i), {
				target: {value: 'set1'},
			});

			fireEvent.click(within(dialog).getByText('create-token-set'));

			expect(
				within(dialog).getByText(
					'a-token-set-with-that-name-already-exists'
				)
			).toBeInTheDocument();
			expect(screen.getByRole('dialog')).toBeInTheDocument();

			fireEvent.click(within(dialog).getByText('cancel'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});
		});

		it('rejects a name that collides with a just-created provisional set', async () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			const firstDialog = await openNewTokenSetModal();

			fireEvent.change(within(firstDialog).getByLabelText(/name/i), {
				target: {value: 'My Set'},
			});

			fireEvent.click(within(firstDialog).getByText('create-token-set'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});

			const secondDialog = await openNewTokenSetModal();

			fireEvent.change(within(secondDialog).getByLabelText(/name/i), {
				target: {value: 'My Set'},
			});

			fireEvent.click(within(secondDialog).getByText('create-token-set'));

			expect(
				within(secondDialog).getByText(
					'a-token-set-with-that-name-already-exists'
				)
			).toBeInTheDocument();

			fireEvent.click(within(secondDialog).getByText('cancel'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});

			await userEvent.click(screen.getByLabelText(/token-set/i));

			expect(
				screen.getAllByRole('option', {name: 'My Set'})
			).toHaveLength(1);
		});

		it('has no accessibility violations in the second modal', async () => {
			render(<NewTokenModalContent {...NEW_TOKEN_MODAL_PROPS} />);

			const dialog = await openNewTokenSetModal();

			await checkAccessibility({bestPractices: true, context: dialog});

			fireEvent.click(within(dialog).getByText('cancel'));

			await waitFor(() => {
				expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
			});
		});

		describe('when the first modal is also opened through the real modal stack', () => {
			function openFirstModal(props = NEW_TOKEN_MODAL_PROPS) {
				openModal({
					contentComponent: ({closeModal}) => (
						<NewTokenModalContent
							{...props}
							closeModal={closeModal}
						/>
					),
				});
			}

			it('lets Esc close only the topmost modal and returns focus to the trigger', async () => {
				openFirstModal();

				const firstDialog = await screen.findByRole('dialog');

				await within(firstDialog).findByLabelText(/token-name/);

				fireEvent.change(
					within(firstDialog).getByLabelText(/token-name/),
					{
						target: {value: 'My Token'},
					}
				);

				const trigger = within(firstDialog).getByRole('button', {
					name: 'new-token-set',
				});

				await userEvent.click(trigger);

				await waitFor(() => {
					expect(screen.getAllByRole('dialog')).toHaveLength(2);
				});

				await screen.findByRole('heading', {name: 'new-token-set'});

				fireEvent.keyDown(document, {key: 'Escape'});

				await waitFor(() => {
					expect(screen.getAllByRole('dialog')).toHaveLength(1);
				});

				expect(
					within(screen.getByRole('dialog')).getByLabelText(
						/token-name/
					)
				).toHaveValue('My Token');

				await waitFor(() => {
					expect(trigger).toHaveFocus();
				});
			});
		});
	});
});
