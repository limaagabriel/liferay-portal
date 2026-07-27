/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.style.book.model.StyleBookToken;

/**
 * Provides the remote service utility for StyleBookToken. This utility wraps
 * <code>com.liferay.style.book.service.impl.StyleBookTokenServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenService
 * @generated
 */
public class StyleBookTokenServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.style.book.service.impl.StyleBookTokenServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static StyleBookToken addStyleBookToken(
			String externalReferenceCode, String description,
			String frontendTokenCategoryName, String frontendTokenSetName,
			String name, long styleBookEntryId, String themeId, String type,
			String value)
		throws PortalException {

		return getService().addStyleBookToken(
			externalReferenceCode, description, frontendTokenCategoryName,
			frontendTokenSetName, name, styleBookEntryId, themeId, type, value);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static StyleBookTokenService getService() {
		return _serviceSnapshot.get();
	}

	private static final Snapshot<StyleBookTokenService> _serviceSnapshot =
		new Snapshot<>(
			StyleBookTokenServiceUtil.class, StyleBookTokenService.class);

}
// LIFERAY-SERVICE-BUILDER-HASH:1134093205