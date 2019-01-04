package com.shangpin.iog.optical.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.optical.dto.Item;
import com.shangpin.iog.optical.schedule.AppContext;
import com.shangpin.iog.optical.util.ReadExcel;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by usr on 2015/9/14.
 */
@Component("opticalstock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    
    private static String url = null;
	private static String filepath = null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("excel.url");
		filepath = bdl.getString("excel.filepath");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        	ReadExcel.downLoadFile(url, filepath);
			List<Item> items = ReadExcel.readExcel(Item.class, filepath);
			logger.info("items.size===================="+items.size());
			for(Item item : items){
				try {
					logger.info(item.getSkuNo()+"----------"+item.getStock()); 
					stockMap.put(item.getSkuNo(), item.getStock());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			
			logger.info("供货商的stockMap.size============="+stockMap.size()); 
        
        }catch(Exception ex){
        	ex.printStackTrace();
        	logError.error(ex);
        	return skustock;
        }
        
        for (String skuno : skuNo) {
        	logger.info(skuno+"++++++++++++++++++++");
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        logger.info("返回的map大小==============="+skustock.size()); 
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();       
    }
}
