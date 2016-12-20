package com.shangpin.ephub.product.business.service.pending.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.pendingCrud.emumeration.ProductState;
import com.shangpin.ephub.product.business.pendingCrud.util.JavaUtil;
import com.shangpin.ephub.product.business.pendingCrud.vo.PendingProductDto;
import com.shangpin.ephub.product.business.service.pending.IPendingProductService;
import com.shangpin.ephub.product.business.util.DateTimeUtil;

@Service
public class PendingProductService implements IPendingProductService{
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSkuPendingGateWay hubSkuPendingGateWay;

	@Override
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
	@Override
	public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId){
		HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
		criteriaDto.setFields("sku_pending_id,hub_sku_size,sp_sku_size_state");
		return hubSkuPendingGateWay.selectByCriteria(criteriaDto);		
	}
	@Override
	public void updatePendingProduct(PendingProductDto pendingProductDto){
		if(null != pendingProductDto){
			//TODO 先验证，验证通过则更新
			hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
			List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
			if(null != pengdingSkus && pengdingSkus.size()>0){
				for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){
					//TODO 先验证，验证通过则更新
					hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
				}
			}
		}
	}
	
	
	/***************************************************************************************************************************/
						//以下为内部调用私有方法
	/**************************************************************************************************************************/
	
	/**
	 * 将pendingSpu转化为pendingProduct
	 * @param pendingSpu
	 * @return
	 */
	private PendingProductDto convertHubSpuPendingDto2PendingProductDto(HubSpuPendingDto pendingSpu){
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
		rowBoundsDto.setCriteria(hubSpuPendingCriteriaDto);
		return rowBoundsDto;
	}
	/**
	 * 跟据不符合項，筛选不符合的产品
	 * @param pendingQuryDto UI查询条件
	 * @param pendingProduct 待验证的产品，需要验证图片/品牌/颜色/货号等等是否不符合，如果不符合则需要返回
	 * @param products 不符合项需要添加的List
	 */
	private void screenProduct(PendingQuryDto pendingQuryDto,PendingProductDto pendingProduct,List<PendingProductDto> products){
		if(null != pendingQuryDto.getInconformities() && pendingQuryDto.getInconformities().size()>0){
			for(Integer item : pendingQuryDto.getInconformities()){
				if(item == ProductState.PICTURE_STATE.getIndex()){
					if(pendingProduct.getPicState() == PicState.NO_PIC.getIndex() || pendingProduct.getPicState() == PicState.PIC_INFO_NOT_COMPLETED.getIndex()){
						products.add(pendingProduct);
						break;
					}					
				}else if(item == ProductState.SPU_MODEL_STATE.getIndex()){
					if(pendingProduct.getSpuModelState() == SpuModelState.VERIFY_FAILED.getIndex()){
						products.add(pendingProduct);
						break;
					}
				}else if(item == ProductState.CATGORY_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_BRAND_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_GENDER_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_SEASON_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_COLOR_STATE.getIndex()){
					
				}else if(item == ProductState.MATERIAL_STATE.getIndex()){

				}else if(item == ProductState.ORIGIN_STATE.getIndex()){

				}
			}
		}
	}
}
