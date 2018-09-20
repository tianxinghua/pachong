package com.shangpin.spider.gather.crawlData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public static void crawlData(Page page,SpiderRules spiderRuleInfo) {
		String url = page.getUrl().get();
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
				if(GatherUtil.filterField(resultFieldName)||GatherUtil.filterNeedClick(resultFieldName)) 
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
					if(i==FIELD_RULES_FLAG) {
						rulesStr = GatherUtil.getValue(page, "", strategyStr, rulesStr,resultFieldName);
					}
					ReflectTypeMap.typeMap(typeName,rulesStr,resultFieldName,resultField,crawlResult);
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
		
		List<CrawlResult> resultList = new ArrayList<CrawlResult>();
		crawlResult.setDetailLink(url);
		crawlResult.setWhiteId(spiderRuleInfo.getWhiteId());
		crawlResult.setSupplierId(spiderRuleInfo.getSupplierId());
		crawlResult.setSupplierNo(spiderRuleInfo.getSupplierNo());
		if(StringUtils.isNotBlank(crawlResult.getSpu())) {
			crawlResult.setProductModel(crawlResult.getSpu());
		}
		
		
/*//		处理颜色和尺寸的关系
		String colorArray = "";
		colorArray = GatherUtil.getValue(page, colorArray, spiderRuleInfo.getColorStrategy(), spiderRuleInfo.getColorRules(), "color");
		String sizeArray = "";
		sizeArray = GatherUtil.getValue(page, sizeArray, spiderRuleInfo.getSizeStrategy(), spiderRuleInfo.getSizeRules(), "size");
		String qtyArray = "";
		qtyArray = GatherUtil.getValue(page, qtyArray, spiderRuleInfo.getQtyStrategy(), spiderRuleInfo.getQtyRules(), "qty");
//		方法一：循环处理
		System.err.println("----颜色的数组----"+colorArray);
		System.err.println("----尺寸的数组----"+sizeArray);
		System.err.println("----库存的数组----"+qtyArray);*/
//		方法二：模拟点击
//		规则表中取出需点击获取值的字段
		String[] needClickField = {"color","size","qty","pics"};
		if(needClickField.length>0) {
			resultList = TwoClickUtil.crawlByClick(needClickField,spiderRuleInfo, url, crawlResult);
		}else {
			resultList.add(crawlResult);
		}
		page.putField("resultList", resultList);
		
	}
	
	
}
