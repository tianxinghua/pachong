package com.shangpin.iog.newMenlook_stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

@Component("menlook")
public class GrabStock extends AbsUpdateProductStock{

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
		Map<String, String> skustock = new HashMap<>();
		for(String id:skuNo){
			String url = "https://staging.menlook.com/dw/shop/v15_4/products/"+id+"/availability?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26&expand=images,prices,variations";
			try{
				
				String result = HttpUtil45.get(url, outTimeConf, null);
				String stock =""; 
				stock = String.valueOf(JSONObject.fromObject(result).getJSONObject("inventory").getInt("stock_level"));
				if(StringUtils.isNotBlank(stock)){
					skustock.put(id,stock);
				}else{
					skustock.put(id,"0");
				}				
								
			}catch(Exception ex){
				skustock.put(id,"0");
				logError.error(ex);
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
		GrabStock grabStockImp = (GrabStock)factory.getBean("menlook");		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("menlook更新数据库开始");
		System.out.println("menlook更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("menlook更新数据库结束");
		System.out.println("menlook更新数据库结束");
		System.exit(0);
		
	}
}
