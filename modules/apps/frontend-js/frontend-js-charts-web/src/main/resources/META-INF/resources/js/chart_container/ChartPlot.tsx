/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {useId} from 'react';

import {useChartContainer} from './ChartContainerContext';

import '../../css/ChartPlot.scss';

interface Props {
	children: React.ReactNode;
	description?: string;
	title: string;
}

export default function ChartPlot({children, description, title}: Props) {
	const {dims} = useChartContainer();

	const reactId = useId();
	const titleId = `${reactId}-title`;
	const descId = description ? `${reactId}-desc` : undefined;

	return (
		<figure
			aria-describedby={descId}
			aria-labelledby={titleId}
			className="charts-plot"
		>
			<figcaption className="charts-plot__title" id={titleId}>
				{title}
			</figcaption>

			{description && (
				<p className="sr-only" id={descId}>
					{description}
				</p>
			)}

			<svg
				focusable="false"
				preserveAspectRatio="xMidYMid meet"
				viewBox={`0 0 ${dims.width} ${dims.height}`}
			>
				{children}
			</svg>
		</figure>
	);
}
