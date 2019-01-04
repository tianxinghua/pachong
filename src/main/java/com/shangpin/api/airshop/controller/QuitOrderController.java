package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.*;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.common.utils.FastJsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 返货
 * Created by ZRS on 2016/1/14.
 */
@RestController
@RequestMapping("/quit")
@SessionAttributes(Constants.SESSION_USER) //添加到session作用域
public class QuitOrderController {

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
            @PathVariable(value = "status") String status,
            @PathVariable(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "start",required = false) String updateTimeBegin,
            @RequestParam(value = "end",required = false) String updateTimeEnd,
            @RequestParam(value = "pageSize",required = false,defaultValue="30") Integer pageSize,
            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        
        if (StringUtils.isEmpty(updateTimeBegin) || StringUtils.isEmpty(updateTimeEnd)) {
        	updateTimeBegin="";
        	updateTimeEnd="";
		}
        JSONObject param = new JSONObject();
        param.put("UpdateTimeBegin", DateFormat.TimeFormatChangeToString(updateTimeBegin, "dd-MM-yyyy", "yyyy-MM-dd") );
        param.put("UpdateTimeEnd", DateFormat.TimeFormatChangeToString(updateTimeEnd,"dd-MM-yyyy", "yyyy-MM-dd") );
        JSONArray statusJson = new JSONArray();
        statusJson.add(status);
        param.put("ReturnOrderStatus", statusJson);
        param.put("PageIndex", pageIndex );
        param.put("PageSize", pageSize );
        param.put("SopUserNo", userInfo.getSopUserNo());
       
        //请求数据
        return  baseService.getReturnAfterOrder(ApiServiceUrlConfig.getQuitListUrl(), param);
        
        
        /*//组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("UpdateTimeBegin", updateTimeBegin);
        paramMap.put("UpdateTimeEnd", updateTimeEnd);
        JSONArray statusJson = new JSONArray();
        statusJson.add(status);
        paramMap.put("ReturnOrderStatus", statusJson);
        paramMap.put("PageIndex", pageIndex );
        paramMap.put("PageSize", pageSize );
        paramMap.put("SopUserNo", userInfo.getSopUserNo());

        //请求数据
        return baseService.requestAPI4One(ApiServiceUrlConfig.getQuitListUrl(), paramMap, QuitOrderList.class);*/
    }

    @RequestMapping(value = "/order/{returnCode}")
    public ResponseContentOne<QuitOrderList> getOrderItem(@PathVariable("returnCode") String returnCode,
             @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SopUserNo", userInfo.getSopUserNo());
        paramMap.put("SopSecondReturnOrderNo", returnCode);

        //请求数据
        ResponseContentOne<QuitOrderList> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getQuitOrderUrl(), paramMap, QuitOrderList.class);
        
        return quitOrderLogic(responseContentOne,userInfo.getSopUserNo());
    }
    /***
     * 掉Api信息
     * @param responseContentOne
     * @return
     */
    public  ResponseContentOne<QuitOrderList> quitOrderLogic(ResponseContentOne<QuitOrderList> responseContentOne,String sopUserNo) {
    	QuitOrderList returnOrderList = responseContentOne.getContent();

        //详情只有一个
        QuitOrders returnOrders = returnOrderList.getSecondReturnOrders().get(0);
        List<QuitOrderDetail> returnOrderDetails = returnOrders.getSecondReturnOrderDetails();

        //遍历sku
        Set<String> skuNoSet = new HashSet<>();
        for (QuitOrderDetail returnOrderDetail : returnOrderDetails) {
            String supplierSkuNo = returnOrderDetail.getSupplierSkuNo();
            if(!StringUtils.isEmpty(supplierSkuNo)){
                skuNoSet.add(supplierSkuNo);
            }
            returnOrderDetail.setProductMsg("");
        }

        //获取商品信息
        Map<String, Product> productMap = productService.list(sopUserNo, skuNoSet);
        if(productMap == null || productMap.isEmpty()){
            return responseContentOne;
        }

        //添加sku
        for (QuitOrderDetail returnOrderDetail : returnOrderDetails) {
            returnOrderDetail.setMemo("");
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
    /***wh
	 * 退货明细导出
	 * @param
	 * @param userInfo
	 * @param
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listExport")
	public String detailExport(String returnCode,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(returnCode)) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
		}else{
			//组装参数
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("SopUserNo", userInfo.getSopUserNo());
	        paramMap.put("SopSecondReturnOrderNo", returnCode);

	        //请求数据
	        ResponseContentOne<QuitOrderList> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getQuitOrderUrl(), paramMap, QuitOrderList.class);
	        ResponseContentOne<QuitOrderList> rOne= quitOrderLogic(responseContentOne,userInfo.getSopUserNo());
		    return exportLogic(response,rOne);
		}		
	}
	
    /**
     * 退货单明细导出
     * @param response
     * @param responseContentOne
     * @throws Exception
     */
	private String exportLogic(HttpServletResponse response, ResponseContentOne<QuitOrderList> responseContentOne)throws Exception {
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		List<QuitOrderDetail> importRS = responseContentOne.getContent().getSecondReturnOrders().get(0).getSecondReturnOrderDetails();
		for(int i=0;i<importRS.size();i++){
			HashMap<String, Object> result=new HashMap<String, Object>();
			result.put("sopReturnOrderNo", importRS.get(i).getSopSecondReturnOrderNo());
			result.put("brand", importRS.get(i).getBrandName()); 
			result.put("supplierSku", importRS.get(i).getSupplierSkuNo()); 
			result.put("product", importRS.get(i).getProductMsg()); 
			result.put("returnReason",importRS.get(i).getMemo());
			result2.add(result);
		}
		if (result2!=null && result2.size()>0) {
			response.setContentType("application/x-download");// 设置为下载application/x-download
			String fileName = "ReturnAfterDetail";
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
        return baseService.requestAPI4One(ApiServiceUrlConfig.getQuitModifyOrderrUrl(), paramMap, String.class);

    }
}
