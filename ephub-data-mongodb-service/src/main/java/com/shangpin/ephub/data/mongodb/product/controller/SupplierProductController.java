package com.shangpin.ephub.data.mongodb.product.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mongodb.product.servcie.SupplierProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:SupplierProductController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:10:44
 */
@RestController
@RequestMapping("/supplier-product")
@Slf4j
public class SupplierProductController {

	@Autowired
	private SupplierProductService supplierProductService;
	
	@RequestMapping(value = "/save")
	public boolean save(@RequestBody Map<String, Object> data){
		Boolean result = null; 
		long start = System.currentTimeMillis();
		try {
			supplierProductService.save(data);
			result = true;
			log.info("MongoDB保存数据完毕，耗时{}milliseconds!", System.currentTimeMillis() - start);
		} catch (Throwable e) {
			result = false;
			log.error("保存数据发生异常："+e.getMessage(), e);
			e.printStackTrace();
		}
		return result;
	}
}
