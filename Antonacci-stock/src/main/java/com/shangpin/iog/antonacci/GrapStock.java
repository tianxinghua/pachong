package com.shangpin.iog.antonacci;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.antonacci.dto.Product;
import com.shangpin.iog.antonacci.dto.Products;
import com.shangpin.iog.antonacci.dto.Size;
import com.shangpin.iog.antonacci.schedule.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

@Component("antonacci")
public class GrapStock extends AbsUpdateProductStock{
	private static void holdorder() throws Exception{
//		String json = " {\"order_no\":\"201703091201\",\"order_items\":\"[{'sku_id':'P15-BE08947-003-100-481121','quantity':1}]\"}";
//		Object js =JSON.parse(json);
//		System.out.println("url:http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9");
//		System.out.println("parameter:"+json);
//		String tr = HttpUtil45.operateData("put","json","http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9", outTimeConf, null, json, null, null);
//		System.out.println(tr);
//	}
//	private static void confirmOrder() throws Exception{
//		String json = " {\"purchase_no\":\"CGDF20170313\",\"order_no\":\"201703091201\",\"order_items\":\"[{'sku_id':'P15-BE08947-003-100-48','quantity':1}]\"}";
//		Object js =JSON.parse(json);
//		System.out.println("url:http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9");
//		System.out.println("parameter:"+json);
//		String tr = HttpUtil45.operateData("delete","json","http://www.luxury888.it/orders/e3eb4b7dd1bc4d33bfe54a095485b6b9", outTimeConf, null, json, null, null);
//		System.out.println(tr);
//	}
//	private static void cancelorder() throws Exception{
//		String json = " {\"order_no\":\"201703091201\",\"status\":\"free\"}";
//		Object js =JSON.parse(json);
//		System.out.println("url:http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9");
//		System.out.println("parameter:"+json);
//		Map map = new HashMap();
//		map.put("order_no","201703091201");
//		map.put("status","free");
//		String tr = HttpUtil45.operateData("delete","","http://www.luxury888.it/holdorders/e3eb4b7dd1bc4d33bfe54a095485b6b9", outTimeConf, map, json, null, null);
//		System.out.println(tr);
//	}
	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60,
			1000 * 60 * 5, 1000 * 60 * 5);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String uri;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {

		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		String result = HttpUtil45.get(uri, outTimeConf, null);
//		result = result.replaceAll("<Season>", "<season>").replaceAll("</Season>", "</season>");
//		result = result.replaceAll("<Color>", "<color>").replaceAll("</Color>", "</color>");
//		result = result.replaceAll("<Material>", "<material>").replaceAll("</Material>", "</material>");
		logInfo.info(result); 			
		Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
		if (products.getProduct().size() > 0) {
			for (Product product : products.getProduct()) {
				if(null != product.getSizes() && null != product.getSizes().getSize()){
					for(Size size :product.getSizes().getSize()){
						//sku
						try{
							String productsize = size.getSize_desc().replaceAll("½", ".5");
							stockMap.put(product.getCode()+"-"+productsize, size.getSize_stock());
							
						}catch(Exception ex){
							
						}
					}
				}
			}
		}
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		
		//System.out.println(stockMap.toString());
		return skustock;
	}
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
//		GrapStock grabStockImp = (GrapStock)factory.getBean("antonacci");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logInfo.info("antonacci更新数据库开始 start");
//		System.out.println("antonacci更新数据库开始");
//		try {
//			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//		} catch (Exception e) {
//			logError.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logInfo.info("antonacci更新数据库结束");
//		System.out.println("antonacci更新数据库结束 over");
//		System.exit(0);
		
	}

}
