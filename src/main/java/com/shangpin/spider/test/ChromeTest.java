package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.utils.CrackDspiderUtil;
import com.shangpin.spider.gather.utils.ClickUtils.ClickFrontUtils;

public class ChromeTest {
	public static void main(String[] args) {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
//		options.addArguments("Content-Type: text/plain;charset=UTF-8");
		
//		options.addArguments("--headless");
		
		caps.setCapability("chromeOptions", options);
		
		/*Map<String,Object> imgSettings = new HashMap<String, Object>();
		imgSettings.put("images", 2);
		Map<String,Object> imgCapsSettings = new HashMap<String, Object>();
		imgCapsSettings.put("profile.default_content_settings", imgSettings);
		caps.setCapability("chrome.prefs", imgCapsSettings);*/
		
		ChromeDriver driver = new ChromeDriver(caps);
		String url = "";
//		url = "https://www.endclothing.com/gb/givenchy-logo-polo-bm709b3006-420.html";
//		url = "https://fr.maje.com/fr/pret-a-porter/selection/robes-violettes/ravira/H18RAVIRA.html?dwvar_H18RAVIRA_color=0102";
//		url = "https://www.thekooples.com/fr/veste-intemporelle-996470.html";
//		url = "https://www.balenciaga.com/fr/track-shoes_cod11583827ra.html";
//		url = "https://www.balenciaga.com/fr/femme/tout-pret-a-porter#{%22ytosQuery%22:%22true%22,%22department%22:%22viewallrtw_w%22,%22gender%22:%22D%22,%22season%22:%22A,P,E%22,%22yurirulename%22:%22searchwithdepartmentgallery%22,%22agerange%22:%22adult%22,%22page%22:%22184%22,%22productsPerPage%22:%2224%22,%22suggestion%22:%22false%22,%22facetsvalue%22:[],%22totalPages%22:%225%22,%22rsiUsed%22:%22false%22,%22totalItems%22:%22109%22,%22partialLoadedItems%22:%2224%22,%22itemsToLoadOnNextPage%22:%2224%22,%22departments%22:[{%22id%22:%22wmnjckts%22,%22page%22:1},{%22id%22:%22wmndnm%22,%22page%22:2},{%22id%22:%22wmncts%22,%22page%22:2},{%22id%22:%22wmntps%22,%22page%22:1},{%22id%22:%22wmnkntwr%22,%22page%22:1},{%22id%22:%22wmnshrts%22,%22page%22:1},{%22id%22:%22wmnjrs%22,%22page%22:2},{%22id%22:%22wmnskrtsdrsss%22,%22page%22:1},{%22id%22:%22wmnpnts%22,%22page%22:1},{%22id%22:%22rchtpwgllr%22,%22page%22:2}]}";
		url = "https://www.balenciaga.com/fr/t-shirt-sweatshirts_cod12189857ja.html#/fr/femme/jersey";
		driver.get(url);
		driver.manage().window().maximize();
		CrackDspiderUtil.crackMask(driver, null);
		SpiderRules spiderRuleInfo = new SpiderRules();
		spiderRuleInfo.setFirstClickFrontRules("#main > div > div.itempart.itemleading > div > div.itemleading-actions > div > section.accordionpanel.accordion-itemvariants-panel.color-panel.has-selection.is-collapsed > div.accordionpanel-header.accordionpanel-toggle");
		spiderRuleInfo.setSecondClickFrontRules("");
		spiderRuleInfo.setThreeClickFrontRules("");
//		测试多层点击前需要的点击规则
		ClickFrontUtils.handle(driver, spiderRuleInfo);
		try {
			WebElement element = driver.findElements(By.cssSelector("#item_selectcolor > div > div > ul > li > div")).get(0);
//			element.click();
			((JavascriptExecutor)driver).executeScript("arguments[0].click()", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String currentUrl = driver.getCurrentUrl();
		System.out.println("----链接:"+currentUrl);
		/*List<WebElement> elements = driver.findElements(By.cssSelector("#product-options-wrapper > div.product-colors-wrapper > div > ul li a"));
		for (WebElement ele : elements) {
			ele.click();
		}*/
		
		/*List<WebElement> elements = driver.findElements(By.cssSelector("#item_selectsize > div > div.selectSize.sizeWSelection.dropdown > ul > li"));
		for (WebElement ele : elements) {
			String ss = ele.getAttribute("class");
			System.out.println("----"+ss);
		}
		*/
		
		
		
		
		
		
		
		
		/*String material = "";
		WebElement element = driver.findElementByCssSelector("#pdpMain > div.product-list-images.clearfix > div.wrapper-tabs.hidden-small > div > div:nth-child(3)");
		if(element.isDisplayed()) {
			System.err.println("---显示----"+element.isDisplayed());
			material = element.getText();
		}else {
			System.err.println("---不显示----"+element.isDisplayed());
			material = element.getAttribute("textContent");
		}
		System.err.println("-------"+material);*/
		//		List<String> list = tt();
//		for (String url : list) {
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			driver.get(url);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String name = driver.findElement(By.cssSelector("#maincontent > div > div > div > section:nth-child(1) > div > div.page-title-wrapper.row.c-page-title.product > h1 > span")).getText();
			String price = driver.findElement(By.cssSelector(".price-wrapper > .price")).getText();
			System.err.println("---名称："+name+"\n---价格："+price);*/
			
//		}
			
		
	}
	
	
	/*private static List<String> tt() {
		List<String> list = new ArrayList<String>();
		list.add("https://www.endclothing.com/gb/givenchy-logo-polo-bm709b3006-420.html");
		list.add("https://www.endclothing.com/gb/givenchy-cuban-embroidered-star-collar-polo-bm700v3006-011.html");
		list.add("https://www.endclothing.com/gb/givenchy-logo-flip-flops-bhz003h06e-001.html");
		list.add("https://www.endclothing.com/gb/givenchy-low-sneaker-bm08219-876-116.html");
		list.add("https://www.endclothing.com/gb/givenchy-tennis-sneaker-bh0018h04p-115.html");
		list.add("https://www.endclothing.com/gb/givenchy-gv-7011-s-sunglasses-23057508655e4.html");
		list.add("https://www.endclothing.com/gb/givenchy-sweat-pant-bm503v3003-601.html");
		list.add("https://www.endclothing.com/gb/givenchy-reverse-logo-large-zipped-pouch-bk600jk08x-973.html");
		list.add("https://www.endclothing.com/gb/givenchy-stripe-waist-bag-bk502wk090-003.html");
		list.add("https://www.endclothing.com/gb/givenchy-logo-sock-bmb00k4037-001.html");
		list.add("https://www.endclothing.com/gb/givenchy-panelled-logo-tee-bm708u3002-001.html");
		list.add("https://www.endclothing.com/gb/givenchy-vintage-paris-logo-hoody-bm708p3003-604.html");
		return list;
	}*/
	

}
