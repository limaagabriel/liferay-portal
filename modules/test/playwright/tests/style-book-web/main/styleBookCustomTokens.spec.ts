/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {isolatedSiteTest} from '../../../fixtures/isolatedSiteTest';
import {loginTest} from '../../../fixtures/loginTest';
import {styleBookPageTest} from '../../../fixtures/styleBookPageTest';
import fillAndClickOutside from '../../../utils/fillAndClickOutside';
import getRandomString from '../../../utils/getRandomString';

const test = mergeTests(isolatedSiteTest, loginTest(), styleBookPageTest);

test.beforeEach(async ({site, styleBooksPage}) => {
	await styleBooksPage.goto(site.friendlyUrlPath);

	await styleBooksPage.create(getRandomString());
});

test(
	'Creates a custom frontend token and sees it rendered in the sidebar',
	{tag: '@LPD-83061'},
	async ({page, styleBooksPage}) => {
		const tokenName = `Custom Token ${getRandomString()}`;
		const tokenValue = getRandomString();

		await test.step('Create a custom token from the "New Token" modal', async () => {
			await styleBooksPage.createCustomToken({tokenName});
		});

		const tokenInput = page.getByLabel(tokenName, {exact: true});
		const customTokenBadge = page.getByRole('img', {
			name: 'Style Book Custom Token',
		});

		await test.step('Assert the new token renders as an editable field, marked as custom', async () => {
			await expect(tokenInput).toBeVisible();
			await expect(customTokenBadge).toBeVisible();
		});

		await test.step("Set the new token's value and wait for it to auto-save", async () => {
			await fillAndClickOutside(page, tokenInput, tokenValue);

			await styleBooksPage.waitForAutoSave();
		});

		await test.step('Reload and assert the token and its value survive', async () => {
			await page.reload();

			await expect(
				page.getByTestId('styleBookEditorSidebarContent')
			).toBeVisible();

			await expect(tokenInput).toHaveValue(tokenValue);
			await expect(customTokenBadge).toBeVisible();
		});
	}
);
