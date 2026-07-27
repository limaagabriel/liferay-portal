/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link StyleBookTokenService}.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenService
 * @generated
 */
public class StyleBookTokenServiceWrapper
	implements ServiceWrapper<StyleBookTokenService>, StyleBookTokenService {

	public StyleBookTokenServiceWrapper() {
		this(null);
	}

	public StyleBookTokenServiceWrapper(
		StyleBookTokenService styleBookTokenService) {

		_styleBookTokenService = styleBookTokenService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _styleBookTokenService.getOSGiServiceIdentifier();
	}

	@Override
	public StyleBookTokenService getWrappedService() {
		return _styleBookTokenService;
	}

	@Override
	public void setWrappedService(StyleBookTokenService styleBookTokenService) {
		_styleBookTokenService = styleBookTokenService;
	}

	private StyleBookTokenService _styleBookTokenService;

}
// LIFERAY-SERVICE-BUILDER-HASH:684718440