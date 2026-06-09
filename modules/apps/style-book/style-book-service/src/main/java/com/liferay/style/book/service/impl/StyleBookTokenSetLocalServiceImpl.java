/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.impl;

import com.liferay.frontend.token.definition.FrontendTokenCategory;
import com.liferay.frontend.token.definition.FrontendTokenDefinitionRegistry;
import com.liferay.frontend.token.definition.FrontendTokenSet;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.style.book.exception.DuplicateStyleBookTokenSetNameException;
import com.liferay.style.book.exception.NoSuchTokenSetException;
import com.liferay.style.book.exception.StyleBookTokenSetFrontendTokenCategoryNameException;
import com.liferay.style.book.exception.StyleBookTokenSetNameException;
import com.liferay.style.book.internal.frontend.token.FrontendTokenDefinitionUtil;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.model.StyleBookTokenSet;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.base.StyleBookTokenSetLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Lima
 */
@Component(
	property = "model.class.name=com.liferay.style.book.model.StyleBookTokenSet",
	service = AopService.class
)
public class StyleBookTokenSetLocalServiceImpl
	extends StyleBookTokenSetLocalServiceBaseImpl {

	@Override
	public StyleBookTokenSet addStyleBookTokenSet(
			String externalReferenceCode, long userId, long styleBookEntryId,
			String description, String frontendTokenCategoryName, String name,
			String themeId)
		throws PortalException {

		_validateName(name);

		_validateDuplicateStyleBookTokenSet(
			frontendTokenCategoryName, name, styleBookEntryId, themeId);

		User user = _userLocalService.getUser(userId);

		FrontendTokenSet frontendTokenSet =
			FrontendTokenDefinitionUtil.fetchFrontendTokenSet(
				_getFrontendTokenCategory(
					user.getCompanyId(), frontendTokenCategoryName, themeId),
				name);

		if (frontendTokenSet != null) {
			throw new DuplicateStyleBookTokenSetNameException(
				"Style book token set name \"" + name + "\" already exists");
		}

		return _addStyleBookTokenSet(
			externalReferenceCode, user, description, frontendTokenCategoryName,
			name, styleBookEntryId, themeId);
	}

	@Override
	public StyleBookTokenSet getOrAddStaticStyleBookTokenSet(
			long userId, String frontendTokenCategoryName, String name,
			long styleBookEntryId, String themeId)
		throws PortalException {

		StyleBookTokenSet styleBookTokenSet =
			styleBookTokenSetPersistence.fetchBySBEI_FTCN_N_T(
				styleBookEntryId, frontendTokenCategoryName, name, themeId);

		if (styleBookTokenSet != null) {
			return styleBookTokenSet;
		}

		_validateName(name);

		User user = _userLocalService.getUser(userId);

		FrontendTokenSet frontendTokenSet =
			FrontendTokenDefinitionUtil.fetchFrontendTokenSet(
				_getFrontendTokenCategory(
					user.getCompanyId(), frontendTokenCategoryName, themeId),
				name);

		if (frontendTokenSet == null) {
			throw new NoSuchTokenSetException(
				"Style book token set \"" + name + "\" does not exist");
		}

		return _addStyleBookTokenSet(
			null, user, StringPool.BLANK, frontendTokenCategoryName, name,
			styleBookEntryId, themeId);
	}

	private StyleBookTokenSet _addStyleBookTokenSet(
			String externalReferenceCode, User user, String description,
			String frontendTokenCategoryName, String name,
			long styleBookEntryId, String themeId)
		throws PortalException {

		StyleBookTokenSet styleBookTokenSet =
			styleBookTokenSetPersistence.create(
				counterLocalService.increment(
					StyleBookTokenSet.class.getName()));

		styleBookTokenSet.setExternalReferenceCode(externalReferenceCode);

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.getStyleBookEntry(styleBookEntryId);

		styleBookTokenSet.setGroupId(styleBookEntry.getGroupId());

		styleBookTokenSet.setCompanyId(user.getCompanyId());
		styleBookTokenSet.setUserId(user.getUserId());
		styleBookTokenSet.setUserName(user.getFullName());

		styleBookTokenSet.setStyleBookEntryId(styleBookEntryId);
		styleBookTokenSet.setDescription(description);
		styleBookTokenSet.setFrontendTokenCategoryName(
			frontendTokenCategoryName);
		styleBookTokenSet.setName(name);
		styleBookTokenSet.setThemeId(themeId);

		return styleBookTokenSetPersistence.update(styleBookTokenSet);
	}

	private FrontendTokenCategory _getFrontendTokenCategory(
			long companyId, String frontendTokenCategoryName, String themeId)
		throws PortalException {

		FrontendTokenCategory frontendTokenCategory =
			FrontendTokenDefinitionUtil.fetchFrontendTokenCategory(
				_frontendTokenDefinitionRegistry.getFrontendTokenDefinition(
					companyId, themeId),
				frontendTokenCategoryName);

		if (frontendTokenCategory == null) {
			throw new StyleBookTokenSetFrontendTokenCategoryNameException(
				"Frontend token category \"" + frontendTokenCategoryName +
					"\" does not exist");
		}

		return frontendTokenCategory;
	}

	private void _validateDuplicateStyleBookTokenSet(
			String frontendTokenCategoryName, String name,
			long styleBookEntryId, String themeId)
		throws PortalException {

		StyleBookTokenSet styleBookTokenSet =
			styleBookTokenSetPersistence.fetchBySBEI_FTCN_N_T(
				styleBookEntryId, frontendTokenCategoryName, name, themeId);

		if (styleBookTokenSet != null) {
			throw new DuplicateStyleBookTokenSetNameException(
				"Style book token set name \"" + name + "\" already exists");
		}
	}

	private void _validateName(String name) throws PortalException {
		if (Validator.isNull(name)) {
			throw new StyleBookTokenSetNameException("Name is null");
		}

		if (name.length() > ModelHintsUtil.getMaxLength(
				StyleBookTokenSet.class.getName(), "name")) {

			throw new StyleBookTokenSetNameException(
				"Maximum length of name exceeded");
		}
	}

	@Reference
	private FrontendTokenDefinitionRegistry _frontendTokenDefinitionRegistry;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Reference
	private UserLocalService _userLocalService;

}