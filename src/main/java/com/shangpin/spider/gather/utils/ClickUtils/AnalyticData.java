package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.gather.utils.GatherUtil;

/**
 * 
 * @author njt
 * @date 2018年10月9日 下午7:32:34
 * @desc 解析动态数据
 * AnalyticData
 */
public class AnalyticData {
	private static Logger LOG = LoggerFactory.getLogger(AnalyticData.class);
	
	public static List<Map<String, String>> handleClickFieldRulesMap(String url, List<Map<String, String>> list,
			ChromeDriver driver, Map<String, Map<String, String>> clickFieldRulesMap) {
		Set<Entry<String, Map<String, String>>> entrySet = clickFieldRulesMap.entrySet();
		Iterator<Entry<String, Map<String, String>>> iterator = entrySet.iterator();
		Map<String, String> map = new HashMap<String, String>();
		LOG.info("开始动态解析{}网页数据-------：",url);
		while (iterator.hasNext()) {
			Entry<String, Map<String, String>> entry = iterator.next();
			String clickfield = entry.getKey().toString();
			Map<String, String> strategyAndRuleMap = entry.getValue();
			String clickfieldValue = GatherUtil.getFieldValue(url, clickfield, strategyAndRuleMap, driver);
			map.put(clickfield, clickfieldValue);
		}
		list.add(map);
		return list;
	}
}
