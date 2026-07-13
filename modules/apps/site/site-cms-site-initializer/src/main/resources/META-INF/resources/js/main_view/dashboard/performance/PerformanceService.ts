/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	RangeSelectors,
	TrendClassification,
} from '@liferay/analytics-reports-js-components-web';

import ApiHelper from '../../../common/services/ApiHelper';
import {
	AssetConsumption,
	MetricType,
	OverviewMetrics,
	PerformanceMetric,
	TopAssets,
} from './types';

const BASE_URL = '/o/analytics-cms-rest/v1.0';

// DON'T MERGE - Fake data to preview the dashboard while Analytics Cloud has no
// data. Set MOCK to false (or drop this block) to hit the real endpoints.

const MOCK: boolean = true;

const MOCK_TREND = {
	classification: TrendClassification.Positive,
	percentage: 22.5,
};

const MOCK_OVERVIEW_METRICS: OverviewMetrics = {
	downloadsMetric: {
		metricType: 'downloadsMetric',
		previousValue: 4800,
		trend: MOCK_TREND,
		value: 5900,
	},
	impressionsMetric: {
		metricType: 'impressionsMetric',
		previousValue: 26000,
		trend: MOCK_TREND,
		value: 31900,
	},
	readsMetric: {
		metricType: 'readsMetric',
		previousValue: 4300,
		trend: MOCK_TREND,
		value: 5260,
	},
	viewsMetric: {
		metricType: 'viewsMetric',
		previousValue: 14800,
		trend: MOCK_TREND,
		value: 18100,
	},
};

const MOCK_LOCATION_METRIC: PerformanceMetric = {
	metricType: 'viewsMetric',
	metrics: [
		{previousValue: 130, value: 162, valueKey: 'US'},
		{previousValue: 28, value: 34, valueKey: 'GB'},
		{previousValue: 30, value: 28, valueKey: 'FR'},
		{previousValue: 22, value: 25, valueKey: 'BR'},
		{previousValue: 19, value: 18, valueKey: 'IT'},
		{previousValue: 12, value: 15, valueKey: 'ES'},
	],
};

const MOCK_CATEGORIES_METRIC: PerformanceMetric = {
	metricType: 'viewsMetric',
	metrics: [
		{previousValue: 70000, value: 83300, valueKey: 'Sales'},
		{previousValue: 15000, value: 17500, valueKey: 'Business'},
		{previousValue: 6000, value: 6500, valueKey: 'Lifestyle'},
		{previousValue: 5000, value: 5500, valueKey: 'Technology'},
		{previousValue: 3000, value: 3700, valueKey: 'Email'},
		{previousValue: 1000, value: 1300, valueKey: 'Others'},
	],
};

const MOCK_ASSET_CONSUMPTION: Record<
	'category' | 'structure' | 'tag' | 'vocabulary',
	AssetConsumption
> = {
	category: {
		performanceAssetConsumptionItems: [
			{count: 342, key: 'sales', title: 'Sales'},
			{count: 268, key: 'business', title: 'Business'},
			{count: 190, key: 'lifestyle', title: 'Lifestyle'},
			{count: 121, key: 'technology', title: 'Technology'},
			{count: 80, key: 'email', title: 'Email'},
		],
		performanceAssetConsumptionItemsCount: 12,
		totalCount: 1001,
	},
	structure: {
		performanceAssetConsumptionItems: [
			{count: 211, key: 'basic-content', title: 'Basic Content'},
			{count: 185, key: 'knowledge-base', title: 'Knowledge Base'},
			{count: 157, key: 'blog', title: 'Blog'},
			{
				count: 123,
				key: 'custom-structure-a',
				title: 'Custom Structure A',
			},
			{count: 95, key: 'custom-structure-b', title: 'Custom Structure B'},
		],
		performanceAssetConsumptionItemsCount: 8,
		totalCount: 1001,
	},
	tag: {
		performanceAssetConsumptionItems: [
			{count: 158, key: 'featured', title: 'featured'},
			{count: 132, key: 'tutorial', title: 'tutorial'},
			{count: 97, key: 'release', title: 'release'},
			{count: 64, key: 'guide', title: 'guide'},
			{count: 41, key: 'faq', title: 'faq'},
		],
		performanceAssetConsumptionItemsCount: 21,
		totalCount: 1001,
	},
	vocabulary: {
		performanceAssetConsumptionItems: [
			{count: 401, key: 'topic', title: 'Topic'},
			{count: 233, key: 'audience', title: 'Audience'},
			{count: 178, key: 'region', title: 'Region'},
			{count: 119, key: 'product-line', title: 'Product Line'},
			{count: 70, key: 'format', title: 'Format'},
		],
		performanceAssetConsumptionItemsCount: 5,
		totalCount: 1001,
	},
};

