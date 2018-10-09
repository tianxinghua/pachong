package com.shangpin.spider.gather.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.SymbolConstants;
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
public class MoreClickUtil {
	private static Logger LOG = LoggerFactory.getLogger(MoreClickUtil.class);
	
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
	private static Map<String,Map<String,String>> clickFieldRulesMap = null;
	
	private static ThreadLocal<List<Map<String,String>>> localList = new ThreadLocal<List<Map<String,String>>>();
	
	private static SpChromeDriverClickPool driverPool = null;
	
//	private static final ReentrantLock rtl = new ReentrantLock();
	/**
	 * 
	 * @param clickFieldMap 
	 * @param needClickField 
	 * @param spiderRuleInfo
	 * @param url
	 * @param crawlResult
	 * @param page 
	 * @param page
	 * @param resultMap 
	 * @return 
	 */
	public static List<CrawlResult> crawlByClick(String[] needClickFieldAry, Map<String, Map<String, String>> clickFieldMap, SpiderRules spiderRuleInfo, String url, CrawlResult crawlResult) {
//		rtl.lock();
		clickFieldRulesMap = clickFieldMap;
		endInt = new AtomicInteger(0);
		firstSize = 0;
		List<CrawlResult> crawlList = new ArrayList<CrawlResult>();
		if(spiderRuleInfo.getDriverPool()!=null) {
			driverPool = spiderRuleInfo.getDriverPool();
		}
		try {
			Class<?> resultClass = Class.forName(CrawlResult.class.getName());
			Field[] resultFields = resultClass.getDeclaredFields();
			
			String jsMenuRules = spiderRuleInfo.getJsMenuRules();
			String[] menuRuleArray = {};
			if(StringUtils.isNotBlank(jsMenuRules)) {
				if(jsMenuRules.contains(SymbolConstants.RULE_SPLIT_FLAG)) {
					menuRuleArray = jsMenuRules.split(SymbolConstants.RULE_SPLIT_FLAG);
				}else {
					LOG.info("---源-{}-的两层点击规则填写有误，缺@,间隔符。",spiderRuleInfo.getWhiteId());
				}
			}else {
				LOG.info("---源-{}-的两层点击规则为空！",spiderRuleInfo.getWhiteId());
				return null;
			}
			
			List<Map<String, String>> resultList = handleClick(url,menuRuleArray);
			if(resultList!=null&&resultList.size()>0) {
				for (Map<String, String> map : resultList) {
					CrawlResult crawlResultNew = new CrawlResult();
					crawlResultNew = (CrawlResult) crawlResult.clone();
					for (String clickfield : needClickFieldAry) {
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
//					特殊处理，spu后拼接colorNum
					/*String colorNum = map.get("colorNum");
					BigDecimal foreignPrice = BigDecimal.valueOf(Double.parseDouble(map.get("foreignPrice")));
					crawlResultNew.setSpu(crawlResultNew.getSpu()+colorNum);
					crawlResultNew.setProductModel(crawlResultNew.getSpu());
					crawlResultNew.setForeignPrice(foreignPrice);
					crawlResultNew.setSalePrice(foreignPrice);
					String detailLink = crawlResultNew.getDetailLink();
					if(!detailLink.contains(colorNum)) {
						detailLink = detailLink.substring(0, detailLink.indexOf("=")+1)+colorNum;
						crawlResultNew.setDetailLink(detailLink);
					}*/
					if(StringUtils.isNotBlank(crawlResultNew.getSpu())){
//						以spu和size定唯一
						crawlResultNew.setSppuHash(GatherUtil.longHashCode(crawlResultNew.getSpu()+crawlResultNew.getSize()));
					}else {
						crawlResultNew.setSppuHash(0L);
					}
					if (StringUtils.isNotBlank(crawlResultNew.getSpu())) {
						crawlResultNew.setProductModel(crawlResultNew.getSpu());
					}
					crawlList.add(crawlResultNew);
				}
			}else {
				LOG.info("链接{}点击后，数据为空！",url);
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e1 ) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
//			rtl.unlock();
			LOG.info("点击获取{}数据结束---！",url);
		}
		return crawlList;
	}
	
	private static List<Map<String, String>> handleClick(String url, String[] menuRuleArray) {
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
			resultList = simulationClick(list,driver,initI,initJ,recursionFlag,url,menuRuleArray);
			initI = new AtomicInteger(0);
			initJ = new AtomicInteger(0);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			driverPool.returnToPool(driver);
		}
		return resultList;
	}
	
	
//	两层动态元素
	private static List<Map<String,String>> simulationClick(List<Map<String,String>> list,ChromeDriver driver,AtomicInteger initI,AtomicInteger initJ,Boolean recursionFlag, String url, String[] menuRuleArray) {
		int j = initJ.get();
		int i = initI.get();
		
//		第一步，模拟点击第一个动态元素
		List<WebElement> elements1 = null;
		try {
			elements1 = driver.findElements(By.cssSelector(menuRuleArray[0]));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第一层元素NO SUCH ELEMENT,错误信息：{}",url,e.getMessage());
		}
		
//		第一层动态元素的个数（一般第一层元素个数是不变的，后面层级的元素是随着父级改变的）
		firstSize = elements1.size();
		System.err.println("----\t第一层的元素个数为："+firstSize);
		System.err.println("----\t第一层此时的下标为："+i);
		if(i+1>firstSize) {
			System.err.println("----\t第一层下标越界。");
			return list;
		}
		WebElement element = null;
		if(recursionFlag||(i==0&&j==0)) {
			if(firstSize!=1) {
				initJ = new AtomicInteger(0);
				j = initJ.get();
				recursionFlag = false;
				element = elements1.get(i);
				try {
//					WebDriverWait wait = new WebDriverWait(driver, 10);
//					wait.until(ExpectedConditions.elementToBeClickable(element));
					element.click();
				} catch (Exception e) {
					LOG.error("链接{}第一次点击事件有误！",url);
				}
//				避免点击频繁，异步加载
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		List<WebElement> elements2 = null;
		try {
			elements2 = driver.findElements(By.cssSelector(menuRuleArray[1]));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第二层元素NO SUCH ELEMENT,错误信息：{}",url,e.getMessage());
		}	
				
		Integer sencondSize = elements2.size();
		System.err.println("----\t第二层的元素个数为："+sencondSize);
		System.err.println("----\t第二层此时的下标为："+j);
		if(j+1>sencondSize) {
			System.err.println("----\t第二层下标越界。");
			return list;
		}
		WebElement element2 = null;
		if(j<=sencondSize-1) {
			element2 = elements2.get(j);
			try {
				WebDriverWait wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.elementToBeClickable(element2));
				element2.click();
			} catch (Exception e) {
				LOG.error("链接{}第二次点击事件有误！--异常:{}",url,e.getMessage());
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		第二层下标加1
		initJ.incrementAndGet();
		if(initJ.get()>=sencondSize) {
			if(i!=firstSize-1) {
				initI.incrementAndGet();
			}
			recursionFlag = true;
		}
		if((!recursionFlag)||(i!=firstSize-1)) {
//			点击后获取的字段值，在此获取
//			list = clickCrawlDate(list,driver);
			list = handleClickFieldRulesMap(url,list,driver);
//			递归			
			simulationClick(list,driver,initI,initJ,recursionFlag,url,menuRuleArray);
		}
//		确保最后一次入库
		if(endInt.get()==0) {
			if(initJ.get()==sencondSize&&i==firstSize-1){
				LOG.info("{}链接最后一次点击入库！",url);
//				list = clickCrawlDate(list,driver);
				list = handleClickFieldRulesMap(url,list,driver);
				endInt.incrementAndGet();
			}
		}
		
		return list;
	}
	
	private static List<Map<String, String>> handleClickFieldRulesMap(String url, List<Map<String,String>> list,ChromeDriver driver) {
		Set<Entry<String, Map<String, String>>> entrySet = clickFieldRulesMap.entrySet();
		Iterator<Entry<String, Map<String, String>>> iterator = entrySet.iterator();
		Map<String,String> map = new HashMap<String,String>();
		while(iterator.hasNext()) {
			Entry<String, Map<String, String>> entry = iterator.next();
			String clickfield = entry.getKey().toString();
			Map<String, String> strategyAndRuleMap = entry.getValue();
			String clickfieldValue = GatherUtil.getFieldValue(url, clickfield, strategyAndRuleMap, driver);
			map.put(clickfield, clickfieldValue);
		}
		list.add(map);
		return list;
	}
	
	/*private static synchronized List<Map<String, String>> clickCrawlDate(List<Map<String,String>> list,ChromeDriver driver) {
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
			qty = 5;
		}
		String size = driver.findElement(By.cssSelector("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li.selected > a > div.defaultSize")).getText();
		String foreignPrice = driver.findElement(By.cssSelector("#pdpMain > div.wrapper-product-image-container.clearfix > div.product-col-2.product-detail > div.productPrices > div > span")).getAttribute("content").toString();
		
		System.err.println("\npics:"+pics+"\nqty:"+qty+"\tsize"+size+"\tcolor:"+color+"\tcolorNum:"+colorNum+"\tforeignPrice:"+foreignPrice);
		System.err.println("----------");
		Map<String,String> map = new HashMap<String,String>();
		map.put("color", color);
		map.put("pics", pics);
		map.put("qty", String.valueOf(qty));
		map.put("size", size);
		map.put("colorNum", colorNum);
		map.put("foreignPrice", foreignPrice);
		list.add(map);
		return list;
	}*/

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
