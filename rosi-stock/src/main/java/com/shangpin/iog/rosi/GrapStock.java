package com.shangpin.iog.rosi;

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
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.rosi.dto.Channel;
import com.shangpin.iog.rosi.dto.Item;
import com.shangpin.iog.rosi.schedule.AppContext;

@Component("rosiStock")
public class GrapStock extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*60,
			1000 * 60 * 60, 1000 * 60 * 60);
	
	private static String uri = "";
	
	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        uri = bdl.getString("uri");
    }
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		
		String result  = "";
		Channel channel = null;
		try{
			result = HttpUtil45.get(uri, outTimeConf, null);
			result = result.replaceAll("Discounted-price", "Discounted_price");
			result = result.replaceAll("product-code", "product_code");		
			channel = XMLUtil.gsonXml2Obj(Channel.class, result);
		}catch(Exception e){
			logError.error(e);
			return skustock;
		}		
		if(channel != null && channel.getItem().size()>0){
			System.out.println("------------一共"+channel.getItem().size()+"条数据---------------"); 
			logger.info("------------一共"+channel.getItem().size()+"条数据---------------"); 
			for(Item item : channel.getItem()){
				stockMap.put(item.getSku(), item.getStock());
			}
		}
		logger.info("供货商这边的map大小是======="+stockMap.size());
//		logger.info("sop这边的skuid是========"+skuNo.toString()); 
		
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		logger.info("供货商返回的map的大小是======="+skustock.size()+"=======");
		logger.info(skustock);
		return skustock;
	}

	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
//		GrapStock grabStockImp = (GrapStock)factory.getBean("rosiStock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("-----rosiStock update stock start-------");
//		System.out.println("-----rosiStock update stock start-------");
//		try {
//			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//		} catch (Exception e) {
//			logError.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logger.info("-----rosiStock update stock end-------");
//		System.out.println("-----rosiStock update stock end-------");
//		System.exit(0);
//		try{
//			GrapStock grabStockImp = new GrapStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		
	}

	
}
