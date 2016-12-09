package com.shangpin.ep.order.module.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.shangpin.ep.order.enumeration.LogLeve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.openapi.OpenApiProperties;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.util.MD5;
import com.shangpin.ep.order.util.ToolsUtils;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Component
public class OpenApiService {
	
//	public static void main(String[] args) {
//		OpenApiService open = new OpenApiService();
//		try {
//			open.setPurchaseException("513b6792d10b4041856392c085c47a5c","3432f51a37704fbbaf0e82cf385e707d","CGD2016112400004");
//		} catch (Exception e) {
//			System.out.println("ss:"+e.getMessage());
//		}
//		try {
//			open.setSkuQuantity("513b6792d10b4041856392c085c47a5c","3432f51a37704fbbaf0e82cf385e707d","57713a2974f759a70dda799d-M",1);
//		} catch (ServiceMessageException e) {
//			System.out.println("ss:"+e.getMessage());
//		}
//	}
	
	@Autowired
    OpenApiProperties openApiProperties;
    /**
     * 更新供货商库存
     * @param app_key：供货商的KEY
     * @param app_secret
     * @param sku
     * @param quantity
     */
    public void setSkuQuantity( String app_key, String app_secret,String sku,int quantity) throws ServiceMessageException{
    	
        String request = "",url="";
        request = "{\"InventoryQuantity\":" + quantity + ",\"SkuNo\":\""+sku+"\",\"SupplierSkuNo\":\"\"}";
        url= openApiProperties.getOpenApi().getUpdatestock(); //"/stock/updatestock";
        try {
        	 String result = getResponseJson(app_key,app_secret,request,url);
        	 JSONObject json = JSONObject.parseObject(result);
             if(json!=null){
             	 if("0".equals(json.getString("responseCode"))){
             		 LogCommon.recordLog("更新成功=="+sku+":"+quantity);
             	 }else{
             		 LogCommon.recordLog(sku+"更新失败=="+json.getString("responseMsg"));
             	 }
             }
       
        } catch (Exception e) {
        	 LogCommon.recordLog("更新供应商:"+app_key+"的skuId:"+sku+"库存时报错", e);
        }
    }

    /**
     * 设置采购异常
     * @param app_key
     * @param app_secret
     * @param purchaseNo
     */
    public void setPurchaseException( String app_key, String app_secret,String purchaseNo)  throws Exception{
    	 String request = "",url="";
    	 request = "{\"purchaseOrderNo\":\"" + purchaseNo +"\"}";
    	 url= openApiProperties.getOpenApi().getFindporder(); //"/purchase/findporder";
    	 String result = getResponseJson(app_key,app_secret,request,url);
    	 String purchaseOrderDetailNo = null;

		 //获取采购单明细
		 if(result!=null){
			 JSONObject json = JSONObject.parseObject(result);
			 if("0".equals(json.getString("responseCode"))){
				String details = json.getString("response");
				JSONObject detailsJson = JSONObject.parseObject(details);
				JSONArray arr = JSONArray.parseArray(detailsJson.getString("PurchaseOrderDetails"));
				 if(null!=arr&&arr.size()>0){

					 purchaseOrderDetailNo = arr.getJSONObject(0).getString("SopPurchaseOrderDetailNo");
					 LogCommon.recordLog(purchaseNo+"获取采购单明细成功=="+purchaseOrderDetailNo,LogLeve.DEBUG);
				 }else{

					 throw new ServiceMessageException("采购单：" + purchaseNo+"获取采购单明细失败==" +json.getString("responseMsg") );
				 }
			 }else{
				 LogCommon.recordLog(purchaseNo+"获取采购单明细失败=="+json.getString("responseMsg"),LogLeve.DEBUG);
			 }
		 }
		 //取消采购单
		 if(purchaseOrderDetailNo!=null){
			 url =openApiProperties.getOpenApi().getCancelpurchase(); //"/purchase/cancelpurchase";
			 request = "{\"Memo\":\"" + purchaseNo +"\",\"SopPurchaseOrderDetailNos\":["+purchaseOrderDetailNo+"]}";
			 result = getResponseJson(app_key,app_secret,request,url);
			 if(result!=null){
				 JSONObject json = JSONObject.parseObject(result);
				 if("0".equals(json.getString("responseCode"))){
					 LogCommon.recordLog(purchaseNo+"采购取消成功");
				 }else{
					 LogCommon.recordLog(" exceptionLog " + purchaseNo+"采购取消失败"+json.getString("responseMsg"), LogLeve.ERROR);
					 throw new Exception("采购异常失败 ，待下次继续");
				 }
			 }
		 }

    }
    
    private String getResponseJson(String app_key, String app_secret,String request,String url){
    	
    	String req_body = null;
        String host = openApiProperties.getOpenApi().getHost(); //."http://192.168.20.83:9090";
        
        if (request != null) req_body = request;
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("app_key", app_key);
        Date timestamp  = new Date();
        params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp));
        if (request != null) params.put("request", req_body);

        String charset = "UTF-8";
        String md5_sign;
        String result = null;
        try {
            md5_sign = "app_key=" + ToolsUtils.urlEncode(app_key, charset)
                    + (params.containsKey("request") ? "&request=" + ToolsUtils.urlEncode(params.get("request"), charset) : "")
                    + "&timestamp=" + ToolsUtils.urlEncode(params.get("timestamp"), charset)
                    + "_" + app_secret;

            String md5_result = MD5.encrypt32(md5_sign);
            params.put("sign", md5_result);
            Map<String, String> headerMap = new LinkedHashMap<String, String>();
            headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            LogCommon.recordLog("params = " + params.toString(),LogLeve.DEBUG);
            result = HttpUtil45.post(host + url,params, headerMap,new OutTimeConfig(1000*3,1000*30,1000*30));
            LogCommon.recordLog("result=="+result);
            LogCommon.recordLog("耗时：" + (System.currentTimeMillis()-timestamp.getTime()) );
            
            if(HttpUtil45.errorResult.equals(result)){
            	return null;
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
    }
}
