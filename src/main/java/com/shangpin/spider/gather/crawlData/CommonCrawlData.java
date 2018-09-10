package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.utils.GatherUtil;
import us.codecraft.webmagic.Page;

/**
 * @author njt
 * @date 创建时间：2018年9月8日 下午10:58:59
 * @version 1.0
 * @parameter
 */

public class CommonCrawlData {
	private static Logger LOG = LoggerFactory.getLogger(CommonCrawlData.class);
	
	public static Map<String, Object> crawlData(Page page,SpiderRules spiderRuleInfo) {
		CrawlResult crawlResult = new CrawlResult();
		String spu = "";
		if (StringUtils.isNotBlank(spiderRuleInfo.getSpuStrategy())&&StringUtils.isNotBlank(spiderRuleInfo.getSpuRules())) {
			spu = GatherUtil.getValue(page, spu, spiderRuleInfo.getSpuStrategy(), spiderRuleInfo.getSpuRules(), "spu");
		}
		spiderRuleInfo.setSppuHashRules(GatherUtil.longHashCode(spu).toString());
		
		try {
			Class<?> ruleClass = Class.forName(SpiderRules.class.getName());
			Class<?> resultClass = Class.forName(CrawlResult.class.getName());
			
			Field[] ruleFields = ruleClass.getDeclaredFields();
			Field[] resultFields = resultClass.getDeclaredFields();
			
			for (Field resultField : resultFields) {
				resultField.setAccessible(true);
				String typeName = resultField.getGenericType().toString();
				String resultFieldName = resultField.getName();
				if(filterField(resultFieldName)) 
				{
					continue;
				}
				int i = 0;
				String strategyStr = "";
				String rulesStr = "";
				try {
					for (Field ruleField : ruleFields) {
						ruleField.setAccessible(true);
						String ruleFieldName = ruleField.getName();
						if(ruleFieldName.contains(resultFieldName)) {
							if(ruleFieldName.contains("Strategy")) {
								strategyStr = (String) ruleField.get(spiderRuleInfo);
							}
							if(ruleFieldName.contains("Rules")) {
								rulesStr = (String) ruleField.get(spiderRuleInfo);
							}
							i++;
						}
					}
					if(i==2) {
						rulesStr = GatherUtil.getValue(page, "", strategyStr, rulesStr,resultFieldName);
					}
					typeMap(typeName,rulesStr,resultFieldName,resultField,crawlResult);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			StackTraceElement traceElement = e.getStackTrace()[0];
			LOG.error("---解析网页数据出错" + e.getLocalizedMessage() + "---错误行数："+ traceElement.getLineNumber());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		crawlResult.setWhiteId(spiderRuleInfo.getWhiteId());
		if(StringUtils.isNotBlank(crawlResult.getSpu())) {
			crawlResult.setProductModel(crawlResult.getSpu());
		}
		map.put("crawlResult", crawlResult);
		return map;
	}
	
	/**
	 * 过滤无需传值的字段
	 * @param resultFieldName
	 * @return
	 */
	private static Boolean filterField(String resultFieldName) {
		Boolean flag = false;
		if("serialVersionUID".equals(resultFieldName)) {
			flag = true;
		}else if("id".equals(resultFieldName)) {
			flag = true;
		}else if("createTime".equals(resultFieldName)) {
			flag = true;
		}else if("updateTime".equals(resultFieldName)) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 处理类型转换异常
	 * @param typeName
	 * @param resultValue
	 * @param resultFieldName
	 * @param resultField
	 * @param crawlResult
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void typeMap(String typeName, String resultValue, String resultFieldName, Field resultField, CrawlResult crawlResult) throws IllegalArgumentException, IllegalAccessException {
		String typeField = "";
		if(typeName.contains("java.lang.")) {
			typeField = typeName.replace("java.lang.", "");
		}else if(typeName.contains("java.math.")) {
			typeField = typeName.replace("java.math.", "");
		}
		if(typeField.contains("String")) {
			resultField.set(crawlResult, resultValue);
		}
		if(typeField.contains("Long")) {
			long parseLong = 0L;
			try {
				parseLong = Long.parseLong(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常",resultFieldName,typeField);
			}
			resultField.set(crawlResult, parseLong);
			
		}
		if(typeField.contains("Integer")) {
			Integer parseInt = 0;
			try {
				parseInt = Integer.parseInt(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常",resultFieldName,typeField);
			}
			resultField.set(crawlResult, parseInt);
		}
		if(typeField.contains("BigDecimal")) {
			BigDecimal bigDecimal = BigDecimal.ZERO;
			try {
				bigDecimal = new BigDecimal(resultValue);
			} catch (Exception e) {
				LOG.error("--{}的值-{}-类型转换异常",resultFieldName,typeField);
			}
			resultField.set(crawlResult, bigDecimal);
		}
		
	}
}
