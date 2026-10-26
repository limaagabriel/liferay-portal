/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {openToast} from 'frontend-js-components-web';
import {escapeHTML} from 'frontend-js-web';

const showToast = (type, message, title, autoClose) => {
	openToast({
		autoClose,
		message: message && escapeHTML(message),
		title: title ? escapeHTML(title) : '',
		type,
	});
};

const toaster = {
	danger: (message, title, autoClose = 5000) => {
		showToast(
			'danger',
			message || Liferay.Language.get('error'),
			title || `${Liferay.Language.get('error')}:`,
			autoClose
		);
	},
	info: (message, title, autoClose = 5000) => {
		showToast('info', message, title, autoClose);
	},
	success: (message, title, autoClose = 5000) => {
		showToast(
			'success',
			message || Liferay.Language.get('success'),
			title || `${Liferay.Language.get('success')}:`,
			autoClose
		);
	},
	warning: (message, title, autoClose = 5000) => {
		showToast('warning', message, title, autoClose);
	},
};

const useToaster = () => toaster;

export {useToaster};
