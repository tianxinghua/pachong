package com.shangpin.api.airshop.service;


import java.util.List;

import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.*;
import com.shangpin.api.airshop.dto.direct.DirectDeliverOrder;
import com.shangpin.api.airshop.dto.direct.DirectDeliveryOrderRS;
import com.shangpin.api.airshop.dto.direct.SupplierSegments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shangpin.api.airshop.dto.base.ApiContentOne;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.common.utils.FastJsonUtil;
/**
 * Date:    2016年1月14日 <br/>
 * @author   wh
 * @since    JDK 7
 */
@Service
public class FindOrderService{	
	private static Logger logger = LoggerFactory.getLogger(FindOrderService.class);
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ProductService productService;
	/**
	 * 发货单列表页
	 * 
	 * @param finddorderRQ
	 * @return
	 */
	public String findOrderList(DeliveryOrdersRQ deliveryOrdersRQ) {
		if (StringUtils.isEmpty(deliveryOrdersRQ.getUpdateTimeBegin())||StringUtils.isEmpty(deliveryOrdersRQ.getUpdateTimeEnd())) {
			deliveryOrdersRQ.setUpdateTimeBegin("");
			deliveryOrdersRQ.setUpdateTimeEnd("");
		}
		// call Api
		String json = supplierService.deliveryOrderList(deliveryOrdersRQ);
		if (StringUtils.isEmpty(json)) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Api返回数据为空"));
		}
		JSONObject jsoobj = JSON.parseObject(json);
		
