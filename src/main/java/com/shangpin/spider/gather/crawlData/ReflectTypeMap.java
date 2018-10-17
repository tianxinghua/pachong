package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
			return;
		}
		if (typeField.contains("Long")) {
			long parseLong = 0L;
			if(StringUtils.isNotBlank(resultValue)) {
				resultValue = rgNum(resultValue);
				try {
					parseLong = Long.parseLong(resultValue);
				} catch (Exception e) {
					LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
				}
			}
			resultField.set(crawlResult, parseLong);
			return;
		}
		if (typeField.contains("Integer")) {
			Integer parseInt = 0;
			if(StringUtils.isNotBlank(resultValue)) {
				resultValue = rgNum(resultValue);
				try {
					parseInt = Integer.parseInt(resultValue);
				} catch (Exception e) {
					LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
				}
			}
			resultField.set(crawlResult, parseInt);
			return;
		}
		if (typeField.contains("BigDecimal")) {
			BigDecimal bigDecimal = BigDecimal.ZERO;
			if(StringUtils.isNotBlank(resultValue)) {
				resultValue = rgNum(resultValue);
				try {
					bigDecimal = new BigDecimal(resultValue);
				} catch (Exception e) {
					LOG.error("--{}的值-{}-类型转换异常", resultFieldName, typeField);
				}
			}
			resultField.set(crawlResult, bigDecimal);
			return;
		}

	}
	
	private static String rgNum(String resultValue) {
		resultValue = resultValue.replaceAll("\\s*", "");
		Pattern compile = Pattern.compile("\\d*[.]?\\d*");
		Matcher matcher = compile.matcher(resultValue);
		String rgValue = "";
		while(matcher.find()) {
			 rgValue += matcher.group();
		}
		return rgValue;
	}
	
	/**
	 * 执行数据解析
	 * @param className
	 * @param needClickFieldAry
	 * @param menuRuleArray
	 * @param clickFieldMap
	 * @param spiderRuleInfo
	 * @param url
	 * @param crawlResult
	 * @return
	 */
	public static Boolean executeClick(String className, String[] needClickFieldAry, String[] menuRuleArray,
			Map<String, Map<String, String>> clickFieldMap, SpiderRules spiderRuleInfo, String url,
			CrawlResult crawlResult, List<CrawlResult> crawlList) {
		Boolean flag = false;
		Class<?> class1 = null;
		try {
			class1 = Class.forName(className);
			Method method = class1.getMethod(Constants.CLICKMETHOD, String[].class, String[].class, Map.class,
					SpiderRules.class, String.class, CrawlResult.class, List.class);
			method.invoke(class1.newInstance(), needClickFieldAry, menuRuleArray, clickFieldMap, spiderRuleInfo, url,
					crawlResult, crawlList);
			flag = true;
		} catch (ClassNotFoundException e1) {
			LOG.error("反射执行点击，反射类不存在！");
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		} catch (InstantiationException e) {
			LOG.error("反射执行点击，反射类实例化出错！");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			LOG.error("反射执行点击，方法不存在！");
			e.printStackTrace();
		} catch (SecurityException e) {
			LOG.error("反射执行多层点击时出错！");
			e.printStackTrace();
		}
		return flag;

	}
}
