package com.shangpin.iog.ostore.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
public class OstoreStockImp extends AbsUpdateProductStock {
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
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {

        Map<String, String> skuStock = new HashMap<>();
        Map<String,String> stock_map = new HashMap<>();

        String url = "http://b2b.officinastore.com/shangpin.asp?mode=stock_only";
        try{
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(360000);
            timeConfig.confConnectOutTime(36000);
            timeConfig.confSocketOutTime(360000);
            List<String> resultList = HttpUtil45.getContentListByInputSteam(url, timeConfig, null, null, null);
            HttpUtil45.closePool();
            StringBuffer buffer =new StringBuffer();
            for(String content:resultList){
                buffer.append(content).append("|||");
            }
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","acanfora");
            mongMap.put("result", buffer.toString()) ;
            try {
                logMongo.info(mongMap);
            } catch (Exception e) {
                e.printStackTrace();
                loggerError.error("存入mongodb失败"+buffer.toString());
            }

            int i=0;
            String stock="",size ="";
            String skuId = "";
            for(String content:resultList) {
                if (i == 0) {
                    i++;
                    continue;
                }
                i++;
                //SKU;Season;Sizes
                // 0 ;  1   ;  2
                String[] contentArray = content.split(";");
                if (null == contentArray || contentArray.length < 3) continue;

                String[] sizeArray = contentArray[2].split(",");
                for(String sizeAndStock:sizeArray) {
                    if(sizeAndStock.contains("(")&&sizeAndStock.length()>1) {
                        size = sizeAndStock.substring(0, sizeAndStock.indexOf("("));
                        stock = sizeAndStock.substring(sizeAndStock.indexOf("(")+1, sizeAndStock.length() - 1);
                    }

                    skuId = contentArray[0] + "-"+size;
                    if(skuId.indexOf("½")>0){
                        skuId = skuId.replace("½","+");
                    }
                    stock_map.put(skuId,stock);
                }
            }

            for(String skuno:skuNo){
                    if (stock_map.containsKey(skuno)) {
                        skuStock.put(skuno, stock_map.get(skuno));
                    }else{
                        skuStock.put(skuno,"0");
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
            loggerError.error(e.getMessage());
        }
        logger.info("Ostore赋值库存数据成功");
        return skuStock;
    }

    public static void main(String args[]) throws Exception {

        AbsUpdateProductStock ostoreStockImp = new OstoreStockImp();
        ostoreStockImp.setUseThread(true);ostoreStockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("OSTORE更新数据库开始");
        ostoreStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("OSTORE更新数据库结束");
        System.exit(0);

        /*OstoreStockImp ostoreStockImp =new OstoreStockImp();
        Collection<String> sku = new HashSet<>();
        sku.add("0112-5523A1888-40");
        Map<String,Integer> stock = new HashMap<>();
        stock = ostoreStockImp.grabStock(sku);
        System.out.println(stock.get("0112-5523A1888-40"));*/
    }
}
