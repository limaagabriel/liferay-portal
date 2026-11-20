/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {globalMenuPagesTest} from '../../../fixtures/globalMenuPagesTest';
import {isolatedSiteTest} from '../../../fixtures/isolatedSiteTest';
import {loginTest} from '../../../fixtures/loginTest';
import {clickAndExpectToBeVisible} from '../../../utils/clickAndExpectToBeVisible';
import {closeProductMenu, openProductMenu} from '../../../utils/productMenu';
import {waitForPageToBeLoaded} from '../../../utils/waitForPageToBeLoaded';
import {contentDashboardPagesTest} from '../../content-dashboard-web/main/fixtures/contentDashboardPagesTest';

type Box = {x: number; y: number};

const test = mergeTests(
	contentDashboardPagesTest,
	globalMenuPagesTest,
	isolatedSiteTest,
	loginTest()
);

test(
	'The navigation item links have active state based on the current page',
	{tag: '@LPD-73706'},
	async ({globalMenuPage, page}) => {
		await test.step('Click on a navigation item and check if it navigates to the correct page', async () => {
			await globalMenuPage.goToControlPanel();

			const homeItem = page.getByRole('menuitem', {
				name: 'Home',
			});

			await expect(homeItem).toHaveClass(/active/);

			const userGroupsItem = page.getByRole('menuitem', {
				name: 'User Groups',
			});

			await expect(userGroupsItem).not.toHaveClass(/active/);

			await userGroupsItem.click();

			await expect(
				page.getByRole('heading', {name: 'User Groups'})
			).toBeAttached();
			await expect(homeItem).not.toHaveClass(/active/);
			await expect(userGroupsItem).toHaveClass(/active/);
		});
	}
);

test(
	'A user can select a site using the site selector',
	{tag: '@LPD-73706'},
	async ({globalMenuPage, page}) => {
		await test.step('Go to an Applications Panel page', async () => {
			await globalMenuPage.goToApplications();
		});

		await test.step('Open site selector and select a site', async () => {
			const goToOtherSiteButton = page.getByRole('button', {
				name: 'Go to Other Site',
			});

			await clickAndExpectToBeVisible({
				target: page.getByRole('heading', {
					name: 'Select Site',
				}),
				trigger: goToOtherSiteButton,
			});

			await page
				.frameLocator('iframe[title="Select Site"]')
				.getByRole('link', {exact: true, name: 'Liferay DXP Site'})
				.click();

			await waitForPageToBeLoaded(page);
		});
	}
);

test(
	'The toggle button opens/hides the side navigation menu',
	{tag: '@LPD-73706'},
	async ({globalMenuPage, page}) => {
		await test.step('Go to an Applications Panel page', async () => {
			await globalMenuPage.goToApplications();
		});

		await test.step('Click the toggle button and check if navigation is open/hidden', async () => {
			const menu = page.getByLabel('Applications Menu', {exact: true});

			await openProductMenu(page);

			await expect(menu).toBeVisible();

			await closeProductMenu(page);

			await expect(menu).not.toBeVisible();
		});
	}
);

test(
	'The side navigation menu visibility persists across page reloads',
	{tag: '@LPD-73706'},
	async ({globalMenuPage, page}) => {
		await test.step('Go to an Applications Panel page', async () => {
			await globalMenuPage.goToApplications();
		});

		const menu = page.getByLabel('Applications Menu', {exact: true});

		const testCases = [
			{expectedState: false, initialState: true},
			{expectedState: true, initialState: false},
		];

		for (const {expectedState, initialState} of testCases) {
			await test.step(`Set the navigation visibility to ${expectedState} and assert after reload`, async () => {
				await expect(menu).toBeVisible({
					visible: initialState,
				});

				// Hovering the toggle of a hidden menu reveals the menu over
				// it, so the pin button in the header opens it instead

				if (expectedState) {
					await openProductMenu(page);
				}
				else {
					await closeProductMenu(page);
				}

				await expect(menu).toBeVisible({
					visible: expectedState,
				});

				await page.reload();

				await waitForPageToBeLoaded(page);

				await expect(menu).toBeVisible({
					visible: expectedState,
				});
			});
		}
	}
);

