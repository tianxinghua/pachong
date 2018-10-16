package com.shangpin.spider.gather.utils.ClickUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 
 * @author njt
 * @date 2018年10月16日 下午12:26:24
 * @desc 多层点击前需要执行的操作
 * ClickFrontUtils
 */
public class ClickFrontUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ClickFrontUtils.class);
	/**
	 * 处理多层点击前的操作
	 * @param driver
	 * @param spiderRuleInfo
	 * @return 
	 */
	public static void handle(ChromeDriver driver, SpiderRules spiderRuleInfo) {
		String firstClickFrontRules = spiderRuleInfo.getFirstClickFrontRules();
		String secondClickFrontRules = spiderRuleInfo.getSecondClickFrontRules();
		String threeClickFrontRules = spiderRuleInfo.getThreeClickFrontRules();
		String[] firstRuleArray = firstClickFrontRules.split(SymbolConstants.RULE_SPLIT_FLAG);
		String[] secondRuleArray = secondClickFrontRules.split(SymbolConstants.RULE_SPLIT_FLAG);
		String[] threeRuleArray = threeClickFrontRules.split(SymbolConstants.RULE_SPLIT_FLAG);
//		测试，先写了first
		handleFrontClick(driver,firstRuleArray,0,firstRuleArray.length);
//		待扩展
		
	}
	
	private static void handleFrontClick(ChromeDriver driver, String[] firstRuleArray, int i, int length) {
		if(length>0) {
			if(i<length) {
				try {
//					Actions action = new Actions(driver);
					String deRule = firstRuleArray[i];
					WebElement element = driver.findElement(By.cssSelector(deRule));
//					action.clickAndHold(element).perform();
//					action.release(element).perform();
					((JavascriptExecutor)driver).executeScript("arguments[0].click()", element);
					i++;
					handleFrontClick(driver,firstRuleArray,i,firstRuleArray.length);
				} catch (Exception e) {
					LOG.error("多层点击前的操作规则执行有误！{}",e.getMessage());
				}
			}
		}else {
			LOG.info("多层点击前的操作规则为空！");
		}
	}
}
