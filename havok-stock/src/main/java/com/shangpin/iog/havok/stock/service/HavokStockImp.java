package com.shangpin.iog.havok.stock.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.havok.stock.dto.Product;
import com.shangpin.iog.havok.stock.dto.Products;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by huxia on 2015/10/28.
 */
public class HavokStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {

        Map<String, String> stockMap = new HashMap<>();
        Map<String, String> sku_Map = new HashMap<>();

        String skuJson = null;
        String skuUrl = "http://webserv.havok.it/stock/v1/product.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin";
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        Gson gson = new Gson();
        try{
            skuJson = HttpUtil45.get(skuUrl,outTimeConfig,null);
        }catch (Exception e){
            e.printStackTrace();
        }

        String stock = "";String skuid = "";
        if (skuJson != null && !skuJson.isEmpty()) {
            Products skuList = null;
            try {
                skuList = gson.fromJson(skuJson, new TypeToken<Product>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (skuList != null && skuList.getSkus() != null) {
                for (Product sku : skuList.getSkus()) {
                    skuid = sku.getSKUID();
                    stock = sku.getStock();
                    sku_Map.put(skuid,stock);
                }
            }
        }

        for(String skuno:skuNo){
            if(sku_Map.containsKey(skuno)){
                try{
                    stockMap.put(skuno,sku_Map.get(skuno));
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info(e.getMessage());
                }

            }
        }
        logger.info("havok更新库存成功");
        return stockMap;
    }

}
