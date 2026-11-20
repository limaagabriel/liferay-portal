/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React from 'react';

import '@testing-library/jest-dom';

import UnifiedSidebar from '../../src/main/resources/META-INF/resources/unified_sidebar/UnifiedSidebar';
import UnifiedSidebarSiteSelector from '../../src/main/resources/META-INF/resources/unified_sidebar/UnifiedSidebarSiteSelector';

const DEFAULT_PROPS = {
	categoryImageUrl: '/images/test-icon.svg',
	label: 'Control Panel',
};

// The pinned sidebar labels its header toggle like its close button, but only
// the toggle reports whether the sidebar is expanded.

const getHeaderToggle = () =>
	screen.getByRole('button', {expanded: true, name: 'close-product-menu'});

describe('UnifiedSidebar', () => {
	it('renders the label', () => {
		render(<UnifiedSidebar {...DEFAULT_PROPS} />);

		expect(screen.getByText('Control Panel')).toBeInTheDocument();
	});

	it('renders no header toggle while the sidebar is closed', () => {
		const {container} = render(<UnifiedSidebar {...DEFAULT_PROPS} />);

		expect(
			screen.queryByLabelText('pin-product-menu')
		).not.toBeInTheDocument();
		expect(
			screen.queryByRole('button', {expanded: true})
		).not.toBeInTheDocument();
		expect(container.querySelector('img')).not.toBeInTheDocument();
	});

	it('renders children inside the panel body', () => {
		render(
			<UnifiedSidebar {...DEFAULT_PROPS}>
				<UnifiedSidebar.Body>
					<p>Panel content</p>
				</UnifiedSidebar.Body>
			</UnifiedSidebar>
		);

		expect(screen.getByText('Panel content')).toBeInTheDocument();
	});

	it('applies testId as data-testid', () => {
		render(<UnifiedSidebar {...DEFAULT_PROPS} testId="my-sidebar" />);

		expect(screen.getByTestId('my-sidebar')).toBeInTheDocument();
	});

	it('renders headerActions inside the header', () => {
		render(
			<UnifiedSidebar
				{...DEFAULT_PROPS}
				headerActions={
					<UnifiedSidebarSiteSelector
						eventName="selectSite"
						url="/select-site"
					/>
				}
			/>
		);

		expect(screen.getByLabelText('go-to-other-site')).toBeInTheDocument();
	});

	it('does not render the site selector button when headerActions is absent', () => {
		render(<UnifiedSidebar {...DEFAULT_PROPS} />);

		expect(
			screen.queryByLabelText('go-to-other-site')
		).not.toBeInTheDocument();
	});

	describe('when LPD-57922 is enabled', () => {
		beforeEach(() => {
			Liferay.FeatureFlags['LPD-57922'] = true;
		});

		afterEach(() => {
			Liferay.FeatureFlags['LPD-57922'] = false;
		});

		it('shows the color scheme toggle button', () => {
			render(
				<UnifiedSidebar
					{...DEFAULT_PROPS}
					colorScheme="light"
					colorSchemeSessionKey="colorScheme"
				/>
			);

			expect(
				screen.getByLabelText('switch-to-dark-mode')
			).toBeInTheDocument();
		});
	});

	it('does not show the color scheme toggle button when LPD-57922 is disabled', () => {
		render(<UnifiedSidebar {...DEFAULT_PROPS} />);

		expect(
			screen.queryByLabelText('switch-to-dark-mode')
		).not.toBeInTheDocument();
	});

	describe('when the pointer is over the toggle', () => {
		beforeEach(() => {
			Object.defineProperty(document.body, 'clientWidth', {
				configurable: true,
				value: 1024,
			});
		});

		afterEach(() => {
			delete (document.body as any).clientWidth;
		});

		it('reveals the closed sidebar as an overlay', async () => {
			render(
				<>
					<button aria-controls="navigation">Toggle</button>

					<UnifiedSidebar
						{...DEFAULT_PROPS}
						navigationId="navigation"
					/>
				</>
			);

			expect(screen.getByTestId('unified-sidebar')).not.toHaveClass(
				'c-slideout-peek'
			);

			await userEvent.hover(screen.getByText('Toggle'));

			expect(screen.getByTestId('unified-sidebar')).toHaveClass(
				'c-slideout-peek'
			);
		});

		it('shows the product logo and the pin icon in the header toggle while revealed', async () => {
			render(
				<>
					<button aria-controls="navigation">Toggle</button>

					<UnifiedSidebar
						{...DEFAULT_PROPS}
						navigationId="navigation"
					/>
				</>
			);

			expect(
				screen.queryByLabelText('pin-product-menu')
			).not.toBeInTheDocument();

			await userEvent.hover(screen.getByText('Toggle'));

			const headerToggle = screen.getByLabelText('pin-product-menu');

			expect(headerToggle).toHaveAttribute('aria-expanded', 'false');
			expect(headerToggle.querySelector('img')).toHaveAttribute(
				'src',
				'/images/test-icon.svg'
			);
			expect(
				headerToggle.querySelector('.lexicon-icon-product-menu-closed')
			).toBeInTheDocument();
		});

		it('pins the sidebar when the pin button is clicked', async () => {
			const onOpenChange = jest.fn();

			render(
				<>
					<button aria-controls="navigation">Toggle</button>

					<UnifiedSidebar
						{...DEFAULT_PROPS}
						navigationId="navigation"
						onOpenChange={onOpenChange}
					/>
				</>
			);

			await userEvent.hover(screen.getByText('Toggle'));
			await userEvent.click(screen.getByLabelText('pin-product-menu'));

			expect(onOpenChange).toHaveBeenCalledWith(true);
		});

		it('does not reveal the pinned sidebar', async () => {
			render(
				<>
					<button aria-controls="navigation">Toggle</button>

					<UnifiedSidebar
						{...DEFAULT_PROPS}
						navigationId="navigation"
						open
					/>
				</>
			);

			await userEvent.hover(screen.getByText('Toggle'));

			expect(screen.getByTestId('unified-sidebar')).not.toHaveClass(
				'c-slideout-peek'
			);
			expect(
				screen.queryByLabelText('pin-product-menu')
			).not.toBeInTheDocument();
		});
	});

	describe('when the sidebar is pinned', () => {
		it('shows the close icon without the product logo in the header toggle', () => {
			const {container} = render(
				<UnifiedSidebar {...DEFAULT_PROPS} open />
			);

			const headerToggle = getHeaderToggle();

			expect(headerToggle).toHaveAttribute('aria-expanded', 'true');
			expect(
				headerToggle.querySelector('.lexicon-icon-product-menu-open')
			).toBeInTheDocument();
			expect(container.querySelector('img')).not.toBeInTheDocument();
			expect(
				screen.queryByLabelText('pin-product-menu')
			).not.toBeInTheDocument();
		});

		it('unpins the sidebar when the header toggle is clicked', async () => {
			const onOpenChange = jest.fn();

			render(
				<UnifiedSidebar
					{...DEFAULT_PROPS}
					onOpenChange={onOpenChange}
					open
				/>
			);

			await userEvent.click(getHeaderToggle());

			expect(onOpenChange).toHaveBeenCalledWith(false);
		});
	});

	it('calls onOpenChange when the close button is clicked', async () => {
		const onOpenChange = jest.fn();

		render(
			<UnifiedSidebar
				{...DEFAULT_PROPS}
				onOpenChange={onOpenChange}
				open
			/>
		);

		await userEvent.click(
			screen
				.getAllByLabelText('close-product-menu')
				.find((button) => !button.hasAttribute('aria-expanded'))!
		);

		expect(onOpenChange).toHaveBeenCalledWith(false);
	});

	it('uses the custom headerMessages when provided', async () => {
		const onOpenChange = jest.fn();

		render(
			<UnifiedSidebar
				{...DEFAULT_PROPS}
				headerMessages={{closeAriaLabel: 'close-site-panel'}}
				onOpenChange={onOpenChange}
				open
			/>
		);

		await userEvent.click(screen.getByLabelText('close-site-panel'));

		expect(onOpenChange).toHaveBeenCalledWith(false);
	});
});
