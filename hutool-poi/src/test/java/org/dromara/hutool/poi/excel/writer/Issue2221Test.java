/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.style.DefaultStyleSet;
import org.dromara.hutool.poi.excel.style.StyleUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class Issue2221Test {

	/**
	 * 设置重复别名的时候，通过原key获取写出位置
	 */
	@Test
	@Disabled
	public void writeDuplicateHeaderAliasTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/duplicateAlias.xlsx");
		// 设置别名
		writer.addHeaderAlias("androidLc", "安卓");
		writer.addHeaderAlias("androidAc", "安卓");
		writer.setOnlyAlias(true);

		// 写入数据
		final List<Map<Object, Object>> data = ListUtil.view(
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人")),
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人"))
		);

		writer.write(data, true);
		writer.close();
	}

	@Test
	@Disabled
	public void writeDuplicateHeaderAliasTest2(){
		// 获取写Excel的流
		final ExcelWriter writer = ExcelUtil.getBigWriter("d:/test/duplicateAlias2.xlsx");

		// 设置头部的背景颜色
		StyleUtil.setColor(((DefaultStyleSet)writer.getStyleSet()).getHeadCellStyle(), IndexedColors.GREY_50_PERCENT, FillPatternType.SOLID_FOREGROUND);

		//设置全局字体
		final Font font = writer.createFont();
		font.setFontName("Microsoft YaHei");
		((DefaultStyleSet)writer.getStyleSet()).setFont(font, false);

		// 设置头部的字体为白颜色
		final Font headerFont = writer.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		((DefaultStyleSet)writer.getStyleSet()).getHeadCellStyle().setFont(headerFont);

		// 跳过多少行
		writer.passRows(1);

		// 冻结多少行
		writer.setFreezePane(2);

		// 设置别名
		writer.addHeaderAlias("date", "日期");
		writer.addHeaderAlias("androidLc", "安卓");
		writer.addHeaderAlias("iosLc", "iOS");
		writer.addHeaderAlias("androidAc", " 安卓");
		writer.addHeaderAlias("iosAc", " iOS");
		writer.setOnlyAlias(true);

		// 设置合并的单元格
		writer.merge(new CellRangeAddress(0, 1, 0, 0), "日期", true);
		writer.merge(new CellRangeAddress(0, 0, 1, 2), "运行次数", true);
		writer.merge(new CellRangeAddress(0, 0, 3, 4), "新增人数", true);

		// 写入数据
		final List<Map<Object, Object>> data = ListUtil.view(
				MapUtil.ofEntries(
						MapUtil.entry("date", "2022-01-01"),
						MapUtil.entry("androidLc", "1次"),
						MapUtil.entry("iosLc", "2次"),
						MapUtil.entry("androidAc", "3次"),
						MapUtil.entry("iosAc", "4人")),
				MapUtil.ofEntries(
						MapUtil.entry("date", "2022-01-02"),
						MapUtil.entry("androidLc", "5次"),
						MapUtil.entry("iosLc", "6次"),
						MapUtil.entry("androidAc", "7次"),
						MapUtil.entry("iosAc", "8人"))
		);

		// 自动尺寸
		writer.autoSizeColumnAll(false, 0);

		writer.write(data, true);
		writer.close();
	}
}
