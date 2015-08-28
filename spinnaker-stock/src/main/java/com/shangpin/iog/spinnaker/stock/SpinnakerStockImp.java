package com.shangpin.iog.spinnaker.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.spinnaker.stock.dto.Quantity;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SpinnakerStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    private Map<String,String> barcode_map = new HashMap<>();

    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, Integer> stock_map = new HashMap<String, Integer>();
        Gson gson = new Gson();
        String barcode="" ,url="",json="";
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        for (String skuno : skuNo) {
//            if (barcode_map.containsKey(skuno)) {
//                continue;
//            } else {
//                barcode_map.put(skuno, null);
//            }

             barcode = skuno;
            //根据供应商skuno获取库存，并更新我方sop库存
             url = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=Default&barcode=[[barcode]]&key=8IZk2x5tVN";
            url = url.replaceAll("\\[\\[barcode\\]\\]", barcode);
             json = null;
            try {
                json = HttpUtil45.get(url, outTimeConfig, null);
            } catch (Exception e) {
                stock_map.put(skuno, 0);  //读取失败的时候赋值为0
                loggerError.error("拉取失败 "+e.getMessage());
                e.printStackTrace();
                continue;
            }
            if (json != null && !json.isEmpty()) {

                if(json.equals("{\"Result\":\"No Record Found\"}")) {    //未找到

                            stock_map.put(skuno, 0);


                }else{//找到赋值

                    try {
                        Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
                        }.getType());
                        stock_map.put(skuno, Integer.valueOf(result.getResult()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }

        return stock_map;
    }

    public static void main(String[] args) throws Exception {
        AbsUpdateProductStock grabStockImp = new SpinnakerStockImp();
        grabStockImp.setUseThread(true);grabStockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("SPINNAKER更新数据库开始");
        grabStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("SPINNAKER更新数据库结束");
        System.exit(0);

    }

}
