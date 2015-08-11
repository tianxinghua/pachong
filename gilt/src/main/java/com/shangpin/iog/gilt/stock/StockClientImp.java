package com.shangpin.iog.gilt.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import org.apache.log4j.Logger;

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
        try{
            logger.info("拉取gilt数据开始");
            Map<String,String> param = new HashMap<>();
            OutTimeConfig outTimeConf = new OutTimeConfig();
            String result= HttpUtil45.get("https://api-sandbox.gilt.com/global/skus", outTimeConf, param, "fb8ea6839b486dba8c5cabb374c03d9d", "");
            List<GiltSkuDTO> list  = getObjectsByJsonString(result);
            for (GiltSkuDTO dto:list){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private static List<GiltSkuDTO> getObjectsByJsonString(String jsonStr){
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

    }
}
