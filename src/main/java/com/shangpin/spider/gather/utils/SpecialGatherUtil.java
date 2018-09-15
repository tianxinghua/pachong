package com.shangpin.spider.gather.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;

public class SpecialGatherUtil {
	private final static Logger LOG = LoggerFactory.getLogger(SpecialGatherUtil.class);
	public static String cssStrategy(String deStrategy,String detailRule,int i,String crawlValue,Page page) {
		if("C".equals(deStrategy)) {
			try {
				if(detailRule.contains("@|")){
					
				}
				
				
				
				
				
				if(detailRule.contains("@|")){
					String attrRule = detailRule.substring(detailRule.indexOf("@|")+2, detailRule.length());
					crawlValue = page.getHtml().getDocument().select(detailRule).attr(attrRule).toString();
				}else {
					crawlValue = page.getHtml().getDocument().select(detailRule).text();
				}
			} catch (Exception e) {
				LOG.error("---------CSS解析方法异常{}",e.getMessage());
			}
			
		}
		return crawlValue;
	}

}
