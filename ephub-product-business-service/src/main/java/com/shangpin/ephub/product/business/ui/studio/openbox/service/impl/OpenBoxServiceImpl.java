package com.shangpin.ephub.product.business.ui.studio.openbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.enumeration.ArriveState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.openbox.dto.OpenBoxQuery;
import com.shangpin.ephub.product.business.ui.studio.openbox.service.OpenBoxService;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxDetailVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxVo;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenBoxServiceImpl implements OpenBoxService {
	
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	@Autowired
	private StudioGateWay studioGateWay;
	@Autowired
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;

	@Override
	public OpenBoxVo slotList(OpenBoxQuery openBoxQuery) {
		OpenBoxVo openBoxVo = new OpenBoxVo();
		try {
			log.info("开箱质检页面接受到的查询参数："+JsonUtil.serialize(openBoxQuery)); 
			StudioSlotCriteriaDto criteria = getStudioSlotCriteria(openBoxQuery);
			List<StudioSlotDto>  list = studioSlotGateWay.selectByCriteria(criteria);
			log.info("开箱质检页面共查询到："+list.size()+"条数据。");  
			String today = DateTimeUtil.format(new Date());
			if(CollectionUtils.isNotEmpty(list)){
				List<StudioSlotDto> prioritySlots = new ArrayList<StudioSlotDto>();
				List<StudioSlotDto> secondarySlots = new ArrayList<StudioSlotDto>();
				for(StudioSlotDto studioSlotDto : list){
					String planShootTime = DateTimeUtil.format(studioSlotDto.getPlanShootTime());
					if(today.equals(planShootTime)){
						prioritySlots.add(studioSlotDto);
					}else{
						secondarySlots.add(studioSlotDto);
					}
				}
				openBoxVo.setPrioritySlots(prioritySlots);
				openBoxVo.setSecondarySlots(secondarySlots); 
			}
			
		} catch (Exception e) {
			log.error("开箱质检页面查询异常："+e.getMessage(),e);
		}
		return openBoxVo;
	}
	/**
	 * 将传入的页面条件转换为数据库查询条件
	 * @param openBoxQuery
	 * @return
	 * @throws Exception
	 */
	private StudioSlotCriteriaDto getStudioSlotCriteria(OpenBoxQuery openBoxQuery) throws Exception{
		StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
		criteria.setOrderByClause("slot_no"); 
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		criteria.createCriteria().andArriveStatusEqualTo(ArriveState.ARRIVED.getIndex().byteValue());
//		.andShotStatusEqualTo(Studioshots);
		Long studioId = getStudioId(openBoxQuery.getStudioNo());
		if(null != studioId){
			criteria.createCriteria().andStudioIdEqualTo(studioId);
		}else{
			throw new Exception("未获得摄影棚编号");
		}
		if(StringUtils.isNotBlank(openBoxQuery.getTrackingNo())){
			criteria.createCriteria().andTrackNoEqualTo(openBoxQuery.getTrackingNo());
		}
		if(StringUtils.isNotBlank(openBoxQuery.getSlotName())){
			criteria.createCriteria().andSlotNoLike(openBoxQuery.getSlotName()+"%");
		}
		if(StringUtils.isNotBlank(openBoxQuery.getOperateStartDate())){
			Date startDate = DateTimeUtil.parse(openBoxQuery.getOperateStartDate());
			criteria.createCriteria().andShootTimeGreaterThanOrEqualTo(startDate);
		}
		if(StringUtils.isNotBlank(openBoxQuery.getOperateEndDate())){
			Date endDate = DateTimeUtil.parse(openBoxQuery.getOperateEndDate());
			criteria.createCriteria().andShootTimeLessThanOrEqualTo(endDate);
		}
		/*
		if(null != openBoxQuery.getPageIndex() && null != openBoxQuery.getPageSize()){
			criteria.setPageNo(openBoxQuery.getPageIndex());
			criteria.setPageSize(openBoxQuery.getPageSize()); 
		}*/
		return criteria;
	}
	/**
	 * 根据摄影棚编号获取主键
	 * @param studioNo
	 * @return
	 */
	private Long getStudioId(String studioNo) {
		if(StringUtils.isBlank(studioNo)){
			return null;
		}
		StudioCriteriaDto criteria = new StudioCriteriaDto();
		criteria.createCriteria().andStudioNoEqualTo(studioNo);
		List<StudioDto> list = studioGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0).getStudioId();
		}else{
			return null;
		}
	}
	@Override
	public OpenBoxDetailVo slotDetail(String slotNo) {
		OpenBoxDetailVo openBoxDetailVo = new OpenBoxDetailVo();
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.setOrderByClause("create_time");
		criteria.setPageNo(1);
		criteria.setPageSize(1000); 
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			openBoxDetailVo.setDetails(list);
		}
		return openBoxDetailVo; 
	}

}
