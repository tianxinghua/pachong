package com.shangpin.iog.levelgroup.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.levelgroup.dto.Product;
import com.shangpin.iog.levelgroup.util.MyTxtUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
@Component("levelgroup")
public class LevelGroupStockImp extends AbsUpdateProductStock {

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

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }


    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());

        Map<String,String> mongMap = new HashMap<>();
        Map<String,String> stockMap2 = getStockList();

        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","levelgroup");

        logMongo.info(mongMap);

        for (String skuno : skuNo) {
            String stock = getStock(skuno);
            String stock2 = stockMap2.get(skuno);
            if (StringUtils.isNotEmpty(stock))
                skustock.put(skuno, stock);
            else if (StringUtils.isNotEmpty(stock2))
                skustock.put(skuno, stock2);
            else
                skustock.put(skuno, "0");
        }
        logger.info("levelgroup赋值库存数据成功");
        return skustock;
    }

    private static Map<String,String> getStockList(){
        boolean flag = true;
        try {
            flag = MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            loggerError.error("下载LEVELGROUP文件失败!"+e.getMessage());
        }
        List<Product> list = null;
        Map<String,String> map = new HashMap();
        if (flag){
            try {
                list = MyTxtUtil.readTXTFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0;i<list.size();i++){
                map.put(list.get(i).getVARIANT_SKU(),list.get(i).getSTOCK_LEVEL());
            }
        }
        return map;
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
        //加载spring
        loadSpringContext();
        //拉取数据
        LevelGroupStockImp levelGroupStockImp =(LevelGroupStockImp)factory.getBean("levelgroup");
        levelGroupStockImp.setUseThread(true);levelGroupStockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("levelgroup更新数据库开始");
        //2015081401431
        levelGroupStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("levelgroup更新数据库结束");
        System.exit(0);
    }
}
