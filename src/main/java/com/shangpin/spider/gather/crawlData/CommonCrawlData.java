package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.utils.GatherUtil;
import com.shangpin.spider.gather.utils.TwoClickUtil;

import us.codecraft.webmagic.Page;

/**
 * @author njt
 * @date 创建时间：2018年9月8日 下午10:58:59
 * @version 1.0
 * @parameter
 */

public class CommonCrawlData {
	private static Logger LOG = LoggerFactory.getLogger(CommonCrawlData.class);
	/**
	 * 字段规则的标识
	 */
	private static final Integer FIELD_RULES_FLAG = 2;

	public static List<CrawlResult> crawlData(Page page, SpiderRules spiderRuleInfo,
			Map<String, Map<String, String>> clickFieldMap) {
		String url = page.getUrl().get();
		CrawlResult crawlResult = new CrawlResult();
		String[] needClickFieldAry = spiderRuleInfo.getNeedClickFieldAry();
		try {
			Class<?> ruleClass = Class.forName(SpiderRules.class.getName());
			Class<?> resultClass = Class.forName(CrawlResult.class.getName());

			Field[] ruleFields = ruleClass.getDeclaredFields();
			Field[] resultFields = resultClass.getDeclaredFields();

			for (Field resultField : resultFields) {
				resultField.setAccessible(true);
				String typeName = resultField.getGenericType().toString();
				String resultFieldName = resultField.getName();
				if (GatherUtil.filterField(resultFieldName)
						|| GatherUtil.filterNeedClick(resultFieldName, needClickFieldAry)) {
					continue;
				}
				int i = 0;
				String strategyStr = "";
				String rulesStr = "";
				try {
					for (Field ruleField : ruleFields) {
						ruleField.setAccessible(true);
						String ruleFieldName = ruleField.getName();
						if (ruleFieldName.contains(resultFieldName)) {
							if (ruleFieldName.contains(Constants.FIELD_STRATEGY_SUFFIX)) {
								strategyStr = (String) ruleField.get(spiderRuleInfo);
							}
							if (ruleFieldName.contains(Constants.FIELD_RULES_SUFFIX)) {
								rulesStr = (String) ruleField.get(spiderRuleInfo);
							}
							i++;
						}
					}
					if (i == FIELD_RULES_FLAG) {
						rulesStr = GatherUtil.getValue(page, "", strategyStr, rulesStr, resultFieldName);
					}
					ReflectTypeMap.typeMap(typeName, rulesStr, resultFieldName, resultField, crawlResult);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			StackTraceElement traceElement = e.getStackTrace()[0];
			LOG.error("---解析网页数据出错" + e.getLocalizedMessage() + "---错误行数：" + traceElement.getLineNumber());
		}

		List<CrawlResult> resultList = new ArrayList<CrawlResult>();
//		crawlResult.setDetailLink(url);
		crawlResult.setWhiteId(spiderRuleInfo.getWhiteId());
		crawlResult.setSupplierId(spiderRuleInfo.getSupplierId());
		crawlResult.setSupplierNo(spiderRuleInfo.getSupplierNo());
//		规则表中取出需点击获取值的字段
		if (needClickFieldAry.length > 0) {
			if (clickFieldMap != null) {
				if (StrategyConstants.TWO_CLICK.equals(spiderRuleInfo.getJsMenuStrategy())) {
//					两层点击
					resultList = TwoClickUtil.crawlByClick(needClickFieldAry, clickFieldMap, spiderRuleInfo, url, crawlResult);
				}
			} else {
				LOG.error("--源{}，需点击字段的规则映射为NULL--", spiderRuleInfo.getWhiteId());
			}
		} else {
			resultList.add(crawlResult);
		}
		return resultList;

	}

}
