package com.shangpin.message.order.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.shangpin.message.order.mongodb.entity.SupplierOrderEntity;

/**
 * <p>Title:SupplierOrderMongoDB.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午4:05:25
 */
@Repository
public class SupplierOrderMongoDB {
	
	@Autowired
	private MongoOperations  mongoOperations;
	/**
	 * 保存消息数据
	 * @param supplierOrderEntity 数据
	 */
	public void save(SupplierOrderEntity supplierOrderEntity) {
		mongoOperations.save(supplierOrderEntity);
	}
	
	
}
