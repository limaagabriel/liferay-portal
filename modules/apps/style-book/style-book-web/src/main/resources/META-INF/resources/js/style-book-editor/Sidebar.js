/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayDropDown, {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {openModal} from 'frontend-js-components-web';
import React, {useEffect, useMemo, useRef, useState} from 'react';

import FrontendTokenSet from './FrontendTokenSet';
import NewTokenModalContent from './NewTokenModalContent';
import {config} from './config';
import {
	useFrontendTokenDefinitions,
	useFrontendTokensValues,
	useSetFrontendTokenDefinitions,
} from './contexts/StyleBookEditorContext';
import {getFrontendTokens} from './utils/getFrontendTokens';

export default React.memo(function Sidebar() {
	const sidebarRef = useRef();
	const frontendTokenDefinitions = useFrontendTokenDefinitions();
	const [activeDefinitionId, setActiveDefinitionId] = useState(
		config.themeFrontendTokenDefinitionId
	);

	const activeDefinition = useMemo(
		() =>
			frontendTokenDefinitions.find(
				(definition) => definition.id === activeDefinitionId
			),
		[activeDefinitionId, frontendTokenDefinitions]
	);

	return (
		<div className="style-book-editor__sidebar" ref={sidebarRef}>
			<div
				className="panel-group-sm style-book-editor__sidebar-content"
				data-qa-id="styleBookEditorSidebarContent"
			>
				{!!frontendTokenDefinitions.length && (
					<TokenDefinitionSelector
						activeDefinitionId={activeDefinitionId}
						setActiveDefinitionId={setActiveDefinitionId}
					/>
				)}

				{activeDefinition?.frontendTokenCategories ? (
					<>
						<FrontendTokenCategories
							activeDefinition={activeDefinition}
						/>
						<UpdateStyle sidebarRef={sidebarRef} />
					</>
				) : (
					<ClayAlert className="m-3" displayType="info">
						{Liferay.Language.get(
							'this-theme-does-not-include-a-token-definition'
						)}
					</ClayAlert>
				)}
			</div>
		</div>
	);
});

function TokenDefinitionSelector({activeDefinitionId, setActiveDefinitionId}) {
	const frontendTokenDefinitions = useFrontendTokenDefinitions();
	const [active, setActive] = useState(false);

	const activeDefinition = frontendTokenDefinitions.find(
		(definition) => definition.id === activeDefinitionId
	);

	if (!activeDefinition) {
		return (
			<ClayAlert className="m-0" displayType="warning">
				{Liferay.Language.get(
					'the-current-theme-does-not-support-editing-style-book-values'
				)}
			</ClayAlert>
		);
	}

	if (frontendTokenDefinitions.length === 1) {
		return (
			<div className="mb-3 p-2">
				<TokenDefinitionInformation
					activeDefinition={activeDefinition}
				/>
			</div>
		);
	}

	return (
		<div className="mb-3">
			<ClayDropDown
				active={active}
				alignmentPosition={Align.BottomLeft}
				className="w-100"
				onActiveChange={setActive}
				trigger={
					<button
						aria-expanded={active}
						aria-haspopup="listbox"
						className="btn btn-unstyled p-2 style-book-editor__sidebar-theme-info-trigger text-left w-100"
						type="button"
					>
						<TokenDefinitionInformation
							activeDefinition={activeDefinition}
							isDropdownOpen={active}
						/>
					</button>
				}
			>
				<ClayDropDown.ItemList>
					{frontendTokenDefinitions.map((definition) => (
						<ClayDropDown.Item
							active={definition.id === activeDefinitionId}
							key={definition.id}
							onClick={() => {
								setActiveDefinitionId(definition.id);
								setActive(false);
							}}
						>
							{getDefinitionName(definition)}
						</ClayDropDown.Item>
					))}
				</ClayDropDown.ItemList>
			</ClayDropDown>
		</div>
	);
}

function UpdateStyle({sidebarRef}) {
	const frontendTokensValues = useFrontendTokensValues();

	useEffect(() => {
		if (sidebarRef.current) {
			sidebarRef.current.removeAttribute('style');

			for (const {
				cssVariableMapping,
				value,
			} of config.sortFrontendTokenValues(frontendTokensValues)) {
				sidebarRef.current.style.setProperty(
					`--${cssVariableMapping}`,
					value
				);
			}
		}
	}, [frontendTokensValues, sidebarRef]);

	return null;
}

function TokenDefinitionInformation({activeDefinition, isDropdownOpen}) {
	const frontendTokenDefinitions = useFrontendTokenDefinitions();

	return (
		<div className="small text-secondary">
			<div className="text-dark">
				<p className="font-weight-bold mb-1">
					{Liferay.Language.get(
						'frontend-token-definition-provided-by'
					)}
				</p>

				<p className="mb-0">
					{getDefinitionName(activeDefinition)}

					{frontendTokenDefinitions.length > 1 && (
						<span className="ml-1">
							<ClayIcon
								symbol={
									isDropdownOpen
										? 'caret-top'
										: 'caret-bottom'
								}
							/>
						</span>
					)}
				</p>
			</div>
		</div>
	);
}

function getDefinitionName({id, name}) {
	return id === config.themeFrontendTokenDefinitionId
		? config.themeName
		: name || id;
}

function FrontendTokenCategories({activeDefinition}) {
	const setFrontendTokenDefinitions = useSetFrontendTokenDefinitions();
	const frontendTokenDefinitions = useFrontendTokenDefinitions();
	const frontendTokensValues = useFrontendTokensValues();
	const isThemeDefinition =
		activeDefinition.id === config.themeFrontendTokenDefinitionId;

	const frontendTokenCategories = activeDefinition.frontendTokenCategories;
	const [active, setActive] = useState(false);
	const [previousDefinitionId, setPreviousDefinitionId] = useState(
		activeDefinition.id
	);
	const [selectedCategory, setSelectedCategory] = useState(
		frontendTokenCategories[0]
	);

	// Reset the selected category only when the active definition itself
	// changes, not when its categories are replaced in place (e.g. after
	// creating a token, which updates frontendTokenCategories for the same
	// definition and should keep the current selection).

	if (activeDefinition.id !== previousDefinitionId) {
		setPreviousDefinitionId(activeDefinition.id);
		setSelectedCategory(frontendTokenCategories[0]);
	}

	const frontendTokens = useMemo(
		() =>
			getFrontendTokens(
				frontendTokenDefinitions,
				config.themeFrontendTokenDefinitionId
			),
		[frontendTokenDefinitions]
	);

	const tokenValues = useMemo(() => {
		const nextTokenValues = {...frontendTokens};

		for (const [name, {value}] of Object.entries(frontendTokensValues)) {
			if (nextTokenValues[name]) {
				nextTokenValues[name] = {
					...nextTokenValues[name],
					value: value || nextTokenValues[name].defaultValue,
				};
			}
		}

		return nextTokenValues;
	}, [frontendTokens, frontendTokensValues]);

	// A join()'d key (rather than frontendTokensValues itself) so that saving
	// a non-custom token value doesn't invalidate this memo, and edits to a
	// custom token's value (but not its presence) don't either.

	const customFrontendTokenNamesKey = useMemo(() => {
		const prefix = `${config.customTokenDefinitionId}:`;

		return Object.keys(frontendTokensValues)
			.filter(
				(name) => name.startsWith(prefix) && frontendTokensValues[name]
			)
			.map((name) => name.slice(prefix.length))
			.sort()
			.join(',');
	}, [frontendTokensValues]);

	const customFrontendTokenNames = useMemo(
		() =>
			new Set(
				customFrontendTokenNamesKey
					? customFrontendTokenNamesKey.split(',')
					: []
			),
		[customFrontendTokenNamesKey]
	);

	const frontendTokenCategoriesWithPrefix = useMemo(() => {
		return frontendTokenCategories.map((category) => ({
			...category,
			frontendTokenSets: category.frontendTokenSets.map((tokenSet) => ({
				...tokenSet,
				frontendTokens: tokenSet.frontendTokens.map((token) => {
					const custom = customFrontendTokenNames.has(token.name);

					return {
						...token,
						custom,
						name: `${activeDefinition.id}:${token.name}`,
						tokenDefinitionId: custom
							? config.customTokenDefinitionId
							: activeDefinition.id,
					};
				}),
			})),
		}));
	}, [activeDefinition, frontendTokenCategories, customFrontendTokenNames]);

	const activeSelectedCategory = useMemo(() => {
		if (!selectedCategory) {
			return frontendTokenCategoriesWithPrefix[0];
		}

		return frontendTokenCategoriesWithPrefix.find(
			(category) => category.name === selectedCategory.name
		);
	}, [selectedCategory, frontendTokenCategoriesWithPrefix]);

	const openNewTokenModal = () => {
		openModal({
			contentComponent: ({closeModal}) => (
				<NewTokenModalContent
					addFrontendTokenURL={config.addFrontendTokenURL}
					categoryName={activeSelectedCategory.name}
					closeModal={closeModal}
					namespace={config.namespace}
					onSuccess={setFrontendTokenDefinitions}
					styleBookEntryId={config.styleBookEntryId}
					tokenSets={activeSelectedCategory.frontendTokenSets.map(
						({label, name}) => ({label, name})
					)}
				/>
			),
		});
	};

	return (
		<>
			{activeSelectedCategory && (
				<div className="align-items-center d-flex mb-4">
					<ClayDropDown
						active={active}
						alignmentPosition={Align.BottomLeft}
						className="flex-grow-1 mr-2"
						menuElementAttrs={{
							containerProps: {
								className: 'cadmin',
							},
						}}
						onActiveChange={setActive}
						trigger={
							<ClayButton
								className="form-control form-control-select form-control-sm text-left"
								displayType="secondary"
								size="sm"
								type="button"
							>
								{activeSelectedCategory.label}
							</ClayButton>
						}
					>
						<ClayDropDown.ItemList>
							{frontendTokenCategoriesWithPrefix.map(
								(frontendTokenCategory, index) => (
									<ClayDropDown.Item
										key={index}
										onClick={() => {
											setSelectedCategory(
												frontendTokenCategory
											);
											setActive(false);
										}}
									>
										{frontendTokenCategory.label}
									</ClayDropDown.Item>
								)
							)}
						</ClayDropDown.ItemList>
					</ClayDropDown>

					{isThemeDefinition && (
						<ClayButton
							displayType="secondary"
							onClick={openNewTokenModal}
							size="sm"
						>
							<ClayIcon
								className="inline-item inline-item-before"
								symbol="plus"
							/>

							{Liferay.Language.get('new-token')}
						</ClayButton>
					)}
				</div>
			)}

			{activeSelectedCategory?.frontendTokenSets.map(
				({frontendTokens, label, name}, index) => (
					<FrontendTokenSet
						frontendTokens={frontendTokens}
						key={name}
						label={label}
						open={index === 0}
						tokenValues={tokenValues}
					/>
				)
			)}
		</>
	);
}
