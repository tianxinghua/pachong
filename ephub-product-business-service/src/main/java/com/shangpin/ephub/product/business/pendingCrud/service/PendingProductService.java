package com.shangpin.ephub.product.business.pendingCrud.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.pendingCrud.util.JavaUtil;
import com.shangpin.ephub.product.business.pendingCrud.vo.PendingProductDto;
import com.shangpin.ephub.product.business.util.DateTimeUtil;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;

@Service
public class PendingProductService {
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSkuPendingGateWay hubSkuPendingGateWay;

	/**
	 * 根据页面查询条件查询待处理表产品
	 * @param pendingQuryDto
	 * @return
	 */
	public List<PendingProductDto> findPendingProduct(PendingQuryDto pendingQuryDto){
		List<PendingProductDto> products = new ArrayList<PendingProductDto>();
		if(null !=pendingQuryDto){
			HubSpuPendingCriteriaWithRowBoundsDto rowBoundsDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
			List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteriaWithRowbounds(rowBoundsDto);
			if(null != pendingSpus && pendingSpus.size()>0){				
				for(HubSpuPendingDto pendingSpu : pendingSpus){
					PendingProductDto pendingProduct = convertHubSpuPendingDto2PendingProductDto(pendingSpu);
					List<HubSkuPendingDto> hubSkus = findPendingSkuBySpuPendingId(pendingSpu.getSpuPendingId());
					pendingProduct.setHubSkus(hubSkus);
					products.add(pendingProduct);
				}
			}
		}
		return products;
	}
	/**
	 * 根据spu查找sku
	 * @param spuPendingId
	 * @return
	 */
	public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId){
		HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
		criteriaDto.setFields("hub_sku_size");
		return hubSkuPendingGateWay.selectByCriteria(criteriaDto);		
	}
	
	/**
	 * 将pendingSpu转化为pendingProduct
	 * @param pendingSpu
	 * @return
	 */
	public PendingProductDto convertHubSpuPendingDto2PendingProductDto(HubSpuPendingDto pendingSpu){
		PendingProductDto pendingProduct = new PendingProductDto();		
		JavaUtil.fatherToChild(pendingSpu, pendingProduct); 
		return pendingProduct;
	}
	
	/**
	 * 将UI查询条件转换成数据库查询条件对象
	 * @param pendingQuryDto UI查询条件对象
	 * @return
	 */
	private HubSpuPendingCriteriaWithRowBoundsDto findhubSpuPendingCriteriaFromPendingQury(PendingQuryDto pendingQuryDto){
		HubSpuPendingCriteriaWithRowBoundsDto rowBoundsDto = new HubSpuPendingCriteriaWithRowBoundsDto();
		if(!StringUtils.isEmpty(pendingQuryDto.getPageIndex()) && !StringUtils.isEmpty(pendingQuryDto.getPageSize())){
			RowBoundsDto rowBounds = new RowBoundsDto(pendingQuryDto.getPageIndex(),pendingQuryDto.getPageSize());
			rowBoundsDto.setRowBounds(rowBounds);
		}
		HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
		Criteria criteria = hubSpuPendingCriteriaDto.createCriteria();
		if(!StringUtils.isEmpty(pendingQuryDto.getSupplierName())){
//			criteria.andsupplierid
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getSpuModel())){
			criteria = criteria.andSpuModelEqualTo(pendingQuryDto.getSpuModel());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubCategory())){
			criteria = criteria.andHubCategoryNoLike(pendingQuryDto.getHubCategory());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubBrand())){
			criteria = criteria.andHubBrandNoLike(pendingQuryDto.getHubBrand());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason())){
			criteria = criteria.andHubSeasonEqualTo(pendingQuryDto.getHubSeason());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubYear())){
//			criteria = criteria.and
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getStatTime())){
			criteria = criteria.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.convertFormat(pendingQuryDto.getStatTime(), dateFormat));
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getEndTime())){
			criteria = criteria.andUpdateTimeLessThan(DateTimeUtil.convertFormat(pendingQuryDto.getEndTime(),dateFormat));
		}		
		criteria = criteria.andCatgoryStateEqualTo((byte) 1);
		
		rowBoundsDto.setCriteria(hubSpuPendingCriteriaDto);
		return rowBoundsDto;
	}
}
