package com.shangpin.iog.grouppo.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.grouppo.schedule.AppContext;
import com.shangpin.iog.grouppo.util.StockWSServiceStub;
import com.shangpin.iog.grouppo.util.StockWSServiceStub.Stock_TabularQuery;
import com.shangpin.iog.grouppo.util.StockWSServiceStub.Stock_TabularQueryResponse;
import com.shangpin.iog.grouppo.util.StockWSServiceStub.Stock_TabularQueryResponseStructure;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

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
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        	StockWSServiceStub stockWSServiceStub = new StockWSServiceStub();
        	stockWSServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(1000*60*60); 
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(1000*60*60));
        	stockWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(1000*60*60));
        	
        	Stock_TabularQuery stock_TabularQuery2 = new StockWSServiceStub.Stock_TabularQuery();
        	stock_TabularQuery2.setM_UserName("shangpin");
        	stock_TabularQuery2.setM_Password("getDataWs16");
        	stock_TabularQuery2.setM_Company("PRITE");
        	stock_TabularQuery2.setSkuId(""); 
        	logger.info("=============开始获取response=============="); 
        	Stock_TabularQueryResponse response = stockWSServiceStub.stock_TabularQuery(stock_TabularQuery2);
        	Stock_TabularQueryResponseStructure[] items = response.getRecords().getItem();
        	logger.info("===========抓取的items.length========="+items.length); 
        	for(Stock_TabularQueryResponseStructure item : items){
        		try {
        			stockMap.put(item.getSkuId(),""+item.getStock());
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        	}
        
        }catch(Exception ex){
        	logError.error(ex);
        	ex.printStackTrace(); 
        }
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        logger.info("==========返回的map大小========="+skustock.size()); 
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();     
    	
    }
}
