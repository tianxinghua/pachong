package com.shangpin.api.airshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.*;
import com.shangpin.api.airshop.dto.base.*;
import com.shangpin.api.airshop.dto.direct.DirtectDeliveryPurchaseInfoForSendDto;
import com.shangpin.api.airshop.dto.direct.LogisticsCompanyDto;
import com.shangpin.api.airshop.dto.request.CommonPurchaseRequest;
import com.shangpin.api.airshop.dto.request.PurchaseRequest;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.FastJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 2016年1月12日 <br/>
 * 
 * @author 陈小峰
 * @since JDK 7
 */
@Service
public class PurOrderService extends BaseService {

	private ObjectMapper mapper = null;
	{
		mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	private static Logger logger = LoggerFactory.getLogger(PurOrderService.class);
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ProductService productService;
	@Autowired
	private  RestTemplate restTemplate;

	public PurchaseOrders list(PurchaseRequest purchaseRequest) {
		String json = supplierService.purchaseOrderList(purchaseRequest);
		System.out.println(json);
		logger.info("list() json {}",json);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}
		ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurchaseOrders purchaseOrders = requestContent.getMessageRes();
			return productService.convert(purchaseOrders, purchaseRequest.getSopUserNo());
			
		}
		return null;
	}

	/**
	 * 代销无库直发、非直发 采购单已取消列表查询接口
	 * @param purchaseRequest
	 * @return
	 */
	public PurchaseOrders findPorderCanceledbyPage(PurchaseRequest purchaseRequest) {
		String json = supplierService.findPorderCanceledbyPage(purchaseRequest);
		logger.info("findPorderCanceledbyPage() result {}",json);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}
		ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurchaseOrders purchaseOrders = requestContent.getMessageRes();
			return productService.convert(purchaseOrders, purchaseRequest.getSopUserNo());
		}
		return null;
	}

	public static void main(String[] args) {

		String json = "{\"resCode\":200,\"isSuccess\":true,\"messageRes\":{\"total\":13,\"purchaseOrderDetails\":[{\"sopPurchaseOrderDetailNo\":2018120100215,\"sopPurchaseOrderNo\":\"CGD2018120100074\",\"skuNo\":\"31153772001\",\"pid\":\"PID4005804004\",\"productName\":\"精华\",\"supplierSkuNo\":\"516925-U\",\"supplierOrderNo\":\"2018120161151\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"516925\",\"barCode\":\"516925-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":81.66,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012148320\",\"createTime\":\"2018-12-01 13:18:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 13:18:29\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578458},{\"sopPurchaseOrderDetailNo\":2018120100214,\"sopPurchaseOrderNo\":\"CGD2018120100073\",\"skuNo\":\"31153780001\",\"pid\":\"PID4005804003\",\"productName\":\"面膜\",\"supplierSkuNo\":\"338329-U\",\"supplierOrderNo\":\"2018120161150\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"338329\",\"barCode\":\"338329-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":121.35,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012148320\",\"createTime\":\"2018-12-01 13:18:28\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 13:18:28\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578466},{\"sopPurchaseOrderDetailNo\":2018120100212,\"sopPurchaseOrderNo\":\"CGD2018120100072\",\"skuNo\":\"31153999001\",\"pid\":\"PID4005804001\",\"productName\":\"妆前乳\",\"supplierSkuNo\":\"1096032-U\",\"supplierOrderNo\":\"2018120161149\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"1096032\",\"barCode\":\"1096032-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":63.84,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012148317\",\"createTime\":\"2018-12-01 13:16:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 13:16:29\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578684},{\"sopPurchaseOrderDetailNo\":2018120100211,\"sopPurchaseOrderNo\":\"CGD2018120100071\",\"skuNo\":\"31154088001\",\"pid\":\"PID4005804000\",\"productName\":\"粉底液/霜\",\"supplierSkuNo\":\"576072-U\",\"supplierOrderNo\":\"2018120161148\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"576072\",\"barCode\":\"576072-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":60.60,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012148317\",\"createTime\":\"2018-12-01 13:16:28\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 13:16:28\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578773},{\"sopPurchaseOrderDetailNo\":2018120100179,\"sopPurchaseOrderNo\":\"CGD2018120100065\",\"skuNo\":\"31154020001\",\"pid\":\"PID4005803968\",\"productName\":\"妆前乳\",\"supplierSkuNo\":\"429861-U\",\"supplierOrderNo\":\"2018120161117\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"429861\",\"barCode\":\"429861-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":56.55,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04598\",\"brandName\":\"HOURGLASS\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012147439\",\"createTime\":\"2018-12-01 11:33:30\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 11:33:30\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578705},{\"sopPurchaseOrderDetailNo\":2018120100178,\"sopPurchaseOrderNo\":\"CGD2018120100064\",\"skuNo\":\"31154184001\",\"pid\":\"PID4005803967\",\"productName\":\"睫毛膏\",\"supplierSkuNo\":\"885944-U\",\"supplierOrderNo\":\"2018120161116\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"885944\",\"barCode\":\"885944-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":37.92,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04598\",\"brandName\":\"HOURGLASS\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012147439\",\"createTime\":\"2018-12-01 11:33:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 11:33:29\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578869},{\"sopPurchaseOrderDetailNo\":2018120100177,\"sopPurchaseOrderNo\":\"CGD2018120100063\",\"skuNo\":\"31154170001\",\"pid\":\"PID4005803966\",\"productName\":\"睫毛膏\",\"supplierSkuNo\":\"1096908-U\",\"supplierOrderNo\":\"2018120161115\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"1096908\",\"barCode\":\"1096908-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":32.25,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04598\",\"brandName\":\"HOURGLASS\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201812012147439\",\"createTime\":\"2018-12-01 11:32:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-12-01 11:32:29\",\"latestConfirmTime\":\"2019-01-06 07:59:59\",\"deliveryBefore\":\"2019-01-06 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578855},{\"sopPurchaseOrderDetailNo\":2018112600455,\"sopPurchaseOrderNo\":\"CGD2018112600240\",\"skuNo\":\"31154037001\",\"pid\":\"PID4005801322\",\"productName\":\"粉饼\",\"supplierSkuNo\":\"487411-U\",\"supplierOrderNo\":\"2018112658943\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"487411\",\"barCode\":\"487411-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":56.52,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04598\",\"brandName\":\"HOURGLASS\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811262135169\",\"createTime\":\"2018-11-26 13:31:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-26 13:31:29\",\"latestConfirmTime\":\"2019-01-01 07:59:59\",\"deliveryBefore\":\"2019-01-01 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578722},{\"sopPurchaseOrderDetailNo\":2018112501053,\"sopPurchaseOrderNo\":\"CGD2018112500492\",\"skuNo\":\"31154211001\",\"pid\":\"PID4005798859\",\"productName\":\"眼线笔/膏/液\",\"supplierSkuNo\":\"616216-U\",\"supplierOrderNo\":\"2018112557896\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"616216\",\"barCode\":\"616216-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":34.26,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811252132754\",\"createTime\":\"2018-11-25 18:38:28\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-25 18:38:28\",\"latestConfirmTime\":\"2018-12-30 07:59:59\",\"deliveryBefore\":\"2018-12-30 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578896},{\"sopPurchaseOrderDetailNo\":2018112500004,\"sopPurchaseOrderNo\":\"CGD2018112500001\",\"skuNo\":\"31154170001\",\"pid\":\"PID4005797810\",\"productName\":\"睫毛膏\",\"supplierSkuNo\":\"1096908-U\",\"supplierOrderNo\":\"2018112556897\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"1096908\",\"barCode\":\"1096908-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":31.88,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04598\",\"brandName\":\"HOURGLASS\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811252130362\",\"createTime\":\"2018-11-25 00:05:28\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-25 00:05:28\",\"latestConfirmTime\":\"2018-12-30 07:59:59\",\"deliveryBefore\":\"2018-12-30 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578855},{\"sopPurchaseOrderDetailNo\":2018112400640,\"sopPurchaseOrderNo\":\"CGD2018112400311\",\"skuNo\":\"31153920001\",\"pid\":\"PID4005797193\",\"productName\":\"唇膏\",\"supplierSkuNo\":\"664631-U\",\"supplierOrderNo\":\"2018112456256\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"664631\",\"barCode\":\"664631-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":67.65,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04597\",\"brandName\":\"CHRISTIAN LOUBOUTIN BEAUTY\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811242128888\",\"createTime\":\"2018-11-24 13:48:28\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-24 13:48:28\",\"latestConfirmTime\":\"2018-12-30 07:59:59\",\"deliveryBefore\":\"2018-12-30 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578605},{\"sopPurchaseOrderDetailNo\":2018112400294,\"sopPurchaseOrderNo\":\"CGD2018112400139\",\"skuNo\":\"31154128001\",\"pid\":\"PID4005796847\",\"productName\":\"遮瑕\",\"supplierSkuNo\":\"523233-U\",\"supplierOrderNo\":\"2018112455876\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"523233\",\"barCode\":\"523233-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":45.39,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811242128078\",\"createTime\":\"2018-11-24 09:37:30\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-24 09:37:30\",\"latestConfirmTime\":\"2018-12-30 07:59:59\",\"deliveryBefore\":\"2018-12-30 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578813},{\"sopPurchaseOrderDetailNo\":2018112300615,\"sopPurchaseOrderNo\":\"CGD2018112300261\",\"skuNo\":\"31153929001\",\"pid\":\"PID4005795608\",\"productName\":\"唇膏\",\"supplierSkuNo\":\"682511-U\",\"supplierOrderNo\":\"2018112354549\",\"detailStatus\":1,\"detailStatusName\":\"待处理\",\"productModel\":\"682511\",\"barCode\":\"682511-U\",\"picUrl\":null,\"warehouseNo\":\"S0001014V01\",\"warehouseName\":null,\"warehouseAddress\":null,\"warehousePost\":null,\"warehouseContactPerson\":null,\"warehouseContactMobile\":null,\"skuPrice\":35.85,\"skuPriceCurrency\":3,\"currencyName\":\"GBP\",\"sopDeliverOrderNo\":0,\"logisticsName\":null,\"logisticsOrderNo\":null,\"isStock\":1,\"brandNo\":\"B04596\",\"brandName\":\"CHANTECAILLE\",\"qty\":1,\"checkStock\":0,\"memo\":null,\"sopUserNo\":0,\"orderNo\":\"201811232124889\",\"createTime\":\"2018-11-23 10:55:29\",\"dateArrival\":null,\"dateCanceled\":\"2018-11-23 10:55:29\",\"latestConfirmTime\":\"2018-12-29 07:59:59\",\"deliveryBefore\":\"2018-12-29 07:59:59\",\"importType\":0,\"isPrint\":0,\"serviceRate\":0.00,\"perquisite\":0.00,\"orderFromNo\":\"SP\",\"mvpFlag\":2,\"sopProductNo\":1578614}]},\"messageResEN\":null}";

		logger.info("【cancel findDirectPorderCanceledbyPage() resultJson {} 】",json);
		if(StringUtils.isEmpty(json)){
			return ;
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ApiContentOne<PurchaseOrders> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<PurchaseOrders>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mapper.readValue()

		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return ;
		}
//		ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurchaseOrders purchaseOrders = requestContent.getMessageRes();
			System.out.println("testes");
		}

	}

    /**
     * 直邮模式 第一步列表接口
     * @param purchaseRequest
     * @return
     */
	public ResponseContentOne<PurchaseOrders> findDirectPorderbyPage(PurchaseRequest purchaseRequest) {
		String json = supplierService.findDirectPorderbyPage(purchaseRequest);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("");
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return ResponseContentOne.errorResp(String.valueOf(jsonObject.get("MessageRes")));
		}
