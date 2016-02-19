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
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.rosi.dto.Channel;
import com.shangpin.iog.rosi.dto.Item;

@Component("rosiStock")
public class GrapStock extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 5*60,
			1000 * 60 * 5, 1000 * 60 * 5);
	
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
		
		String result = HttpUtil45.get(uri, outTimeConf, null);
		result = result.replaceAll("Discounted-price", "Discounted_price");
		result = result.replaceAll("product-code", "product_code");		
		Channel channel = XMLUtil.gsonXml2Obj(Channel.class, result);
		if(channel != null && channel.getItem().size()>0){
			System.out.println("------------一共"+channel.getItem().size()+"条数据---------------"); 
			logger.info("------------一共"+channel.getItem().size()+"条数据---------------"); 
			for(Item item : channel.getItem()){
				stockMap.put(item.getSKUAndSize(), item.getStock());
			}
		}
		
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		
		return skustock;
	}

	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
		GrapStock grabStockImp = (GrapStock)factory.getBean("rosiStock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("-----rosiStock update stock start-------");
		System.out.println("-----rosiStock update stock start-------");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			loggerError.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("-----rosiStock update stock end-------");
		System.out.println("-----rosiStock update stock end-------");
		System.exit(0);
//		try{
//			GrapStock grabStockImp = new GrapStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		
	}

	
}
