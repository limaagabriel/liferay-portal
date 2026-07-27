/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service;

import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.style.book.model.StyleBookToken;

/**
 * Provides a wrapper for {@link StyleBookTokenLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookTokenLocalService
 * @generated
 */
public class StyleBookTokenLocalServiceWrapper
	implements ServiceWrapper<StyleBookTokenLocalService>,
			   StyleBookTokenLocalService {

	public StyleBookTokenLocalServiceWrapper() {
		this(null);
	}

	public StyleBookTokenLocalServiceWrapper(
		StyleBookTokenLocalService styleBookTokenLocalService) {

		_styleBookTokenLocalService = styleBookTokenLocalService;
	}

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
	@Override
	public StyleBookToken addStyleBookToken(StyleBookToken styleBookToken) {
		return _styleBookTokenLocalService.addStyleBookToken(styleBookToken);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new style book token with the primary key. Does not add the style book token to the database.
	 *
	 * @param styleBookTokenId the primary key for the new style book token
	 * @return the new style book token
	 */
	@Override
	public StyleBookToken createStyleBookToken(long styleBookTokenId) {
		return _styleBookTokenLocalService.createStyleBookToken(
			styleBookTokenId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.deletePersistedModel(persistedModel);
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
	@Override
	public StyleBookToken deleteStyleBookToken(long styleBookTokenId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.deleteStyleBookToken(
			styleBookTokenId);
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
	@Override
	public StyleBookToken deleteStyleBookToken(StyleBookToken styleBookToken) {
		return _styleBookTokenLocalService.deleteStyleBookToken(styleBookToken);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _styleBookTokenLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _styleBookTokenLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _styleBookTokenLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _styleBookTokenLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _styleBookTokenLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _styleBookTokenLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _styleBookTokenLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _styleBookTokenLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public StyleBookToken fetchStyleBookToken(long styleBookTokenId) {
		return _styleBookTokenLocalService.fetchStyleBookToken(
			styleBookTokenId);
	}

	@Override
	public StyleBookToken fetchStyleBookTokenByExternalReferenceCode(
		String externalReferenceCode, long groupId) {

		return _styleBookTokenLocalService.
			fetchStyleBookTokenByExternalReferenceCode(
				externalReferenceCode, groupId);
	}

	/**
	 * Returns the style book token matching the UUID and group.
	 *
	 * @param uuid the style book token's UUID
	 * @param groupId the primary key of the group
	 * @return the matching style book token, or <code>null</code> if a matching style book token could not be found
	 */
	@Override
	public StyleBookToken fetchStyleBookTokenByUuidAndGroupId(
		String uuid, long groupId) {

		return _styleBookTokenLocalService.fetchStyleBookTokenByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _styleBookTokenLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _styleBookTokenLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _styleBookTokenLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _styleBookTokenLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the style book token with the primary key.
	 *
	 * @param styleBookTokenId the primary key of the style book token
	 * @return the style book token
	 * @throws PortalException if a style book token with the primary key could not be found
	 */
	@Override
	public StyleBookToken getStyleBookToken(long styleBookTokenId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.getStyleBookToken(styleBookTokenId);
	}

	@Override
	public StyleBookToken getStyleBookTokenByExternalReferenceCode(
			String externalReferenceCode, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.
			getStyleBookTokenByExternalReferenceCode(
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
	@Override
	public StyleBookToken getStyleBookTokenByUuidAndGroupId(
			String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _styleBookTokenLocalService.getStyleBookTokenByUuidAndGroupId(
			uuid, groupId);
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
	@Override
	public java.util.List<StyleBookToken> getStyleBookTokens(
		int start, int end) {

		return _styleBookTokenLocalService.getStyleBookTokens(start, end);
	}

	/**
	 * Returns all the style book tokens matching the UUID and company.
	 *
	 * @param uuid the UUID of the style book tokens
	 * @param companyId the primary key of the company
	 * @return the matching style book tokens, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId) {

		return _styleBookTokenLocalService.getStyleBookTokensByUuidAndCompanyId(
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
	@Override
	public java.util.List<StyleBookToken> getStyleBookTokensByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<StyleBookToken>
			orderByComparator) {

		return _styleBookTokenLocalService.getStyleBookTokensByUuidAndCompanyId(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of style book tokens.
	 *
	 * @return the number of style book tokens
	 */
	@Override
	public int getStyleBookTokensCount() {
		return _styleBookTokenLocalService.getStyleBookTokensCount();
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
	@Override
	public StyleBookToken updateStyleBookToken(StyleBookToken styleBookToken) {
		return _styleBookTokenLocalService.updateStyleBookToken(styleBookToken);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _styleBookTokenLocalService.getBasePersistence();
	}

	@Override
	public CTPersistence<StyleBookToken> getCTPersistence() {
		return _styleBookTokenLocalService.getCTPersistence();
	}

	@Override
	public Class<StyleBookToken> getModelClass() {
		return _styleBookTokenLocalService.getModelClass();
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<StyleBookToken>, R, E>
				updateUnsafeFunction)
		throws E {

		return _styleBookTokenLocalService.updateWithUnsafeFunction(
			updateUnsafeFunction);
	}

	@Override
	public StyleBookTokenLocalService getWrappedService() {
		return _styleBookTokenLocalService;
	}

	@Override
	public void setWrappedService(
		StyleBookTokenLocalService styleBookTokenLocalService) {

		_styleBookTokenLocalService = styleBookTokenLocalService;
	}

	private StyleBookTokenLocalService _styleBookTokenLocalService;

}
// LIFERAY-SERVICE-BUILDER-HASH:-1500402927