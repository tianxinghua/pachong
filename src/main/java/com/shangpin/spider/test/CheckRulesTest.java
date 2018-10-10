package com.shangpin.spider.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.shangpin.spider.gather.utils.UserAgentUtil;


/**
 * 
 * @author njt
 * @date 2018年10月10日 下午7:19:34
 * @desc 规则测试
 * CheckRulesTest
 */
public class CheckRulesTest {
	
	public static void main(String[] args) {
		String url = "https://www.thekooples.com/fr/baskets-blanches-large-semelle-1522000.html";
		String ruleStr = "form .price-box .price";
		String field = "foreignPrice";
		check(url, ruleStr, field);
	}
	
//	静态的
	public static void check(String url,String ruleStr,String field) {
		try {
			Document doc = Jsoup.connect(url).userAgent(UserAgentUtil.getUserAgent()).timeout(200000).get();
			String text = doc.select(ruleStr).text();
			System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	动态的
}
