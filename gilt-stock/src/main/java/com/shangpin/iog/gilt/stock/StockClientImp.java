package com.shangpin.iog.gilt.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import com.shangpin.iog.gilt.dto.InventoryDTO;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunny on 2015/8/10.
 */
@Component("giltStock")
public class StockClientImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;

    private static String url ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
        url = bdl.getString("url") + "/global/realtime-inventory/";
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String,String> stockMap = new HashMap<>();
        try{
            logger.info("拉取gilt数据库存开始");

            for (String skuno : skuNo) {
                stockMap.put(skuno,this.getInventory(skuno));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return stockMap;
    }


    private static String getInventory(String skuId){

        OutTimeConfig outTimeConf = new OutTimeConfig(1000*30,1000*30,1000*30);
        String jsonStr = HttpUtil45.get(url+skuId,outTimeConf,null,key,"");
        logger.info("get skuId :"+skuId +" 库存返回值为："+jsonStr );
        if(HttpUtil45.errorResult.equals(jsonStr)){    //链接异常
            return  "0";
        }
        return getInventoryByJsonString(jsonStr);


    }


    private static String getInventoryByJsonString(String jsonStr){
        InventoryDTO obj=null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, InventoryDTO.class);
        } catch (Exception e) {
            loggerError.error("转化 :"+jsonStr +" 到库存对象失败 :" +e.getMessage());
            e.printStackTrace();
            return "0";
        }
        return obj.getQuantity();
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private static List<InventoryDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<InventoryDTO> objs = new ArrayList<InventoryDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<InventoryDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }
    private static GiltSkuDTO getObject(String jsonStr){
        Gson gson = new Gson();
        GiltSkuDTO obj = null;
        try{
            obj  = gson.fromJson(jsonStr,GiltSkuDTO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  obj;
    }
    private static List<GiltSkuDTO> getObjects(String jsonStr){
        Gson gson = new Gson();
        List<GiltSkuDTO> objs = new ArrayList<GiltSkuDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<GiltSkuDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("get List<ApennineProductDTO> fail :"+e);
        }
        return objs;
    }
    
    
    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockClientImp giltStockImp = (StockClientImp)factory.getBean("giltStock");
        //AbsUpdateProductStock giltStockImp = new StockClientImp();
        giltStockImp.setUseThread(true);giltStockImp.setSkuCount4Thread(100);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("gilt更新库存开始");
        try {
            giltStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        } catch (Exception e) {
        	loggerError.equals("gilt更新库存失败"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("gilt更新库存结束");
        System.exit(0);
    }
}
