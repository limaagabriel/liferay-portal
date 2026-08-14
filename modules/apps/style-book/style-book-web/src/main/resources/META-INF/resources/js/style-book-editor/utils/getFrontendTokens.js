/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

export function getFrontendTokens(
	frontendTokenDefinitions,
	themeFrontendTokenDefinitionId
) {
	const tokens = {};

	frontendTokenDefinitions.forEach((definition) => {
		const {frontendTokenCategories, id: definitionId} = definition;

		if (!frontendTokenCategories) {
			return;
		}

		for (const category of frontendTokenCategories) {
			for (const tokenSet of category.frontendTokenSets) {
				for (const token of tokenSet.frontendTokens) {
					const namespacedName = `${definitionId}:${token.name}`;

					const tokenData = {
						...token,
						name: namespacedName,
						tokenCategoryLabel: category.label,
						tokenSetLabel: tokenSet.label,
						value: token.defaultValue,
					};

					tokens[namespacedName] = tokenData;

					if (definitionId === themeFrontendTokenDefinitionId) {
						tokens[token.name] = {
							...tokenData,
							name: token.name,
						};
					}
				}
			}
		}
	});

	return tokens;
}
