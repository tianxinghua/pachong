package com.shangpin.spider.gather.downloader;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

/**
 * @author njt
 * @date 创建时间：2017年10月30日 下午2:11:16
 * @version 1.0
 * @parameter
 */

public class YcmSeleniumDownloader implements Downloader {
	private static final Logger log = LoggerFactory
			.getLogger(YcmSeleniumDownloader.class);
	/**
	 * 1s
	 */
	private int sleepTime = 1000;
	private YcmWebDriverPool webDriverPool = null;
	private Spider spiderDown = null;
	
	public YcmSeleniumDownloader() {
	}

	public YcmSeleniumDownloader(int sleepTime, YcmWebDriverPool pool, int threadNum,
			Spider spider) {
		this.sleepTime = sleepTime;
		if (pool != null) {
			webDriverPool = pool;
		}
		if (spider != null) {
			spiderDown = spider;
		}
	}

	public YcmSeleniumDownloader setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	@Override
	public Page download(Request request, Task task) {
		Page pagexin = new Page();
		
		int innerSite = webDriverPool.innerSite();
		log.info("innerSite的数量为：" + innerSite);
		if (innerSite == 0) {
			webDriverPool.shutdown();
			
			String threadName = Thread.currentThread().getName();
			log.info("当前线程名："+threadName);
			log.info("当前线程下js个数为:" + innerSite);
		}

		WebDriver webDriver = null;
		try {
			webDriver = getWebDriver(request, task);
			log.info("webDriver为：" + webDriver);
		} catch (Exception e) {
			log.error("本次抓取异常结束，因某些原因导致获取webDriver错误！" + webDriver);
			// return null;
		}
		if (webDriver == null) {
			webDriverPool.shutdown();
			log.error("本次抓取提前结束，webDriver为空，导致失败！");
			// return null;
		}

		
		// 控制下拉条
		/*
		 * long height=(Long)((JavascriptExecutor)webDriver).executeScript(
		 * "return document.body.scrollHeight;"); String
		 * js="var q=document.documentElement.scrollTop=" +height;
		 * ((JavascriptExecutor)webDriver).executeScript(js+";");
		 */

		// 持续点击加载更多/显示更多
		// Page page = moreClick(pagexin,webDriver,request,task);

		// 点击下一页
		// Page page = nextPage(pagexin,webDriver,request,task);

		// 无需点击，直接获取源码html
		if (webDriver != null) {
			Page page = getHtml(pagexin, webDriver, request, task);
			webDriverPool.returnToPool(webDriver);
			return page;
		}
		int thraedDeathSite = webDriverPool.thraedDeathSite();
		log.info("thraedDeathSite的数量为：" + thraedDeathSite);
		if (thraedDeathSite >= 1) {
			log.info("js挂掉次数频繁，3分钟后，终止程序！");
			try {
				Thread.sleep(3 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			spiderDown.stop();
		}
		
		//pagexin.setDownloadSuccess(false);
		return null;
	}

	@Override
	public void setThread(int thread) {

	}

	/**
	 * 从WebDriver池中取出一个webDriver实例
	 * @param request
	 * @param task
	 * @return
	 */
	public WebDriver getWebDriver(Request request, Task task) {
		log.info("获取webdriver----------开始");
		WebDriver webDriver;
		try {
			webDriver = webDriverPool.get();
			log.info("获取webdriver----------开始--1");
		} catch (InterruptedException e) {
			log.info("获取webDriver异常！");
			e.printStackTrace();
			return null;
		}
		log.info("downloading page " + request.getUrl());
		if (webDriver == null) {
			log.info("获取webdriver----------null--1");
			return null;
		}
		try {
			webDriver.get(request.getUrl());
//			Thread.sleep(sleepTime);
			WebDriver.Options manage = webDriver.manage();
			Site site = task.getSite();
			log.info("task里 的site是什么： " + site);
			/*if (site.getCookies() != null) {
				for (Map.Entry<String, String> cookieEntry : site.getCookies()
						.entrySet()) {
					Cookie cookie = new Cookie(cookieEntry.getKey(),
							cookieEntry.getValue());
					manage.addCookie(cookie);
				}
			}*/
			manage.window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
			webDriverPool.returnToPool(webDriver);
			log.error("抓取链接可能有误！----将此webDriver返回webDriverPool");
		}
		return webDriver;
	}

	/**
	 *  点击持续加载更多
	 * @param pagexin
	 * @param webDriver
	 * @param request
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unused")
	private Page moreClick(Page pagexin, WebDriver webDriver, Request request,
			Task task) {
		boolean flag = true;
		Page page = new Page();
		try {
			while (flag) {
				WebElement element = webDriver.findElement(By
						.cssSelector(".get-more-line>a"));
				String text = element.getText();
				if (!"加载更多".equals(text)) {
					flag = false;
					break;
				}
				element.click();
				Thread.sleep(sleepTime);
				// flag=true;
				WebElement webElement = webDriver
						.findElement(By.xpath("/html"));
				String content = webElement.getAttribute("outerHTML");
				page.setRawText(content);
				page.setHtml(new Html(content, webDriver.getCurrentUrl()));
				page.setUrl(new PlainText(webDriver.getCurrentUrl()));
				page.setRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取加载按钮有误！");
		}
		return page;
	}

	/**
	 *  无需点击，只获取动态加载的html
	 * @param pagexin
	 * @param webDriver
	 * @param request
	 * @param task
	 * @return
	 */
	public Page getHtml(Page pagexin, WebDriver webDriver, Request request,
			Task task) {
//		切换网页frame捕捉相应元素时使用
//		webDriver.switchTo().frame("").findElement(By.xpath("/html"));
		WebElement webElement = webDriver.findElement(By.xpath("/html"));
//		String content = JavascriptExecutor.execute_script("return document.documentElement.outerHTML");
		String content = webElement.getAttribute("outerHTML");
		Page page = new Page();
		page.setRawText(content);
		page.setHtml(new Html(content, webDriver.getCurrentUrl()));
		page.setUrl(new PlainText(webDriver.getCurrentUrl()));
		page.setRequest(request);
		return page;
	}

	/**
	 *  点击下一页
	 * @param pagexin
	 * @param webDriver
	 * @param request
	 * @param task
	 * @return
	 */
	/*private Page nextPage(Page pagexin, WebDriver webDriver, Request request,
			Task task) {
		Page page = new Page();
		try {
			WebElement element = webDriver.findElement(By.linkText("下一页"));
			String cssStr = element.getCssValue("display");
			if ("none;".equals(cssStr)) {
				return null;
			}
			element.click();
			Thread.sleep(sleepTime);
			// flag=true;
			WebElement webElement = webDriver.findElement(By.xpath("/html"));
			String content = webElement.getAttribute("outerHTML");
			page.setRawText(content);
			page.setHtml(new Html(content));
			page.setUrl(new PlainText(webDriver.getCurrentUrl()));
			page.setRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("点击下一页按钮有误！");
		}
		return page;
	}*/

}
