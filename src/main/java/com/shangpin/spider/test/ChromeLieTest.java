package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.SysexMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.shangpin.spider.gather.utils.CrackDspiderUtil;

public class ChromeLieTest {
	public static void main(String[] args) {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		caps.setCapability("enableNativeEvents", true);
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
//		options.addArguments("Content-Type: text/plain;charset=UTF-8");
		
//		options.addArguments("--headless");
		
		ChromeDriver driver = new ChromeDriver(caps);
		String url = "";
//		url = "https://www.balenciaga.com/fr/homme/chaussures";
//		url = "https://www.balenciaga.com/fr/femme/chaussures";
		url = "https://www.balenciaga.com/fr/femme/chaussures#{%22ytosQuery%22:%22true%22,%22department%22:%22wmnccshs_micro%22,%22gender%22:%22D%22,%22season%22:%22A,P,E%22,%22yurirulename%22:%22searchwithdepartmentgallery%22,%22agerange%22:%22adult%22,%22page%22:%2230%22,%22productsPerPage%22:%2224%22,%22suggestion%22:%22false%22,%22facetsvalue%22:[],%22totalPages%22:%224%22,%22rsiUsed%22:%22false%22,%22totalItems%22:%2288%22,%22partialLoadedItems%22:%2224%22,%22itemsToLoadOnNextPage%22:%2224%22,%22departments%22:[{%22id%22:%22wmnccshs_micro%22,%22page%22:4}]}";
		driver.get(url);
		driver.manage().window().maximize();
		CrackDspiderUtil.crackMask(driver, null);
		handleScroll(url,driver);
		List<WebElement> elements = driver.findElements(By.cssSelector(".viewmore-btn"));
		handleMore(url, ".viewmore-btn", driver, elements,elements.size(),0);
		
		/*WebElement webElement = driver.findElement(By.xpath("/html"));
		String content = webElement.getAttribute("outerHTML");
		System.err.println("--源码：---"+content);
		if(content.contains("cod11425886gf")) {
			System.out.println("TRUE");
		}else {
			System.out.println("FALSE");
		}*/
		List<WebElement> list = driver.findElements(By.xpath("//a[@class='item-display-image-container item-link']"));
		System.out.println("list的长度为："+list.size());
		
		System.out.println("完毕");
		
		
	}
	/**
	 * 下拉条的处理
	 * @param url
	 * @param webDriver
	 * @return
	 */
	private static RemoteWebDriver handleScroll(String url, RemoteWebDriver webDriver) {
		Map<String,Long> map = new HashMap<String,Long>();
		for (int i = 0; i < 100; i++) {
			((JavascriptExecutor)webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long heightFlag=(Long)((JavascriptExecutor)webDriver).executeScript("return document.body.scrollHeight;");
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
		
		long heightEnd=(Long)((JavascriptExecutor)webDriver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行后列表页"+url+"的--浏览器的高为："+heightEnd);
		return webDriver;
	}
	/**
	 * 点击更多的处理
	 * @param url
	 * @param webDriver
	 * @param i 
	 * @param size 
	 * @return 
	 */
	private static RemoteWebDriver handleMore(String url, String nextPageTagDe, RemoteWebDriver webDriver, List<WebElement> elements, int size, int i) {
		try {
			
//			Actions actions = new Actions(webDriver);
			if(i<size) {
				try {
					WebElement element = webDriver.findElement(By.cssSelector("#SearchboxReset > span.icn.icn-close"));
//					actions.click(element).build().perform();
//					element.click();
					((JavascriptExecutor)webDriver).executeScript("arguments[0].click()", element);
				} catch (Exception e) {
					System.err.println("执行多余的X号！"+e.getMessage());
				}
				WebElement ele = elements.get(i);
//				WebDriverWait wait = new WebDriverWait(webDriver, 1);
//				wait.until(ExpectedConditions.elementToBeClickable(ele));
//				ele.click();
//				Action action = actions.clickAndHold(ele).release().build();
//				action.perform();
				((JavascriptExecutor)webDriver).executeScript("arguments[0].click()", ele);
				WebElement webElement = webDriver.findElement(By.xpath("/html"));
				String content = webElement.getAttribute("outerHTML");
				System.err.println("--源码：---"+content);
				if(content.contains("cod11425886gf")) {
					System.out.println("TRUE");
				}else {
					System.out.println("FALSE");
				}
				i++;
				webDriver = handleMore(url,nextPageTagDe,webDriver,elements,size,i);
			}
		} catch (Exception e) {
			System.err.println("点击查看更多按钮有误！");
			e.printStackTrace();
		}
		
		return webDriver;
	}

}