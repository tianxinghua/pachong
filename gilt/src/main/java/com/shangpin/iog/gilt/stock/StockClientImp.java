package com.shangpin.iog.gilt.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import com.shangpin.iog.gilt.dto.InventoryDTO;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunny on 2015/8/10.
 */
public class StockClientImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();
        String supplierId = "201508081715";
        String stockUrl="https://api-sandbox.gilt.com/global/inventory";
        try{
            logger.info("拉取gilt数据开始");
            Map<String,String> param = new HashMap<>();
            OutTimeConfig outTimeConf = new OutTimeConfig();
            String result= HttpUtil45.get(stockUrl, outTimeConf, param, "fb8ea6839b486dba8c5cabb374c03d9d", "");
            List<InventoryDTO> list  = getObjectsByJsonString(result);
            for (InventoryDTO dto:list){
                skustock.put(dto.getSku_id(),Integer.parseInt(dto.getQuantity()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return skustock;
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
            //logger.info("get List<ApennineProductDTO> fail :"+e);
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
      /*  String url = "https://api-sandbox.gilt.com/global/inventory/";
        String skuUrl="https://api-sandbox.gilt.com/global/skus/";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();

        param.put("sku_id","144740");//197672
        String result= HttpUtil45.get(skuUrl+"197672", outTimeConf, param, "fb8ea6839b486dba8c5cabb374c03d9d", "");
        System.out.println(result);
        GiltSkuDTO dto  =getObject(result);
        System.out.println(dto.getAttributes().get(3).getSize().getValue());*/
        String supplierId = "201508081715";
        AbsUpdateProductStock giltStockImp = new StockClientImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("gilt更新数据库开始");
        giltStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("gilt更新数据库结束");
        System.exit(0);
    }
}
