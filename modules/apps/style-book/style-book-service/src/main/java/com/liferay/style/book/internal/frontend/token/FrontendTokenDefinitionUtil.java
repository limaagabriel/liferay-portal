/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.internal.frontend.token;

import com.liferay.frontend.token.definition.FrontendTokenCategory;
import com.liferay.frontend.token.definition.FrontendTokenDefinition;
import com.liferay.frontend.token.definition.FrontendTokenSet;

import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 */
public class FrontendTokenDefinitionUtil {

	public static FrontendTokenCategory fetchFrontendTokenCategory(
		FrontendTokenDefinition frontendTokenDefinition, String name) {

		if (frontendTokenDefinition == null) {
			return null;
		}

		for (FrontendTokenCategory frontendTokenCategory :
				frontendTokenDefinition.getFrontendTokenCategories()) {

			if (Objects.equals(name, frontendTokenCategory.getName())) {
				return frontendTokenCategory;
			}
		}

		return null;
	}

	public static FrontendTokenSet fetchFrontendTokenSet(
		FrontendTokenCategory frontendTokenCategory, String name) {

		if (frontendTokenCategory == null) {
			return null;
		}

		for (FrontendTokenSet frontendTokenSet :
				frontendTokenCategory.getFrontendTokenSets()) {

			if (Objects.equals(name, frontendTokenSet.getName())) {
				return frontendTokenSet;
			}
		}

		return null;
	}

}