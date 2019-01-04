package com.shangpin.api.airshop.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.Product;
import com.shangpin.api.airshop.dto.ReturnOrder;
import com.shangpin.api.airshop.dto.ReturnOrderDetail;
import com.shangpin.api.airshop.dto.ReturnOrderList;
import com.shangpin.api.airshop.dto.ReturnOrderPage;
import com.shangpin.api.airshop.dto.ReturnOrders;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ApiContentStr;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.enums.ReturnOrderCause;
import com.shangpin.common.utils.FastJsonUtil;

/**
 * 返货
 * Created by ZRS on 2016/1/14.
 */
@RestController
@RequestMapping("/return")
@SessionAttributes(Constants.SESSION_USER) //添加到session作用域
public class ReturnOrderController {

    @Autowired
    BaseService baseService;

    @Autowired
    ProductService productService;

    /**
     * 返货列表
     * @param updateTimeBegin 开始时间
     * @param updateTimeEnd 结束时间
     * @param status 返货状态
     * @param pageIndex 页码
     * @param pageSize 页数量
     * @return 返货列表
     */
    @RequestMapping(value="/list/{status}/{pageIndex}")
    public JSONObject getOrderList(
    		//public ResponseContentOne<ReturnOrderList> getOrderList(
            @PathVariable(value = "status") String status,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "start",required = false) String updateTimeBegin,
            @RequestParam(value = "end",required = false) String updateTimeEnd,
            @RequestParam(value = "pageSize",required = false) Integer pageSize,
            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        //默认每页20 分页最大20
        pageSize = StringUtils.isEmpty(pageSize) || pageSize > 20 ? 20 : pageSize;

        //做些操作 DT
        /*if(!StringUtils.isEmpty(updateTimeBegin) && StringUtils.isEmpty(updateTimeEnd)){
            updateTimeEnd = "01/01/2100 00:00:00";
        }
        if(!StringUtils.isEmpty(updateTimeEnd) && StringUtils.isEmpty(updateTimeBegin)){
            updateTimeBegin = "01/01/1900 00:00:00";
        }*/
        
        if (StringUtils.isEmpty(updateTimeBegin) || StringUtils.isEmpty(updateTimeEnd)) {
        	updateTimeBegin="";
        	updateTimeEnd="";
		}

        //组装参数
        /*Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("UpdateTimeBegin", DateFormat.TimeFormatChangeToString(updateTimeBegin, "dd-MM-yyyy", "yyyy-MM-dd") );
        paramMap.put("UpdateTimeEnd", DateFormat.TimeFormatChangeToString(updateTimeEnd,"dd-MM-yyyy", "yyyy-MM-dd") );
        JSONArray statusJson = new JSONArray();
        String[] stat=status.split("-");
        for (String st : stat) {
        	statusJson.add(st);			
		}
        paramMap.put("ReturnOrderStatus", statusJson);
        paramMap.put("PageIndex", pageIndex );
        paramMap.put("PageSize", pageSize );
        paramMap.put("SopUserNo", userInfo.getSopUserNo());*/

        //组装参数
        JSONObject param = new JSONObject();
        param.put("UpdateTimeBegin", DateFormat.TimeFormatChangeToString(updateTimeBegin, "dd-MM-yyyy", "yyyy-MM-dd") );
        param.put("UpdateTimeEnd", DateFormat.TimeFormatChangeToString(updateTimeEnd,"dd-MM-yyyy", "yyyy-MM-dd") );
        JSONArray statusJson = new JSONArray();
        String[] stat=status.split("-");
        for (String st : stat) {
        	statusJson.add(st);			
		}
        param.put("ReturnOrderStatus", statusJson);
        param.put("PageIndex", pageIndex );
        param.put("PageSize", pageSize );
        param.put("SopUserNo", userInfo.getSopUserNo());
       
