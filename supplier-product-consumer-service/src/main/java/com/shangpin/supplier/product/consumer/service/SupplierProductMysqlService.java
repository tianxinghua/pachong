package com.shangpin.supplier.product.consumer.service;

import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
/**
 * <p>Title:SupplierProductMysqlService </p>
 * <p>Description: Supplier表的增删改查Service</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 下午7:24:08
 *
 */
@Component
public class SupplierProductMysqlService {

	/**
	 * 判断hubSpu是否存在或主要信息发生变化
	 * @param hubSpu
	 * @return
	 */
	public ProductStatus isHubSpuChanged(HubSupplierSpu hubSpu){
		return ProductStatus.NEW;
	}
}
