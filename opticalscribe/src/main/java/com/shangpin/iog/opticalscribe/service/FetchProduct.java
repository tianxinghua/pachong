package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.opticalscribe.dto.Channel;
import com.shangpin.iog.opticalscribe.dto.Item;
import com.shangpin.iog.opticalscribe.dto.Product;
import com.shangpin.iog.opticalscribe.dto.Rss;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("opticalscribe")
public class FetchProduct {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String uri;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	private static List getUrlList() {
		List<String> list = new ArrayList();
			try {
				HttpResponse response = HttpUtils.get(uri);
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements categorys = doc.select("#main-nav").select("li");
					for (Element category : categorys) {
						Elements picUrls = category.select(".sub-nav").select("li");
						for (Element cat : picUrls) {
							String picUrl=	cat.select("a").attr("href");
//							String name = category.select("a").text();
							System.out.println(picUrl);
							list.add(picUrl);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}
	
	
	private static List<String> proUrlList = null;
	private static void getCategoryUrl() {
		
		proUrlList = new ArrayList();
		StringBuffer s = new StringBuffer();
	
		List<String> list = getUrlList();
		for(String url :list){
			getProductUrl(url);
		}
	}
	
	private static void getProductUrl(String url) {
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Element categorys = doc.select("#content > div").first();
				if(categorys!=null){
					Elements div = categorys.select("div.wf-cell");
					for (Element category : div) {
						String picUrl = category.select("a").attr("href");
						proUrlList.add(picUrl);
					}
					Elements next = doc.select("#content > div").select("div.woocommerce-pagination").select("div.page-nav").select(".nav-next");
					String nextPage = next.select("a").attr("href");
					if(!nextPage.equals("")){
						getProductUrl(nextPage);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
private static List getProductList() {
		
		List<Product> lists = new ArrayList();
//		proUrlList = new ArrayList();
		getCategoryUrl();
//		proUrlList.add("http://www.opticalscribe.com/prodotto/christian-dior-so-real-appdc-size-4822/");
		for(String url :proUrlList){
			Product pro = null;
			try {
				HttpResponse response = HttpUtils.get(url);
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					pro = new Product();
					Element categorys = doc.select("#content > div").first();
					
					//商品spu
					String spuId = categorys.attr("id");
					pro.setSpuId(spuId);
					//图片
					String pic = categorys.select("div.images").select("a").attr("href");
					pro.setUrl(pic);
					Elements s = categorys.select("div.summary").select("> div");
					
					Elements ds = s.select("div[itemprop]");
					Elements f = ds.first().select("meta");
					//价格
					String price = f.first().attr("content");
					pro.setPrice(price);
					//币种
					String priceCurrey = f.get(1).attr("content");
					pro.setPriceCurry(priceCurrey);
					//商品sku
					String sku = s.select(".product_meta").select(".sku").text();
					pro.setSkuId(sku);
					//描述
					String desc = categorys.select("#tab-description").select("p").text();
					pro.setDesc(desc);
					Elements color = categorys.select("#tab-additional_information").select("tr");
					for(Element col : color){
						
						String param = col.select("th").text();

						String value = col.select("p").text();
						reflect(pro,param,value);
						System.out.println(value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			lists.add(pro);
		}
		return lists;
	}
	private static void reflect(Product pro ,String param,String value) throws Exception, SecurityException{
		
		Class<Product> c =(Class<Product>) pro.getClass();
		Method[] methods = c.getMethods();
		for(Method me : methods){
			if(me.getName().equals("set"+param)){
				 Method method=c.getMethod("set"+param, String.class);  
		          method.invoke(pro, value);  
		          break;
			}
		}
		 
	}
	/**
	 * fetch product and save into db
	 */
	/**
	 * message mapping and save into DB
	 */
	public void messMappingAndSave() {
		
		List<Product> s = getProductList();
		for (Product item : s) {
			SpuDTO spu = new SpuDTO();
			try {
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(item.getSpuId());
				spu.setCategoryName(item.getDesc());
				spu.setBrandName(item.getBrand());
				spu.setMaterial(item.getFrame());
				productFetchService.saveSPU(spu);
			} catch (Exception e) {
				try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
			}

			SkuDTO sku = new SkuDTO();
			try {
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(item.getSpuId());
				sku.setSkuId(item.getSkuId());
				sku.setProductSize(item.getSize());
				sku.setMarketPrice(item.getPrice());
				sku.setColor(item.getColor());
				sku.setProductDescription(item.getDesc());
				sku.setSaleCurrency(item.getPriceCurry());
				sku.setMarketPrice(item.getPrice());
				productFetchService.saveSKU(sku);
				
			} catch (ServiceException e) {
				if (e.getMessage().equals("数据插入失败键重复")) {
					try {
						productFetchService.updatePriceAndStock(sku);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				} else {
					e.printStackTrace();
				}
			}
			if (StringUtils.isNotBlank(item.getUrl())) {
				String[] picArray = {item.getUrl()};
				productFetchService.savePicture(supplierId, null, item.getSkuId(), Arrays.asList(picArray));
			}
		}
	}

}



