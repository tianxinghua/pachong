package com.shangpin.iog.woolrich.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.sop.AbsUpdateProductStock;

@Component("woolrichstock")
public class WoolrichStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static ResourceBundle bdl=null;
    private static String supplierId;
	private static String host;
	private static String app_key;
	private static String app_secret;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("sop");
        supplierId = bdl.getString("supplierId");
        host = bdl.getString("HOST");
        app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
    }


    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        for (String skuno : skuNo) {
            String stock = getStock(skuno);
            if (StringUtils.isNotEmpty(stock)){
            	skustock.put(skuno, Integer.valueOf(stock));
            }else{
            	skustock.put(skuno, 0);
            }
        }
        logger.info("woolrich赋值库存数据成功");
        return skustock;
    }

    private static String getStock(String sku){
    	String url = "";
    	if (sku.substring(0, 2).equals("07")) {
    		url = "http://www.woolrich.eu/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=07&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}else if(sku.substring(0, 2).equals("02")){
			url = "http://www.aspesi.com/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=02&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}else if(sku.substring(0, 2).equals("05")){
			url="http://www.casadei.com/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=05&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}
        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
        if( jsonstr != null && jsonstr.length() >0){
            JSONObject json = JSONObject.fromObject(jsonstr);
            if (!json.isNullObject() && !json.containsKey("fault")) {
            	String string = json.getString("c_madeIn");
            	System.out.println(string);
                JSONObject inventObj = json.getJSONObject("inventory");
                if (!inventObj.isNullObject() && !inventObj.isEmpty()){
                    int instock = inventObj.getInt("stock_level");
                    return instock+"";
                }
            }
        }
        return null;
    }

    public static void main(String args[]) throws Exception {
        //加载spring 078055683960235
        loadSpringContext();
        //拉取数据
        WoolrichStockImp woolrichStockImp =(WoolrichStockImp)factory.getBean("woolrichstock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("woolrich更新数据库开始");
        //2015081401431
        try {
        	woolrichStockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
        } catch (Exception e) {
            loggerError.error("woolrich库存更新失败");
            e.printStackTrace();
        }
        logger.info("woolrich更新数据库结束");
        System.exit(0);
    }
}
