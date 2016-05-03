package com.shangpin.iog.pavinGroup.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.pavinGroup.dto.Channel;
import com.shangpin.iog.pavinGroup.dto.Item;
import com.shangpin.iog.pavinGroup.dto.Rss;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("pavignFrameFetchProduct")
public class PavignFrameFetchProduct extends AbsSaveProduct{

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
	private List<String> getCategoryUrl() {
		List<String> list = new ArrayList<String>();
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements ele1 = doc.select("#rss-table-category");
				Elements categorys = ele1.select("a[href]");
				for (Element category : categorys) {
					String url = category.attr("href");
					System.out.println(url);
					list.add(url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave11() {
		List<String> array = getCategoryUrl();
		List<Rss> list = null;
		try {
				for(int i=0;i<array.size();i++){
					list = new ArrayList<Rss>();
					System.out.println("-------------------------第"+(i+1)+"个开始--------------------------------");
					fetchProduct(array.get(i));
					System.out.println("-------------------------第"+(i+1)+"个结束--------------------------------");
				} 
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	private List<Item> allItem = new ArrayList<Item>();
	private void fetchProduct(String url){
		
		try {
			String xml = null;
			
			xml = HttpUtil45
					.get(url,
							new OutTimeConfig(1000 * 60*5, 1000 * 60*5, 1000 * 60*5),
							null);
			System.out.println(url);
				ByteArrayInputStream is = null;
				
				is = new ByteArrayInputStream(
						xml.getBytes("UTF-8"));
				Rss rss = null;
				rss = ObjectXMLUtil.xml2Obj(Rss.class, is);
				if(rss!=null){
					Channel channel = rss.getChannel();
					if(channel!=null){
						List<Item> item = channel.getListItem();
						if(item!=null){
							allItem.addAll(item);
						}
						
						if(channel.getNextPage()!=null){
							fetchProduct(channel.getNextPage());
						}
					}
				}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * message mapping and save into DB
	 */
	@Override
	public Map<String, Object> fetchProductAndSave() {
		fetchProductAndSave11();
//	private Map<String, Object> fetchProductAndSave(List<Item> array,String tile) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		
			if(allItem!=null){
				for (Item item : allItem) {
					SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(item.getSupplierSkuNo());
						spu.setCategoryName(item.getGroup_description());
						spu.setBrandName(item.getBrand());
						spu.setSpuName(item.getTitle());
						spu.setMaterial(item.getMaterial());
						spu.setCategoryGender(item.getGender());
						String desc = item.getDescription();
						int index = desc.indexOf("Made in");
						String sss = null;
				    	if(index!=-1){
				    		String s = desc.substring(index);	
				    		int i = s.indexOf("<br>");
				    		if(i!=-1){
				    			sss = s.substring(0,s.indexOf("<br>"));
				    			System.out.println(sss);
				    		}else{
				    			sss = s.substring(0);
				    			System.out.println(sss);
				    		}
				    		
				    	}
				    	spu.setProductOrigin(sss);
				    	spuList.add(spu);
				
					SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(item.getSupplierSkuNo());
						sku.setSkuId(item.getSupplierSkuNo());
						sku.setProductSize(item.getProductSize());
						sku.setStock(item.getStock());
						sku.setMarketPrice(item.getPrice());
						sku.setSalePrice("");
						sku.setSupplierPrice("");
						sku.setColor(item.getProductColor());
						sku.setProductName(item.getTitle());
						sku.setProductDescription(item.getDescription());
						 sku.setSaleCurrency("EUR");
						skuList.add(sku);						
					if (StringUtils.isNotBlank(item.getImages())) {
						String[] picArray = item.getImages().split("\\|");
						imageMap.put(sku.getSkuId()+";"+sku.getSkuId().split("_")[0]+" "+sku.getColor(), Arrays.asList(picArray));
					}
				}
			}
			returnMap.put("sku", skuList);
			returnMap.put("spu", spuList);
			returnMap.put("image", imageMap);
			return returnMap;
	}
	public static String picpath;
	public static int day;

	static {
		if (null == bdl)
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
	}
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	public static void main(String[] args) throws Exception {
	  	//加载spring
        loadSpringContext();
        PavignFrameFetchProduct stockImp =(PavignFrameFetchProduct)factory.getBean("pavignFrameFetchProduct");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, picpath);
	}
}



