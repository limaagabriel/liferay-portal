/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {openToast} from 'frontend-js-components-web';
import {escapeHTML} from 'frontend-js-web';
import {useEffect} from 'react';

export default function Alert({displayType = 'danger', info}) {
	useEffect(() => {
		if (info && (info.title || info.message)) {
			openToast({
				message: escapeHTML(info.message || info.title),
				title: info.title && info.message ? escapeHTML(info.title) : '',
				type: displayType,
			});
		}
	}, [displayType, info]);

	return null;
}
