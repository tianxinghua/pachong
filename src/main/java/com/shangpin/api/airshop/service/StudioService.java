package com.shangpin.api.airshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.request.SlotDefectiveSpu;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.FastJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8.
 */
@Service
public class StudioService {

    private static Logger logger = LoggerFactory.getLogger(StudioService.class);

    @Autowired
    private RestTemplate restTemplate;

    /*
    * 获取待处理商品列表
    * */
    public JSONObject getPendingProductList(String supplierId, String brandName, String categoryName, String seasonName,
                                            String sku, String itemCode, String startTime, String endTime, int pageIndex, int pageSize) {

        String url = ApiServiceUrlConfig.getStudioPendingProducts();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("brandName", brandName);
        request.put("categoryName", categoryName);
        request.put("seasonName", seasonName);
        request.put("supplierSpuNo", sku);
        request.put("slotSpuNo", itemCode);
        request.put("startTime", DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("endTime",  DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("PageIndex", String.valueOf(pageIndex));
        request.put("PageSize", String.valueOf(pageSize));
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);


    }

    //包装Http请求参数
    private HttpEntity<String> getHttpPostData(String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> paramEntity = new HttpEntity<String>("=" + param, headers);
        return paramEntity;
    }

    /*
    获取供应商已申请批次
    */
    public JSONObject getSupplierSlotList(String supplierNo, String status) {

        String url = ApiServiceUrlConfig.getStudioSupplierSlots();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierNo);
        request.put("status", status);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /*
    释放申请的批次
    */
    public JSONObject getReleaseStudioSlot(String supplierNo, String slotId, String userName) {

        String url = ApiServiceUrlConfig.getReleaseStudioSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierNo);
        request.put("studioSlotId", slotId);
        request.put("createUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }



    /*
* 删除商品供货商商品
* */
    public  JSONObject delSlotProduct(String supplierNo, String slotSSId, String userName){
        String url = ApiServiceUrlConfig.getdelSlotProduct();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierNo);
        request.put("slotSSId", slotSSId);
        request.put("createUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /*
   * 获取批次详情
   * */
    public JSONObject getSlotInfo(String supplierId, String slotNo) {

        String url = ApiServiceUrlConfig.getStudioSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("slotNo", slotNo);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);

    }


    /*
 * 批次添加商品
 * */
    public JSONObject addProductIntoSlot(String supplierNo, String slotNo, String slotSSId, String userName) {

        String url = ApiServiceUrlConfig.getAddSpuSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierNo);
        request.put("slotNo", slotNo);
        request.put("slotSSIds", slotSSId);
        request.put("createUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);

    }

    /*
 * 批次删除商品
 * */
    public JSONObject delProductFromSlot(String supplierId, String slotNo, String slotSSId, String slotSSDId, String userName) {

        String url = ApiServiceUrlConfig.getdelSpuSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("slotNo", slotNo);
        request.put("slotSSId", slotSSId);
        request.put("slotSSDId", slotSSDId);
        request.put("createUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 添加物流信息
     *
     * @param supplierId
     * @param slotId
     * @param trackName
     * @param trackingNo
     * @param userName
     * @return
     */
    public JSONObject insertSlotLogistic(String supplierId, String slotId, String trackName, String trackingNo,String memo, String userName) {

        String url = ApiServiceUrlConfig.getAddLogisticSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioSlotId", slotId);
        request.put("trackName", trackName);
        request.put("trackingNo", trackingNo);
        request.put("memo", memo);
        request.put("createUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 获取物流信息
     *
     * @param supplierId
     * @param slotId
     * @return
     */
    public JSONObject getSlotLogisticInfo(String supplierId, String slotId) {

        String url = ApiServiceUrlConfig.getSlotLogisticInfo();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioSlotId", slotId);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }


    /*
* 验证批次，确定能否发货
* */
    public JSONObject checkProductAndSendSlot(String supplierId, String slotNo) {
        String url = ApiServiceUrlConfig.getCheckSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("slotNo", slotNo);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);

    }
    /*打印批次*/
    public JSONObject slotPrint(String supplierId, String slotId) {
        String url = ApiServiceUrlConfig.getSlotPrint();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioSlotId", slotId);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);

    }



    /*
   * 获取可申请批次列表
   * */
    public JSONObject getStudioSlotList(String supplierId,String StudioId, String startTime, String endTime, String categoryNos, int pageIndex, int pageSize ,int history) {

        String url = ApiServiceUrlConfig.getStudioSlots();
        Map<String, String> request = new HashMap<>();
        request.put("studioId", StudioId);
        request.put("startTime", DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("endTime",  DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("categoryNos", categoryNos);
        request.put("pageIndex", String.valueOf(pageIndex));
        request.put("pageSize", String.valueOf(pageSize));
        request.put("supplierId",supplierId);
        request.put("history", String.valueOf(history));
        return JSONObject.parseObject(HttpClientUtil.doPost(url, request));
    }

    /*获取摄影棚信息*/
    public JSONObject getStudioList(String categoryNos,String isHistory) {
        String url = ApiServiceUrlConfig.getStudioList();
        Map<String, String> request = new HashMap<>();
        request.put("categoryNos", categoryNos);
        request.put("isHistory", isHistory);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }


    /*
* 批次申请
* */
    public JSONObject applySlot(String supplierId, String slotIds, String userName) {

        String url = ApiServiceUrlConfig.getApplySlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioSlotIds", slotIds);
        request.put("supplierUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 获取未确认的返货单列表
     *
     * @param supplierId
     * @return
     */
    public JSONObject getReturnSlotList(String supplierId,String StudioId, String startTime,String endTime) {
        String url = ApiServiceUrlConfig.getReturnSlotList();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioId", StudioId);
        request.put("startTime", DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("endTime",  DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("pageSize", String.valueOf(100));
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 获取未确认的返货单列表
     *
     * @param supplierId
     * @return
     */
    public JSONObject getReceivedSlotList(String StudioId, String startTime,String endTime, int pageIndex, int pageSize ,String supplierId ,int status) {
        String url = ApiServiceUrlConfig.getReturnSlotList();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioId", StudioId);
        request.put("startTime", DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("endTime",  DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("arriveState", String.valueOf(status));
        request.put("pageIndex", String.valueOf(pageIndex));
        request.put("pageSize", String.valueOf(pageSize));
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 返货单签收
     * @param supplierId
     * @param id
     * @param userName
     * @return
     */
    public JSONObject ReceiveReturnSlot(String supplierId,String id, String userName ){
        String url = ApiServiceUrlConfig.getReceiveSlot();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("id", id);
        request.put("supplierUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 获取返货单详情
     * @param supplierId
     * @param id
     * @return
     */
    public JSONObject getReceivedSlotInfo(String supplierId,String id){
        String url = ApiServiceUrlConfig.getReceivedSlotInfo();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("id", id);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 扫描拣货
     * @param supplierId
     * @param id
     * @return
     */
    public JSONObject addProductFromScan(String supplierId,String id,String barcode,String userName){
        String url = ApiServiceUrlConfig.getAddProductFromScan();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("id", id);
        request.put("barcode", barcode);
        request.put("supplierUser", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 拣货结果确认
     * @param supplierId
     * @param id
     * @return
     */
    public JSONObject confirmSlotInfo(String supplierId,String id,String userName){
        String url = ApiServiceUrlConfig.getConfirmSlotInfo();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("id", id);
        request.put("supplierUser", id);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 残品信息添加
     * @param defectiveSpu
     * @return
     */
    public JSONObject SlotDefectiveSpu(SlotDefectiveSpu defectiveSpu){
        ResponseEntity<String> response  = restTemplate.postForEntity(ApiServiceUrlConfig.getAddDefective(), defectiveSpu, String.class);
        if(response.getStatusCode()== HttpStatus.OK){
            return JSONObject.parseObject(response.getBody());
        }
        return null;
    }

    /**
     * 残品列表
     * @param supplierId
     * @return
     */
    public JSONObject getDefectiveList(String supplierId,String StudioId, String startTime,String endTime){
        String url = ApiServiceUrlConfig.getDefectiveList();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("studioId", StudioId);
        request.put("startTime", DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        request.put("endTime",  DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd"));
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }

    /**
     * 删除残品信息
     * @param supplierId
     * @param id
     * @return
     */
    public JSONObject deleteDefective(String supplierId,String id,String userName){
        String url = ApiServiceUrlConfig.getDeleteDefective();
        Map<String, String> request = new HashMap<>();
        request.put("supplierId", supplierId);
        request.put("id", id);
        request.put("userName", userName);
        String json = HttpClientUtil.doPost(url, request);
        return JSONObject.parseObject(json);
    }
}
