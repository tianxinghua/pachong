package com.shangpin.ephub.product.business.ui.hub.all.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDto;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProducts;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.product.business.ui.hub.common.service.HubCommonProductService;

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
	private HubSkuGateWay hubSkuClient;
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
	@Override
	public List<HubProductDto> findProductDtails(Long spuId){
		HubSpuDto hubSpu = hubSpuClient.selectByPrimaryKey(spuId);
		if(null != hubSpu){
			List<HubProductDto> hubProducts = new ArrayList<HubProductDto>();
			HubSkuCriteriaDto criteriaDto = new HubSkuCriteriaDto();
			criteriaDto.createCriteria().andSpuNoEqualTo(hubSpu.getSpuNo());
			List<HubSkuDto> hubSkus =  hubSkuClient.selectByCriteria(criteriaDto);
			if(null != hubSkus && hubSkus.size()>0){
				for(HubSkuDto hubSku : hubSkus){
					HubProductDto hubProuct = covertHubSpuToHubProduct(hubSpu,hubSku);
					hubProducts.add(hubProuct);
				}
			}
			return hubProducts;
		}else{
			return null;
		}
	}
	/**
	 * 赋值转换
	 * @param hubSpu
	 * @param hubSku
	 * @return
	 */
	private HubProductDto covertHubSpuToHubProduct(HubSpuDto hubSpu,HubSkuDto hubSku){
		HubProductDto hubProuct = new HubProductDto();
		if(null != hubSpu){
//			hubProuct.
		}
		if(null != hubSku){
			
		}
		return hubProuct;
	}

}
