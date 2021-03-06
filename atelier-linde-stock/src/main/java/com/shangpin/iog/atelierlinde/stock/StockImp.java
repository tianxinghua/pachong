package com.shangpin.iog.atelierlinde.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

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
@Component("atelierlindestock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	Map<String,String> skuMap = new HashMap<String,String>();
    	String data = "";
    	OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600);
    	String skuData = HttpUtil45.postNTAuth(url+"GetAllAvailabilityMarketplace", null, outTimeConfig, "Shangpin", "Shang$linde01");
		String[] skuStrings = skuData.split("\\r\\n");
		logger.info("待更新库存+++"+skuNo.size()+"读取库存+++"+skuStrings.length);
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
        Map<String,String> returnMap = new HashMap<String,String>();
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        logger.info("循环赋值");
        String skuId = "";
        String stock = "0";
        int num = 0;
        while (iterator.hasNext()){
        	skuId = iterator.next();
        	if (StringUtils.isNotBlank(skuId)) {
        		if (skuMap.containsKey(skuId)) {
        			stock = skuMap.get(skuId);
        			if (stock.contains("-")) {
        				stock="0";
					}
        			returnMap.put(skuId, stock);
        			num++;
				}else{
					returnMap.put(skuId, "0");
				}
			}
        }
        logger.info("元数据包含的有"+num);
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockImp stockImp =(StockImp)factory.getBean("atelierlindestock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("更新数据库开始");
        try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("russoCapri ATELIER更新库存数据库出错"+e.toString());
		}
        logger.info("russoCapri ATELIER更新数据库结束");
        System.exit(0);
    }
}
