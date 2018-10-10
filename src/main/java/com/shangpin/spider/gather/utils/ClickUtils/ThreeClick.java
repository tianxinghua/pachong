package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 
 * @author njt
 * @date 2018年10月9日 下午6:20:56
 * @desc
 * ThreeClick
 */
public class ThreeClick extends MoreClickUtil{
	
	private static Map<String, Map<String, String>> clickFieldRulesMap = null;
	
	@Override
	public void initClick(ChromeDriver driver) {
		clickFieldRulesMap = super.clickFieldRulesMap;
	}
	
	@Override
	public List<Map<String, String>> executeClick(ChromeDriver driver, String url, String[] menuRuleArray) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Map<String, String>> threeClick(List<Map<String, String>> list, ChromeDriver driver,
			AtomicInteger initI, AtomicInteger initJ, Boolean recursionFlag, String url, String[] menuRuleArray) {
		// TODO Auto-generated method stub
		return null;
	}


}
