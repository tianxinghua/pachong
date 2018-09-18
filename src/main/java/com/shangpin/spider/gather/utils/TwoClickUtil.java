package com.shangpin.spider.gather.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverClickPool;
import com.shangpin.spider.gather.crawlData.ReflectTypeMap;

/**
 * 需两层点击后获取值
 * @author njt
 * @date 创建时间：2018年9月13日 上午02:32:01
 * @version 1.0
 * @parameter
 */
public class TwoClickUtil {
	private static Logger LOG = LoggerFactory.getLogger(TwoClickUtil.class);
	
//	计数器，第一层的下标
	private static ThreadLocal<AtomicInteger> init_i = new ThreadLocal<AtomicInteger>();
//	private static AtomicInteger init_i = new AtomicInteger(0);
//	计数器，第二层的下标
	private static ThreadLocal<AtomicInteger> init_j = new ThreadLocal<AtomicInteger>();
//	private static AtomicInteger init_j = new AtomicInteger(0);
//	计数器，第二层的下标
	private static ThreadLocal<AtomicInteger> endLocalInt = new ThreadLocal<AtomicInteger>();
	private static AtomicInteger endInt = endLocalInt.get();
	private static ThreadLocal<Integer> firstLocalSize = new ThreadLocal<Integer>();
	private static Integer firstSize = firstLocalSize.get();
//	private static Boolean recursionFlag = false;
	/**
	 * 需点击字段及规则的映射
	 */
	private static Map<String,String> clickFieldRulesMap = null;
	
	private static ThreadLocal<List<Map<String,String>>> localList = new ThreadLocal<List<Map<String,String>>>();
	
	private static SpChromeDriverClickPool driverPool = null;
	
	private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	/**
	 * 
	 * @param needClickField 
	 * @param spiderRuleInfo
	 * @param url
	 * @param crawlResult
	 * @param page 
	 * @param page
	 * @param resultMap 
	 * @return 
	 */
	public static List<CrawlResult> crawlByClick(String[] needClickField, SpiderRules spiderRuleInfo, String url, CrawlResult crawlResult) {
		rwl.readLock().lock();
		endInt = new AtomicInteger(0);
		firstSize = 0;
		List<CrawlResult> crawlList = new ArrayList<CrawlResult>();
		if(spiderRuleInfo.getDriverPool()!=null) {
			driverPool = spiderRuleInfo.getDriverPool();
		}
		try {
			Class<?> ruleClass = Class.forName(SpiderRules.class.getName());
			Class<?> resultClass = Class.forName(CrawlResult.class.getName());
			Field[] ruleFields = ruleClass.getDeclaredFields();
			Field[] resultFields = resultClass.getDeclaredFields();
			clickFieldRulesMap = new HashMap<String,String>();
			for (String clickfield : needClickField) {
				for (Field ruleField : ruleFields) {
					ruleField.setAccessible(true);
					String ruleFieldName = ruleField.getName();
					if(ruleFieldName.contains(clickfield)&&ruleFieldName.contains("Rules")) {
						if(ruleFieldName.contains("Rules")) {
							String rulesStr = (String) ruleField.get(spiderRuleInfo);
							clickFieldRulesMap.put(clickfield, rulesStr);
							break;
						}
					}
				}
			}
//			规则暂时写死--测试
			
			List<Map<String, String>> resultList = handleClick(url);
			for (Map<String, String> map : resultList) {
				CrawlResult crawlResultNew = new CrawlResult();
				crawlResultNew = (CrawlResult) crawlResult.clone();
				for (String clickfield : needClickField) {
					if(map.containsKey(clickfield)) {
						String clickValue = map.get(clickfield);
						for (Field resultField : resultFields) {
							resultField.setAccessible(true);
							String typeName = resultField.getGenericType().toString();
							String resultFieldName = resultField.getName();
							if(clickfield.equals(resultFieldName)) {
								ReflectTypeMap.typeMap(typeName,clickValue,resultFieldName,resultField,crawlResultNew);
								break;
							}
						}	
					}
				}
//				特殊处理，spu后拼接colorNum
				String colorNum = map.get("colorNum");
				crawlResultNew.setSpu(crawlResultNew.getSpu()+colorNum);
				crawlResultNew.setProductModel(crawlResultNew.getSpu());
				String detailLink = crawlResultNew.getDetailLink();
				if(!detailLink.contains(colorNum)) {
					detailLink = detailLink.substring(0, detailLink.indexOf("=")+1)+colorNum;
					crawlResultNew.setDetailLink(detailLink);
				}
				crawlList.add(crawlResultNew);
			}
		} catch (IllegalArgumentException | IllegalAccessException e1 ) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			rwl.readLock().unlock();
		}
		return crawlList;
	}
	
	private static synchronized List<Map<String, String>> handleClick(String url) {
//		从Pool里取出driver
		ChromeDriver driver = null;
		List<Map<String,String>> resultList = null;
		try {
			if(driverPool!=null) {
				driver = (ChromeDriver) driverPool.get();
			}
			driver.get(url);
			AtomicInteger initI = init_i.get();
			initI = new AtomicInteger(0);
			AtomicInteger initJ = init_j.get();
			initJ = new AtomicInteger(0);
			ThreadLocal<Boolean> recursionLocalFlag = new ThreadLocal<Boolean>();
			Boolean recursionFlag = recursionLocalFlag.get();
			recursionFlag = false;
			List<Map<String, String>> list = localList.get();
			list = new ArrayList<Map<String, String>>();
			resultList = simulationClick(list,driver,initI,initJ,recursionFlag,url);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			driverPool.returnToPool(driver);
		}
		return resultList;
	}
	
	
