/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {act, renderHook} from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import {useUnifiedSidebarPeek} from '../../src/main/resources/META-INF/resources/unified_sidebar/useUnifiedSidebarPeek';

const NAVIGATION_ID = 'navigation';

function createToggle() {
	const toggle = document.createElement('button');

	toggle.setAttribute('aria-controls', NAVIGATION_ID);

	document.body.appendChild(toggle);

	return toggle;
}

function dispatchPointer(
	type: string,
	target: Element,
	relatedTarget: Element,
	{clientX, clientY}: {clientX: number; clientY: number}
) {
	act(() => {
		target.dispatchEvent(
			Object.assign(new Event(type, {bubbles: true}), {
				clientX,
				clientY,
				pointerType: 'mouse',
				relatedTarget,
			})
		);
	});
}

function movePointer(from: Element, to: Element) {
	function dispatch(type: string, target: Element, relatedTarget: Element) {
		act(() => {
			target.dispatchEvent(
				Object.assign(new Event(type, {bubbles: true}), {
					pointerType: 'mouse',
					relatedTarget,
				})
			);
		});
	}

	dispatch('pointerout', from, to);
	dispatch('pointerover', to, from);
}

function renderPeek({
	hasNavigationId = true,
	open = false,
}: {
	hasNavigationId?: boolean;
	open?: boolean;
} = {}) {
	const navigationId = hasNavigationId ? NAVIGATION_ID : undefined;

	const panel = document.createElement('div');

	const input = document.createElement('input');

	panel.id = NAVIGATION_ID;

	panel.appendChild(input);

	document.body.appendChild(panel);

	const panelRef = {current: panel};

	const hook = renderHook(
		(props: {navigationId?: string; open: boolean}) =>
			useUnifiedSidebarPeek({...props, panelRef}),
		{initialProps: {navigationId, open}}
	);

	return {
		...hook,
		input,
		outside: document.body,
		panel,
	};
}

