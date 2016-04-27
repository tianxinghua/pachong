package com.shangpin.iog.smets.crawer;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.crawer.customer.PicCustomer;
import com.shangpin.iog.smets.crawer.customer.SkuCustomer;
import com.shangpin.iog.smets.crawer.customer.SpuCustomer;
import com.shangpin.iog.smets.util.HttpResponse;
import com.shangpin.iog.smets.util.HttpUtils;
import com.shangpin.iog.smets.util.SaveTo;

@Component("queueCraw")
public class QueueCraw {
	private static Logger log = Logger.getLogger("info");
	private static String supplierId = "201604271112";
	private static ExecutorService executor = new ThreadPoolExecutor(4, 60, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(60),new ThreadPoolExecutor.CallerRunsPolicy());
	@Autowired
	ProductFetchService productFetchService;
	
	private LinkedList<String> initCrawlerWithSeeds(String[] feeds){
		LinkedList<String> relist = new LinkedList<String>();
		for (String feed : feeds) {
			//访问feed 获取品类url
			LinkedList<String> list = getSubUrl(feed);
			relist.addAll(list);
		}
		return relist;
	}
	private LinkedList<String> getSubUrl(String uri){
		LinkedList<String> list = new LinkedList<String>();
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements ele1 = doc.select("#lp-filters");
				Elements categorys = ele1.select("li[class=facet-item tree-item]");
				for (Element category : categorys) {
					 uri = category.select("a").get(0).attr("href");
					 list.add(uri+"|"+category.select("a").get(0).attr("title"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void crawling(String[] feeds){
		//消费线程
//		executor.execute(new SkuCustomer(productFetchService));
//		executor.execute(new SpuCustomer(productFetchService));
		executor.execute(new PicCustomer(productFetchService));
		
		
		
		//生产线程
		LinkedList<String> linkedList = initCrawlerWithSeeds(feeds);
		for (String string : linkedList) {
			String all1 = string;
			String visitUrl1 = all1.split("\\|")[0];
			String category = all1.split("\\|")[1];
			int num = 0;
			//1、解析url 获取性别 品类
			//2、访问url，分页获取所有产品，解析
			try {
				HttpResponse response = HttpUtils.get("www.farfetch.com"+visitUrl1);
				System.out.println("www.farfetch.com"+visitUrl1);
				if (response.getStatus()==200) {
					String htmlContentPage = response.getResponse();
					Document docpage = Jsoup.parse(htmlContentPage);
					
					Elements pages = docpage.select("span[data-tstid=paginationTotal]");
					Integer maxPage = Integer.valueOf(pages.get(0).ownText());
					for (int i = 1; i <=maxPage; i++) {
						executor.execute(new SaveTo(visitUrl1, i, ++num, category, supplierId, productFetchService));
					} 
				}
			}catch (Exception e) {
				log.info("++++++++++++++++++++++++++++++++"+num);
				e.printStackTrace();
			}
		//=============================
		}
	}
	
	
	
}
