/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.style.book.model.StyleBookToken;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing StyleBookToken in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class StyleBookTokenCacheModel
	implements CacheModel<StyleBookToken>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof StyleBookTokenCacheModel)) {
			return false;
		}

		StyleBookTokenCacheModel styleBookTokenCacheModel =
			(StyleBookTokenCacheModel)object;

		if ((styleBookTokenId == styleBookTokenCacheModel.styleBookTokenId) &&
			(mvccVersion == styleBookTokenCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, styleBookTokenId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", ctCollectionId=");
		sb.append(ctCollectionId);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", externalReferenceCode=");
		sb.append(externalReferenceCode);
		sb.append(", styleBookTokenId=");
		sb.append(styleBookTokenId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", styleBookTokenSetId=");
		sb.append(styleBookTokenSetId);
		sb.append(", description=");
		sb.append(description);
		sb.append(", name=");
		sb.append(name);
		sb.append(", tokenKey=");
		sb.append(tokenKey);
		sb.append(", type=");
		sb.append(type);
		sb.append(", value=");
		sb.append(value);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public StyleBookToken toEntityModel() {
		StyleBookTokenImpl styleBookTokenImpl = new StyleBookTokenImpl();

		styleBookTokenImpl.setMvccVersion(mvccVersion);
		styleBookTokenImpl.setCtCollectionId(ctCollectionId);

		if (uuid == null) {
			styleBookTokenImpl.setUuid("");
		}
		else {
			styleBookTokenImpl.setUuid(uuid);
		}

		if (externalReferenceCode == null) {
			styleBookTokenImpl.setExternalReferenceCode("");
		}
		else {
			styleBookTokenImpl.setExternalReferenceCode(externalReferenceCode);
		}

		styleBookTokenImpl.setStyleBookTokenId(styleBookTokenId);
		styleBookTokenImpl.setGroupId(groupId);
		styleBookTokenImpl.setCompanyId(companyId);
		styleBookTokenImpl.setUserId(userId);

		if (userName == null) {
			styleBookTokenImpl.setUserName("");
		}
		else {
			styleBookTokenImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			styleBookTokenImpl.setCreateDate(null);
		}
		else {
			styleBookTokenImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			styleBookTokenImpl.setModifiedDate(null);
		}
		else {
			styleBookTokenImpl.setModifiedDate(new Date(modifiedDate));
		}

		styleBookTokenImpl.setStyleBookTokenSetId(styleBookTokenSetId);

		if (description == null) {
			styleBookTokenImpl.setDescription("");
		}
		else {
			styleBookTokenImpl.setDescription(description);
		}

		if (name == null) {
			styleBookTokenImpl.setName("");
		}
		else {
			styleBookTokenImpl.setName(name);
		}

		if (tokenKey == null) {
			styleBookTokenImpl.setTokenKey("");
		}
		else {
			styleBookTokenImpl.setTokenKey(tokenKey);
		}

		if (type == null) {
			styleBookTokenImpl.setType("");
		}
		else {
			styleBookTokenImpl.setType(type);
		}

		if (value == null) {
			styleBookTokenImpl.setValue("");
		}
		else {
			styleBookTokenImpl.setValue(value);
		}

		styleBookTokenImpl.resetOriginalValues();

		return styleBookTokenImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		ctCollectionId = objectInput.readLong();
		uuid = objectInput.readUTF();
		externalReferenceCode = objectInput.readUTF();

		styleBookTokenId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		styleBookTokenSetId = objectInput.readLong();
		description = objectInput.readUTF();
		name = objectInput.readUTF();
		tokenKey = objectInput.readUTF();
		type = objectInput.readUTF();
		value = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(ctCollectionId);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		if (externalReferenceCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(externalReferenceCode);
		}

		objectOutput.writeLong(styleBookTokenId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(styleBookTokenSetId);

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (tokenKey == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(tokenKey);
		}

		if (type == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(type);
		}

		if (value == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(value);
		}
	}

	public long mvccVersion;
	public long ctCollectionId;
	public String uuid;
	public String externalReferenceCode;
	public long styleBookTokenId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long styleBookTokenSetId;
	public String description;
	public String name;
	public String tokenKey;
	public String type;
	public String value;

}
// LIFERAY-SERVICE-BUILDER-HASH:-1825824819