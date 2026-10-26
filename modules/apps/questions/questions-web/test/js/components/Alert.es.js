/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {cleanup} from '@testing-library/react';
import {openToast} from 'frontend-js-components-web';
import React from 'react';

import Alert from '../../../src/main/resources/META-INF/resources/js/components/Alert.es';
import {renderComponent} from '../../helpers.es';

import '@testing-library/jest-dom';

jest.mock('frontend-js-components-web', () => ({
	openToast: jest.fn(),
}));

describe('Alert', () => {
	afterEach(cleanup);

	it('renders alert with its title and message', () => {
		const info = {
			message: 'Info message',
			title: 'Info title',
		};

		renderComponent({
			ui: <Alert info={info} />,
		});

		expect(openToast).toHaveBeenCalledWith({
			message: info.message,
			title: info.title,
			type: 'danger',
		});
	});
});
