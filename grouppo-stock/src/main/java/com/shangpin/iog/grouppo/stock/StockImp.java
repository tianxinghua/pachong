package com.shangpin.iog.grouppo.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.grouppo.schedule.AppContext;
import com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub;
import com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub.Stock_TabularQuery;
import com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub.Stock_TabularQueryResponse;
import com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub.Stock_TabularQueryResponseStructure;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("grouppostock")
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
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据    	
		Map<String,String> stockMap = new HashMap<String, String>();
//		Map<String,Double> zeroStock = new HashMap<String, Double>();
        
        try{
        	//业务实现
        	StockWSServiceStub stockWSServiceStub = new StockWSServiceStub();
        	stockWSServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(1000*60); 
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(1000*60*60));
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(1000*60*60));
        	
        	Stock_TabularQuery stock_TabularQuery2 = new StockWSServiceStub.Stock_TabularQuery();
        	stock_TabularQuery2.setM_UserName("shangpin");
        	stock_TabularQuery2.setM_Password("getDataWs16");
        	stock_TabularQuery2.setM_Company("PRITE");
        	
        	for(String skuId : skuNo){        		
        		try {
	        		stock_TabularQuery2.setSkuId(skuId); 
	        		
	            	Stock_TabularQueryResponse response = null;            	
            		response = stockWSServiceStub.stock_TabularQuery(stock_TabularQuery2);            		
            		Stock_TabularQueryResponseStructure[] items = response.getRecords().getItem();
                	for(Stock_TabularQueryResponseStructure item : items){
                		try {
            				stockMap.put(skuId,""+(int)item.getStock());
            				logger.info(skuId+"================"+(int)item.getStock());                     			
        				} catch (Exception e) {
        					stockMap.put(skuId,"0");            					
        				}            		
                	}
                
    			} catch (Exception e) {
    				logError.error("第1次异常===="+e);
    				stockMap.put(skuId,"0");
    			}
        	}    	
        
        }catch(Exception ex){
        	logError.error(ex);
        	ex.printStackTrace(); 
        }
      
        logger.info("==========返回的map大小========="+stockMap.size()); 
        return stockMap;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();    

//    	StockImp stockImp = new StockImp();
//    	 stockImp.grabStock(null);
    	
    }
}