test(
	'Navigation item groups maintain their state when the page reloads',
	{tag: '@LPD-79369'},
	async ({globalMenuPage, page}) => {
		await globalMenuPage.goToApplications();

		const workflowItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Workflow',
		});

		try {
			const categories = page.locator('button.collapse-icon');

			const categoriesExpanded = page.locator(
				'button.collapse-icon[aria-expanded="true"]'
			);

			await expect(categories).toHaveCount(
				await categoriesExpanded.count()
			);

			await workflowItem.click();

			await page.reload();

			await waitForPageToBeLoaded(page);

			await expect(workflowItem).toHaveAttribute(
				'aria-expanded',
				'false'
			);
		}
		finally {

			// Reset the state of the navigation item

			await expect(async () => {
				await workflowItem.click();

				await expect(workflowItem).toHaveAttribute(
					'aria-expanded',
					'true'
				);
			}).toPass();
		}
	}
);

test(
	'Escape key does not close the side navigation menu',
	{tag: '@LPD-79543'},
	async ({globalMenuPage, page}) => {
		const menu = page.getByLabel('Applications Menu', {exact: true});

		await test.step('Go to an Applications Panel page', async () => {
			await globalMenuPage.goToApplications();

			await expect(menu).toBeVisible();
		});

		await test.step('Press the Escape key and check if the navigation menu is still visible', async () => {
			await page.keyboard.press('Escape');

			await page.reload();

			await expect(menu).toBeVisible();
		});
	}
);

test(
	'The side navigation shows a visible divider separating it from the content',
	{tag: '@LPD-93647'},
	async ({globalMenuPage, page}) => {
		await test.step('Go to an Applications Panel page', async () => {
			await globalMenuPage.goToApplications();

			await expect(
				page.getByLabel('Applications Menu', {exact: true})
			).toBeVisible();
		});

		await test.step('Assert the divider border is actually rendered', async () => {
			const {borderRightColor, borderRightStyle, borderRightWidth} =
				await page.getByTestId('sideNavigation').evaluate((element) => {
					const computedStyle = window.getComputedStyle(element);

					return {
						borderRightColor: computedStyle.borderRightColor,
						borderRightStyle: computedStyle.borderRightStyle,
						borderRightWidth: computedStyle.borderRightWidth,
					};
				});

			expect(borderRightStyle).toBe('solid');

			expect(parseFloat(borderRightWidth)).toBeGreaterThan(0);

			expect(borderRightColor).not.toBe('rgba(0, 0, 0, 0)');
		});
	}
);

test(
	'Side navigation remains visible after Liferay.Portlet.refresh call',
	{tag: '@LPD-86410'},
	async ({contentDashboardPage, page, site}) => {
		async function expectSideNavigationToBeRendered() {
			const headerToggler = page.getByTestId(
				'unifiedSidebarHeaderToggler'
			);
			const sideNavigation = page.getByLabel('Applications Menu', {
				exact: true,
			});
			const toggler = page.getByTestId('sideNavigationToggler');

			// While the menu is pinned, its header holds the toggle

			await expect(sideNavigation).toBeVisible();
			await expect(headerToggler).toBeVisible();
			await expect(toggler).toBeHidden();

			await closeProductMenu(page);

			await expect(sideNavigation).toBeHidden();
			await expect(toggler).toBeVisible();

			await openProductMenu(page);

			await expect(sideNavigation).toBeVisible();
			await expect(headerToggler).toBeVisible();
			await expect(toggler).toBeHidden();
		}

		await test.step('Go to Applications > Content Dashboard', async () => {
			await contentDashboardPage.goto(site.friendlyUrlPath);

			await waitForPageToBeLoaded(page);

			await expectSideNavigationToBeRendered();
		});

		const modalTitle = page.getByRole('heading', {
			exact: true,
			name: 'Configuration',
		});

		await test.step('Click on the settings (cog) icon to open the modal', async () => {
			await page
				.locator('button', {
					has: page.locator('svg.lexicon-icon-cog'),
				})
				.click();

			await expect(modalTitle).toBeVisible();
		});

		await test.step('Close the modal and verify side navigation visibility', async () => {
			await page
				.getByRole('button', {exact: true, name: 'Close'})
				.click();

			await expect(modalTitle).toBeHidden();

			await expectSideNavigationToBeRendered();
		});
	}
);

