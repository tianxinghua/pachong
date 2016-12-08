package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierDate;
/**
 * <p>Title:AtelierCommonHandler </p>
 * <p>Description: atelier供应商通用处理逻辑类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午8:54:28
 *
 */
@Component
public class AtelierCommonHandler {

	/**
	 * atelier通用处理主流程
	 * @param message
	 * @param headers
	 */
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers){
		if(!StringUtils.isEmpty(message.getData())){
			AtelierDate atelierDate = new Gson().fromJson(message.getData(),AtelierDate.class);
			
		}
		
	}
	
}
