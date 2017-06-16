package com.shangpin.ephub.product.business.rest.studio.studio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 17/6/8.
 */
@Service
@Slf4j
public class StudioService {
	@Autowired
	private StudioGateWay studioGateWay;
	public List<StudioDto> getAllStudio(){
		log.info("查询所有摄影棚基础信息----start");
		StudioCriteriaDto dto = new StudioCriteriaDto();
		List<StudioDto> listStudioDto = studioGateWay.selectByCriteria(dto);
		log.info("查询所有摄影棚基础信息----end");
		return listStudioDto;
	}
}
