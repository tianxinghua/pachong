package com.shangpin.ephub.product.business.ui.studio.openbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.shangpin.ephub.client.data.studio.enumeration.ArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotShootState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioArriveState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.openbox.dto.OpenBoxQuery;
import com.shangpin.ephub.product.business.ui.studio.openbox.service.OpenBoxService;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.CheckDetailVo;
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
		try {
			OpenBoxVo openBoxVo = new OpenBoxVo();
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
			return openBoxVo;
		} catch (Exception e) {
			log.error("开箱质检页面查询异常："+e.getMessage(),e);
		}
		return null;
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
		criteria.createCriteria().andArriveStatusEqualTo(ArriveState.ARRIVED.getIndex().byteValue())
		.andShotStatusEqualTo(StudioSlotShootState.WAIT_SHOOT.getIndex().byteValue());
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
	public Long getStudioId(String studioNo) {
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
	@Override
	public boolean slotDetailCheck(String slotNoSpuId) {
		try {
			String slotNo = slotNoSpuId.substring(0, slotNoSpuId.indexOf("-"));
			Long spuPendingId = Long.valueOf(slotNoSpuId.substring(slotNoSpuId.indexOf("-") + 1));
			StudioSlotSpuSendDetailWithCriteriaDto withCriteria = new StudioSlotSpuSendDetailWithCriteriaDto();
			StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
			criteria.createCriteria().andSlotNoEqualTo(slotNo).andSpuPendingIdEqualTo(spuPendingId);
			withCriteria.setCriteria(criteria );
			StudioSlotSpuSendDetailDto studioSlotSpuSendDetailDto = new StudioSlotSpuSendDetailDto();
			studioSlotSpuSendDetailDto.setArriveState(StudioSlotStudioArriveState.RECEIVED.getIndex().byteValue());
			withCriteria.setStudioSlotSpuSendDetailDto(studioSlotSpuSendDetailDto );
			studioSlotSpuSendDetailGateWay.updateByCriteria(withCriteria);
			return true;
		} catch (Exception e) {
			log.error("扫码质检异常："+e.getMessage(),e); 
		}
		return false;
	}
	@Override
	public CheckDetailVo checkResult(String slotNo) {
		try {
			CheckDetailVo checkDetailVo = new CheckDetailVo();
			//先更新批次状态
			StudioSlotWithCriteriaDto slotWithCriteria = new StudioSlotWithCriteriaDto();
			StudioSlotCriteriaDto slotCriteria = new StudioSlotCriteriaDto();
			slotCriteria.createCriteria().andSlotNoEqualTo(slotNo);
			slotWithCriteria.setCriteria(slotCriteria );
			StudioSlotDto studioSlotDto =  new StudioSlotDto();
			studioSlotDto.setShotStatus(StudioSlotShootState.NORMAL.getIndex().byteValue());
			studioSlotDto.setShootTime(new Date()); 
			slotWithCriteria.setStudioSlotDto(studioSlotDto );
			studioSlotGateWay.updateByCriteria(slotWithCriteria);
			//TODO 暂时没有盘盈
			//下面是盘亏
			StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
			criteria.setOrderByClause("create_time");
			criteria.setPageNo(1);
			criteria.setPageSize(1000); 
			criteria.createCriteria().andSlotNoEqualTo(slotNo).andArriveStateEqualTo(StudioSlotStudioArriveState.NOT_ARRIVE.getIndex().byteValue()); 
			List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
			if(CollectionUtils.isNotEmpty(list)){
				checkDetailVo.setInventoryLosses(list); 
			}
			return checkDetailVo;
		} catch (Exception e) {
			log.info("确认批次时异常："+e.getMessage(),e); 
		}
		return null;
	}

}
