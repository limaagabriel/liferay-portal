/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.impl;

import com.liferay.frontend.token.definition.FrontendToken;
import com.liferay.frontend.token.definition.FrontendTokenCategory;
import com.liferay.frontend.token.definition.FrontendTokenDefinitionRegistry;
import com.liferay.frontend.token.definition.FrontendTokenSet;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.style.book.exception.DuplicateStyleBookTokenNameException;
import com.liferay.style.book.exception.StyleBookTokenNameException;
import com.liferay.style.book.internal.frontend.token.FrontendTokenDefinitionUtil;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.model.StyleBookToken;
import com.liferay.style.book.model.StyleBookTokenSet;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.StyleBookTokenSetLocalService;
import com.liferay.style.book.service.base.StyleBookTokenLocalServiceBaseImpl;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Lima
 */
@Component(
	property = "model.class.name=com.liferay.style.book.model.StyleBookToken",
	service = AopService.class
)
public class StyleBookTokenLocalServiceImpl
	extends StyleBookTokenLocalServiceBaseImpl {

	@Override
	public StyleBookToken addStyleBookToken(
			String externalReferenceCode, long userId, String description,
			String frontendTokenCategoryName, String frontendTokenSetName,
			String name, long styleBookEntryId, String themeId, String type,
			String value)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		StyleBookTokenSet styleBookTokenSet =
			_styleBookTokenSetLocalService.getOrAddStaticStyleBookTokenSet(
				userId, frontendTokenCategoryName, frontendTokenSetName,
				styleBookEntryId, themeId);

		_validate(user.getCompanyId(), name, styleBookTokenSet);

		StyleBookToken styleBookToken = styleBookTokenPersistence.create(
			counterLocalService.increment(StyleBookToken.class.getName()));

		styleBookToken.setExternalReferenceCode(externalReferenceCode);

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.getStyleBookEntry(styleBookEntryId);

		styleBookToken.setGroupId(styleBookEntry.getGroupId());

		styleBookToken.setCompanyId(user.getCompanyId());
		styleBookToken.setUserId(user.getUserId());
		styleBookToken.setUserName(user.getFullName());

		styleBookToken.setStyleBookTokenSetId(
			styleBookTokenSet.getStyleBookTokenSetId());
		styleBookToken.setDescription(description);
		styleBookToken.setName(name);
		styleBookToken.setTokenKey(FriendlyURLNormalizerUtil.normalize(name));
		styleBookToken.setType(type);
		styleBookToken.setValue(value);

		return styleBookTokenPersistence.update(styleBookToken);
	}

	private FrontendToken _getFrontendToken(
		FrontendTokenSet frontendTokenSet, String tokenKey) {

		for (FrontendToken frontendToken :
				frontendTokenSet.getFrontendTokens()) {

			if (Objects.equals(
					tokenKey,
					FriendlyURLNormalizerUtil.normalize(
						frontendToken.getName()))) {

				return frontendToken;
			}
		}

		return null;
	}

	private void _validate(
			long companyId, String name, StyleBookTokenSet styleBookTokenSet)
		throws PortalException {

		if (Validator.isNull(name)) {
			throw new StyleBookTokenNameException("Name is null");
		}

		if (name.length() > ModelHintsUtil.getMaxLength(
				StyleBookToken.class.getName(), "name")) {

			throw new StyleBookTokenNameException(
				"Name exceeds the maximum length");
		}

		String tokenKey = FriendlyURLNormalizerUtil.normalize(name);

		StyleBookToken styleBookToken =
			styleBookTokenPersistence.fetchBySBTSI_TK(
				styleBookTokenSet.getStyleBookTokenSetId(), tokenKey);

		if (styleBookToken != null) {
			throw new DuplicateStyleBookTokenNameException(
				"Style book token name \"" + name + "\" already exists");
		}

		FrontendTokenCategory frontendTokenCategory =
			FrontendTokenDefinitionUtil.fetchFrontendTokenCategory(
				_frontendTokenDefinitionRegistry.getFrontendTokenDefinition(
					companyId, styleBookTokenSet.getThemeId()),
				styleBookTokenSet.getFrontendTokenCategoryName());

		if (frontendTokenCategory == null) {
			return;
		}

		FrontendTokenSet frontendTokenSet =
			FrontendTokenDefinitionUtil.fetchFrontendTokenSet(
				frontendTokenCategory, styleBookTokenSet.getName());

		if (frontendTokenSet == null) {
			return;
		}

		if (_getFrontendToken(frontendTokenSet, tokenKey) != null) {
			throw new DuplicateStyleBookTokenNameException(
				"Style book token name \"" + name + "\" already exists");
		}
	}

	@Reference
	private FrontendTokenDefinitionRegistry _frontendTokenDefinitionRegistry;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Reference
	private StyleBookTokenSetLocalService _styleBookTokenSetLocalService;

	@Reference
	private UserLocalService _userLocalService;

}