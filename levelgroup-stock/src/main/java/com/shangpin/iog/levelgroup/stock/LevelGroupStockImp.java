package com.shangpin.iog.levelgroup.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.levelgroup.dto.Product;
import com.shangpin.iog.levelgroup.util.MyTxtUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
@Component("levelgroup")
public class LevelGroupStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
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
        host = bdl.getString("host");
        app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
    }


    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());

        Map<String,String> mongMap = new HashMap<>();
        Map<String,String> stockMap2 = getStockList();

        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","levelgroup");

//        logMongo.info(mongMap);

        for (String skuno : skuNo) {
            String stock = getStock(skuno);
            String stock2 = stockMap2.get(skuno);
            if (StringUtils.isNotEmpty(stock))
                skustock.put(skuno, Integer.valueOf(stock));
            else if (StringUtils.isNotEmpty(stock2)){
            	logger.info(skuno+"===通过文件获取库存====");
                skustock.put(skuno, Integer.valueOf(stock2));
            }            	
            else
                skustock.put(skuno, 0);
        }
        logger.info("levelgroup赋值库存数据成功");
        return skustock;
    }

    private static Map<String,String> getStockList(){
        boolean flag = false;
        int j=0;
        try {
        	while(!flag && j<10){
        		logger.info("=================第"+j+"次开始下载文件=================="); 
        		flag = MyTxtUtil.txtDownload();
        		j++;
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("下载LEVELGROUP文件失败!"+e.getMessage());
        }
        List<Product> list = null;
        Map<String,String> map = new HashMap();
        if (flag){
            try {
                list = MyTxtUtil.readTXTFile();
            } catch (Exception e) {
            	loggerError.error("读取txt文件时出错====="+e); 
            }
            for (int i = 0;i<list.size();i++){
                map.put(list.get(i).getVARIANT_SKU(),list.get(i).getSTOCK_LEVEL());
            }
        }
        return map;
    }

    private static String getStock(String sku){
    	
    	if (sku.length()<15) {
			sku = "09"+sku;
		}
    	
        String url = "http://www.ln-cc.com/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=09&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";

        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
        if( jsonstr != null && jsonstr.length() >0){
            JSONObject json = JSONObject.fromObject(jsonstr);
            if (!json.isNullObject() && !json.containsKey("fault")) {
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
        //加载spring
        loadSpringContext();
        //拉取数据
        LevelGroupStockImp levelGroupStockImp =(LevelGroupStockImp)factory.getBean("levelgroup");
//        levelGroupStockImp.setUseThread(true);levelGroupStockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("levelgroup更新数据库开始");
        //2015081401431
        try {
        	levelGroupStockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
        } catch (Exception e) {
            loggerError.error("levelgroup库存更新失败");
            e.printStackTrace();
        }
        logger.info("levelgroup更新数据库结束");
        System.exit(0);
    }
}
