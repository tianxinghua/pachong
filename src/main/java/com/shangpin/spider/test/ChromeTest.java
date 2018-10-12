package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.shangpin.spider.gather.utils.CrackDspiderUtil;

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
		url = "https://www.thekooples.com/fr/veste-intemporelle-996470.html";
		driver.get(url);
		driver.manage().window().maximize();
		CrackDspiderUtil.crackMask(driver, null);
		List<WebElement> elements = driver.findElements(By.cssSelector("#product-options-wrapper > div.product-colors-wrapper > div > ul li a"));
		for (WebElement ele : elements) {
			ele.click();
		}
		
		
		
		
		
		
		
		
		
		
		
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
