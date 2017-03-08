package com.shangpin.ephub.product.business.rest.hub.spu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: huaPendingSpu校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubSpuService {
	
	@Autowired
	HubSpuGateWay hubSpuGateWay;
	
	/**
	 * 根据品牌编号和货号查询是否已存在hubSpu表中
	 * @param brandNo
	 * @param spuModel
	 * @return
	 */
	public HubSpuDto  findHubSpuByBrandNoAndSpuModel(String brandNo,String spuModel){
		
		if(brandNo!=null&&spuModel!=null){
			HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
			criteria.createCriteria().andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel);
			List<HubSpuDto> listSpu = hubSpuGateWay.selectByCriteria(criteria);
			if(listSpu!=null&&listSpu.size()>0){
				return listSpu.get(0);
			}
		}
		return null;
	}

	public HubSpuDto findHubSpuByHubSpuNo(String hubSpuNo) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuNoEqualTo(hubSpuNo);
		List<HubSpuDto> listSpu = hubSpuGateWay.selectByCriteria(criteria);
		if(listSpu!=null&&listSpu.size()>0){
			return listSpu.get(0);
		}
		return null;
	}
}
