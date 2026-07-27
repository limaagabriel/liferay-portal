/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.exception;

import com.liferay.portal.kernel.exception.DuplicateExternalReferenceCodeException;

/**
 * @author Brian Wing Shun Chan
 */
public class DuplicateStyleBookTokenExternalReferenceCodeException
	extends DuplicateExternalReferenceCodeException {

	public DuplicateStyleBookTokenExternalReferenceCodeException() {
	}

	public DuplicateStyleBookTokenExternalReferenceCodeException(String msg) {
		super(msg);
	}

	public DuplicateStyleBookTokenExternalReferenceCodeException(
		String msg, Throwable throwable) {

		super(msg, throwable);
	}

	public DuplicateStyleBookTokenExternalReferenceCodeException(
		Throwable throwable) {

		super(throwable);
	}

}