package com.shangpin.spider.gather.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.config.SpringContextHolder;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.crawlData.CommonCrawlData;
import com.shangpin.spider.gather.utils.GatherUtil;
import com.shangpin.spider.service.gather.CrawlService;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.thread.CountableThreadPool;

/**
 * 处理数据
 * 
 * @author njt
 * 
 */
public class MyPageProcessor implements PageProcessor {
	private static Logger LOG = LoggerFactory.getLogger(MyPageProcessor.class);
	private Site site;
	private SpiderRules spiderRuleInfo;
	private Map<String, Map<String, String>> clickFieldMap;
	protected CountableThreadPool crawlThreadPool;
	private CrawlService crawlService;

	public MyPageProcessor(SpiderRules spiderRuleInfo, Map<String, Map<String, String>> clickFieldMap, CountableThreadPool crawlThreadPool) {
		// 域名
		String domain = GatherUtil.getFefferrerHost(spiderRuleInfo.getSourceUrl());
		spiderRuleInfo.setChannelRules(domain);
		this.site = Site.me().setUserAgent(spiderRuleInfo.getUserAgent()).setTimeOut(spiderRuleInfo.getTimeout())
				.setRetryTimes(spiderRuleInfo.getRetryNum()).setSleepTime(spiderRuleInfo.getSleep())
				.setCharset(StringUtils.isBlank(spiderRuleInfo.getCharset()) ? null : spiderRuleInfo.getCharset())
				.setDomain(domain);
		if (StringUtils.isNotBlank(spiderRuleInfo.getCookie())) {
			site.addHeader("Cookie", spiderRuleInfo.getCookie());
		}
		this.spiderRuleInfo = spiderRuleInfo;
		this.clickFieldMap = clickFieldMap;
		this.crawlThreadPool = crawlThreadPool;
		this.crawlService = SpringContextHolder.getBean(CrawlService.class);
	}

	@Override
	public void process(Page page) {
		processHandle(page, spiderRuleInfo, clickFieldMap);
	}

	@Override
	public Site getSite() {
		return site;
	}

	private void processHandle(Page page, SpiderRules spiderRuleInfo, Map<String, Map<String, String>> clickFieldMap) {
		String url = page.getUrl().toString();
		try {
			LOG.info("---processHandle--链接为{}---", url);
			if (GatherUtil.isFilterUrl(url, spiderRuleInfo)) {
				page.setSkip(true);
				return;
			}
			if (page.getStatusCode() != 200) {
				LOG.warn("---链接{}-的响应码{}，不成功，跳过！--", url, page.getStatusCode());
				page.setSkip(true);
				return;
			}
//			详情页
			if (GatherUtil.isDetailUrl(url, spiderRuleInfo)) {
//				以下为详情页的处理
//				是否匹配过滤链接正则

//				策略以|或者&间隔，代表——或且
//				规则以@,间隔，与策略一一对应。具体属性在规则中以@|间隔
				String proName = "";
				if (StringUtils.isNotBlank(spiderRuleInfo.getProNameStrategy())
						&& StringUtils.isNotBlank(spiderRuleInfo.getProNameRules())) {
					proName = GatherUtil.getValue(page, proName, spiderRuleInfo.getProNameStrategy(),
							spiderRuleInfo.getProNameRules(), "proName");
				}
				if (StringUtils.isBlank(proName)) {
//					名称为空，不抓
//					分析抓取到的商品名称，如果为空，相关数据入记错库
					page.setSkip(true);
					return;
				}
//				添加线程池，异步
				crawlThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        	List<CrawlResult> resultList = CommonCrawlData.crawlData(page, spiderRuleInfo, clickFieldMap);
//                        	因为是对动态网站采取了，动静结合的策略，因此，putField方法在存库的过程中受异步的影响，
//                        	页面加载完后，抓取数据为异步操作，导致，ResultItems中还没有存入抓取的值。
//                        	因此，此处的数据存储，自定义！
//                        	page.putField(Constants.RESULTFLAG, resultList);
                        	if(resultList!=null&&resultList.size()>0) {
            					for (CrawlResult crawlResult : resultList) {
            						System.err.println("----抓取结果为："+crawlResult.toString());
            						Long i = crawlService.insert(crawlResult);
            						if(i>0) {
            							LOG.info("---商品spu为{}-存库成功！",crawlResult.getSpu());
            						}else {
            							LOG.error("---商品spu为{}-存库失败！",crawlResult.getSpu());
            						}
            					}
            				}else {
            					LOG.error("---{}获取到的商品列表为空！",url);
            				}
                        } catch (Exception e) {
                            LOG.error("---链接{},抓取数据出错！，异常：{}",url,e);
                        }
                    }
                });
				
			} else if (GatherUtil.isLieUrl(url, spiderRuleInfo)) {
				Set<String> links = new HashSet<String>();
				List<String> all = null;
				
				if(StringUtils.isNotBlank(spiderRuleInfo.getDetailUrlStrategy())&&StringUtils.isNotBlank(spiderRuleInfo.getDetailUrlRules())){
					if(spiderRuleInfo.getDetailUrlStrategy().equals(StrategyConstants.C)) {
						all = page.getHtml().css(spiderRuleInfo.getDetailUrlRules()).links().all();
					}else if(spiderRuleInfo.getDetailUrlStrategy().equals(StrategyConstants.X)) {
						all = page.getHtml().xpath(spiderRuleInfo.getDetailUrlRules()).links().all();
					}
					for (String link : all) {
						links.add(link);
					}
				}else {
					all = page.getHtml().links().all();
					// 如果页面包含iframe则也进行抽取
					for (Element iframe : page.getHtml().getDocument().getElementsByTag("iframe")) {
						final String src = iframe.attr("src");
						all.add(src);
					}
					for (String link : all) {
						if (GatherUtil.isLieUrl(link, spiderRuleInfo) || GatherUtil.isDetailUrl(link, spiderRuleInfo)) {
							links.add(link);
						}
					}
				}
				
				
				if (links != null && links.size() > 0) { 
					for (String link : links) {
						Request request = new Request(link);
						request.setMethod("get");
						Map<String, Object> extras = new HashMap<String, Object>();
						extras.put("whiteId", spiderRuleInfo.getWhiteId());
						request.setExtras(extras);
						// ----向下一个要抓取的队列中传参数---
						LOG.info("---进入队列的链接-{}", link);
						page.addTargetRequest(request);
					}
				} else {
					LOG.warn("---链接{}--无匹配的页面链接", url);
				}
			} else {
				LOG.warn("---链接{}--既不是详情页也不是列表页！", url);
			}

		} catch (Exception e) {
			StackTraceElement traceElement = e.getStackTrace()[0];
			LOG.error("---网页{}出错{}，错误类{}，错误行数{}", url, e.getLocalizedMessage(), traceElement.getFileName(),
					traceElement.getLineNumber());
			return;
		}
		// 处理结束
	}
	

}
