package com.shangpin.spider.gather.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 
 * @author njt
 * @date 2018年10月11日 上午10:14:58
 * @desc 
 * CrackDspiderUtil
 */
public class CrackDspiderUtil {
	private static Logger LOG = LoggerFactory.getLogger(CrackDspiderUtil.class);
	
	/**
	 * 破解反爬的策略（按具体情况添加策略）
	 * @param driver
	 * @param spiderRuleInfo
	 */
	public static void crackMask(RemoteWebDriver driver, SpiderRules spiderRuleInfo) {
//		测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
//		CSS表达式从规则类中取出，待开发
		WebElement element = null;
		
		try {
			element = driver.findElement(By.cssSelector(".close-btn-small"));
			if(element!=null) {
				element.click();
			}
		} catch (Exception e) {
			LOG.error("点击前破解网站反爬有误1！{}",e.getMessage());
			try {
				element = driver.findElement(By.cssSelector(".mfp-close"));
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
