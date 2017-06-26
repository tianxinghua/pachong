package com.shangpin.ephub.product.business.ui.studio.common.operation.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotShootState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;

public class OperationServiceImpl implements OperationService {
	
	@Autowired
	private StudioGateWay studioGateWay;
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;

	@Override
	public List<StudioSlotDto> slotList(OperationQuery operationQuery) throws Exception {
		StudioSlotCriteriaDto criteria = getStudioSlotCriteria(operationQuery);
		return studioSlotGateWay.selectByCriteria(criteria);
	}
	
	/**
	 * 将传入的页面条件转换为数据库查询条件
	 * @param operationQuery
	 * @return
	 * @throws Exception
	 */
	private StudioSlotCriteriaDto getStudioSlotCriteria(OperationQuery operationQuery) throws Exception{
		StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
		criteria.setOrderByClause("slot_no"); 
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		Criteria createCriteria = criteria.createCriteria();
		createCriteria.andArriveStatusEqualTo(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
		if(operationQuery.getOperationQueryType() == OperationQueryType.OPEN_BOX.getIndex()){
			createCriteria.andShotStatusEqualTo(StudioSlotShootState.WAIT_SHOOT.getIndex().byteValue());
		}else if(operationQuery.getOperationQueryType() == OperationQueryType.IMAGE_UPLOAD.getIndex()){
			createCriteria.andShotStatusEqualTo(StudioSlotShootState.NORMAL.getIndex().byteValue());
		}
		
		Long studioId = getStudioId(operationQuery.getStudioNo());
		if(null != studioId){
			createCriteria.andStudioIdEqualTo(studioId);
		}else{
			throw new Exception("未获得摄影棚编号");
		}
		if(StringUtils.isNotBlank(operationQuery.getTrackingNo())){
			createCriteria.andTrackNoEqualTo(operationQuery.getTrackingNo());
		}
		if(StringUtils.isNotBlank(operationQuery.getSlotName())){
			createCriteria.andSlotNoLike(operationQuery.getSlotName()+"%");
		}
		List<String> operateDate = operationQuery.getOperateDate();
		if(CollectionUtils.isNotEmpty(operateDate)){
			Date startDate = DateTimeUtil.parse(operationQuery.getOperateDate().get(0));
			createCriteria.andShootTimeGreaterThanOrEqualTo(startDate);
		}
		if(CollectionUtils.isNotEmpty(operateDate) && operateDate.size() > 1){
			Date endDate = DateTimeUtil.parse(operationQuery.getOperateDate().get(1)); 
			createCriteria.andShootTimeLessThanOrEqualTo(endDate);
		}
		/*
		if(null != openBoxQuery.getPageIndex() && null != openBoxQuery.getPageSize()){
			criteria.setPageNo(openBoxQuery.getPageIndex());
			criteria.setPageSize(openBoxQuery.getPageSize()); 
		}*/
		return criteria;
	}
	
	@Override
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

}
