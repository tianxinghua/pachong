package com.shangpin.iog.sanremo.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.sanremo.stock.dto.Quantity;
import com.shangpin.iog.sanremo.stock.schedule.AppContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("sanremoStock")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    private Map<String,String> barcode_map = new HashMap<>();

    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }


    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> stock_map = new HashMap<String, String>();
        Gson gson = new Gson();
        int i=0;
        Map<String,String> mongMap = new HashMap<>();
        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","sanremo");
        StringBuffer buffer = new StringBuffer();
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        String url ="",itemId="";
        for (String skuno : skuNo) {
//            if (barcode_map.containsKey(skuno)) {
//                continue;
//            } else {
//                barcode_map.put(skuno, null);
//            }

             itemId = skuno.trim();
            //根据供应商skuno获取库存，并更新我方sop库存


            if(itemId.length()<10){//item id
                url = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetQuantityByItemID?DBContext=sanremo&ItemID=[[itemId]]&key=8IZk2x5tVN";
            }else{   //baocode
                url = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=sanremo&barcode=[[itemId]]&key=8IZk2x5tVN";
            }

            url = url.replaceAll("\\[\\[itemId\\]\\]", itemId);
            String json = null;
            try {
                json = HttpUtil45.get(url, outTimeConfig, null);
                buffer.append(json).append("|||");

            } catch (Exception e) {
//                loggerError.error("拉取数据失败---" + e.getMessage());
                e.printStackTrace();
            }
            if (json != null && !json.isEmpty()) {
                if(json.equals("{\"error\":\"发生异常错误\"}")){
                    //重复调用5次
                    while(i<5){
                        json = HttpUtil45.get(url,outTimeConfig, null);
                        if(json.equals("{\"error\":\"发生异常错误\"}")){
                            i++;
                        }else{
                            i=0;
                            break;
                        }

                    }
                    if(json.equals("{\"error\":\"发生异常错误\"}")){
                        stock_map.put(skuno, "0");
                        i=0;
                        continue;
                    }
                }
                try {
                    Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
                    }.getType());
                    if("No Record Found".equals(result.getResult())){

                        stock_map.put(skuno, "0");
                    }else{
                        stock_map.put(skuno,result.getResult());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mongMap.put("result",buffer.toString());
        logMongo.info(mongMap);
        return stock_map;
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        loadSpringContext();
        //拉取数据
//        StockImp grabStockImp =(StockImp)factory.getBean("sanremoStock");
////        AbsUpdateProductStock grabStockImp = new StockImp();
//        grabStockImp.setUseThread(true);grabStockImp.setSkuCount4Thread(500);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("sanremo更新数据库开始");
//        try {
//            grabStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//        } catch (Exception e) {
//            loggerError.error("sanremo库存更新失败");
//            e.printStackTrace();
//        }
//        logger.info("sanremo更新数据库结束");
//        System.exit(0);

    }

}
