package com.l2jserver.gameserver.util;

import com.l2jserver.util.StringUtil;

/**
 * A class containing useful methods for constructing HTML
 * @author NosBit
 */
public class HtmlUtil
{
	/**
	 * Gets the HTML representation of CP gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getCpGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_CP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_CP_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of HP gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getHpGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_HP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_HP_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of HP Warn gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getHpWarnGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_HPWarn_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_HPWarn_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of HP Fill gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getHpFillGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_HPFill_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_HPFill_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of MP Warn gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getMpGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_MP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_MP_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of EXP Warn gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getExpGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_EXP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_EXP_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of Food gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @return the HTML
	 */
	public static String getFoodGauge(int width, long current, long max, boolean displayAsPercentage)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_Food_Bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_Food_Center", 17, -13);
	}
	
	/**
	 * Gets the HTML representation of Weight gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @param level a number from 1 to 5 for the 5 different colors of weight gauge
	 * @return the HTML
	 */
	public static String getWeightGauge(int width, long current, long max, boolean displayAsPercentage, long level)
	{
		return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_Weight_bg_Center" + level, "L2UI_CT1.Gauges.Gauge_DF_Large_Weight_Center" + level, 17, -13);
	}
	
	/**
	 * Gets the HTML representation of a gauge.
	 * @param width the width
	 * @param current the current value
	 * @param max the max value
	 * @param displayAsPercentage if {@code true} the text in middle will be displayed as percent else it will be displayed as "current / max"
	 * @param backgroundImage the background image
	 * @param image the foreground image
	 * @param imageHeight the image height
	 * @param top the top adjustment
	 * @return the HTML
	 */
	private static String getGauge(int width, long current, long max, boolean displayAsPercentage, String backgroundImage, String image, long imageHeight, long top)
	{
		current = Math.min(current, max);
		final StringBuilder sb = new StringBuilder();
		StringUtil.append(sb, "<table width=", String.valueOf(width), " cellpadding=0 cellspacing=0><tr><td background=\"" + backgroundImage + "\">");
		StringUtil.append(sb, "<img src=\"" + image + "\" width=", String.valueOf((long) (((double) current / max) * width)), " height=", String.valueOf(imageHeight), ">");
		StringUtil.append(sb, "</td></tr><tr><td align=center><table cellpadding=0 cellspacing=", String.valueOf(top), "><tr><td>");
		if (displayAsPercentage)
		{
			StringUtil.append(sb, "<table cellpadding=0 cellspacing=2><tr><td>", String.format("%.2f%%", ((double) current / max) * 100), "</td></tr></table>");
		}
		else
		{
			final String tdWidth = String.valueOf((width - 10) / 2);
			StringUtil.append(sb, "<table cellpadding=0 cellspacing=0><tr><td width=" + tdWidth + " align=right>", String.valueOf(current), "</td>");
			StringUtil.append(sb, "<td width=10 align=center>/</td><td width=" + tdWidth + ">", String.valueOf(max), "</td></tr></table>");
		}
		StringUtil.append(sb, "</td></tr></table></td></tr></table>");
		return sb.toString();
	}
	
}