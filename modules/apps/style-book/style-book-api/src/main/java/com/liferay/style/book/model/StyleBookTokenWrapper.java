/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.model;

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link StyleBookToken}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookToken
 * @generated
 */
public class StyleBookTokenWrapper
	extends BaseModelWrapper<StyleBookToken>
	implements ModelWrapper<StyleBookToken>, StyleBookToken {

	public StyleBookTokenWrapper(StyleBookToken styleBookToken) {
		super(styleBookToken);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("ctCollectionId", getCtCollectionId());
		attributes.put("uuid", getUuid());
		attributes.put("externalReferenceCode", getExternalReferenceCode());
		attributes.put("styleBookTokenId", getStyleBookTokenId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("styleBookTokenSetId", getStyleBookTokenSetId());
		attributes.put("description", getDescription());
		attributes.put("name", getName());
		attributes.put("tokenKey", getTokenKey());
		attributes.put("type", getType());
		attributes.put("value", getValue());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long ctCollectionId = (Long)attributes.get("ctCollectionId");

		if (ctCollectionId != null) {
			setCtCollectionId(ctCollectionId);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		String externalReferenceCode = (String)attributes.get(
			"externalReferenceCode");

		if (externalReferenceCode != null) {
			setExternalReferenceCode(externalReferenceCode);
		}

		Long styleBookTokenId = (Long)attributes.get("styleBookTokenId");

		if (styleBookTokenId != null) {
			setStyleBookTokenId(styleBookTokenId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long styleBookTokenSetId = (Long)attributes.get("styleBookTokenSetId");

		if (styleBookTokenSetId != null) {
			setStyleBookTokenSetId(styleBookTokenSetId);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String tokenKey = (String)attributes.get("tokenKey");

		if (tokenKey != null) {
			setTokenKey(tokenKey);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String value = (String)attributes.get("value");

		if (value != null) {
			setValue(value);
		}
	}

	@Override
	public StyleBookToken cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the company ID of this style book token.
	 *
	 * @return the company ID of this style book token
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this style book token.
	 *
	 * @return the create date of this style book token
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the ct collection ID of this style book token.
	 *
	 * @return the ct collection ID of this style book token
	 */
	@Override
	public long getCtCollectionId() {
		return model.getCtCollectionId();
	}

	/**
	 * Returns the description of this style book token.
	 *
	 * @return the description of this style book token
	 */
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	 * Returns the external reference code of this style book token.
	 *
	 * @return the external reference code of this style book token
	 */
	@Override
	public String getExternalReferenceCode() {
		return model.getExternalReferenceCode();
	}

	/**
	 * Returns the group ID of this style book token.
	 *
	 * @return the group ID of this style book token
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the modified date of this style book token.
	 *
	 * @return the modified date of this style book token
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this style book token.
	 *
	 * @return the mvcc version of this style book token
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the name of this style book token.
	 *
	 * @return the name of this style book token
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the primary key of this style book token.
	 *
	 * @return the primary key of this style book token
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the style book token ID of this style book token.
	 *
	 * @return the style book token ID of this style book token
	 */
	@Override
	public long getStyleBookTokenId() {
		return model.getStyleBookTokenId();
	}

	/**
	 * Returns the style book token set ID of this style book token.
	 *
	 * @return the style book token set ID of this style book token
	 */
	@Override
	public long getStyleBookTokenSetId() {
		return model.getStyleBookTokenSetId();
	}

	/**
	 * Returns the token key of this style book token.
	 *
	 * @return the token key of this style book token
	 */
	@Override
	public String getTokenKey() {
		return model.getTokenKey();
	}

	/**
	 * Returns the type of this style book token.
	 *
	 * @return the type of this style book token
	 */
	@Override
	public String getType() {
		return model.getType();
	}

	/**
	 * Returns the user ID of this style book token.
	 *
	 * @return the user ID of this style book token
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this style book token.
	 *
	 * @return the user name of this style book token
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this style book token.
	 *
	 * @return the user uuid of this style book token
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns the uuid of this style book token.
	 *
	 * @return the uuid of this style book token
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * Returns the value of this style book token.
	 *
	 * @return the value of this style book token
	 */
	@Override
	public String getValue() {
		return model.getValue();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this style book token.
	 *
	 * @param companyId the company ID of this style book token
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this style book token.
	 *
	 * @param createDate the create date of this style book token
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the ct collection ID of this style book token.
	 *
	 * @param ctCollectionId the ct collection ID of this style book token
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId) {
		model.setCtCollectionId(ctCollectionId);
	}

	/**
	 * Sets the description of this style book token.
	 *
	 * @param description the description of this style book token
	 */
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	 * Sets the external reference code of this style book token.
	 *
	 * @param externalReferenceCode the external reference code of this style book token
	 */
	@Override
	public void setExternalReferenceCode(String externalReferenceCode) {
		model.setExternalReferenceCode(externalReferenceCode);
	}

	/**
	 * Sets the group ID of this style book token.
	 *
	 * @param groupId the group ID of this style book token
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this style book token.
	 *
	 * @param modifiedDate the modified date of this style book token
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this style book token.
	 *
	 * @param mvccVersion the mvcc version of this style book token
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the name of this style book token.
	 *
	 * @param name the name of this style book token
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the primary key of this style book token.
	 *
	 * @param primaryKey the primary key of this style book token
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the style book token ID of this style book token.
	 *
	 * @param styleBookTokenId the style book token ID of this style book token
	 */
	@Override
	public void setStyleBookTokenId(long styleBookTokenId) {
		model.setStyleBookTokenId(styleBookTokenId);
	}

	/**
	 * Sets the style book token set ID of this style book token.
	 *
	 * @param styleBookTokenSetId the style book token set ID of this style book token
	 */
	@Override
	public void setStyleBookTokenSetId(long styleBookTokenSetId) {
		model.setStyleBookTokenSetId(styleBookTokenSetId);
	}

	/**
	 * Sets the token key of this style book token.
	 *
	 * @param tokenKey the token key of this style book token
	 */
	@Override
	public void setTokenKey(String tokenKey) {
		model.setTokenKey(tokenKey);
	}

	/**
	 * Sets the type of this style book token.
	 *
	 * @param type the type of this style book token
	 */
	@Override
	public void setType(String type) {
		model.setType(type);
	}

	/**
	 * Sets the user ID of this style book token.
	 *
	 * @param userId the user ID of this style book token
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this style book token.
	 *
	 * @param userName the user name of this style book token
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this style book token.
	 *
	 * @param userUuid the user uuid of this style book token
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this style book token.
	 *
	 * @param uuid the uuid of this style book token
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	/**
	 * Sets the value of this style book token.
	 *
	 * @param value the value of this style book token
	 */
	@Override
	public void setValue(String value) {
		model.setValue(value);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	public Map<String, Function<StyleBookToken, Object>>
		getAttributeGetterFunctions() {

		return model.getAttributeGetterFunctions();
	}

	@Override
	public Map<String, BiConsumer<StyleBookToken, Object>>
		getAttributeSetterBiConsumers() {

		return model.getAttributeSetterBiConsumers();
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected StyleBookTokenWrapper wrap(StyleBookToken styleBookToken) {
		return new StyleBookTokenWrapper(styleBookToken);
	}

}
// LIFERAY-SERVICE-BUILDER-HASH:-903446474