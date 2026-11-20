/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {ClayButtonWithIcon} from '@clayui/button';
import React, {useEffect, useMemo, useRef, useState} from 'react';

const HEADER_TOGGLER_SELECTOR = '[data-qa-id="unifiedSidebarHeaderToggler"]';

// The header toggler belongs to another React root, so it can render a few
// frames after this toggler learns about the new state.

const FOCUS_ATTEMPTS = 20;

type FocusHandOver = 'header' | 'toggler';

interface Props {
	navigationId: string;
	stateChangedEventName: string;
	stateRequestedEventName: string;
	visible: boolean;
}

function UnifiedSidebarToggler({
	navigationId,
	stateChangedEventName,
	stateRequestedEventName,
	visible: initialVisible,
}: Props) {
	const [visible, setVisible] = useState(initialVisible);

	const buttonRef = useRef<HTMLButtonElement>(null);
	const focusHandOverRef = useRef<FocusHandOver | null>(null);
	const visibleRef = useRef(initialVisible);

	async function toggle() {
		Liferay.fire(stateRequestedEventName, {visible: !visible});
	}

	useEffect(() => {
		function handleStateChanged({visible}: {visible: boolean}) {
			if (visible !== visibleRef.current) {

				// A pinned sidebar hides this toggler, which would drop its
				// focus. The header toggler takes the toggle role then, so it
				// takes the focus too, and gives it back when unpinning.

				focusHandOverRef.current = !visible
					? 'toggler'
					: document.activeElement === buttonRef.current
						? 'header'
						: null;
			}

			setVisible(visible);
		}

		Liferay.on(stateChangedEventName, handleStateChanged);

		// The navigation can change its state on load, for example when it
		// starts closed on a narrow screen or a page editor closes it, before
		// this toggler subscribes, so read its current state as well.

		const navigation = document.getElementById(navigationId);

		if (navigation) {
			setVisible(navigation.classList.contains('c-slideout-show'));
		}

		return () => Liferay.detach(stateChangedEventName, handleStateChanged);
	}, [navigationId, stateChangedEventName]);

	useEffect(() => {
		visibleRef.current = visible;

		const focusHandOver = focusHandOverRef.current;

		focusHandOverRef.current = null;

		if (!focusHandOver) {
			return;
		}

		let attempts = 0;
		let frame = 0;

		function attemptFocus() {
			const navigation = document.getElementById(navigationId);

			if (focusHandOver === 'header') {
				const headerToggler =
					navigation?.querySelector<HTMLElement>(
						HEADER_TOGGLER_SELECTOR
					) ?? null;

				headerToggler?.focus();

				if (headerToggler && document.activeElement === headerToggler) {
					return;
				}
			}
			else {

				// Focus that is still in the panel or lost to the page when
				// the panel went inert has nowhere else to go.

				const activeElement = document.activeElement;

				if (
					activeElement === document.body ||
					navigation?.contains(activeElement)
				) {
					buttonRef.current?.focus();
				}

				return;
			}

			if (++attempts < FOCUS_ATTEMPTS) {
				frame = requestAnimationFrame(attemptFocus);
			}
		}

		// Let the sidebar settle first, as it restores focus of its own when
		// it closes and marks the panel inert.

		frame = requestAnimationFrame(attemptFocus);

		return () => cancelAnimationFrame(frame);
	}, [navigationId, visible]);

	const title = useMemo(
		() =>
			visible
				? Liferay.Language.get('close-product-menu')
				: Liferay.Language.get('open-product-menu'),
		[visible]
	);

	return (
		<ClayButtonWithIcon
			aria-controls={navigationId}
			aria-expanded={visible}
			aria-label={title}
			className="control-menu-nav-link"
			data-qa-id="sideNavigationToggler"
			displayType="unstyled"
			hidden={visible}
			monospaced
			onClick={toggle}
			ref={buttonRef}
			role="tab"
			size="sm"
			symbol={visible ? 'product-menu-open' : 'product-menu-closed'}
			title={title}
		/>
	);
}

export default UnifiedSidebarToggler;
