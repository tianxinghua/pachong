package com.shangpin.spider.gather.chromeDownloader;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.utils.CrackDspiderUtil;
import com.shangpin.spider.gather.utils.GatherUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

public class SpSeleniumDownloader implements Downloader{
	private static final Logger LOG = LoggerFactory
			.getLogger(SpSeleniumDownloader.class);
	
	private int sleepTime = 1000;
	private SpChromeDriverPool webDriverPool = null;
	private Spider spiderDown = null;
	private SpiderRules spiderRuleInfo = null;
	private HttpClientDownloader httpClientDownloader = null;
	
	
	
	
	public SpSeleniumDownloader(SpiderRules spiderRuleInfo, SpChromeDriverPool webDriverPool
			, Spider spiderDown) {
		super();
		if (spiderRuleInfo != null) {
			this.sleepTime = spiderRuleInfo.getSleep();
			this.spiderRuleInfo = spiderRuleInfo;
		}
		if (webDriverPool != null) {
			this.webDriverPool = webDriverPool;
		}
		if(spiderDown!=null) {
			this.spiderDown = spiderDown;
		}
	}
	
	

	@Override
	public Page download(Request request, Task task) {
		Page page = null;
		int innerSite = webDriverPool.innerSite();
		
		if(innerSite==100) {
			LOG.info("----webDriverPool初始化！");
		}
		RemoteWebDriver webDriver = null;
		Boolean uniqueFlag = true;
		String url = request.getUrl();
		
		
		if(GatherUtil.isLieUrl(url, spiderRuleInfo)) {
			try {
				webDriver = (RemoteWebDriver) initWebDriver(request,task,uniqueFlag);
			} catch (Exception e) {
				LOG.debug("---获取driver有误,{}",e.getMessage());
			}
			if (webDriver == null) {
				webDriverPool.shutdownEnd();
				LOG.info("---本次抓取提前结束，webDriver为空，导致失败！");
				return page;
			}
//			测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
			CrackDspiderUtil.crackMask(webDriver,spiderRuleInfo);
			webDriver.manage().window().maximize();
			// 控制下拉条
			if(StrategyConstants.SCROLL.equals(spiderRuleInfo.getNextPageFlag())) {
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
				
			}
//			下一页
			if(StrategyConstants.NEXT.equals(spiderRuleInfo.getNextPageFlag())) {
				
			}
//			查看更多
			if(StrategyConstants.MORE.equals(spiderRuleInfo.getNextPageFlag())) {
				
			}
			page = getHtml(webDriver, request, task);
			webDriverPool.returnToPool(webDriver);
			return page;
		}
		
		if(spiderRuleInfo.getFirstAjaxFlag()) {
			uniqueFlag = false;
			try {
				webDriver = (RemoteWebDriver) initWebDriver(request,task,uniqueFlag);
			} catch (Exception e) {
				LOG.debug("---获取driver有误,{}",e.getMessage());
			}
			if (webDriver == null) {
				webDriverPool.shutdownEnd();
				LOG.info("---本次抓取提前结束，webDriver为空，导致失败！");
				return page;
			}
//			第一层动态加载，相对静态耗时
			page = getHtml(webDriver, request, task);
			webDriverPool.returnToPool(webDriver);
		}else {
//			第一层静态处理，获取动态数据在第二层操作
			if(httpClientDownloader==null) {
				httpClientDownloader = new HttpClientDownloader();
			}
			page = httpClientDownloader.download(request,task);
		}
		return page;
	}

	private Page getHtml(RemoteWebDriver webDriver, Request request, Task task) {
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



	private WebDriver initWebDriver(Request request, Task task, Boolean uniqueFlag) {
		// TODO Auto-generated method stub
		WebDriver driver = webDriverPool.get(uniqueFlag);
		driver.get(request.getUrl());
		return driver;
	}



	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
		
	}

}
