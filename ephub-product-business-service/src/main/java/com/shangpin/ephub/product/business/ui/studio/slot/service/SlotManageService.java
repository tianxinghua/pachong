package com.shangpin.ephub.product.business.ui.studio.slot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.SlotManageQuery;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 批次管理接口</p>
 * <p>Company: </p> 
 * @author zhaogenchun
 * @date 2017年6月12日 
 *
 */
@Service
public class SlotManageService {

	@Autowired
	StudioSlotGateWay studioSlotGateWay;
	public HubResponse<?> findSlotManageList(SlotManageQuery slotManageQuery) {
		StudioSlotCriteriaDto studioSlotCriteriaDto = new StudioSlotCriteriaDto();
		studioSlotGateWay.countByCriteria(studioSlotCriteriaDto);
		return null;
	}

}
