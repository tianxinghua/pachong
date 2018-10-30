package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.common.MoreClickEnum;
import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.utils.GatherUtil;
import com.shangpin.spider.gather.utils.ClickUtils.CheckClickRulesUtil;

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
	
	private static ThreadLocal<ArrayList<CrawlResult>> resultListLocal = new ThreadLocal<ArrayList<CrawlResult>>();

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

		List<CrawlResult> resultList = resultListLocal.get();
		resultList = new ArrayList<CrawlResult>();
		crawlResult.setWhiteId(spiderRuleInfo.getWhiteId());
		crawlResult.setSupplierId(spiderRuleInfo.getSupplierId());
		crawlResult.setSupplierNo(spiderRuleInfo.getSupplierNo());
		boolean clickFlag = true;
//		规则表中取出需点击获取值的字段
		if (needClickFieldAry!=null&&needClickFieldAry.length > 0) {
			if (clickFieldMap != null) {
//				最后执行哪种点击策略的标识
				int sizeFlag = 1;
//				多层点击
//				resultList = MoreClickUtil.crawlByClick(needClickFieldAry, clickFieldMap, spiderRuleInfo, url, crawlResult);
//				判断点击的策略
				String jsMenuStrategy = spiderRuleInfo.getJsMenuStrategy();
				String jsMenuRules = spiderRuleInfo.getJsMenuRules();
				String[] menuRuleArray = null;
//				点击的策略和规则都以#OR分割
				if(jsMenuStrategy.contains(SymbolConstants.SUB_FIRSTOR)) {
					if(jsMenuRules.contains(SymbolConstants.SUB_FIRSTOR)) {
//						多种策略，检查每个策略的可行性，正确为止
						String[] jsMenuStrategyArray = jsMenuStrategy.split(SymbolConstants.SUB_FIRSTOR);
						String[] jsMenuRuleArray = jsMenuRules.split(SymbolConstants.SUB_FIRSTOR);
//						根据策略权重从大到小排序，待开发
//						------
						if(jsMenuStrategyArray.length==jsMenuRuleArray.length) {
							Boolean checkFlag = false;
							for (int j = 0; j < jsMenuStrategyArray.length; j++) {
//								String menuDeStrategy = jsMenuStrategyArray[j];
								String menuDeRules = jsMenuRuleArray[j];
								menuRuleArray = menuDeRules.split(SymbolConstants.RULE_SPLIT_FLAG);
								sizeFlag = menuRuleArray.length;
								checkFlag = CheckClickRulesUtil.check(url, spiderRuleInfo, menuRuleArray[0], sizeFlag);
								if(checkFlag) {
									break;
								}
							}
						}else {
							LOG.error("--源{}，点击策略与点击规则不匹配！--", spiderRuleInfo.getWhiteId());
						}
					}else {
						clickFlag = false;
						LOG.error("--源{}，点击策略与点击规则不匹配！--", spiderRuleInfo.getWhiteId());
					}
				}else {
//					单个策略
					menuRuleArray = jsMenuRules.split(SymbolConstants.RULE_SPLIT_FLAG);
					sizeFlag = menuRuleArray.length;
				}
				Boolean invokeFlag = ReflectTypeMap.executeClick(MoreClickEnum.getName(sizeFlag), needClickFieldAry, menuRuleArray, clickFieldMap, spiderRuleInfo, url, crawlResult, resultList);
				if(!invokeFlag) {
					clickFlag = false;
					LOG.error("--链接{}，多层点击的执行出错--", url);
				}
			} else {
				clickFlag = false;
				LOG.error("--源{}，需点击字段的规则映射为NULL--", spiderRuleInfo.getWhiteId());
			}
		} else {
			clickFlag = false;
		}
		if(!clickFlag) {
			if (!spiderRuleInfo.getAjaxFlag()) {
//				静态for循环生成颜色，尺寸，库存的组合数据（不准确，不推荐）
				String colorArrayStr = crawlResult.getColor();
				colorArrayStr = colorArrayStr.replace(SymbolConstants.SPLIT_FLAG, SymbolConstants.COMMA);
				String sizeArrayStr = crawlResult.getSize();
				sizeArrayStr = sizeArrayStr.replace(SymbolConstants.SPLIT_FLAG, SymbolConstants.COMMA);
				String qtyArrayStr = crawlResult.getQty();
				String[] colorArray = colorArrayStr.split(SymbolConstants.COMMA);
				String[] sizeArray = sizeArrayStr.split(SymbolConstants.COMMA);
				String[] qtyArray = qtyArrayStr.split(SymbolConstants.COMMA);
				if(sizeArray.length!=qtyArray.length) {
					LOG.error("--源{}，静态获取数据，尺寸与库存不匹配！--", spiderRuleInfo.getWhiteId());
				}else {
					for (String colorStr : colorArray) {
						for (int i = 0; i < sizeArray.length; i++) {
							String sizeStr = sizeArray[i];
							String qtyStr = qtyArray[i];
							CrawlResult crawlResultNew = new CrawlResult();
							crawlResultNew = (CrawlResult) crawlResult.clone();
							crawlResultNew.setColor(colorStr);
							crawlResultNew.setSize(sizeStr);
							crawlResultNew.setQty(qtyStr);
							if (StringUtils.isNotBlank(crawlResultNew.getSpu())) {
								crawlResultNew.setSpu(crawlResultNew.getSpu()+colorStr);
								crawlResultNew.setProductModel(crawlResultNew.getSpu()+colorStr);
							}
							if(StringUtils.isNotBlank(crawlResultNew.getSpu())){
//								以spu和size定唯一
								crawlResultNew.setSppuHash(GatherUtil.longHashCode(crawlResultNew.getSpu()+crawlResultNew.getSize()));
							}else {
								crawlResultNew.setSppuHash(0L);
							}
							resultList.add(crawlResultNew);
						}
					}
				}
			}else {
				if (StringUtils.isNotBlank(crawlResult.getSpu())) {
					crawlResult.setProductModel(crawlResult.getSpu());
				}
				if(StringUtils.isNotBlank(crawlResult.getSpu())){
//					以spu和size定唯一
					crawlResult.setSppuHash(GatherUtil.longHashCode(crawlResult.getSpu()+crawlResult.getSize()));
				}else {
					crawlResult.setSppuHash(0L);
				}
				resultList.add(crawlResult);
			}
		}
		return resultList;

	}
	
}
