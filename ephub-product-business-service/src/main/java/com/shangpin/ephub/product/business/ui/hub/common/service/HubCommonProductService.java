package com.shangpin.ephub.product.business.ui.hub.common.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
/**
 * <p>Title:HubCommonProductService </p>
 * <p>Description: 针对hub_spu/hub_sku这两张表有公用的功能，写到这里</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:35:47
 *
 */
@Service
public class HubCommonProductService {
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 返回HubSpuCriteriaWithRowBoundsDto对象
	 * @param hubQuryDto
	 * @return
	 * @throws Exception
	 */
	public HubSpuCriteriaWithRowBoundsDto getHubSpuCriteriaWithRowBoundsByHubQuryDto(HubQuryDto hubQuryDto) throws Exception{
		HubSpuCriteriaWithRowBoundsDto boundsDto = new HubSpuCriteriaWithRowBoundsDto();
		if(!StringUtils.isEmpty(hubQuryDto.getPageIndex()) && !StringUtils.isEmpty(hubQuryDto.getPageSize())){
			RowBoundsDto rowBounds =new RowBoundsDto(hubQuryDto.getPageIndex(),hubQuryDto.getPageSize());
			boundsDto.setRowBounds(rowBounds);
			HubSpuCriteriaDto criteria = getHubSpuCriteriaDtoByHubQuryDto(hubQuryDto);
			boundsDto.setCriteria(criteria);
		}
		return boundsDto;
	}
	/**
	 * 返回HubSpuCriteriaDto对象
	 * @param hubQuryDto
	 * @return
	 */
	public HubSpuCriteriaDto getHubSpuCriteriaDtoByHubQuryDto(HubQuryDto hubQuryDto){
		HubSpuCriteriaDto criteriaDto = new HubSpuCriteriaDto();		
		if(!StringUtils.isEmpty(hubQuryDto.getPageIndex()) && !StringUtils.isEmpty(hubQuryDto.getPageSize())){
			criteriaDto.setPageNo(hubQuryDto.getPageIndex());
			criteriaDto.setPageSize(hubQuryDto.getPageSize()); 
		}
		Criteria criteria = criteriaDto.createCriteria();
		if(!StringUtils.isEmpty(hubQuryDto.getSpuModel())){
			criteria = criteria.andSpuModelEqualTo(hubQuryDto.getSpuModel());
		}
		if(!StringUtils.isEmpty(hubQuryDto.getBrandNo())){
			criteria = criteria.andBrandNoEqualTo(hubQuryDto.getBrandNo());
		}
		String hubCategoryNo = hubQuryDto.getCategoryNo();
		if(!StringUtils.isEmpty(hubCategoryNo)){
			if(hubCategoryNo.length() < 12){
				criteria.andCategoryNoLike(hubCategoryNo+"%");
			}else{
				criteria.andCategoryNoEqualTo(hubCategoryNo);
			}
		}
		if(!StringUtils.isEmpty(hubQuryDto.getStartUpdateTime())){
			criteria = criteria.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.convertFormat(hubQuryDto.getStartUpdateTime(),dateFormat));
		}
		if(!StringUtils.isEmpty(hubQuryDto.getEndUpdateTime())){
			criteria = criteria.andUpdateTimeLessThan(DateTimeUtil.convertFormat(hubQuryDto.getEndUpdateTime(),dateFormat));
		}
//		if(hubQuryDto.getSpuState() == (byte)1){//TODO 这块要写枚举
//			criteria = criteria.andSpuStateEqualTo((byte)1);
//		}else if(hubQuryDto.getSpuState() == (byte)0){
//			criteria = criteria.andSpuStateEqualTo((byte)0);
//		}
		return criteriaDto;

	}
	
	
}
