package com.shangpin.spider.gather.chromeDownloader;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.httpClientDownloader.SpHttpClientDownloader;
import com.shangpin.spider.gather.utils.DownloaderUtils;
import com.shangpin.spider.gather.utils.GatherUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;

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
		if (GatherUtil.isLieUrl(url, spiderRuleInfo)) {
			return DownloaderUtils.driverNextPage(url, webDriver, spiderRuleInfo, page, request, task, uniqueFlag, webDriverPool);
		}
		
//		详情页运行第一次时，非点击数据的获取策略（静态数据）
		if(spiderRuleInfo.getFirstAjaxFlag()) {
			uniqueFlag = false;
			try {
				webDriver = (RemoteWebDriver) DownloaderUtils.initWebDriver(request,task,uniqueFlag,webDriverPool);
			} catch (Exception e) {
				LOG.debug("---获取driver有误,{}",e.getMessage());
			}
			if (webDriver == null) {
				webDriverPool.shutdownEnd();
				LOG.info("---本次抓取提前结束，webDriver为空，导致失败！");
				return page;
			}
//			第一层动态加载，相对静态耗时
			page = DownloaderUtils.getHtml(webDriver, request, task);
			webDriverPool.returnToPool(webDriver);
		}else {
//			第一层静态处理，获取动态数据在第二层操作
			page = httpClientDownloader.download(request,task);
		}
		return page;
	}

	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
	}

}
