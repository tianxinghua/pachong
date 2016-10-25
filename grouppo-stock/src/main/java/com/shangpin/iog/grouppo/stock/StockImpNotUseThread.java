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
@Component("StockImpNotUseThread")
public class StockImpNotUseThread  extends AbsUpdateProductStock {
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
    	Map<String,String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		Map<String,Double> zeroStock = new HashMap<String, Double>();
        
        try{
        	//业务实现
        	logger.info("=================开始拉取供应商库存信息==================");
        	StockWSServiceStub stockWSServiceStub = new StockWSServiceStub();
        	stockWSServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(1000*60*60); 
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(1000*60*60));
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(1000*60*60));
        	
        	Stock_TabularQuery stock_TabularQuery2 = new StockWSServiceStub.Stock_TabularQuery();
        	stock_TabularQuery2.setM_UserName("shangpin");
        	stock_TabularQuery2.setM_Password("getDataWs16");
        	stock_TabularQuery2.setM_Company("PRITE");
        	stock_TabularQuery2.setSkuId(""); 
        	Stock_TabularQueryResponse response = null; 	
    		try {    			           	
        		response = stockWSServiceStub.stock_TabularQuery(stock_TabularQuery2);
			} catch (Exception e) {
				logger.info("第1次异常===="+e.toString());
				logError.error("第1次异常===="+e);
				try {
					response = stockWSServiceStub.stock_TabularQuery(stock_TabularQuery2);	
				} catch (Exception e2) {
					logger.info("第2次异常===="+e2.toString());
					logError.error("第2次异常===="+e2);
					try {
						response = stockWSServiceStub.stock_TabularQuery(stock_TabularQuery2);	
					} catch (Exception e3) {
						logger.info("第3次异常===="+e3.toString());
						logError.error("第3次异常===="+e3);
						throw new Exception("========拉取供应商库存信息失败==========="+e.toString());
					}
				}
			}
    		Stock_TabularQueryResponseStructure[] items = response.getRecords().getItem();
        	for(Stock_TabularQueryResponseStructure item : items){
        		try {
    				if(item.getStock() >0){
    					stockMap.put(item.getSkuId().trim(),""+(int)item.getStock());        					
    				}else{
    					zeroStock.put(item.getSkuId().trim(), item.getStock());
    				}                  			
				} catch (Exception e) {
					e.printStackTrace();             					
				}            		
        	}
        }catch(Exception ex){
        	logError.error(ex);
        	ex.printStackTrace(); 
        	return skustock;
        }
        logger.info("zeroStock.size==="+zeroStock.size()); 
        logger.info("stockMap.size==="+stockMap.size()); 
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        return skustock;
    }

    //test main
    public static void main(String[] args) throws Exception {
    	//加载spring
//        loadSpringContext();    

    	StockImpNotUseThread stockImp = new StockImpNotUseThread();
    	 stockImp.grabStock(null);
    	
    }
}
