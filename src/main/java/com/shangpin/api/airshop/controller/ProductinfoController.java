package com.shangpin.api.airshop.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.request.ProductListRequest;
import com.shangpin.api.airshop.service.PurOrderService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.common.utils.FastJsonUtil;

/** 
 * 商品信息接口
 * @author   liyongqiao
 * @since    JDK 7
 */
@RestController
@RequestMapping("/productinfo")
@SessionAttributes({Constants.SESSION_USER}) //添加到session作用域
public class ProductinfoController {
	
	@Autowired
	private ProductService productService;
	@RequestMapping(value = "/lists", produces = "text/html;charset=UTF-8")
	public String lists(ProductListRequest product,HttpServletRequest request,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo
			) {/*
		String pageIndex = product.getPageIndex();
		String pageSize = product.getPageSize();
		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}
		String supplierNo = userInfo.getSupplierNo();
		product.setSupplierNo(supplierNo);
		
		product.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(product.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
		product.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(product.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
		if (product.getUpdateTimeBegin()==null||product.getUpdateTimeEnd()==null) {
			product.setUpdateTimeBegin("");
			product.setUpdateTimeEnd("");
		}
		PurchaseOrders purchaseOrders = purOrderService.list(product);
		if (null == purchaseOrders) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorResp("返回数据为空"));
		}
		PurchaseOrders pOrders = purchaseOrders;
		ResponseContentOne<PurchaseOrders> result = ResponseContentOne
				.successResp(pOrders);
		return FastJsonUtil.serialize2StringEmpty(result);
		
	*/
	return null;	
	}
}
