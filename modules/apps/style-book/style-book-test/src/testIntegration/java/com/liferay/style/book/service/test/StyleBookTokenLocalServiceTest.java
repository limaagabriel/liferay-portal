/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.style.book.exception.DuplicateStyleBookTokenNameException;
import com.liferay.style.book.exception.NoSuchTokenSetException;
import com.liferay.style.book.exception.StyleBookTokenNameException;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.model.StyleBookToken;
import com.liferay.style.book.model.StyleBookTokenSet;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.service.StyleBookTokenLocalService;
import com.liferay.style.book.service.StyleBookTokenSetLocalService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Lima
 */
@RunWith(Arquillian.class)
public class StyleBookTokenLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_group, TestPropsValues.getUserId());

		_styleBookEntry = _styleBookEntryLocalService.addStyleBookEntry(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(),
			_group.getGroupId(), false, null, RandomTestUtil.randomString(),
			null, RandomTestUtil.randomString(), _serviceContext);

		_styleBookTokenSet =
			_styleBookTokenSetLocalService.addStyleBookTokenSet(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_styleBookEntry.getStyleBookEntryId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				RandomTestUtil.randomString(), _THEME_ID_CLASSIC);
	}

	@Test
	public void testAddStyleBookToken() throws Exception {
		String externalReferenceCode = RandomTestUtil.randomString();
		String name = RandomTestUtil.randomString();
		String type = RandomTestUtil.randomString();
		String value = RandomTestUtil.randomString();
		String description = RandomTestUtil.randomString();

		StyleBookToken styleBookToken =
			_styleBookTokenLocalService.addStyleBookToken(
				externalReferenceCode, TestPropsValues.getUserId(), description,
				_FRONTEND_TOKEN_CATEGORY_NAME, _styleBookTokenSet.getName(),
				name, _styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				type, value);

		Assert.assertEquals(description, styleBookToken.getDescription());
		Assert.assertEquals(
			externalReferenceCode, styleBookToken.getExternalReferenceCode());
		Assert.assertEquals(_group.getGroupId(), styleBookToken.getGroupId());
		Assert.assertEquals(name, styleBookToken.getName());
		Assert.assertEquals(
			_styleBookTokenSet.getStyleBookTokenSetId(),
			styleBookToken.getStyleBookTokenSetId());
		Assert.assertEquals(
			FriendlyURLNormalizerUtil.normalize(name),
			styleBookToken.getTokenKey());
		Assert.assertEquals(type, styleBookToken.getType());
		Assert.assertEquals(
			TestPropsValues.getUserId(), styleBookToken.getUserId());
		Assert.assertEquals(value, styleBookToken.getValue());
	}

	@Test(expected = DuplicateStyleBookTokenNameException.class)
	public void testAddStyleBookTokenWhenNameIsDuplicate() throws Exception {
		String name = RandomTestUtil.randomString();

		_styleBookTokenLocalService.addStyleBookToken(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(),
			RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
			_styleBookTokenSet.getName(), name,
			_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		_styleBookTokenLocalService.addStyleBookToken(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(),
			RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
			_styleBookTokenSet.getName(), name,
			_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
			RandomTestUtil.randomString(), RandomTestUtil.randomString());
	}

	@Test
	public void testAddStyleBookTokenWhenNameIsInvalid() throws Exception {
		Assert.assertThrows(
			StyleBookTokenNameException.class,
			() -> _styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_styleBookTokenSet.getName(), StringPool.BLANK,
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString()));
		Assert.assertThrows(
			StyleBookTokenNameException.class,
			() -> _styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_styleBookTokenSet.getName(), null,
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString()));
		Assert.assertThrows(
			StyleBookTokenNameException.class,
			() -> _styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_styleBookTokenSet.getName(), RandomTestUtil.randomString(76),
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString()));
	}

	@Test
	public void testAddStyleBookTokenWhenParentTokenSetIsNotStatic()
		throws Exception {

		StyleBookToken styleBookToken =
			_styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_styleBookTokenSet.getName(), RandomTestUtil.randomString(),
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

		Assert.assertTrue(styleBookToken.getStyleBookTokenId() > 0);
	}

	@Test
	public void testAddStyleBookTokenWhenParentTokenSetIsStatic()
		throws Exception {

		int styleBookTokenSetsCount =
			_styleBookTokenSetLocalService.getStyleBookTokenSetsCount();

		StyleBookToken styleBookToken =
			_styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_BASE_FRONTEND_TOKEN_SET_NAME, RandomTestUtil.randomString(),
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

		Assert.assertEquals(
			styleBookTokenSetsCount + 1,
			_styleBookTokenSetLocalService.getStyleBookTokenSetsCount());
		Assert.assertTrue(styleBookToken.getStyleBookTokenId() > 0);
		Assert.assertTrue(styleBookToken.getStyleBookTokenSetId() > 0);

		StyleBookTokenSet styleBookTokenSet =
			_styleBookTokenSetLocalService.getStyleBookTokenSet(
				styleBookToken.getStyleBookTokenSetId());

		Assert.assertEquals(
			_BASE_FRONTEND_TOKEN_SET_NAME, styleBookTokenSet.getName());
	}

	@Test(expected = NoSuchTokenSetException.class)
	public void testAddStyleBookTokenWhenStyleBookTokenSetDoesNotExist()
		throws Exception {

		_styleBookTokenLocalService.addStyleBookToken(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(),
			RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
			RandomTestUtil.randomString(), RandomTestUtil.randomString());
	}

	@Test
	public void testAddStyleBookTokenWhenStyleBookTokenSetIdIsDifferent()
		throws Exception {

		String name = RandomTestUtil.randomString();

		StyleBookToken styleBookToken1 =
			_styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				_styleBookTokenSet.getName(), name,
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

		StyleBookTokenSet styleBookTokenSet2 =
			_styleBookTokenSetLocalService.addStyleBookTokenSet(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_styleBookEntry.getStyleBookEntryId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				RandomTestUtil.randomString(), _THEME_ID_CLASSIC);

		StyleBookToken styleBookToken2 =
			_styleBookTokenLocalService.addStyleBookToken(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				RandomTestUtil.randomString(), _FRONTEND_TOKEN_CATEGORY_NAME,
				styleBookTokenSet2.getName(), name,
				_styleBookEntry.getStyleBookEntryId(), _THEME_ID_CLASSIC,
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

		Assert.assertNotEquals(
			styleBookToken1.getStyleBookTokenId(),
			styleBookToken2.getStyleBookTokenId());
	}

	private static final String _BASE_FRONTEND_TOKEN_SET_NAME = "brandColors";

	private static final String _FRONTEND_TOKEN_CATEGORY_NAME = "colorSystem";

	private static final String _THEME_ID_CLASSIC = "classic_WAR_classictheme";

	@DeleteAfterTestRun
	private Group _group;

	private ServiceContext _serviceContext;
	private StyleBookEntry _styleBookEntry;

	@Inject
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Inject
	private StyleBookTokenLocalService _styleBookTokenLocalService;

	private StyleBookTokenSet _styleBookTokenSet;

	@Inject
	private StyleBookTokenSetLocalService _styleBookTokenSetLocalService;

}