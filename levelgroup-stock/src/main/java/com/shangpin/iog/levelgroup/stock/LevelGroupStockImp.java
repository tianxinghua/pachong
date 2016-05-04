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
	
	private static String urllncc = null;
	private static String url = null;
	private static String pathincc = null;
	private static String path = null;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("sop");
        supplierId = bdl.getString("supplierId");
        host = bdl.getString("host");
        app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
		
		urllncc = bdl.getString("urllncc");
		url = bdl.getString("url");
		pathincc = bdl.getString("pathincc");
		path = bdl.getString("path");
    }


    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());

        Map<String,String> mongMap = new HashMap<>();
        //从文件里获取
        Map<String,String> stockMap2 = getStockList();

        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","levelgroup");

//        logMongo.info(mongMap);

        for (String skuno : skuNo) { 
        	String stock = "";
            String stock2 = stockMap2.get(skuno); 
            
            if (StringUtils.isNotEmpty(stock2)){//先在文件里查找             	
                skustock.put(skuno, Integer.valueOf(stock2));
//            }else if (StringUtils.isNotEmpty(stock = getStock(skuno))){//查找不到再去接口
//            	logger.info(skuno+"===通过接口获取库存===="+stock);
//            	skustock.put(skuno, Integer.valueOf(stock));
            }else{
            	skustock.put(skuno, 0);
            }
                
        }
        logger.info("levelgroup赋值库存数据成功");
        return skustock;
    }

    private static Map<String,String> getStockList(){
    	
        Map<String,String> map = new HashMap();
        boolean flag = false;
        int j=0;
        try {
        	while(!flag && j<10){//如果下载失败，尝试10遍！！！！
        		logger.info("=============="+url+"第"+j+"次开始下载文件=================="); 
        		flag = MyTxtUtil.txtDownload(url,path);
        		j++;
        	}
        	logger.info("================="+path+"下载结束=================="); 
        	List<Product> list = MyTxtUtil.readTXTFile(path);
        	for (int i = 0;i<list.size();i++){
        		try {
        			map.put(list.get(i).getVARIANT_SKU(),list.get(i).getSTOCK_LEVEL());
				} catch (Exception e) {
					
				}                
            }
        	
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("下载"+path+"文件失败!"+e.getMessage());
        }
        
        boolean flag2 = false;
        int k=0;
        try {
        	while(!flag2 && k<10){//如果下载失败，尝试10遍！！！！
        		logger.info("=============="+urllncc+"第"+k+"次开始下载文件=================="); 
        		flag2 = MyTxtUtil.txtDownload(urllncc,pathincc);
        		k++;
        	}
        	logger.info("================="+pathincc+"下载结束=================="); 
        	List<Product> list = MyTxtUtil.readTXTFile(pathincc);
        	for (int i = 0;i<list.size();i++){
        		try {
        			map.put(list.get(i).getVARIANT_SKU(),list.get(i).getSTOCK_LEVEL());
				} catch (Exception e) {
					
				}                
            }
        	
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("下载"+pathincc+"文件失败!"+e.getMessage());
        }
        
        return map;
    }

    private static String getStock(String sku){
    	
    	try {
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
		} catch (Exception e) {
			loggerError.error(e); 
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
