package com.shangpin.iog.gebnegozio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gebnegozio.dto.StockDTO;
import com.shangpin.iog.gebnegozio.util.HttpClientUtil;
import com.shangpin.iog.gebnegozio.util.HttpRequestMethedEnum;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Component
public class ProductFetchUtil {
    public static final String STOCK_URL = "https://www.gebnegozionline.com/rest/marketplace_shangpin/V1/stockStatuses/";
    public static final String POST_URL = "https://www.gebnegozionline.com/rest/marketplace_shangpin/V1/integration/customer/token";
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl = null;
    private static String supplierId = "",usr="",pwd="",recordCount="",language="";
    Gson gson = new Gson();
    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        usr = bdl.getString("usr");
        pwd = bdl.getString("pwd");
        recordCount = bdl.getString("recordCount");
        language = bdl.getString("language");
        supplierId = bdl.getString("supplierId");
    }
    ObjectMapper mapper = new ObjectMapper();
    OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    /**
     * 程序配置：
     * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
     * 时间配置 获取当前时间
     * 页码为程序中循环增加，
     * 程序的循环结束（while循环）标志：
     * 拉取的信息的 list size()为 0 的时候结束
     */
    public Map<String,String> getProductStock(Collection<String> skuNos) {
        //定义供应商 skuNo （key） Quantita(value) Map集合
        logger.info("===============Collection<String> skuNos size()================"+skuNos.size());
        Map<String, String> spStockMap = new HashMap<>();
        String data = "";
        try {

            for (String sku : skuNos) {
                String token = selToken();
                if(null != token && !token.equals("")) {
                    String qty = selStock( sku , token );
                    if( null == qty && qty.equals("") ){
                        qty = "0";
                    }
                    spStockMap.put( sku , qty );
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("成功获取到的map大小  spStockMap.size======"+spStockMap.size());
        return spStockMap;
    }
    /**
     *  获取token
     */
    public String selToken(){
        String token = "";
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");

        // 请求正文内容
        String json = "{\"username\":\"ming.liu@shangpin.com\",\"password\":\"Ex7n4AQ5\"}";

        //返回值是token
        HashMap<String,String> response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,POST_URL, null, header, json);
        if(null != response && response.size() > 0 && response.get("code").equals("200") ){
            token = response.get("resBody");
            token = token.substring( 1, token.length()-1 );
        }else {
            logger.info("获取token异常，正在重新获取"+ response.get("message"));
            selToken();
        }
        return token;
    }
    /**
     * 携带token获取内容、get公用方法
     */
    public String selMessage(String token , String url){
        String respValue = "";
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);

        HashMap<String,String> response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,url  ,null, header,null);
        if(null != response && response.size() > 0 && response.get("code").equals("200") ) {
            respValue = response.get("resBody");
        }
        return respValue;
    }
    /**
     * 查库存
     * @return
     */
    public String selStock( String sku , String token ){
        String qty = null;
        if ( null != sku && !sku.equals("") ){
            try {
                if(sku.contains("\\\\")){
                    sku = sku.replaceAll("\\\\\\\\","\\\\");
                }
                String urlStr = URLEncoder.encode( sku , "UTF-8");
                String url = STOCK_URL + urlStr;
                String stockJson = selMessage(token , url);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson , StockDTO.class);
                    qty = stockDTO.getStockItem().getQty();
                    if(null == qty && qty.equals("")){
                        qty = "0";
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  qty;
    }
}

