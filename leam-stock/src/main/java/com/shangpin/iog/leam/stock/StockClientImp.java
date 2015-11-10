package com.shangpin.iog.leam.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.leam.dto.LeamDTO;
import com.shangpin.iog.leam.dto.TokenDTO;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunny on 2015/8/18.
 */
public class StockClientImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    String user="shamping";
    String password="PA#=k2xU^ddUc6Jm";

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String tokenUrl;
    private static String skuUrl;
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        tokenUrl = bdl.getString("token");
        skuUrl=bdl.getString("skuUrl");
    }

//    String tokenUrl="http://188.226.153.91/modules/api/v2/getToken/";
//    String skuUrl="http://188.226.153.91/modules/api/v2/stock/";//请求sku地址
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();
//        String stockUrl="http://188.226.153.91/modules/api/v2/stock/id/";

        String token ="";
        int i=0;
        try {
            List<LeamDTO> list=getSkus(skuUrl);
            logger.info("拉到的数据量是:"+list.size());
            String size ="";
            if(null!=list){
                Map<String,String> skuMap = new HashMap<>();
                for(LeamDTO leamDTO:list) {
                    size = leamDTO.getSize();
                    if(null==size) continue;
                    size = size.replace(",", ".");
                    skuMap.put(leamDTO.getStock_id()+"-"+size,leamDTO.getQty());
                }

                for(String skuId:skuNo){
                    if(skuMap.containsKey(skuId)){
                        i++;
                        skustock.put(skuId,skuMap.get(skuId));
                    }else{
                        skustock.put(skuId,"0");
                    }
                }
            }

        }catch (Exception e){
            loggerError.error("leam 获取供应商的库存失败.--"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("待更新的库存数是 :" + skustock.size()+" 相匹配的有:" + i);
        return skustock;
    }
    private  String getToken(String tokenUrl){
        Map<String, String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        param.put("user",user);
        param.put("password",password);
        String result = HttpUtil45.post(tokenUrl,param,outTimeConf);
        result=getTokenByjsonstr(result);
        return result;
    }
    private  List<LeamDTO> getSkus(String url){
        List<LeamDTO>list = new ArrayList<>();
        String result="";
        String token="";
        try {
            token=getToken(tokenUrl);
            Map<String, String> param = new HashMap<>();
            param.put("user",user);
            param.put("password",password);
            OutTimeConfig outTimeConf = new OutTimeConfig(1000*60,1000*60*30,1000*60*30);
            result= HttpUtil45.post(url+"?t="+token, param, outTimeConf);
            System.out.println(" result = "+ result);
            list = getObjectsByJsonString(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private  List<LeamDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<LeamDTO> objs = new ArrayList<LeamDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<LeamDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }

    private  String getTokenByjsonstr(String jsonStr){
        Gson gson = new Gson();
        TokenDTO obj=null;
        try {
            obj=gson.fromJson(jsonStr, TokenDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.getToken();
    }
    private LeamDTO getObjectByjsonstr(String jsonStr){
        Gson gson = new Gson();
        LeamDTO obj=null;
        try {
            obj=gson.fromJson(jsonStr, LeamDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static void main(String[] args) throws Exception {
        StockClientImp impl = new StockClientImp();
//
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("LEAM更新数据库开始");
//        StockClientImp.supplierSkuIdMain=true;
        try {
            impl.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        } catch (Exception e) {
            loggerError.error("leam 更新库存失败."+e.getMessage());
            e.printStackTrace();
        }
//        impl.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
        logger.info("LEAM更新数据库结束");
        System.exit(0);


//        impl.getSkus("http://188.226.153.91/modules/api/v2/stock/");

    }
}
