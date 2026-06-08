/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.style.book.constants.StyleBookActionKeys;
import com.liferay.style.book.constants.StyleBookConstants;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.model.StyleBookToken;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.base.StyleBookTokenServiceBaseImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Lima
 */
@Component(
	property = {
		"json.web.service.context.name=stylebook",
		"json.web.service.context.path=StyleBookToken"
	},
	service = AopService.class
)
public class StyleBookTokenServiceImpl extends StyleBookTokenServiceBaseImpl {

	@Override
	public StyleBookToken addStyleBookToken(
			String externalReferenceCode, String description,
			String frontendTokenCategoryName, String frontendTokenSetName,
			String name, long styleBookEntryId, String themeId, String type,
			String value)
		throws PortalException {

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.getStyleBookEntry(styleBookEntryId);

		_portletResourcePermission.check(
			getPermissionChecker(), styleBookEntry.getGroupId(),
			StyleBookActionKeys.MANAGE_STYLE_BOOK_ENTRIES);

		return styleBookTokenLocalService.addStyleBookToken(
			externalReferenceCode, getUserId(), description,
			frontendTokenCategoryName, frontendTokenSetName, name,
			styleBookEntryId, themeId, type, value);
	}

	@Reference(
		target = "(resource.name=" + StyleBookConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

}