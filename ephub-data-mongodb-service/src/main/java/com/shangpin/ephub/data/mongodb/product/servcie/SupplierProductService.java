package com.shangpin.ephub.data.mongodb.product.servcie;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>Title:SupplierProductService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月22日 上午11:50:00
 */
@Service
public class SupplierProductService {

	@Autowired
	private MongoTemplate mongoTemplate;
	/**
	 * 保存数据
	 * @param data 数据
	 */
	public void save(Map<String, Object> data) {
		mongoTemplate.save(data,"supplierProduct");
	}
}
