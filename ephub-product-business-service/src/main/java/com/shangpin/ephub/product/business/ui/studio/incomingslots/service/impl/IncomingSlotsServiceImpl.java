package com.shangpin.ephub.product.business.ui.studio.incomingslots.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSendState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.ConfirmQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.IncomingSlotsQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.service.IncomingSlotsService;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.vo.IncomingSlotDto;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title: IncomingSlotsServiceImpl</p>
 * <p>Description: 样品收货页面所有的业务逻辑实现 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月20日 下午3:27:24
 *
 */
@Service
@Slf4j
public class IncomingSlotsServiceImpl implements IncomingSlotsService {
	
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	@Autowired
	private OperationService operationService;

	@Override
	public List<IncomingSlotDto> list(IncomingSlotsQuery query) {
		try {
			log.info("样品收货页面查询参数："+JsonUtil.serialize(query)); 
			List<IncomingSlotDto> prioritySlots = new ArrayList<IncomingSlotDto>();
			StudioSlotCriteriaDto criteria = formatStudioSlotCriteria(query);
			List<StudioSlotDto> list  = studioSlotGateWay.selectByCriteria(criteria );
			log.info("样品收货页面共查询到："+list.size()+"条数据。");  
			String today = DateTimeUtil.format(new Date());
			if(CollectionUtils.isNotEmpty(list)){
				List<IncomingSlotDto> secondarySlots = new ArrayList<IncomingSlotDto>();
				for(StudioSlotDto dto : list){
					IncomingSlotDto slotDto = convert(dto);
					String planArriveTime = DateTimeUtil.format(dto.getPlanArriveTime());
					if(today.equals(planArriveTime)){
						prioritySlots.add(slotDto);
					}else{
						secondarySlots.add(slotDto);
					}
				}
				prioritySlots.addAll(secondarySlots);
			}
			return prioritySlots;
		} catch (Exception e) {
			log.error("样品收货页面查询异常："+e.getMessage(),e); 
		}
		
		return null;
	}
	/**
	 * 转换
	 * @param dto
	 * @return
	 */
	private IncomingSlotDto convert(StudioSlotDto dto){
		IncomingSlotDto slotDto = new IncomingSlotDto();
		slotDto.setStudioSlotId(dto.getStudioSlotId());
		slotDto.setSlotNo(dto.getSlotNo());
		List<StudioSlotSpuSendDetailDto> list = operationService.selectDetail(dto.getSlotNo());
		slotDto.setQty(list.size());
		slotDto.setSender(dto.getSendUser());
		slotDto.setSendingDate(dto.getSendTime());
		slotDto.setETA(dto.getPlanArriveTime());
		slotDto.setTrackingNo(dto.getTrackNo());
		return slotDto;
	}
	/**
	 * 条件转换
	 * @param query
	 * @return
	 * @throws Exception
	 */
	private StudioSlotCriteriaDto formatStudioSlotCriteria(IncomingSlotsQuery query) throws Exception {
		StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(100);
		criteria.setOrderByClause("plan_arrive_time");
		Criteria createCriteria = criteria.createCriteria();
		createCriteria.andArriveStatusEqualTo(StudioSlotArriveState.NOT_ARRIVE.getIndex().byteValue()).andSendStateEqualTo(StudioSlotSendState.SEND.getIndex().byteValue());
//		Long studioId = operationService.getStudioId(query.getStudioNo());
		if(StringUtils.isNotBlank(query.getStudioNo())){
			createCriteria.andStudioIdEqualTo(Long.valueOf(query.getStudioNo())); 
		}else{
			throw new Exception("未获得摄影棚编号");
		}
		if(StringUtils.isNotBlank(query.getTrackingNo())){
			createCriteria.andTrackNoEqualTo(query.getTrackingNo());
		}
		if(StringUtils.isNotBlank(query.getPlanArriveStartTime())){
			Date startDate = DateTimeUtil.parse(query.getPlanArriveStartTime());
			createCriteria.andPlanArriveTimeGreaterThanOrEqualTo(startDate);
		}
		if(StringUtils.isNotBlank(query.getPlanArriveEndTime())){
			Date endDate = DateTimeUtil.parse(query.getPlanArriveEndTime());
			createCriteria.andPlanArriveTimeLessThan(endDate);
		}
		return criteria;
	}

	@Override
	public boolean confirm(ConfirmQuery confirmQuery) {
		try {
			log.info("样品收货确认接受到参数："+JsonUtil.serialize(confirmQuery)); 
			List<Long> ids = confirmQuery.getIds();
			if(CollectionUtils.isNotEmpty(ids )){
				StudioSlotWithCriteriaDto withCriteria = new StudioSlotWithCriteriaDto();
				StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
				criteria.createCriteria().andStudioSlotIdIn(ids);
				withCriteria.setCriteria(criteria );
				StudioSlotDto studioSlot =  new StudioSlotDto();
				studioSlot.setArriveStatus(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
				studioSlot.setSlotStatus(StudioSlotState.RECEIVED.getIndex().byteValue());
//				studioSlot.setShotStatus(StudioSlotShootState.WAIT_SHOOT.getIndex().byteValue());
				Date date = new Date();
				studioSlot.setArriveTime(date);
				studioSlot.setUpdateTime(date); 
				studioSlot.setArriveUser(confirmQuery.getArriveUser());  
				withCriteria.setStudioSlot(studioSlot );
				studioSlotGateWay.updateByCriteriaSelective(withCriteria);
				return true;
			}
		} catch (Exception e) {
			log.error("样品收货确认异常："+e.getMessage(),e); 
		}
		return false;
	}

}
