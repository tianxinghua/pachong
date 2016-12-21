package com.shangpin.ephub.product.business.ui.hub.waitselected.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dao.HubWaitSelectedRequestDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseDto;

/**
 * <p>
 * Title:SupplierOrderService.java
 * Company: www.shangpin.com
 * @author zhaogenchun
 * @date 2016年12月21日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
public class HubSpuService {
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired 
	HubSpuGateWay hubSpuGateWay;
	public List<HubSpuDto> findHubSpuList(HubWaitSelectedRequestDto dto) {
		
		HubSpuCriteriaWithRowBoundsDto hubSpuCriteriaWithRowBoundsDto = new HubSpuCriteriaWithRowBoundsDto();
		HubSpuCriteriaDto hubSpuCriteriaDto = new HubSpuCriteriaDto();
		HubSpuCriteriaDto.Criteria criteria = hubSpuCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(dto.getProductCode())){
			criteria.andSpuModelEqualTo(dto.getProductCode());	
		}
		if(StringUtils.isNotBlank(dto.getBrandNo())){
			criteria.andBrandNoEqualTo(dto.getBrandNo());	
		}
		if(StringUtils.isNotBlank(dto.getCategoryNo())){
			criteria.andCategoryNoEqualTo(dto.getCategoryNo());	
		}
		if(StringUtils.isNotBlank(dto.getProductState())){
			criteria.andDataStateEqualTo((byte)Integer.parseInt(dto.getProductCode()));	
		}
		hubSpuCriteriaWithRowBoundsDto.setCriteria(hubSpuCriteriaDto);
		RowBoundsDto RowBounds = new RowBoundsDto(dto.getPageNo(),dto.getPageSize());
		hubSpuCriteriaWithRowBoundsDto.setRowBounds(RowBounds);
		List<HubSpuDto> hubSpuList = hubSpuGateWay.selectByCriteriaWithRowbounds(hubSpuCriteriaWithRowBoundsDto);
		
		return hubSpuList;
	}

}
