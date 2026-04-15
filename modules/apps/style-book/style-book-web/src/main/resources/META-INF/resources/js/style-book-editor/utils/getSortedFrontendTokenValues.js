/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const PRIORITY_LEGACY = 300;

export function getSortedFrontendTokenValues(
	frontendTokensValues,
	frontendTokenDefinitions
) {
	const tokenDefinitionPriorities = Object.fromEntries(
		frontendTokenDefinitions.map(({id, priority}) => [id, priority])
	);

	return Object.values(frontendTokensValues).sort(
		(a, b) =>
			(tokenDefinitionPriorities[a.tokenDefinitionId] ?? PRIORITY_LEGACY) -
			(tokenDefinitionPriorities[b.tokenDefinitionId] ?? PRIORITY_LEGACY)
	);
}
