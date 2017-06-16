package com.shangpin.ephub.product.business.rest.studio.studio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 17/6/8.
 */
@Service
@Slf4j
public class StudioDicSlotService {
	@Autowired
	private StudioDicSlotGateWay studioDicSlotGateWay;
	public List<StudioDicSlotDto> getStudioDicSlotByStudioId(long studioId){
		log.info("查询摄影棚批次基础信息----start");
		StudioDicSlotCriteriaDto dto = new StudioDicSlotCriteriaDto();
		dto.createCriteria().andStudioIdEqualTo(studioId);
		List<StudioDicSlotDto> listStudioDicSlotDto = studioDicSlotGateWay.selectByCriteria(dto);
		log.info("查询摄影棚批次基础信息----end");
		return listStudioDicSlotDto;
	}
}
