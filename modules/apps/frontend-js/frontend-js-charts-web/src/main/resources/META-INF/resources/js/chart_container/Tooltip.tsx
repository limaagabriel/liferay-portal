/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import {useChartContainer} from './ChartContainerContext';

import '../../css/ChartTooltip.scss';

const CHAR_WIDTH = 6.5;
const CHIP_HEIGHT = 22;
const CHIP_PADDING_X = 10;
const CORNER_OFFSET = 4;
const POINT_GAP = 12;
const SWATCH_GAP = 6;
const SWATCH_RADIUS = 4;

export type TooltipPlacement = 'corner' | 'popover';

export interface TooltipProps {

	/** Formats the active datum's numeric value. Defaults to `String`. */
	format?: (value: number) => string;

	/**
	 * `'popover'` floats the chip near the active datum's anchor point;
	 * `'corner'` pins it to the plot's top-left corner. Defaults to
	 * `'popover'`.
	 */
	placement?: TooltipPlacement;
}

function clamp(value: number, min: number, max: number): number {
	return Math.min(Math.max(value, min), max);
}

/**
 * Plot-level tooltip chip for the container's unified `active` datum
 * (hover-or-focus, published by `LineSeries`/`BarSeries`). Reads `active`
 * straight from context and renders nothing of its own — no hover
 * listeners here, so the chip already tracks pointer AND keyboard focus for
 * free. Takes the series display name from `active.label` (the publishing
 * series' own name) and the swatch color from the matched `series` metadata
 * by `active.seriesId` — using the datum's label rather than the metadata's
 * avoids a `colorByCategory` bar, whose registered `ChartSeriesMeta.label`
 * is overloaded to `category: value` for the Legend, rendering that pair
 * twice.
 *
 * Renders as an svg `<g>` inside the shared `ChartPlot` svg rather than an
 * absolutely-positioned HTML overlay: `ChartPlot`'s svg is responsive
 * (`viewBox` plus `width: 100%`, see `ChartPlot.scss`), so `active.position`
 * — itself expressed in svg user-space units — only lines up with a
 * sibling HTML overlay when the rendered size happens to equal `dims`,
 * which a responsive svg does not guarantee.
 *
 * Marked `aria-hidden`: the active element's own `aria-label` already
 * carries the datum's meaning to assistive tech, so the chip stays purely
 * visual and does not double-announce.
 */
export default function Tooltip({
	format = String,
	placement = 'popover',
}: TooltipProps) {
	const {active, scale, series} = useChartContainer();

	if (!active) {
		return null;
	}

	const meta = series.find((candidate) => candidate.id === active.seriesId);

	if (!meta) {
		return null;
	}

	const text = `${active.category} · ${active.label}: ${format(active.value)}`;

	const chipWidth =
		text.length * CHAR_WIDTH +
		CHIP_PADDING_X * 2 +
		SWATCH_RADIUS * 2 +
		SWATCH_GAP;

	if (placement === 'corner') {
		return (
			<g
				aria-hidden="true"
				className="charts-tooltip charts-tooltip--corner"
				transform={`translate(${scale.plot.x + CORNER_OFFSET} ${scale.plot.y + CORNER_OFFSET})`}
			>
				<rect
					className="charts-tooltip__box"
					height={CHIP_HEIGHT}
					rx={4}
					width={chipWidth}
					x={0}
					y={0}
				/>

				<circle
					className="charts-tooltip__swatch"
					cx={CHIP_PADDING_X + SWATCH_RADIUS}
					cy={CHIP_HEIGHT / 2}
					fill={meta.color}
					r={SWATCH_RADIUS}
				/>

				<text
					className="charts-tooltip__text"
					x={CHIP_PADDING_X + SWATCH_RADIUS * 2 + SWATCH_GAP}
					y={CHIP_HEIGHT / 2 + 4}
				>
					{text}
				</text>
			</g>
		);
	}

	const centerX = clamp(
		active.position.x,
		scale.plot.x + chipWidth / 2,
		scale.plot.x + scale.plot.width - chipWidth / 2
	);

	const below = active.position.y - POINT_GAP - CHIP_HEIGHT < scale.plot.y;
	const centerY = below
		? active.position.y + POINT_GAP + CHIP_HEIGHT / 2
		: active.position.y - POINT_GAP - CHIP_HEIGHT / 2;

	const arrowX = clamp(
		active.position.x - centerX,
		-chipWidth / 2 + 8,
		chipWidth / 2 - 8
	);
	const arrowBaseY = below ? -CHIP_HEIGHT / 2 : CHIP_HEIGHT / 2;
	const arrowTipY = below ? -CHIP_HEIGHT / 2 - 5 : CHIP_HEIGHT / 2 + 5;

	return (
		<g
			aria-hidden="true"
			className="charts-tooltip charts-tooltip--popover"
			transform={`translate(${centerX} ${centerY})`}
		>
			<rect
				className="charts-tooltip__box"
				height={CHIP_HEIGHT}
				rx={4}
				width={chipWidth}
				x={-chipWidth / 2}
				y={-CHIP_HEIGHT / 2}
			/>

			<polygon
				className="charts-tooltip__arrow"
				points={`${arrowX - 5},${arrowBaseY} ${arrowX + 5},${arrowBaseY} ${arrowX},${arrowTipY}`}
			/>

			<circle
				className="charts-tooltip__swatch"
				cx={-chipWidth / 2 + CHIP_PADDING_X + SWATCH_RADIUS}
				cy={0}
				fill={meta.color}
				r={SWATCH_RADIUS}
			/>

			<text
				className="charts-tooltip__text"
				x={
					-chipWidth / 2 +
					CHIP_PADDING_X +
					SWATCH_RADIUS * 2 +
					SWATCH_GAP
				}
				y={4}
			>
				{text}
			</text>
		</g>
	);
}