//		ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		ApiContentOne<PurchaseOrders> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<PurchaseOrders>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (requestContent!=null&&Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurchaseOrders purchaseOrders = requestContent.getMessageRes();
			PurchaseOrders convert = productService.convert(purchaseOrders, purchaseRequest.getSopUserNo());
			return ResponseContentOne.successResp(convert);
		}
		return null;
	}

	public ResponseContentOne<DeliveryOrderDetailResDTO> createDeliveryOrder(CreateDeliveryPurOrderReqDTO doDTO) {
		String json = supplierService.createDeliveryOrder(doDTO);
		//String json = "{\"MessageRes\":\"2015040800001\",\"responseCode\":200,\"IsSuccess\":\"true\"}";
		logger.debug("createDeliveryOrder json:{}",json);
		@SuppressWarnings("rawtypes")
		ApiContentOne responseMessageRes = FastJsonUtil.deserializeString2Obj(json, ApiContentOne.class);
		if (Constants.SUCCESS.equals(responseMessageRes.getResCode())) {
			DeliveryOrderDetailResDTO deliveryOrderDetailResDTO = new DeliveryOrderDetailResDTO();
			deliveryOrderDetailResDTO.setDeliveryPurOrderNo(responseMessageRes.getMessageRes().toString());
			return ResponseContentOne.successResp(deliveryOrderDetailResDTO);
		}
		return ResponseContentOne.errorRespWithApi(responseMessageRes.getResCode());
	}
	

	/**
	 * 检查采购单库存数量
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResponseContentOne checkStock(List<Map<String, String>> params, String type){
		String json = supplierService.checkStock(params, type);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		
	
		ApiContentBase request = FastJsonUtil.deserializeString2Obj(json, ApiContentBase.class);
		if(Constants.SUCCESS.equals(request.getResCode())){
			return ResponseContentOne.successResp(null);
		}
		JSONObject jsonObject = JSON.parseObject(json);
		
		String code = String.valueOf(jsonObject.get("ResCode"));
		if(!Constants.SUCCESS.equals(code)){
			String msg = jsonObject.getString("MessageRes");
			return ResponseContentOne.errorResp(msg);
		}
		return null;
	}


	/**
	 * 检查采购单库存数量
	 * @param list 采购单号标志位、供应商用户id
	 * @return
	 */
	public ResponseContentOne modifyDirectPorderStock(List<Map<String, String>> list){
		String json = supplierService.modifyDirectPorderStock(list);

		if(StringUtils.isEmpty(json)){
			return null;
		}
		ApiContentBase request = FastJsonUtil.deserializeString2Obj(json, ApiContentBase.class);
		if(Constants.SUCCESS.equals(request.getResCode())){
			return ResponseContentOne.successResp(null);
		}
		JSONObject jsonObject = JSON.parseObject(json);

		String code = String.valueOf(jsonObject.get("ResCode"));
		if(!Constants.SUCCESS.equals(code)){
			String msg = jsonObject.getString("MessageRes");
			return ResponseContentOne.errorResp(msg);
		}
		return null;
	}
    
	public ResponseContentOne<PurchaseOrderDetailForGather> findPurOrderDetails(String purchaseOrderNo,String sopUserNo ,String supplierNo) {
		if(StringUtils.isEmpty(purchaseOrderNo)||StringUtils.isEmpty(sopUserNo)){
			return ResponseContentOne.errorParam();
		}
		String json = supplierService.findPurOrderDetail(purchaseOrderNo,sopUserNo);
		logger.debug("findDeliveryOrderDetail json:{}",json);
	//	String json = "{\"MessageRes\":[{\"Total\":205,\"PurchaseOrderDetails\":[{\"SopPurchaseOrderNo\":\"CGD20150507000000001\",\"SopPurchaseOrderDetailNo\":\"2015050700001\",\"SkuNo\":\" 30002958001\",\"ProductModel\":\"MHA13118\",\"SupplierSkuNo\":\"MHA13118-3\",\"BarCode\":\"MHA13118-3\",\"PicUrl\":\"http://pic4.shangpin.com/f/p/00/00/00/20160107213514842679-0-0.jpg\",\"DetailStatus\":1,\"SopDeliverOrderNo\":\" 2015050700008\",\"LogisticsName\":\" DHL\",\"LogisticsOrderNo\":\" 4015050700009\",\"IsStock\":1,\"DateStart\":\" 2015-05-07 09:37:25.023\",\"DateEnd\":\" 2015-05-22 09:36:49.367\",\"SkuPrice\":\"100.00\",\"SkuPriceCurrency\":\"人民币\",\"WarehouseNo\":\"B\",\"WarehouseName\":\"北京代销实体仓\",\"WarehouseAddress\":\" 北京市通州区马驹桥物流基地兴贸一街 11号华润物流园区5号库\",\"WarehousePost\":\"101012\",\"WarehouseContactPerson\":\"收货组\",\"WarehouseContactMobile\":\"80849285\"}] }],\"responseCode\":200,\"IsSuccess\":\"true\"}";
		ApiContentOne<PurchaseOrders> requestContent = (ApiContentOne<PurchaseOrders>) FastJsonUtil.fromJson(json,new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		PurchaseOrderDetailForGather purchaseOrderDetailForGather=new PurchaseOrderDetailForGather();
	
		if (Constants.SUCCESS.equals(requestContent.getResCode().toString())) {
			PurchaseOrders purchaseOrders=requestContent.getMessageRes();
			PurchaseOrders purchaseOrdersConvert=productService.convert(purchaseOrders, sopUserNo);
			if(purchaseOrdersConvert==null){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
			List<PurchaseOrderDetail> purchaseOrderDetailList=purchaseOrdersConvert.getPurchaseOrderDetails();
			if(purchaseOrders==null||purchaseOrderDetailList==null||purchaseOrderDetailList.size()<1){
				return ResponseContentOne.errorResp("Return the data is empty");
			}
		
			List<PurOrderSkuDetail> skulist=new ArrayList<PurOrderSkuDetail>();
			for(int i=0;i<purchaseOrderDetailList.size();i++){
				PurchaseOrderDetail purchaseOrderDetail=purchaseOrderDetailList.get(i);
				PurOrderSkuDetail purOrderSkuDetail=new PurOrderSkuDetail();
				BeanUtils.copyProperties(purchaseOrderDetail, purOrderSkuDetail);
				skulist.add(purOrderSkuDetail);
			}
			BeanUtils.copyProperties(purchaseOrderDetailList.get(0), purchaseOrderDetailForGather);
			purchaseOrderDetailForGather.setSkuList(skulist);
			return ResponseContentOne.successResp(purchaseOrderDetailForGather);
		}
		return ResponseContentOne.errorRespWithApi(requestContent.getResCode());
			
	}
	public JSONObject findPurOrderDetail(String purchaseOrderNo,String sopUserNo ,String supplierNo) {
		JSONObject param = new JSONObject();
		param.put("SopPurchaseOrderNo", purchaseOrderNo);
		param.put("SopUserNo",sopUserNo);
		String paramStr = param.toJSONString();
		String findPorder = ApiServiceUrlConfig.getFindPorder();
		logger.info("--- 发货商品清单  请求接口数据 url： {} | params： {}",findPorder,paramStr);
		String skuStockResult= restTemplate.postForObject(findPorder,getHttpPostData(paramStr), String.class);
		logger.info("--- 发货商品清单 接口请求结果 skuStockResult： {} ",skuStockResult);
		JSONObject jsonResult= JSONObject.parseObject(skuStockResult);
		JSONObject result= new JSONObject();
		result.put("code","0");
		result.put("message","success");
		result.put("content",new Object());
		if (jsonResult.getBooleanValue("isSuccess")&&jsonResult.getJSONObject("messageRes")!=null) {
			JSONArray purchaseList=jsonResult.getJSONObject("messageRes").getJSONArray("purchaseOrderDetails");
			if (purchaseList==null) {
				result.put("code","1");
				result.put("message","No PurchaseOrderDetails");
				return result;
			}
			JSONObject JSONContent = new JSONObject();
			if (purchaseList.size()==0) {
				result.put("content", JSONContent); 
			}
			JSONContent.put("createTime", DateFormat.TimeFormatChangeToString(purchaseList.getJSONObject(0).getString("createTime"),"yyyy-MM-dd HH:mm:ss","dd-MM-yyyy HH:mm:ss")  );
			JSONContent.put("logisticsName", "");
			JSONContent.put("logisticsOrderNo", "");
			JSONContent.put("sopPurchaseOrderDetailNo", purchaseList.getJSONObject(0).getString("sopPurchaseOrderDetailNo"));
			JSONContent.put("sopPurchaseOrderNo", purchaseList.getJSONObject(0).getString("sopPurchaseOrderNo"));
			JSONContent.put("warehouseAddress", purchaseList.getJSONObject(0).getString("warehouseAddress"));
			JSONContent.put("warehouseContactMobile", purchaseList.getJSONObject(0).getString("warehouseContactMobile"));
			JSONContent.put("warehouseContactPerson", purchaseList.getJSONObject(0).getString("warehouseContactPerson"));
			JSONContent.put("warehouseName", purchaseList.getJSONObject(0).getString("warehouseName"));
			JSONContent.put("warehouseNo", purchaseList.getJSONObject(0).getString("warehouseNo"));
			JSONContent.put("warehousePost", purchaseList.getJSONObject(0).getString("warehousePost"));
			JSONContent.put("warehouseContactPerson", purchaseList.getJSONObject(0).getString("warehouseContactPerson"));
			JSONContent.put("orderFromNo", purchaseList.getJSONObject(0).getString("orderFromNo"));

			JSONObject pidPar = new JSONObject();
			pidPar.put("supplierNO", supplierNo);
			pidPar.put("sopPurchaseOrderNo",purchaseList.getJSONObject(0).getString("sopPurchaseOrderNo"));
			logger.info("获取的PID参数:"+pidPar.toString());
			String pid= restTemplate.postForObject(ApiServiceUrlConfig.getFindPurchaseOrderPid(),getHttpPostData(pidPar.toJSONString()), String.class);
			logger.info("获取的PID:"+pid);
			JSONObject jsonPID= JSONObject.parseObject(pid);
			String pidStr = jsonPID.get("messageRes").toString();
			JSONContent.put("pid", pidStr);
			JSONArray skuList=new JSONArray();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < purchaseList.size(); i++) {
				JSONObject item = new JSONObject();
				sb.append( purchaseList.getJSONObject(i).getString("supplierSkuNo")   +",");
				item.put("barCode", purchaseList.getJSONObject(i).getString("barCode"));
				item.put("brandName", purchaseList.getJSONObject(i).getString("brandName"));
				item.put("brandNo", purchaseList.getJSONObject(i).getString("brandNo"));
				item.put("detailStatus", purchaseList.getJSONObject(i).getString("detailStatus"));
				item.put("isStock", purchaseList.getJSONObject(i).getString("isStock"));
				String url = getPicUrl(purchaseList.getJSONObject(i).getString("skuNo"));
				if(url!=null){
					item.put("picUrl", url);
				}else{
					item.put("picUrl", "");
				}
				item.put("productModel", null);
				item.put("productName", "");
				item.put("qty", purchaseList.getJSONObject(i).getString("qty"));
				item.put("size", "");
				item.put("skuNo", purchaseList.getJSONObject(i).getString("skuNo"));
				item.put("skuPrice", purchaseList.getJSONObject(i).getString("skuPrice"));
				item.put("skuPriceCurrency", purchaseList.getJSONObject(i).getString("skuPriceCurrency"));
				item.put("sopDeliverOrderNo", purchaseList.getJSONObject(i).getString("sopDeliverOrderNo"));
				item.put("supplierSkuNo", purchaseList.getJSONObject(i).getString("supplierSkuNo"));
				skuList.add(item);
			}
			String productParamResult = null;
			try {
				productParamResult = getProductProperty(sopUserNo,URLEncoder.encode(sb.substring(0,sb.length()-1),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONArray productParamJSONResult=JSONObject.parseArray(productParamResult);
			getSkuStockJoin( skuList,productParamJSONResult);
			JSONContent.put("skuList",skuList);
			result.put("content", JSONContent);
		}else {
			result.put("code","1");
			result.put("message","Request Service Error");
			return result;
		}
		return result;
	}
	
	private String getPicUrl(String  sku){
		String url = null;
		try{
            String requestPicUrl = ApiServiceUrlConfig.getPicHost()+"/ListingCatalog/getPicListBySkuNoList?skuNoList="+sku;
            logger.info(" 请求商品 skuPicUrl : "+requestPicUrl);
            String json = HttpClientUtil.doGet(requestPicUrl);
			PictureObj obj = new Gson().fromJson(json, PictureObj.class);
			if(obj!=null){
				if(obj.getContent()!=null){
					if(obj.getContent().getList()!=null&&!obj.getContent().getList().isEmpty()){
						url = obj.getContent().getList().get(0).getPicUrl();
					}
				}
			}
		}catch(Exception e){
		}
		return url;
	}

	/**两个结果集数据组合
	 * @param skuResult1
	 * @param skuResult2
	 */
	private void getSkuStockJoin(JSONArray skuResult1,JSONArray skuResult2){
		if (skuResult2.size()>0&&skuResult2.size()>0) {
			for (int i = 0; i < skuResult1.size(); i++) {
				for (int j = 0; j < skuResult2.size(); j++) {
					if (skuResult1.getJSONObject(i).getString("supplierSkuNo")!=null&&skuResult1.getJSONObject(i).getString("supplierSkuNo").equals(skuResult2.getJSONObject(j).getString("skuId")) ) {
						if (skuResult2.getJSONObject(j).getString("color")==null) {
							skuResult1.getJSONObject(i).put("color", "");
						}else {
							skuResult1.getJSONObject(i).put("color", skuResult2.getJSONObject(j).getString("color"));
						}
						if (skuResult2.getJSONObject(j).getString("size")==null) {
							skuResult1.getJSONObject(i).put("size", "");
						}else {
							skuResult1.getJSONObject(i).put("size", skuResult2.getJSONObject(j).getString("size"));
						}
						if (skuResult2.getJSONObject(j).getString("productName")==null) {
							skuResult1.getJSONObject(i).put("productName", "");
						}else {
							skuResult1.getJSONObject(i).put("productName", skuResult2.getJSONObject(j).getString("productName"));
						}
						if (skuResult2.getJSONObject(j).getString("productCode")==null||skuResult2.getJSONObject(j).getString("productCode").isEmpty()) {
							skuResult1.getJSONObject(i).put("productModel", "");
						}else {
							skuResult1.getJSONObject(i).put("productModel", skuResult2.getJSONObject(j).getString("productCode"));
						}
						
					}
				}
			}
		}
	}
	//获取商品 颜色尺码
	private String getProductProperty(String supplierNo, String supplierSkuNo) {
		return productService.product(supplierNo, supplierSkuNo);
			/*//String url = ApiServiceUrlConfig.getProductUri() + "/" + supplierNo + "/" + URLEncoder.encode(supplierSkuNo, "UTF-8");
			String url = ApiServiceUrlConfig.getProductUri() + "/" + supplierNo + "/" + supplierSkuNo;
			return restTemplate.getForObject(url, String.class);*/
	}
	//包装Http请求参数
    private HttpEntity<String> getHttpPostData(String param){
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> paramEntity  = new HttpEntity<String>("=" +param,headers);
			return paramEntity;
	}
    
	 /***
     * 获得导出数据（采购单库存标记数据导出）
     * @param purchase
     * @return
     */
	public List<HashMap<String, Object>> findPorderCheckExport(String supplierId,CommonPurchaseRequest purchase) {
		purchase.converDate();
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		String json = supplierService.findPorderImport(purchase);
		logger.info("findPorderCheckExport() json {}",json);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}		
		ApiContent<PurchaseOrderDetail> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContent<PurchaseOrderDetail>>() {});
		if (Constants.SUCCESS.equals(requestContent.getResCode())&&requestContent.getMessageRes().size()>0) {
			ApiContent<PurchaseOrderDetail> requestContents = DataLogic(purchase.getSopUserNo(), requestContent);
			List<PurchaseOrderDetail>  importRS = requestContents.getMessageRes();
			
			
			for(int i=0;i<importRS.size();i++){
				HashMap<String, Object> result=new HashMap<String, Object>();
				
				String productParamResult= getProductProperty(purchase.getSopUserNo(),importRS.get(i).getSupplierSkuNo());
				JSONArray productParamJSONResult=JSONObject.parseArray(productParamResult);
				if(productParamJSONResult!=null&&productParamJSONResult.size()>0){
					result.put("productModel",productParamJSONResult.getJSONObject(0).getString("productCode")); 
				}
				result.put("sopPurchaseOrderDetailNo", importRS.get(i).getSopPurchaseOrderDetailNo());
				result.put("barCode", importRS.get(i).getBarCode());
				result.put("brand", importRS.get(i).getBrandName());
				result.put("sopPurchaseOrderNo", importRS.get(i).getSopPurchaseOrderNo()); 
				result.put("supplierSkuNo", importRS.get(i).getSupplierSkuNo()); 
				String itemName = importRS.get(i).getProductName()== null?"":importRS.get(i).getProductName();
				itemName +=importRS.get(i).getColor()== null?"":importRS.get(i).getColor();
				result.put("itemName", itemName); 
				result.put("size", importRS.get(i).getSize()); 
				result.put("skuPrice", importRS.get(i).getSkuPrice()); 
				String orderNo = importRS.get(i).getOrderNo();
				if("2015101501608".equals(supplierId)){
					String masterNo = getOrderNoByMastertNo(orderNo,"2015101501608",importRS.get(i).getSkuNo());
					if(masterNo!=null){
						orderNo = masterNo;
					}
				}
				result.put("orderNo", orderNo); 
				result.put("createTime",importRS.get(i).getCreateTime() == null?"":importRS.get(i).getCreateTime()); 
				result.put("qty", importRS.get(i).getQty());
				result.put("stock", importRS.get(i).getIsStock());
				//20180823 香港 ISA 导出增加PID 字段
				result.put("PID", importRS.get(i).getPid());
				result2.add(result);
			}
			return result2;
		}
		return null;
	}
	public String product(String supplierNo, String supplierSkuNo){
		String url=ApiServiceUrlConfig.getProductUri();
		Map<String,String> request = new HashMap<>();
		request.put("supplierId",supplierNo);
		request.put("skuId",supplierSkuNo);
		return HttpClientUtil.doPost(url, request);// String.class);
	}
	 /***
     * 获得导出数据 （采购单明细数据导出）
     * @param purchase
     * @return
     */
	public List<HashMap<String, Object>> findPorderImport(String supplierId,CommonPurchaseRequest purchase) {
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		purchase.converDate();
		String json = supplierService.findPorderImport(purchase);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}
		ApiContent<PurchaseOrderDetail> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContent<PurchaseOrderDetail>>() {});
		if (Constants.SUCCESS.equals(requestContent.getResCode())&&requestContent.getMessageRes().size()>0) {
			ApiContent<PurchaseOrderDetail> requestContents = DataLogic(purchase.getSopUserNo(), requestContent);
			List<PurchaseOrderDetail>  importRS = requestContents.getMessageRes();
			for(int i=0;i<importRS.size();i++){
				HashMap<String, Object> result=new HashMap<String, Object>();
				result.put("brand", importRS.get(i).getBrandName());
				result.put("barCode", importRS.get(i).getBarCode());
				result.put("sopPurchaseOrderNo", importRS.get(i).getSopPurchaseOrderNo()); 
				result.put("supplierSkuNo", importRS.get(i).getSupplierSkuNo()); 
				
				String jsonProduct = null;
				try {
					jsonProduct = product(purchase.getSopUserNo(), URLEncoder.encode(importRS.get(i).getSupplierSkuNo(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(StringUtils.isEmpty(json) || "[]".equals(json)){
					return null;
				}
				List<Product> products = FastJsonUtil.deserializeString2ObjectList(jsonProduct, Product.class);
				if(products!=null&&products.size()>0){
					result.put("productModel", products.get(0).getProductCode());
				}
				 
				String name = importRS.get(i).getProductName()==null?"":importRS.get(i).getProductName();
				name += importRS.get(i).getColor()==null?"":importRS.get(i).getColor();
				result.put("itemName", name); 
				result.put("size", importRS.get(i).getSize()); 
				result.put("skuPrice", importRS.get(i).getSkuPrice()); 
				
				String orderNo = importRS.get(i).getOrderNo();
				if("2015101501608".equals(supplierId)){
					String masterNo = getOrderNoByMastertNo(orderNo,"2015101501608",importRS.get(i).getSkuNo());
					if(masterNo!=null){
						orderNo = masterNo;
					}
				}
				result.put("orderNo", orderNo); 
				result.put("resend", importRS.get(i).getDetailStatus().trim().equals("1")?"No":"Yes"); 
				result.put("createTime",importRS.get(i).getCreateTime() == null?"":importRS.get(i).getCreateTime()); 
				result.put("qty", importRS.get(i).getQty());
				result.put("stock", importRS.get(i).getIsStock());
				result2.add(result);
			}
			return result2;
		}
		return null;
	}
	
	/**
	 * 处理Api返回的数据
	 * 
	 * @param sopUserNo
	 * @param pList
	 * @return
	 */
	public ApiContent<PurchaseOrderDetail> DataLogic(String sopUserNo, ApiContent<PurchaseOrderDetail> pList) {
		String skus = "";
		for (PurchaseOrderDetail pContent : pList.getMessageRes()) {
			String sku = pContent.getSupplierSkuNo();
			skus += sku + ",";
		}
		List<Product> products = productService.list(sopUserNo, skus);
		for (PurchaseOrderDetail detail : pList.getMessageRes()) {
			if (null == products) {
				detail.setProductName("");
				detail.setSize("");
				detail.setColor("");
			} else {
				for (Product product : products) {
					if (detail.getSupplierSkuNo().equals(product.getSkuId())) {
						detail.setProductName(product.getProductName());
						detail.setSize(product.getSize());
						detail.setColor(product.getColor());
					}
				}
			}
		}
		return pList;
	}
	
	/***
	 * 采购单库存标记数据导入
	 * 
	 * @param pList
	 */
	public String findPorderImport(List<PurImport> pList) {
		String json = supplierService.findPorderImport(pList);
		if (StringUtils.isEmpty(json)) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Api返回数据为空"));
		}

		ApiContent<PurImport> deliveryOrderRS = FastJsonUtil.fromJson(json, new TypeReference<ApiContent<PurImport>>() {
		});
		if (deliveryOrderRS == null) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Api转换数据错误"));
		}
		if (Constants.SUCCESS.equals(deliveryOrderRS.getResCode())) {
			List<PurImport> dList= deliveryOrderRS.getMessageRes();
			ResponseContentList<PurImport> dOne=new ResponseContentList<>();
			dOne.setCode(deliveryOrderRS.getResCode());
			dOne.setContent(dList);
			return FastJsonUtil.serialize2StringEmpty(dOne);
		} else {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorRespWithApi(deliveryOrderRS.getResCode()));
		}
	}

	public String countPrice(PurchaseRequest purchase) {
			String json = HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPorderSkuPrice() , FastJsonUtil.serialize2StringEmpty(purchase));
			if(StringUtils.isEmpty(json)){
				return null;
			}
			JSONObject jsonObject = JSON.parseObject(json);
			String code = String.valueOf(jsonObject.get("resCode"));
			if(!Constants.SUCCESS.equals(code)){
				return null;
			}
			String jsons = jsonObject.get("messageRes").toString();
			
		return jsons;
	}

	public String getCGDDetailByOrderNo(String orderNo,String supplierOrderNo,
			String supplierNo) {
//		supplierOrderNo = "2016040507017";
//		orderNo ="201604050049897";
		String ss = "{\"SopUserNo\":\""+supplierNo+"\",\"OrderNo\":\""+orderNo+"\",\"SupplierOrderNo\":\""+supplierOrderNo+"\"}";
		String json = HttpClientUtil.doPost(ApiServiceUrlConfig.getFindProductBrandInfoBySupplierOrder() ,ss);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}
		String jsons = jsonObject.get("messageRes").toString();
		
		return jsons;
	}

	public ResponseContentOne checkIsPrint(String sopPurchaseOrderDetailNo) {
		String ss = sopPurchaseOrderDetailNo;
		String json =  HttpClientUtil.doPost(ApiServiceUrlConfig.getFindPurchaseIsPrint() ,ss);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		ApiContentBase request = FastJsonUtil.deserializeString2Obj(json, ApiContentBase.class);
		if(Constants.SUCCESS.equals(request.getResCode())){
			return ResponseContentOne.successResp(null);
		}
		JSONObject jsonObject = JSON.parseObject(json);
		
		String code = String.valueOf(jsonObject.get("ResCode"));
		if(!Constants.SUCCESS.equals(code)){
			String msg = jsonObject.getString("MessageRes");
			return ResponseContentOne.errorResp(msg);
		}
		return null;
	}

	public ResponseContentOne updatePrintStatus(String sopPurchaseOrderDetailNo) {
		String ss = sopPurchaseOrderDetailNo;
		String json =  HttpClientUtil.doPost(ApiServiceUrlConfig.getModifyPurchasePrint() ,ss);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		ApiContentBase request = FastJsonUtil.deserializeString2Obj(json, ApiContentBase.class);
		if(Constants.SUCCESS.equals(request.getResCode())){
			return ResponseContentOne.successResp(null);
		}
		JSONObject jsonObject = JSON.parseObject(json);
		
		String code = String.valueOf(jsonObject.get("ResCode"));
		if(!Constants.SUCCESS.equals(code)){
			String msg = jsonObject.getString("MessageRes");
			return ResponseContentOne.errorResp(msg);
		}
		return null;
	}

	public String getOrderNoByMastertNo(String masertNo,String supplierId,String spSkuNo) {
		String json = "{\"spSkuNo\":\""+spSkuNo+"\",\"supplierId\":\""+supplierId+"\",\"masterOrderNo\":\""+masertNo+"\"}";
		String returnData = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getOrderDetail() ,json);
		logger.info("返回数据："+returnData);
		return returnData; 
	}

    /**
     * 直邮模式 第二步 列表接口
     * @param purchaseRequest
     * @return
     */
    public ResponseContentOne<PurchaseOrders> findDirectPorderStockedbyPage(PurchaseRequest purchaseRequest) {
        String json = supplierService.findDirectPorderStockedbyPage(purchaseRequest);
        if(StringUtils.isEmpty(json)){
            return ResponseContentOne.errorResp("");
        }
        JSONObject jsonObject = JSON.parseObject(json);
        String code = String.valueOf(jsonObject.get("resCode"));
        if(!Constants.SUCCESS.equals(code)){
            return ResponseContentOne.errorResp(String.valueOf(jsonObject.get("messageRes")));
        }
        //ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		ApiContentOne<PurchaseOrders> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<PurchaseOrders>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (requestContent!=null&&Constants.SUCCESS.equals(requestContent.getResCode())) {
            PurchaseOrders purchaseOrders = requestContent.getMessageRes();
            PurchaseOrders convert = productService.convert(purchaseOrders, purchaseRequest.getSopUserNo());
            return ResponseContentOne.successResp(convert);
        }
        return null;
    }


	/**
	 * 真直发 保存 发货数据
	 * @param map
	 * @return
	 */
	public ResponseContentOne createDirectDeliveryOrder(Map<String, Object> map) {
		String json = supplierService.createDirectDeliveryOrder(map);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("调用api 接口数据为空");
		}
		ApiContentOne<String> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<String>>() {});
		if(Constants.SUCCESS.equals(requestContent.getResCode())){
			String  sopDeliveryOrderNo = requestContent.getMessageRes();
			return ResponseContentOne.successResp(sopDeliveryOrderNo);
		}else{
			String  sopDeliveryOrderNo = requestContent.getMessageRes();
			return ResponseContentOne.errorResp(sopDeliveryOrderNo);
		}
	}

	/**
	 * 获取供应商 物流公司信息列表
	 * @param map
	 * @return
	 */
	public ResponseContentOne findDirectSupplierLogisticsCompany(Map<String, Object> map) {
		String json = supplierService.findDirectSupplierLogisticsCompany(map);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("调用api 接口数据为空");
		}
		//ApiContentOne<List<LogisticsCompanyDto>> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<List<LogisticsCompanyDto>>>() {});
		ApiContentOne<List<LogisticsCompanyDto>> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<List<LogisticsCompanyDto>>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (requestContent == null) {
			return ResponseContentOne.errorResp("Api转换数据错误");
		}
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			List<LogisticsCompanyDto> logisticsCompanyDtos = requestContent.getMessageRes();
			return ResponseContentOne.successResp(logisticsCompanyDtos);
		}
		return null;
	}

	/**
	 * 真直发 第三步 采购单录入国内段物流信息
	 * @param map
	 * @return
	 */
	public ResponseContentOne entryDirectDorderDomesticLogistics(Map<String, Object> map) {
		String json = supplierService.entryDirectDorderDomesticLogistics(map);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("调用api 接口数据为空");
		}
		ApiContentOne<String> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<String>>() {});
		if (requestContent == null) {
			return ResponseContentOne.errorResp("Api转换数据错误");
		}
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			return ResponseContentOne.successResp("");
		}else{
			String reulstMsg = requestContent.getMessageRes();
			return ResponseContentOne.errorResp(reulstMsg);
		}
	}

	/**
	 * 查询供应 采购单品牌列表
	 * @param sopUserNo
	 * @return
	 */
	public ResponseContentOne findAllBrand(String sopUserNo) {
		String json = supplierService.findAllBrand(sopUserNo);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("调用api 接口数据为空");
		}
		//ApiContentOne<List<BrandDTO>> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<List<BrandDTO>>>() {});
		ApiContentOne<List<BrandDTO>> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<List<BrandDTO>>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (requestContent == null) {
			return ResponseContentOne.errorResp("Api转换数据错误");
		}
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			return ResponseContentOne.successResp(requestContent.getMessageRes());
		}
		return ResponseContentOne.errorResp(json);
	}

	/**
	 * 直邮模式（真直发）查询取消采购单列表
	 * @param purchase
	 * @return
	 */
	public PurchaseOrders findDirectPorderCanceledbyPage(PurchaseRequest purchase) {
		String json = supplierService.findDirectPorderCanceledbyPage(purchase);
		logger.info("【cancel findDirectPorderCanceledbyPage() resultJson {} 】",json);
		if(StringUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("resCode"));
		if(!Constants.SUCCESS.equals(code)){
			return null;
		}
//		ApiContentOne<PurchaseOrders> requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<PurchaseOrders>>() {});
		ApiContentOne<PurchaseOrders> requestContent = null;
		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<PurchaseOrders>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (requestContent!=null&&Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurchaseOrders purchaseOrders = requestContent.getMessageRes();
			return productService.convert(purchaseOrders, purchase.getSopUserNo());
		}
		return null;
	}

	/**
	 * 真直发 获取采购单 发货页面数据
	 * @param map
	 * @return
	 */
	public ResponseContentOne findDirectPorderTransInfo(Map<String, String> map) {

		String json = supplierService.findDirectPorderTransInfo(map);
		if(StringUtils.isEmpty(json)){
			return ResponseContentOne.errorResp("调用api 接口数据为空");
		}
		int  i = 0;
		ApiContentOne<DirtectDeliveryPurchaseInfoForSendDto> requestContent = null;
//		while(true){
//			if(i==4){
//				break;
//			}
//			try {
//				requestContent = FastJsonUtil.fromJson(json, new TypeReference<ApiContentOne<DirtectDeliveryPurchaseInfoForSendDto>>() {});
//				if("200".equals(requestContent.getResCode())){
//					break;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			i++;
//		}

		try {
			requestContent = mapper.readValue(json,new com.fasterxml.jackson.core.type.TypeReference<ApiContentOne<DirtectDeliveryPurchaseInfoForSendDto>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (requestContent == null) {
			return ResponseContentOne.errorResp("Api转换数据错误");
		}
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			DirtectDeliveryPurchaseInfoForSendDto rirtectDeliveryPurchaseInfoForSendDto = requestContent.getMessageRes();
			String picUrl = rirtectDeliveryPurchaseInfoForSendDto.getPicUrl();
			if(StringUtils.isEmpty(picUrl)){
				try{
					picUrl = getPicUrl(rirtectDeliveryPurchaseInfoForSendDto.getSkuNo());
				}catch(Exception ex){
				}
				if(picUrl!=null){
					rirtectDeliveryPurchaseInfoForSendDto.setPicUrl(picUrl);
				}else{
					rirtectDeliveryPurchaseInfoForSendDto.setPicUrl("");
				}
			}
			return ResponseContentOne.successResp(rirtectDeliveryPurchaseInfoForSendDto);
		}

		return ResponseContentOne.errorResp("调用api 接口数据为空");
	}



}