describe('useUnifiedSidebarPeek', () => {
	const util = Liferay.Util;

	let bodyWidth = 1024;

	let user: ReturnType<typeof userEvent.setup>;

	function hoverAt(
		target: Element,
		coords: {clientX: number; clientY: number}
	) {
		return user.pointer({coords, target});
	}

	beforeEach(() => {
		jest.useFakeTimers();

		jest.clearAllMocks();

		bodyWidth = 1024;

		Object.defineProperty(document.body, 'clientWidth', {
			configurable: true,
			get: () => bodyWidth,
		});

		Liferay.Util = {
			...util,
			Session: {
				get: jest.fn(),
				set: jest.fn(() => Promise.resolve()),
			},
		};

		user = userEvent.setup({advanceTimers: jest.advanceTimersByTime});
	});

	afterEach(() => {
		delete (document.body as any).clientWidth;

		document.body.innerHTML = '';

		Liferay.Util = util;

		jest.useRealTimers();
	});

	it('reveals the sidebar when the pointer is over the toggle', async () => {
		const toggle = createToggle();

		const {result} = renderPeek();

		expect(result.current).toBe(false);

		await user.hover(toggle);

		expect(result.current).toBe(true);
	});

	it('reveals the sidebar when the pointer is over the panel', async () => {
		const {panel, result} = renderPeek();

		await user.hover(panel);

		expect(result.current).toBe(true);
	});

	it('hides the sidebar 250 ms after the pointer leaves', async () => {
		const toggle = createToggle();

		const {outside, result} = renderPeek();

		await user.hover(toggle);
		await user.hover(outside);

		act(() => {
			jest.advanceTimersByTime(249);
		});

		expect(result.current).toBe(true);

		act(() => {
			jest.advanceTimersByTime(1);
		});

		expect(result.current).toBe(false);
	});

	it('keeps the sidebar when the pointer re-enters before the delay', async () => {
		const toggle = createToggle();

		const {outside, panel, result} = renderPeek();

		await user.hover(toggle);
		await user.hover(outside);

		act(() => {
			jest.advanceTimersByTime(200);
		});

		await user.hover(panel);

		act(() => {
			jest.advanceTimersByTime(1000);
		});

		expect(result.current).toBe(true);
	});

	it('keeps the sidebar when the pointer moves from the toggle to the panel', async () => {
		const toggle = createToggle();

		const {panel, result} = renderPeek();

		await user.hover(toggle);

		movePointer(toggle, panel);

		act(() => {
			jest.advanceTimersByTime(1000);
		});

		expect(result.current).toBe(true);
	});

	it('keeps the sidebar while the focus is inside the panel', async () => {
		const toggle = createToggle();

		const {input, outside, result} = renderPeek();

		await user.hover(toggle);

		act(() => input.focus());

		await user.hover(outside);

		act(() => {
			jest.advanceTimersByTime(1000);
		});

		expect(result.current).toBe(true);

		act(() => input.blur());

		act(() => {
			jest.advanceTimersByTime(250);
		});

		expect(result.current).toBe(false);
	});

	it('hides the sidebar on Escape until the pointer leaves and comes back', async () => {
		const toggle = createToggle();

		const {panel, result} = renderPeek();

		await user.hover(toggle);
		await user.keyboard('{Escape}');

		expect(result.current).toBe(false);

		movePointer(toggle, panel);

		expect(result.current).toBe(false);

		await user.hover(document.body);
		await user.hover(toggle);

		expect(result.current).toBe(true);
	});

	it('stays hidden after being unpinned under the pointer until the pointer leaves and comes back', async () => {
		const toggle = createToggle();

		const {outside, rerender, result} = renderPeek({open: true});

		await hoverAt(toggle, {clientX: 10, clientY: 10});

		rerender({navigationId: NAVIGATION_ID, open: false});

		expect(result.current).toBe(false);

		await hoverAt(toggle, {clientX: 10, clientY: 10});

		expect(result.current).toBe(false);

		await hoverAt(outside, {clientX: 500, clientY: 500});
		await hoverAt(toggle, {clientX: 10, clientY: 10});

		expect(result.current).toBe(true);
	});

	it('stays hidden when unpinning moves the layout under the resting pointer', async () => {
		const toggle = createToggle();

		const {input, outside, rerender, result} = renderPeek({open: true});

		const point = {clientX: 10, clientY: 10};

		await hoverAt(input, point);

		rerender({navigationId: NAVIGATION_ID, open: false});

		// The panel leaves from under the pointer, so the browser reports the
		// pointer leaving it and entering the toggle without any movement

		dispatchPointer('pointerout', input, outside, point);
		dispatchPointer('pointerover', outside, input, point);
		dispatchPointer('pointerout', outside, toggle, point);
		dispatchPointer('pointerover', toggle, outside, point);

		expect(result.current).toBe(false);

		// It reveals again once the pointer really leaves and comes back

		dispatchPointer('pointerout', toggle, outside, {
			clientX: 400,
			clientY: 400,
		});
		dispatchPointer('pointerover', outside, toggle, {
			clientX: 400,
			clientY: 400,
		});
		dispatchPointer('pointerout', outside, toggle, point);
		dispatchPointer('pointerover', toggle, outside, point);

		expect(result.current).toBe(true);
	});

	it('does nothing while the sidebar is pinned', async () => {
		const toggle = createToggle();

		const {result} = renderPeek({open: true});

		await user.hover(toggle);

		expect(result.current).toBe(false);
	});

	it('hides the sidebar when it gets pinned', async () => {
		const toggle = createToggle();

		const {rerender, result} = renderPeek();

		await user.hover(toggle);

		rerender({navigationId: NAVIGATION_ID, open: true});

		expect(result.current).toBe(false);
	});

	it('does nothing on a narrow screen', async () => {
		bodyWidth = 767;

		const toggle = createToggle();

		const {result} = renderPeek();

		await user.hover(toggle);

		expect(result.current).toBe(false);
	});

	it('does nothing with touch', async () => {
		const toggle = createToggle();

		const {result} = renderPeek();

		act(() => {
			toggle.dispatchEvent(
				Object.assign(new Event('pointerover', {bubbles: true}), {
					pointerType: 'touch',
				})
			);
		});

		expect(result.current).toBe(false);
	});

	it('does nothing without a navigationId', async () => {
		const toggle = createToggle();

		const {result} = renderPeek({hasNavigationId: false});

		await user.hover(toggle);

		expect(result.current).toBe(false);
	});

	it('finds a toggle that mounts after the sidebar', async () => {
		const {result} = renderPeek();

		await user.hover(document.body);

		const toggle = createToggle();

		await user.hover(toggle);

		expect(result.current).toBe(true);
	});

	it('does not save or announce any state change', async () => {
		const toggle = createToggle();

		const {outside} = renderPeek();

		await user.hover(toggle);
		await user.keyboard('{Escape}');
		await user.hover(outside);
		await user.hover(toggle);
		await user.hover(outside);

		act(() => {
			jest.advanceTimersByTime(1000);
		});

		expect(Liferay.Util.Session.set).not.toHaveBeenCalled();
		expect(Liferay.fire).not.toHaveBeenCalled();
	});
});
