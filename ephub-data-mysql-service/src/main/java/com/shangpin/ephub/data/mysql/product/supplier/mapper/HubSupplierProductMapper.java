package com.shangpin.ephub.data.mysql.product.supplier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shangpin.ephub.data.mysql.product.supplier.bean.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.data.mysql.product.supplier.po.HubSupplierProduct;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@Mapper
public interface HubSupplierProductMapper {
	
	List<HubSupplierProduct> selectHubSupplierProductByPage(HubSupplierProductRequestWithPage request);
	
	int count(HubSupplierProductRequestWithPage request);
	
	void insert(HubSupplierProduct product);
}