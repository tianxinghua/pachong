package com.shangpin.ephub.product.business.rest.order.controller;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.product.business.rest.order.dto.SpuQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.order.dto.SizeDto;
import com.shangpin.ephub.product.business.rest.order.service.OrderService;
import com.shangpin.ephub.response.HubResponse;



/**
 * <p>Title: OrderService</p>
 * <p>Description: 为订单服务提供一些ephub的服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月21日 下午5:57:26
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;



	/**
	 * 根据供应商门户编号和供应商原始sku编号查找原始尺码
	 * @return
	 */
	@RequestMapping(value= "/product-size", method= RequestMethod.POST)
	public HubResponse<?> findProductSize(@RequestBody SizeDto sizeDto){
		String size = orderService.findProductSize(sizeDto.getSupplierId(), sizeDto.getSupplierSkuNo());
		if(StringUtils.isEmpty(size)){
			return HubResponse.errorResp("查询失败");
		}else{
			return HubResponse.successResp(size);
		}
	}


	@RequestMapping(value= "/supplier-spu", method= RequestMethod.POST)
	public HubResponse<?> findSupplierSpu(@RequestBody SpuQueryVO queryVO){
		HubSupplierSpuDto supplierSpuDto = orderService.getSupplierSpu(queryVO.getSupplierId(), queryVO.getSupplierSpuNo());
		if(null== supplierSpuDto){
			return HubResponse.errorResp("查询失败");
		}else{
			return HubResponse.successResp(supplierSpuDto);
		}
	}
	/**
	 * 根据供应商门户编号和供应商原始sku编号查找SupplierSpuId
	 * @return
	 */
	@RequestMapping(value = "/supplier-spu-id", method= RequestMethod.POST)
	public HubResponse<?> findSupplierSpuId(@RequestBody SizeDto sizeDto){
		Long supplierSpuId = orderService.findSupplierSpuId(sizeDto.getSupplierId(), sizeDto.getSupplierSkuNo());
		if(StringUtils.isEmpty(supplierSpuId)){
			return HubResponse.errorResp("查询失败");
		}else{
			return HubResponse.successResp(supplierSpuId);
		}
	}
	/**
	 * 根据SupplierSpuId查找供应商原始颜色
	 * @return
	 */
	@RequestMapping(value = "/supplier-spu-color", method= RequestMethod.POST)
	public HubResponse<?> findSupplierSpuColor(@RequestBody SizeDto sizeDto){
		String supplierSpuColor = orderService.findSupplierSpuColor(sizeDto.getSupplierSpuId());
		if(StringUtils.isEmpty(supplierSpuColor)){
			return HubResponse.errorResp("查询失败");
		}else{
			return HubResponse.successResp(supplierSpuColor);
		}
	}
}
