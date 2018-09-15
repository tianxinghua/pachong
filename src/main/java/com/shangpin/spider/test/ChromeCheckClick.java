package com.shangpin.spider.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
/**
 * 
 * @author njt
 *
 */
public class ChromeCheckClick {
//	计数器，第一层的下标
	private static AtomicInteger init_i = new AtomicInteger(0);
//	计数器，第二层的下标
	private static AtomicInteger init_j = new AtomicInteger(0);
//	计数器，第二层的下标
	private static AtomicInteger endInt = new AtomicInteger(0);
	private static Integer firstSize = 0;
	private static Boolean recursionFlag = false;
	
	private static List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	
	public static void main(String[] args) {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(caps);
		String url = "https://fr.maje.com/fr/pret-a-porter/collection/robes/rosalba/H18ROSALBA.html?dwvar_H18ROSALBA_color=0501";
		url = "https://fr.maje.com/fr/pret-a-porter/selection/tartan/ripper/H18RIPPER.html?dwvar_H18RIPPER_color=1066";
		driver.get(url);
//		先以CSS为测试：
		
		
//		问题一，每点击一次，页面都会刷新一次，因此有些元素，尽管标签存在，但是无法获取到的，是个未来元素。
//		问题二，页面刷新后，不能再次选用上次选取的元素执行操作，是未知元素，在新页面中不存在。
		
		list = simulationClick(list,driver,init_i,init_j,recursionFlag);
		
		if(list!=null&&list.size()>0) {
			for (Map<String,String> map : list) {
				String color = map.get("color");
				String pics = map.get("pics");
				String qty = map.get("qty");
				String size = map.get("size");
				System.err.println("结果为：color:"+color+"--pics:"+pics+"---qty:"+qty+"---size:"+size+"\n");
			}
		}else {
			System.err.println("list为null");
		}
		
	}
	
//	两层动态元素
	private static List<Map<String,String>> simulationClick(List<Map<String,String>> list,ChromeDriver driver,AtomicInteger init_i,AtomicInteger init_j,Boolean recursionFlag) {
		int j = init_j.get();
		int i = init_i.get();
		
//		第一步，模拟点击第一个动态元素
		List<WebElement> elements1 = driver.findElements(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.color > div > ul > li > a"));
//		第一层动态元素的个数（一般第一层元素个数是不变的，后面层级的元素是随着父级改变的）
		firstSize = elements1.size();
		System.err.println("----\t第一层的元素个数为："+firstSize);
		if(recursionFlag||(i==0&&j==0)) {
			if(firstSize!=1) {
				init_j = new AtomicInteger(0);
				j = init_j.get();
				recursionFlag = false;
				WebElement element = elements1.get(i);
				element.click();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		List<WebElement> elements2 = driver.findElements(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li > a"));
		Integer sencondSize = elements2.size();
		System.err.println("----\t第二层的元素个数为："+sencondSize);
		WebElement element2 = null;
		if(j<=sencondSize-1) {
			element2 = elements2.get(j);
			element2.click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		第二层下标加1
		init_j.incrementAndGet();
		if(init_j.get()==sencondSize) {
			if(i!=firstSize-1) {
				init_i.incrementAndGet();
			}
			recursionFlag = true;
		}
		if(!recursionFlag||i!=firstSize-1) {
//			点击后获取的字段值，在此获取
			list = clickCrawlDate(list,driver);
//			递归			
			simulationClick(list,driver,init_i,init_j,recursionFlag);
		}
//		确保最后一次入库
		if(endInt.get()==0) {
			if(init_j.get()==sencondSize&&i==firstSize-1){
				list = clickCrawlDate(list,driver);
				endInt.incrementAndGet();
			}
		}
		
		return list;
	}
	
	private static List<Map<String, String>> clickCrawlDate(List<Map<String,String>> list,ChromeDriver driver) {
		WebElement colorEle = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.color > div > ul > li.selected > a"));
		String color = colorEle.getAttribute("title");
		List<WebElement> imgElements = driver.findElements(By.cssSelector(".img-product"));
		String pics = "";
		for (WebElement imgEle : imgElements) {
			pics += imgEle.getAttribute("src")+"|";
		}
		if(pics.contains("|")) {
			pics = pics.substring(0, pics.length()-1);
		}
		String qtyFlag = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li.selected")).getAttribute("class");
		int qty = 0;
		if(!qtyFlag.contains("unselectable")) {
			qty = 1;
		}
		String size = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li.selected > a > div.defaultSize")).getText();
		
		System.err.println("color:"+color+"\npics:"+pics+"\nqty:"+qty+"\nsize"+size);
		System.err.println("----------");
		Map<String,String> map = new HashMap<String,String>();
		map.put("color", color);
		map.put("pics", pics);
		map.put("qty", String.valueOf(qty));
		map.put("size", size);
		list.add(map);
		return list;
	}
	
}
