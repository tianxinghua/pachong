package com.shangpin.iog.${supplierName}.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.sop.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.${supplierName}.schedule.AppContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import com.shangpin.iog.${supplierName}.dto.Item;
import com.shangpin.iog.${supplierName}.util.CSVUtil;
import com.shangpin.iog.${supplierName}.util.DownLoad;

import java.io.File;

/**
 * Created by ${createdby} on 2015/9/14.
 */
@Component("${supplierName}stock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    
    private static ResourceBundle bdl=null;
    private static String supplierId;
    
    private static String csvurl = null;
	private static String csvfilepath = null;
	private static char csvsep;
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("${profileName}");
        supplierId = bdl.getString("supplierId");
        csvurl = bdl.getString("csvurl");
		csvfilepath = bdl.getString("csvfilepath");
		csvsep = (char)bdl.getObject("csvsep");
    }
    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, Integer> skustock = new HashMap<String, Integer>();
		Map<String,Integer> stockMap = new HashMap<String, Integer>();
        
        try{
        	//业务实现
        	DownLoad.downFromNet(csvurl, csvfilepath);
			File file = new File(csvfilepath);
			List<Item> items = CSVUtil.readCSV(file, Item.class, csvsep);
			logger.info("拉取的item数量========"+items.size()); 
			for(Item item : items){
				try {
					
					stockMap.put(item.getSkuId(), Integer.parseInt(item.getStock()));
					
				} catch (Exception e) {
					logError.error(e); 
				}
			}        
        
        }catch(Exception ex){
        	logError.error(ex);
        	return skustock;
        }
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, 0);
            }
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();       
    }
}
