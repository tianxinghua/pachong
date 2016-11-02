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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
@Component("opticalscribe")
public class WangFetchProduct {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String uri;
	private static String sex;
	private static String path;
	private static int pageStart;
	private static int pageEnd;
//	private static FileWriter out = null; 
	private static OutputStreamWriter  out= null;
	static String splitSign = ",";
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
//		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
		sex = bdl.getString("gender");
		path = bdl.getString("path");
		if(!bdl.getString("pageStart").isEmpty()){
			pageStart = Integer.parseInt(bdl.getString("pageStart"));
		}
		if(!bdl.getString("pageEnd").isEmpty()){
			pageEnd = Integer.parseInt(bdl.getString("pageEnd"));
		}
		
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
//			out = new FileWriter(new File(path),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<Collection<String>> subCollection(Collection<String> skuNoSet) {
		int thcnt = 100;
		List<Collection<String>> list=new ArrayList<>();
		int count=0;int currentSet=0;
		for (Iterator<String> iterator = skuNoSet.iterator(); iterator
				.hasNext();) {
			String skuNo = iterator.next();
			if(count==thcnt)
				count=0;
			if(count==0){
				Collection<String> e = new ArrayList<>();
				list.add(e);
				currentSet++;
			}
			list.get(currentSet-1).add(skuNo);
			count++;
		}
		return list;
	}
