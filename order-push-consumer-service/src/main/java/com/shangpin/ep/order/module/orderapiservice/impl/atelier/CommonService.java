package com.shangpin.ep.order.module.orderapiservice.impl.atelier;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.dto.SizeDto;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.response.HubResponse;
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.bean.HubSkuCriteria;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonService {
	
	@Autowired
    private HubSkuMapper skuDAO;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private SupplierProperties supplierProperties;

	/**
     * 根据供应商门户编号和供应商skuid查找尺码
     * @param supplierId
     * @param supplierSkuId
     * @return
     */
    public String getProductSize(String supplierId,String supplierSkuId){
    	try {
    		HubSkuCriteria skuCriteria  = new HubSkuCriteria();
        	skuCriteria.createCriteria().andSupplierIdEqualTo(supplierId).andSkuIdEqualTo(supplierSkuId);
        	skuCriteria.setFields("PRODUCT_SIZE");
        	List<HubSku> list = skuDAO.selectByExample(skuCriteria);
        	if(CollectionUtils.isNotEmpty(list)){
        		return list.get(0).getProductSize();
        	}else{
        		return getSizeFromEphub(supplierId, supplierSkuId);
        	}
		} catch (Exception e) {	
			log.error("获取尺码异常："+e.getMessage(),e);
			return "";
		}
    }
    
    public String getSizeFromEphub(String supplierId, String supplierSkuNo){
    	SizeDto sizeDto = new SizeDto();
    	sizeDto.setSupplierId(supplierId);
    	sizeDto.setSupplierSkuNo(supplierSkuNo);
    	String getSizeFromEphubUrl = supplierProperties.getSupplier().getGetSizeFromEphubUrl();
		HubResponse<String> response = restTemplate.postForObject(getSizeFromEphubUrl, sizeDto, HubResponse.class);
    	if("0".equals(response.getCode())){
    		return response.getContent();
    	}else{
    		return "";
    	}
    }
}
