package com.shangpin.iog.vela.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.spinnaker.stock.dto.Quantity;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huxia on 2015/7/16.
 */
public class GrabStockImp extends AbsUpdateProductStock{

    private static Logger logger = Logger.getLogger("info");

    private Map<String,String> barcode_map = new HashMap<>();

    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, Integer> stock_map = new HashMap<String, Integer>();
        Gson gson = new Gson();
        for (String skuno : skuNo) {
            if (barcode_map.containsKey(skuno)) {
                continue;
            } else {
                barcode_map.put(skuno, null);
            }

            String barcode = skuno;
            //根据供应商skuno获取库存，并更新我方sop库存
            String url = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=Default&barcode=[[barcode]]&key=MPm32XJp7M";
            url = url.replaceAll("\\[\\[barcode\\]\\]", barcode);
            String json = null;
            try {
                json = HttpUtils.get(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (json != null && !json.isEmpty()) {
                try {
                    Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
                    }.getType());
                    stock_map.put(skuno, Integer.valueOf(result.getResult()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return stock_map;
    }

}
