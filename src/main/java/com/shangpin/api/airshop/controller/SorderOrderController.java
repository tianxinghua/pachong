package com.shangpin.api.airshop.controller;


import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.dto.DeliveryOrdersRQ;
import com.shangpin.api.airshop.dto.PurAccountRQ;
import com.shangpin.api.airshop.dto.PurAccountRS;
import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.request.PurchaseRequest;
import com.shangpin.api.airshop.service.FindOrderService;
import com.shangpin.api.airshop.service.SorderOrderService;
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
@RequestMapping("/sorder")
@SessionAttributes({Constants.SESSION_USER}) //添加到session作用域
public class SorderOrderController {
	
	@Autowired
	private SorderOrderService sorderOrderService;
	
	/***
	 * 结算单单查询列表  
	 * @param finddorderRs
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public JSONObject list(@RequestParam(value="pageIndex",defaultValue="1") String pageIndex
			,@RequestParam(value="pageSize",defaultValue="30") String pageSize
			,@RequestParam(value="SopPurchaseOrderNo",defaultValue="") String SopPurchaseOrderNo
			,@RequestParam(value="updateTimeBegin",defaultValue="")String updateTimeBegin
			,@RequestParam(value="updateTimeEnd",defaultValue="")String updateTimeEnd
			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return sorderOrderService.statementList(userInfo.getSopUserNo(), SopPurchaseOrderNo, updateTimeBegin, updateTimeEnd, pageIndex, pageSize);
		
	}
	
	/***
	 * 结算单单查询列表  OVER
	 * @param finddorderRs
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/lists", produces = "text/html;charset=UTF-8")
	public String lists(@Valid final PurAccountRQ purAccountRQ,
			@ModelAttribute(Constants.SESSION_USER) UserInfo uInfo,
			ModelMap model, HttpServletRequest request) {
		purAccountRQ.setSopUserNo(uInfo.getSopUserNo());
		if (StringUtils.isEmpty(purAccountRQ.getPageIndex()) || StringUtils.isEmpty(purAccountRQ.getPageSize())) {
			return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
		}
		
		PurAccountRS pRs = sorderOrderService.sorderOrderList(purAccountRQ);
		if(null == pRs){
			return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("返回数据为空"));
		}
		ResponseContentOne<PurAccountRS> result = ResponseContentOne.successResp(pRs);
		return FastJsonUtil.serialize2StringEmpty(result);
	}
	
	/***wh
	 * 采购单明细数据导出
	 * @param purchase
	 * @param userInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listExport")
	public String detailExport(PurAccountRQ purAccountRQ,
			@ModelAttribute(Constants.SESSION_USER) UserInfo uInfo,
			HttpServletResponse response) throws Exception {
		purAccountRQ.setSopUserNo(uInfo.getSopUserNo());
		List<HashMap<String, Object>> result = sorderOrderService.findSorderExport(purAccountRQ);
		if (result!=null&&result.size()>0) {
			response.setContentType("application/x-download");// 设置为下载application/x-download
			String fileName = "供应商已完结采购单";
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
			OutputStream out = response.getOutputStream();
			String[] headers = {"Order Code","Brand","Supplier SKU","Item Name","Qty","Currency","Amount"};
			String[] columns = { "SopPurchaseOrderNo", "BrandName", "SupplierSkuNo","itemName","qty","currency","amount"};
			ExportExcelUtils.exportExcel(fileName, headers, columns, result, out, "");
			out.close();
			return null;
		}
	  return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("Not Data"));
	}
}
