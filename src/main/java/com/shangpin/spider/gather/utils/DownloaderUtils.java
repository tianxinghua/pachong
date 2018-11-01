package com.shangpin.spider.gather.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverPool;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

public class DownloaderUtils {
	private static final Logger LOG = LoggerFactory.getLogger(DownloaderUtils.class);

	/**
	 * 列表翻页的处理
	 * 
	 * @param url
	 * @param webDriver
	 * @param spiderRuleInfo
	 * @param page
	 * @param request
	 * @param task
	 * @param uniqueFlag
	 * @param webDriverPool
	 * @return
	 */
	public static Page driverNextPage(String url, RemoteWebDriver webDriver, SpiderRules spiderRuleInfo, Page page,
			Request request, Task task, Boolean uniqueFlag, SpChromeDriverPool webDriverPool) {
		try {
			webDriver = (RemoteWebDriver) initWebDriver(request, task, uniqueFlag, webDriverPool);
		} catch (Exception e) {
			LOG.debug("---获取driver有误,{}", e.getMessage());
		}
		if (webDriver == null) {
			webDriverPool.shutdownEnd();
			LOG.info("---本次抓取提前结束，webDriver为空，导致失败！");
			return page;
		}
//			测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
		CrackDspiderUtil.crackMask(webDriver, spiderRuleInfo);
		webDriver.manage().window().maximize();
		
		if(StringUtils.isNotBlank(spiderRuleInfo.getNextPageFlag().trim())&&StringUtils.isNotBlank(spiderRuleInfo.getNextPageTag().trim())) {
			String[] nextPageFlagArray = spiderRuleInfo.getNextPageFlag().split(SymbolConstants.AND_FLAG);
			String[] nextPageTagArray = spiderRuleInfo.getNextPageTag().split(SymbolConstants.RULE_SPLIT_FLAG);
			for (int i = 0; i < nextPageFlagArray.length; i++) {
				String nextPageFlagDe = nextPageFlagArray[i];
				String nextPageTagDe = nextPageTagArray[i];
	//					控制下拉条
				if (StrategyConstants.SCROLL.equals(nextPageFlagDe)) {
					webDriver = handleScroll(url, webDriver);
					continue;
				}
	//					下一页，待开发
				if (StrategyConstants.NEXT.equals(nextPageFlagDe)) {
	
					continue;
				}
	//					查看更多
				if (StrategyConstants.MORE.equals(nextPageFlagDe)) {
					List<WebElement> elements = webDriver.findElements(By.cssSelector(nextPageTagDe));
					webDriver = handleMore(url, nextPageTagDe, webDriver, elements, elements.size(), 0);
					continue;
				}
	
			}
		}
		page = getHtml(webDriver, request, task);
		webDriverPool.returnToPool(webDriver);
		return page;
	}

	public static Page getHtml(RemoteWebDriver webDriver, Request request, Task task) {
		WebElement webElement = webDriver.findElement(By.xpath("/html"));
		String content = webElement.getAttribute("outerHTML");
//		System.err.println("--源码：---"+content);
		Page page = new Page();
		page.setRawText(content);
		page.setHtml(new Html(content, webDriver.getCurrentUrl()));
		page.setUrl(new PlainText(webDriver.getCurrentUrl()));
		page.setRequest(request);
		return page;
	}

	public static WebDriver initWebDriver(Request request, Task task, Boolean uniqueFlag,
			SpChromeDriverPool webDriverPool) {
		WebDriver driver = webDriverPool.get(uniqueFlag);
		driver.get(request.getUrl());
		return driver;
	}

	/**
	 * 下拉条的处理
	 * 
	 * @param url
	 * @param webDriver
	 * @return
	 */
	private static RemoteWebDriver handleScroll(String url, RemoteWebDriver webDriver) {
		Map<String, Long> map = new HashMap<String, Long>();
		for (int i = 0; i < 100; i++) {
			((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long heightFlag = (Long) ((JavascriptExecutor) webDriver)
					.executeScript("return document.body.scrollHeight;");
			System.err.println("--执行第" + i + "次的列表页" + url + "的--浏览器的高为：" + heightFlag);
			if (i > 0) {
				int j = i - 1;
				Long heightJudge = map.get(j + "");
				if (heightFlag == heightJudge) {
					System.err.println("滚动条被操作：" + j + "次。");
					break;
				}
			}
			map.put(i + "", heightFlag);
		}

		long heightEnd = (Long) ((JavascriptExecutor) webDriver).executeScript("return document.body.scrollHeight;");
		System.err.println("--执行后列表页" + url + "的--浏览器的高为：" + heightEnd);
		return webDriver;
	}

	/**
	 * 点击更多的处理
	 * 
	 * @param url
	 * @param webDriver
	 * @return
	 */
	private static RemoteWebDriver handleMore(String url, String nextPageTagDe, RemoteWebDriver webDriver,
			List<WebElement> elements, int size, int i) {
		try {
			if (i < size) {
				try {
					WebElement element = webDriver.findElement(By.cssSelector("#SearchboxReset > span.icn.icn-close"));
					((JavascriptExecutor) webDriver).executeScript("arguments[0].click()", element);
				} catch (Exception e) {
					System.err.println("执行多余的X号！" + e.getMessage());
				}
				WebElement ele = elements.get(i);
				((JavascriptExecutor) webDriver).executeScript("arguments[0].click()", ele);
				i++;
				webDriver = handleMore(url, nextPageTagDe, webDriver, elements, size, i);
			}
		} catch (Exception e) {
			LOG.error("点击更多处理有误！" + e.getMessage());
		}

		return webDriver;
	}

	/**
	 * 点击下一页的处理
	 * 
	 * @return
	 */
	private RemoteWebDriver handleNext(String url, RemoteWebDriver webDriver) {

		return webDriver;
	}

	/*
	 * private static Integer handleMoret(String url, String nextPageTagDe, Integer
	 * t, Integer i, Integer j) { if(i<5) { t++; j++; i++; t =
	 * handleMoret(url,nextPageTagDe,t,i,j); }
	 * 
	 * return t; }
	 * 
	 * public static void main(String[] args) { Integer t =
	 * handleMoret("www","sss",0,0,0); System.out.println(t); }
	 */
}
