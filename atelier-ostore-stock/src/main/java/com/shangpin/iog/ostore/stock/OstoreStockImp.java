package com.shangpin.iog.ostore.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by houkun on 2015/9/14.
 */
@Component("atelierOstoreStock")
public class OstoreStockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
	private static String host;
	private static String app_key;
	private static String app_secret;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("sop");
        supplierId = bdl.getString("supplierId");
        host = bdl.getString("HOST");
		app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
        url = bdl.getString("url");
    }
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	Map<String, String> skuMap = getOldData();
    	int num = 0;
    	String data = "";
    	String skuData = HttpUtil45.post(url+"GetAllAvailabilityMarketplace",
				new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
		String[] skuStrings = skuData.split("\\r\\n");
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
			
				if (i==1) {
					  data =  skuStrings[i].split("\\n")[1];
					}else {
					  data = skuStrings[i];
					}
					String[] skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
        			String stock = skuArr[2];
        			String barCode = skuArr[5];
        			skuMap.put(skuArr[0]+"-"+barCode, stock);
        			if(!stock.equals("0")){
        				num++;
        			}
			}
		}
		logger.info("新数据库存不为0的有：：："+num);
        Map<String,Integer> returnMap = new HashMap<String,Integer>();
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        logger.info("循环赋值");
        String skuId = "";
        String stock = "0";
        while (iterator.hasNext()){
        	skuId = iterator.next();
        	if (StringUtils.isNotBlank(skuId)) {
        		if (skuMap.containsKey(skuId)) {
        			stock = skuMap.get(skuId);
        			returnMap.put(skuId, Integer.valueOf(stock));
				}else{
					returnMap.put(skuId, 0);
				}
			}
        }
        return returnMap;
    }
    
    public Map<String,String> getOldData(){
    	logger.info("=================获取老数据=================");
    	int num = 0;
        Map<String,String> stock_map = new HashMap<>();

        String url = "http://b2b.officinastore.com/shangpin.asp?mode=stock_only";
        
        OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
        timeConfig.confRequestOutTime(5*60*1000);
        timeConfig.confConnectOutTime(1000*60*30);
        timeConfig.confSocketOutTime(1000*60*30);
        List<String> resultList = HttpUtil45.getContentListByInputSteam(url, timeConfig, null, null, null);

        int i=0;
        String stock="",size ="";
        String skuId = "";
        for(String content:resultList) {
            if (i == 0) {
                i++;
                continue;
            }
            i++;
            //SKU;Season;Sizes
            // 0 ;  1   ;  2
            String[] contentArray = content.split(";");
            if (null == contentArray || contentArray.length < 3) continue;

            String[] sizeArray = contentArray[2].split(",");
            for(String sizeAndStock:sizeArray) {
                if(sizeAndStock.contains("(")&&sizeAndStock.length()>1) {
                    size = sizeAndStock.substring(0, sizeAndStock.indexOf("("));
                    stock = sizeAndStock.substring(sizeAndStock.indexOf("(")+1, sizeAndStock.length() - 1);
                }

                skuId = contentArray[0] + "-"+size;
                if(skuId.indexOf("½")>0){
                    skuId = skuId.replace("½","+");
                }
                stock_map.put(skuId,stock);
                if (!stock.equals("0")) {
					num++;
				}
            }
        }
        logger.info("老数据库存不为0的数据有：：："+num);
        return stock_map;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        OstoreStockImp stockImp =(OstoreStockImp)factory.getBean("atelierOstoreStock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("ostoreATELIER更新数据库开始");
        try {
			stockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
        } catch (Exception e) {
			logger.info("ostoreATELIER更新库存数据库出错"+e.toString());
		}
        logger.info("ostoreATELIER更新数据库结束");
        System.exit(0);
    }
}
