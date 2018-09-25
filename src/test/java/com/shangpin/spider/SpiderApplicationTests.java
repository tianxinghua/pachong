package com.shangpin.spider;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {
	WebDriver driver;
//	访问网站的地址
	public static String baseUrl = "https://www.baidu.com";
//设定grid设定node的url地址，后续通过访问此地址连接到node计算机
	public static String nodeUrl = "http://192.168.15.205:6655/wd/hub";
	
	@Before
	public void beforeMethod() throws MalformedURLException {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setBrowserName("chrome");
		caps.setPlatform(Platform.WIN10);
		driver = new RemoteWebDriver(new URL(nodeUrl), caps);
	}
	@Test
	public void contextLoads() {
		driver.get(baseUrl);
		driver.findElement(By.id("kw")).sendKeys("新闻");
		driver.findElement(By.id("su")).click();
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver d) {
				return d.findElement(By.cssSelector("#rs > div")).getText().contains("相关搜索");
			}
		});
		Assert.assertTrue(driver.getPageSource().contains("新闻"));
		
	}
	@After
	public void afterMethod() {
		driver.quit();
	}
	

}