        //请求数据
        return  baseService.getReturnBeforOrder(ApiServiceUrlConfig.getReturnListUrl(), param);
        //return baseService.requestAPI4One(ApiServiceUrlConfig.getReturnListUrl(), paramMap, ReturnOrderList.class);
    }

    @RequestMapping(value = "/order/{returnCode}")
    public ResponseContentOne<ReturnOrderList> getOrderItem(@PathVariable("returnCode") String returnCode, @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
             HttpServletResponse response) throws Exception {

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SopUserNo", userInfo.getSopUserNo());
        paramMap.put("SopReturnOrderNo", returnCode);

        //请求数据
        ResponseContentOne<ReturnOrderList> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getReturnOrderUrl(), paramMap, ReturnOrderList.class);
        return returnOrderLogic(responseContentOne,userInfo.getSopUserNo());
    }
    
    /***
     * 掉Api信息
     * @param responseContentOne
     * @return
     */
    private  ResponseContentOne<ReturnOrderList> returnOrderLogic( ResponseContentOne<ReturnOrderList> responseContentOne,String sopUserNo) {
    	 ReturnOrderList returnOrderList = responseContentOne.getContent();

    	 //详情只有一个
         ReturnOrders returnOrders = returnOrderList.getReturnOrders().get(0);
         List<ReturnOrderDetail> returnOrderDetails = returnOrders.getReturnOrderDetails();

         //遍历sku
         Set<String> skuNoSet = new HashSet<>();
         if(returnOrderDetails!=null){
        	 for (ReturnOrderDetail returnOrderDetail : returnOrderDetails) {
                 //原因改成说明
                 returnOrderDetail.setExceptionDesc(ReturnOrderCause.getDesc(returnOrderDetail.getExceptionType()));
                 String supplierSkuNo = returnOrderDetail.getSupplierSkuNo();
                 if(!StringUtils.isEmpty(supplierSkuNo)){
                     skuNoSet.add(supplierSkuNo);
                 }
                 returnOrderDetail.setProductMsg("");
             }
         }

         //获取商品信息
         Map<String, Product> productMap = productService.list(sopUserNo, skuNoSet);
         if(productMap == null || productMap.isEmpty()){
             return responseContentOne;
         }

         //添加sku
         for (ReturnOrderDetail returnOrderDetail : returnOrderDetails) {
             String supplierSkuNo = returnOrderDetail.getSupplierSkuNo();
             Product product = productMap.get(supplierSkuNo);
             if(product == null){
                 continue;
             }
             //追加属性
             returnOrderDetail.setSize(product.getSize());
             String prodectMsg = (product.getProductName() == null ? "" : product.getProductName()) + " " +
                     (product.getColor() == null ? "" : product.getColor()) + " " + (product.getSize() == null ? "" : product.getSize());
             returnOrderDetail.setProductMsg(prodectMsg);
         }
         return responseContentOne;
	}
    
    /**
     * 导出信息处理
     * @param response
     * @param responseContentOne
     * @throws Exception
     */
	private String exportLogic(HttpServletResponse response, ResponseContentOne<ReturnOrderList> responseContentOne)throws Exception {
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		List<ReturnOrderDetail> importRS = responseContentOne.getContent().getReturnOrders().get(0).getReturnOrderDetails();
		for(int i=0;i<importRS.size();i++){
			HashMap<String, Object> result=new HashMap<String, Object>();
			result.put("sopReturnOrderNo", importRS.get(i).getSopReturnOrderNo());
			result.put("brand", importRS.get(i).getBrandName()); 
			result.put("supplierSku", importRS.get(i).getSupplierSkuNo()); 
			result.put("product", importRS.get(i).getProductMsg()); 
			result.put("returnReason",importRS.get(i).getMemo());
			result2.add(result);
		}
		if (result2!=null && result2.size()>0) {
			response.setContentType("application/x-download");// 设置为下载application/x-download
			String fileName = "ReturnBeforeDetail";
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
			OutputStream out = response.getOutputStream();
			String[] headers = {"OrderCode","Brand","Supplier SKU","Item Name","Return Reason"};
			String[] columns = { "sopReturnOrderNo", "brand", "supplierSku","product","returnReason"};
			ExportExcelUtils.exportExcel(fileName, headers, columns, result2, out, "");
			out.close();
			return null;
		}
		 return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Not Data"));
	}

	/***wh
	 * 返货明细导出
	 * @param
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listExport")
	public String detailExport(String returnCode,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(returnCode)) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
		}else{
	         Map<String, Object> paramMap = new HashMap<>();
		     paramMap.put("SopUserNo", userInfo.getSopUserNo());
		     paramMap.put("SopReturnOrderNo", returnCode);

		        //请求数据
		     ResponseContentOne<ReturnOrderList> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getReturnOrderUrl(), paramMap, ReturnOrderList.class);
		     ResponseContentOne<ReturnOrderList> rOne= returnOrderLogic(responseContentOne,userInfo.getSopUserNo());
		     return exportLogic(response,rOne);
		}	
	}
	
    @RequestMapping(value = "/modify/{returnCode}")
    public ResponseContentOne<String> modifyOrder(@PathVariable("returnCode") String returnCode,
             @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SopUserNo", userInfo.getSopUserNo());
        JSONArray statusJson = new JSONArray();
        statusJson.add(returnCode);
        paramMap.put("SopReturnOrderNos", statusJson);

        //请求数据
        return baseService.requestAPI4One(ApiServiceUrlConfig.getReturnModifyOrder(), paramMap, String.class);

    }
	private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
    /***wh
	 * 返货明细导出
	 * @param
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/returnOrderList")
	public String returnOrderList(@RequestParam(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "start",required = false) String updateTimeBegin,
            @RequestParam(value = "end",required = false) String updateTimeEnd,
            @RequestParam(value = "pageSize",required = false) Integer pageSize,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ReturnOrderPage page = null;
		//"2017031001865"
         Map<String, Object> paramMap = new HashMap<>();
         paramMap.put("SopUserNo", userInfo.getSopUserNo());
    	 String [] arr = updateTimeBegin.split("-");
    	 paramMap.put("BeginTime",arr[2]+"-"+arr[1]+"-"+arr[0]+" 00:00:00");
	 
    	 String [] arrEnd = updateTimeEnd.split("-");
    	 paramMap.put("EndTime",arrEnd[2]+"-"+arrEnd[1]+"-"+arrEnd[0]+" 23:59:59");
    
	     paramMap.put("PageIndex", ""+pageIndex);
	     paramMap.put("PageSize", ""+pageSize);
	        //请求数据
	     ResponseContentOne<ReturnOrderPage> result = null;
	     String requestJson = FastJsonUtil.serialize2String(paramMap);
	     List<ReturnOrder> listReturnOrder = null;
	     ApiContentStr apiContentStr = baseService.postApi(ApiServiceUrlConfig.getReturnOrderList(), requestJson);
	     if(apiContentStr!=null&&apiContentStr.getIsSuccess()==true){
	    	 page = new ReturnOrderPage();
	    	 int total = 0;
	    	String returnJson = apiContentStr.getMessageRes();
	    	page = FastJsonUtil.deserializeString2Obj(returnJson,ReturnOrderPage.class);
	    	listReturnOrder = page.getOrderSendReturnList();
	    	total = page.getTotal();
	    	if(listReturnOrder!=null&&listReturnOrder.size()>0){
	    		for(ReturnOrder returnOrder : listReturnOrder){
	    			String supplierSkuNo = returnOrder.getSupplierSkuNo();
	    			String supplierId = userInfo.getSopUserNo();
	    			List<Product> lists = productService.list(supplierId, supplierSkuNo);
	    			if(lists!=null&&lists.size()>0){
	    				Product product = lists.get(0);
	    				String productName = product.getProductName();
	    				String brandName = product.getBrandName();
	    				returnOrder.setBrandName(brandName);
	    				returnOrder.setProductName(productName);
	    			}
	    		}
	    	}
	    	page.setTotal(total);
	    }else{
	    	result = ResponseContentOne.errorResp("无数据");	
	    }
	    result = ResponseContentOne.successResp(page);
		return FastJsonUtil.serialize2StringEmpty(result);
	     
	}
	  
//	@RequestMapping(value = "/exportReturnList")
//	public String returnExport(@RequestParam(value = "pageIndex") Integer pageIndex,
//            @RequestParam(value = "start",required = false) String updateTimeBegin,
//            @RequestParam(value = "end",required = false) String updateTimeEnd,
//            @RequestParam(value = "pageSize",required = false) Integer pageSize,
//            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
//			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		ReturnOrderPage page = null;
//		//"2017031001865"
//         Map<String, Object> paramMap = new HashMap<>();
//         paramMap.put("SopUserNo", userInfo.getSopUserNo());
//    	 String [] arr = updateTimeBegin.split("-");
////    	 paramMap.put("BeginTime",arr[2]+"-"+arr[1]+"-"+arr[0]+" 00:00:00");
//	 
//    	 String [] arrEnd = updateTimeEnd.split("-");
//    	 paramMap.put("EndTime",arrEnd[2]+"-"+arrEnd[1]+"-"+arrEnd[0]+" 23:59:59");
//    
//	     paramMap.put("PageIndex", ""+pageIndex);
//	     paramMap.put("PageSize", ""+pageSize);
//	        //请求数据
//	     ResponseContentOne<ReturnOrderPage> result = null;
//	     String requestJson = FastJsonUtil.serialize2String(paramMap);
//	     List<ReturnOrder> listReturnOrder = null;
//	     ApiContentStr apiContentStr = baseService.postApi(ApiServiceUrlConfig.getReturnOrderList(), requestJson);
//	     if(apiContentStr!=null&&apiContentStr.getIsSuccess()==true){
//	    	 page = new ReturnOrderPage();
//	    	 int total = 0;
//	    	String returnJson = apiContentStr.getMessageRes();
//	    	page = FastJsonUtil.deserializeString2Obj(returnJson,ReturnOrderPage.class);
//	    	listReturnOrder = page.getOrderSendReturnList();
//	    	total = page.getTotal();
//	    	if(listReturnOrder!=null&&listReturnOrder.size()>0){
//	    		for(ReturnOrder returnOrder : listReturnOrder){
//	    			String supplierSkuNo = returnOrder.getSupplierSkuNo();
//	    			String supplierId = userInfo.getSopUserNo();
//	    			List<Product> lists = productService.list(supplierId, supplierSkuNo);
//	    			if(lists!=null&&lists.size()>0){
//	    				Product product = lists.get(0);
//	    				String productName = product.getProductName();
//	    				String brandName = product.getBrandName();
//	    				returnOrder.setBrandName(brandName);
//	    				returnOrder.setProductName(productName);
//	    			}
//	    		}
//	    	}
//	    	page.setTotal(total);
//	    }else{
//	    	result = ResponseContentOne.errorResp("无数据");	
//	    }
//	    result = ResponseContentOne.successResp(page);
//		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
//		List<ReturnOrderDetail> importRS = response.getContent().getReturnOrders().get(0).getReturnOrderDetails();
//		for(int i=0;i<importRS.size();i++){
//			HashMap<String, Object> result=new HashMap<String, Object>();
//			result.put("sopReturnOrderNo", importRS.get(i).getSopReturnOrderNo());
//			result.put("brand", importRS.get(i).getBrandName()); 
//			result.put("supplierSku", importRS.get(i).getSupplierSkuNo()); 
//			result.put("product", importRS.get(i).getProductMsg()); 
//			result.put("returnReason",importRS.get(i).getMemo());
//			result2.add(result);
//		}
//		if (result2!=null && result2.size()>0) {
//			response.setContentType("application/x-download");// 设置为下载application/x-download
//			String fileName = "ReturnBeforeDetail";
//			response.addHeader("Content-Disposition",
//					"attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
//			OutputStream out = response.getOutputStream();
//			String[] headers = {"OrderCode","Brand","Supplier SKU","Item Name","Return Reason"};
//			String[] columns = { "sopReturnOrderNo", "brand", "supplierSku","product","returnReason"};
//			ExportExcelUtils.exportExcel(fileName, headers, columns, result2, out, "");
//			out.close();
//			return null;
//		}
//		 return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Not Data"));
//	}
}
