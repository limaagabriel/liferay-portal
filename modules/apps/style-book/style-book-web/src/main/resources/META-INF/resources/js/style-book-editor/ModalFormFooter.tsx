/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayModal from '@clayui/modal';
import React from 'react';

interface ModalFormFooterProps {
	closeModal: () => void;
	disabled?: boolean;
	formId: string;
	loading?: boolean;
	submitLabel: string;
}

const ModalFormFooter = ({
	closeModal,
	disabled,
	formId,
	loading,
	submitLabel,
}: ModalFormFooterProps) => (
	<ClayModal.Footer
		last={
			<ClayButton.Group spaced>
				<ClayButton displayType="secondary" onClick={closeModal}>
					{Liferay.Language.get('cancel')}
				</ClayButton>

				<ClayButton
					aria-busy={loading}
					disabled={disabled}
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

					{submitLabel}
				</ClayButton>
			</ClayButton.Group>
		}
	/>
);

export default ModalFormFooter;
