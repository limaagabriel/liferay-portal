/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.upload.test.util.UploadTestUtil;
import com.liferay.style.book.exception.DuplicateStyleBookEntryKeyException;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.service.StyleBookEntryLocalService;
import com.liferay.style.book.test.util.FrontendTokenDefinitionTestUtil;
import com.liferay.style.book.zip.processor.StyleBookEntryZipProcessorImportResultEntry;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
@RunWith(Arquillian.class)
public class ExportImportStyleBookEntriesMVCResourceCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_sourceGroup = GroupTestUtil.addGroup();
		_targetGroup = GroupTestUtil.addGroup();
	}

	@Test
	public void testExportImportMultipleStyleBookEntries() throws Exception {
		String styleBookEntryKey1 = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry1 =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), styleBookEntryKey1,
				RandomTestUtil.randomString(), serviceContext);

		String styleBookEntryKey2 = RandomTestUtil.randomString();

		StyleBookEntry styleBookEntry2 =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), styleBookEntryKey2,
				RandomTestUtil.randomString(), serviceContext);

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {
				styleBookEntry1.getStyleBookEntryId(),
				styleBookEntry2.getStyleBookEntryId()
			});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			false);

		Assert.assertEquals(
			2,
			_styleBookEntryLocalService.getStyleBookEntriesCount(
				_targetGroup.getGroupId()));
		Assert.assertNotNull(
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey1));
		Assert.assertNotNull(
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey2));
	}

	@Test
	public void testExportImportSingleStyleBookEntry() throws Exception {
		String name = RandomTestUtil.randomString();
		String styleBookEntryKey = RandomTestUtil.randomString();
		String themeId = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				name, styleBookEntryKey, themeId, serviceContext);

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntry.getStyleBookEntryId()});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			false);

		Assert.assertEquals(
			1,
			_styleBookEntryLocalService.getStyleBookEntriesCount(
				_targetGroup.getGroupId()));

		StyleBookEntry targetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		Assert.assertEquals(name, targetGroupStyleBookEntry.getName());
		Assert.assertEquals(themeId, targetGroupStyleBookEntry.getThemeId());

		JSONAssert.assertEquals(
			styleBookEntry.getFrontendTokensValues(),
			targetGroupStyleBookEntry.getFrontendTokensValues(),
			JSONCompareMode.STRICT);

		Assert.assertTrue(
			Validator.isBlank(
				targetGroupStyleBookEntry.getFrontendTokenDefinition()));
	}

	@Test(expected = DuplicateStyleBookEntryKeyException.class)
	public void testExportImportSingleStyleBookEntryAndNotOverwrite()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), serviceContext);

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntry.getStyleBookEntryId()});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			false);

		StyleBookEntry updatedStyleBookEntry =
			_styleBookEntryLocalService.updateStyleBookEntry(
				styleBookEntry.getStyleBookEntryId(),
				_read("updated-frontend-tokens-values.json"),
				RandomTestUtil.randomString(),
				ServiceContextTestUtil.getServiceContext(
					_sourceGroup, TestPropsValues.getUserId()));

		ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {updatedStyleBookEntry.getStyleBookEntryId()});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			false);
	}

	@Test
	public void testExportImportSingleStyleBookEntryAndOverwrite()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), styleBookEntryKey,
				RandomTestUtil.randomString(), serviceContext);

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntry.getStyleBookEntryId()});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			false);

		String name = RandomTestUtil.randomString();

		StyleBookEntry updatedStyleBookEntry =
			_styleBookEntryLocalService.updateStyleBookEntry(
				styleBookEntry.getStyleBookEntryId(),
				_read("updated-frontend-tokens-values.json"), name,
				ServiceContextTestUtil.getServiceContext(
					_sourceGroup, TestPropsValues.getUserId()));

		file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {updatedStyleBookEntry.getStyleBookEntryId()});

		ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file, true);

		Assert.assertEquals(
			1,
			_styleBookEntryLocalService.getStyleBookEntriesCount(
				_targetGroup.getGroupId()));

		StyleBookEntry updatedTargetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		Assert.assertEquals(name, updatedTargetGroupStyleBookEntry.getName());

		JSONAssert.assertEquals(
			updatedStyleBookEntry.getFrontendTokensValues(),
			updatedTargetGroupStyleBookEntry.getFrontendTokensValues(),
			JSONCompareMode.STRICT);
	}

	@Test
	public void testExportImportSingleStyleBookEntryAndOverwriteWithFrontendTokenDefinition()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry = _addStyleBookEntry(
			_sourceGroup, StringPool.BLANK, styleBookEntryKey, serviceContext);

		StyleBookEntry targetStyleBookEntry = _addStyleBookEntry(
			_targetGroup, StringPool.BLANK, styleBookEntryKey,
			ServiceContextTestUtil.getServiceContext(
				_targetGroup, TestPropsValues.getUserId()));

		String frontendTokenDefinition =
			FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
				RandomTestUtil.randomString());

		StyleBookEntry updatedStyleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				styleBookEntry.getStyleBookEntryId(), frontendTokenDefinition,
				serviceContext);

		File file = _exportStyleBookEntry(
			updatedStyleBookEntry.getStyleBookEntryId());

		_importStyleBookEntry(file, true);

		StyleBookEntry updatedTargetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		JSONAssert.assertEquals(
			frontendTokenDefinition,
			updatedTargetGroupStyleBookEntry.getFrontendTokenDefinition(),
			JSONCompareMode.STRICT);

		Assert.assertEquals(
			targetStyleBookEntry.getStyleBookEntryId(),
			updatedTargetGroupStyleBookEntry.getStyleBookEntryId());
	}

	@Test
	public void testExportImportSingleStyleBookEntryAndOverwriteWithInvalidFrontendTokenDefinition()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry = _addStyleBookEntry(
			_sourceGroup, StringPool.BLANK, styleBookEntryKey, serviceContext);

		StyleBookEntry targetStyleBookEntry = _addStyleBookEntry(
			_targetGroup,
			FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
				RandomTestUtil.randomString()),
			styleBookEntryKey,
			ServiceContextTestUtil.getServiceContext(
				_targetGroup, TestPropsValues.getUserId()));

		String name = RandomTestUtil.randomString();

		StyleBookEntry updatedStyleBookEntry =
			_styleBookEntryLocalService.updateStyleBookEntry(
				styleBookEntry.getStyleBookEntryId(),
				_read("updated-frontend-tokens-values.json"), name,
				ServiceContextTestUtil.getServiceContext(
					_sourceGroup, TestPropsValues.getUserId()));

		updatedStyleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				updatedStyleBookEntry.getStyleBookEntryId(),
				FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
					RandomTestUtil.randomString()),
				serviceContext);

		File file = _exportStyleBookEntry(
			updatedStyleBookEntry.getStyleBookEntryId());

		File invalidFrontendTokenDefinitionFile =
			_getFileWithFrontendTokenDefinition(file, "{not valid json");

		List<StyleBookEntryZipProcessorImportResultEntry> importResultEntries =
			_importStyleBookEntry(invalidFrontendTokenDefinitionFile, true);

		Assert.assertEquals(
			importResultEntries.toString(), 1, importResultEntries.size());

		StyleBookEntryZipProcessorImportResultEntry importResultEntry =
			importResultEntries.get(0);

		Assert.assertEquals(
			StyleBookEntryZipProcessorImportResultEntry.Status.INVALID,
			importResultEntry.getStatus());

		StyleBookEntry updatedTargetStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		Assert.assertEquals(
			targetStyleBookEntry.getName(),
			updatedTargetStyleBookEntry.getName());

		JSONAssert.assertEquals(
			targetStyleBookEntry.getFrontendTokensValues(),
			updatedTargetStyleBookEntry.getFrontendTokensValues(),
			JSONCompareMode.STRICT);

		JSONAssert.assertEquals(
			targetStyleBookEntry.getFrontendTokenDefinition(),
			updatedTargetStyleBookEntry.getFrontendTokenDefinition(),
			JSONCompareMode.STRICT);
	}

	@Test
	public void testExportImportSingleStyleBookEntryAndOverwriteWithoutFrontendTokenDefinitionClearsExistingOne()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry = _addStyleBookEntry(
			_sourceGroup, StringPool.BLANK, styleBookEntryKey, serviceContext);

		File file = _exportStyleBookEntry(styleBookEntry.getStyleBookEntryId());

		StyleBookEntry targetGroupStyleBookEntry = _addStyleBookEntry(
			_targetGroup, StringPool.BLANK, styleBookEntryKey,
			ServiceContextTestUtil.getServiceContext(
				_targetGroup, TestPropsValues.getUserId()));

		_styleBookEntryLocalService.updateFrontendTokenDefinition(
			targetGroupStyleBookEntry.getStyleBookEntryId(),
			FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
				RandomTestUtil.randomString()),
			ServiceContextTestUtil.getServiceContext(
				_targetGroup, TestPropsValues.getUserId()));

		_importStyleBookEntry(file, true);

		StyleBookEntry updatedTargetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		Assert.assertTrue(
			Validator.isBlank(
				updatedTargetGroupStyleBookEntry.getFrontendTokenDefinition()));

		Assert.assertEquals(
			targetGroupStyleBookEntry.getStyleBookEntryId(),
			updatedTargetGroupStyleBookEntry.getStyleBookEntryId());
	}

	@Test
	public void testExportImportSingleStyleBookEntryWithFrontendTokenDefinition()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry = _addStyleBookEntry(
			_sourceGroup, StringPool.BLANK, styleBookEntryKey, serviceContext);

		String frontendTokenDefinition =
			FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
				RandomTestUtil.randomString());

		styleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				styleBookEntry.getStyleBookEntryId(), frontendTokenDefinition,
				serviceContext);

		File file = _exportStyleBookEntry(styleBookEntry.getStyleBookEntryId());

		_importStyleBookEntry(file, false);

		StyleBookEntry targetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		JSONAssert.assertEquals(
			frontendTokenDefinition,
			targetGroupStyleBookEntry.getFrontendTokenDefinition(),
			JSONCompareMode.STRICT);
	}

	@Test
	public void testExportImportSingleStyleBookEntryWithInvalidFrontendTokenDefinition()
		throws Exception {

		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry = _addStyleBookEntry(
			_sourceGroup, StringPool.BLANK, styleBookEntryKey, serviceContext);

		styleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				styleBookEntry.getStyleBookEntryId(),
				FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
					RandomTestUtil.randomString()),
				serviceContext);

		File file = _exportStyleBookEntry(styleBookEntry.getStyleBookEntryId());

		File invalidFrontendTokenDefinitionFile =
			_getFileWithFrontendTokenDefinition(file, "{not valid json");

		List<StyleBookEntryZipProcessorImportResultEntry> importResultEntries =
			_importStyleBookEntry(invalidFrontendTokenDefinitionFile, false);

		Assert.assertEquals(
			importResultEntries.toString(), 1, importResultEntries.size());

		StyleBookEntryZipProcessorImportResultEntry importResultEntry =
			importResultEntries.get(0);

		Assert.assertEquals(
			StyleBookEntryZipProcessorImportResultEntry.Status.INVALID,
			importResultEntry.getStatus());

		Assert.assertEquals(
			0,
			_styleBookEntryLocalService.getStyleBookEntriesCount(
				_targetGroup.getGroupId()));

		Assert.assertNull(
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey));
	}

	@Test
	public void testExportImportSingleStyleBookEntryWithScopedFrontendTokenIsValid()
		throws Exception {

		String frontendTokenName = RandomTestUtil.randomString();
		String styleBookEntryKey = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK,
				JSONUtil.put(
					frontendTokenName,
					JSONUtil.put("value", RandomTestUtil.randomString())
				).toString(),
				RandomTestUtil.randomString(), styleBookEntryKey,
				RandomTestUtil.randomString(), serviceContext);

		styleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				styleBookEntry.getStyleBookEntryId(),
				FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
					frontendTokenName),
				serviceContext);

		File file = _exportStyleBookEntry(styleBookEntry.getStyleBookEntryId());

		MockLiferayPortletActionRequest mockLiferayPortletActionRequest =
			_getMockLiferayPortletActionRequest(file);

		_processAction(mockLiferayPortletActionRequest);

		Assert.assertNull(
			SessionMessages.get(
				mockLiferayPortletActionRequest,
				"styleBookFrontendTokensValuesNotValidated"));

		StyleBookEntry targetGroupStyleBookEntry =
			_styleBookEntryLocalService.fetchStyleBookEntry(
				_targetGroup.getGroupId(), styleBookEntryKey);

		Assert.assertNotNull(targetGroupStyleBookEntry);
	}

	@Test
	public void testExportStyleBookEntries() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), serviceContext);

		FileEntry fileEntry = _addFileEntry(styleBookEntry);

		_styleBookEntryLocalService.updatePreviewFileEntryId(
			styleBookEntry.getStyleBookEntryId(), fileEntry.getFileEntryId(),
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId()));

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntry.getStyleBookEntryId()});

		try (ZipFile zipFile = new ZipFile(file)) {
			int count = 0;

			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = enumeration.nextElement();

				if (!zipEntry.isDirectory()) {
					_validateZipEntry(styleBookEntry, zipEntry, zipFile);

					count++;
				}
			}

			Assert.assertEquals(3, count);
		}
	}

	@Test
	public void testExportStyleBookEntriesWithFrontendTokenDefinition()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		StyleBookEntry styleBookEntry =
			_styleBookEntryLocalService.addStyleBookEntry(
				null, TestPropsValues.getUserId(), _sourceGroup.getGroupId(),
				false, StringPool.BLANK, _read("frontend-tokens-values.json"),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), serviceContext);

		FileEntry fileEntry = _addFileEntry(styleBookEntry);

		_styleBookEntryLocalService.updatePreviewFileEntryId(
			styleBookEntry.getStyleBookEntryId(), fileEntry.getFileEntryId(),
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId()));

		styleBookEntry =
			_styleBookEntryLocalService.updateFrontendTokenDefinition(
				styleBookEntry.getStyleBookEntryId(),
				FrontendTokenDefinitionTestUtil.getFrontendTokenDefinition(
					RandomTestUtil.randomString()),
				serviceContext);

		File file = ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntry.getStyleBookEntryId()});

		try (ZipFile zipFile = new ZipFile(file)) {
			int count = 0;

			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = enumeration.nextElement();

				if (!zipEntry.isDirectory()) {
					_validateZipEntry(styleBookEntry, zipEntry, zipFile);

					count++;
				}
			}

			Assert.assertEquals(4, count);
		}
	}

	private FileEntry _addFileEntry(StyleBookEntry styleBookEntry)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_sourceGroup, TestPropsValues.getUserId());

		Repository repository = PortletFileRepositoryUtil.addPortletRepository(
			_sourceGroup.getGroupId(), RandomTestUtil.randomString(),
			serviceContext);

		Class<?> clazz = getClass();

		return PortletFileRepositoryUtil.addPortletFileEntry(
			null, _sourceGroup.getGroupId(), TestPropsValues.getUserId(),
			StyleBookEntry.class.getName(),
			styleBookEntry.getStyleBookEntryId(), RandomTestUtil.randomString(),
			repository.getDlFolderId(),
			clazz.getResourceAsStream("dependencies/thumbnail.png"),
			RandomTestUtil.randomString(), ContentTypes.IMAGE_PNG, false);
	}

	private StyleBookEntry _addStyleBookEntry(
			Group group, String frontendTokenDefinition,
			String styleBookEntryKey, ServiceContext serviceContext)
		throws Exception {

		return _styleBookEntryLocalService.addStyleBookEntry(
			null, TestPropsValues.getUserId(), group.getGroupId(), false,
			frontendTokenDefinition, _read("frontend-tokens-values.json"),
			RandomTestUtil.randomString(), styleBookEntryKey,
			RandomTestUtil.randomString(), serviceContext);
	}

	private File _exportStyleBookEntry(long styleBookEntryId) throws Exception {
		return ReflectionTestUtil.invoke(
			_exportStyleBookEntriesMVCResourceCommand,
			"_exportStyleBookEntries", new Class<?>[] {long[].class},
			new long[] {styleBookEntryId});
	}

	private File _getFileWithFrontendTokenDefinition(
			File file, String frontendTokenDefinition)
		throws Exception {

		File updatedFile = File.createTempFile("style-book-entries", ".zip");

		updatedFile.deleteOnExit();

		try (ZipFile zipFile = new ZipFile(file);
			ZipOutputStream zipOutputStream = new ZipOutputStream(
				new FileOutputStream(updatedFile))) {

			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = enumeration.nextElement();

				zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));

				if (!zipEntry.isDirectory()) {
					if (_isStyleBookFile(
							zipEntry.getName(),
							_FRONTEND_TOKEN_DEFINITION_FILE_NAME)) {

						zipOutputStream.write(
							frontendTokenDefinition.getBytes());
					}
					else {
						zipOutputStream.write(
							FileUtil.getBytes(
								zipFile.getInputStream(zipEntry)));
					}
				}

				zipOutputStream.closeEntry();
			}
		}

		return updatedFile;
	}

	private MockLiferayPortletActionRequest _getMockLiferayPortletActionRequest(
			File file)
		throws Exception {

		MockLiferayPortletActionRequest mockLiferayPortletActionRequest =
			new MockLiferayPortletActionRequest(
				_getMockMultipartHttpServletRequest(file));

		mockLiferayPortletActionRequest.addParameter("overwrite", "false");
		mockLiferayPortletActionRequest.setAttribute(
			JavaConstants.JAKARTA_PORTLET_CONFIG, _getPortletConfig());
		mockLiferayPortletActionRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		return mockLiferayPortletActionRequest;
	}

	private MockMultipartHttpServletRequest _getMockMultipartHttpServletRequest(
			File file)
		throws Exception {

		MockMultipartHttpServletRequest mockMultipartHttpServletRequest =
			new MockMultipartHttpServletRequest();

		byte[] bytes = FileUtil.getBytes(file);

		mockMultipartHttpServletRequest.addFile(
			new MockMultipartFile("file", bytes));

		mockMultipartHttpServletRequest.setCharacterEncoding(StringPool.UTF8);

		String boundary = "WebKitFormBoundary" + StringUtil.randomString();

		mockMultipartHttpServletRequest.setContent(
			_getMultipartContent(boundary, bytes));
		mockMultipartHttpServletRequest.setContentType(
			MediaType.MULTIPART_FORM_DATA_VALUE + "; boundary=" + boundary);

		return mockMultipartHttpServletRequest;
	}

	private byte[] _getMultipartContent(String boundary, byte[] bytes) {
		String start = StringBundler.concat(
			StringPool.DOUBLE_DASH, boundary,
			"\r\nContent-Disposition:form-data;name=\"file\";filename=\"",
			RandomTestUtil.randomString(), ".zip",
			"\";\r\nContent-type:application/zip\r\n\r\n");

		String end = StringBundler.concat(
			"\r\n--", boundary, StringPool.DOUBLE_DASH);

		return ArrayUtil.append(start.getBytes(), bytes, end.getBytes());
	}

	private LiferayPortletConfig _getPortletConfig() {
		return (LiferayPortletConfig)ProxyUtil.newProxyInstance(
			LiferayPortletConfig.class.getClassLoader(),
			new Class<?>[] {LiferayPortletConfig.class},
			(proxy, method, args) -> {
				if (Objects.equals(method.getName(), "getPortletId")) {
					return RandomTestUtil.randomString();
				}

				if (Objects.equals(method.getName(), "getResourceBundle")) {
					return ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE;
				}

				return null;
			});
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(_targetGroup.getCompanyId()));

		Layout layout = new LayoutImpl();

		layout.setType(LayoutConstants.TYPE_CONTROL_PANEL);

		themeDisplay.setLayout(layout);

		themeDisplay.setScopeGroupId(_targetGroup.getGroupId());
		themeDisplay.setSiteDefaultLocale(LocaleUtil.US);
		themeDisplay.setUser(TestPropsValues.getUser());

		return themeDisplay;
	}

	private List<StyleBookEntryZipProcessorImportResultEntry>
			_importStyleBookEntry(File file, boolean overwrite)
		throws Exception {

		return ReflectionTestUtil.invoke(
			_importStyleBookEntriesMVCActionCommand, "_importStyleBookEntries",
			new Class<?>[] {long.class, long.class, File.class, boolean.class},
			TestPropsValues.getUserId(), _targetGroup.getGroupId(), file,
			overwrite);
	}

	private boolean _isStyleBookFile(String path, String fileName) {
		String[] pathParts = StringUtil.split(path, CharPool.SLASH);

		if ((pathParts.length == 2) && Objects.equals(pathParts[1], fileName)) {
			return true;
		}

		return false;
	}

	private void _processAction(
			MockLiferayPortletActionRequest mockLiferayPortletActionRequest)
		throws Exception {

		Portal originalPortal = (Portal)ReflectionTestUtil.getAndSetFieldValue(
			_importStyleBookEntriesMVCActionCommand, "_portal",
			ProxyUtil.newProxyInstance(
				Portal.class.getClassLoader(), new Class<?>[] {Portal.class},
				(proxy, method, args) -> {
					if (Objects.equals(
							method.getName(), "getUploadPortletRequest")) {

						LiferayPortletRequest liferayPortletRequest =
							_portal.getLiferayPortletRequest(
								mockLiferayPortletActionRequest);

						return UploadTestUtil.createUploadPortletRequest(
							_portal.getUploadServletRequest(
								liferayPortletRequest.getHttpServletRequest()),
							liferayPortletRequest,
							_portal.getPortletNamespace(
								liferayPortletRequest.getPortletName()));
					}

					return method.invoke(_portal, args);
				}));

		try {
			_importStyleBookEntriesMVCActionCommand.processAction(
				mockLiferayPortletActionRequest,
				new MockLiferayPortletActionResponse());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				_importStyleBookEntriesMVCActionCommand, "_portal",
				originalPortal);
		}
	}

	private String _read(String fileName) throws Exception {
		return new String(
			FileUtil.getBytes(getClass(), "dependencies/" + fileName));
	}

	private void _validateZipEntry(
			StyleBookEntry styleBookEntry, ZipEntry zipEntry, ZipFile zipFile)
		throws Exception {

		if (_isStyleBookFile(zipEntry.getName(), "style-book.json")) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				StringUtil.read(zipFile.getInputStream(zipEntry)));

			Assert.assertEquals(
				styleBookEntry.getName(), jsonObject.getString("name"));
			Assert.assertEquals(
				"frontend-tokens-values.json",
				jsonObject.getString("frontendTokensValuesPath"));

			if (Validator.isNotNull(
					styleBookEntry.getFrontendTokenDefinition())) {

				Assert.assertEquals(
					_FRONTEND_TOKEN_DEFINITION_FILE_NAME,
					jsonObject.getString("frontendTokenDefinitionPath"));
			}
		}

		if (_isStyleBookFile(
				zipEntry.getName(), _FRONTEND_TOKEN_DEFINITION_FILE_NAME)) {

			JSONAssert.assertEquals(
				styleBookEntry.getFrontendTokenDefinition(),
				StringUtil.read(zipFile.getInputStream(zipEntry)),
				JSONCompareMode.STRICT);
		}

		if (_isStyleBookFile(
				zipEntry.getName(), "frontend-tokens-values.json")) {

			JSONAssert.assertEquals(
				styleBookEntry.getFrontendTokensValues(),
				StringUtil.read(zipFile.getInputStream(zipEntry)),
				JSONCompareMode.STRICT);
		}

		if (_isStyleBookFile(zipEntry.getName(), "thumbnail.png")) {
			Assert.assertArrayEquals(
				FileUtil.getBytes(getClass(), "dependencies/thumbnail.png"),
				FileUtil.getBytes(zipFile.getInputStream(zipEntry)));
		}
	}

	private static final String _FRONTEND_TOKEN_DEFINITION_FILE_NAME =
		"frontend-token-definition.json";

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject(filter = "mvc.command.name=/style_book/export_style_book_entries")
	private MVCResourceCommand _exportStyleBookEntriesMVCResourceCommand;

	@Inject(filter = "mvc.command.name=/style_book/import_style_book_entries")
	private MVCActionCommand _importStyleBookEntriesMVCActionCommand;

	@Inject
	private Portal _portal;

	@DeleteAfterTestRun
	private Group _sourceGroup;

	@Inject
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@DeleteAfterTestRun
	private Group _targetGroup;

}