package com.shangpin.api.airshop.supplier.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.api.airshop.service.PurOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.CreateDeliveryPurOrderReqDTO;
import com.shangpin.api.airshop.dto.DeliverAddress;
import com.shangpin.api.airshop.dto.DeliveryOrdersRQ;
import com.shangpin.api.airshop.dto.PurAccountRQ;
import com.shangpin.api.airshop.dto.PurImport;
import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.dto.SupplierInfoApi;
import com.shangpin.api.airshop.dto.base.ApiContentOne;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.request.CommonPurchaseRequest;
import com.shangpin.api.airshop.dto.request.PurchaseRequest;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.DateUtil;
import com.shangpin.common.utils.FastJsonUtil;
import com.sun.beans.TypeResolver;


/**请求供应商系统调用的方法
 * @author qinyingchun
 *
 */
@Service
public class SupplierService {

    private static Logger log = LoggerFactory.getLogger(SupplierService.class);

	/**
	 * 采购单查询列表<br/>
	 * @param request
	 * @return
	 */
	public String purchaseOrderList(PurchaseRequest request){
		String json = FastJsonUtil.serialize2StringEmpty(request);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getPurchaseListUri() , json);
	}

	/**
	 * 代销无库直发、非直发 采购单已取消列表查询接口
	 * @param request
	 * @return
	 */
	public String findPorderCanceledbyPage(PurchaseRequest request){
		String json = FastJsonUtil.serialize2StringEmpty(request);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorderCanceledbyPage() , json);
	}
	
	/**
	 * 采购单库存检查
	 * @param params [{"SopPurchaseOrderDetailNo":"001","IsStock":"1"},{"SopPurchaseOrderDetailNo":"002","IsStock":"2"}]
	 * @return
	 */
	public String checkStock(List<Map<String, String>> params, String type){
		String json = FastJsonUtil.serialize2String(params);
		if(StringUtils.isEmpty(type)){
			return HttpClientUtil.doPost(ApiServiceUrlConfig.getCheckStockUri(), json);
		}
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getStockUri(), json);
	}

	public String findPurOrderDetail(String purchaseOrderNo,String sopUserNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopPurchaseOrderNo", purchaseOrderNo);
		params.put("SopUserNo",sopUserNo);
		String json = FastJsonUtil.serialize2StringEmpty(params);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorder() ,json);
	}

	public String findDeliveryOrderDetail(String deliveryOrderNo,String sopUserNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopDeliveryOrderNo", deliveryOrderNo);
		params.put("SopUserNo",sopUserNo );
		String json = FastJsonUtil.serialize2StringEmpty(params);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindDorder() , json);
	}



	public String createDeliveryOrder(CreateDeliveryPurOrderReqDTO cdDTO) {
		String sopPurchaseOrderDetail=cdDTO.getSopPurchaseOrderDetailNo();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopUserNo", cdDTO.getSopUserNo());
		params.put("DateDeliver", cdDTO.getDateDeliver()==null?DateUtil.datetime19(new Date()):cdDTO.getDateDeliver());
		params.put("LogisticsName", cdDTO.getLogisticsName());
		params.put("LogisticsOrderNo", cdDTO.getLogisticsOrderNo());
		params.put("SopPurchaseOrderDetailNos",sopPurchaseOrderDetail==null?"":sopPurchaseOrderDetail.split("[,]"));
		String json = FastJsonUtil.serialize2StringEmpty(params);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getCreateDeliveryorder(), json);
	}
	
    /***
     * 封装请求发货单列表的数据
     * @param deliveryOrdersRQ
     * @return
     */
	public String deliveryOrderList(DeliveryOrdersRQ deliveryOrdersRQ) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopUserNo", deliveryOrdersRQ.getSopUserNo());
		params.put("LogisticsOrderNo", deliveryOrdersRQ.getLogisticsOrderNo());
		/*params.put("UpdateTimeBegin",DeliveryOrdersRQ.covertDate(deliveryOrdersRQ.getUpdateTimeBegin()));
		params.put("UpdateTimeEnd",DeliveryOrdersRQ.covertDate(deliveryOrdersRQ.getUpdateTimeEnd()));*/
		
		params.put("UpdateTimeBegin",DateFormat.TimeFormatChangeToString(deliveryOrdersRQ.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
		params.put("UpdateTimeEnd",DateFormat.TimeFormatChangeToString(deliveryOrdersRQ.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
		
		deliveryOrdersRQ.getDeliveryStatus().add("2");
		params.put("DeliveryStatus",deliveryOrdersRQ.getDeliveryStatus());
		params.put("SopDeliverOrderNo",deliveryOrdersRQ.getSopDeliverOrderNo());
		params.put("PageIndex",deliveryOrdersRQ.getStart());
		params.put("PageSize",deliveryOrdersRQ.getLength());
		String json = FastJsonUtil.serialize2StringEmpty(params);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getDeliverOrderListUri() , json);
	}
   /**
    * 结算列表
    * @param purAccountRQ
    * @return
    */
	public String sorderOrderList(PurAccountRQ purAccountRQ) {
		String json = FastJsonUtil.serialize2StringEmpty(purAccountRQ);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getSorderOrderListUri() , json);
	}
	/**
	 * 采购单库存标记数据导入<br/>
	 * @param request
	 */
	public String findPorderCheck(CommonPurchaseRequest request){
		String json = FastJsonUtil.serialize2StringEmpty(request);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorderCheckExportUri() , json);
	}
	/**
	 * 采购单明细数据导出<br/>
	 * @param purchase
	 */
	public String findPorderImport(CommonPurchaseRequest purchase) {
		String json = FastJsonUtil.serialize2StringEmpty(purchase);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorderExportUri() , json);
	}
    /**
     * 采购单库存标记数据导入
     * @param pList
     * @return
     */
	public String findPorderImport(List<PurImport> pList) {
		String json = FastJsonUtil.serialize2StringEmpty(pList);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorderImportUri() , json);
	}
    /**
     * 供应商已完结采购单导出
     * @param purAccountRQ
     * @return
     */
	public String findSorderExport(PurAccountRQ purAccountRQ) {
		String json = FastJsonUtil.serialize2StringEmpty(purAccountRQ);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindSorderExportUri() , json);
	}

	public String countGeneralTradeAndCrossBorder(PurchaseRequest purchase) {
		String json = FastJsonUtil.serialize2StringEmpty(purchase);
//		return HttpClientUtil.doPost(ApiServiceUrlConfig.getCountTradeAndCross() , json);
		return null;
	}

	public String getSupplierInfo(String sopUserNo) {
		String json = HttpClientUtil.doPost(ApiServiceUrlConfig.getFindSupplierInfo() , sopUserNo);
		
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}	
		String api = String.valueOf(jsonObject.get("messageRes"));
		JSONObject apiObject = JSON.parseObject(api);
		String apiJaon = apiObject.get("supplierInfoApi").toString();
		JSONObject aa = JSON.parseObject(apiJaon);
		return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(aa));
	}

	public String getReturnAddress(String SupplierNo) {
		
		String json = HttpClientUtil.doPost(ApiServiceUrlConfig.getFindReturnAddress() ,SupplierNo);
		
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}	
		if(jsonObject.get("messageRes")==null){
			return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.errorParam());
		}
		String api = String.valueOf(jsonObject.get("messageRes"));
		JSONArray apiObject = JSON.parseArray(api);
