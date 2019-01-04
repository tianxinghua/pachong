package com.shangpin.iog.MrMrs;

import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class XinghuaClickTest {
	public static String cookieStr="";
	public static ChromeDriver driver=null;
	public static void main(String[] args) {
		XinghuaClickTest object=new XinghuaClickTest();
		object.getCookies();
	}
	public static void getCookies(){
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		// css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		caps.setCapability("enableNativeEvents", true);

		caps.setCapability("browserConnectionEnabled", true);
		caps.setCapability("databaseEnabled", true);
		caps.setCapability("networkConnectionEnabled", true);
		caps.setCapability("rotatable", true);
		caps.setCapability("applicationCacheEnabled", true);
		caps.setCapability("acceptInsecureCerts", true);
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		driver = new ChromeDriver(caps);
		String url = "https://www.mmi.it/cn/";
		driver.get(url);
		driver.manage().window().maximize();
		//点击EUR
		WebElement element6 = driver.findElement(By.cssSelector("body > div.wrapper > div > div.header-container.type9 > div.top-links-container > div > div.form-currency.top-select > span > div.select > b > i"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element6);

		WebElement element7 = driver.findElement(By.cssSelector("body > div.wrapper > div > div.header-container.type9 > div.top-links-container > div > div.form-currency.top-select > span > div.dropdown > ul > li.selected.sel > a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element7);
//		点击运到-意大利
		WebElement element = driver.findElement(By.cssSelector("#select-country > span > div.select > div"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);

		WebElement element2 = driver.findElement(By.cssSelector("#ui-id-1 > span"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element2);

		WebElement element3 = driver.findElement(By.cssSelector(".ui-accordion-content-active #IT"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element3);

//		点击语言-中国
		WebElement element4 = driver.findElement(By.cssSelector("#select-language > span > div.select > b > i"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element4);

		WebElement element5 = driver.findElement(By.cssSelector("#eu_cn"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", element5);
//
		StringBuffer buffer = new StringBuffer();
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			System.err.println("名称："+cookie.getName()+"---值："+cookie.getValue());
			buffer.append(cookie.getName());
			buffer.append("=");
			buffer.append(cookie.getValue());
			buffer.append("; ");
		}
		cookieStr=buffer.toString().substring(0,buffer.toString().length()-2);
		//driver.quit();
		//System.out.println("cookie为："+buffer.toString().trim());
		//return buffer.toString().substring(0,buffer.toString().length()-2);

	}
	public static void closedriver(){
		driver.quit();
	}
}
