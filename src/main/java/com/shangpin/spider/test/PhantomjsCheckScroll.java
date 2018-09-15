package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomjsCheckScroll {
	public static void main(String[] args) {
//		下拉失败---
		DesiredCapabilities caps = DesiredCapabilities.phantomjs();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		/*List<String> cmdList = new ArrayList<>();  
        // 禁用图片  
        cmdList.add("--load-images=false");  
        // 本地缓存  
        cmdList.add("--disk-cache=true");
        cmdList.add("--web-security=false");
        cmdList.add("--ssl-protocol=any");
        cmdList.add("--ignore-ssl-errors=true");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cmdList);*/
		
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX+ "User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"D:/software/software/phantomjs-2.1.1-windows/bin/phantomjs.exe");
		
		
		
		PhantomJSDriver driver = new PhantomJSDriver(caps);
        //设置隐性等待（作用于全局）
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		String url = "https://fr.maje.com/fr/pret-a-porter/collection/robes";
		driver.get(url);
		long height=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行前列表页"+url+"的--浏览器的高为："+height);
		for (int i = 0; i < 10; i++) {
			((JavascriptExecutor)driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		long height2=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行后列表页"+url+"的--浏览器的高为："+height2);
		
		
		WebElement webElement = driver.findElement(By.xpath("/html"));
		String html = webElement.getAttribute("outerHTML");
		System.err.println(html);
		
		
		
		/*String url = "https://fr.maje.com/fr/pret-a-porter/collection/robes";
		driver.get(url);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long height1=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行前列表页"+url+"的--浏览器的高为："+height1);
		WebElement element = driver.findElement(By.cssSelector("#primary > div.bottom-of-pages > p > span"));
		int y = element.getLocation().getY();
		System.err.println("--Y的---值为："+y);
//		String js = String.format("window.scroll(0, %s)", y);
//		((JavascriptExecutor)driver).executeScript(js, new Object[]{});
		String js = String.format("window.scrollBy(0, %s)",60000);
		driver.executeScript(js);
		long height2=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行后列表页"+url+"的--浏览器的高为："+height2);
		
		String js="scroll(0,60000);";
		((JavascriptExecutor)driver).executeScript(js, new Object[]{});
		PhantomJSDriver.Options manage = driver.manage();
		manage.window().maximize();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement element2 = driver.findElement(By.xpath("/html"));
		String content = element2.getAttribute("outerHTML");
		System.err.println("--源码：---"+content);
		driver.quit();*/
		
	}

}
