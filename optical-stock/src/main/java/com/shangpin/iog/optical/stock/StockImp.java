package com.shangpin.iog.optical.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.optical.dto.Item;
import com.shangpin.iog.optical.schedule.AppContext;
import com.shangpin.iog.optical.util.ReadExcel;
import com.shangpin.iog.optical.util.Utils;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
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
//    @Override
//    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
//    	
//    	//获取库存元数据
//    	Map<String, Integer> skustock = new HashMap<String, Integer>();
//		Map<String,String> stockMap = new HashMap<String, String>();
//        
//        try{
//        	//业务实现
//        	ReadExcel.downLoadFile(url, filepath);
//			List<Item> items = ReadExcel.readExcel(Item.class, filepath);
//			logger.info("items.size===================="+items.size());
//			for(Item item : items){
//				try {
//					logger.info(item.getSkuNo()+"----------"+item.getStock()); 
//					stockMap.put(item.getSkuNo(), item.getStock());
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} 
//			
//			logger.info("供货商的stockMap.size============="+stockMap.size()); 
//        
//        }catch(Exception ex){
//        	ex.printStackTrace();
//        	logError.error(ex);
//        	return skustock;
//        }
//        
//        for (String skuno : skuNo) {
//        	logger.info(skuno+"++++++++++++++++++++");
//            if(stockMap.containsKey(skuno)){
//                skustock.put(skuno, Integer.parseInt(stockMap.get(skuno)));
//            } else{
//                skustock.put(skuno, 0);
//            }
//        }
//        logger.info("返回的map大小==============="+skustock.size()); 
//        return skustock;
//    }
public String getValue(String value){
		
		if(StringUtils.isNotBlank(value)){
			return value;
		}else{
			return null;
		}
		
		
	}
	  private static void readLine(String content){
	    	File file = new File(filepath);
	    	FileWriter fwriter = null;
	    	   try {
	    	    fwriter = new FileWriter(file);
	    	    fwriter.write(content);
	    	   } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	   } finally {
	    	    try {
	    	     fwriter.flush();
	    	     fwriter.close();
	    	    } catch (Exception ex) {
	    	     ex.printStackTrace();
	    	    }
	    	   }
	    }
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, Integer> skustock = new HashMap<String, Integer>();
		Map<String,Integer> stockMap = new HashMap<String, Integer>();
        
        try{
        	//业务实现
        	String xml = HttpUtil45.get(url, new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20), null);
        	readLine(xml);
        	List<List<String>> list = Utils.getListProduct(filepath);
			logger.info("items.size===================="+list.size());
			for(List<String> item : list){
				logger.info(getValue(item.get(4))+"----------"+getValue(item.get(15))); 
				stockMap.put(getValue(item.get(4)), Integer.parseInt(getValue(item.get(15))));
			
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
                skustock.put(skuno, 0);
            }
        }
        logger.info("返回的map大小==============="+skustock.size()); 
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();   
    	//new StockImp().grabStock(null);
    }
}
