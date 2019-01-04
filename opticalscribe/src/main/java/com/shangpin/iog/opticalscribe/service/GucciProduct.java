package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import org.omg.IOP.Encoding;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.opticalscribe.dto.Channel;
import com.shangpin.iog.opticalscribe.dto.Item;
import com.shangpin.iog.opticalscribe.dto.Rss;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("gucciProduct")
public class GucciProduct {
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
	
	
	class UpdateThread extends Thread{
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
	class fetchThread extends Thread{
		private int i;
		private String url;
		public fetchThread(int i,String url) {
			this.i = i;
			this.url = url;
		}
		@Override
		public void run() {
			try {
				fetch(i,url);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
//	ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
										new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
	private void save(String proUrl){
		Map<String,String> map = getProductUrl(proUrl);
		
		String brand = null;
		String productName = null;
		String price = null;
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(price);
		price = matcher.replaceAll("");
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
	
	private void fetch(int i,String url) throws Exception{
		exe.execute(new UpdateThread(url));
	}
	public  void getUrlList() throws Exception {
		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SopProductName 商品名称" + splitSign + "farfetchId" + splitSign
				+  "color 颜色" + splitSign
				+  "material 材质" + splitSign
				+  "size尺码" + splitSign
				+ "ProductOrigin 产地" + splitSign
				+ "Gender 性别" + splitSign+ "新市场价"+ splitSign+ "原价"+splitSign+ "OFF价"+splitSign+ "现售价"+splitSign 	+ "新季" + splitSign
				+ "PcDesc 描述" + splitSign
				+ "图片" + splitSign).append("\r\n");
		out.write(buffer.toString());
		String page = null;
		int pageCount = 0;
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				
				Document doc = Jsoup.parse(htmlContent);

				
				Elements pp = doc.select("#pdlist");
				Elements pages = pp.select("li");
				fetch(pages);
				
				
				Elements totalPage = doc.select("#addlist"); 
				if(totalPage!=null){//
					Elements totalPage1 = totalPage.select("a");
					if(totalPage1!=null){
						String po = totalPage1.get(0).attr("data-pn");
						if(po!=null){
							htmlContent = HttpUtil45.get("https://www.gucci.cn/zh/itemList?pn="+po+"&ni=12&_=1529405469032",new OutTimeConfig(),null);
							Document doc1 = Jsoup.parse(htmlContent);
							Elements pp1 = doc1.select("li");
							fetch(pp1);
						}
						System.out.println(po);
					}
				}
				
			}
			if(pageStart==0){
				pageStart = 1;
			}
			for(int i=pageStart;i<=pageCount;i++){
//				exe1.execute(new fetchThread(i));
			
			}
			
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	private void fetch(Elements pages) throws Exception{

		
		if(pages!=null&&pages.size()>0){
			for(int i=0;i<pages.size();i++){
				
				Element pageEle  = pages.get(i);
				String pageTotal = pageEle.select("a").first().attr("href");
				fetch(i,pageTotal);
			}
			
		}
	}
//	public static void main(String[] args) {
//		getProductUrl("https://www.farfetch.com/cn/shopping/women/gucci-icon-signature-chain-wallet-item-11924450.aspx?storeid=9270&from=listing&tglmdl=1&rnkdmnly=1&ffref=lp_pic_5_1_");
//	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();
		String  materal = null;
		String  desc= null;
		String  barCode= null;
		String productCode = null;
		String colorCode = null;
		String made = null;
		String size = null;
		StringBuffer pic = new StringBuffer();
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				
				Elements price1 = doc.select("span[data-tstid=itemprice]");
				if(price1!=null&&price1.size()>0){
					productCode = price1.get(0).text().replace(",", "");
					map.put("price1", productCode.replace(",", "").replace("¥",""));
				}
				Elements price2 = doc.select("span[data-tstid=itemdiscountA]");
				if(price2!=null&&price2.size()>0){
					productCode = price2.text();
					map.put("price2", productCode.replace(",", "").replace("¥",""));
				}
				Elements price3 = doc.select("span[data-tstid=itemsalesprice]");
				if(price3!=null&&price3.size()>0){
					productCode = price3.text();
					map.put("price3", productCode.replace(",", "").replace("¥",""));
				}
				productCode = null;
				Elements categorys = doc.select("div .accordion").select(".accordion-xl").select(".product-detail");
				Elements categoryPics = doc.select(".sliderProduct").select(".js-sliderProductFull").select("li");
				Elements categorySize1 = doc.select("div .accordion-item"); 
				for (Element ele : categorySize1) {
					
					Elements trkEle = ele.select("div").eq(1);
					
			
					
					String trk = trkEle.attr("trk");
					if("233".equals(trk)){
						Elements categorySize =  ele.select("div").eq(2);;
						if(categorySize!=null&&!categorySize.isEmpty()){
							Element category4 = categorySize.select("dd").get(0);
							if(category4!=null){
								size = category4.text();
								size = size.replace(",",".");
							}
						}
						break;
					} 
				}
				
			
				int i=0;
				if(categoryPics!=null&&categoryPics.size()>0){
					for(Element categoryPic:categoryPics){
						i++;
						String pic1 = categoryPic.select("img").attr("data-fullsrc");
						pic.append(pic1).append("|");
						if(i==5){
							break;
						}
					}
				}
				
				if(categorys!=null&&categorys.size()>0){
						Elements category1  = categorys.select("p[itemprop=description]");
						
					
						if(category1!=null){
							desc = category1.text();
							desc = desc.replace(",",".");
								
						}
						Elements category2  = categorys.select("span[itemprop=sku]");
						if(category2!=null){
							barCode = category2.text();	
						}
						
						Elements category3 = categorys.select("p[data-tstid=designerStyleId]").select("span");
						if(category3!=null){
							productCode = category3.text();
						}
						//<span itemprop="color">32140</span>
						Elements category6 = categorys.select("span[itemprop=color]");
						if(category6!=null){
							colorCode = category6.text();	
						}
						
						Elements category4 = categorys.select("span[data-tstid=MadeInLabel]");
						if(category4!=null){
							made = category4.text();
						}
						Elements category5 = categorys.select("div .product-detail-dl");
						if(category5!=null){
							String ss = category5.text();
							int index = ss.indexOf("洗涤说明");
							//成分
							if(index<0){
								materal = ss.substring(0);
							}else{
								materal = ss.substring(0,ss.indexOf("洗涤说明"));
							}
						}
					
						map.put("productCode", productCode);
						if(StringUtils.isBlank(barCode)){
							System.out.println(barCode);
						}
						map.put("barCode", barCode);
						map.put("desc", desc);
						map.put("materl", materal);
						map.put("picUrl", pic.toString());
						map.put("made", made);
						map.put("size", size);
						map.put("colorCode",colorCode);
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

			String brandName = dto.getBrand();
			buffer.append(brandName).append(splitSign);
			// 货号
			buffer.append(
					null == dto.getProductCode() ? "" : dto
							.getProductCode().replaceAll(",", " ")).append(
					splitSign);
			// 供应商SKUID
			// 产品名称
			String productName = dto.getProductname();

			buffer.append(null==productName?"":productName.replace(","," ")).append(splitSign);

			buffer.append(dto.getBarCode()).append(
					splitSign);
			buffer.append(dto.getColorCode()).append(
					splitSign);

			// 获取材质
			String material = dto.getMaterl();
			if (StringUtils.isBlank(material)) {
				material = "";
			} else {

				material = material.replaceAll(splitSign, " ")
						.replaceAll("\\r", "").replaceAll("\\n", "");
			}

			buffer.append(material).append(splitSign);
			buffer.append(dto.getSize()).append(
					splitSign);
			// 获取产地
			String productOrigin = dto.getMade();

			buffer.append(productOrigin).append(splitSign);
			// 欧洲习惯 第一个先看 男女
			buffer.append(
					sex).append(splitSign);
			// 新的价格
			String newMarketPrice = dto.getPrice();
			buffer.append(newMarketPrice).append(splitSign);
			buffer.append(dto.getItemprice()).append(splitSign);
			buffer.append(dto.getItemdiscountA()).append(splitSign);
			buffer.append(dto.getItemsaleprice()).append(splitSign);
			buffer.append(dto.getMemo()).append(
					splitSign);
			buffer.append(dto.getDescript()).append(
					splitSign);
			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}