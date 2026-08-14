/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import {Option, Picker} from '@clayui/core';
import ClayForm, {ClayInput, ClaySelectWithOption} from '@clayui/form';
import ClayModal from '@clayui/modal';
import {FieldBase} from 'frontend-js-components-web';
import {fetch, objectToFormData} from 'frontend-js-web';
import React, {useState} from 'react';

interface FrontendTokenSetOption {
	label: string;
	name: string;
}

interface NewTokenModalContentProps {
	addFrontendTokenURL: string;
	categoryName: string;
	closeModal: () => void;
	namespace: string;
	onSuccess: (frontendTokenDefinitions: unknown[]) => void;
	styleBookEntryId: number;
	tokenSets: FrontendTokenSetOption[];
}

const EDITOR_TYPE_OPTIONS: {label: string; value: string}[] = [
	{label: Liferay.Language.get('default'), value: 'Default'},
	{label: Liferay.Language.get('color-picker'), value: 'ColorPicker'},
	{label: Liferay.Language.get('length'), value: 'Length'},
];

const NewTokenModalContent = ({
	addFrontendTokenURL,
	categoryName,
	closeModal,
	namespace,
	onSuccess,
	styleBookEntryId,
	tokenSets,
}: NewTokenModalContentProps) => {
	const [description, setDescription] = useState('');
	const [editorType, setEditorType] = useState(EDITOR_TYPE_OPTIONS[0].value);
	const [errorMessage, setErrorMessage] = useState('');
	const [label, setLabel] = useState('');
	const [loading, setLoading] = useState(false);
	const [tokenSetName, setTokenSetName] = useState<React.Key>(
		tokenSets[0]?.name ?? ''
	);
	const [value, setValue] = useState('');

	const validateLabel = (label: string) => {
		const errorMessage = !label.trim()
			? Liferay.Language.get('this-field-is-required')
			: '';

		setErrorMessage(errorMessage);

		return errorMessage;
	};

	const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (validateLabel(label)) {
			return;
		}

		setLoading(true);

		const body = Liferay.Util.ns(namespace, {
			categoryName,
			description,
			editorType,
			label,
			styleBookEntryId,
			tokenSetName,
			value,
		});

		fetch(addFrontendTokenURL, {
			body: objectToFormData(body),
			method: 'POST',
		})
			.then((response) => response.json())
			.then(({error, frontendTokenDefinitions}) => {
				if (error) {
					setErrorMessage(error);
					setLoading(false);
				}
				else {
					onSuccess(frontendTokenDefinitions);
					closeModal();
				}
			})
			.catch((error) => {
				setErrorMessage(
					error?.error ||
						Liferay.Language.get('an-unexpected-error-occurred')
				);
				setLoading(false);
			});
	};

	const descriptionId = `${namespace}newTokenDescription`;
	const editorTypeId = `${namespace}newTokenEditorType`;
	const formId = `${namespace}newTokenForm`;
	const labelId = `${namespace}newTokenLabel`;
	const tokenSetId = `${namespace}newTokenSet`;
	const valueId = `${namespace}newTokenValue`;

	return (
		<>
			<ClayModal.Header
				closeButtonAriaLabel={Liferay.Language.get('close')}
			>
				{Liferay.Language.get('new-custom-token')}
			</ClayModal.Header>

			<ClayModal.Body>
				<ClayForm id={formId} onSubmit={handleSubmit}>
					<FieldBase
						errorMessage={errorMessage}
						id={labelId}
						label={Liferay.Language.get('token-name')}
						required
					>
						<ClayInput
							id={labelId}
							onChange={(event) => {
								const label = event.target.value;

								setLabel(label);

								validateLabel(label);
							}}
							value={label}
						/>
					</FieldBase>

					<FieldBase
						id={editorTypeId}
						label={Liferay.Language.get('editor-type')}
						required
					>
						<ClaySelectWithOption
							id={editorTypeId}
							onChange={(event) =>
								setEditorType(event.target.value)
							}
							options={EDITOR_TYPE_OPTIONS}
							value={editorType}
						/>
					</FieldBase>

					<FieldBase
						id={valueId}
						label={Liferay.Language.get('value')}
					>
						<ClayInput
							id={valueId}
							onChange={(event) => setValue(event.target.value)}
							placeholder="#FFF456"
							value={value}
						/>
					</FieldBase>

					<FieldBase
						id={tokenSetId}
						label={Liferay.Language.get('token-set')}
						required
					>
						<Picker
							id={tokenSetId}
							items={tokenSets}
							messages={{
								itemDescribedby: Liferay.Language.get(
									'you-are-currently-on-a-text-element,-inside-of-a-list-box'
								),
								itemSelected:
									Liferay.Language.get('x-selected'),
								scrollToBottomAriaLabel:
									Liferay.Language.get('scroll-to-bottom'),
								scrollToTopAriaLabel:
									Liferay.Language.get('scroll-to-top'),
							}}
							onSelectionChange={setTokenSetName}
							selectedKey={tokenSetName}
						>
							{(item) => (
								<Option key={item.name} textValue={item.label}>
									{item.label}
								</Option>
							)}
						</Picker>
					</FieldBase>

					<FieldBase
						className="mb-0"
						id={descriptionId}
						label={Liferay.Language.get('description')}
					>
						<ClayInput
							component="textarea"
							id={descriptionId}
							onChange={(event) =>
								setDescription(event.target.value)
							}
							value={description}
						/>
					</FieldBase>
				</ClayForm>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton
							displayType="secondary"
							onClick={closeModal}
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton
							aria-busy={loading}
							disabled={Boolean(errorMessage)}
							displayType="primary"
							form={formId}
							type="submit"
						>
							{loading && (
								<span className="inline-item inline-item-before">
									<span
										aria-hidden="true"
										className="loading-animation"
									></span>
								</span>
							)}

							{Liferay.Language.get('create-token')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</>
	);
};

export default NewTokenModalContent;