		if (jsoobj == null) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Api转换数据错误"));
		}
		if (Constants.SUCCESS.equals(jsoobj.getString("resCode"))) {
			String hs = jsoobj.getString("messageRes");
            if(hs==null){
                return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Api返回数据为空"));
            }
			DeliveryOrderRS dOrderRS = FastJsonUtil.deserializeString2Obj(hs, DeliveryOrderRS.class);
			for (DeliveryOrdersContent dContent : dOrderRS.getDeliverOrders()) {
				dContent.setDateDeliver(DateFormat.TimeFormatChangeToString(dContent.getDateDeliver(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss") );
			}
			DeliveryOrderRS dOrders = dOrderRS;
			ResponseContentOne<DeliveryOrderRS> result = ResponseContentOne.successResp(dOrders);
			return FastJsonUtil.serialize2StringEmpty(result);
		} else {
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorRespWithApi(jsoobj.getString("resCode")));
		}
	}


	/**
	 * 真直发发货单 列表页
	 * @param deliveryOrdersRQ 请求参数
	 * @return
	 */
	public ResponseContentOne<DirectDeliveryOrderRS> findDirectDorderbyPage(DeliveryOrdersRQ deliveryOrdersRQ) {
		if (StringUtils.isEmpty(deliveryOrdersRQ.getUpdateTimeBegin())||StringUtils.isEmpty(deliveryOrdersRQ.getUpdateTimeEnd())) {
			deliveryOrdersRQ.setUpdateTimeBegin("");
			deliveryOrdersRQ.setUpdateTimeEnd("");
		}
		// call Api
		String json = supplierService.findDirectDorderbyPage(deliveryOrdersRQ);
		if (StringUtils.isEmpty(json)) {
			return ResponseContentOne.errorResp("Api返回数据为空");
		}
		JSONObject jsoobj = JSON.parseObject(json);

		if (jsoobj == null) {
			return ResponseContentOne.errorResp("Api转换数据错误");
		}
		if (Constants.SUCCESS.equals(jsoobj.getString("resCode"))) {
			String hs = jsoobj.getString("messageRes");
            if(hs==null){
                return ResponseContentOne.errorResp("Api返回数据为空");
            }
			DirectDeliveryOrderRS dOrderRS = FastJsonUtil.deserializeString2Obj(hs, DirectDeliveryOrderRS.class);

			ResponseContentOne<DirectDeliveryOrderRS> result = ResponseContentOne.successResp(dOrderRS);
			return result;
		} else {
			return ResponseContentOne.errorRespWithApi(jsoobj.getString("resCode"));
		}
	}

	public ResponseContentOne<PurchaseOrders> findDeliveryOrderDetail(
			String deliveryOrderNo,String sopUserNo,String supplierNo) {
		if(StringUtils.isEmpty(deliveryOrderNo)||StringUtils.isEmpty(sopUserNo)){
			return ResponseContentOne.errorParam();
		}
		String json = supplierService.findDeliveryOrderDetail(deliveryOrderNo,sopUserNo);
		JSONObject jsonObject = JSON.parseObject(json);
		logger.debug("findDeliveryOrderDetail json:{}",json);
		if (Constants.SUCCESS.equals(jsonObject.get("resCode").toString())) {
            Object messageRes = jsonObject.get("messageRes");
            if(messageRes==null){
                return ResponseContentOne.errorResp("Return the data is empty");
            }
            String pus = messageRes.toString();
			PurchaseOrders purchaseOrders= FastJsonUtil.deserializeString2Obj(pus, PurchaseOrders.class);
			PurchaseOrders pOrders = productService.convert(purchaseOrders, sopUserNo);
			if(pOrders==null){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			List<PurchaseOrderDetail> purchaseOrderDetails = pOrders.getPurchaseOrderDetails();
			if(purchaseOrders==null||purchaseOrderDetails==null||purchaseOrderDetails.size()<1){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			
			return ResponseContentOne.successResp(pOrders);
		}
		return ResponseContentOne.errorRespWithApi(jsonObject.get("resCode1").toString());
	}

	/**
	 * 真直发 获取发货 商品清单信息
	 * @param sopDeliverOrderNo 发货单号
	 * @param sopUserNo
	 * @param supplierNo
	 * @return
	 */
	public ResponseContentOne<PurchaseOrderDetail> findDirectDorderDetail(
			String sopDeliverOrderNo,String sopUserNo,String supplierNo) {
		if(StringUtils.isEmpty(sopDeliverOrderNo)||StringUtils.isEmpty(sopUserNo)){
			return ResponseContentOne.errorParam();
		}
		String json = supplierService.findDirectDorderDetail(sopDeliverOrderNo,sopUserNo);
		JSONObject jsonObject = JSON.parseObject(json);
		logger.debug(" findDirectDorderDetail json:{}",json);
		if (Constants.SUCCESS.equals(jsonObject.get("resCode").toString())) {
            Object messageRes = jsonObject.get("messageRes");
            if(messageRes==null){
                return ResponseContentOne.errorResp("Return the data is empty");
            }
            String pus = messageRes.toString();
			PurchaseOrderDetail purchaseOrderDetail= FastJsonUtil.deserializeString2Obj(pus, PurchaseOrderDetail.class);
			if(purchaseOrderDetail==null){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			return ResponseContentOne.successResp(purchaseOrderDetail);
		}
		return ResponseContentOne.errorRespWithApi(jsonObject.get("resCode").toString());
	}

	/**
	 * 查询直发供应商物流段信息
	 * @param user 当前登录供应商信息
	 * @return
	 */
	public ResponseContentOne<SupplierSegments> findSupplierSegments(UserInfo user) {
		if(StringUtils.isEmpty(user.getSopUserNo())||StringUtils.isEmpty(user.getSupplierNo())){
			return ResponseContentOne.errorParam();
		}
		String json = supplierService.findSupplierSegments(user.getSopUserNo(),user.getSupplierNo());
		JSONObject jsonObject = JSON.parseObject(json);
		logger.info(" findSupplierSegments json:{}",json);
		if (Constants.SUCCESS.equals(jsonObject.get("resCode").toString())) {
            Object messageRes = jsonObject.get("messageRes");
            if(messageRes==null){
                return ResponseContentOne.errorResp("Return the data is empty");
            }
			String pus = messageRes.toString();
			SupplierSegments supplierSegments= FastJsonUtil.deserializeString2Obj(pus, SupplierSegments.class);
			if(supplierSegments==null){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			return ResponseContentOne.successResp(supplierSegments);
		}
		return ResponseContentOne.errorRespWithApi(jsonObject.get("resCode").toString());
	}

	/**
	 * 真直发 获取发货 商品清单信息
	 * @param purchaseOrderNo 采购单号
	 * @param sopUserNo
	 * @return
	 */
	public ResponseContentOne findDirectPorderDetail(String purchaseOrderNo, String sopUserNo) {
		if(StringUtils.isEmpty(purchaseOrderNo)||StringUtils.isEmpty(sopUserNo)){
			return ResponseContentOne.errorParam();
		}
		String json = supplierService.findDirectPorderDetail(purchaseOrderNo,sopUserNo);
		JSONObject jsonObject = JSON.parseObject(json);
		logger.debug(" findDirectPorderDetail json:{}",json);
		if (Constants.SUCCESS.equals(jsonObject.get("resCode").toString())) {
            Object messageRes = jsonObject.get("messageRes");
            if(messageRes==null){
                return ResponseContentOne.errorResp("Return the data is empty");
            }
            String pus = messageRes.toString();
			PurchaseOrderDetail purchaseOrderDetail= FastJsonUtil.deserializeString2Obj(pus, PurchaseOrderDetail.class);
			if(purchaseOrderDetail==null){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			return ResponseContentOne.successResp(purchaseOrderDetail);
		}
		return ResponseContentOne.errorRespWithApi(jsonObject.get("resCode").toString());
	}

	public static void main(String[] args) {
		String json="{\"resCode\":200,\"isSuccess\":true,\"messageRes\":{\"sopPurchaseOrderDetailNo\":2018101200062,\"sopPurchaseOrderNo\":\"CGD2018101200037\",\"skuNo\":\"30670651001\",\"pid\":\"PID4003998043\",\"productName\":\"zbj测试海外供应商直发 红色 20*20\",\"supplierSkuNo\":\"456145646\",\"supplierOrderNo\":\"2018101289010\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"201710161828\",\"barCode\":\"4654564654\",\"picUrl\":\"http://192.168.9.73/f/p/00/00/00/20171016183307258286-0-0.jpg\",\"warehouseNo\":\"S0000889V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":500.00,\"skuPriceCurrency\":0,\"currencyName\":\"CNY\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":0,\"brandNo\":\"B0797\",\"brandName\":\"MO&Co.\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201810121697065\",\"createTime\":\"2018-10-12 19:22:05\",\"dateArrival\":null,\"dateCanceled\":\"2018-10-12 19:22:05\",\"latestConfirmTime\":\"2018-12-04 23:59:59\",\"deliveryBefore\":\"2018-11-28 23:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"sopProductNo\":448904}}";
		JSONObject jsonObject = JSON.parseObject(json);
		logger.debug(" findDirectPorderDetail json:{}",json);
		if (Constants.SUCCESS.equals(jsonObject.get("resCode").toString())) {
			String pus = jsonObject.get("messageRes").toString();
			PurchaseOrderDetail purchaseOrderDetail= FastJsonUtil.deserializeString2Obj(pus, PurchaseOrderDetail.class);
			if(purchaseOrderDetail==null){
				ResponseContentOne.errorResp("Return the data is empty");
			}
			 ResponseContentOne.successResp(purchaseOrderDetail);
		}
	}
}
