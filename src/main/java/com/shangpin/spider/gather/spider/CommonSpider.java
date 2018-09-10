package com.shangpin.spider.gather.spider;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.downloader.YcmSeleniumDownloader;
import com.shangpin.spider.gather.downloader.YcmWebDriverPool;
import com.shangpin.spider.gather.pipliner.MyPipeline;
import com.shangpin.spider.gather.processor.MyPageProcessor;
import com.shangpin.spider.gather.scheduler.MyRedisScheduler;
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
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
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
	@Value("${phantomjs_path}")
	private String phantomjsPath;

	public void start(SpiderRules spiderRuleInfo) {
		MySpider spider = null;
		if (spiderRuleInfo.getAjaxFlag()) {
			// phantomjs加载
			YcmWebDriverPool pool = new YcmWebDriverPool(
					spiderRuleInfo.getThreadNum(), phantomjsPath,
					spiderRuleInfo);
			spider = makeSpider(spiderRuleInfo, pool);
			spider.setDownloader(new YcmSeleniumDownloader(spiderRuleInfo
					.getSleep(), pool, spiderRuleInfo.getThreadNum(), spider));
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
		Request request = new Request(spiderRuleInfo.getSourceUrl());
		Map<String, Object> extras = new HashMap<String,Object>();
		extras.put("whiteId", spiderRuleInfo.getWhiteId());
		request.setExtras(extras);
		spider.addRequest(request);
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
			YcmWebDriverPool pool) {
		log.info("创建爬虫！");
		MySpider spider = new MySpider(new MyPageProcessor(spiderRuleInfo),
				spiderRuleInfo, pool);
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
		private YcmWebDriverPool pool;
		
		public MySpider(PageProcessor pageProcessor,
				SpiderRules spiderRuleInfo) {
			super(pageProcessor);
			this.spiderRuleInfo = spiderRuleInfo;
		}

		public MySpider(PageProcessor pageProcessor,
				SpiderRules spiderRuleInfo, YcmWebDriverPool pool) {
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
					pool.shutdown();
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

		public YcmWebDriverPool getPool() {
			return pool;
		}

		public void setPool(YcmWebDriverPool pool) {
			this.pool = pool;
		}

		public SpiderRules getSpiderRuleInfo() {
			return spiderRuleInfo;
		}
	}

}
