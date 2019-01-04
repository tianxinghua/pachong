package com.shangpin.api.airshop.controller;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shangpin.api.airshop.dto.DeliveryOrdersRQ;
import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.service.FindOrderService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.common.utils.FastJsonUtil;

/** 
 * 发货单
 * Date:     2016年1月14日 <br/> 
 * @author   wanghua
 * @since    JDK 7
 */
@RestController
@RequestMapping("/deliveryOrder")
@SessionAttributes({Constants.SESSION_USER}) //添加到session作用域
public class DeliveryOrderController {
	
	@Autowired
	private FindOrderService findOrderService;
	
	/***
	 * 发货单查询列表 produces这段代码请去掉，参考给迎春写的TODO
	 * 
	 * @param finddorderRs
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", produces = "text/html;charset=UTF-8")
	public String list(@Valid final DeliveryOrdersRQ deliveryOrdersRQ,
			@ModelAttribute(Constants.SESSION_USER) UserInfo uInfo, ModelMap model, HttpServletRequest request) {
		deliveryOrdersRQ.setSopUserNo(uInfo.getSopUserNo());
		if (StringUtils.isEmpty(deliveryOrdersRQ.getStart()) || StringUtils.isEmpty(deliveryOrdersRQ.getLength())) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
		}
		// 请求数据
		return findOrderService.findOrderList(deliveryOrdersRQ);
	}
	
	/**
	 * 发货单详情
	 * @author liling
	 * @param PurOrderDetailReqDTO 
	 * @return
	 */
	@RequestMapping(value = "/{deliveryOrderNo}/detail",produces = "text/html;charset=UTF-8")
	public String findDeliveryOrderDetail(@PathVariable("deliveryOrderNo")String deliveryOrderNo, @ModelAttribute(Constants.SESSION_USER) UserInfo user) {
		ResponseContentOne<PurchaseOrders> result = findOrderService.findDeliveryOrderDetail(deliveryOrderNo,user.getSopUserNo(),user.getSupplierNo());
		return FastJsonUtil.serialize2StringEmpty(result);

	}
	
	//导出发货单详情
	@RequestMapping(value="/{deliveryOrderNo}/detailExport")
	public String deliveryOrderDetailExport(@PathVariable("deliveryOrderNo")String deliveryOrderNo, @ModelAttribute(Constants.SESSION_USER) UserInfo user
			,HttpServletResponse response) throws Exception{
		
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("3","In Transit");
		map.put("4","Waiting for Resending");
		map.put("5","Canceled");
		map.put("6","Received");
		map.put("7","Order Dropped");
		
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		ResponseContentOne<PurchaseOrders> result = findOrderService.findDeliveryOrderDetail(deliveryOrderNo,user.getSopUserNo(),user.getSupplierNo());
		
		response.setContentType("application/x-download");// 设置为下载application/x-download
		String fileName = "ParcelDetail";
		response.addHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		if (result!=null&&result.getContent()!=null&&result.getContent().getPurchaseOrderDetails()!=null) {
			for (int i = 0; i < result.getContent().getPurchaseOrderDetails().size(); i++) {
				HashMap<String, Object> item=new HashMap<String, Object>();
				item.put("brandName", result.getContent().getPurchaseOrderDetails().get(i).getBrandName());
				item.put("orderNo", result.getContent().getPurchaseOrderDetails().get(i).getOrderNo()); 
				item.put("orderCode", result.getContent().getPurchaseOrderDetails().get(i).getSopPurchaseOrderNo()); 
				item.put("skuNo", result.getContent().getPurchaseOrderDetails().get(i).getSupplierSkuNo()); 
				item.put("barCode", result.getContent().getPurchaseOrderDetails().get(i).getProductModel()); 
				item.put("productName",result.getContent().getPurchaseOrderDetails().get(i).getProductName()+" "+result.getContent().getPurchaseOrderDetails().get(i).getColor()+" "+result.getContent().getPurchaseOrderDetails().get(i).getSize());
				item.put("skuPrice",result.getContent().getPurchaseOrderDetails().get(i).getSkuPrice());
				item.put("createTime",result.getContent().getPurchaseOrderDetails().get(i).getCreateTime());
				item.put("qty",result.getContent().getPurchaseOrderDetails().get(i).getQty());
				String statusCode = result.getContent().getPurchaseOrderDetails().get(i).getDetailStatus();
				if(map.containsKey(statusCode)){
					item.put("status",map.get(statusCode));
				}else{
					item.put("status","Normal");
				}
				result2.add(item);
			}
			String[] headers = {"Brand","OrderNo","Order Code","Supplier SKU","Item Code","Item Name","Price","Date","Qty","Status"};
			String[] columns = { "brandName", "orderNo", "orderCode" ,"skuNo","barCode","productName","skuPrice","createTime","qty","status"};
			ExportExcelUtils.exportExcel(fileName, headers, columns, result2, out, "");
			out.close();
		}else{
			ExportExcelUtils.exportExcel(fileName, new String[0], new String[0], result2, out, "");
		}
		return null;
	}
	
}
