package com.shangpin.spider.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HttpUnitCheck {
	public static void main(String[] args) {
//		下拉测试失败---
		HtmlUnitDriver driver = new HtmlUnitDriver(true);
		
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
	}
}
