package com.shangpin.iog.${supplierName}.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.${supplierName}.schedule.AppContext;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ${createdby} on 2015/9/14.
 */
@Component("${supplierName}stock")
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
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("${profileName}");
        supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        
        
        
        }catch(Exception ex){
        	logError.error(ex);
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
    }
}
