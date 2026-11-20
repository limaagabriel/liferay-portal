/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Page, expect} from '@playwright/test';

export async function openProductMenu(page: Page) {
	const productMenu = page
		.getByLabel('Product Menu', {exact: true})
		.or(page.getByTestId('sideNavigation'));

	const isOpen = await productMenu.evaluate(
		(element) =>
			element.classList.contains('open') ||
			element.classList.contains('c-slideout-show')
	);

	if (!isOpen) {
		const button = page
			.getByLabel('Open Product Menu')
			.or(page.getByTestId('sideNavigationToggler'));

		const pinButton = page.getByLabel('Pin Product Menu');

		await expect(async () => {

			// Hovering the toggle reveals a hidden unified sidebar over it, so
			// its pin button opens the sidebar instead

			if (!(await pinButton.isVisible())) {
				await button.hover({timeout: 2000});
			}

			if (await pinButton.isVisible()) {
				await pinButton.click({timeout: 2000});
			}
			else {
				await button.click({timeout: 2000});
			}

			await expect(productMenu).toHaveClass(/open|c-slideout-show/, {
				timeout: 2000,
			});
		}).toPass();
	}
}

export async function closeProductMenu(page: Page) {
	const productMenu = page
		.getByLabel('Product Menu', {exact: true})
		.or(page.getByTestId('sideNavigation'));

	const isClosed = async () =>
		await productMenu.evaluate(
			(element) =>
				!element.classList.contains('open') &&
				!element.classList.contains('c-slideout-show')
		);

	if (!(await isClosed())) {

		// A pinned unified sidebar hides the Control Menu toggle, as its
		// header holds the toggle then. Projects differ in the attribute
		// that getByTestId reads, so the header toggle is found by its own.

		const button = page
			.getByRole('tab', {name: 'Close Product Menu'})
			.or(page.locator('[data-qa-id="unifiedSidebarHeaderToggler"]'))
			.or(page.getByTestId('sideNavigationToggler'))
			.or(page.locator('[data-qa-id="productMenu"]'))
			.filter({visible: true});

		await expect(async () => {
			await button.click();

			// Give the menu time to close, as another click would reopen it

			await expect.poll(isClosed, {timeout: 2000}).toBeTruthy();
		}).toPass({
			timeout: 5000,
		});
	}
}
