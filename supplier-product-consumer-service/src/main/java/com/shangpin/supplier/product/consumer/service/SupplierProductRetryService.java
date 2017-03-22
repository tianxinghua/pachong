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
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProducts;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.manager.SupplierProductRetryManager;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
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
	private static final Integer PAGESIZE = 100;
	
	/**
	 * 处理供应商商品
	 * @param picDtos
	 */
	public void processProduct(Byte state) throws Exception{
		
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.createCriteria().andInfoStateEqualTo(state);
		criteria.setPageNo(1);
		criteria.setPageSize(PAGESIZE);
		List<HubSupplierSpuDto> products = supplierProductPictureManager.findSupplierProduct(criteria);
		
		if(products!=null&&products.size()>0){
			
			for(HubSupplierSpuDto spu : products){
				loopProduct(spu,state);
				updateSupplierInfoState(spu);
			}
			if(products.size()==PAGESIZE){
				processProduct(state);
			}
		}
	}
	private void updateSupplierInfoState(HubSupplierSpuDto spu) {
		HubSupplierSpuDto hubSupplierSpu = new HubSupplierSpuDto();
    	hubSupplierSpu.setSupplierSpuId(spu.getSupplierSpuId());
    	hubSupplierSpu.setInfoState((byte)0);
    	hubSupplierSpu.setUpdateTime(new Date());
    	supplierProductPictureManager.updateSupplierSpu(hubSupplierSpu);		
	}

	private void loopProduct(HubSupplierSpuDto spu,byte state) throws Exception{
		
		HubSeasonDicDto season = supplierProductPictureManager.findCurrentSeason(spu.getSupplierId());
		if(season==null){
			log.info("===="+spu.getSupplierId()+":"+spu.getSupplierSpuId()+":"+spu.getSupplierSeasonname()+"非当季");
			return;
		}
	
		HubSupplierValueMappingDto supplier = supplierProductPictureManager.findHubSupplierValueMapping(spu.getSupplierId());
		if(supplier==null){
			log.info("===="+spu.getSupplierId()+"未找到供应商名称");
			return;
		}
 
    	Spu spuHead = new Spu();
		spuHead.setSupplierId(spu.getSupplierId());
		spuHead.setSpuNo(spu.getSupplierSpuNo());
		spuHead.setStatus(state);
		
	   	PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierNo(supplier.getHubValNo()); 
		pendingProduct.setSupplierId(spu.getSupplierId());
		pendingProduct.setSupplierName(supplier.getHubVal());
		
		PendingSpu pendingSpu = new PendingSpu();
		supplierProductMysqlService.convertHubSpuToPendingSpu(spu, pendingSpu);
		pendingSpu.setSupplierNo(supplier.getHubValNo());
		pendingProduct.setData(pendingSpu);
		
		List<PendingSku> skus = new ArrayList<PendingSku>();
		//开始构造消息头
		
		List<HubSupplierSkuDto> hubSkus = supplierProductMysqlService.findSupplierSkuBySupplierIdAndSupplierSpuId(spu.getSupplierId(),spu.getSupplierSpuId());
		
		List<Sku> headSkus = new ArrayList<Sku>();		
		if(hubSkus != null && hubSkus.size()>0){
			for(HubSupplierSkuDto hubSku : hubSkus){
				try {					
					Sku headSku = new Sku();
					PendingSku pendingSku = new PendingSku();
					//开始保存hubSku到数据库
					hubSku.setSupplierSpuId(pendingSpu.getSupplierSpuId()); //在这里回写supplierSpuId
					convertHubSkuToPendingSku(hubSku,pendingSku);
					skus.add(pendingSku);
					headSku.setSupplierId(spu.getSupplierId());
					headSku.setSkuNo(hubSku.getSupplierSkuNo());
					headSku.setStatus(state);
					headSkus.add(headSku);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}				
			}
		}else{
			log.info("===="+spu.getSupplierId()+":"+spu.getSupplierSpuId()+"无sku信息");
		}
		pendingSpu.setSkus(skus);
		pendingProduct.setData(pendingSpu);		
		spuHead.setSkus(headSkus);	
		
		
		Map<String,String> headers = new HashMap<String,String>();	
    	headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
    	supplierProductSendToPending.dispatchSupplierProduct(pendingProduct, headers);
    	
	}
	private void convertHubSkuToPendingSku(HubSupplierSkuDto hubSku, PendingSku pendingSku) throws Exception {
		pendingSku.setSupplierId(hubSku.getSupplierId());
		pendingSku.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		pendingSku.setHubSkuSize(hubSku.getSupplierSkuSize());
		pendingSku.setMarketPrice(hubSku.getMarketPrice());
		pendingSku.setMarketPriceCurrencyorg(hubSku.getMarketPriceCurrencyorg());
		pendingSku.setSalesPrice(hubSku.getSalesPrice());
		pendingSku.setSalesPriceCurrency(hubSku.getSalesPriceCurrency());
		pendingSku.setSkuName(hubSku.getSupplierSkuName());
		pendingSku.setStock(hubSku.getStock());
		pendingSku.setSupplierBarcode(hubSku.getSupplierBarcode());
		pendingSku.setSupplyPrice(hubSku.getSupplyPrice());
		pendingSku.setSupplyPriceCurrency(hubSku.getSupplyPriceCurrency());
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
