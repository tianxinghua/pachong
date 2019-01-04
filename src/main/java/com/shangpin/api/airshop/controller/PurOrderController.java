package com.shangpin.api.airshop.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.CreateDeliveryPurOrderReqDTO;
import com.shangpin.api.airshop.dto.DeliveryOrderDetailResDTO;
import com.shangpin.api.airshop.dto.GeneralTradeAndCrossBorder;
import com.shangpin.api.airshop.dto.PictureObj;
import com.shangpin.api.airshop.dto.PurImport;
import com.shangpin.api.airshop.dto.PurchaseOrderDetail;
import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.request.PurchaseRequest;
import com.shangpin.api.airshop.service.PurOrderService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.DateUtil;
import com.shangpin.common.utils.FastJsonUtil;

/**
 * 采购单 Date: 2016年1月12日 <br/>
 * 
 * @author 陈小峰
 * @since JDK 7
 */
@RestController
@RequestMapping("/purchase")
@SessionAttributes(Constants.SESSION_USER)
// 添加到session作用域
public class PurOrderController {
	private static Logger logger = LoggerFactory
			.getLogger(PurOrderController.class);
	@Autowired
	private PurOrderService purOrderService;

	/**
	 * 采购单查询列表
	 * 
	 * @param purchase
	 *            请求参数实体
	 * @return
	 */
	@RequestMapping(value = "/overTime", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String overTimelist(PurchaseRequest purchase,
		
			HttpServletRequest request) {
		purchase.setOverTimeStatus((byte)1);
		return list(purchase, request,true);
	}
	
	/**
	 * 根据供应商单号获取尚品主单号
	 * 
	 * @param purchase
	 *            请求参数实体
	 * @return
	 */
	@RequestMapping(value = "/getOrderNoByMastertNo", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	private String getOrderNoByMastertNo(String maseterNo,String supplierId,String spSkuNo){
		return purOrderService.getOrderNoByMastertNo(maseterNo,supplierId,spSkuNo);
	}
	
	/**
	 * 采购单查询列表
	 * 
	 * @param purchase
	 *            请求参数实体
	 * @return
	 */
	@RequestMapping(value = "/list", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String list(PurchaseRequest purchase,HttpServletRequest request,boolean flag) {
		long startTime = new Date().getTime();
		String pageIndex = purchase.getPageIndex();
		String pageSize = purchase.getPageSize();
		String detailStatus = (String) purchase.getDetailStatus();
		String isStock = (String) purchase.getIsStock();
		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
		String userNo = userInfo.getSopUserNo();
		String supplierNo = userInfo.getSupplierNo();
		
		if(flag){
			purchase.setDetailStatus(null);
			purchase.setIsDoStock("0");
		}else{
			purchase.setDetailStatus(this.convert(detailStatus));
		}
		
		purchase.setIsStock(this.convert(isStock));
		purchase.setSopUserNo(userNo);
		purchase.setSupplierNo(supplierNo);
		if(purchase.getImportType()==null){
			purchase.setImportType("0");	
		}
		purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
		purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
		if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
			purchase.setUpdateTimeBegin("");
			purchase.setUpdateTimeEnd("");
		}
		PurchaseOrders purchaseOrders = purOrderService.list(purchase);
		if (null == purchaseOrders) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorResp("返回数据为空"));
		}
		PurchaseOrders pOrders = purchaseOrders;
		//http://tms0.shangpin.com/Delivery/OrderManage/GetPrintDataBySupplierOrderNo?opType=0&supplierOrderNo=2016051208191
		if(pOrders!=null){
			List<PurchaseOrderDetail> list = pOrders.getPurchaseOrderDetails();
			if(list!=null&&!list.isEmpty()){
				for(PurchaseOrderDetail detail : list){
						try{
							String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
							logger.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo());
							String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
							logger.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo()+",返回结果："+productUrl);
							detail.setProductUrl(productUrl);
						}catch(Exception e){
							
						}
						
					String orderNo = detail.getOrderNo();
					logger.info(userInfo.getSopUserNo());
					if("2015101501608".equals(userInfo.getSopUserNo())){
						String masterNo = getOrderNoByMastertNo(orderNo,"2015101501608",detail.getSkuNo());
						if(masterNo!=null){
							detail.setOrderNo(masterNo);
						}
					}
					
					String url = null;
					try{
						url = getPicUrl(detail);
					}catch(Exception ex){
						ex.printStackTrace();
						logger.info(ex.getMessage());
					}
					
					if(url!=null){
						detail.setPicUrl(url);
					}else{
						detail.setPicUrl("");
					}
					String urlTMS = ApiServiceUrlConfig.getIp()+"/wayprint/printPIDAndSupplierOrderNo?supplierOrderNo="+detail.getSupplierOrderNo();
					detail.setTmsUrl(urlTMS);
				}
			}
		}
		ResponseContentOne<PurchaseOrders> result = ResponseContentOne
				.successResp(pOrders);
		logger.info("查询订单列表总耗时："+(new Date().getTime()-startTime));
		return FastJsonUtil.serialize2StringEmpty(result);
	}


	/**
	 * 取消采购单查询列表
	 *
	 * @param purchase
	 *            请求参数实体
	 * @return
	 */
	@RequestMapping(value = "/findPorderCanceledbyPage", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String findPorderCanceledbyPage(PurchaseRequest purchase,HttpServletRequest request,boolean flag) {
		long startTime = new Date().getTime();
		String pageIndex = purchase.getPageIndex();
		String pageSize = purchase.getPageSize();
		String detailStatus = (String) purchase.getDetailStatus();
		String isStock = (String) purchase.getIsStock();
		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
		String userNo = userInfo.getSopUserNo();
		String supplierNo = userInfo.getSupplierNo();

		if(flag){
			purchase.setDetailStatus(null);
			purchase.setIsDoStock("0");
		}else{
			purchase.setDetailStatus(this.convert(detailStatus));
		}

		purchase.setIsStock(this.convert(isStock));
		purchase.setSopUserNo(userNo);
		purchase.setSupplierNo(supplierNo);
		if(purchase.getImportType()==null){
			purchase.setImportType("0");
		}
		purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
		purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
		if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
			purchase.setUpdateTimeBegin("");
			purchase.setUpdateTimeEnd("");
		}
		PurchaseOrders purchaseOrders = purOrderService.findPorderCanceledbyPage(purchase);
		if (null == purchaseOrders) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorResp("返回数据为空"));
		}
		PurchaseOrders pOrders = purchaseOrders;
		//http://tms0.shangpin.com/Delivery/OrderManage/GetPrintDataBySupplierOrderNo?opType=0&supplierOrderNo=2016051208191
		if(pOrders!=null){
			List<PurchaseOrderDetail> list = pOrders.getPurchaseOrderDetails();
			if(list!=null&&!list.isEmpty()){
				for(PurchaseOrderDetail detail : list){
					try{
						String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
						logger.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo());
						String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
						logger.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo()+",返回结果："+productUrl);
						detail.setProductUrl(productUrl);
					}catch(Exception e){

					}

					String orderNo = detail.getOrderNo();
					logger.info(userInfo.getSopUserNo());
					if("2015101501608".equals(userInfo.getSopUserNo())){
						String masterNo = getOrderNoByMastertNo(orderNo,"2015101501608",detail.getSkuNo());
						if(masterNo!=null){
							detail.setOrderNo(masterNo);
						}
					}

					String url = null;
					try{
						url = getPicUrl(detail);
					}catch(Exception ex){
						ex.printStackTrace();
						logger.info(ex.getMessage());
					}

					if(url!=null){
						detail.setPicUrl(url);
					}else{
						detail.setPicUrl("");
					}
					String urlTMS = ApiServiceUrlConfig.getIp()+"/wayprint/printPIDAndSupplierOrderNo?supplierOrderNo="+detail.getSupplierOrderNo();
					detail.setTmsUrl(urlTMS);
				}
			}
		}
		ResponseContentOne<PurchaseOrders> result = ResponseContentOne
				.successResp(pOrders);
		logger.info("查询订单列表总耗时："+(new Date().getTime()-startTime));
		return FastJsonUtil.serialize2StringEmpty(result);
	}


	@RequestMapping(value = "/countTradeAndCross", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String countGeneralTradeAndCrossBorder(PurchaseRequest purchase,
		
			HttpServletRequest request) {
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
		String userNo = userInfo.getSopUserNo();
		String supplierNo = userInfo.getSupplierNo();
		String detailStatus = (String) purchase.getDetailStatus();
		String isStock = (String) purchase.getIsStock();
		purchase.setDetailStatus(this.convert(detailStatus));
		purchase.setIsStock(this.convert(isStock));
		purchase.setSopUserNo(userNo);
		purchase.setSupplierNo(supplierNo);
		purchase.setPageIndex("1");
		purchase.setPageSize("10");
		//跨境
		purchase.setImportType("1");
		JSONArray jsonArray = JSON.parseArray(purOrderService.countPrice(purchase));
		int resultCross = 0;
		double priceCross =  0;
		StringBuffer crossSupplierOrder = null;
		StringBuffer crossSopPurchaseOrderNo = null;
		if(jsonArray!=null&&!jsonArray.isEmpty()){
			crossSupplierOrder = new StringBuffer();
			crossSopPurchaseOrderNo = new StringBuffer();
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject1 = (JSONObject)jsonArray.get(i);
				double val =  Double.parseDouble(jsonObject1.get("skuPrice").toString());
				priceCross += val;
				crossSupplierOrder.append(",");
				crossSupplierOrder.append(jsonObject1.get("supplierOrderNo").toString());
				crossSopPurchaseOrderNo.append(",");
				crossSopPurchaseOrderNo.append(jsonObject1.get("sopPurchaseOrderNo").toString());
			}
			resultCross = jsonArray.size();
		}
		
		//一般贸易
		purchase.setImportType("2");
		int resultTrade = 0;
		double priceTrade = 0;
		JSONArray jsonArray1 = JSON.parseArray(purOrderService.countPrice(purchase));
		StringBuffer tradeSupplierOrder =null;
		StringBuffer tradeSopPurchaseOrderNo =null;
		if(jsonArray1!=null&&!jsonArray1.isEmpty()){
			tradeSupplierOrder = new StringBuffer();
			tradeSopPurchaseOrderNo = new StringBuffer();
			for(int i=0;i<jsonArray1.size();i++){
				JSONObject jsonObject2 = (JSONObject)jsonArray1.get(i);
				double val =  Double.parseDouble(jsonObject2.get("skuPrice").toString());
				priceTrade += val;
				tradeSupplierOrder.append(",");
				tradeSupplierOrder.append(jsonObject2.get("supplierOrderNo").toString());
				tradeSopPurchaseOrderNo.append(",");
				tradeSopPurchaseOrderNo.append(jsonObject2.get("sopPurchaseOrderNo").toString());
			}
			resultTrade = jsonArray1.size();
		}
		
		//个人物品
		purchase.setImportType("3");
		int resultPersonal = 0;
		double pricePersonal = 0;
		JSONArray jsonArray3 = JSON.parseArray(purOrderService.countPrice(purchase));
		StringBuffer personalSupplierOrder =null;
		StringBuffer personalSopPurchaseOrderNo =null;
		if(jsonArray3!=null&&!jsonArray3.isEmpty()){
			personalSupplierOrder = new StringBuffer();
			personalSopPurchaseOrderNo = new StringBuffer();
			for(int i=0;i<jsonArray3.size();i++){
				JSONObject jsonObject3 = (JSONObject)jsonArray3.get(i);
				double val =  Double.parseDouble(jsonObject3.get("skuPrice").toString());
				pricePersonal += val;
				personalSupplierOrder.append(",");
				personalSupplierOrder.append(jsonObject3.get("supplierOrderNo").toString());
				personalSopPurchaseOrderNo.append(",");
				personalSopPurchaseOrderNo.append(jsonObject3.get("sopPurchaseOrderNo").toString());
			}
			resultPersonal = jsonArray3.size();
		}
		//1:Cross Border   2:General Trade 3:Personal Goods
//		String	json = null;
//		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//		String date = s.format(new Date());
//		if(tradeSopPurchaseOrderNo==null&&crossSupplierOrder!=null){
//			json = "{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"},{\"category\":\"1\",\"sopPurchaseOrderNo\":\""+crossSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+crossSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}]}";
//		}
//		if(tradeSopPurchaseOrderNo==null&&crossSupplierOrder==null){
//			json = "{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"},{\"category\":\"1\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}]}";
//		}
//		if(tradeSopPurchaseOrderNo!=null&&crossSupplierOrder!=null){
//			json = "{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\""+tradeSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+tradeSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"},{\"category\":\"1\",\"sopPurchaseOrderNo\":\""+crossSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+crossSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}]}";
//		}
//		if(tradeSopPurchaseOrderNo!=null&&crossSupplierOrder==null){
//			json = "{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\""+tradeSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+tradeSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"},{\"category\":\"1\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}]}";
//		}
//		String	json = "{\"parcelsPoolList\":["+
//				"{\"category\":\"2\",\"sopPurchaseOrderNo\":\""+tradeSopPurchaseOrderNo+"\",\"supplierOrderOrPurchNo\":\""+tradeSupplierOrder+"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"},"+
//				"{\"category\":\"1\",\"sopPurchaseOrderNo\":\""+crossSopPurchaseOrderNo==null?"":crossSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+crossSupplierOrder==null?"":crossSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}]}";
		
		
		StringBuffer json= new StringBuffer();
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = s.format(new Date());
		if(tradeSopPurchaseOrderNo==null){
			json.append("{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}else{
			json.append("{\"parcelsPoolList\":[{\"category\":\"2\",\"sopPurchaseOrderNo\":\""+tradeSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+tradeSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultTrade+"\",\"totalPrice\":\""+priceTrade+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}
		
		if(crossSupplierOrder==null){
			json.append("{\"category\":\"1\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}else{
			json.append("{\"category\":\"1\",\"sopPurchaseOrderNo\":\""+crossSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+crossSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultCross+"\",\"totalPrice\":\""+priceCross+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}
		if(personalSopPurchaseOrderNo==null){
			json.append("{\"category\":\"3\",\"sopPurchaseOrderNo\":\"\",\"supplierOrderOrPurchNo\":\"\",\"orderQty\":\""+resultPersonal+"\",\"totalPrice\":\""+pricePersonal+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}else{
			json.append("{\"category\":\"3\",\"sopPurchaseOrderNo\":\""+personalSopPurchaseOrderNo.substring(1)+"\",\"supplierOrderOrPurchNo\":\""+personalSupplierOrder.substring(1)+"\",\"orderQty\":\""+resultPersonal+"\",\"totalPrice\":\""+pricePersonal+"\",\"totalWeight\":\"\",\"lastUpdateTime\":\""+date+"\"}");
		}	
		json.append("]}");
		System.out.println(json);
		GeneralTradeAndCrossBorder requestContent = FastJsonUtil.fromJson(json.toString(), new TypeReference<GeneralTradeAndCrossBorder>() {});
		ResponseContentOne<GeneralTradeAndCrossBorder> result = ResponseContentOne
				.successResp(requestContent);
		return FastJsonUtil.serialize2StringEmpty(result);
	}

	private String getPicUrl(PurchaseOrderDetail detail) throws Exception{
		String sku = detail.getSkuNo();
		String url = null;
		String json = HttpClientUtil.doGet(ApiServiceUrlConfig.getPicHost()+"/ListingCatalog/getPicListBySkuNoList?skuNoList="+sku);
		logger.info("请求图片 返回结果：{} ",json);
		JSONObject jsonObject = JSON.parseObject(json);
		PictureObj obj = new Gson().fromJson(json, PictureObj.class);
		if(obj!=null){
			if(obj.getContent()!=null){
				if(obj.getContent().getList()!=null&&!obj.getContent().getList().isEmpty()){
					url = obj.getContent().getList().get(0).getPicUrl();
				}
			}
		}
		return url;
	}
	
	/**
	 * 采购发货
	 * 
	 * @param CreateDeliveryPurOrderReqDTO
	 * @return
	 */
	@RequestMapping(value = "/create/deliveryOrder", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deliveryPurchaseOrder(CreateDeliveryPurOrderReqDTO doDTO,
			@ModelAttribute(Constants.SESSION_USER) UserInfo user) {
		logger.debug("create deliveryPurchaseOrder request:{}",
				FastJsonUtil.serialize2String(doDTO));
		if (StringUtils.isEmpty(doDTO.getLogisticsName())
				|| StringUtils.isEmpty(doDTO.getLogisticsOrderNo())
				|| StringUtils.isEmpty(doDTO.getSopPurchaseOrderDetailNo())) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}
		doDTO.setSopUserNo(user.getSopUserNo());
		ResponseContentOne<DeliveryOrderDetailResDTO> resDTO = purOrderService
				.createDeliveryOrder(doDTO);
		return FastJsonUtil.serialize2StringEmpty(resDTO);
	}

	/**
	 * 检查采购单库存
	 * @param pruchaseOrderInfo  (批量，pruchaseOrderInfo 是多个采购详情编号 以 | 分割 每个单元以 sopPurchaseOrderDetailNo_stockFlag 组成
	 * 	                         其中 sopPurchaseOrderDetailNo:采购详情单号  stockFlag:有无库存标志 1:有库存 2:无库存 0:待确认库存 )
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/checkStock", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseContentOne checkStock(String pruchaseOrderInfo, String type,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(pruchaseOrderInfo)) {
			return ResponseContentOne.errorParam();
		}
		String userNo = userInfo.getSopUserNo();
		// String userNo = "2015032600062";
		String[] info = pruchaseOrderInfo.trim().split("\\|");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < info.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			String[] str = info[i].trim().split("\\_");
			if (str.length < 2 || StringUtils.isEmpty(str[0])) {
				return ResponseContentOne.errorParam();
			}
			String purchaseNo = str[0];
			String stu = str[1];
			map.put("SopUserNo", userNo);
			map.put("SopPurchaseOrderDetailNo", purchaseNo);
			map.put("IsStock", stu);
			list.add(map);
		}
		ResponseContentOne content = purOrderService.checkStock(list, type);
		return content;
	}

	/**
	 * 采购单详情
	 * 
	 * @author liling
	 * @param PurOrderDetailReqDTO
	 * @return
	 */
	@RequestMapping(value = "/{purchaseOrderNo}/details", produces = "text/html;charset=UTF-8")
	public String findPurOrderDetails(
			@PathVariable("purchaseOrderNo") String purchaseOrderNo,
			@ModelAttribute(Constants.SESSION_USER) UserInfo user) {
		// 这个地方序列化是为了处理null，变成""

		return FastJsonUtil.serialize2StringEmpty(purOrderService
				.findPurOrderDetail(purchaseOrderNo, user.getSopUserNo(),
						user.getSupplierNo()));
	}
	@RequestMapping(value = "/{purchaseOrderNo}/detail", produces = "text/html;charset=UTF-8")
	public String findPurOrderDetail(
			@PathVariable("purchaseOrderNo") String purchaseOrderNo,
			@ModelAttribute(Constants.SESSION_USER) UserInfo user) {
		// 这个地方序列化是为了处理null，变成""
		
		return purOrderService.findPurOrderDetail(purchaseOrderNo, user.getSopUserNo(),user.getSupplierNo()).toJSONString();
		/*return FastJsonUtil.serialize2StringEmpty(purOrderService
				.findPurOrderDetail(purchaseOrderNo, user.getSopUserNo(),
						user.getSupplierNo()));*/
	}

	/**
	 * 组装请求参数（接口所需的格式）
	 *
	 * @param params
	 * @return
	 */
	private String[] convert(String params) {
		if (StringUtils.isEmpty(params)) {
			return new String[] {};
		}
		if (params.indexOf("_") > -1) {
			return params.split("\\_");
		}
		return new String[] { params };
	}

	/***
	 * wh 采购单库存标记数据导出
	 * 
	 * @param purchase
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inventoryExport")
	public String porderCheck(PurchaseRequest purchase,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response,HttpServletRequest req) throws Exception {
		purchase.setSopUserNo(userInfo.getSopUserNo());
		purchase.setSupplierNo(userInfo.getSupplierNo());
		if(StringUtils.isEmpty(userInfo.getSopUserNo()) 
				&& StringUtils.isEmpty(userInfo.getSupplierNo())){
			UserInfo u1 = (UserInfo) req.getSession().getAttribute(Constants.SESSION_USER);
			if(u1!=null){
				purchase.setSopUserNo(u1.getSopUserNo());
				purchase.setSupplierNo(u1.getSupplierNo());
			}
		}
		if(purchase.getImportType()==null){
			purchase.setImportType("0");
		}
		String isStock = (String) purchase.getIsStock();
		purchase.setIsStock(this.convert(isStock));
		String status = (String) purchase.getDetailStatus();
		purchase.setDetailStatus(this.convert(status));
		List<HashMap<String, Object>> result = purOrderService
				.findPorderCheckExport(userInfo.getSopUserNo(),purchase);
		response.setContentType("application/x-download");// 设置为下载application/x-download
		String fileName = "Stock Check (1st time)";
		if (purchase.getUpdateTimeBegin()!=null&&purchase.getUpdateTimeEnd()!=null&&!purchase.getUpdateTimeEnd().equals("")&&!purchase.getUpdateTimeBegin().equals("")) {
			fileName+=DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeBegin(), "yyyy-MM-dd"), "MM-dd-yyyy")+" to "+DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeEnd(), "yyyy-MM-dd"), "MM-dd-yyyy");
		}else{
			fileName+="All";
		}
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes("gb2312"), "ISO8859-1")
				+ ".xls");
		OutputStream out = response.getOutputStream();
		if (result != null && result.size() > 0) {

			String[] headers = { "Order Detail Code", "Brand", "Order Code", "Bar Code",
					"Supplier SKU", "Item Code",  "Item Name", "Size", "Price",
					"Shangpin Code", "Date", "Qty",
					"Stock", "PID"};
			String[] columns = { "sopPurchaseOrderDetailNo", "brand",
					"sopPurchaseOrderNo", "barCode","supplierSkuNo", "productModel",
					"itemName", "size", "skuPrice", "orderNo", "createTime",
					"qty","stock", "PID"};
			ExportExcelUtils.exportExcel(fileName, headers, columns, result,
					out, "");

			out.close();
			return null;
		}
		else
		{
			List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> result1=new HashMap<String, Object>();
			result1.put("oops", "Export failed. Please try again later.");
			result2.add(result1);
			String[] headers = { "Oops" };
			String[] columns = { "oops" };
			ExportExcelUtils.exportExcel(fileName, headers, columns, result2,
					out, "");
			out.close();
			return null;
		}
	}

	/***
	 * wh 采购单明细数据导出
	 * 
	 * @param purchase
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detailExport")
	public String detailExport(PurchaseRequest purchase,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) throws Exception {
		purchase.setSopUserNo(userInfo.getSopUserNo());
		purchase.setSupplierNo(userInfo.getSupplierNo());
		String status = String.valueOf(purchase.getDetailStatus());
		if(purchase.getImportType()==null){
			purchase.setImportType("0");	
		}
		purchase.setDetailStatus(this.convert(status));
		List<HashMap<String, Object>> result = purOrderService
				.findPorderImport(userInfo.getSopUserNo(),purchase);
		response.setContentType("application/x-download");// 设置为下载application/x-download
		String fileName = "Stock Check (2st time)";
		if (purchase.getUpdateTimeBegin()!=null&&purchase.getUpdateTimeEnd()!=null&&!purchase.getUpdateTimeEnd().equals("")&&!purchase.getUpdateTimeBegin().equals("")) {
			fileName+=DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeBegin(), "yyyy-MM-dd"), "MM-dd-yyyy")+" to "+DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeEnd(), "yyyy-MM-dd"), "MM-dd-yyyy");
		}else{
			fileName+="All";
		}
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes("gb2312"), "ISO8859-1")
				+ ".xls");
		OutputStream out = response.getOutputStream();
		if (result != null && result.size() > 0) {
			String[] headers = { "Brand", "Order Code", "Bar Code","Supplier SKU",
					"Item Code", "Item Name", "Size", "Price", "Shangpin Code",
					"Product Resend", "Date", "Qty", "Stock" };
			String[] columns = { "brand", "sopPurchaseOrderNo","barCode",
					"supplierSkuNo", "productModel", "itemName", "size",
					"skuPrice", "orderNo", "resend", "createTime", "qty",
					"stock" };
			ExportExcelUtils.exportExcel(fileName, headers, columns, result,
					out, "");
			out.close();
			return null;
		}
		else
		{
			List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> result1=new HashMap<String, Object>();
			result1.put("oops", "Export failed. Please try again later.");
			result2.add(result1);
			String[] headers = { "Oops" };
			String[] columns = { "oops" };
			ExportExcelUtils.exportExcel(fileName, headers, columns, result2,
					out, "");
			out.close();
			return null;
		}
		// FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Not Data"));
	}

	/***
	 * 采购单库存标记数据导入
	 * 
	 * @param uploadfile Excel
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inventoryImport")
	public String inventoryImport(
			@RequestParam(value = "uploadfile", required = true) MultipartFile uploadfile,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) throws Exception {
		InputStream is = uploadfile.getInputStream();
		// InputStream is = new FileInputStream("d:\\77.xls");
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			List<PurImport> pList = new ArrayList<>();
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				PurImport pImport = new PurImport();
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				pImport.setSupplierSkuNo(userInfo.getSopUserNo());
				pImport.setSopPurchaseOrderDetailNo(getValue(hssfRow.getCell(0)));
				pImport.setSopPurchaseOrderNo(getValue(hssfRow.getCell(2)));
				try {
					pImport.setIsStock(new Float(Float
							.parseFloat(getValue(hssfRow.getCell(11))))
							.intValue());
				} catch (Exception e) {
					pImport.setIsStock(0);
				}
				pList.add(pImport);
			}
			return purOrderService.findPorderImport(pList);
		}
		return "";
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	
	
	/**
	 * 检查直发采购单是否已打印
	 * 
	 * @param pruchaseOrderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkIsPrint", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseContentOne checkIsPrint(String sopPurchaseOrderDetailNo, 
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
		if (StringUtils.isEmpty(sopPurchaseOrderDetailNo)) {
			return ResponseContentOne.errorParam();
		}
		// String userNo = "2015032600062";
		ResponseContentOne content = purOrderService.checkIsPrint(sopPurchaseOrderDetailNo);
		return content;
	}
	
	/**
	 * 检查直发采购单是否已打印
	 * 
	 * @param pruchaseOrderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePurchasePrintStatus", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseContentOne updatePrintStatus(String sopPurchaseOrderDetailNo, 
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
		if (StringUtils.isEmpty(sopPurchaseOrderDetailNo)) {
			return ResponseContentOne.errorParam();
		}
		// String userNo = "2015032600062";
		ResponseContentOne content = purOrderService.updatePrintStatus(sopPurchaseOrderDetailNo);
		return content;
	}


	/**
	 * 检查直发采购单是否已打印
	 *
	 * @param pruchaseOrderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAllBrand", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseContentOne findAllBrand(@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
		if(userInfo==null){
			return ResponseContentOne.errorResp("3","");
		}
		// String userNo = "2015032600062";
		ResponseContentOne content = purOrderService.findAllBrand(userInfo.getSopUserNo());
		return content;
	}



}
