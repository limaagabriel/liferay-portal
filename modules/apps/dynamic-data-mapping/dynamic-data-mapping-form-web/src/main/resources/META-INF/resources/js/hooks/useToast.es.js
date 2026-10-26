/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import {openToast} from 'frontend-js-components-web';
import React from 'react';

function addToast({action, displayType, message, title}) {
	openToast({
		message,
		title,
		toastProps: action
			? {actions: <ClayButton.Group>{action}</ClayButton.Group>}
			: undefined,
		type: displayType,
	});
}

export function useToast() {
	return addToast;
}
