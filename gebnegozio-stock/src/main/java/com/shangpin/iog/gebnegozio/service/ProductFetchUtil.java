package com.shangpin.iog.gebnegozio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gebnegozio.dto.StockDTO;
import com.shangpin.iog.gebnegozio.dto.SupplierToken;
import com.shangpin.iog.gebnegozio.dto.TokenResp;
import com.shangpin.iog.gebnegozio.schedule.Schedule;
import com.shangpin.iog.gebnegozio.util.HttpClientUtil;
import com.shangpin.iog.gebnegozio.util.HttpRequestMethedEnum;
import javafx.scene.chart.PieChart;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Component
public class ProductFetchUtil {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl = null;
    private static String supplierId = "",tokenUrl="",stockUrl="",postUrl="";
    Gson gson = new Gson();

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        tokenUrl = bdl.getString("tokenUrl");
        stockUrl = bdl.getString("stockUrl");
        postUrl = bdl.getString("postUrl");
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
        String token = "";
        try {
            for (String sku : skuNos) {
                SupplierToken supplierToken = queryToken(supplierId);
                if (null != supplierToken){
                    token = supplierToken.getAccessToken();
                }
                if(null != token && !token.equals("")) {
                    String qty = selStock( sku , token );
                    if( null == qty || qty.equals("") ){
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
                String url = stockUrl + urlStr;
                String stockJson = selMessage(token , url);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson , StockDTO.class);
                    qty = stockDTO.getStockItem().getQty();
                    if(null == qty || qty.equals("")){
                        qty = "0";
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  qty;
    }

    /**
     *  根据 supplierId 查token
     * @param supplierId
     * @return
     */
    public SupplierToken queryToken(String supplierId){
        SupplierToken supplierTokenDTO = new SupplierToken();
        TokenResp tokenResp = new TokenResp();
        Map<String, String> param = new HashMap<String, String>();
        param.put("supplierId", supplierId);
        try {
            String result = HttpUtil45.operateData("get", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
                    1000 * 60 * 30, 1000 * 60 * 30), param, "", "", "");
            tokenResp = gson.fromJson( result, TokenResp.class);
            String data = tokenResp.getData();
            if (null != tokenResp && !tokenResp.equals("") && tokenResp.getCode().equals("200")){
                if( null != data && !data.equals("") ){
                    supplierTokenDTO = gson.fromJson(data,SupplierToken.class);
                }else {
                    supplierTokenDTO = null;
                }
            }else {
                supplierTokenDTO = null;
                logger.info("根据 supplierId 查token失败：" + tokenResp.getMessage());
            }
        } catch (ServiceException e) {
            logger.error("根据 supplierId 查token异常：" + e.getMessage());
            e.printStackTrace();
        }
        return supplierTokenDTO;
    }
}

