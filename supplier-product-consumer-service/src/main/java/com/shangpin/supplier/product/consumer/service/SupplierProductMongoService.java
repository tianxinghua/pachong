package com.shangpin.supplier.product.consumer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mongodb.product.supplier.gateway.SupplierProductGateway;
import com.shangpin.ephub.client.data.mongodb.product.supplier.gateway.dto.SupplierProductDto;
import com.shangpin.supplier.product.consumer.util.ReflectBeanUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: SupplierProductMongoService</p>
 * <p>Description: 涉及到mongodb的业务类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月22日 下午4:37:41
 *
 */
@Service
@Slf4j
public class SupplierProductMongoService {
	
	@Autowired
	private SupplierProductGateway gateway;
	
	/**
	 * 保存原始数据到mongodb
	 * @param supplierId
	 * @param supplierSpuNo
	 * @param spuDto
	 */
	public <T> void save(String supplierId,String supplierSpuNo,T spuDto){
		try {
			SupplierProductDto supplierProductDto = new SupplierProductDto();
			Map<String, Object> data = ReflectBeanUtils.objectToMap(spuDto);
			supplierProductDto.setProduct(data);
			supplierProductDto.setId(supplierId+"_"+supplierSpuNo);
			boolean flag = gateway.save(supplierProductDto);
			if(flag){
				log.info(supplierId+"_"+supplierSpuNo+" 保存原始到mongodb成功"); 
			}
		} catch (Exception e) {
			log.error(supplierId+"_"+supplierSpuNo+" 保存原始到mongodb失败："+e.getMessage()); 
		}
		
	}

}
