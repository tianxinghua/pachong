package com.shangpin.spider.gather.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.crawlData.CommonCrawlData;
import com.shangpin.spider.gather.utils.GatherUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyPageProcessor implements PageProcessor {
	private static Logger LOG = LoggerFactory.getLogger(MyPageProcessor.class);
	private Site site;
	private SpiderRules spiderRuleInfo;
	

	public MyPageProcessor(SpiderRules spiderRuleInfo) {
		// 域名
		String domain = GatherUtil.getFefferrerHost(spiderRuleInfo
				.getSourceUrl());
		spiderRuleInfo.setChannelRules(domain);
		this.site = Site.me()
				.setUserAgent(spiderRuleInfo.getUserAgent())
				.setTimeOut(spiderRuleInfo.getTimeout())
				.setRetryTimes(spiderRuleInfo.getRetryNum())
				.setSleepTime(spiderRuleInfo.getSleep())
				.setCharset(
						StringUtils.isBlank(spiderRuleInfo.getCharset()) ? null
								: spiderRuleInfo.getCharset())
				.setDomain(domain)
				;
		if(StringUtils.isNotBlank(spiderRuleInfo.getCookie())) {
			site.addHeader("Cookie", spiderRuleInfo.getCookie());
		}
		this.spiderRuleInfo = spiderRuleInfo;
	}

	@Override
	public void process(Page page) {
		processHandle(page, spiderRuleInfo);
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	private void processHandle(Page page, SpiderRules spiderRuleInfo) {
		try {
			String url = page.getUrl().toString();
			LOG.warn("-processHandle--链接为{}---",url);
			if(GatherUtil.isFilterUrl(url, spiderRuleInfo)) {
				page.setSkip(true);
				return;
			}
//			详情页
			if(GatherUtil.isDetailUrl(url, spiderRuleInfo)) {
//				以下为详情页的处理
//				是否匹配过滤链接正则
				
//				策略以|或者&间隔，代表——或且
//				规则以@,间隔，与策略一一对应。具体属性在规则中以@|间隔
				String proName = "";
				if (StringUtils.isNotBlank(spiderRuleInfo.getProNameStrategy())&&StringUtils.isNotBlank(spiderRuleInfo.getProNameRules())) {
					proName = GatherUtil.getValue(page, proName, spiderRuleInfo.getProNameStrategy(), spiderRuleInfo.getProNameRules(), "proName");
				}
				if (StringUtils.isBlank(proName)) {
//					名称为空，不抓
//					分析抓取到的商品名称，如果为空，相关数据入记错库
					page.setSkip(true);
					return;
				}
				Map<String, Object> resultMap = CommonCrawlData.crawlData(page,spiderRuleInfo);
				page.putField("resultMap", resultMap);
			}else if(GatherUtil.isLieUrl(url, spiderRuleInfo)) {
				List<String> links = new ArrayList<String>();
				List<String> all = page.getHtml().links().all();
				for (String link : all) {
				    if (GatherUtil.isLieUrl(link, spiderRuleInfo)||GatherUtil.isDetailUrl(link, spiderRuleInfo)) {
				    	links.add(link);
				    }
				}
				// 如果页面包含iframe则也进行抽取
				for (Element iframe : page.getHtml().getDocument().getElementsByTag("iframe")) {
					final String src = iframe.attr("src");
					// iframe抽取规则遵循设定的url正则
					if (GatherUtil.isLieUrl(src, spiderRuleInfo)||GatherUtil.isDetailUrl(src, spiderRuleInfo)) {
				    	links.add(src);
				    }
				}
				if (links != null && links.size() >0) {
					for (String link : links) {
						Request request = new Request(link);
				        request.setMethod("get");
				        Map<String, Object> extras = new HashMap<String, Object>();
				        extras.put("whiteId", spiderRuleInfo.getWhiteId());
				        request.setExtras(extras);
				        // ----向下一个要抓取的队列中传参数---
				        page.addTargetRequest(request);
					}
				}else{
					LOG.warn("-链接{}---------无匹配的页面链接",url);
				}
			}
			
		} catch (Exception e) {
			StackTraceElement traceElement = e.getStackTrace()[0];
			LOG.error("处理网页出错{}，错误类{}，错误行数{}",e.getLocalizedMessage(),traceElement.getFileName(),traceElement.getLineNumber());
		}
		// 处理结束
	}

}
