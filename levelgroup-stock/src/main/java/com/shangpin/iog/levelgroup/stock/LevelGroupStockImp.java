package com.shangpin.iog.levelgroup.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
public class LevelGroupStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = "2015092901551";
    }


    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());

        Map<String,String> mongMap = new HashMap<>();

        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","levelgroup");

        logMongo.info(mongMap);

        for (String skuno : skuNo) {
            String stock = getStock(skuno);
            if (StringUtils.isNotEmpty(stock))
                skustock.put(skuno, stock);
            else
                skustock.put(skuno, "0");
        }
        logger.info("levelgroup赋值库存数据成功");
        return skustock;
    }


    private static String getStock(String sku){
        String url = "http://www.ln-cc.com/dw/shop/v15_8/products/09"+sku+"/availability?inventory_ids=09&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";

        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
        if( jsonstr != null && jsonstr.length() >0){
            JSONObject json = JSONObject.fromObject(jsonstr);
            if (!json.isNullObject() && !json.containsKey("fault")) {
                JSONObject inventObj = json.getJSONObject("inventory");
                if (!inventObj.isNullObject() && !inventObj.isEmpty()){
                    int instock = inventObj.getInt("stock_level");
                    return instock+"";
                }
            }
        }
        return null;
    }

    public static void main(String args[]) throws Exception {

        AbsUpdateProductStock levelGroupStockImp = new LevelGroupStockImp();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("levelgroup更新数据库开始");
        //2015081401431
        levelGroupStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("levelgroup更新数据库结束");
        System.exit(0);
    }
}
