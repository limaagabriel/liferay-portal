/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.style.book.model.StyleBookToken;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for StyleBookToken. This utility wraps
 * <code>com.liferay.style.book.service.impl.StyleBookTokenLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenLocalService
 * @generated
 */
public class StyleBookTokenLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.style.book.service.impl.StyleBookTokenLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

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
	public static StyleBookToken addStyleBookToken(
		StyleBookToken styleBookToken) {

		return getService().addStyleBookToken(styleBookToken);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	public static StyleBookToken createStyleBookToken(long styleBookTokenId) {
		return getService().createStyleBookToken(styleBookTokenId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

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
	public static StyleBookToken deleteStyleBookToken(long styleBookTokenId)
		throws PortalException {

		return getService().deleteStyleBookToken(styleBookTokenId);
	}

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
	public static StyleBookToken deleteStyleBookToken(
		StyleBookToken styleBookToken) {

		return getService().deleteStyleBookToken(styleBookToken);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

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
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

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
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static StyleBookToken fetchStyleBookToken(long styleBookTokenId) {
		return getService().fetchStyleBookToken(styleBookTokenId);
	}

	public static StyleBookToken fetchStyleBookTokenByExternalReferenceCode(
		String externalReferenceCode, long groupId) {

		return getService().fetchStyleBookTokenByExternalReferenceCode(
			externalReferenceCode, groupId);
	}

	/**
	 * Returns the style book token matching the UUID and group.
	 *
	 * @param uuid the style book token's UUID
	 * @param groupId the primary key of the group
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	public static StyleBookToken fetchStyleBookTokenByUuidAndGroupId(
		String uuid, long groupId) {

		return getService().fetchStyleBookTokenByUuidAndGroupId(uuid, groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the style book token with the primary key.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws PortalException if a style book token with the primary key could not be found
	 */
	public static StyleBookToken getStyleBookToken(long styleBookTokenId)
		throws PortalException {

		return getService().getStyleBookToken(styleBookTokenId);
	}

	public static StyleBookToken getStyleBookTokenByExternalReferenceCode(
			String externalReferenceCode, long groupId)
		throws PortalException {

		return getService().getStyleBookTokenByExternalReferenceCode(
			externalReferenceCode, groupId);
	}

	/**
	 * Returns the style book token matching the UUID and group.
	 *
	 * @param uuid the style book token's UUID
	 * @param groupId the primary key of the group
	 * @return the matching style book token
	 * @throws PortalException if a matching style book token could not be found
	 */
	public static StyleBookToken getStyleBookTokenByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return getService().getStyleBookTokenByUuidAndGroupId(uuid, groupId);
	}

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
	public static List<StyleBookToken> getStyleBookTokens(int start, int end) {
		return getService().getStyleBookTokens(start, end);
	}

	/**
	 * Returns all the style book tokens matching the UUID and company.
	 *
	 * @param uuid the UUID of the style book tokens
	 * @param companyId the primary key of the company
	 * @return the matching style book tokens, or an empty list if no matches were found
	 */
	public static List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().getStyleBookTokensByUuidAndCompanyId(
			uuid, companyId);
	}

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
	public static List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<StyleBookToken> orderByComparator) {

		return getService().getStyleBookTokensByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of style book tokens.
	 *
	 * @return the number of style book tokens
	 */
	public static int getStyleBookTokensCount() {
		return getService().getStyleBookTokensCount();
	}

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
	public static StyleBookToken updateStyleBookToken(
		StyleBookToken styleBookToken) {

		return getService().updateStyleBookToken(styleBookToken);
	}

	public static StyleBookTokenLocalService getService() {
		return _serviceSnapshot.get();
	}

	private static final Snapshot<StyleBookTokenLocalService> _serviceSnapshot =
		new Snapshot<>(
			StyleBookTokenLocalServiceUtil.class,
			StyleBookTokenLocalService.class);

}
// LIFERAY-SERVICE-BUILDER-HASH:387744135