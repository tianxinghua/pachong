package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.chrome.ChromeDriver;

import com.shangpin.spider.gather.utils.CrackDspiderUtil;

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
//		测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
		CrackDspiderUtil.crackMask(driver, super.spiderRuleInfo);
	}
	
	@Override
	public List<Map<String, String>> executeClick(ChromeDriver driver, String[] menuRuleArray, String oneClickedRules, String oneClickedStrategy) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Map<String, String>> threeClick(List<Map<String, String>> list, ChromeDriver driver,
			AtomicInteger initI, AtomicInteger initJ, Boolean recursionFlag, String[] menuRuleArray) {
		String url = driver.getCurrentUrl();
		return null;
	}


}
