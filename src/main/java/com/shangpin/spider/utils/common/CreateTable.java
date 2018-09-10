package com.shangpin.spider.utils.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public class CreateTable {
	public static String createTable(String tableName) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		LocalDateTime now = LocalDateTime.now();
		String nowStr = now.format(formatter);
		if(StringUtils.isBlank(tableName.replaceAll("_", ""))||tableName.contains("null")) {
			tableName = "SP_"+nowStr;
		}
		return tableName;
	}
}
