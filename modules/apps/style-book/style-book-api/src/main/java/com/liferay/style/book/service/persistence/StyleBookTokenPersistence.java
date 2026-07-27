/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.persistence;

import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.style.book.exception.NoSuchTokenException;
import com.liferay.style.book.model.StyleBookToken;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the style book token service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenUtil
 * @generated
 */
@ProviderType
public interface StyleBookTokenPersistence
	extends BasePersistence<StyleBookToken>, CTPersistence<StyleBookToken> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link StyleBookTokenUtil} to access the style book token persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public java.util.List<StyleBookToken> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
				orderByComparator)
		throws NoSuchTokenException;

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator);

	/**
	 * Removes all the style book tokens where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of style book tokens where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching style book tokens
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findByUUID_G(String uuid, long groupId)
		throws NoSuchTokenException;

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache);

	/**
	 * Removes the style book token where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	public StyleBookToken removeByUUID_G(String uuid, long groupId)
		throws NoSuchTokenException;

	/**
	 * Returns the number of style book tokens where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	public int countByUUID_G(String uuid, long groupId);

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public java.util.List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
				orderByComparator)
		throws NoSuchTokenException;

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator);

	/**
	 * Removes all the style book tokens where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching style book tokens
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns an ordered range of all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public java.util.List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findByStyleBookTokenSetId_First(
			long styleBookTokenSetId,
			com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
				orderByComparator)
		throws NoSuchTokenException;

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchByStyleBookTokenSetId_First(
		long styleBookTokenSetId,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator);

	/**
	 * Removes all the style book tokens where styleBookTokenSetId = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 */
	public void removeByStyleBookTokenSetId(long styleBookTokenSetId);

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @return the number of matching style book tokens
	 */
	public int countByStyleBookTokenSetId(long styleBookTokenSetId);

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws NoSuchTokenException;

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey, boolean useFinderCache);

	/**
	 * Removes the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the style book token that was removed
	 */
	public StyleBookToken removeBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws NoSuchTokenException;

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63; and tokenKey = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the number of matching style book tokens
	 */
	public int countBySBTSI_TK(long styleBookTokenSetId, String tokenKey);

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public StyleBookToken findByERC_G(
			String externalReferenceCode, long groupId)
		throws NoSuchTokenException;

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public StyleBookToken fetchByERC_G(
		String externalReferenceCode, long groupId, boolean useFinderCache);

	/**
	 * Removes the style book token where externalReferenceCode = &#63; and groupId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	public StyleBookToken removeByERC_G(
			String externalReferenceCode, long groupId)
		throws NoSuchTokenException;

	/**
	 * Returns the number of style book tokens where externalReferenceCode = &#63; and groupId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	public int countByERC_G(String externalReferenceCode, long groupId);

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	public StyleBookToken create(long styleBookTokenId);

	/**
	 * Removes the style book token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token that was removed
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	public StyleBookToken remove(long styleBookTokenId)
		throws NoSuchTokenException;

	public StyleBookToken updateImpl(StyleBookToken styleBookToken);

	/**
	 * Returns the style book token with the primary key or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	public StyleBookToken findByPrimaryKey(long styleBookTokenId)
		throws NoSuchTokenException;

	/**
	 * Returns the style book token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token, or <code>null</code> if a style book token with the primary key could not be found
	 */
	public StyleBookToken fetchByPrimaryKey(long styleBookTokenId);

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public default StyleBookToken fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public default StyleBookToken fetchBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey) {

		return fetchBySBTSI_TK(styleBookTokenSetId, tokenKey, true);
	}

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public default StyleBookToken fetchByERC_G(
		String externalReferenceCode, long groupId) {

		return fetchByERC_G(externalReferenceCode, groupId, true);
	}

	/**
	 * Returns all the style book tokens where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid(String uuid) {
		return findByUuid(
			uuid, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS,
			com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null, true);
	}

	/**
	 * Returns a range of all the style book tokens where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @return the range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null, true);
	}

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid_C(
		String uuid, long companyId) {

		return findByUuid_C(
			uuid, companyId,
			com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS,
			com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null, true);
	}

	/**
	 * Returns a range of all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @return the range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null, true);
	}

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @return the matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId) {

		return findByStyleBookTokenSetId(
			styleBookTokenSetId,
			com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS,
			com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null, true);
	}

	/**
	 * Returns a range of all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @return the range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end) {

		return findByStyleBookTokenSetId(
			styleBookTokenSetId, start, end, null, true);
	}

	/**
	 * Returns an ordered range of all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching style book tokens
	 */
	public default java.util.List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator) {

		return findByStyleBookTokenSetId(
			styleBookTokenSetId, start, end, orderByComparator, true);
	}

}
// LIFERAY-SERVICE-BUILDER-HASH:-1509668640