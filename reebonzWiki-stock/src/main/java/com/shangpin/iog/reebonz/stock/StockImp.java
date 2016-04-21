package com.shangpin.iog.reebonz.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
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

import java.text.ParseException;
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
    	
    	try{
    		String []array = skuIds.split("\\|");
         	String skuNo = array[0];
         	String size = null;
        	if(array.length==2){
        		size = array[1];
        	}else{
        		logger.info("sku为："+skuNo+"的商品尺码未获取到，更新库存失败，库存已置为0");
        		size ="B";
        	}
        	if("A".equals(size)){
        		size = "";
        	}
        	//根据sku从数据库EVENT_PRODUCT查询最新的eventId
        	EventProductDTO event=null;
    		try {
    			event = eventProductService.selectEventProductDTOBySku(skuNo,supplierId);
    			
    		} catch (ServiceException e) {
    			e.printStackTrace();
    		}
        	Map<String,String> map = new HashMap<String,String>();
        	String jsonStr = null;
        	SimpleDateFormat sf = new SimpleDateFormat(
    				"yyyy-MM-dd HH:mm:ss");
    		Date endDate = sf.parse(getString(event.getEndDate()));
    		boolean before = endDate.before(new Date());
    		if (before) {
    			// 旧活动已结束
    			return "0";
    		}
//        	if()
        	if(event!=null){
        		map.put("event_id", event.getEventId());
        		map.put("sku", skuNo);
        		jsonStr =  HttpUtil45
        				.get(stockUrl,
        						new OutTimeConfig(1000 * 60*2, 1000 * 60*2, 1000 * 60*2),
        						map);
                System.out.println("get skuId :"+skuNo +" 库存返回值为："+jsonStr );
               
        	}
        	 if(HttpUtil45.errorResult.equals(jsonStr)){    //链接异常
        		 return "0";
             }else{
            	 return getInventoryByJsonString(jsonStr,size);
             }
    	}catch(Exception e){
    		return "0";
    	}
    	
    }
    private static String getInventoryByJsonString(String jsonStr,String size){
    	 String returnSize = "0";
    	try{
    		ResponseObject obj=null;
         
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
          						break;
          					}
          				}
          			}
                  } catch (Exception e) {
                      loggerError.error("转化 :"+jsonStr +" 到库存对象失败 :" +e.getMessage());
                      e.printStackTrace(); 
                  }
            }
    	}catch(Exception e){
    	}
    	return returnSize;
    }
    
    public static void main(String[] args) {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("reebonz");
        stockImp.setUseThread(true);stockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("reebonz更新库存开始");
        System.out.println("reebonz更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("reebonz更新库存结束");
        System.out.println("reebonz更新库存结束");
        System.exit(0);
    }
    private static String getString(String ts) throws ParseException {
		ts = ts.replace("Z", " UTC");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date dt = sdf.parse(ts);
		TimeZone tz = sdf.getTimeZone();
		Calendar c = sdf.getCalendar();
		StringBuffer result = new StringBuffer();
		result.append(c.get(Calendar.YEAR));
		result.append("-");
		result.append((c.get(Calendar.MONTH) + 1));
		result.append("-");
		result.append(c.get(Calendar.DAY_OF_MONTH));
		result.append(" ");
		result.append(c.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(c.get(Calendar.MINUTE));
		result.append(":");
		result.append(c.get(Calendar.SECOND));
		return result.toString();
	}
}
