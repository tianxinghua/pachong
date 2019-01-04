package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("theCluterFetchProduct")
public class TheCluterFetchProduct {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri;
	private static String sex;
	private static String path;
	private static int pageStart;
	private static int pageEnd;
	private static OutputStreamWriter  out= null;
	static String splitSign = ",";
	private static String fileName;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		uri = bdl.getString("uri");
		sex = bdl.getString("gender");
		path = bdl.getString("path");
		if(!bdl.getString("pageStart").isEmpty()){
			pageStart = Integer.parseInt(bdl.getString("pageStart"));
		}
		if(!bdl.getString("pageEnd").isEmpty()){
			pageEnd = Integer.parseInt(bdl.getString("pageEnd"));
		}
	}
	
//	class SaveThread extends Thread{
//		private int pageNo;
//		public SaveThread(int pageNo) {
//			this.pageNo = pageNo;
//		}
//		@Override
//		public void run() {
//			try {
//				fetch(pageNo);
//			} catch (Exception e) {
//				logger.warn(Thread.currentThread().getName() + "处理出错", e);
//			}
//		}
//	}
	class UpdateThread extends Thread{
		private Element html;
		public UpdateThread(Element html) {
			this.html = html;
		}
		@Override
		public void run() {
			try {
				save(html);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
//	class fetchThread extends Thread{
//		private int i;
//		public fetchThread(int i) {
//			this.i = i;
//		}
//		@Override
//		public void run() {
//			try {
//				fetch(i);
//			} catch (Exception e) {
//				logger.warn(Thread.currentThread().getName() + "处理出错", e);
//			}
//		}
//	}
//	ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
										new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
	private void save(Element category){
		String detailUrl = category.attr("href");
		if(StringUtils.isBlank(detailUrl)){
			return;
		}
		Map<String,String> map = getProductUrl(detailUrl);
		
		
		Product pro = null;
		pro = new Product();
		pro.setUrl(map.get("picUrl"));
		pro.setBarCode(map.get("spu"));
		if(StringUtils.isNotBlank(pro.getUrl())){
			messMappingAndSave(pro);	
		}
		
	}
	
	private void fetch(String url,int i) throws Exception{
		String htmlContent = null;
		if(i==1){
			htmlContent = HttpUtil45.get(url,new OutTimeConfig(1000*20,1000*20,1000*20),null);	
		}else{
			htmlContent = HttpUtil45.get(url+"?currPage="+i,new OutTimeConfig(1000*20,1000*20,1000*20),null);	
		}
		Document doc = Jsoup.parse(htmlContent);
		//product-box item col-sm-6 
		Elements categorys = doc.select(".product");
		
		int j=0;
		for (Element category : categorys) {
			j++;
			Element content = category.select("a").select(".photo").get(0);
			System.out.println("第"+i+"页："+",第"+j+"条数据");
			save(content);
//			exe.execute(new UpdateThread(content));
		}
	}
	public  void getUrlList() throws Exception {
		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("spuId" + splitSign + "图片" + splitSign).append("\r\n");
		out.write(buffer.toString());
		String page = null;
		int pageCount = 0;
		try {
			String [] arrUri = uri.split(",");
			for(String url:arrUri){
				String htmlContent = HttpUtil45.get(url,new OutTimeConfig(1000*20,1000*20,1000*20),null);
				
				Document doc = Jsoup.parse(htmlContent);
				Elements pageEles  = doc.select("div.container.list-box.clearfix");
//				Element pageEless = pageEles.last();
				pageEles = pageEles.select("ul").select("li").select(".link");
				Element pageEle = pageEles.get(4);
				page = pageEle.text();
				System.out.println("总页数："+page);
				if(page!=null){
					pageCount = Integer.parseInt(page);
				}else{
					pageCount = pageEnd;
				}
				if(pageStart==0){
					pageStart = 1;
				}
				for(int i=pageStart;i<=pageCount;i++){
	//				exe1.execute(new fetchThread(i));
					fetch(url,i);
				}
			
//				exe.shutdown();
//				while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {
//	
//				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();
		String  spuModel = null;
		String desc = null;
		StringBuffer pic = new StringBuffer();
		try {
			String htmlContent = HttpUtil45.get("https://www.theclutcher.com"+url,new OutTimeConfig(1000*20,1000*20,1000*20),null);
			
				Document doc = Jsoup.parse(htmlContent);
		
				try{
					Elements ss=doc.select("div.acc-content.curr");
					Element descEle = ss.select("p").get(0);
					desc = descEle.text();
					if(desc!=null&&desc.contains("Dimensions:")){
						desc = desc.substring(desc.indexOf("Dimensions:")).replace("Dimensions:","").trim();
					}
					Element spuSpan = ss.select("p[itemprop=productID]").get(0);
					spuModel = spuSpan.text();
					if(spuModel!=null){
						spuModel = spuModel.replace("PRODUCT CODE:","").trim();
					}
				}catch(Exception e){
				}
				Elements picEles = doc.select("#product-gallery").select(".product-image");
				Map<String,String> temMap = new HashMap<String,String>();
				for (Element picEle : picEles) {
					String picUrl = picEle.select("a").attr("href");
					if(!temMap.containsKey(picUrl)){
						pic.append(picUrl).append(",");	
						temMap.put(picUrl, spuModel);
					}
				}
				map.put("picUrl", desc);
				map.put("spu", spuModel);
		} catch (Exception e) {
		}
		return map;
	}

	public void messMappingAndSave(Product item) {
		exportExvel(item);
	}


	private static void exportExvel(Product dto){

		StringBuffer buffer  = new StringBuffer();
		try {
			String productName = dto.getBarCode();
			buffer.append(productName).append(splitSign);
			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}