//	class UpdateThread extends Thread{
//		private Collection<String> skuNos;
//		private Map<String, String> localAndIceSkuId;
//		private String html;
//		private List<Integer> totoalFailCnt;
//		private Map<String,String> sopPriceMap;
//
//		public UpdateThread(String html) {
//			this.html = html;
//		}
//		@Override
//		public void run() {
//			try {
//				fetch(html);
//			} catch (Exception e) {
//				logger.warn(Thread.currentThread().getName() + "处理出错", e);
//			}
//		}
//
//	}
	int j=0;
	private void fetch(String htmlContent){
		Document doc = Jsoup.parse(htmlContent);
		Elements pageEle  = doc.select("span[data-tstid=paginationTotal]");
		Elements categorys = doc.select("div.listing-flexbox");
		Elements category1 = categorys.select("div .baseline").select(".col9").select(".col-md-8");
		Elements category2 = category1.select("section");
		Elements category3 = category2.select("article");
		
		for (Element category : category3) {
			j=j+1;
			Elements picUrl1 = category.select("div");
			Elements picUrl2 = picUrl1.select("a");
			String proUrl ="http://www.farfetch.com"+ picUrl2.attr("href");
			String picUrl = picUrl2.select("noscript").select("img").attr("src");
			Map<String,String> map = getProductUrl(proUrl);
			
			Element product = category.select("a").last();
			String brand = product.select("h5").text();
			String productName = product.select("p").text();
			String price = product.select("span").last().text();
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
			pro.setPrice(price);
			pro.setMemo("第"+i+"页第"+j+"数据");
			messMappingAndSave(pro);
		}
		System.out.println("供拉取商品数量："+j+"个");
	}
	public  void getUrlList() throws Exception {
		
		
		System.out.println("文件保存目录："+path);
		
			 StringBuffer buffer = new StringBuffer("BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
					+ "SopProductName 商品名称" + splitSign + "BarCode 条形码" + splitSign
					+  "material 材质" + splitSign
					+  "size尺码" + splitSign
					  + "ProductOrigin 产地" + splitSign
					+ "Gender 性别" + splitSign+ "新市场价"+ splitSign  
					 + "PcDesc 描述" + splitSign
					+ "图片" + splitSign+  "备注").append("\r\n");
			 out.write(buffer.toString());
			String page = null;
			int pageCount = 0;
			try {
				HttpResponse response = HttpUtils.get(uri);
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Element pageEle  = doc.select("span[data-tstid=paginationTotal]").get(0);
					page = pageEle.text();
					System.out.println("总页数："+page);
				}
				if(page!=null){
					pageCount = Integer.parseInt(page);
				}else{
					pageCount = pageEnd;
				}
				if(pageStart==0){
					pageStart = 1;
				}
				
//				int poolCnt=pageCount;
//				ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
//				final List<Collection<String>> subSkuNos=subCollection();
//				logger.warn("线程池数："+(poolCnt/4+1));
//				for(int i = 0 ; i <subSkuNos.size();i++){
//					Map<String,String> sopPriceMap = new HashMap<>();
//					
//				}
				
				for(int i=pageStart;i<=pageCount;i++){
					response = HttpUtils.get(uri+"&page="+i);
//					HttpResponse response = HttpUtils.get("http://www.farfetch.com/cn/shopping/women/clothing-1/items.aspx?ffref=hd_mnav&discount=0-0&page=66");
					System.out.println("第"+i+"页："+uri+"&page="+i);
					if (response.getStatus()==200) {
						i=i+1;
						String htmlContent = response.getResponse();
//						exe.execute(new UpdateThread(htmlContent));
						fetch(htmlContent);
						
					}
				}
				out.close();
//				exe.shutdown();
//				while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {
//
//				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
			
	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();
		String  materal = null;
		String  desc= null;
		String  barCode= null;
		String productCode = null;
		String productName = null;
		String made = null;
		String size = null;
		StringBuffer pic = new StringBuffer();
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements categorys = doc.select("div .accordion").select(".accordion-xl").select(".product-detail");
				Elements categoryPicUrl = doc.select("#PDPContainer") ;
				Elements categoryPics = doc.select(".sliderProduct").select(".js-sliderProductFull").select("li");
			
				int i=0;
				for(Element categoryPic:categoryPics){
					i++;
					String pic1 = categoryPic.select("img").attr("data-fullsrc");
					pic.append(pic1).append("|");
					if(i==4){
						break;
					}
				}
				
				
				Elements category1 = doc.select("div .accordion-item"); 
//				String category1 = categorys.select("div").attr("data-index");
//				String s= category1.text(); 
				if(categorys!=null){
					for (Element ele : category1) {
						
						Elements trkEle = ele.select("div").eq(1);
						Elements descEle = ele.select("div").eq(2);
						
						String trk = trkEle.attr("trk");
						
						if("258".equals(trk)){
							productName = descEle.select("p").eq(0).text();
							productName = productName.replace(",",".");
							Elements category4 = ele.select("span[data-tstid=MadeInLabel]");
							if(category4!=null){
								made = category4.text();
								
							}
							Elements category5  = ele.select("p[itemprop=description]");
							desc = category5.text();
							desc = desc.replace(",",".");
							Elements category2  = ele.select("span[itemprop=sku]");
							Elements category3 = ele.select("p[data-tstid=designerStyleId]").select("span");
							barCode = category2.text();
							productCode = category3.text();
							
						} 
						if("233".equals(trk)){
							Elements categorySize = ele.select("div .product-detail-dl");
							Element category4 = categorySize.select("dd").get(0);
							if(category4!=null){
								size = category4.text();
								size = size.replace(",",".");
							}
							
						} 
						if("29".equals(trk)){ 
							
							Elements category2 = ele.select("div .product-detail-dl");
							StringBuffer str = new StringBuffer();
							String ss = category2.text();
							
							int index = ss.indexOf("洗涤说明");
						
							//成分
							
							if(index<0){
								materal = ss.substring(0);
							}else{
								materal = ss.substring(0,ss.indexOf("洗涤说明"));
							}
					    	//品牌编号
					    	map.put("productCode", productCode);
					    	map.put("barCode", barCode);
					    	map.put("desc", desc);
					    	map.put("materl", materal);
					    	map.put("picUrl", pic.toString());
					    	map.put("made", made);
					    	map.put("size", size);
					    	map.put("productName", productName);
					    	
					    	break;
						}
					
					}
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
	int i=1;
	
	public void messMappingAndSave(Product item) {
	
		exportExvel(item);
//			SpuDTO spu = new SpuDTO();
//			try {
//				i=i+1;
//				spu.setId(UUIDGenerator.getUUID());
//				spu.setSupplierId(supplierId);
//				spu.setSpuId(item.getBarCode());
//				spu.setCategoryGender(sex);
//				spu.setBrandName(item.getBrand());
//				spu.setMaterial(item.getMaterl());
//				spu.setProductOrigin(item.getMade());
//				spu.setSpuName(item.getProductname());
//				spu.setPicUrl(item.getUrl());
//				productFetchService.saveSPU(spu);
//			} catch (Exception e) {
//				try {
//					productFetchService.updateMaterial(spu);
//				} catch (ServiceException e1) {
//					e1.printStackTrace();
//				}
//			}
//			SkuDTO sku = new SkuDTO();
//			try {
//				sku.setId(UUIDGenerator.getUUID());
//				sku.setSupplierId(supplierId);
//				sku.setSpuId(item.getBarCode());
//				sku.setSkuId(item.getBarCode());
//				sku.setMarketPrice(item.getPrice());
//				sku.setProductCode(item.getProductCode());
//				sku.setProductName(item.getProductname());
//				sku.setColor(item.getDescript());
//				sku.setBarcode(item.getBarCode());
//				sku.setMemo(item.getMemo());
//				productFetchService.saveSKU(sku);
//			
//			} catch (ServiceException e) {
//				if (e.getMessage().equals("数据插入失败键重复")) {
//					try {
//						productFetchService.updatePriceAndStock(sku);
//					} catch (ServiceException e1) {
//						e1.printStackTrace();
//					}
//				} else {
//					e.printStackTrace();
//				}
//			}
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

				buffer.append(productName).append(splitSign);

				buffer.append(dto.getBarCode()).append(
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
				buffer.append(dto.getDescript()).append(
						splitSign);
				
				buffer.append(dto.getUrl()).append(
						splitSign);
				buffer.append(dto.getMemo());
				buffer.append("\r\n");
				 out.write(buffer.toString());
			    out.flush();
			} catch (Exception e) {
			}
	}
}



