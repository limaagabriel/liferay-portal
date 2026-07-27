/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.persistence.impl;

import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.service.persistence.impl.CollectionPersistenceFinder;
import com.liferay.portal.kernel.service.persistence.impl.FinderColumn;
import com.liferay.portal.kernel.service.persistence.impl.UniquePersistenceFinder;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.style.book.exception.DuplicateStyleBookTokenExternalReferenceCodeException;
import com.liferay.style.book.exception.NoSuchTokenException;
import com.liferay.style.book.model.StyleBookToken;
import com.liferay.style.book.model.StyleBookTokenTable;
import com.liferay.style.book.model.impl.StyleBookTokenImpl;
import com.liferay.style.book.model.impl.StyleBookTokenModelImpl;
import com.liferay.style.book.service.persistence.StyleBookTokenPersistence;
import com.liferay.style.book.service.persistence.StyleBookTokenUtil;
import com.liferay.style.book.service.persistence.impl.constants.StyleBookPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the style book token service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = StyleBookTokenPersistence.class)
public class StyleBookTokenPersistenceImpl
	extends BasePersistenceImpl<StyleBookToken, NoSuchTokenException>
	implements StyleBookTokenPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>StyleBookTokenUtil</code> to access the style book token persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		StyleBookTokenImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private CollectionPersistenceFinder<StyleBookToken, NoSuchTokenException>
		_collectionPersistenceFinderByUuid;

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	@Override
	public List<StyleBookToken> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return _collectionPersistenceFinderByUuid.find(
			finderCache, new Object[] {uuid}, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken findByUuid_First(
			String uuid, OrderByComparator<StyleBookToken> orderByComparator)
		throws NoSuchTokenException {

		return _collectionPersistenceFinderByUuid.findFirst(
			finderCache, new Object[] {uuid}, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchByUuid_First(
		String uuid, OrderByComparator<StyleBookToken> orderByComparator) {

		return _collectionPersistenceFinderByUuid.fetchFirst(
			finderCache, new Object[] {uuid}, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		_collectionPersistenceFinderByUuid.remove(
			finderCache, new Object[] {uuid});
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countByUuid(String uuid) {
		return _collectionPersistenceFinderByUuid.count(
			finderCache, new Object[] {uuid});
	}

	private UniquePersistenceFinder<StyleBookToken, NoSuchTokenException>
		_uniquePersistenceFinderByUUID_G;

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken findByUUID_G(String uuid, long groupId)
		throws NoSuchTokenException {

		return _uniquePersistenceFinderByUUID_G.find(
			finderCache, new Object[] {uuid, groupId});
	}

	/**
	 * Returns the style book token where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		return _uniquePersistenceFinderByUUID_G.fetch(
			finderCache, new Object[] {uuid, groupId}, useFinderCache);
	}

	/**
	 * Removes the style book token where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	@Override
	public StyleBookToken removeByUUID_G(String uuid, long groupId)
		throws NoSuchTokenException {

		StyleBookToken styleBookToken = findByUUID_G(uuid, groupId);

		return remove(styleBookToken);
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		return _uniquePersistenceFinderByUUID_G.count(
			finderCache, new Object[] {uuid, groupId});
	}

	private CollectionPersistenceFinder<StyleBookToken, NoSuchTokenException>
		_collectionPersistenceFinderByUuid_C;

	/**
	 * Returns an ordered range of all the style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>StyleBookTokenModelImpl</code>.
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
	@Override
	public List<StyleBookToken> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return _collectionPersistenceFinderByUuid_C.find(
			finderCache, new Object[] {uuid, companyId}, start, end,
			orderByComparator, useFinderCache);
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
	@Override
	public StyleBookToken findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<StyleBookToken> orderByComparator)
		throws NoSuchTokenException {

		return _collectionPersistenceFinderByUuid_C.findFirst(
			finderCache, new Object[] {uuid, companyId}, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return _collectionPersistenceFinderByUuid_C.fetchFirst(
			finderCache, new Object[] {uuid, companyId}, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		_collectionPersistenceFinderByUuid_C.remove(
			finderCache, new Object[] {uuid, companyId});
	}

	/**
	 * Returns the number of style book tokens where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		return _collectionPersistenceFinderByUuid_C.count(
			finderCache, new Object[] {uuid, companyId});
	}

	private CollectionPersistenceFinder<StyleBookToken, NoSuchTokenException>
		_collectionPersistenceFinderByStyleBookTokenSetId;

	/**
	 * Returns an ordered range of all the style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching style book tokens
	 */
	@Override
	public List<StyleBookToken> findByStyleBookTokenSetId(
		long styleBookTokenSetId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator,
		boolean useFinderCache) {

		return _collectionPersistenceFinderByStyleBookTokenSetId.find(
			finderCache, new Object[] {styleBookTokenSetId}, start, end,
			orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken findByStyleBookTokenSetId_First(
			long styleBookTokenSetId,
			OrderByComparator<StyleBookToken> orderByComparator)
		throws NoSuchTokenException {

		return _collectionPersistenceFinderByStyleBookTokenSetId.findFirst(
			finderCache, new Object[] {styleBookTokenSetId}, orderByComparator);
	}

	/**
	 * Returns the first style book token in the ordered set where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchByStyleBookTokenSetId_First(
		long styleBookTokenSetId,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return _collectionPersistenceFinderByStyleBookTokenSetId.fetchFirst(
			finderCache, new Object[] {styleBookTokenSetId}, orderByComparator);
	}

	/**
	 * Removes all the style book tokens where styleBookTokenSetId = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 */
	@Override
	public void removeByStyleBookTokenSetId(long styleBookTokenSetId) {
		_collectionPersistenceFinderByStyleBookTokenSetId.remove(
			finderCache, new Object[] {styleBookTokenSetId});
	}

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countByStyleBookTokenSetId(long styleBookTokenSetId) {
		return _collectionPersistenceFinderByStyleBookTokenSetId.count(
			finderCache, new Object[] {styleBookTokenSetId});
	}

	private UniquePersistenceFinder<StyleBookToken, NoSuchTokenException>
		_uniquePersistenceFinderBySBTSI_TK;

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken findBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws NoSuchTokenException {

		return _uniquePersistenceFinderBySBTSI_TK.find(
			finderCache, new Object[] {styleBookTokenSetId, tokenKey});
	}

	/**
	 * Returns the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchBySBTSI_TK(
		long styleBookTokenSetId, String tokenKey, boolean useFinderCache) {

		return _uniquePersistenceFinderBySBTSI_TK.fetch(
			finderCache, new Object[] {styleBookTokenSetId, tokenKey},
			useFinderCache);
	}

	/**
	 * Removes the style book token where styleBookTokenSetId = &#63; and tokenKey = &#63; from the database.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the style book token that was removed
	 */
	@Override
	public StyleBookToken removeBySBTSI_TK(
			long styleBookTokenSetId, String tokenKey)
		throws NoSuchTokenException {

		StyleBookToken styleBookToken = findBySBTSI_TK(
			styleBookTokenSetId, tokenKey);

		return remove(styleBookToken);
	}

	/**
	 * Returns the number of style book tokens where styleBookTokenSetId = &#63; and tokenKey = &#63;.
	 *
	 * @param styleBookTokenSetId the style book token set ID
	 * @param tokenKey the token key
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countBySBTSI_TK(long styleBookTokenSetId, String tokenKey) {
		return _uniquePersistenceFinderBySBTSI_TK.count(
			finderCache, new Object[] {styleBookTokenSetId, tokenKey});
	}

	private UniquePersistenceFinder<StyleBookToken, NoSuchTokenException>
		_uniquePersistenceFinderByERC_G;

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the matching style book token
	 * @throws NoSuchTokenException if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken findByERC_G(
			String externalReferenceCode, long groupId)
		throws NoSuchTokenException {

		return _uniquePersistenceFinderByERC_G.find(
			finderCache, new Object[] {externalReferenceCode, groupId});
	}

	/**
	 * Returns the style book token where externalReferenceCode = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchByERC_G(
		String externalReferenceCode, long groupId, boolean useFinderCache) {

		return _uniquePersistenceFinderByERC_G.fetch(
			finderCache, new Object[] {externalReferenceCode, groupId},
			useFinderCache);
	}

	/**
	 * Removes the style book token where externalReferenceCode = &#63; and groupId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the style book token that was removed
	 */
	@Override
	public StyleBookToken removeByERC_G(
			String externalReferenceCode, long groupId)
		throws NoSuchTokenException {

		StyleBookToken styleBookToken = findByERC_G(
			externalReferenceCode, groupId);

		return remove(styleBookToken);
	}

	/**
	 * Returns the number of style book tokens where externalReferenceCode = &#63; and groupId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param groupId the group ID
	 * @return the number of matching style book tokens
	 */
	@Override
	public int countByERC_G(String externalReferenceCode, long groupId) {
		return _uniquePersistenceFinderByERC_G.count(
			finderCache, new Object[] {externalReferenceCode, groupId});
	}

	public StyleBookTokenPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(StyleBookToken.class);

		setModelImplClass(StyleBookTokenImpl.class);
		setModelPKClass(long.class);

		setTable(StyleBookTokenTable.INSTANCE);
	}

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	@Override
	public StyleBookToken create(long styleBookTokenId) {
		StyleBookToken styleBookToken = new StyleBookTokenImpl();

		styleBookToken.setNew(true);
		styleBookToken.setPrimaryKey(styleBookTokenId);

		String uuid = PortalUUIDUtil.generate();

		styleBookToken.setUuid(uuid);

		styleBookToken.setCompanyId(CompanyThreadLocal.getCompanyId());

		return styleBookToken;
	}

	/**
	 * Removes the style book token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token that was removed
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	@Override
	public StyleBookToken remove(long styleBookTokenId)
		throws NoSuchTokenException {

		return remove((Serializable)styleBookTokenId);
	}

	@Override
	protected StyleBookToken removeImpl(StyleBookToken styleBookToken) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(styleBookToken)) {
				styleBookToken = (StyleBookToken)session.get(
					StyleBookTokenImpl.class,
					styleBookToken.getPrimaryKeyObj());
			}

			if ((styleBookToken != null) &&
				ctPersistenceHelper.isRemove(styleBookToken)) {

				session.delete(styleBookToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (styleBookToken != null) {
			clearCache(styleBookToken);
		}

		return styleBookToken;
	}

	@Override
	public StyleBookToken updateImpl(StyleBookToken styleBookToken) {
		boolean isNew = styleBookToken.isNew();

		if (!(styleBookToken instanceof StyleBookTokenModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(styleBookToken.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					styleBookToken);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in styleBookToken proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom StyleBookToken implementation " +
					styleBookToken.getClass());
		}

		StyleBookTokenModelImpl styleBookTokenModelImpl =
			(StyleBookTokenModelImpl)styleBookToken;

		if (Validator.isNull(styleBookToken.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			styleBookToken.setUuid(uuid);
		}

		if (Validator.isNull(styleBookToken.getExternalReferenceCode())) {
			styleBookToken.setExternalReferenceCode(styleBookToken.getUuid());
		}
		else {
			if (!Objects.equals(
					styleBookTokenModelImpl.getColumnOriginalValue(
						"externalReferenceCode"),
					styleBookToken.getExternalReferenceCode())) {

				long userId = GetterUtil.getLong(
					PrincipalThreadLocal.getName());

				if (userId > 0) {
					long companyId = styleBookToken.getCompanyId();

					long groupId = styleBookToken.getGroupId();

					long classPK = 0;

					if (!isNew) {
						classPK = styleBookToken.getPrimaryKey();
					}

					try {
						styleBookToken.setExternalReferenceCode(
							SanitizerUtil.sanitize(
								companyId, groupId, userId,
								StyleBookToken.class.getName(), classPK,
								ContentTypes.TEXT_HTML, Sanitizer.MODE_ALL,
								styleBookToken.getExternalReferenceCode(),
								null));
					}
					catch (SanitizerException sanitizerException) {
						throw new SystemException(sanitizerException);
					}
				}
			}

			StyleBookToken ercStyleBookToken = fetchByERC_G(
				styleBookToken.getExternalReferenceCode(),
				styleBookToken.getGroupId());

			if (isNew) {
				if (ercStyleBookToken != null) {
					throw new DuplicateStyleBookTokenExternalReferenceCodeException(
						"Duplicate style book token with external reference code " +
							styleBookToken.getExternalReferenceCode() +
								" and group " + styleBookToken.getGroupId());
				}
			}
			else {
				if ((ercStyleBookToken != null) &&
					(styleBookToken.getStyleBookTokenId() !=
						ercStyleBookToken.getStyleBookTokenId())) {

					throw new DuplicateStyleBookTokenExternalReferenceCodeException(
						"Duplicate style book token with external reference code " +
							styleBookToken.getExternalReferenceCode() +
								" and group " + styleBookToken.getGroupId());
				}
			}
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (styleBookToken.getCreateDate() == null)) {
			if (serviceContext == null) {
				styleBookToken.setCreateDate(date);
			}
			else {
				styleBookToken.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!styleBookTokenModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				styleBookToken.setModifiedDate(date);
			}
			else {
				styleBookToken.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(styleBookToken)) {
				if (!isNew) {
					session.evict(
						StyleBookTokenImpl.class,
						styleBookToken.getPrimaryKeyObj());
				}

				session.save(styleBookToken);
			}
			else {
				styleBookToken = (StyleBookToken)session.merge(styleBookToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		cacheUniqueFindersResult(styleBookToken, false);

		if (isNew) {
			styleBookToken.setNew(false);
		}

		styleBookToken.resetOriginalValues();

		return styleBookToken;
	}

	/**
	 * Returns the style book token with the primary key or throws a <code>NoSuchTokenException</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws NoSuchTokenException if a style book token with the primary key could not be found
	 */
	@Override
	public StyleBookToken findByPrimaryKey(long styleBookTokenId)
		throws NoSuchTokenException {

		return findByPrimaryKey((Serializable)styleBookTokenId);
	}

	@Override
	protected CTPersistenceHelper getCTPersistenceHelper() {
		return ctPersistenceHelper;
	}

	/**
	 * Returns the style book token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token, or <code>null</code> if a style book token with the primary key could not be found
	 */
	@Override
	public StyleBookToken fetchByPrimaryKey(long styleBookTokenId) {
		return fetchByPrimaryKey((Serializable)styleBookTokenId);
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "styleBookTokenId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_STYLEBOOKTOKEN;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return StyleBookTokenModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "StyleBookToken";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctMergeColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("uuid_");
		ctStrictColumnNames.add("externalReferenceCode");
		ctStrictColumnNames.add("groupId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("userId");
		ctStrictColumnNames.add("userName");
		ctStrictColumnNames.add("createDate");
		ctIgnoreColumnNames.add("modifiedDate");
		ctMergeColumnNames.add("styleBookTokenSetId");
		ctMergeColumnNames.add("description");
		ctMergeColumnNames.add("name");
		ctMergeColumnNames.add("tokenKey");
		ctMergeColumnNames.add("type_");
		ctMergeColumnNames.add("value");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(CTColumnResolutionType.MERGE, ctMergeColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("styleBookTokenId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);

		_uniqueIndexColumnNames.add(new String[] {"uuid_", "groupId"});

		_uniqueIndexColumnNames.add(
			new String[] {"styleBookTokenSetId", "tokenKey"});

		_uniqueIndexColumnNames.add(
			new String[] {"externalReferenceCode", "groupId"});
	}

	/**
	 * Initializes the style book token persistence.
	 */
	@Activate
	public void activate() {
		_collectionPersistenceFinderByUuid = new CollectionPersistenceFinder<>(
			this,
			new FinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
				new String[] {
					String.class.getName(), Integer.class.getName(),
					Integer.class.getName(), OrderByComparator.class.getName()
				},
				new String[] {"uuid_"}, true),
			new FinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
				new String[] {String.class.getName()}, new String[] {"uuid_"},
				0, 1, true, null),
			new FinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
				new String[] {String.class.getName()}, new String[] {"uuid_"},
				0, 1, false, null),
			_SQL_SELECT_STYLEBOOKTOKEN_WHERE, _SQL_COUNT_STYLEBOOKTOKEN_WHERE,
			StyleBookTokenModelImpl.ORDER_BY_JPQL, _ENTITY_ALIAS_PREFIX, "", "",
			null,
			new FinderColumn<>(
				"styleBookToken.", "uuid", "uuid_", FinderColumn.Type.STRING,
				"=", true, true, StyleBookToken::getUuid));

		_uniquePersistenceFinderByUUID_G = new UniquePersistenceFinder<>(
			this,
			createUniqueFinderPath(
				FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
				new String[] {String.class.getName(), Long.class.getName()},
				new String[] {"uuid_", "groupId"}, 0, 1, false,
				convertNullFunction(StyleBookToken::getUuid),
				StyleBookToken::getGroupId),
			_SQL_SELECT_STYLEBOOKTOKEN_WHERE, "",
			new FinderColumn<>(
				"styleBookToken.", "uuid", "uuid_", FinderColumn.Type.STRING,
				"=", true, true, StyleBookToken::getUuid),
			new FinderColumn<>(
				"styleBookToken.", "groupId", FinderColumn.Type.LONG, "=", true,
				true, StyleBookToken::getGroupId));

		_collectionPersistenceFinderByUuid_C =
			new CollectionPersistenceFinder<>(
				this,
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
					new String[] {
						String.class.getName(), Long.class.getName(),
						Integer.class.getName(), Integer.class.getName(),
						OrderByComparator.class.getName()
					},
					new String[] {"uuid_", "companyId"}, true),
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
					new String[] {String.class.getName(), Long.class.getName()},
					new String[] {"uuid_", "companyId"}, 0, 1, true, null),
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
					new String[] {String.class.getName(), Long.class.getName()},
					new String[] {"uuid_", "companyId"}, 0, 1, false, null),
				_SQL_SELECT_STYLEBOOKTOKEN_WHERE,
				_SQL_COUNT_STYLEBOOKTOKEN_WHERE,
				StyleBookTokenModelImpl.ORDER_BY_JPQL, _ENTITY_ALIAS_PREFIX, "",
				"", null,
				new FinderColumn<>(
					"styleBookToken.", "uuid", "uuid_",
					FinderColumn.Type.STRING, "=", true, true,
					StyleBookToken::getUuid),
				new FinderColumn<>(
					"styleBookToken.", "companyId", FinderColumn.Type.LONG, "=",
					true, true, StyleBookToken::getCompanyId));

		_collectionPersistenceFinderByStyleBookTokenSetId =
			new CollectionPersistenceFinder<>(
				this,
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
					"findByStyleBookTokenSetId",
					new String[] {
						Long.class.getName(), Integer.class.getName(),
						Integer.class.getName(),
						OrderByComparator.class.getName()
					},
					new String[] {"styleBookTokenSetId"}, true),
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"findByStyleBookTokenSetId",
					new String[] {Long.class.getName()},
					new String[] {"styleBookTokenSetId"}, true),
				new FinderPath(
					FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
					"countByStyleBookTokenSetId",
					new String[] {Long.class.getName()},
					new String[] {"styleBookTokenSetId"}, false),
				_SQL_SELECT_STYLEBOOKTOKEN_WHERE,
				_SQL_COUNT_STYLEBOOKTOKEN_WHERE,
				StyleBookTokenModelImpl.ORDER_BY_JPQL, _ENTITY_ALIAS_PREFIX, "",
				"", null,
				new FinderColumn<>(
					"styleBookToken.", "styleBookTokenSetId",
					FinderColumn.Type.LONG, "=", true, true,
					StyleBookToken::getStyleBookTokenSetId));

		_uniquePersistenceFinderBySBTSI_TK = new UniquePersistenceFinder<>(
			this,
			createUniqueFinderPath(
				FINDER_CLASS_NAME_ENTITY, "fetchBySBTSI_TK",
				new String[] {Long.class.getName(), String.class.getName()},
				new String[] {"styleBookTokenSetId", "tokenKey"}, 0, 2, false,
				StyleBookToken::getStyleBookTokenSetId,
				convertNullFunction(StyleBookToken::getTokenKey)),
			_SQL_SELECT_STYLEBOOKTOKEN_WHERE, "",
			new FinderColumn<>(
				"styleBookToken.", "styleBookTokenSetId",
				FinderColumn.Type.LONG, "=", true, true,
				StyleBookToken::getStyleBookTokenSetId),
			new FinderColumn<>(
				"styleBookToken.", "tokenKey", FinderColumn.Type.STRING, "=",
				true, true, StyleBookToken::getTokenKey));

		_uniquePersistenceFinderByERC_G = new UniquePersistenceFinder<>(
			this,
			createUniqueFinderPath(
				FINDER_CLASS_NAME_ENTITY, "fetchByERC_G",
				new String[] {String.class.getName(), Long.class.getName()},
				new String[] {"externalReferenceCode", "groupId"}, 0, 1, false,
				convertNullFunction(StyleBookToken::getExternalReferenceCode),
				StyleBookToken::getGroupId),
			_SQL_SELECT_STYLEBOOKTOKEN_WHERE, "",
			new FinderColumn<>(
				"styleBookToken.", "externalReferenceCode",
				FinderColumn.Type.STRING, "=", true, true,
				StyleBookToken::getExternalReferenceCode),
			new FinderColumn<>(
				"styleBookToken.", "groupId", FinderColumn.Type.LONG, "=", true,
				true, StyleBookToken::getGroupId));

		StyleBookTokenUtil.setPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		StyleBookTokenUtil.setPersistence(null);

		entityCache.removeCache(StyleBookTokenImpl.class.getName());
	}

	@Override
	@Reference(
		target = StyleBookPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = StyleBookPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = StyleBookPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected CTPersistenceHelper ctPersistenceHelper;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _ENTITY_ALIAS_PREFIX =
		StyleBookTokenModelImpl.ENTITY_ALIAS + ".";

	private static final String _SQL_SELECT_STYLEBOOKTOKEN =
		"SELECT styleBookToken FROM StyleBookToken styleBookToken";

	private static final String _SQL_SELECT_STYLEBOOKTOKEN_WHERE =
		"SELECT styleBookToken FROM StyleBookToken styleBookToken WHERE ";

	private static final String _SQL_COUNT_STYLEBOOKTOKEN_WHERE =
		"SELECT COUNT(styleBookToken) FROM StyleBookToken styleBookToken WHERE ";

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid", "type"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}
// LIFERAY-SERVICE-BUILDER-HASH:2014875293