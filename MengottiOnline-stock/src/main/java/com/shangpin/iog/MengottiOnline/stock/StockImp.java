package com.shangpin.iog.MengottiOnline.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.MengottiOnline.dto.Item;
import com.shangpin.iog.MengottiOnline.util.CVSUtil;
import com.shangpin.iog.MengottiOnline.util.DownLoad;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2016/9/14.
 */
@Component("MengottiOnlinestock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger error = Logger.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static String local = null;
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
		local = bdl.getString("local");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> stockMap = new HashMap<String,String>();
        Map<String,String> skustock = new HashMap<String,String>();
        List<Item> items = null;
        try{
        	DownLoad.downFromNet(url, local);
    		File file = new File(local);
    		items = CVSUtil.readCSV(file, Item.class, ';');
        }catch(Exception e){
        	e.printStackTrace();
        	error.error(e);
        	return skustock;
        }
        
		if (null != items && items.size() > 0) {
			for (Item item : items) {
				try {					
					stockMap.put(item.getSupplierSkuNo(),item.getStock());					
				} catch (Exception e) {
					e.printStackTrace();
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
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockImp stockImp =(StockImp)factory.getBean("MengottiOnlinestock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("MengottiOnline更新数据库开始");
        try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("MengottiOnline更新库存数据库出错"+e.toString());
		}
        logger.info("MengottiOnline更新数据库结束");
        System.exit(0);
    }
}
