package com.shangpin.iog.biondini.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.biondini.util.SoapUtil;
import com.shangpin.iog.service.EventProductService;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("biondini")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
   
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
    	Map<String,String> qtyMap = SoapUtil.getProductStockList();
    	Map<String,String> stockMap = new HashMap<String,String>();
        for (String skuno : skuNo) {
        	if(qtyMap.containsKey(skuno)){
        		stockMap.put(skuno,qtyMap.get(skuno));
        	}else{
        		stockMap.put(skuno,"0");
        	}
        }
        return stockMap;
    }

    public static void main(String[] args) {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("biondini");
        
        try {
			stockImp.grabStock(null);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//        stockImp.setUseThread(true);stockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("inviqa更新库存开始");
        System.out.println("inviqa更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        logger.info("inviqa更新库存结束");
        System.out.println("inviqa更新库存结束");
        System.exit(0);
    }
}
