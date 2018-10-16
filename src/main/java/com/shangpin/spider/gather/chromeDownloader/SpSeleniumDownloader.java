package com.shangpin.spider.gather.chromeDownloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.httpClientDownloader.SpHttpClientDownloader;
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
	private SpHttpClientDownloader httpClientDownloader = null;
	
	
	
	
	public SpSeleniumDownloader(SpiderRules spiderRuleInfo, SpChromeDriverPool webDriverPool
			, Spider spiderDown, SpHttpClientDownloader httpClientDownloader) {
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
		if(spiderDown!=null) {
			this.httpClientDownloader = httpClientDownloader;
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
			
			String[] nextPageFlagArray = spiderRuleInfo.getNextPageFlag().split(SymbolConstants.AND_FLAG);
			String[] nextPageTagArray = spiderRuleInfo.getNextPageTag().split(SymbolConstants.RULE_SPLIT_FLAG);
			for (int i = 0; i < nextPageFlagArray.length; i++) {
				String nextPageFlagDe = nextPageFlagArray[i];
				String nextPageTagDe = nextPageTagArray[i];
//					控制下拉条
				if(StrategyConstants.SCROLL.equals(nextPageFlagDe)) {
					webDriver = handleScroll(url, webDriver);
					continue;
				}
//					下一页
				if(StrategyConstants.NEXT.equals(nextPageFlagDe)) {
					
					continue;
				}
//					查看更多
				if(StrategyConstants.MORE.equals(nextPageFlagDe)) {
					List<WebElement> elements = webDriver.findElements(By.cssSelector(nextPageTagDe));
					webDriver = handleMore(url, nextPageTagDe, webDriver, elements);
					continue;
				}
				
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
	
	/**
	 * 下拉条的处理
	 * @param url
	 * @param webDriver
	 * @return
	 */
	private RemoteWebDriver handleScroll(String url, RemoteWebDriver webDriver) {
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
		return webDriver;
	}
	/**
	 * 点击更多的处理
	 * @param url
	 * @param webDriver
	 * @return 
	 */
	private RemoteWebDriver handleMore(String url, String nextPageTagDe, RemoteWebDriver webDriver, List<WebElement> elements) {
		try {
			if(elements!=null&&elements.size()>0) {
				WebElement ele = elements.get(0);
				WebDriverWait wait = new WebDriverWait(webDriver, 1);
				wait.until(ExpectedConditions.elementToBeClickable(ele));
				ele.click();
				elements = webDriver.findElements(By.cssSelector(nextPageTagDe));
				webDriver = handleMore(url,nextPageTagDe,webDriver,elements);
			}
		} catch (Exception e) {
			LOG.error("点击更多处理有误！"+e.getMessage());
		}
		
		return webDriver;
	}
	
	/*private static Integer handleMoret(String url, String nextPageTagDe, Integer t, Integer i, Integer j) {
		if(i<5) {
			t++;
			j++;
			i++;
			t = handleMoret(url,nextPageTagDe,t,i,j);
		}
		
		return t;
	}
	
	public static void main(String[] args) {
		Integer t = handleMoret("www","sss",0,0,0);
		System.out.println(t);
	}*/
	
	/**
	 * 点击下一页的处理 
	 * @return 
	 */
	private RemoteWebDriver handleNext(String url, RemoteWebDriver webDriver) {
		
		return webDriver;
	}
	
	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
		
	}

}
