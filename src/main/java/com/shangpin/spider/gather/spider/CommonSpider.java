package com.shangpin.spider.gather.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.RedisCache;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverClickPool;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverPool;
import com.shangpin.spider.gather.chromeDownloader.SpSeleniumDownloader;
import com.shangpin.spider.gather.downloader.WebDriverPool;
import com.shangpin.spider.gather.downloader.YcmSeleniumDownloader;
import com.shangpin.spider.gather.downloader.YcmWebDriverPool;
import com.shangpin.spider.gather.httpClientDownloader.SpHttpClientDownloader;
import com.shangpin.spider.gather.pipliner.MyPipeline;
import com.shangpin.spider.gather.processor.MyPageProcessor;
import com.shangpin.spider.gather.scheduler.MyRedisScheduler;
import com.shangpin.spider.redis.RedisManager;
import com.shangpin.spider.task.TaskManager;
import com.shangpin.spider.utils.common.ReflectUtil;

/*import com.xr.gather.gather.download.YcmSeleniumDownloader;
import com.xr.gather.gather.download.YcmWebDriverPool;
import com.xr.gather.gather.pipeline.MyPipeline;
import com.xr.gather.gather.processor.MyPageProcessor;
import com.xr.gather.gather.schedual.MyRedisScheduler;
import com.xr.gather.gather.task.TaskManager;
import com.xr.gather.model.SpiderRuleInfo;*/

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.thread.CountableThreadPool;

/**
 * @author njt
 * @date 创建时间：2017年11月22日 上午11:54:03
 * @version 1.0
 * @parameter
 */
@Component
public class CommonSpider {

	private Logger log = LoggerFactory.getLogger(CommonSpider.class);
	@Autowired
	private MyPipeline mypipeline;
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private MyRedisScheduler myRedisScheduler;
	@Autowired
	private RedisManager redisManager;
	@Value("${phantomjs_path}")
	private String phantomjsPath;
	@Value("${chrome_driver_path}")
	private String chromeDriverPath;
	private CountableThreadPool threadPool;
	private ExecutorService executorService;
	
	public void start(SpiderRules spiderRuleInfo) {
		MySpider spider = null;
		spiderRuleInfo.setChromeDriverPath(chromeDriverPath);
		if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(spiderRuleInfo.getThreadNum(), executorService);
            } else {
                threadPool = new CountableThreadPool(spiderRuleInfo.getThreadNum());
            }
        }
		SpHttpClientDownloader httpClientDownloader = new SpHttpClientDownloader();
		if (spiderRuleInfo.getAjaxFlag()) {
//			phantomjs加载
			/*
			 * YcmWebDriverPool pool = new YcmWebDriverPool( spiderRuleInfo.getThreadNum(),
			 * phantomjsPath, spiderRuleInfo); spider = makeSpider(spiderRuleInfo, pool);
			 * spider.setDownloader(new YcmSeleniumDownloader(spiderRuleInfo, pool,
			 * spider));
			 */
//	-------------------------------------------
//			模拟点击使用的pool
			SpChromeDriverClickPool driverClickPool = new SpChromeDriverClickPool(spiderRuleInfo.getThreadNum(),
					chromeDriverPath, spiderRuleInfo);
			spiderRuleInfo.setDriverPool(driverClickPool);
			SpChromeDriverPool pool = new SpChromeDriverPool(spiderRuleInfo.getThreadNum(), chromeDriverPath,
					spiderRuleInfo);
			spider = makeSpider(spiderRuleInfo, pool, driverClickPool);
			spider.setDownloader(new SpSeleniumDownloader(spiderRuleInfo, pool, spider, httpClientDownloader));
			spider.setScheduler(myRedisScheduler);
		} else {
			spider = makeSpider(spiderRuleInfo,threadPool);
			// 默认的httpclient加载
			/*
			 * String proxyHost = spiderRuleInfo.getProxyHost(); int proxyPort =
			 * spiderRuleInfo.getProxyPort(); if (StringUtils.isNotBlank(proxyHost) &&
			 * proxyPort > 0) {
			 * httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new
			 * Proxy(proxyHost,proxyPort))); }
			 */
			spider.setDownloader(httpClientDownloader);
			spider.setScheduler(new QueueScheduler());
		}

		String whiteName = spiderRuleInfo.getWhiteName();
		Integer taskCount = 0;
		List<RedisCache> redisList = redisManager.getRedisList();
		if (redisList != null && redisList.size() > 0) {
			for (RedisCache redisCache : redisList) {
				String webName = redisCache.getWebName();
				if (webName.equals(whiteName)) {
					taskCount = redisCache.getTaskCount();
					break;
				}
			}
		}
