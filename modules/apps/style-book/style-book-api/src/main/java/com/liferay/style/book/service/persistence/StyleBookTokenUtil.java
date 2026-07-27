/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.style.book.model.StyleBookToken;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the style book token service. This utility wraps <code>com.liferay.style.book.service.persistence.impl.StyleBookTokenPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenPersistence
 * @generated
 */
public class StyleBookTokenUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#cacheResult(List)
	 */
	public static void cacheResult(List<StyleBookToken> styleBookTokens) {
		getPersistence().cacheResult(styleBookTokens);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#cacheResult(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void cacheResult(StyleBookToken styleBookToken) {
		getPersistence().cacheResult(styleBookToken);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(StyleBookToken styleBookToken) {
		getPersistence().clearCache(styleBookToken);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, StyleBookToken> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<StyleBookToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<StyleBookToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<StyleBookToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static StyleBookToken update(StyleBookToken styleBookToken) {
		return getPersistence().update(styleBookToken);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static StyleBookToken update(
		StyleBookToken styleBookToken, ServiceContext serviceContext) {

		return getPersistence().update(styleBookToken, serviceContext);
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
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public static List<StyleBookToken> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findByUuid_First(
			String uuid, OrderByComparator<StyleBookToken> orderByComparator)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByUuid_First(
		String uuid, OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching style book tokens
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findByUUID_G(String uuid, long groupId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, useFinderCache);
	}

	/**
	 * Removes the style book token where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	public static StyleBookToken removeByUUID_G(String uuid, long groupId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
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
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public static List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<StyleBookToken> orderByComparator)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching style book tokens
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
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
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	public static List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByStyleBookTokenSetId(
			styleBookTokenSetId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findByStyleBookTokenSetId_First(
			long styleBookTokenSetId,
			OrderByComparator<StyleBookToken> orderByComparator)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByStyleBookTokenSetId_First(
			styleBookTokenSetId, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByStyleBookTokenSetId_First(
		long styleBookTokenSetId,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().fetchByStyleBookTokenSetId_First(
			styleBookTokenSetId, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where styleBookTokenSetId = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 */
	public static void removeByStyleBookTokenSetId(long styleBookTokenSetId) {
		getPersistence().removeByStyleBookTokenSetId(styleBookTokenSetId);
	}

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @return the number of matching style book tokens
	 */
	public static int countByStyleBookTokenSetId(long styleBookTokenSetId) {
		return getPersistence().countByStyleBookTokenSetId(styleBookTokenSetId);
	}

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findBySBTSI_TK(styleBookTokenSetId, tokenKey);
	}

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey, boolean useFinderCache) {

		return getPersistence().fetchBySBTSI_TK(
			styleBookTokenSetId, tokenKey, useFinderCache);
	}

	/**
	 * Removes the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the style book token that was removed
	 */
	public static StyleBookToken removeBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().removeBySBTSI_TK(styleBookTokenSetId, tokenKey);
	}

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63; and tokenKey = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the number of matching style book tokens
	 */
	public static int countBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey) {

		return getPersistence().countBySBTSI_TK(styleBookTokenSetId, tokenKey);
	}

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	public static StyleBookToken findByERC_G(
			String externalReferenceCode, long groupId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByERC_G(externalReferenceCode, groupId);
	}

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByERC_G(
		String externalReferenceCode, long groupId, boolean useFinderCache) {

		return getPersistence().fetchByERC_G(
			externalReferenceCode, groupId, useFinderCache);
	}

	/**
	 * Removes the style book token where externalReferenceCode = &#63; and groupId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	public static StyleBookToken removeByERC_G(
			String externalReferenceCode, long groupId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().removeByERC_G(externalReferenceCode, groupId);
	}

	/**
	 * Returns the number of style book tokens where externalReferenceCode = &#63; and groupId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	public static int countByERC_G(String externalReferenceCode, long groupId) {
		return getPersistence().countByERC_G(externalReferenceCode, groupId);
	}

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	public static StyleBookToken create(long styleBookTokenId) {
		return getPersistence().create(styleBookTokenId);
	}

	/**
	 * Removes the style book token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token that was removed
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	public static StyleBookToken remove(long styleBookTokenId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().remove(styleBookTokenId);
	}

	public static StyleBookToken updateImpl(StyleBookToken styleBookToken) {
		return getPersistence().updateImpl(styleBookToken);
	}

	/**
	 * Returns the style book token with the primary key or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	public static StyleBookToken findByPrimaryKey(long styleBookTokenId)
		throws com.liferay.style.book.exception.NoSuchTokenException {

		return getPersistence().findByPrimaryKey(styleBookTokenId);
	}

	/**
	 * Returns the style book token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token, or <code>null</code> if a style book token with the primary key could not be found
	 */
	public static StyleBookToken fetchByPrimaryKey(long styleBookTokenId) {
		return getPersistence().fetchByPrimaryKey(styleBookTokenId);
	}

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey) {

		return getPersistence().fetchBySBTSI_TK(styleBookTokenSetId, tokenKey);
	}

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchByERC_G(
		String externalReferenceCode, long groupId) {

		return getPersistence().fetchByERC_G(externalReferenceCode, groupId);
	}

	/**
	 * Returns all the style book tokens where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching style book tokens
	 */
	public static List<StyleBookToken> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
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
	public static List<StyleBookToken> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
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
	public static List<StyleBookToken> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching style book tokens
	 */
	public static List<StyleBookToken> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
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
	public static List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
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
	public static List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @return the matching style book tokens
	 */
	public static List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId) {

		return getPersistence().findByStyleBookTokenSetId(styleBookTokenSetId);
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
	public static List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end) {

		return getPersistence().findByStyleBookTokenSetId(
			styleBookTokenSetId, start, end);
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
	public static List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getPersistence().findByStyleBookTokenSetId(
			styleBookTokenSetId, start, end, orderByComparator);
	}

	public static StyleBookTokenPersistence getPersistence() {
		return _persistence;
	}

	public static void setPersistence(StyleBookTokenPersistence persistence) {
		_persistence = persistence;
	}

	private static volatile StyleBookTokenPersistence _persistence;

}
// LIFERAY-SERVICE-BUILDER-HASH:-1800246806