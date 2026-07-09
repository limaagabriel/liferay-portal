/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import classNames from 'classnames';
import React, {useId} from 'react';

import {useChartContainer} from './ChartContainerContext';

import '../../css/ChartPlot.scss';

export interface ChartPlotProps {
	children: React.ReactNode;

	/** Optional class name merged onto the root `<figure>`. */
	className?: string;
	description?: string;

	/** Optional inline style applied to the root `<figure>`. */
	style?: React.CSSProperties;
	title: string;
}

export default function ChartPlot({
	children,
	className,
	description,
	style,
	title,
}: ChartPlotProps) {
	const {dims} = useChartContainer();

	const reactId = useId();
	const titleId = `${reactId}-title`;
	const descId = description ? `${reactId}-desc` : undefined;

	return (
		<figure
			aria-describedby={descId}
			aria-labelledby={titleId}
			className={classNames('charts-plot', className)}
			style={style}
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
