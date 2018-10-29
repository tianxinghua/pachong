package com.shangpin.spider.gather.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 
 * @author njt
 * @date 2018年10月11日 上午10:14:58
 * @desc 破解反爬的策略（按具体情况添加策略）
 * CrackDspiderUtil
 */
public class CrackDspiderUtil {
	private static Logger LOG = LoggerFactory.getLogger(CrackDspiderUtil.class);
	
	/**
	 * @param driver
	 * @param spiderRuleInfo
	 */
	public static void crackMask(RemoteWebDriver driver, SpiderRules spiderRuleInfo) {
//		测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
//		CSS表达式从规则类中取出，待开发
		if(StringUtils.isBlank(spiderRuleInfo.getCrackDspiderStrategy())||StringUtils.isBlank(spiderRuleInfo.getCrackDspiderRules())) {
			return;
		}
		if(StringUtils.isNotBlank(spiderRuleInfo.getCrackDspiderRules())) {
			String[] rulesArray = spiderRuleInfo.getCrackDspiderRules().split(SymbolConstants.RULE_SPLIT_FLAG);
			switch(spiderRuleInfo.getCrackDspiderStrategy()) {
				case StrategyConstants.CLICK_X:
					closeX(driver, rulesArray);
			}
		}
		
	}
	
	/**
	 * 关闭遮罩
	 * @param driver
	 * @param rulesArray
	 */
	private static void closeX(RemoteWebDriver driver,String[] rulesArray) {
		WebElement element = null;
		try {
//			.close-btn-small
			element = driver.findElement(By.cssSelector(rulesArray[0]));
			if(element!=null) {
				element.click();
			}
		} catch (Exception e) {
			LOG.error("点击前破解网站反爬有误1！{}",e.getMessage());
			try {
//				.mfp-close
				element = driver.findElement(By.cssSelector(rulesArray[1]));
				if(element!=null) {
					element.click();
				}
			} catch (Exception e2) {
				LOG.error("点击前破解网站反爬有误2！{}",e2.getMessage());
//					e.printStackTrace();
			}
		}
	}

}
