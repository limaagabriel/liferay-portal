/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayForm, {ClayInput} from '@clayui/form';
import ClayModal from '@clayui/modal';
import {FieldBase} from 'frontend-js-components-web';
import React, {useState} from 'react';

import ModalFormFooter from './ModalFormFooter';

export interface NewFrontendTokenSet {
	description: string;
	label: string;
	name: string;
}

interface NewTokenSetModalContentProps {
	closeModal: () => void;
	existingTokenSetNames: string[];
	namespace: string;
	onSuccess: (frontendTokenSet: NewFrontendTokenSet) => void;
}

const NewTokenSetModalContent = ({
	closeModal,
	existingTokenSetNames,
	namespace,
	onSuccess,
}: NewTokenSetModalContentProps) => {
	const [description, setDescription] = useState('');
	const [errorMessage, setErrorMessage] = useState('');
	const [label, setLabel] = useState('');

	const validateLabel = (label: string) => {
		let errorMessage = '';

		if (!label.trim()) {
			errorMessage = Liferay.Language.get('this-field-is-required');
		}
		else if (existingTokenSetNames.includes(label)) {
			errorMessage = Liferay.Language.get(
				'a-token-set-with-that-label-already-exists'
			);
		}

		setErrorMessage(errorMessage);

		return errorMessage;
	};

	const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (validateLabel(label)) {
			return;
		}

		onSuccess({description, label, name: label});
		closeModal();
	};

	const descriptionId = `${namespace}newTokenSetDescription`;
	const formId = `${namespace}newTokenSetForm`;
	const labelId = `${namespace}newTokenSetLabel`;

	return (
		<>
			<ClayModal.Header
				closeButtonAriaLabel={Liferay.Language.get('close')}
			>
				{Liferay.Language.get('new-token-set')}
			</ClayModal.Header>

			<ClayModal.Body>
				<ClayForm id={formId} onSubmit={handleSubmit}>
					<FieldBase
						errorMessage={errorMessage}
						id={labelId}
						label={Liferay.Language.get('label')}
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

			<ModalFormFooter
				closeModal={closeModal}
				disabled={Boolean(errorMessage)}
				formId={formId}
				submitLabel={Liferay.Language.get('create-token-set')}
			/>
		</>
	);
};

export default NewTokenSetModalContent;
