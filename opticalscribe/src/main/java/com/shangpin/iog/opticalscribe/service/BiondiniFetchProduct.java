package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component("biondiniFetchProduct")
public class BiondiniFetchProduct {
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
	
	class SaveThread extends Thread{
		private int pageNo;
		public SaveThread(int pageNo) {
			this.pageNo = pageNo;
		}
		@Override
		public void run() {
			try {
//				fetch(pageNo);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}

	class UpdateThread extends Thread{
		private Element html;
		private String productUrl = "";
		public UpdateThread(String  url) {
			this.productUrl = url;
		}
		@Override
		public void run() {
			try {
				save(productUrl);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}

	class fetchThread extends Thread{
		private int i;
		public fetchThread(int i) {
			this.i = i;
		}
		@Override
		public void run() {
			try {
//				fetch(i);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
//	ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
	ExecutorService exe = new ThreadPoolExecutor(20,120, 500, TimeUnit.MILLISECONDS,
										new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());

	private void save(String  url){

		Map<String, String> productMap = getProductUrl(url);

		Product pro = new Product();
		pro.setSpuNo(productMap.get("spuNo"));
		pro.setUrl(productMap.get("picUrl"));
		messMappingAndSave(pro);
	}
	

	public  void getUrlList() throws Exception {
		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("spuNo" + splitSign +  "pic" + splitSign ).append("\r\n");
		out.write(buffer.toString());
		String page = null;
		int pageCount = 0;
		try {
			List<String> urlList = new ArrayList<>();
			urlList.add("https://biondiniparis.com/en/6-women-shoes");
			urlList.add("https://biondiniparis.com/en/7-women-accessories");
//			urlList.add("https://biondiniparis.com/en/nouveaux-produits");

			String nextPage = "https://biondiniparis.com/modules/totfeatureproduct/ajax_totfeatureproduct.php?category=6&page=3&order_by=position&order_way=asc";
			Set<String> productUrlList=new HashSet<>();
			int   total = 0;
			String productTotal = "";
			Map<Integer,String> category = new HashMap<>();
			category.put(0,"6");
			category.put(1,"7");
            int j=-1;
			for(String url:urlList){

				HttpResponse response = HttpUtils.get(url);
				j++;
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);

					Elements productElements = doc.select(".product_img_link");


					for(Element element:productElements){
						productUrlList.add(element.attr("href"));

					}
					Elements pageElement  = doc.select(".js-load-more");
					if(null!=pageElement&&pageElement.size()>0){

						//获取下页内容
						productTotal = pageElement.attr("data-total-pages");
						if(StringUtils.isNotBlank(productTotal)){
							try {
								total = Integer.valueOf(productTotal);
								for(int n=1;n<=total;n++) {

									System.out.println("category = " + category.get(j) + " page = " + n );
									nextPage = "https://biondiniparis.com/modules/totfeatureproduct/ajax_totfeatureproduct.php?category=" + category.get(j) + "&page="+ n +"&order_by=position&order_way=asc";
									response = HttpUtils.get(nextPage);
									if (response.getStatus()==200) {
										htmlContent = response.getResponse();
										doc = Jsoup.parse(htmlContent);
										productElements = doc.select(".product_img_link");
										for (Element element : productElements) {
											logger.info("product link = "+ element.attr("href"));
											System.out.println("product link = "+ element.attr("href"));
											productUrlList.add(element.attr("href"));

										}
									}
								}

							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}

			System.out.println("product set size ="+productUrlList.size());
			for(String productUrl :productUrlList){
				logger.info("product url:"+ productUrl);
				exe.execute(new UpdateThread(productUrl));
			}

			
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public static void main(String[] args) {
		BiondiniFetchProduct fetch = new BiondiniFetchProduct();
		try {
			fetch.getUrlList();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		getProductUrl("https://biondiniparis.com/fr/femmes/5188-bottines-buckle-balenciaga-en-cuir-blanc.html");
	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();


		String spuNo = "";


		try {
			StringBuffer picBuffer = new StringBuffer();
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				
				Elements imageElements = doc.select(".image-wrapper");
                String picUrl = "";
				for(Element element:imageElements){
					picUrl = element.select("img").attr("src");
					if(StringUtils.isBlank(picUrl)){

						picUrl = element.select("img").attr("data-src");
					}
					if(StringUtils.isNotBlank(picUrl)){
						picBuffer.append(picUrl).append("|");
					}

				}
				map.put("picUrl",picBuffer.toString());

				Elements skuEle = doc.select("span[itemprop=sku]");
				if(skuEle!=null){
					spuNo = skuEle.get(0).attr("content");
					System.out.println(spuNo);
					map.put("spuNo", spuNo);
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * fetch product and save into db
	 */
	/**
	 * message mapping and save into DB
	 */

	public void messMappingAndSave(Product item) {

		exportExvel(item);
	}


	private static void exportExvel(Product dto){
		//此处设置为true即可追加
		//继续追加

//		   InputStream in = new BufferedInputStream(new ByteArrayInputStream(buffer.toString().getBytes("gb2312")));
//		   File file = new File(path);
//		   FileOutputStream out = new FileOutputStream(file);
//            byte[] data = new byte[1024];
//            int len = 0;
//            while (-1 != (len=in.read(data, 0, data.length))) {
//                out.write(data, 0, len);
//            }
//            out.flush();
//            out.close();
//
//

		StringBuffer buffer  = new StringBuffer();
		try {
			//supplierId 供货商


			buffer.append(dto.getSpuNo()).append(splitSign);
			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}