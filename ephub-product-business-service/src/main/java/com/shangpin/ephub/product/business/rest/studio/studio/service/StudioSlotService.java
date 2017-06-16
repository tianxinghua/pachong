package com.shangpin.ephub.product.business.rest.studio.studio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 17/6/8.
 */
@Service
@Slf4j
public class StudioSlotService {
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	
	public List<StudioSlotDto> getStudioSlotByCreateDate(String DT){
		List<StudioSlotDto> listStudioDto = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			log.info("查询当天是否生成了批次信息----start");
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			String startDate = DT+" 00:00:00";
			String endDate = DT+" 23:59:59";
			Date sDate = sdf.parse(startDate);
			Date eDate = sdf.parse(endDate);
			dto.createCriteria().andPlanShootTimeBetween(sDate, eDate);
			listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			log.info("查询当天是否生成了批次信息----end");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listStudioDto; 
	}
	public void insertStudioSlot(StudioSlotDto dto){
		try {
			studioSlotGateWay.insert(dto);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("slotNo:"+dto.getSlotNo()+"创建摄影棚批次出错!");
		}
		
	}
	
	public void selectAndUpdateStudioSlotBeforePlanDate(Date nowDate){
		List<StudioSlotDto> listStudioDto = null;
		log.info("查询并更新当天计划拍摄日期之前未被申请的批次信息----start");
		StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
		dto.createCriteria().andPlanShootTimeLessThan(nowDate).andApplyStatusEqualTo((byte) 0);
		listStudioDto = studioSlotGateWay.selectByCriteria(dto);
		for(StudioSlotDto studioSlotDto : listStudioDto){
			studioSlotDto.setApplyStatus((byte) 3);//3 已过期
			studioSlotGateWay.updateByPrimaryKey(studioSlotDto);
		}
		log.info("查询并更新当天计划拍摄日期之前未被申请的批次信息----end");
	}
}
