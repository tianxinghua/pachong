package com.shangpin.iog.spinnaker.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.spinnaker.stock.dto.Quantity;
import com.shangpin.iog.spinnaker.stock.schedule.AppContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("spinnaker")
public class SpinnakerStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

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
        Map<String, String> stock_map = new HashMap<String, String>();
        Gson gson = new Gson();
        String barcode="" ,url="",json="";
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        for (String skuno : skuNo) {
//            if (barcode_map.containsKey(skuno)) {
//                continue;
//            } else {
//                barcode_map.put(skuno, null);
//            }

             barcode = skuno.trim();
            //根据供应商skuno获取库存，并更新我方sop库存
             url = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=Default&barcode=[[barcode]]&key=8IZk2x5tVN";
            url = url.replaceAll("\\[\\[barcode\\]\\]", barcode);
            logger.info("url===="+url); 
             json = null;
            try {
                json = HttpUtil45.get(url, outTimeConfig, null);
//                logger.info("json====="+json); 
            } catch (Exception e) {
                stock_map.put(skuno, "0");  //读取失败的时候赋值为0
//                loggerError.error("拉取失败 "+e.getMessage());
                e.printStackTrace();
                continue;
            }
            if (json != null && !json.isEmpty()) {

                if(json.equals("{\"Result\":\"No Record Found\"}")) {    //未找到

                            stock_map.put(skuno, "0");


                }else{//找到赋值

                    try {
                        Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
                        }.getType());
                        stock_map.put(skuno, result.getResult());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }

        logger.info("返回的map大小  stock_map.size======"+stock_map.size()); 
        return stock_map;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        //拉取数据
//        SpinnakerStockImp stockImp =(SpinnakerStockImp)factory.getBean("spinnaker");
//        
////        AbsUpdateProductStock grabStockImp = new SpinnakerStockImp();
//        stockImp.setUseThread(true);stockImp.setSkuCount4Thread(500);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("SPINNAKER更新数据库开始");
//        try {
//            stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//        } catch (Exception e) {
//            loggerError.error("spinnaker更新库存失败."+e.getMessage());
//            e.printStackTrace();
//        }
//        logger.info("SPINNAKER更新数据库结束");
//        System.exit(0);

    }

}
