package com.shangpin.spider.test;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shangpin.spider.gather.httpClientDownloader.SpHttpClientDownloader;

import us.codecraft.webmagic.selector.Html;

public class JsoupTest {
	/**
	 * 源码中不存在的内容，以下方法无法获得，仅支持静态内容
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*String ss = "10|25|14|36|29|48|46";
		ss = ss.replace("|", ",");
		System.out.println(ss);
		String[] sss = ss.split(",");
		System.out.println("数组的个数："+sss.length);
		for (String str : sss) {
			System.out.println(str);
		}*/
		
		
		String url = "https://news.sina.com.cn/world/";
		try {
			/*SpHttpClientDownloader downloader = new SpHttpClientDownloader();
			Html html = downloader.download(url);
			List<String> all = html.css(".blk122 a").links().all();
			for (String str : all) {
				System.out.println("httpClient:"+str);
			}
//			url = "https://news.sina.com.cn/w/2018-10-29/doc-ihnaivxq0561993.shtml";
			url = "https://fr.sandro-paris.com/fr/femme/robes/robe-sans-manches-col-claudine/R20357H.html?dwvar_R20357H_color=20";
//			url = "https://fr.sandro-paris.com/fr/femme/robes/#sz=82";
			Document document = Jsoup.connect(url).timeout(20000).get();
			Elements eles = document.select("#product-content  span[itemprop=color] > a");
			for (Element ele : eles) {
//				System.out.println("jsoup:"+ele.attr("abs:src").toString());
				System.out.println("jsoup:"+ele.text().toString());
			}*/
			url = "https://fr.sandro-paris.com/fr/femme/robes/robe-droite-en-maille-a-collerette/R2897H.html?dwvar_R2897H_color=100";
			SpHttpClientDownloader downloader = new SpHttpClientDownloader(null,null);
			Html html = downloader.download(url);
			
			
			List<String> list = html.css("#product-content  span > a").links().all();
			System.out.println("---个数："+list.size());
			for (String str : list) {
				System.err.println(str);
			}
			
			/*Elements elements = html.getDocument().select(".search-result-content a.thumb-link");
			System.out.println("---ele的个数："+elements.size());
			for (Element ele : elements) {
				String str = ele.attr("href");
				System.err.println(str);
			}
			
			
			System.err.println(html.toString());
			String text = html.getDocument().select("#product-content > div.product-promotion-percentage > div > span").attr("content").toString();
			System.out.println("价格："+text);*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