//		源链接为多个用#OR间隔
		if (taskCount == 0) {
			String sourceUrl = spiderRuleInfo.getSourceUrl();
			if (sourceUrl.contains(SymbolConstants.SUB_FIRSTOR)) {
				List<Request> list = new ArrayList<Request>();
				String[] sourceUrlArray = sourceUrl.split(SymbolConstants.SUB_FIRSTOR);
				for (String sourceUrlStr : sourceUrlArray) {
					Request request = new Request();
					request.setUrl(sourceUrlStr);
					Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("whiteId", spiderRuleInfo.getWhiteId());
					request.setExtras(extras);
					request.setMethod("get");
					list.add(request);
				}
				spider.startRequest(list);
			} else {
				Request request = new Request();
				request.setUrl(sourceUrl);
				Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("whiteId", spiderRuleInfo.getWhiteId());
				request.setExtras(extras);
				request.setMethod("get");
				spider.addRequest(request);
			}

		}
		// 设置pipeline,scheduler
		spider.addPipeline(mypipeline);
		spider.setExitWhenComplete(true);
		spider.thread(spiderRuleInfo.getThreadNum());
		taskManager.init(spiderRuleInfo.getWhiteName(), spider);
		spider.start();
		// spider.run();
	}

	/**
	 * 创建爬虫（静态页）
	 * 
	 * @param spiderRuleInfo
	 * @param threadPool2 
	 * @return
	 */
	private MySpider makeSpider(SpiderRules spiderRuleInfo, CountableThreadPool threadPool2) {
		log.info("创建爬虫！");
		MySpider spider = null;
//		获取需要点击的字段规则
		if (StringUtils.isNoneBlank(spiderRuleInfo.getClickFieldStr())) {
			String[] needClickFieldAry = spiderRuleInfo.getClickFieldStr().split(",");
			spiderRuleInfo.setNeedClickFieldAry(needClickFieldAry);
			Map<String, Map<String, String>> clickFieldMap = ReflectUtil.getClickFieldMap(spiderRuleInfo);
			spider = new MySpider(new MyPageProcessor(spiderRuleInfo, clickFieldMap, threadPool), spiderRuleInfo, threadPool);
		} else {
			spider = new MySpider(new MyPageProcessor(spiderRuleInfo, null, threadPool), spiderRuleInfo, threadPool);
		}
		return spider;
	}

	/**
	 * 创建爬虫(动态页)
	 * 
	 * @param spiderRuleInfo
	 * @param pool
	 * @return
	 */
	private MySpider makeSpider(SpiderRules spiderRuleInfo, WebDriverPool pool, WebDriverPool clickPool) {
		log.info("创建爬虫！");
		MySpider spider = null;
//		获取需要点击的字段规则
		if (StringUtils.isNoneBlank(spiderRuleInfo.getClickFieldStr())) {
			String[] needClickFieldAry = spiderRuleInfo.getClickFieldStr().split(",");
			spiderRuleInfo.setNeedClickFieldAry(needClickFieldAry);
			Map<String, Map<String, String>> clickFieldMap = ReflectUtil.getClickFieldMap(spiderRuleInfo);
			spider = new MySpider(new MyPageProcessor(spiderRuleInfo, clickFieldMap, threadPool), spiderRuleInfo, pool, clickPool, threadPool);
			spider.setUUID(spiderRuleInfo.getWhiteId().toString());
		} else {
			spider = new MySpider(new MyPageProcessor(spiderRuleInfo, null, threadPool), spiderRuleInfo, pool, clickPool, threadPool);
			spider.setUUID(spiderRuleInfo.getWhiteId().toString());
		}
		return spider;
	}

	/**
	 * 自定义spider
	 * 
	 * @author njt
	 *
	 */
	public class MySpider extends Spider {

		private Logger log = LoggerFactory.getLogger(MySpider.class);
		private final SpiderRules spiderRuleInfo;
		private WebDriverPool pool;
		private WebDriverPool clickPool;
		private CountableThreadPool threadPool;

		public MySpider(PageProcessor pageProcessor, SpiderRules spiderRuleInfo, CountableThreadPool threadPool) {
			super(pageProcessor);
			this.spiderRuleInfo = spiderRuleInfo;
			this.threadPool = threadPool;
		}

		public MySpider(PageProcessor pageProcessor, SpiderRules spiderRuleInfo, WebDriverPool pool,
				WebDriverPool clickPool, CountableThreadPool threadPool) {
			super(pageProcessor);
			this.spiderRuleInfo = spiderRuleInfo;
			this.pool = pool;
			this.clickPool = clickPool;
			this.threadPool = threadPool;
		}

		@Override
		protected void onSuccess(Request request) {
			super.onSuccess(request);
			// 抓取数量超过设置最大数量就停止
			int count = (int) (this.getPageCount());
			/*
			 * log.info("抓取数量为:" + count); if (count == 0) { log.info("爬虫" +
			 * spiderRuleInfo.getId() + "已处理页面" + count + ",退出。"); this.stop(); if
			 * (spiderRuleInfo.getAjaxFlag()) { // 停止phantomjs pool.shutdown(); }
			 * 
			 * }else
			 */
			if (count >= spiderRuleInfo.getMaxPageGather()) {
				log.info("爬虫" + spiderRuleInfo.getId() + "已处理页面" + count + ",超过设置最大量"
						+ spiderRuleInfo.getMaxPageGather() + "，退出。");
				this.stop();
				if (spiderRuleInfo.getAjaxFlag()) {
					// 停止phantomjs
//					pool.shutdown();
					pool.shutdownEnd();
					clickPool.shutdownEnd();
					threadPool.shutdown();
				}

			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			MySpider mySpider = (MySpider) o;

			return new EqualsBuilder().append(this.getUUID(), mySpider.getUUID()).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.getUUID()).toHashCode();
		}

		public WebDriverPool getPool() {
			return pool;
		}

		public void setPool(WebDriverPool pool) {
			this.pool = pool;
		}

		public WebDriverPool getClickPool() {
			return clickPool;
		}

		public void setClickPool(WebDriverPool clickPool) {
			this.clickPool = clickPool;
		}
		
		public CountableThreadPool getThreadPool() {
			return threadPool;
		}

		public void setThreadPool(CountableThreadPool threadPool) {
			this.threadPool = threadPool;
		}

		public SpiderRules getSpiderRuleInfo() {
			return spiderRuleInfo;
		}
	}

}
