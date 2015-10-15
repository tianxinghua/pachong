package com.shangpin.iog.reebonz.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.service.EventProductService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("reebonz")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String stockUrl;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        stockUrl = bdl.getString("stockUrl");
    }
   
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
        //定义三方
    	Map<String,String> stockMap = new HashMap<>();
        for (String skuno : skuNo) {
            stockMap.put(skuno,getInventory(skuno));
        }
        return stockMap;
    }

    private  String getInventory(String skuIds){
    	
    	String []array = skuIds.split("\\|");
    	String skuNo = array[0];
    	String size = array[1];
    	if("A".equals(size)){
    		size = "";
    	}
    	//根据sku从数据库EVENT_PRODUCT查询最新的eventId
    	String eventId=null;
		try {
			if(eventProductService==null){
				eventProductService = new EventProductServiceImpl();
			}
			eventId = eventProductService.selectEventIdBySku(skuNo,supplierId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	Map<String,String> map = new HashMap<String,String>();
    	String jsonStr = null;
    	if(eventId!=null){
    		map.put("event_id", eventId);
    		map.put("sku", skuNo);
    		jsonStr =  HttpUtil45
    				.get(stockUrl,
    						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
    						map);;
            logger.info("get skuId :"+skuNo +" 库存返回值为："+jsonStr );
           
    	}
    	 if(HttpUtil45.errorResult.equals(jsonStr)){    //链接异常
             return  "0";
         }else{
        	 return getInventoryByJsonString(jsonStr,size);
         }
    }
    private static String getInventoryByJsonString(String jsonStr,String size){
    	ResponseObject obj=null;
        String returnSize = "0";
        if(jsonStr!=null){
        	  try {
              	obj = new Gson().fromJson(jsonStr, ResponseObject.class);
      			Object o = obj.getResponse();
      			JSONObject jsonObject = JSONObject.fromObject(o); 
      			List<Item> array = new Gson().fromJson(jsonObject.toString(), Items.class).getDocs();
      			if(array.size()>0){
      				for(Item item:array){
      					if(size.equals(item.getOption_code())){
      						returnSize = item.getTotal_stock_qty();
      					}
      				}
      			}
              } catch (Exception e) {
                  loggerError.error("转化 :"+jsonStr +" 到库存对象失败 :" +e.getMessage());
                  e.printStackTrace(); 
              }
        }
		return returnSize;
    }
    
    public static void main(String[] args) throws Exception {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("reebonz");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("reebonz更新库存开始");
        stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("reebonz更新库存结束");
        System.exit(0);
    }
}
