package com.shangpin.supplier.product.message.original.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.supplier.product.message.common.e.Code;
import com.shangpin.supplier.product.message.common.response.APIRsponse;
import com.shangpin.supplier.product.message.original.conf.message.SupplierProduct;
import com.shangpin.supplier.product.message.original.service.OriginalProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:OriginalProductAPI.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午2:29:24
 */
@RestController
@Slf4j
@RequestMapping(value = "/message/api")
public class OriginalProductAPI {

	@Autowired
	private OriginalProductService originalProductService;
	@RequestMapping(value = "/original-product")
	public APIRsponse stockSync(@RequestBody SupplierProduct supplierProduct){
		long start = System.currentTimeMillis();
		APIRsponse apiRsponse = null;
		try {
			if (originalProductService.dispatchOriginalProduct(supplierProduct)) {
				apiRsponse = new APIRsponse(Code.OK.getCode(), Code.OK.getMessage());
			} else {
				apiRsponse = new APIRsponse(Code.NO.getCode(), Code.NO.getMessage());
			}
		} catch (Exception e) {
			log.error("系统发生异常："+e.getMessage(), e);
			e.printStackTrace();
			apiRsponse = new APIRsponse(Code.NO.getCode(), e.getMessage());
		}
		log.info("系统成功发送编号为{}的消息耗时{}milliseconds",supplierProduct.getMessageId(),System.currentTimeMillis()-start);
		return apiRsponse;
	}
}
