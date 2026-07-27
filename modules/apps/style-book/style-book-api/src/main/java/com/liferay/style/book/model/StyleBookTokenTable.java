/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;StyleBookToken&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookToken
 * @generated
 */
public class StyleBookTokenTable extends BaseTable<StyleBookTokenTable> {

	public static final StyleBookTokenTable INSTANCE =
		new StyleBookTokenTable();

	public final Column<StyleBookTokenTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<StyleBookTokenTable, Long> ctCollectionId =
		createColumn(
			"ctCollectionId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<StyleBookTokenTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> externalReferenceCode =
		createColumn(
			"externalReferenceCode", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Long> styleBookTokenId =
		createColumn(
			"styleBookTokenId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<StyleBookTokenTable, Long> groupId = createColumn(
		"groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, Long> styleBookTokenSetId =
		createColumn(
			"styleBookTokenSetId", Long.class, Types.BIGINT,
			Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> description = createColumn(
		"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> tokenKey = createColumn(
		"tokenKey", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> type = createColumn(
		"type_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<StyleBookTokenTable, String> value = createColumn(
		"value", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private StyleBookTokenTable() {
		super("StyleBookToken", StyleBookTokenTable::new);
	}

}
// LIFERAY-SERVICE-BUILDER-HASH:1514170653