/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.style.book.model.StyleBookToken;

import java.io.Serializable;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the local service interface for StyleBookToken. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenLocalServiceUtil
 * @generated
 */
@CTAware
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface StyleBookTokenLocalService
	extends BaseLocalService, CTService<StyleBookToken>,
			PersistedModelLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.style.book.service.impl.StyleBookTokenLocalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the style book token local service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link StyleBookTokenLocalServiceUtil} if injection and service tracking are not available.
	 */
	public StyleBookToken addStyleBookToken(
			String externalReferenceCode, long userId, String description,
			String frontendTokenCategoryName, String frontendTokenSetName,
			String name, long styleBookEntryId, String themeId, String type,
			String value)
		throws PortalException;

	/**
	 * Adds the style book token to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookToken the style book token
	 * @return the style book token that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	public StyleBookToken addStyleBookToken(StyleBookToken styleBookToken);

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	@Transactional(enabled = false)
	public StyleBookToken createStyleBookToken(long styleBookTokenId);

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	/**
	 * Deletes the style book token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token that was removed
	 * @throws PortalException if a style book token with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	public StyleBookToken deleteStyleBookToken(long styleBookTokenId)
		throws PortalException;

	/**
	 * Deletes the style book token from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookToken the style book token
	 * @return the style book token that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	public StyleBookToken deleteStyleBookToken(StyleBookToken styleBookToken);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> T dslQuery(DSLQuery dslQuery);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int dslQueryCount(DSLQuery dslQuery);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end);

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken fetchStyleBookToken(long styleBookTokenId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken fetchStyleBookTokenByExternalReferenceCode(
		String externalReferenceCode, long groupId);

	/**
	 * Returns the style book token matching the UUID and group.
	 *
	 * @param uuid the style book token's UUID
	 * @param groupId the primary key of the group
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken fetchStyleBookTokenByUuidAndGroupId(
		String uuid, long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	/**
	 * @throws PortalException
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	 * Returns the style book token with the primary key.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws PortalException if a style book token with the primary key could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken getStyleBookToken(long styleBookTokenId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken getStyleBookTokenByExternalReferenceCode(
			String externalReferenceCode, long groupId)
		throws PortalException;

	/**
	 * Returns the style book token matching the UUID and group.
	 *
	 * @param uuid the style book token's UUID
	 * @param groupId the primary key of the group
	 * @return the matching style book token
	 * @throws PortalException if a matching style book token could not be found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StyleBookToken getStyleBookTokenByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException;

	/**
	 * Returns a range of all the style book tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.style.book.model.impl.StyleBookTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @return the range of style book tokens
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<StyleBookToken> getStyleBookTokens(int start, int end);

	/**
	 * Returns all the style book tokens matching the UUID and company.
	 *
	 * @param uuid the UUID of the style book tokens
	 * @param companyId the primary key of the company
	 * @return the matching style book tokens, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId);

	/**
	 * Returns a range of style book tokens matching the UUID and company.
	 *
	 * @param uuid the UUID of the style book tokens
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of style book tokens
	 * @param end the upper bound of the range of style book tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching style book tokens, or an empty list if no matches were found
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator);

	/**
	 * Returns the number of style book tokens.
	 *
	 * @return the number of style book tokens
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getStyleBookTokensCount();

	/**
	 * Updates the style book token in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect StyleBookTokenLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param styleBookToken the style book token
	 * @return the style book token that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	public StyleBookToken updateStyleBookToken(StyleBookToken styleBookToken);

	@Override
	@Transactional(enabled = false)
	public CTPersistence<StyleBookToken> getCTPersistence();

	@Override
	@Transactional(enabled = false)
	public Class<StyleBookToken> getModelClass();

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<StyleBookToken>, R, E>
				updateUnsafeFunction)
		throws E;

}
// LIFERAY-SERVICE-BUILDER-HASH:1432136238