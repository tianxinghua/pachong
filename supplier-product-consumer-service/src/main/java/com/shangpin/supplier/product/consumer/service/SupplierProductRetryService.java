package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProducts;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.manager.SupplierProductRetryManager;
import com.shangpin.supplier.product.consumer.service.dto.Spu;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:SupplierProductRetryService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月2日 下午12:45:06
 */
@Service
@Slf4j
public class SupplierProductRetryService {

	@Autowired
	private SupplierProductRetryManager supplierProductPictureManager;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	SupplierProductMysqlService supplierProductMysqlService;
	private static final Integer PAGESIZE = 50;
	
	/**
	 * 处理供应商商品
	 * @param picDtos
	 */
	public void processProduct() throws Exception{
		
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.createCriteria().andInfoStateEqualTo((byte)2);
		int totalSize = supplierProductPictureManager.getSupplierProductRetryCount(criteria);//总记录数
    	log.info("supplierProduct待重试总记录数："+totalSize); 
    	if(totalSize > 0){
    		List<HubSupplierSpuDto> lists = new ArrayList<HubSupplierSpuDto>();
    		int pageCount = getPageCount(totalSize,PAGESIZE);//页数
    		log.info("sku导出总页数："+pageCount); 
        	for(int i =1; i <= pageCount; i++){
        		criteria.setPageNo(i);
        		criteria.setPageSize(PAGESIZE);
        		List<HubSupplierSpuDto> products = supplierProductPictureManager.findSupplierProduct(criteria);
        		lists.addAll(products);
        	}
        	Spu spuHead = null;
        	Map<String,String> headers = null;
        	PendingProduct pendingProduct = null;
        	Map<String,List<HubSupplierSpuDto>> map = new HashMap<String,List<HubSupplierSpuDto>>();
        	if(lists.size()>0){
        		for(HubSupplierSpuDto spu:lists){
        			
        			HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto = new HubSupplierValueMappingCriteriaDto();
        			hubSupplierValueMappingCriteriaDto.createCriteria().andSupplierIdEqualTo(spu.getSupplierId()).andHubValTypeEqualTo((byte)5);
        			List<HubSupplierValueMappingDto> s = supplierProductPictureManager.findHubSupplierValueMapping(hubSupplierValueMappingCriteriaDto);
        			if(s!=null&&s.size()>0){
        				spuHead = new Spu();
                		spuHead.setSupplierId(spu.getSupplierId());
                		spuHead.setSpuNo(spu.getSupplierSpuNo());
                		spuHead.setStatus(4);
                		pendingProduct = new PendingProduct();
                		pendingProduct.setSupplierNo(s.get(0).getHubValNo()); 
                		pendingProduct.setSupplierId(spu.getSupplierId());
                		pendingProduct.setSupplierName(s.get(0).getHubVal());
                		PendingSpu pendingSpu = new PendingSpu();
                		supplierProductMysqlService.convertHubSpuToPendingSpu(spu, pendingSpu);
                		pendingSpu.setSupplierNo(s.get(0).getHubValNo());
                		pendingProduct.setData(pendingSpu);
                		
                		headers = new HashMap<String,String>();	
                    	headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
                    	supplierProductSendToPending.dispatchSupplierProduct(pendingProduct, headers);
                    	HubSupplierSpuDto hubSupplierSpu = new HubSupplierSpuDto();
                    	hubSupplierSpu.setSupplierSpuId(spu.getSupplierSpuId());
                    	hubSupplierSpu.setInfoState((byte)0);
                    	hubSupplierSpu.setUpdateTime(new Date());
                    	supplierProductPictureManager.updateSupplierSpu(hubSupplierSpu);
        			}
        		}
        	}
        	

    	}
	}
	
	/**
	 * 获取总页数
	 * @param totalSize 总计路数
	 * @param pagesize 每页记录数
	 * @return
	 */
	private Integer getPageCount(Integer totalSize, Integer pageSize) {
		if(totalSize % pageSize == 0){
			return totalSize/pageSize;
		}else{
			return (totalSize/pageSize) + 1;
		}
	}
	
}
