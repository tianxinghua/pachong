package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 任务队列消费
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>	
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class DataHandleService {
	
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	
	public List<HubSpuDto> selectHubSpu(HubSpuPendingDto hubPendingSpuDto) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(hubPendingSpuDto.getSpuModel()).andBrandNoEqualTo(hubPendingSpuDto.getHubBrandNo());
		return  hubSpuGateway.selectByCriteria(criteria);
	}

	public BrandModelResult checkSpuModel(HubSpuPendingDto hubPendingSpuDto) {
		BrandModelDto dto = new BrandModelDto();
		dto.setBrandMode(hubPendingSpuDto.getSpuModel());
		dto.setHubBrandNo(hubPendingSpuDto.getHubBrandNo());
		dto.setHubCategoryNo(hubPendingSpuDto.getHubCategoryNo());
		return hubBrandModelRuleGateWay.verify(dto);
	}

	public List<HubSpuPendingDto> selectPendingSpu(HubSpuPendingDto hubPendingSpuDto) {
		HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
		hubSpuPendingCriteriaDto.createCriteria().andSupplierIdEqualTo(hubPendingSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(hubPendingSpuDto.getSupplierSpuNo());
		return hubSpuPendingGateWay.selectByCriteria(hubSpuPendingCriteriaDto);
		
	}

	public List<HubSkuPendingDto> selectHubSkuPendingBySpuPendingId(HubSpuPendingDto hubSpuPendingDro) {
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(hubSpuPendingDro.getSupplierId())
				.andSpuPendingIdEqualTo(hubSpuPendingDro.getSpuPendingId());
		criteria.setPageNo(1);
		criteria.setPageSize(1000);
		 return hubSkuPendingGateWay.selectByCriteria(criteria);
	}

	public void insertHubSkuPendingDto(HubSkuPendingDto hubSkuPendingDto) {
		hubSkuPendingGateWay.insert(hubSkuPendingDto);		
	}

	public void updateHubSkuPendingByPrimaryKey(HubSkuPendingDto hubSkuPendingDto) {
		hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);		
	}

	public HubSkuDto findHubSkuBySpuNoAndSize(String hubSpuNo,String size,String sizeType) {
		HubSkuCriteriaDto sku = new HubSkuCriteriaDto();
		if (size != null && sizeType != null) {
			sku.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(size)
					.andSkuSizeTypeEqualTo(sizeType);
		} else {
			sku.createCriteria().andSpuNoEqualTo(hubSpuNo);
		}
		List<HubSkuDto> skuList = hubSkuGateWay.selectByCriteria(sku);
		if(skuList!=null&&skuList.size()>0){
			return skuList.get(0);
		}
		return null; 
	}
	
}
