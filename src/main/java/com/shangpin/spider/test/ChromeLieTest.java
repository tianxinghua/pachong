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
		
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
//		options.addArguments("Content-Type: text/plain;charset=UTF-8");
		
//		options.addArguments("--headless");
		
		ChromeDriver driver = new ChromeDriver(caps);
		String url = "";
		url = "https://www.balenciaga.com/fr/femme/tout-pret-a-porter";
		driver.get(url);
		driver.manage().window().maximize();
		CrackDspiderUtil.crackMask(driver, null);
		handleScroll(url,driver);
		List<WebElement> elements = driver.findElements(By.cssSelector(".viewmore-btn"));
		handleMore(url, ".viewmore-btn", driver, elements);
		
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
	 * @return 
	 */
	private static RemoteWebDriver handleMore(String url, String nextPageTagDe, RemoteWebDriver webDriver, List<WebElement> elements) {
		try {
			if(elements!=null&&elements.size()>0) {
				WebElement ele = elements.get(0);
				WebDriverWait wait = new WebDriverWait(webDriver, 1);
				wait.until(ExpectedConditions.elementToBeClickable(ele));
				ele.click();
				elements = webDriver.findElements(By.cssSelector(nextPageTagDe));
				webDriver = handleMore(url,nextPageTagDe,webDriver,elements);
			}
		} catch (Exception e) {
			System.err.println("点击查看更多按钮有误！");
			e.printStackTrace();
		}
		
		return webDriver;
	}

}