test(
	'The filter finds a screen inside an application',
	{tag: '@LPD-103691'},
	async ({globalMenuPage, page}) => {
		const sideNavigation = page.getByLabel('Control Panel Menu', {
			exact: true,
		});

		const fileStorageItem = page.getByRole('menuitem', {
			name: 'File Storage',
		});

		await test.step('Go to the Control Panel', async () => {
			await globalMenuPage.goToControlPanel();

			await expect(sideNavigation).toBeVisible();
			await expect(fileStorageItem).toBeHidden();
		});

		await test.step('Filter for a screen inside an application', async () => {
			await sideNavigation.getByRole('searchbox').fill('File Storage');

			await expect(fileStorageItem).toBeVisible();
		});

		await test.step('Follow the screen', async () => {
			await fileStorageItem.click();

			await waitForPageToBeLoaded(page);

			await expect(
				page.getByRole('heading', {name: 'File Storage'})
			).toBeAttached();
		});
	}
);

test(
	'The filter finds a screen inside a permission filtered application',
	{tag: '@LPD-103691'},
	async ({globalMenuPage, page}) => {
		const sideNavigation = page.getByLabel('Control Panel Menu', {
			exact: true,
		});

		const regularRolesItem = page.getByRole('menuitem', {
			name: 'Regular Roles',
		});

		await test.step('Go to the Control Panel', async () => {
			await globalMenuPage.goToControlPanel();

			await expect(sideNavigation).toBeVisible();
			await expect(regularRolesItem).toBeHidden();
		});

		await test.step('Filter for a screen inside an application', async () => {
			await sideNavigation.getByRole('searchbox').fill('Regular Roles');

			await expect(regularRolesItem).toBeVisible();
		});

		await test.step('Follow the screen', async () => {
			await regularRolesItem.click();

			await waitForPageToBeLoaded(page);

			await expect(
				page.getByRole('heading', {name: 'Roles'})
			).toBeAttached();
		});
	}
);

test(
	'The Control Panel root shows the System and Instance scopes',
	{tag: '@LPD-100172'},
	async ({globalMenuPage, page}) => {
		const sideNavigation = page.getByLabel('Control Panel Menu', {
			exact: true,
		});

		await test.step('Go to the Control Panel', async () => {
			await globalMenuPage.goToControlPanel();

			await expect(sideNavigation).toBeVisible();
		});

		await test.step('Both scopes mark the start of a zone', async () => {
			const scopeItems = sideNavigation.getByTestId(
				'sideNavigationScopeItem'
			);

			await expect(scopeItems).toHaveCount(2);
			await expect(scopeItems.first()).toHaveText('System');
			await expect(scopeItems.last()).toContainText('Instance:');
		});

		await test.step('Neither scope is collapsible', async () => {
			await expect(
				sideNavigation
					.getByTestId('sideNavigationScopeItem')
					.getByRole('button')
			).toHaveCount(0);
		});

		await test.step('An application of a scope sits at the root', async () => {
			await expect(
				sideNavigation.getByRole('menuitem', {
					exact: true,
					name: 'Server Administration',
				})
			).not.toHaveAttribute('aria-expanded');
		});

		await test.step('A category below a scope still collapses', async () => {
			await expect(
				sideNavigation.getByRole('menuitem', {
					exact: true,
					name: 'Users',
				})
			).toHaveAttribute('aria-expanded');
		});
	}
);