//	两层动态元素
	private static synchronized List<Map<String,String>> simulationClick(List<Map<String,String>> list,ChromeDriver driver,AtomicInteger init_i,AtomicInteger init_j,Boolean recursionFlag, String url) {
		int j = init_j.get();
		int i = init_i.get();
		
//		第一步，模拟点击第一个动态元素
		List<WebElement> elements1 = null;
		try {
			elements1 = driver.findElements(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.color > div > ul > li > a"));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第一层元素NO SUCH ELEMENT,错误信息：{}",url,e.getMessage());
		}
		
//		第一层动态元素的个数（一般第一层元素个数是不变的，后面层级的元素是随着父级改变的）
		firstSize = elements1.size();
		System.err.println("----\t第一层的元素个数为："+firstSize);
		System.err.println("----\t第一层此时的下标为："+i);
		if(recursionFlag||(i==0&&j==0)) {
			if(firstSize!=1) {
				init_j = new AtomicInteger(0);
				j = init_j.get();
				recursionFlag = false;
				WebElement element = elements1.get(i);
				try {
					element.click();
				} catch (Exception e) {
					LOG.error("链接{}第一次点击事件有误！",url);
				}
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		List<WebElement> elements2 = null;
		try {
			elements2 = driver.findElements(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li > a"));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第二层元素NO SUCH ELEMENT,错误信息：{}",url,e.getMessage());
		}	
				
		Integer sencondSize = elements2.size();
		System.err.println("----\t第二层的元素个数为："+sencondSize);
		System.err.println("----\t第二层此时的下标为："+j);
		WebElement element2 = null;
		if(j<=sencondSize-1) {
			element2 = elements2.get(j);
			try {
				element2.click();
			} catch (Exception e) {
				LOG.error("链接{}第二次点击事件有误！",url);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		第二层下标加1
		init_j.incrementAndGet();
		if(init_j.get()>=sencondSize) {
			if(i!=firstSize-1) {
				init_i.incrementAndGet();
			}
			recursionFlag = true;
		}
		if((!recursionFlag)||(i!=firstSize-1)) {
//			点击后获取的字段值，在此获取
			list = clickCrawlDate(list,driver);
//			递归			
			simulationClick(list,driver,init_i,init_j,recursionFlag,url);
		}
//		确保最后一次入库
		if(endInt.get()==0) {
			if(init_j.get()==sencondSize&&i==firstSize-1){
				LOG.error("{}链接最后一次点击入库！",url);
				list = clickCrawlDate(list,driver);
				endInt.incrementAndGet();
			}
		}
		
		return list;
	}
	
	private static synchronized List<Map<String, String>> clickCrawlDate(List<Map<String,String>> list,ChromeDriver driver) {
		WebElement colorEle = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.color > div > ul > li.selected > a"));
		String color = colorEle.getAttribute("title");
		WebElement colorEleNum = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.color > div > ul > li.selected"));
		String colorNumStr = colorEleNum.getAttribute("class").trim();
		Pattern liePattern = Pattern.compile("\\d+");
		Matcher lieMatcger = liePattern.matcher(colorNumStr);
		String colorNum = "";
		if(lieMatcger.find()) {
			colorNum = lieMatcger.group();
		}
		
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
		
		System.err.println("\npics:"+pics+"\nqty:"+qty+"\tsize"+size+"\tcolor:"+color+"\tcolorNum:"+colorNum);
		System.err.println("----------");
		Map<String,String> map = new HashMap<String,String>();
		map.put("color", color);
		map.put("pics", pics);
		map.put("qty", String.valueOf(qty));
		map.put("size", size);
		map.put("colorNum", colorNum);
		list.add(map);
		return list;
	}
	
	/*public static void main(String[] args) {
		CrawlResult result = new CrawlResult();
		CrawlResult result2 = new CrawlResult();
		result.setForeignPrice(new BigDecimal(1));
		result2 = (CrawlResult) result.clone();
		System.out.println("--result2的值-"+result2.getForeignPrice());
		result2.setForeignPrice(new BigDecimal(2));
		System.out.println("---result的值为：--"+result.getForeignPrice());
		System.out.println("---result2的值为：--"+result2.getForeignPrice());
		
	}*/
	/*public static void main(String[] args) {
		String colorNumStr = "emptyswatch color0515";
		Pattern liePattern = Pattern.compile("\\d+");
		Matcher lieMatcger = liePattern.matcher(colorNumStr);
		String colorNum = "";
		if(lieMatcger.find()) {
			colorNum = lieMatcger.group();
		}
		System.out.println(colorNum);
		String detailLink = "https://fr.maje.com/fr/pret-a-porter/collection/blousons/basalt/E18BASALT.html?dwvar_E18BASALT_color=0067";
		String colorNum = "0515";
		if(!detailLink.contains(colorNum)) {
			detailLink = detailLink.substring(0, detailLink.indexOf("=")+1)+colorNum;
		}
		System.out.println(detailLink);
	}*/
}
