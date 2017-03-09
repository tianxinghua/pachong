package com.shangpin.ephub.product.business.common.service.check.property;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.rest.model.service.impl.HubBrandModelRuleService;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class SpuModelCheck extends CommonCheckBase {

	@Autowired
	HubBrandModelRuleService hubBrandModelRuleService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
		
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuModelState()!=null&&hubSpuPendingIsExist.getSpuModelState()==SpuModelState.VERIFY_PASSED.getIndex()){
			return null;
		}
		
		hubSpuPendingIsExist.setSpuModel(spuPendingDto.getSpuModel());
		if(checkBrandModel(hubSpuPendingIsExist,spuPendingDto)){
			return null;
		}else{
			return "货号校验失败";
		}
	}
	
	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuModelState()!=null&&hubSpuPendingIsExist.getSpuModelState()==SpuModelState.VERIFY_PASSED.getIndex()){
			return true;
		}
		return checkOrSetBrandModel(hubSpuPendingIsExist,spuPendingDto);
	}
	
	protected boolean checkOrSetBrandModel(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
		String spuModel = null;
		if (!StringUtils.isEmpty(hubSpuPending.getHubBrandNo()) && !StringUtils.isEmpty(hubSpuPending.getSpuModel())) {
			spuModel = hubBrandModelRuleService.regexVerify(hubSpuPending.getHubBrandNo(),
					hubSpuPending.getHubCategoryNo(), hubSpuPending.getSpuModel());
			if (spuModel != null) {
				hubSpuPendingIsExist.setSpuModel(spuModel);
				hubSpuPendingIsExist.setSpuModelState(SpuModelState.VERIFY_PASSED.getIndex());
				return true;
			}
		}
		hubSpuPendingIsExist.setSpuModel(hubSpuPending.getSpuModel());
		hubSpuPendingIsExist.setSpuModelState(SpuModelState.VERIFY_FAILED.getIndex());
		return false;
	}
	
	protected boolean checkBrandModel(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
		String spuModel = null;
		if (!StringUtils.isEmpty(hubSpuPending.getHubBrandNo()) && !StringUtils.isEmpty(hubSpuPending.getSpuModel())) {
			spuModel = hubBrandModelRuleService.regexVerify(hubSpuPending.getHubBrandNo(),
					hubSpuPending.getHubCategoryNo(), hubSpuPending.getSpuModel());
			if (spuModel != null) {
				hubSpuPendingIsExist.setSpuModel(spuModel);
				hubSpuPendingIsExist.setSpuModelState(SpuModelState.VERIFY_PASSED.getIndex());
				return true;
			}
		}
		hubSpuPendingIsExist.setSpuModelState(SpuModelState.VERIFY_FAILED.getIndex());
		return false;
	}
}
