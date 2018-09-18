package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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
		options.addArguments("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		options.addArguments("Content-Type: text/plain;charset=UTF-8");
		
		caps.setCapability("chromeOptions", options);
		ChromeDriver driver = new ChromeDriver(caps);
		String url = "https://www.endclothing.com/gb/givenchy-logo-polo-bm709b3006-420.html";
//		List<String> list = tt();
//		for (String url : list) {
			try {
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
			System.err.println("---名称："+name+"\n---价格："+price);
			
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
