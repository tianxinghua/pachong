package com.shangpin.spider.test;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeCheckScroll {
	public static void main(String[] args) {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
//		caps.setCapability(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,"D:/software/driver/chromedriver.exe");
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		
		
//		FirefoxDriver driver = new FirefoxDriver();
//		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("excludeSwitches", "[\"ignore-certificate-errors\"]");
//		
//		下拉成功--
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(caps);
		
		
		String url = "https://fr.maje.com/fr/pret-a-porter/collection/robes";
		driver.get(url);
		long height=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行前列表页"+url+"的--浏览器的高为："+height);
		Map<String,Long> map = new HashMap<String,Long>();
		for (int i = 0; i < 100; i++) {
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long heightFlag=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
			System.err.println("--执行第"+i+"次的列表页"+url+"的--浏览器的高为："+heightFlag);
			if(i>0) {
				int j = i-1;
				Long heightJudge = map.get(j+"");
				if(heightFlag==heightJudge) {
					System.err.println("滚动条被操作："+j+"次。");
					break;
				}
			}
			map.put(i+"", heightFlag);
		}
		
		long heightEnd=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行后列表页"+url+"的--浏览器的高为："+heightEnd);
		
		
		WebElement webElement = driver.findElement(By.xpath("/html"));
		String html = webElement.getAttribute("outerHTML");
//		System.err.println(html);
		
	}

}
