package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	private static int pageTem;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
		sex = bdl.getString("gender");
		path = bdl.getString("path");
		pageTem = Integer.parseInt(bdl.getString("page"));
	}
	public  void getUrlList() throws Exception {
		
		
		
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
					pageCount = pageTem;
				}
				for(int i=1;i<=pageCount;i++){
					response = HttpUtils.get(uri+"&page="+i);
//					HttpResponse response = HttpUtils.get("http://www.farfetch.com/cn/shopping/women/clothing-1/items.aspx?ffref=hd_mnav&discount=0-0&page=66");
					System.out.println("第"+i+"页："+uri+"&page="+i);
					if (response.getStatus()==200) {
						String htmlContent = response.getResponse();
						Document doc = Jsoup.parse(htmlContent);
						Elements pageEle  = doc.select("span[data-tstid=paginationTotal]");
						Elements categorys = doc.select("div.listing-flexbox");
						Elements category1 = categorys.select("div .baseline").select(".col9").select(".col-md-8");
						Elements category2 = category1.select("section");
						Elements category3 = category2.select("article");
						int j=0;
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
							price = price.replace(",","").substring(1);
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
							pro.setPrice(price);
							pro.setMemo("第"+i+"页第"+j+"数据");
							messMappingAndSave(pro);
						}
						System.out.println("供拉取商品数量："+j+"个");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			   InputStream in = new BufferedInputStream(new ByteArrayInputStream(buffer.toString().getBytes("gb2312")));
			   File file = new File(path);
			   FileOutputStream out = new FileOutputStream(file);
	            byte[] data = new byte[1024];
	            int len = 0;
	            while (-1 != (len=in.read(data, 0, data.length))) {
	                out.write(data, 0, len);
	            }
	            out.flush();
	            out.close();

			
	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();
		String  materal = null;
		String  desc= null;
		String  barCode= null;
		String productCode = null;
		String productName = null;
		String made = null;
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements categorys = doc.select("div .accordion").select(".accordion-xl").select(".product-detail");
				Elements categoryPicUrl = doc.select("#PDPContainer") ;
				Elements categoryPic = doc.select(".sliderProduct").select(".js-sliderProductFull").select("li").eq(0);
				String pic = categoryPic.select("img").attr("data-fullsrc");
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
							Elements category4 = ele.select("span[data-tstid=MadeInLabel]");
							if(category4!=null){
								made = category4.text();
								
							}
							Elements category2  = ele.select("span[itemprop=sku]");
							Elements category3 = ele.select("p[data-tstid=designerStyleId]").select("span");
							barCode = category2.text();
							productCode = category3.text();
							
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
								desc= ss.substring(ss.indexOf("洗涤说明"));
							}
					    	//品牌编号
					    	map.put("productCode", productCode);
					    	map.put("barCode", barCode);
					    	map.put("desc", desc);
					    	map.put("materl", materal);
					    	map.put("picUrl", pic);
					    	map.put("made", made);
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
	int i=0;
	public void messMappingAndSave(Product item) {
	
		String bs=exportExvel(item);
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
	static String splitSign = ",";
	
	static StringBuffer buffer = new StringBuffer("BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
			+ "SopProductName 商品名称" + splitSign + "BarCode 条形码" + splitSign
			+  "material 材质" + splitSign
			  + "ProductOrigin 产地" + splitSign
			+ "Gender 性别" + splitSign+ "新市场价"+ splitSign  
			 + "PcDesc 描述" + splitSign
			+ "图片" + splitSign+  "备注").append("\r\n");
	private static String exportExvel(Product dto){

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
			} catch (Exception e) {
			}

		return buffer.toString();
		
		
	}
}



