package com.shangpin.iog.daniello.stock;

import com.shangpin.framework.ServiceException;


import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by houkun on 2015/9/14.
 */
@Component("daniellostock")
public class DanielloStockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String user,password;
    private static String url;
    private static ApplicationContext factory;
    private static String saveLocal;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("sop");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        user = bdl.getString("user");
        password = bdl.getString("password");
        saveLocal = bdl.getString("saveLocal");
    }
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	Map<String,String> skuMap = new HashMap<String,String>();
    	String data = "";
    	final String skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace", null,
				new OutTimeConfig(1000*60*10,1000*60*60,1000*60*60),user,password);
    	int kk=0;
    	
    	String date2 = "";
    	while(StringUtils.isBlank(skuData) && kk<100){
    		Thread.sleep(1000*5); 
    		date2 = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace", null,
    				new OutTimeConfig(1000*60*10,1000*60*60,1000*60*60),user,password);
    		kk++;
    	}
    	
    	logger.info("kk=========================="+kk); 
    	final String skuData2 = date2;
    	
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
			}
		}
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
                    try {
                        returnMap.put(skuId, Integer.valueOf(stock));
                    } catch (NumberFormatException e) {
                        returnMap.put(skuId, 0);
                    }
                }else{
					returnMap.put(skuId, 0);
				}
			}
        }
        
        try {
        	Thread t = new Thread(new Runnable() {    			
    			@Override
    			public void run() {
    				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    				String dateStr = format.format(new Date());
    				String fileName = dateStr + ".txt";
    				BufferedWriter out = null;
    				BufferedReader in = null;
    				try {
    					String data = "";
    					if(StringUtils.isNotBlank(skuData)){
    						data = skuData;
    					}else{
    						data = skuData2;
    					}
    					out =new BufferedWriter(new FileWriter(saveLocal+File.separator+fileName));
    					in =new BufferedReader(new StringReader(data));
    					String temp = "";
    					while(StringUtils.isNotBlank(temp=in.readLine())){
    						out.append(temp).append("\n"); 
    					}
    					out.flush();
    					logger.info(fileName+"保存本地成功!!!!!!!!!!!!!!!!!");
    				} catch (Exception e) {
    					logError.error(e);
    					logError.error(fileName+"保存到本地失败"); 
    				}finally{
    					try {
    						if(null != out){
    							out.close();
    						}
    						if(null != in){
    							in.close();
    						}
    					} catch (Exception e2) {
    						e2.printStackTrace();
    					}
    				}
    			}
    		});
        	t.start(); 
		} catch (Exception e) {
			logError.error(e);
		}
        
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        DanielloStockImp stockImp =(DanielloStockImp)factory.getBean("daniellostock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("daniello更新数据库开始");
        try {
            String host = bdl.getString("HOST");
            String app_key = bdl.getString("APP_KEY");
            String app_secret= bdl.getString("APP_SECRET");
            String startDate = bdl.getString("startDate");
			stockImp.updateProductStock(host,app_key,app_secret,supplierId, startDate, format.format(new Date()));
		} catch (Exception e) {
			logger.info("daniello更新库存数据库出错"+e.toString());
		}
        logger.info("daniello更新数据库结束");
        System.exit(0);
    }
}
