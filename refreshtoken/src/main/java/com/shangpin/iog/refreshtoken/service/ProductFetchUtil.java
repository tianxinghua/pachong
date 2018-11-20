package com.shangpin.iog.refreshtoken.service;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.refreshtoken.dto.SupplierToken;
import com.shangpin.iog.refreshtoken.dto.TokenResp;
import com.shangpin.iog.refreshtoken.dto.VipTokenResp;
import com.shangpin.iog.refreshtoken.util.HttpClientUtil;
import com.shangpin.iog.refreshtoken.util.HttpRequestMethedEnum;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Component
public class ProductFetchUtil {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl = null;
    private static String gebSupplierId = "",vipSupplierId = "",tokenUrl="",queryGebTokenUrl="",queryVipTokenUrl="";
    Gson gson = new Gson();

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        gebSupplierId = bdl.getString("gebSupplierId");
        vipSupplierId = bdl.getString("vipSupplierId");
        tokenUrl = bdl.getString("tokenUrl");
        queryGebTokenUrl = bdl.getString("queryGebTokenUrl");
        queryVipTokenUrl = bdl.getString("queryVipTokenUrl");
    }

    /**
     *  获取geb token
     */
    public String queryGebToken(){
        String token = "";
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        // 请求正文内容
        String json = "{\"username\":\"ming.liu@shangpin.com\",\"password\":\"Ex7n4AQ5\"}";
        //发送请求
        HashMap<String,String> response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,queryGebTokenUrl, null, header, json);
        if(null != response && response.size() > 0 && response.get("code").equals("200") ){
            token = response.get("resBody");
            token = token.substring( 1, token.length()-1 );
            SupplierToken supplierToken = queryToken(gebSupplierId);
            if (null == supplierToken){
                addToken(token,gebSupplierId);//token入库
            }else {
                updateToken(token,gebSupplierId);//更新token
            }
        }else {
            logger.info("获取token异常，重新获取一次："+ response.get("message"));
            HashMap<String,String> tokenUpdateResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,queryGebTokenUrl, null, header, json);
            if(null != tokenUpdateResp && tokenUpdateResp.size() > 0 && tokenUpdateResp.get("code").equals("200") ) {
                token = tokenUpdateResp.get("resBody");
                token = token.substring(1, token.length() - 1);
                SupplierToken supplierToken = queryToken(gebSupplierId);
                if (null == supplierToken){
                    addToken(token,gebSupplierId);//token入库
                }else {
                    updateToken(token,gebSupplierId);//更新token
                }
            }

        }
        return token;
    }

    /**
     * 获取
     * @return
     */
    public String queryVipToken(){
            String token = "";
            String entity = "{\"uid\":\"087\",\"username\":\"shangpin\",\"password\":\"10122han9p1n2018\"}";
            HashMap<String,String> productsJSON = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,queryVipTokenUrl  ,null, null,entity);
            if(null != productsJSON && productsJSON.size() >0 && productsJSON.get("code").equals("200")){
                VipTokenResp tokenResp = gson.fromJson( productsJSON.get("resBody"), VipTokenResp.class );
                String errorCode = tokenResp.getErrorCode();
                if(errorCode.equals("0")){
                    token = tokenResp.getToken();
                    SupplierToken supplierToken = queryToken(vipSupplierId);
                    if (null == supplierToken){
                        addToken(token,vipSupplierId);//token入库
                    }else {
                        updateToken(token,vipSupplierId);//更新token
                    }
                }else if (errorCode.equals("2")){
                    logger.info("获取token间隔时间太短：" + tokenResp.getErrorMessage());
                    token = tokenResp.getToken();
                    SupplierToken supplierToken = queryToken(vipSupplierId);
                    if (null == supplierToken){
                        addToken(token,vipSupplierId);//token入库
                    }else {
                        updateToken(token,vipSupplierId);//更新token
                    }
                }else {
                    logger.info("获取token异常，重新获取一次："+ tokenResp.getErrorMessage());
                    HashMap<String,String> productsUpdateJSON = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,queryVipTokenUrl  ,null, null,entity);
                    if(null != productsUpdateJSON && productsUpdateJSON.size() >0 && productsUpdateJSON.get("code").equals("200")) {
                        VipTokenResp tokenUpdateResp = gson.fromJson(productsUpdateJSON.get("resBody"), VipTokenResp.class);
                        String errorUpdateCode = tokenUpdateResp.getErrorCode();
                        if (errorUpdateCode.equals("0")) {
                            token = tokenResp.getToken();
                            SupplierToken supplierToken = queryToken(vipSupplierId);
                            if (null == supplierToken) {
                                addToken(token, vipSupplierId);//token入库
                            } else {
                                updateToken(token, vipSupplierId);//更新token
                            }
                        } else if (errorUpdateCode.equals("2")) {
                            logger.info("获取token间隔时间太短：" + tokenResp.getErrorMessage());
                            token = tokenResp.getToken();
                            SupplierToken supplierToken = queryToken(vipSupplierId);
                            if (null == supplierToken) {
                                addToken(token, vipSupplierId);//token入库
                            } else {
                                updateToken(token, vipSupplierId);//更新token
                            }
                        }
                    }
                }
            }

            return token;
    }
    /**
     * 添加token到数据库
     * @param token
     * @return
     */
    public void addToken(String token, String supplierId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierToken supplierTokenDTO = new SupplierToken();
        supplierTokenDTO.setAccessToken(token);
        supplierTokenDTO.setCreateTime(df.format(new Date()));// new Date()为获取当前系统时间
        supplierTokenDTO.setSupplierId(supplierId);

        String supplierToken = gson.toJson(supplierTokenDTO);
        Map<String, String> param = new HashMap<String, String>();
        param.put("supplierToken", supplierToken);
        String result = null;
        TokenResp tokenResp = new TokenResp();
        try {
            result = HttpUtil45.operateData("post", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
                    1000 * 60 * 30, 1000 * 60 * 30), param, "", "", "");
            tokenResp = gson.fromJson( result, TokenResp.class);
            if (null != tokenResp && !tokenResp.equals("") && tokenResp.getCode().equals("200")){
            }else {
                logger.info("数据库添加token失败：" + tokenResp.getMessage());
            }
        } catch (ServiceException e) {
            logger.error("数据库添加token异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateToken(String token, String supplierId){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierToken supplierTokenDTO = new SupplierToken();
        supplierTokenDTO.setAccessToken(token);
        supplierTokenDTO.setExpireTime(df.format(new Date()));// new Date()为获取当前系统时间
        supplierTokenDTO.setSupplierId(supplierId);

        String supplierToken = gson.toJson(supplierTokenDTO);
        Map<String, String> param = new HashMap<String, String>();
        param.put("supplierToken", supplierToken);
        String result = null;
        TokenResp tokenResp = new TokenResp();
        try {
            result = HttpUtil45.operateData("put", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
                    1000 * 60 * 30, 1000 * 60 * 30), param, "", "", "");
        tokenResp = gson.fromJson( result, TokenResp.class);
        if (null != tokenResp && !tokenResp.equals("") && tokenResp.getCode().equals("200")){
        }else {
            logger.info("数据库更新token失败：" + tokenResp.getMessage());
        }
    } catch (ServiceException e) {
        logger.error("数据库更新token异常：" + e.getMessage());
        e.printStackTrace();
    }
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
            String result = null;
                result = HttpUtil45.operateData("get", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
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