test(
	'Hovering the toggle reveals the hidden side navigation without pinning it',
	{tag: '@LPD-110660'},
	async ({globalMenuPage, page}) => {
		const controlMenu = page.locator('nav.control-menu-container');
		const menu = page.getByLabel('Control Panel Menu', {exact: true});
		const pageBody = page.locator('.portlet-boundary').first();
		const headerToggler = page.getByTestId('unifiedSidebarHeaderToggler');
		const toggler = page.getByTestId('sideNavigationToggler');

		let reference: Box;

		// The visible toggles are the ones rendered and not covered by
		// another element, as a revealed menu covers the Control Menu toggle

		async function expectOneToggle(testId: string) {
			await expect(async () => {
				const toggles = await page.evaluate(() =>
					Array.from(
						document.querySelectorAll<HTMLElement>(
							'[data-qa-id="sideNavigationToggler"], [data-qa-id="unifiedSidebarHeaderToggler"]'
						)
					)
						.map((element) => {
							const {height, width, x, y} =
								element.getBoundingClientRect();

							const topElement = document.elementFromPoint(
								x + width / 2,
								y + height / 2
							);

							return {
								covered: !topElement?.closest(
									`[data-qa-id="${element.dataset.qaId}"]`
								),
								height,
								testId: element.dataset.qaId,
								width,
								x,
								y,
							};
						})
						.filter(
							({covered, height, width}) =>
								!covered && width > 0 && height > 0
						)
				);

				expect(toggles.map(({testId}) => testId)).toEqual([testId]);

				const [{height, width, x, y}] = toggles;

				expect({height, width}).toEqual({height: 32, width: 32});
				expect(x).toBeCloseTo(reference.x, 0);
				expect(Math.abs(y - reference.y)).toBeLessThanOrEqual(4);
			}).toPass({timeout: 5000});
		}

		async function getHeaderTogglerStyle() {
			return headerToggler.evaluate((element) => {
				const {backgroundColor, borderRadius, color, height, width} =
					getComputedStyle(element);

				return {
					backgroundColor,
					borderRadius,
					color,
					height,
					iconColor: getComputedStyle(element.querySelector('svg')!)
						.color,
					width,
				};
			});
		}

		async function hoverToggler() {
			await moveAwayFromMenu();

			await toggler.hover();
		}

		async function moveAwayFromMenu() {
			await page.mouse.move(900, 500);
		}

		try {
			await test.step('Go to the Control Panel and hide the menu', async () => {
				await globalMenuPage.goToControlPanel();

				await closeProductMenu(page);

				await moveAwayFromMenu();

				await expect(menu).toBeHidden();
				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
			});

			await test.step('Only the Control Menu toggle shows while the menu is hidden', async () => {
				reference = (await toggler.boundingBox())!;

				await expectOneToggle('sideNavigationToggler');

				const html = await (await page.request.get(page.url())).text();

				expect(html).toContain('product-menu-toast-toggle');
			});

			await test.step('Hovering the toggle reveals the menu without moving the layout', async () => {
				const controlMenuBox = await controlMenu.boundingBox();
				const pageBodyBox = await pageBody.boundingBox();

				expect(controlMenuBox).not.toBeNull();
				expect(pageBodyBox).not.toBeNull();

				await toggler.hover();

				await expect(menu).toBeVisible();
				await expect(menu).toHaveClass(/c-slideout-peek/);

				expect(await controlMenu.boundingBox()).toEqual(controlMenuBox);
				expect(await pageBody.boundingBox()).toEqual(pageBodyBox);

				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
			});

			await test.step('Moving from the toggle into the menu keeps it revealed', async () => {
				const menuBox = await menu.boundingBox();

				await page.mouse.move(
					menuBox!.x + menuBox!.width / 2,
					menuBox!.y + menuBox!.height / 2,
					{steps: 10}
				);

				await expect(menu).toBeVisible();
				await expect(menu).toHaveClass(/c-slideout-peek/);
			});

			await test.step('Moving away hides the menu', async () => {
				await moveAwayFromMenu();

				await expect(menu).toBeHidden();
				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
			});

			await test.step('Escape hides the revealed menu', async () => {
				await hoverToggler();

				await expect(menu).toBeVisible();

				await page.keyboard.press('Escape');

				await expect(menu).toBeHidden();
				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
			});

			await test.step('Following a navigation item keeps the menu unpinned', async () => {
				await hoverToggler();

				await expect(menu).toBeVisible();

				await menu.getByRole('menuitem', {name: 'User Groups'}).click();

				await waitForPageToBeLoaded(page);

				await expect(
					page.getByRole('heading', {name: 'User Groups'})
				).toBeAttached();
				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
			});

			await test.step('The menu stays unpinned after a reload', async () => {
				await moveAwayFromMenu();

				await page.reload();

				await waitForPageToBeLoaded(page);

				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
				await expect(menu).toBeHidden();
			});

			await test.step('The revealed header toggle shows the logo until it is hovered', async () => {
				const icon = headerToggler.locator('svg');
				const logo = headerToggler.locator('img');

				await hoverToggler();

				await expect(menu).toBeVisible();

				const menuBox = await menu.boundingBox();

				await page.mouse.move(
					menuBox!.x + menuBox!.width / 2,
					menuBox!.y + menuBox!.height / 2,
					{steps: 10}
				);

				await expect(menu).toHaveClass(/c-slideout-peek/);
				await expect(headerToggler).toHaveAttribute(
					'aria-label',
					'Pin Product Menu'
				);
				await expect(logo).toBeVisible();
				await expect(icon).toBeHidden();

				await expectOneToggle('unifiedSidebarHeaderToggler');

				await headerToggler.hover();

				await expect(icon).toBeVisible();
				await expect(logo).toBeHidden();
			});

			await test.step('The revealed header toggle is light on hover and has no active background', async () => {
				const style = {
					borderRadius: '8px',
					color: 'rgb(107, 108, 126)',
					height: '32px',
					iconColor: 'rgb(107, 108, 126)',
					width: '32px',
				};

				const menuBox = await menu.boundingBox();

				await page.mouse.move(
					menuBox!.x + menuBox!.width / 2,
					menuBox!.y + menuBox!.height / 2,
					{steps: 10}
				);

				await expect(headerToggler).toHaveAttribute(
					'aria-expanded',
					'false'
				);

				await expect(async () => {
					expect(await getHeaderTogglerStyle()).toEqual({
						...style,
						backgroundColor: 'rgba(0, 0, 0, 0)',
					});
				}).toPass({timeout: 5000});

				await headerToggler.hover();

				await expect(async () => {
					expect(await getHeaderTogglerStyle()).toEqual({
						...style,
						backgroundColor: 'rgb(241, 242, 245)',
					});
				}).toPass({timeout: 5000});
			});

			await test.step('The header toggle of the revealed menu pins it', async () => {
				const pageBodyBox = await pageBody.boundingBox();

				await hoverToggler();

				await expect(menu).toBeVisible();

				await headerToggler.click();

				await expect(toggler).toHaveAttribute('aria-expanded', 'true');
				await expect(menu).not.toHaveClass(/c-slideout-peek/);
				await expect(headerToggler).toBeVisible();
				await expect(headerToggler).toHaveAttribute(
					'aria-expanded',
					'true'
				);

				await expect(async () => {
					expect((await pageBody.boundingBox())!.x).toBeGreaterThan(
						pageBodyBox!.x
					);
				}).toPass({timeout: 5000});
			});

			await test.step('Only the header toggle shows while the menu is pinned', async () => {
				await moveAwayFromMenu();

				await expectOneToggle('unifiedSidebarHeaderToggler');

				await expect(toggler).toBeHidden();
				await expect(toggler).toHaveAttribute('aria-expanded', 'true');

				// The pinned header toggle shows the active background, also on hover

				// rgba(39, 40, 51, 0.08) as Chromium serializes a relative color

				const pinnedStyle = {
					backgroundColor: 'color(srgb 0.152941 0.156863 0.2 / 0.08)',
					borderRadius: '8px',
					color: 'rgb(107, 108, 126)',
					height: '32px',
					iconColor: 'rgb(107, 108, 126)',
					width: '32px',
				};

				await expect(async () => {
					expect(await getHeaderTogglerStyle()).toEqual(pinnedStyle);
				}).toPass({timeout: 5000});

				await headerToggler.hover();

				await expect(async () => {
					expect(await getHeaderTogglerStyle()).toEqual(pinnedStyle);
				}).toPass({timeout: 5000});

				await moveAwayFromMenu();

				// The hidden toggle leaves no gap behind

				await expect(async () => {
					expect(
						await toggler
							.locator('xpath=ancestor::li')
							.evaluate(
								(element) =>
									element.getBoundingClientRect().width
							)
					).toBe(0);
				}).toPass({timeout: 5000});

				// The page is served without the toggle placeholder

				const html = await (await page.request.get(page.url())).text();

				expect(html).not.toContain('product-menu-toast-toggle');
			});

			await test.step('Unpinning with the keyboard moves the focus to the Control Menu toggle', async () => {

				// The mouse pinned the menu, so the header toggle has the focus

				await expect(headerToggler).toBeFocused();

				await page.keyboard.press('Enter');

				await expect(menu).toBeHidden();
				await expect(toggler).toHaveAttribute('aria-expanded', 'false');
				await expect(toggler).toBeFocused();

				await expectOneToggle('sideNavigationToggler');
			});

			await test.step('Pinning with the keyboard moves the focus to the header toggle', async () => {
				await page.keyboard.press('Enter');

				await expect(toggler).toHaveAttribute('aria-expanded', 'true');
				await expect(menu).toBeVisible();
				await expect(headerToggler).toBeFocused();

				await expectOneToggle('unifiedSidebarHeaderToggler');

				await expect(toggler).toBeHidden();
			});

			await test.step('The pinned menu stays pinned with one toggle after a reload', async () => {
				await page.reload();

				await waitForPageToBeLoaded(page);

				await expect(menu).toBeVisible();

				await expectOneToggle('unifiedSidebarHeaderToggler');

				await expect(toggler).toBeHidden();
			});
		}
		finally {

			// Restore the pinned menu so later tests do not start hidden

			await moveAwayFromMenu();

			await openProductMenu(page);
		}
	}
);
