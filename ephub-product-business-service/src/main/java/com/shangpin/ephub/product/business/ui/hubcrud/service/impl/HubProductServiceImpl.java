package com.shangpin.ephub.product.business.ui.hubcrud.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.common.dto.HubQuryDto;
import com.shangpin.ephub.product.business.ui.common.service.HubCommonProductService;
import com.shangpin.ephub.product.business.ui.hubcrud.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hubcrud.vo.HubProducts;

/**
 * <p>Title:HubProductServiceImpl </p>
 * <p>Description: hub页面service实现类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午6:25:26
 *
 */
@Service
public class HubProductServiceImpl implements IHubProductService {
	
	@Autowired
	private HubSpuGateWay hubSpuClient;
	@Autowired
	private HubCommonProductService hubCommonProductService;

	@Override
	public HubProducts findHubProductds(HubQuryDto hubQuryDto) {
		try {
			if(null != hubQuryDto){
				HubProducts hubProducts = new HubProducts();
				HubSpuCriteriaWithRowBoundsDto rowBoundsDto = hubCommonProductService.getHubSpuCriteriaWithRowBoundsByHubQuryDto(hubQuryDto);
				int total = hubSpuClient.countByCriteria(rowBoundsDto.getCriteria());
				if(total>0){
					List<HubSpuDto> hubSpus = hubSpuClient.selectByCriteriaWithRowbounds(rowBoundsDto);
					hubProducts.setHubProducts(hubSpus);
				}
				hubProducts.setTotal(total);
				return hubProducts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
