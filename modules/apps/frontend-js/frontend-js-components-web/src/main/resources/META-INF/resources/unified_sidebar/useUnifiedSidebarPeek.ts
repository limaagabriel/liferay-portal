/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useIsMobileDevice} from '@clayui/shared';
import {useEffect, useRef, useState} from 'react';

const HIDE_DELAY = 250;

/**
 * Tells whether a closed sidebar is revealed as an overlay because the pointer
 * rests on its toggle or on the panel itself. It only reads the saved state
 * (`open`) and never changes it.
 *
 * The toggle is found through `aria-controls` on every event instead of once,
 * because it renders in a separate React root that can mount after the
 * sidebar.
 */
export function useUnifiedSidebarPeek({
	navigationId,
	open,
	panelRef,
}: {
	navigationId?: string;
	open: boolean;
	panelRef: React.RefObject<HTMLElement | null>;
}) {
	const isMobile = useIsMobileDevice();

	const [revealed, setRevealed] = useState(false);

	const dismissedRef = useRef(false);
	const focusInsideRef = useRef(false);
	const hideTimeoutRef = useRef<ReturnType<typeof setTimeout>>();
	const openRef = useRef(open);
	const pointerInsideRef = useRef(false);

	const enabled = !!navigationId && !isMobile;

	useEffect(
		function resetOnOpenChange() {
			openRef.current = open;

			clearTimeout(hideTimeoutRef.current);

			hideTimeoutRef.current = undefined;

			setRevealed(false);

			// Closing the sidebar under the pointer must not reveal it again
			// until the pointer leaves and comes back.

			dismissedRef.current = pointerInsideRef.current;
		},
		[open]
	);

	useEffect(
		function trackPointerAndFocus() {
			if (!enabled) {
				return;
			}

			function isInsidePanel(node: EventTarget | null) {
				return (
					node instanceof Node && !!panelRef.current?.contains(node)
				);
			}

			function isInsideToggle(node: EventTarget | null) {
				return (
					node instanceof Element &&
					node
						.closest('[aria-controls]')
						?.getAttribute('aria-controls') === navigationId
				);
			}

			function isInsidePeekArea(node: EventTarget | null) {
				return isInsidePanel(node) || isInsideToggle(node);
			}

			function update() {
				if (openRef.current) {
					return;
				}

				if (pointerInsideRef.current) {
					clearTimeout(hideTimeoutRef.current);

					hideTimeoutRef.current = undefined;

					if (!dismissedRef.current) {
						setRevealed(true);
					}
				}
				else if (focusInsideRef.current) {
					clearTimeout(hideTimeoutRef.current);

					hideTimeoutRef.current = undefined;
				}
				else if (hideTimeoutRef.current === undefined) {
					hideTimeoutRef.current = setTimeout(() => {
						hideTimeoutRef.current = undefined;

						setRevealed(false);
					}, HIDE_DELAY);
				}
			}

			function handlePointerOver(event: PointerEvent) {
				if (event.pointerType !== 'mouse') {
					return;
				}

				if (isInsidePeekArea(event.target)) {
					pointerInsideRef.current = true;

					update();
				}
			}

			function handlePointerOut(event: PointerEvent) {
				if (event.pointerType !== 'mouse') {
					return;
				}

				if (
					pointerInsideRef.current &&
					!isInsidePeekArea(event.relatedTarget)
				) {
					pointerInsideRef.current = false;
					dismissedRef.current = false;

					update();
				}
			}

			function handleFocusIn(event: FocusEvent) {
				focusInsideRef.current = isInsidePanel(event.target);

				update();
			}

			function handleFocusOut(event: FocusEvent) {
				if (!isInsidePanel(event.relatedTarget)) {
					focusInsideRef.current = false;

					update();
				}
			}

			document.addEventListener('focusin', handleFocusIn);
			document.addEventListener('focusout', handleFocusOut);
			document.addEventListener('pointerout', handlePointerOut);
			document.addEventListener('pointerover', handlePointerOver);

			return () => {
				document.removeEventListener('focusin', handleFocusIn);
				document.removeEventListener('focusout', handleFocusOut);
				document.removeEventListener('pointerout', handlePointerOut);
				document.removeEventListener('pointerover', handlePointerOver);

				clearTimeout(hideTimeoutRef.current);

				hideTimeoutRef.current = undefined;
				focusInsideRef.current = false;
				pointerInsideRef.current = false;

				setRevealed(false);
			};
		},
		[enabled, navigationId, panelRef]
	);

	const peek = enabled && revealed && !open;

	useEffect(
		function hideOnEscape() {
			if (!peek) {
				return;
			}

			function handleKeyDown(event: KeyboardEvent) {
				if (event.key !== 'Escape') {
					return;
				}

				clearTimeout(hideTimeoutRef.current);

				hideTimeoutRef.current = undefined;
				dismissedRef.current = pointerInsideRef.current;
				focusInsideRef.current = false;

				setRevealed(false);
			}

			document.addEventListener('keydown', handleKeyDown);

			return () => document.removeEventListener('keydown', handleKeyDown);
		},
		[peek]
	);

	return peek;
}
