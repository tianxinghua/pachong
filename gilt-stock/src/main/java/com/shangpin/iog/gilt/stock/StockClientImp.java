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

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();
        String stockUrl="https://api-sandbox.gilt.com/global/realtime-inventory";
        try{
            logger.info("拉取gilt数据开始");
            Map<String,String> param = new HashMap<>();
            OutTimeConfig outTimeConf = new OutTimeConfig();
            List<InventoryDTO>list=new ArrayList<>();
            StringBuffer str = new StringBuffer();
            int limit = 50;
            int offset = 0;
            param.put("","");
            String result="";
            do{
                param.put("offset",offset+"");
                result=HttpUtil45.get(stockUrl, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","");
                list = getObjectsByJsonString(result);
                offset = offset+50;
                str.append(result);
            }while (list.size()==limit);
            list  = getObjectsByJsonString(str.toString());
            for (InventoryDTO dto:list){
                skustock.put(dto.getSku_id(),dto.getQuantity());
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



        AbsUpdateProductStock giltStockImp = new StockClientImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("gilt更新库存开始");
        giltStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("gilt更新库存结束");
        System.exit(0);

    }
}
