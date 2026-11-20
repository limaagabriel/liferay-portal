/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {act, render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';

import '@testing-library/jest-dom';

import UnifiedSidebarToggler from '../../src/main/resources/META-INF/resources/unified_sidebar/UnifiedSidebarToggler';

function changeState(visible: boolean) {
	const [, handleStateChanged] = (Liferay.on as jest.Mock).mock.calls
		.filter(([eventName]) => eventName === 'stateChanged')
		.pop();

	act(() => {
		handleStateChanged({visible});
	});
}

function renderNavigation({pinned}: {pinned: boolean}) {
	const navigation = document.createElement('div');

	navigation.id = 'navigationId';

	if (pinned) {
		navigation.classList.add('c-slideout-show');
	}

	const headerToggler = document.createElement('button');

	headerToggler.setAttribute('data-qa-id', 'unifiedSidebarHeaderToggler');

	navigation.appendChild(headerToggler);

	document.body.appendChild(navigation);

	return {headerToggler, navigation};
}

function renderToggler(visible: boolean) {
	render(
		<UnifiedSidebarToggler
			navigationId="navigationId"
			stateChangedEventName="stateChanged"
			stateRequestedEventName="stateRequested"
			visible={visible}
		/>
	);

	return screen.getByRole('tab', {hidden: true});
}

describe('UnifiedSidebarToggler', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	afterEach(() => {
		document.getElementById('navigationId')?.remove();
	});

	it('controls the given navigation', () => {
		const toggler = renderToggler(false);

		expect(toggler).toHaveAttribute('aria-controls', 'navigationId');
		expect(toggler).toHaveAttribute('aria-expanded', 'false');
	});

	it('follows a navigation that rendered before it', () => {
		renderNavigation({pinned: false});

		expect(renderToggler(true)).toHaveAttribute('aria-expanded', 'false');
	});

	it('requests the opposite state on click', async () => {
		await userEvent.click(renderToggler(false));

		expect(Liferay.fire).toHaveBeenCalledWith('stateRequested', {
			visible: true,
		});
	});

	it('is hidden while the navigation is pinned', () => {
		const toggler = renderToggler(true);

		expect(toggler).not.toBeVisible();

		changeState(false);

		expect(toggler).toBeVisible();

		changeState(true);

		expect(toggler).not.toBeVisible();
	});

	describe('focus', () => {
		it('moves to the header toggler when pinning from the toggler', async () => {
			const {headerToggler} = renderNavigation({pinned: false});

			const toggler = renderToggler(false);

			await userEvent.click(toggler);

			expect(toggler).toHaveFocus();

			changeState(true);

			await waitFor(() => expect(headerToggler).toHaveFocus());
		});

		it('waits for the header toggler to render when pinning', async () => {
			const {headerToggler, navigation} = renderNavigation({
				pinned: false,
			});

			headerToggler.remove();

			const toggler = renderToggler(false);

			toggler.focus();

			changeState(true);

			setTimeout(() => navigation.appendChild(headerToggler), 50);

			await waitFor(() => expect(headerToggler).toHaveFocus());
		});

		it('stays where it is when pinning from elsewhere', async () => {
			const {headerToggler} = renderNavigation({pinned: false});

			const button = document.createElement('button');

			document.body.appendChild(button);

			try {
				renderToggler(false);

				button.focus();

				changeState(true);

				await new Promise((resolve) => setTimeout(resolve, 100));

				expect(button).toHaveFocus();
				expect(headerToggler).not.toHaveFocus();
			}
			finally {
				button.remove();
			}
		});

		it('returns to the toggler when unpinning from the header toggler', async () => {
			const {headerToggler} = renderNavigation({pinned: false});

			const toggler = renderToggler(false);

			toggler.focus();

			changeState(true);

			await waitFor(() => expect(headerToggler).toHaveFocus());

			changeState(false);

			await waitFor(() => expect(toggler).toHaveFocus());
		});

		it('returns to the toggler when unpinning after a mouse pin', async () => {
			const {headerToggler} = renderNavigation({pinned: false});

			const toggler = renderToggler(false);

			// The sidebar was revealed, so the pin came from its own toggle

			headerToggler.focus();

			changeState(true);

			expect(headerToggler).toHaveFocus();

			changeState(false);

			await waitFor(() => expect(toggler).toHaveFocus());
		});

		it('returns to the toggler when unpinning lost the focus to the page', async () => {
			const {headerToggler} = renderNavigation({pinned: true});

			const toggler = renderToggler(true);

			expect(headerToggler).not.toHaveFocus();
			expect(document.body).toHaveFocus();

			changeState(false);

			await waitFor(() => expect(toggler).toHaveFocus());
		});

		it('stays where it is when unpinning from outside the navigation', async () => {
			renderNavigation({pinned: true});

			const input = document.createElement('input');

			document.body.appendChild(input);

			try {
				const toggler = renderToggler(true);

				input.focus();

				changeState(false);

				await new Promise((resolve) => setTimeout(resolve, 100));

				expect(input).toHaveFocus();
				expect(toggler).not.toHaveFocus();
			}
			finally {
				input.remove();
			}
		});

		it('is not taken when the navigation settles on load', async () => {
			renderNavigation({pinned: false});

			const toggler = renderToggler(true);

			await new Promise((resolve) => setTimeout(resolve, 100));

			expect(toggler).toBeVisible();
			expect(toggler).not.toHaveFocus();
		});
	});
});
