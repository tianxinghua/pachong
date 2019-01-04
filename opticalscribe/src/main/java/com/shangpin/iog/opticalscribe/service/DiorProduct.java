package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
//@Component("diorProduct")
public class DiorProduct {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri;
	private static String host;
	private static String sex;
	private static String path;
	private static int pageStart;
	private static int pageEnd;
	private static OutputStreamWriter out = null;
	static String splitSign = ",";
	private static String fileName;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		uri = bdl.getString("uri");
		host = bdl.getString("host");
		sex = bdl.getString("gender");
		path = bdl.getString("path");
		if (!bdl.getString("pageStart").isEmpty()) {
			pageStart = Integer.parseInt(bdl.getString("pageStart"));
		}
		if (!bdl.getString("pageEnd").isEmpty()) {
			pageEnd = Integer.parseInt(bdl.getString("pageEnd"));
		}
	}

	class UpdateThread extends Thread {
		private String html;

		public UpdateThread(String html) {
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

	class fetchThread extends Thread {
		private int i;
		private String url;

		public fetchThread(int i, String url) {
			this.i = i;
			this.url = url;
		}

		@Override
		public void run() {
			try {
				fetch(i, url);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}

	// ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
	ExecutorService exe = new ThreadPoolExecutor(10, 10, 500,
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),
			new ThreadPoolExecutor.CallerRunsPolicy());

	private void save(String proUrl) throws Exception {
		List<Map<String, String>> returnList = getProductUrl(proUrl);

		for (Map<String, String> map : returnList) {
			String brand = null;
			String productName = null;
			String price = null;
			Product pro = null;
			pro = new Product();
			pro.setBrand(brand);
			pro.setMaterl(map.get("materl"));
			pro.setProductCode(map.get("productCode"));
			pro.setDescript(map.get("desc"));
			pro.setProductname(productName);
			pro.setUrl(map.get("picUrl"));
			pro.setBarCode(map.get("barCode"));
			pro.setMade(map.get("made"));
			pro.setSize(map.get("size"));
			pro.setColorCode(map.get("colorCode"));
			pro.setPrice(price);
			pro.setItemprice(map.get("price1"));
			pro.setItemdiscountA(map.get("price2"));
			pro.setItemsaleprice(map.get("price3"));
			messMappingAndSave(pro);
		}

	}

	private void fetch(int i, String url) throws Exception {
		save(url);
		//exe.execute(new UpdateThread(url));
	}

	public void getUrlList() throws Exception {
		System.out.println("要下载的url：" + uri);
		System.out.println("文件保存目录：" + path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),
					"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("BrandName 品牌" + splitSign
				+ "ProductModel 货号" + splitSign + "SopProductName 商品名称"
				+ splitSign + "farfetchId" + splitSign + "color 颜色" + splitSign
				+ "material 材质" + splitSign + "size尺码" + splitSign
				+ "ProductOrigin 产地" + splitSign + "Gender 性别" + splitSign
				+ "新市场价" + splitSign + "原价" + splitSign + "OFF价" + splitSign
				+ "现售价" + splitSign + "新季" + splitSign + "PcDesc 描述"
				+ splitSign + "图片" + splitSign).append("\r\n");
		out.write(buffer.toString());
		String page = null;
		int pageCount = 0;
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus() == 200) {
				String htmlContent = response.getResponse();

				Document doc = Jsoup.parse(htmlContent);
				Elements pp2 =doc.select("[class=categories js-categories product-container]");
				for(int i=3;i<1000;i++){
					Elements pp = doc.select("#zone_"+i);
					Elements pages = pp.select("[class=push-link]");
					fetch(pages);

					Elements totalPage = doc.select("#addlist");
					if (totalPage != null&&!totalPage.isEmpty()) {//
						Elements totalPage1 = totalPage.select("a");
						if (totalPage1 != null) {
							String po = totalPage1.get(0).attr("data-pn");
							if (po != null) {
								htmlContent = HttpUtil45.get(
										"https://www.gucci.cn/zh/itemList?pn=" + po
												+ "&ni=12&_=1529405469032",	
										new OutTimeConfig(), null);
								Document doc1 = Jsoup.parse(htmlContent);
								Elements pp1 = doc1.select("li");
								fetch(pp1);
							}
							System.out.println(po);
						}
					}
				}
				

			}
			if (pageStart == 0) {
				pageStart = 1;
			}
			for (int i = pageStart; i <= pageCount; i++) {
				// exe1.execute(new fetchThread(i));

			}

			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void fetch(Elements pages) throws Exception {

		if (pages != null && pages.size() > 0) {
			for (int i = 0; i < pages.size(); i++) {

				Element pageEle = pages.get(i);
				String pageTotal = pageEle.select("a").first().attr("href");
				fetch(i, pageTotal);
			}
		}
	}

	public static void main(String[] args) {
		try {
			getProductUrl("/couture/zh_cn/%E5%A5%B3%E5%A3%AB%E6%97%B6%E8%A3%85/%E5%8C%85%E5%85%B7/lady-dior%E8%8E%B2%E8%8A%B1%E8%89%B2-9-50247");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getProductInfo(Document doc) {
		String materal = null;
		String desc = null;
		String productName = null;
		String productCode = null;
		String colorCode = null;
		String made = null;
		String size = null;
		Map map = new HashMap<String, String>();
		StringBuffer pic = new StringBuffer();
		Elements descEles = doc.select("#c-description").select("section");
		if (descEles != null) {
			// for(int i=0;i<descEles.size();i++){
			Element descEle0 = descEles.get(0);
			Elements descStrEle = descEle0
					.select("div .product-description-content");
			if (descStrEle != null) {
				productName = descStrEle.text();
			}
			Element descEle1 = descEles.get(1);
			Elements descContentEle = descEle1
					.select("div .product-description-content");

			if (descContentEle != null) {
				desc = descContentEle.html();
				String[] descArr = desc.split("<br>");
				desc = descContentEle.text();
			}

			Element descEle2 = descEles.get(2);

			productCode = descEle2.select("h2").select("div").text();
			System.out.println("s");
			// }
		}
		Elements picEles = doc.select("div .c-product-details");
		Elements imgEles = picEles.select("img");
		for (Element img : imgEles) {
			pic.append(img.attr("src"));
			System.out.println(pic);
		}
		map.put("desc", desc);
		map.put("materl", materal);
		map.put("picUrl", pic.toString());
		map.put("made", made);
		map.put("size", size);
		map.put("colorCode", colorCode);
		map.put("productName", productName);
		map.put("productCode", productCode);
		return map;
	}

	private static List<Map<String, String>> getProductUrl(String url) throws Exception {
		List<Map<String, String>> returnList = new ArrayList<>();
		String materal = null;
		String desc = null;
		String barCode = null;
		String productName = null;
		String productCode = null;
		String colorCode = null;
		String made = null;
		String size = null;
		Map<String,String> map = null;
		StringBuffer pic = new StringBuffer();
		url = URLEncoder.encode(url.toString());
		url = url.replace("%3A", ":").replace("%2F","/");
			HttpResponse response = HttpUtils.get(host + url);
			if (response.getStatus() == 200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements productdetail = doc
						.select("div .c-product-information");
				Elements productNameEle = productdetail.select("h1")
						.select("a");
				productName = productNameEle.text();

				Elements price1 = productdetail
						.select("div .c-product-variations-swatches")
						.select("ul").select("li");

				if (price1 != null && price1.size() > 0) {
					for (int i = 0; i < price1.size(); i++) {
						Element p = price1.get(i);
						Elements p2 = p.select("[class=selected]");
						if (p2 != null&&p2.isEmpty()) {
							url=p.select("a").attr("href");
							url = URLEncoder.encode(url.toString());
							url = url.replace("%3A", ":").replace("%2F","/");
							HttpResponse response1 = HttpUtils.get(url);
							if (response1.getStatus() == 200) {
								htmlContent = response1.getResponse();
								Document doc1 = Jsoup.parse(htmlContent);
								 map = getProductInfo(doc1);
							} 
						}else{
							map = getProductInfo(doc);
						}
					}
				}else{
					map = getProductInfo(doc);
				}
				
				returnList.add(map);
			}
				
		return returnList;
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

	private static void exportExvel(Product dto) {
		// 此处设置为true即可追加
		// 继续追加

		// InputStream in = new BufferedInputStream(new
		// ByteArrayInputStream(buffer.toString().getBytes("gb2312")));
		// File file = new File(path);
		// FileOutputStream out = new FileOutputStream(file);
		// byte[] data = new byte[1024];
		// int len = 0;
		// while (-1 != (len=in.read(data, 0, data.length))) {
		// out.write(data, 0, len);
		// }
		// out.flush();
		// out.close();
		//
		//

		StringBuffer buffer = new StringBuffer();
		try {
			// supplierId 供货商

			String brandName = dto.getBrand();
			buffer.append(brandName).append(splitSign);
			// 货号
			buffer.append(
					null == dto.getProductCode() ? "" : dto.getProductCode()
							.replaceAll(",", " ")).append(splitSign);
			// 供应商SKUID
			// 产品名称
			String productName = dto.getProductname();

			buffer.append(
					null == productName ? "" : productName.replace(",", " "))
					.append(splitSign);

			buffer.append(dto.getBarCode()).append(splitSign);
			buffer.append(dto.getColorCode()).append(splitSign);

			// 获取材质
			String material = dto.getMaterl();
			if (StringUtils.isBlank(material)) {
				material = "";
			} else {

				material = material.replaceAll(splitSign, " ")
						.replaceAll("\\r", "").replaceAll("\\n", "");
			}

			buffer.append(material).append(splitSign);
			buffer.append(dto.getSize()).append(splitSign);
			// 获取产地
			String productOrigin = dto.getMade();

			buffer.append(productOrigin).append(splitSign);
			// 欧洲习惯 第一个先看 男女
			buffer.append(sex).append(splitSign);
			// 新的价格
			String newMarketPrice = dto.getPrice();
			buffer.append(newMarketPrice).append(splitSign);
			buffer.append(dto.getItemprice()).append(splitSign);
			buffer.append(dto.getItemdiscountA()).append(splitSign);
			buffer.append(dto.getItemsaleprice()).append(splitSign);
			buffer.append(dto.getMemo()).append(splitSign);
			buffer.append(dto.getDescript()).append(splitSign);
			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}