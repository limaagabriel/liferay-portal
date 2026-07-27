/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.style.book.exception.DuplicateStyleBookTokenExternalReferenceCodeException;
import com.liferay.style.book.exception.NoSuchTokenException;
import com.liferay.style.book.model.StyleBookToken;
import com.liferay.style.book.service.StyleBookTokenLocalServiceUtil;
import com.liferay.style.book.service.persistence.StyleBookTokenPersistence;
import com.liferay.style.book.service.persistence.StyleBookTokenUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class StyleBookTokenPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.style.book.service"));

	@Before
	public void setUp() {
		_persistence = StyleBookTokenUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<StyleBookToken> iterator = _styleBookTokens.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		StyleBookToken styleBookToken = _persistence.create(pk);

		Assert.assertNotNull(styleBookToken);

		Assert.assertEquals(styleBookToken.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		_persistence.remove(newStyleBookToken);

		StyleBookToken existingStyleBookToken = _persistence.fetchByPrimaryKey(
			newStyleBookToken.getPrimaryKey());

		Assert.assertNull(existingStyleBookToken);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addStyleBookToken();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		newStyleBookToken.setCtCollectionId(RandomTestUtil.nextLong());

		newStyleBookToken.setUuid(RandomTestUtil.randomString());

		newStyleBookToken.setExternalReferenceCode(
			RandomTestUtil.randomString());

		newStyleBookToken.setGroupId(RandomTestUtil.nextLong());

		newStyleBookToken.setCompanyId(RandomTestUtil.nextLong());

		newStyleBookToken.setUserId(RandomTestUtil.nextLong());

		newStyleBookToken.setUserName(RandomTestUtil.randomString());

		newStyleBookToken.setCreateDate(RandomTestUtil.nextDate());

		newStyleBookToken.setModifiedDate(RandomTestUtil.nextDate());

		newStyleBookToken.setStyleBookTokenSetId(RandomTestUtil.nextLong());

		newStyleBookToken.setDescription(RandomTestUtil.randomString());

		newStyleBookToken.setName(RandomTestUtil.randomString());

		newStyleBookToken.setTokenKey(RandomTestUtil.randomString());

		newStyleBookToken.setType(RandomTestUtil.randomString());

		newStyleBookToken.setValue(RandomTestUtil.randomString());

		newStyleBookToken = _persistence.update(newStyleBookToken);

		_styleBookTokens.add(newStyleBookToken);

		StyleBookToken existingStyleBookToken = _persistence.findByPrimaryKey(
			newStyleBookToken.getPrimaryKey());

		Assert.assertEquals(
			existingStyleBookToken.getMvccVersion(),
			newStyleBookToken.getMvccVersion());
		Assert.assertEquals(
			existingStyleBookToken.getCtCollectionId(),
			newStyleBookToken.getCtCollectionId());
		Assert.assertEquals(
			existingStyleBookToken.getUuid(), newStyleBookToken.getUuid());
		Assert.assertEquals(
			existingStyleBookToken.getExternalReferenceCode(),
			newStyleBookToken.getExternalReferenceCode());
		Assert.assertEquals(
			existingStyleBookToken.getStyleBookTokenId(),
			newStyleBookToken.getStyleBookTokenId());
		Assert.assertEquals(
			existingStyleBookToken.getGroupId(),
			newStyleBookToken.getGroupId());
		Assert.assertEquals(
			existingStyleBookToken.getCompanyId(),
			newStyleBookToken.getCompanyId());
		Assert.assertEquals(
			existingStyleBookToken.getUserId(), newStyleBookToken.getUserId());
		Assert.assertEquals(
			existingStyleBookToken.getUserName(),
			newStyleBookToken.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingStyleBookToken.getCreateDate()),
			Time.getShortTimestamp(newStyleBookToken.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingStyleBookToken.getModifiedDate()),
			Time.getShortTimestamp(newStyleBookToken.getModifiedDate()));
		Assert.assertEquals(
			existingStyleBookToken.getStyleBookTokenSetId(),
			newStyleBookToken.getStyleBookTokenSetId());
		Assert.assertEquals(
			existingStyleBookToken.getDescription(),
			newStyleBookToken.getDescription());
		Assert.assertEquals(
			existingStyleBookToken.getName(), newStyleBookToken.getName());
		Assert.assertEquals(
			existingStyleBookToken.getTokenKey(),
			newStyleBookToken.getTokenKey());
		Assert.assertEquals(
			existingStyleBookToken.getType(), newStyleBookToken.getType());
		Assert.assertEquals(
			existingStyleBookToken.getValue(), newStyleBookToken.getValue());
	}

	@Test(
		expected = DuplicateStyleBookTokenExternalReferenceCodeException.class
	)
	public void testUpdateWithExistingExternalReferenceCode() throws Exception {
		StyleBookToken styleBookToken = addStyleBookToken();

		StyleBookToken newStyleBookToken = addStyleBookToken();

		newStyleBookToken.setGroupId(styleBookToken.getGroupId());

		newStyleBookToken = _persistence.update(newStyleBookToken);

		Session session = _persistence.getCurrentSession();

		session.evict(newStyleBookToken);

		newStyleBookToken.setExternalReferenceCode(
			styleBookToken.getExternalReferenceCode());

		_persistence.update(newStyleBookToken);
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G("", RandomTestUtil.nextLong());

		_persistence.countByUUID_G("null", 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByStyleBookTokenSetId() throws Exception {
		_persistence.countByStyleBookTokenSetId(RandomTestUtil.nextLong());

		_persistence.countByStyleBookTokenSetId(0L);
	}

	@Test
	public void testCountBySBTSI_TK() throws Exception {
		_persistence.countBySBTSI_TK(RandomTestUtil.nextLong(), "");

		_persistence.countBySBTSI_TK(0L, "null");

		_persistence.countBySBTSI_TK(0L, (String)null);
	}

	@Test
	public void testCountByERC_G() throws Exception {
		_persistence.countByERC_G("", RandomTestUtil.nextLong());

		_persistence.countByERC_G("null", 0L);

		_persistence.countByERC_G((String)null, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		StyleBookToken existingStyleBookToken = _persistence.findByPrimaryKey(
			newStyleBookToken.getPrimaryKey());

		Assert.assertEquals(existingStyleBookToken, newStyleBookToken);
	}

	@Test(expected = NoSuchTokenException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<StyleBookToken> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"StyleBookToken", "mvccVersion", true, "ctCollectionId", true,
			"uuid", true, "externalReferenceCode", true, "styleBookTokenId",
			true, "groupId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true,
			"styleBookTokenSetId", true, "description", true, "name", true,
			"tokenKey", true, "type", true, "value", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		StyleBookToken existingStyleBookToken = _persistence.fetchByPrimaryKey(
			newStyleBookToken.getPrimaryKey());

		Assert.assertEquals(existingStyleBookToken, newStyleBookToken);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		StyleBookToken missingStyleBookToken = _persistence.fetchByPrimaryKey(
			pk);

		Assert.assertNull(missingStyleBookToken);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		StyleBookToken newStyleBookToken1 = addStyleBookToken();
		StyleBookToken newStyleBookToken2 = addStyleBookToken();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newStyleBookToken1.getPrimaryKey());
		primaryKeys.add(newStyleBookToken2.getPrimaryKey());

		Map<Serializable, StyleBookToken> styleBookTokens =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, styleBookTokens.size());
		Assert.assertEquals(
			newStyleBookToken1,
			styleBookTokens.get(newStyleBookToken1.getPrimaryKey()));
		Assert.assertEquals(
			newStyleBookToken2,
			styleBookTokens.get(newStyleBookToken2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, StyleBookToken> styleBookTokens =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(styleBookTokens.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		StyleBookToken newStyleBookToken = addStyleBookToken();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newStyleBookToken.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, StyleBookToken> styleBookTokens =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, styleBookTokens.size());
		Assert.assertEquals(
			newStyleBookToken,
			styleBookTokens.get(newStyleBookToken.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, StyleBookToken> styleBookTokens =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(styleBookTokens.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newStyleBookToken.getPrimaryKey());

		Map<Serializable, StyleBookToken> styleBookTokens =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, styleBookTokens.size());
		Assert.assertEquals(
			newStyleBookToken,
			styleBookTokens.get(newStyleBookToken.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			StyleBookTokenLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<StyleBookToken>() {

				@Override
				public void performAction(StyleBookToken styleBookToken) {
					Assert.assertNotNull(styleBookToken);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			StyleBookToken.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"styleBookTokenId", newStyleBookToken.getStyleBookTokenId()));

		List<StyleBookToken> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		StyleBookToken existingStyleBookToken = result.get(0);

		Assert.assertEquals(existingStyleBookToken, newStyleBookToken);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			StyleBookToken.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"styleBookTokenId", RandomTestUtil.nextLong()));

		List<StyleBookToken> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			StyleBookToken.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("styleBookTokenId"));

		Object newStyleBookTokenId = newStyleBookToken.getStyleBookTokenId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"styleBookTokenId", new Object[] {newStyleBookTokenId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingStyleBookTokenId = result.get(0);

		Assert.assertEquals(existingStyleBookTokenId, newStyleBookTokenId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			StyleBookToken.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("styleBookTokenId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"styleBookTokenId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		StyleBookToken newStyleBookToken = addStyleBookToken();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newStyleBookToken.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		StyleBookToken newStyleBookToken = addStyleBookToken();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			StyleBookToken.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"styleBookTokenId", newStyleBookToken.getStyleBookTokenId()));

		List<StyleBookToken> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(StyleBookToken styleBookToken) {
		Assert.assertEquals(
			styleBookToken.getUuid(),
			ReflectionTestUtil.invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(styleBookToken.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			Long.valueOf(styleBookToken.getStyleBookTokenSetId()),
			ReflectionTestUtil.<Long>invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "styleBookTokenSetId"));
		Assert.assertEquals(
			styleBookToken.getTokenKey(),
			ReflectionTestUtil.invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "tokenKey"));

		Assert.assertEquals(
			styleBookToken.getExternalReferenceCode(),
			ReflectionTestUtil.invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "externalReferenceCode"));
		Assert.assertEquals(
			Long.valueOf(styleBookToken.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				styleBookToken, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));
	}

	protected StyleBookToken addStyleBookToken() throws Exception {
		long pk = RandomTestUtil.nextLong();

		StyleBookToken styleBookToken = _persistence.create(pk);

		styleBookToken.setCtCollectionId(RandomTestUtil.nextLong());

		styleBookToken.setUuid(RandomTestUtil.randomString());

		styleBookToken.setExternalReferenceCode(RandomTestUtil.randomString());

		styleBookToken.setGroupId(RandomTestUtil.nextLong());

		styleBookToken.setCompanyId(RandomTestUtil.nextLong());

		styleBookToken.setUserId(RandomTestUtil.nextLong());

		styleBookToken.setUserName(RandomTestUtil.randomString());

		styleBookToken.setCreateDate(RandomTestUtil.nextDate());

		styleBookToken.setModifiedDate(RandomTestUtil.nextDate());

		styleBookToken.setStyleBookTokenSetId(RandomTestUtil.nextLong());

		styleBookToken.setDescription(RandomTestUtil.randomString());

		styleBookToken.setName(RandomTestUtil.randomString());

		styleBookToken.setTokenKey(RandomTestUtil.randomString());

		styleBookToken.setType(RandomTestUtil.randomString());

		styleBookToken.setValue(RandomTestUtil.randomString());

		_styleBookTokens.add(_persistence.update(styleBookToken));

		return styleBookToken;
	}

	private List<StyleBookToken> _styleBookTokens =
		new ArrayList<StyleBookToken>();
	private StyleBookTokenPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}
// LIFERAY-SERVICE-BUILDER-HASH:2099281033