package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 抓取数据中，反射类型处理
 * 
 * @author njt
 *
 */
public class ReflectTypeMap {
	private static Logger LOG = LoggerFactory.getLogger(ReflectTypeMap.class);

	/**
	 * 处理类型转换异常
	 * 
	 * @param typeName
	 * @param resultValue
	 * @param resultFieldName
	 * @param resultField
	 * @param crawlResult
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void typeMap(String typeName, String resultValue, String resultFieldName, Field resultField,
			CrawlResult crawlResult) throws IllegalArgumentException, IllegalAccessException {
		String typeField = "";
		if (typeName.contains("java.lang.")) {
			typeField = typeName.replace("java.lang.", "");
		} else if (typeName.contains("java.math.")) {
			typeField = typeName.replace("java.math.", "");
		}
		if (typeField.contains("String")) {
			resultField.set(crawlResult, resultValue);
		}
		if (typeField.contains("Long")) {
			long parseLong = 0L;
			try {
				parseLong = Long.parseLong(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
			}
			resultField.set(crawlResult, parseLong);

		}
		if (typeField.contains("Integer")) {
			Integer parseInt = 0;
			try {
				parseInt = Integer.parseInt(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
			}
			resultField.set(crawlResult, parseInt);
		}
		if (typeField.contains("BigDecimal")) {
			BigDecimal bigDecimal = BigDecimal.ZERO;
			try {
				bigDecimal = new BigDecimal(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
			}
			resultField.set(crawlResult, bigDecimal);
		}

	}

	public static Boolean executeClick(String className, String[] needClickFieldAry, String[] menuRuleArray,
			Map<String, Map<String, String>> clickFieldMap, SpiderRules spiderRuleInfo, String url,
			CrawlResult crawlResult) {
		Boolean flag = false;
		Class<?> class1 = null;
		try {
			class1 = Class.forName(className);
			flag = true;
		} catch (ClassNotFoundException e1) {
			LOG.error("反射执行多层点击时出错！");
			e1.printStackTrace();
		}
		try {
			Method method = class1.getMethod(Constants.CLICKMETHOD, String[].class, String[].class, Map.class,
					SpiderRules.class, String.class, CrawlResult.class);
			try {
				method.invoke(class1, needClickFieldAry, menuRuleArray, clickFieldMap, spiderRuleInfo, url,
						crawlResult);
			} catch (IllegalAccessException e) {
				LOG.error("反射执行多层点击时出错！");
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				LOG.error("反射执行多层点击时出错！");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				LOG.error("反射执行多层点击时出错！");
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		} catch (SecurityException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		}
		return flag;

	}
}
