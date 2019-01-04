package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("stefanFetchProduct")
public class StefanFetchProduct {
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

    //读取本地文件并转为字符串
    private static String getJson(String fileName) {

		String fullFileName = "E://html.html";

		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
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
	Map<String,String> map = new HashMap<String,String>();
	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
										new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
	private void save(Element category){
		Elements pr = category.select(".Price");
		String pp = null;
		if(pr!=null&&!pr.isEmpty()){
			pp = pr.get(0).text().replace("€", "").replace(",",".");
		}
		Element ca = category.select("a").get(0);
		String ur = ca.attr("href");
		if(ur!=null&&ur.contains("http")&&!map.containsKey(ur)&&pp!=null){
			map.put(ur, "1");
			Product pro = null;
			pro = new Product();
			pro.setUrl(ur);
//			getProductUrl(ur);
//			pro.setBarCode(map.get("spu"));
			pro.setPrice(pp);
			if(StringUtils.isNotBlank(pro.getUrl())){
				messMappingAndSave(pro);	
			}
		}
//		Elements price = category.select("ul").select("li");
//		String pp = price.select("a").select("div").get(0).text();
//		
		
		
////		String detailUrl = category.attr("href");
//		if(StringUtils.isBlank(detailUrl)){
//			return;
//		}
//		Map<String,String> map = getProductUrl(ur);
//		
	
		
	}
	
	private void fetch(String url,int i) throws Exception{
		String htmlContent = getJson(null);
//		if(i==1){
//			htmlContent = HttpUtil45.get(url,new OutTimeConfig(1000*20,1000*20,1000*20),null);	
//		}else{
//			htmlContent = HttpUtil45.get(url+"?p="+i,new OutTimeConfig(1000*20,1000*20,1000*20),null);	
//		}
		Document doc = Jsoup.parse(htmlContent);
		//product-box item col-sm-6 
		Elements categorys = doc.select("ul").select("li");
		
		int j=0;
		for (Element category : categorys) {
			
				save(category);
			
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
//				HttpResponse response = HttpUtils.get(url);
//				Document doc = null;
//				if (response.getStatus()==200) {
//					String htmlContent = response.getResponse();
//					doc = Jsoup.parse(htmlContent);
//				}
//				Document doc = Jsoup.parse(htmlContent);
//				Elements pageEles  = doc.select("div.container.list-box.clearfix");
////				Element pageEless = pageEles.last();
//				pageEles = pageEles.select("ul").select("li").select(".link");
//				Element pageEle = pageEles.get(4);
//				page = pageEle.text();
//				System.out.println("总页数："+page);
//				if(page!=null){
//					pageCount = Integer.parseInt(page);
//				}else{
//					pageCount = pageEnd;
//				}
				pageCount = 20;
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
//			url = url.replace("http","http");
			String htmlContent = HttpUtil45.get(url,new OutTimeConfig(1000*20,1000*60,1000*60*20),null);
			
				Document doc = Jsoup.parse(htmlContent);
				StringBuffer size = new StringBuffer();
				try{
					Elements ss=doc.select(".style-name");
					Elements descEles = ss.select("p");
					for(Element descEle:descEles){
						desc = descEle.select("span").get(0).text();
						String pp = descEle.select("span").get(1).text();
						if(desc!=null&&desc.contains("Lunghezza")){
							size.append(pp).append(",");
						}
						if(desc!=null&&desc.contains("Altezza")){
							size.append(pp).append(",");
						}
						if(desc!=null&&desc.contains("Profondità")){
							size.append(pp).append(",");
						}
						if(desc!=null&&desc.contains("Manico")){
							size.append(pp).append(",");
						}
					}
					
					Element spuSpan = doc.select("span[itemprop=sku]").get(0);
					spuModel = spuSpan.text();
				}catch(Exception e){
				}
				map.put("picUrl", size.toString());
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
			String productName = dto.getPrice();
			buffer.append(productName).append(splitSign);
			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}