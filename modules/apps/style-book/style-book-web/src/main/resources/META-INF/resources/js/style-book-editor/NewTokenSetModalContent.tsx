/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayForm, {ClayInput} from '@clayui/form';
import ClayModal from '@clayui/modal';
import {FieldBase} from 'frontend-js-components-web';
import React, {useState} from 'react';

export interface NewFrontendTokenSet {
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
	const [errorMessage, setErrorMessage] = useState('');
	const [name, setName] = useState('');

	const validateName = (name: string) => {
		let errorMessage = '';

		if (!name.trim()) {
			errorMessage = Liferay.Language.get('this-field-is-required');
		}
		else if (existingTokenSetNames.includes(name)) {
			errorMessage = Liferay.Language.get(
				'a-token-set-with-that-name-already-exists'
			);
		}

		setErrorMessage(errorMessage);

		return errorMessage;
	};

	const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (validateName(name)) {
			return;
		}

		onSuccess({name});
		closeModal();
	};

	const formId = `${namespace}newTokenSetForm`;
	const nameId = `${namespace}newTokenSetName`;

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
						className="mb-0"
						errorMessage={errorMessage}
						id={nameId}
						label={Liferay.Language.get('name')}
						required
					>
						<ClayInput
							id={nameId}
							onChange={(event) => {
								const name = event.target.value;

								setName(name);

								validateName(name);
							}}
							value={name}
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
							disabled={Boolean(errorMessage)}
							displayType="primary"
							form={formId}
							type="submit"
						>
							{Liferay.Language.get('create-token-set')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</>
	);
};

export default NewTokenSetModalContent;
