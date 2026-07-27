/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.style.book.model.StyleBookToken;

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

	@Override
	public StyleBookToken addStyleBookToken(
			String externalReferenceCode, String description,
			String frontendTokenCategoryName, String frontendTokenSetName,
			String name, long styleBookEntryId, String themeId, String type,
			String value)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenService.addStyleBookToken(
			externalReferenceCode, description, frontendTokenCategoryName,
			frontendTokenSetName, name, styleBookEntryId, themeId, type, value);
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
// LIFERAY-SERVICE-BUILDER-HASH:1008534087