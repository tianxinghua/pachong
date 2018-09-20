package com.shangpin.spider.gather.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shangpin.spider.entity.gather.RedisCache;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverClickPool;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverPool;
import com.shangpin.spider.gather.chromeDownloader.SpSeleniumDownloader;
import com.shangpin.spider.gather.downloader.WebDriverPool;
import com.shangpin.spider.gather.downloader.YcmSeleniumDownloader;
import com.shangpin.spider.gather.downloader.YcmWebDriverPool;
import com.shangpin.spider.gather.pipliner.MyPipeline;
import com.shangpin.spider.gather.processor.MyPageProcessor;
import com.shangpin.spider.gather.scheduler.MyRedisScheduler;
import com.shangpin.spider.redis.RedisManager;
import com.shangpin.spider.task.TaskManager;

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


/**
 * @author njt
 * @date 创建时间：2017年11月22日 上午11:54:03
 * @version 1.0
 * @parameter
 */
@Component
public class CommonSpider{
	
	private  Logger log = LoggerFactory.getLogger(CommonSpider.class);
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

	public void start(SpiderRules spiderRuleInfo) {
		MySpider spider = null;
		if (spiderRuleInfo.getAjaxFlag()) {
//			phantomjs加载
			/*YcmWebDriverPool pool = new YcmWebDriverPool(
					spiderRuleInfo.getThreadNum(), phantomjsPath,
					spiderRuleInfo);
			spider = makeSpider(spiderRuleInfo, pool);
			spider.setDownloader(new YcmSeleniumDownloader(spiderRuleInfo, pool, spider));*/
//	-------------------------------------------
//			模拟点击使用的pool
			SpChromeDriverClickPool driverClickPool = new SpChromeDriverClickPool(spiderRuleInfo.getThreadNum(),chromeDriverPath);
			spiderRuleInfo.setDriverPool(driverClickPool);
			SpChromeDriverPool pool = new SpChromeDriverPool(
					spiderRuleInfo.getThreadNum(), chromeDriverPath,
					spiderRuleInfo);
			spider = makeSpider(spiderRuleInfo, pool);
			spider.setDownloader(new SpSeleniumDownloader(spiderRuleInfo, pool, spider));
			spider.setScheduler(myRedisScheduler);
		} else {
			spider = makeSpider(spiderRuleInfo);
			// 默认的httpclient加载
			HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
			/*String proxyHost = spiderRuleInfo.getProxyHost();
			int proxyPort = spiderRuleInfo.getProxyPort();
			if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
				httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyHost,proxyPort)));
			}*/
		    spider.setDownloader(httpClientDownloader);
			spider.setScheduler(new QueueScheduler());
		}
		
		String whiteName = spiderRuleInfo.getWhiteName();
		Integer taskCount = 0;
		List<RedisCache> redisList = redisManager.getRedisList();
		if(redisList!=null&&redisList.size()>0) {
			for (RedisCache redisCache : redisList) {
				String webName = redisCache.getWebName();
				if(webName.equals(whiteName)) {
					taskCount = redisCache.getTaskCount();
					break;
				}
			}
		}
		if(taskCount==0) {
			String sourceUrl = spiderRuleInfo.getSourceUrl();
			if(sourceUrl.contains("#OR")) {
				List<Request> list = new ArrayList<Request>();
				String[] sourceUrlArray = sourceUrl.split("#OR");
				for (String sourceUrlStr : sourceUrlArray) {
					Request request = new Request();
					request.setUrl(sourceUrlStr);
					Map<String, Object> extras = new HashMap<String,Object>();
					extras.put("whiteId", spiderRuleInfo.getWhiteId());
					request.setExtras(extras);
					request.setMethod("get");
					list.add(request);
				}
				spider.startRequest(list);
			}else {
				Request request = new Request();
				request.setUrl(sourceUrl);
				Map<String, Object> extras = new HashMap<String,Object>();
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
		//spider.run();
	}

	
	/**
	 * 创建爬虫（静态页）
	 * @param spiderRuleInfo
	 * @return
	 */
	private MySpider makeSpider(SpiderRules spiderRuleInfo) {
		log.info("创建爬虫！");
		MySpider spider = new MySpider(new MyPageProcessor(spiderRuleInfo),
				spiderRuleInfo);
		return spider;
	}

	/**
	 * 创建爬虫(动态页)
	 * @param spiderRuleInfo
	 * @param pool
	 * @return
	 */
	private MySpider makeSpider(SpiderRules spiderRuleInfo,
			WebDriverPool pool) {
		log.info("创建爬虫！");
		MySpider spider = new MySpider(new MyPageProcessor(spiderRuleInfo),
				spiderRuleInfo, pool);
		spider.setUUID(spiderRuleInfo.getWhiteId().toString());
		return spider;
	}

	/**
	 * 自定义spider
	 * @author njt
	 *
	 */
	public class MySpider extends Spider {

		private Logger log = LoggerFactory.getLogger(MySpider.class);
		private final SpiderRules spiderRuleInfo;
		private WebDriverPool pool;
		
		public MySpider(PageProcessor pageProcessor,
				SpiderRules spiderRuleInfo) {
			super(pageProcessor);
			this.spiderRuleInfo = spiderRuleInfo;
		}

		public MySpider(PageProcessor pageProcessor,
				SpiderRules spiderRuleInfo, WebDriverPool pool) {
			super(pageProcessor);
			this.spiderRuleInfo = spiderRuleInfo;
			this.pool = pool;
		}

		@Override
		protected void onSuccess(Request request) {
			super.onSuccess(request);
			// 抓取数量超过设置最大数量就停止
			int count = (int) (this.getPageCount());
			/*log.info("抓取数量为:" + count);
			if (count == 0) {
				log.info("爬虫" + spiderRuleInfo.getId() + "已处理页面" + count
						+ ",退出。");
				this.stop();
				if (spiderRuleInfo.getAjaxFlag()) {
					// 停止phantomjs
					pool.shutdown();
				}

			}else */
			if (count >= spiderRuleInfo.getMaxPageGather()) {
				log.info("爬虫" + spiderRuleInfo.getId() + "已处理页面" + count
						+ ",超过设置最大量" + spiderRuleInfo.getMaxPageGather()
						+ "，退出。");
				this.stop();
				if (spiderRuleInfo.getAjaxFlag()) {
					// 停止phantomjs
//					pool.shutdown();
					pool.shutdownEnd();
				}

			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o){
				return true;
			}
			if (o == null || getClass() != o.getClass()){
				return false;
			}

			MySpider mySpider = (MySpider) o;

			return new EqualsBuilder().append(this.getUUID(),
					mySpider.getUUID()).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.getUUID())
					.toHashCode();
		}

		public WebDriverPool getPool() {
			return pool;
		}

		public void setPool(WebDriverPool pool) {
			this.pool = pool;
		}

		public SpiderRules getSpiderRuleInfo() {
			return spiderRuleInfo;
		}
	}

}
