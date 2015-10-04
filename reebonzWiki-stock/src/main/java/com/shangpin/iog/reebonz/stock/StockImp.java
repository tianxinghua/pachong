package com.shangpin.iog.reebonz.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    private static ResourceBundle bdl=null;
    private static String supplierId;

//    static {
//        if(null==bdl)
//            bdl=ResourceBundle.getBundle("conf");
//        supplierId = bdl.getString("supplierId");
//    }
    public static void main(String[] args) throws Exception {
    	System.out.println(getInventory("A62053E003193906|250|no-size"));;
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
        //定义三方
    	 Map<String,String> stockMap = new HashMap<>();
    	 stockMap.put("supplierId",supplierId);
        for (String skuno : skuNo) {
            stockMap.put(skuno,getInventory(skuno));
        }
        return stockMap;
    }

    private static String getInventory(String skuIds){
    	
    	String []array = skuIds.split("\\|");
    	String skuNo = array[0];
    	String eventId = array[1];
    	String size = null;
    	if(array.length==3){
    		size = array[2];
    	}
    	Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("sku", skuNo);
    	String jsonStr =  HttpUtil45
				.get("http://hps.sit.titan.reebonz-dev.com/api/shangpin/product_qty",
						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
						map);;
        logger.info("get skuId :"+skuNo +" 库存返回值为："+jsonStr );
        if(HttpUtil45.errorResult.equals(jsonStr)){    //链接异常
            return  "0";
        }
        return getInventoryByJsonString(jsonStr,size);
    }

    private static String getInventoryByJsonString(String jsonStr,String size){
    	ResponseObject obj=null;
        Gson gson = new Gson();
        String returnSize = "0";
        try {
        	obj = new Gson().fromJson(jsonStr, ResponseObject.class);
			Object o = obj.getResponse();
			JSONObject jsonObject = JSONObject.fromObject(o); 
			List<Item> array = new Gson().fromJson(jsonObject.toString(), Items.class).getDocs();
			if(array.size()>0){
				for(Item item:array){
					if(item.getOption_name().equals(size)){
						returnSize = item.getTotal_stock_qty();
					}
				}
			}
		
        } catch (Exception e) {
            loggerError.error("转化 :"+jsonStr +" 到库存对象失败 :" +e.getMessage());
           
            e.printStackTrace(); return "0";
        }
		return returnSize;
    }

}