const MOCK_TOP_ASSETS: TopAssets = {
	lastPage: 1,
	page: 1,
	pageSize: 20,
	performanceTopAssetItems: [
		{
			downloads: 18599,
			engagement: 0.91,
			impressions: 20000,
			mimeType: 'application/vnd.liferay.journal.article',
			title: 'Case Study',
			trend: MOCK_TREND,
			views: 83676,
		},
		{
			downloads: 9462,
			engagement: 0.85,
			impressions: 13600,
			mimeType: 'image/jpeg',
			title: 'Madrid.jpg',
			trend: {
				classification: TrendClassification.Negative,
				percentage: -5.6,
			},
			views: 4349,
		},
	],
	totalCount: 2,
};

function buildQuery(params: Record<string, unknown>): string {
	const searchParams = new URLSearchParams();

	for (const [key, value] of Object.entries(params)) {
		if (value === undefined || value === null || value === '') {
			continue;
		}

		if (Array.isArray(value)) {
			for (const item of value) {
				searchParams.append(key, String(item));
			}
		}
		else {
			searchParams.append(key, String(value));
		}
	}

	const query = searchParams.toString();

	return query ? `?${query}` : '';
}

async function getOverviewMetrics({
	depotEntryIds,
	rangeKey,
}: {
	depotEntryIds?: string[];
	rangeKey: RangeSelectors;
}) {
	if (MOCK) {
		return {data: MOCK_OVERVIEW_METRICS, error: null};
	}

	return ApiHelper.get<OverviewMetrics>(
		`${BASE_URL}/performance-overview-metric${buildQuery({
			depotEntryIds,
			rangeKey,
		})}`
	);
}

async function getMetric({
	depotEntryIds,
	groupBy,
	metricType,
	rangeKey,
}: {
	depotEntryIds?: string[];
	groupBy: 'categories' | 'location';
	metricType: MetricType;
	rangeKey: RangeSelectors;
}) {
	if (MOCK) {
		return {
			data:
				groupBy === 'location'
					? MOCK_LOCATION_METRIC
					: MOCK_CATEGORIES_METRIC,
			error: null,
		};
	}

	return ApiHelper.get<PerformanceMetric>(
		`${BASE_URL}/performance-metric${buildQuery({
			depotEntryIds,
			groupBy,
			metricType,
			rangeKey,
		})}`
	);
}

async function getAssetConsumption({
	categoryId,
	depotEntryIds,
	groupBy,
	page,
	pageSize,
	rangeKey,
	structureId,
	tagId,
	vocabularyId,
}: {
	categoryId?: string;
	depotEntryIds?: string[];
	groupBy: 'category' | 'structure' | 'tag' | 'vocabulary';
	page?: number;
	pageSize?: number;
	rangeKey: RangeSelectors;
	structureId?: string;
	tagId?: string;
	vocabularyId?: string;
}) {
	if (MOCK) {
		return {data: MOCK_ASSET_CONSUMPTION[groupBy], error: null};
	}

	return ApiHelper.get<AssetConsumption>(
		`${BASE_URL}/performance-asset-consumption${buildQuery({
			categoryId,
			depotEntryIds,
			groupBy,
			page,
			pageSize,
			rangeKey,
			structureId,
			tagId,
			vocabularyId,
		})}`
	);
}

async function getTopAssets({
	assetFilterString,
	depotEntryIds,
	page,
	pageSize,
	rangeKey,
	sort,
}: {
	assetFilterString?: string;
	depotEntryIds?: string[];
	page?: number;
	pageSize?: number;
	rangeKey: RangeSelectors;
	sort?: string;
}) {
	if (MOCK) {
		return {data: MOCK_TOP_ASSETS, error: null};
	}

	return ApiHelper.get<TopAssets>(
		`${BASE_URL}/performance-top-asset${buildQuery({
			assetFilterString,
			depotEntryIds,
			page,
			pageSize,
			rangeKey,
			sort,
		})}`
	);
}

function getMetricExportURL({
	depotEntryIds,
	groupBy,
	metricType,
	rangeKey,
}: {
	depotEntryIds?: string[];
	groupBy: 'categories' | 'location';
	metricType: MetricType;
	rangeKey: RangeSelectors;
}) {
	return `${BASE_URL}/performance-metric/export${buildQuery({
		depotEntryIds,
		groupBy,
		metricType,
		rangeKey,
	})}`;
}

function getTopAssetsExportURL({
	assetFilterString,
	depotEntryIds,
	rangeKey,
	sort,
}: {
	assetFilterString?: string;
	depotEntryIds?: string[];
	rangeKey: RangeSelectors;
	sort?: string;
}) {
	return `${BASE_URL}/performance-top-asset/export${buildQuery({
		assetFilterString,
		depotEntryIds,
		rangeKey,
		sort,
	})}`;
}

export default {
	getAssetConsumption,
	getMetric,
	getMetricExportURL,
	getOverviewMetrics,
	getTopAssets,
	getTopAssetsExportURL,
};