//		String apiJaon = apiObject.get("supplierInfoApi").toString();
//		List<SupplierInfoApi> apiObj = FastJsonUtil.fromJson(json, new TypeResolver(List<SupplierInfoApi>.class));//(apiObject, SupplierInfoApi.class);
		
		return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(apiObject));
	}

	public String getSupplierDeliverAddress(String sopUserNo) {

		String json = HttpClientUtil.doPost(ApiServiceUrlConfig.getFindSupplierDeliverAddress() , sopUserNo);
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}	
		String api = String.valueOf(jsonObject.get("messageRes"));
		DeliverAddress apiObj = FastJsonUtil.deserializeString2ObjectList(api, DeliverAddress.class).get(0);
		
		return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(apiObj));
		
	}

	/**
	 * 真直发 第一步 采购单查询确认库存列表
	 * @param purchaseRequest
	 * @return
	 */
	public String findDirectPorderbyPage(PurchaseRequest purchaseRequest) {
		String paramJson = FastJsonUtil.serialize2StringEmpty(purchaseRequest);
		String findDirectPorderbyPage = ApiServiceUrlConfig.getFindDirectPorderbyPage();
		log.info("【 真直发 第一步 采购列表接口 url {} | param {} 】 ",findDirectPorderbyPage,paramJson);
		String resultJson = HttpClientUtil.doPost( findDirectPorderbyPage, paramJson);
		log.info("【 真直发 第一步 采购列表接口 resultJson {} 】 ",resultJson);
		return resultJson;
	}

	/**
	 * 真直发 确认库存接口
	 * @param list
	 * @return
	 */
	public String modifyDirectPorderStock(List<Map<String, String>> list) {
		String json = FastJsonUtil.serialize2String(list);
		log.info("-- 库存变动入参 json {}",json);
		String resultJson = HttpClientUtil.doPost(ApiServiceUrlConfig.getModifyDirectPorderStock(), json);
		log.info("-- 库存变动 接口返回结果 resultJson {}",resultJson);
		return resultJson;
	}

	/**
	 * 直邮模式 第二步 采购单待发货列表
	 * @param purchaseRequest
	 * @return
	 */
	public String findDirectPorderStockedbyPage(PurchaseRequest purchaseRequest) {
		String paramJson = FastJsonUtil.serialize2StringEmpty(purchaseRequest);
		String findDirectPorderbyPage = ApiServiceUrlConfig.getFindDirectPorderStockedbyPage();
		log.info("【 真直发 第二步 采购单待发货列表 url {} | param {} 】 ",findDirectPorderbyPage,paramJson);
		String resultJson = HttpClientUtil.doPost( findDirectPorderbyPage, paramJson);
		log.info("【 真直发 第二步 采购单待发货列表 resultJson {} 】 ",resultJson);
		return resultJson;
	}


	/**
	 * 真直发 获取采购单 已发货页面数据接口
	 * @param map
	 * @return
	 */
	public String findDirectPorderTransInfo(Map<String, String> map) {
		String json = FastJsonUtil.serialize2StringEmpty(map);
        String findDirectPorderTransInfo = ApiServiceUrlConfig.getFindDirectPorderTransInfo();
        log.info("--- 请求真直发采购单 已发货页面数据接口 url {} | params {}",findDirectPorderTransInfo,json);
        String resultJson = HttpClientUtil.doPost(findDirectPorderTransInfo, json);
        log.info("--- 请求真直发采购单 已发货页面数据接口 resultJson {}",resultJson);
        return resultJson;
	}

	/**
	 * 保存 真直发 发货数据
	 * @param map
	 * @return
	 */
	public String createDirectDeliveryOrder(Map<String, Object> map) {
		String json = FastJsonUtil.serialize2StringEmpty(map);
		String createDirectDeliveryOrder = ApiServiceUrlConfig.getCreateDirectDeliveryOrder();
		log.info("--- 保存真直发采购单 发货页面数据接口 url {} | params {}",createDirectDeliveryOrder,json);
		String resultJson = HttpClientUtil.doPost(createDirectDeliveryOrder, json);
		log.info("--- 保存真直发采购单 发货页面数据接口 resultJson {}",resultJson);
		return resultJson;
	}

	/**
	 * 真直发 列表页请求接口
	 * @param deliveryOrdersRQ
	 * @return
	 */
	public String findDirectDorderbyPage(DeliveryOrdersRQ deliveryOrdersRQ) {
		Map<String, Object> params = new HashMap<>();
		params.put("SopUserNo", deliveryOrdersRQ.getSopUserNo());
		params.put("LogisticsOrderNo", deliveryOrdersRQ.getLogisticsOrderNo());
		params.put("UpdateTimeBegin",DateFormat.TimeFormatChangeToString(deliveryOrdersRQ.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
		params.put("UpdateTimeEnd",DateFormat.TimeFormatChangeToString(deliveryOrdersRQ.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));

		deliveryOrdersRQ.getDeliveryStatus().add("2");
		params.put("DeliveryStatus",deliveryOrdersRQ.getDeliveryStatus());
		params.put("SopDeliverOrderNo",deliveryOrdersRQ.getSopDeliverOrderNo());
		params.put("PageIndex",deliveryOrdersRQ.getStart());
		params.put("PageSize",deliveryOrdersRQ.getLength());
		params.put("SopPurchaseOrderNo", deliveryOrdersRQ.getSopPurchaseOrderNo());
		//params.put("BrandNo",deliveryOrdersRQ.getBrandNo());
		String json = FastJsonUtil.serialize2StringEmpty(params);
		String deliverOrderListUri =  ApiServiceUrlConfig.getFindDirectDorderbyPage();
		log.info("--- 真直发 发货数据列表 请求接口数据 url {} | params {}",deliverOrderListUri,json);
		String resultJson = HttpClientUtil.doPost(deliverOrderListUri, json);
		log.info("--- 真直发 发货数据列表 resultJson {}",resultJson);
		return resultJson;
	}

	/**
	 * 获取 国内段物流时用到的物流公司列表
	 * @param map
	 * @return
	 */
	public String findDirectSupplierLogisticsCompany(Map<String, Object> map) {
		String json = FastJsonUtil.serialize2StringEmpty(map);
		String createDirectDeliveryOrder = ApiServiceUrlConfig.getFindDirectSupplierLogisticsCompany();
		log.info("--- 真直发获取 国内段物流时用到的物流公司列表 请求接口 url： {} | params： {}",createDirectDeliveryOrder,json);
		String resultJson = HttpClientUtil.doPost(createDirectDeliveryOrder, json);
		log.info("--- 真直发获取 国内段物流时用到的物流公司列表接口 数据结果： resultJson： {}",resultJson);
		return resultJson;
	}

	/**
	 * 真直发采购单 录入国内段物流信息
	 * @param map
	 * @return
	 */
	public String entryDirectDorderDomesticLogistics(Map<String, Object> map) {
		String json = FastJsonUtil.serialize2StringEmpty(map);
		String createDirectDeliveryOrder = ApiServiceUrlConfig.getEntryDirectDorderDomesticLogistics();
		log.info("---  真直发采购单 录入国内段物流信息 请求接口 url： {} | params： {}",createDirectDeliveryOrder,json);
		String resultJson = HttpClientUtil.doPost(createDirectDeliveryOrder, json);
		log.info("---  真直发采购单 录入国内段物流信息 数据结果： resultJson： {}",resultJson);
		return resultJson;
	}

	/**
	 * 真直发 获取发货清单数据
	 * @param sopDeliverOrderNo 发货物流单号
	 * @param sopUserNo
	 * @return
	 */
	public String findDirectDorderDetail(String sopDeliverOrderNo,String sopUserNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopDeliverOrderNo", sopDeliverOrderNo);
		params.put("SopUserNo",sopUserNo );
		String json = FastJsonUtil.serialize2StringEmpty(params);
		String findDirectSopDeliveryOrderNoDetailUrl =  ApiServiceUrlConfig.getFindDirectDorderDetail();
		log.info("---  真直发采购单 获取发货清单 请求接口 url： {} | params： {}",findDirectSopDeliveryOrderNoDetailUrl,json);
		String resultJsonStr = HttpClientUtil.doPost(findDirectSopDeliveryOrderNoDetailUrl , json);
		log.info("---  真直发采购单 获取发货清单 数据结果： resultJsonStr： {}",resultJsonStr);
		return resultJsonStr;
	}

	/**
	 * 查询直发供应商物流段信息
	 * @param sopUserNo
	 * @param supplierNo
	 * @return
	 */
	public String findSupplierSegments(String sopUserNo, String supplierNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SupplierNo", supplierNo);
		params.put("SopUserNo",sopUserNo );
		String json = FastJsonUtil.serialize2StringEmpty(params);
		String findSupplierSegments =  ApiServiceUrlConfig.getFindSupplierSegments();
		log.info("---  真直发 查询直发供应商物流段信息 请求接口 url： {} | params： {}",findSupplierSegments,json);
		String resultJsonStr = HttpClientUtil.doPost(findSupplierSegments , json);
		log.info("---  真直发 查询直发供应商物流段信息 数据结果： resultJsonStr： {}",resultJsonStr);
		return resultJsonStr;
	}

	/**
	 * 真直发 获取发货清单数据
	 * @param purchaseOrderNo 采购单号
	 * @param sopUserNo
	 * @return
	 */
    public String findDirectPorderDetail(String purchaseOrderNo, String sopUserNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopPurchaseOrderNo", purchaseOrderNo);
		params.put("SopUserNo",sopUserNo );
		String json = FastJsonUtil.serialize2StringEmpty(params);
		String findDirectSopDeliveryOrderNoDetailUrl =  ApiServiceUrlConfig.getFindDirectPorderDetail();
		log.info("---  真直发采购单 获取发货清单详情 请求接口findSupplierSegments()  url： {} | params： {}",findDirectSopDeliveryOrderNoDetailUrl,json);
		String resultJsonStr = HttpClientUtil.doPost(findDirectSopDeliveryOrderNoDetailUrl , json);
		log.info("---  真直发采购单 获取发货清单详情 findDirectPorderDetail() 数据结果： resultJsonStr： {}",resultJsonStr);
		return resultJsonStr;
    }

	/**
	 * 查询供应 采购单品牌列表
	 * @param sopUserNo
	 * @return
	 */
	public String findAllBrand(String sopUserNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SopUserNo",sopUserNo );
		String json = FastJsonUtil.serialize2StringEmpty(params);
		String findDirectSopDeliveryOrderNoDetailUrl =  ApiServiceUrlConfig.getFindAllBrand();
		log.info("---   获取 供应商采购单品牌列表 findAllBrand()  url： {} | params： {}",findDirectSopDeliveryOrderNoDetailUrl,json);
		String resultJsonStr = HttpClientUtil.doPost(findDirectSopDeliveryOrderNoDetailUrl , json);
		log.info("---   获取 供应商采购单品牌列表 findAllBrand() 数据结果： resultJsonStr： {}",resultJsonStr);
		return resultJsonStr;
	}

	/**
	 * 真直发 查询取消 采购单列表
	 * @param purchase
	 * @return
	 */
    public String findDirectPorderCanceledbyPage(PurchaseRequest purchase) {
		String json = FastJsonUtil.serialize2StringEmpty(purchase);
		return HttpClientUtil.doPost(ApiServiceUrlConfig.getFindDirectPorderCanceledbyPage() , json);
    }


}
