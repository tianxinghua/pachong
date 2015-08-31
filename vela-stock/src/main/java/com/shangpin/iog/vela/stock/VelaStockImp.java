package com.shangpin.iog.vela.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.vela.stock.dto.Price;
import com.shangpin.iog.vela.stock.dto.Quantity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/7/17.
 */
@Component("vela")
public class VelaStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;





    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    private Map<String,String> barcode_map = new HashMap<>();

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {

        Map<String,String> supplierPirceMap = skuPriceService.getSkuPriceMap(supplierId);
        Map<String, String> stock_map = new HashMap<>();
        Gson gson = new Gson();
        int i=0;
        Map<String,String> mongMap = new HashMap<>();
        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","vela");
        StringBuffer buffer = new StringBuffer();
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        String url="",priceUrl="",priceJson="",itemId="";
        for (String skuno : skuNo) {
//            if (barcode_map.containsKey(skuno)) {
//                continue;
//            } else {
//                barcode_map.put(skuno, null);
//            }

             itemId = skuno;
            //根据供应商skuno获取库存，并更新我方sop库存
            url = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetQuantityByItemID?DBContext=Default&ItemID=[[itemId]]&key=MPm32XJp7M";
            url = url.replaceAll("\\[\\[itemId\\]\\]", itemId);
            priceUrl = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetPriceByItemID?DBContext=Default&ItemID="+itemId+"&key=MPm32XJp7M";

            String json = null;
            try {
                json = HttpUtil45.get(url, outTimeConfig, null);
                buffer.append(json).append("|||");

            } catch (Exception e) {
                loggerError.error("拉取数据失败---" + e.getMessage());
                e.printStackTrace();
            }
            if (json != null && !json.isEmpty()) {
                if(json.equals("{\"error\":\"发生异常错误\"}")){
                    //重复调用5次
                     while(i<5){
                         json = HttpUtil45.get(url, outTimeConfig, null);
                         if(json.equals("{\"error\":\"发生异常错误\"}")){
                             i++;
                         }else{
                             i=0;
                             break;
                         }

                     }
                    if(json.equals("{\"error\":\"发生异常错误\"}")){
                        stock_map.put(skuno, "0|");
                        i=0;
                        continue;
                    }
                }
                try {
                    Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
                    }.getType());


                    if("No Record Found".equals(result.getResult())){


                    }else{
                        stock_map.put(skuno, result.getResult());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取价格
            priceUrl = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetPriceByItemID?DBContext=Default&ItemID="+itemID+"&key=MPm32XJp7M";
            try {
                priceJson = HttpUtil45.get(priceUrl, new OutTimeConfig(), null);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            if(priceJson != null && !priceJson.isEmpty()){
                Price price = null;
                price = gson.fromJson(json,new TypeToken<Price>() {}.getType());
                if(price!=null&&price.getMarket_price()!=null||price.getSuply_price()!=null){

                }
            }

        }
        mongMap.put("result",buffer.toString());
        logMongo.info(mongMap);
        return stock_map;
    }


    public static void main(String[] args) throws Exception {
        AbsUpdateProductStock velaStockImp = new VelaStockImp();
        velaStockImp.setUseThread(true);velaStockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("VELA更新数据库开始");
        velaStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("VELA更新数据库结束");
        /*VelaStockImp velaStockImp = new VelaStockImp();
        Collection<String> sku = new HashSet<>();
        sku.add("75901");
        Map<String,Integer> stock = new HashMap<>();
        stock = velaStockImp.grabStock(sku);
        System.out.println(stock.get("75901"));*/
        System.exit(0);
    }

}
