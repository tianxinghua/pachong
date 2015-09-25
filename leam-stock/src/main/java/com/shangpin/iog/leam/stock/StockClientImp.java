package com.shangpin.iog.leam.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
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

    String user="shamping";
    String password="PA#=k2xU^ddUc6Jm";

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();
        String stockUrl="http://188.226.153.91/modules/api/v2/stock/id/";
        String tokenUrl="http://188.226.153.91/modules/api/v2/getToken/";
        String token ="";
        try {
            logger.info("拉取leam数据开始");
            Map<String, String> param = new HashMap<>();
            param.put("user",user);
            param.put("password",password);
            OutTimeConfig outTimeConf = new OutTimeConfig();
            token=getToken(tokenUrl);
            LeamDTO dto = new LeamDTO();
            String result ="";
            Iterator<String>it=skuNo.iterator();
            if(it.hasNext()){
                String skuno=it.next();
                result = HttpUtil45.post(stockUrl+skuno+"/?t="+token,param,outTimeConf);
                try {
                    dto=getObjectByjsonstr(result);
                    skustock.put(skuno,dto.getQty());
                } catch (Exception e) {
                    skustock.put(skuno,"0");
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("LEAM更新数据库开始");
        impl.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//        impl.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
        logger.info("LEAM更新数据库结束");
        System.exit(0);
    }
}
