package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 检查多层点击策略的有效性（针对多种点击策略时）
 * @author njt
 * @date 2018年10月9日 下午5:22:37
 * @desc
 * CheckClickRulesUtil
 */
public class CheckClickRulesUtil {
	private static final Logger LOG = LoggerFactory.getLogger(CheckClickRulesUtil.class); 
	public static Boolean check(String url,SpiderRules spiderRuleInfo,String menuDeRule,int size) {
		Boolean flag = true;
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		caps.setCapability("cssSelectorsEnabled", true);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("Content-Type: text/plain;charset=UTF-8");
		options.addArguments("--headless");
		caps.setCapability("chromeOptions", options);
		System.setProperty("webdriver.chrome.driver", spiderRuleInfo.getChromeDriverPath());
		System.err.println("检查多层点击策略的有效性--：");
		ChromeDriver driver = new ChromeDriver(caps);
		driver.get(url);
		try {
			List<WebElement> elements = driver.findElements(By.cssSelector(menuDeRule));
			if(elements==null||elements.size()<=0) {
				flag = false;
			}
			/*for (WebElement ele : elements) {
				ele.click();
			}*/
		} catch (Exception e) {
			flag = false;
		}finally{
			driver.quit();
			System.err.println("----退出--！");
		}
		if(!flag) {
			LOG.info("--链接{}点击策略排除{}层--",url,size);
		}
		return flag;
	}

}
