package com.shangpin.api.airshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.*;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.api.airshop.util.HttpUtil45;
import com.shangpin.api.airshop.util.OutTimeConfig;
import com.shangpin.common.utils.FastJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TMSFZFService {
	private static Logger logger = LoggerFactory.getLogger(TMSFZFService.class);
	@Autowired
	private PurOrderService purOrderService;
	public String getTrackList(TMSTrack tmsTrack,String SupplierNo) {
		// TODO Auto-generated method stub
		String endTime = tmsTrack.getUpdateTimeEnd();
		String startTime = tmsTrack.getUpdateTimeBegin();
		
		try {
			if(endTime!=null&&!StringUtils.isEmpty(endTime)){
				endTime = endTime.split("-")[2] + "-" +endTime.split("-")[1] + "-" +endTime.split("-")[0] + " 23:59:59";	
				endTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime).getTime()+"";
				endTime = "/Date("+endTime+"+0800)/";
			}else{
				endTime = null;
			}
			if(startTime!=null&&!StringUtils.isEmpty(startTime)){
				startTime = startTime.split("-")[2] + "-" +startTime.split("-")[1] + "-" +startTime.split("-")[0] + " 00:00:00";
				startTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()+"";
				startTime = "/Date("+startTime+"+0800)/";
			}else{
				startTime = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("MasterTrackNo",tmsTrack.getMasterNo());
		params.put("SearchETime",endTime);
		params.put("SearchSTime",startTime);
		params.put("TaskBatchNo",tmsTrack.getTaskBatchNo());
		if(tmsTrack.getStatus().equals("5")){
			params.put("TaskStatus",null);
		}else{
			params.put("TaskStatus",tmsTrack.getStatus());
		}
		
		params.put("SupplierNo",SupplierNo);
		logger.info("查询批次传递的参数："+params.toString());
		//tian3
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getGetTrackFZF() , params);
		ReturnWayBillTask returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnWayBillTask.class);
		logger.info("查询批次返回的结果："+returnInOutBound.getSuccessed());
		if("true".equals(returnInOutBound.getSuccessed())){
			return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(returnInOutBound));
		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorRespWithApi(returnInOutBound.getMessage()));
		}
	}
	//创建批次
	public String createTrack(String supplierNo, String sopUserNo, String supplierOrders,String sopPurchaseNo,
							  String opUser) {

		supplierOrders = supplierOrders.replace(",","|");
		Map<String, String> params = new HashMap<String, String>();
		params.put("OpUser",opUser);
		params.put("SupplierOrderNos",supplierOrders);
		logger.info(supplierOrders+"TMSFZF创建批次传递参数:" + params);
		if(supplierOrders.isEmpty()){
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorResp(""));
		}
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getCreateTrackFZF() , params);
		logger.info(supplierOrders+"TMSFZF创建批次完成返回结果:" + result);
		ReturnCreateTask returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnCreateTask.class);
		if("true".equals(returnInOutBound.getSuccessed())){
			//tms创建物流成功，通知sop采购单状态为完成

//			String str = "{\"SupplierNo\":\""+supplierNo+"\",\"PurchaseOrderNos\":\""+sopPurchaseNo+"\"}";
//			logger.info("tms创建物流成功，通知sop采购单状态为完成传递的参数:" + str);
//			String sopResult = HttpClientUtil.doPost(ApiServiceUrlConfig.getModifyPurchaseOrderStatus() , str);
//			logger.info(supplierOrders+"修改采购单状态为完成返回结果:" + sopResult);
			return FastJsonUtil
					.serialize2String(ResponseContentOne.successResp(returnInOutBound.getWaybillTaskList().get(0).getTaskBatchNo()));
		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorResp(returnInOutBound.getMessage()));
		}
	}
	//根据批次号去查询批次明细和采购详情
	public String getTrackDetail(String taskBatchNo,String supplierNo,String importType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("CTaskBatchNo",taskBatchNo);

		String json = "{\"CTaskBatchNo\":\""+taskBatchNo+"\"}";
		logger.info(taskBatchNo+"查询批次明细传参："+json);
		String result = HttpUtil45.operateData("post","json", ApiServiceUrlConfig.getGetTrackDetailFZF(), new OutTimeConfig(1000*10*6,1000*10*6,1000*10*6), null, json, null,null);
		logger.info(taskBatchNo+"查询批次明细返回结果："+result);
//		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getGetTrackDetail() , json);
		ReturnWaybillTaskOrderDetail returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnWaybillTaskOrderDetail.class);
		if("true".equals(returnInOutBound.getSuccessed())){
			//根据批次号得到批次下所有的orderNo
			List<WaybillTaskOrderDetail> list = returnInOutBound.getWaybillTaskOrderDetailList();
			if(list!=null&&!list.isEmpty()){
				List<CBatchTaskDetail> taskList = new ArrayList<CBatchTaskDetail>();
				for(WaybillTaskOrderDetail way : list){
					//根据orderNo获取采购单信息
					String orderNo = way.getOrderNo();
					String supplierOrderNo = way.getSupplierOrderNO();
					CBatchTaskDetail detail = new CBatchTaskDetail();
					detail.setOrderNo(orderNo);
					detail.setQty(way.getOrderProductCount());
					String status = way.getIsRequestSuccess();
					if("0".equals(status)){
						detail.setStatus("nohandle");
					}else if("1".equals(status)||"2".equals(status)){
						detail.setStatus("success");
					}else{
						detail.setStatus("fail");
					}
					detail.setLogisticsOrderNo(way.getTrackNo());
					detail.setRequestTimes(way.getRequestTimes());
					String path = way.getPlaneSinglePath();
					if(path!=null&&!path.isEmpty()){
						path = way.getPlaneSinglePath().replace("\\","/");
						detail.setServiceUrl(ApiServiceUrlConfig.getIp()+"/wayprint/print?path="+path);
					}else{
						detail.setServiceUrl(null);
					}

					detail.setSupplierOrderNo(supplierOrderNo);
					String porderDetailResult = purOrderService.getCGDDetailByOrderNo(orderNo,supplierOrderNo,supplierNo);
					logger.info("根据supplierOrderNo:"+supplierOrderNo+"和orderNo:"+orderNo+"获取采购单信息返回结果："+porderDetailResult);
					JSONArray jsonObject = JSON.parseArray(porderDetailResult);
					if(jsonObject!=null&&!jsonObject.isEmpty()){
						PurchaseOrderDetail porderDetai = FastJsonUtil.deserializeString2Obj(jsonObject.get(0).toString(),PurchaseOrderDetail.class);
						if(porderDetai!=null){
							//根据supplierSkuNo和supplierId获取原始货号
							String jsonProduct = null;
							try {
								jsonProduct = product(supplierNo, URLEncoder.encode(porderDetai.getSupplierSkuNo(),"UTF-8"));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(StringUtils.isEmpty(json) || "[]".equals(json)){
								return null;
							}
							List<Product> products = FastJsonUtil.deserializeString2ObjectList(jsonProduct, Product.class);
							if(products!=null&&products.size()>0){
								detail.setProductModel(products.get(0).getProductCode());
							}
							detail.setBrandName(porderDetai.getBrandNo());

							detail.setProductName(porderDetai.getProductName());

							detail.setSopPurchaseOrderNo(porderDetai.getSopPurchaseOrderNo());
							detail.setSupplierSkuNo(porderDetai.getSupplierSkuNo());
							detail.setSkuPrice(porderDetai.getSkuPrice());
							detail.setImportType(importType);
							if(importType!=null&&"3".equals(importType)){
								detail.setServiceUrl(ApiServiceUrlConfig.getIp()+"/wayprint/printDeliveryNo?deliveryNo="+way.getTrackNo());
							}

						}
					}
					taskList.add(detail);
				}
				return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(taskList));
			}else{
				return FastJsonUtil
						.serialize2String(ResponseContentOne.errorParam());
			}

		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorRespWithApi(returnInOutBound.getMessage()));
		}
	}
	//补充信息查询调用
	public String product(String supplierNo, String supplierSkuNo){
		try{
			String url=ApiServiceUrlConfig.getProductUri();
			Map<String,String> request = new HashMap<>();
			request.put("supplierId",supplierNo);
			request.put("skuId",supplierSkuNo);
			return HttpClientUtil.doPost(url, request);// String.class);
		}catch(Exception e){

		}
		return null;
	}
}
