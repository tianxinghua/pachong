package com.shangpin.ephub.product.business.ui.studio.common.operation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.gateway.HubSlotSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotShootState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotStudioArriveState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;

@Service
public class OperationServiceImpl implements OperationService {
	
	@Autowired
	private StudioGateWay studioGateWay;
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	@Autowired
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	private HubSlotSpuGateWay hubSlotSpuGateWay;
	@Autowired
	private HubSlotSpuSupplierGateway hubSlotSpuSupplierGateway;

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
	
	@Override
	public StudioSlotVo formatDto(StudioSlotDto studioSlotDto) {
		StudioSlotVo slotVo = new StudioSlotVo();
		slotVo.setSlotNo(studioSlotDto.getSlotNo());
		slotVo.setOperateDate(studioSlotDto.getShootTime());
		setDetailQty(studioSlotDto.getSlotNo(), slotVo);
		slotVo.setTrackingNo(studioSlotDto.getTrackNo()); 
		return slotVo;
	}
	/**
	 * 获取详情数量
	 * @param slotNo
	 * @return
	 */
	private void setDetailQty(String slotNo, StudioSlotVo slotVo){
		List<StudioSlotSpuSendDetailDto> list = selectDetail(slotNo);
		int qty = 0;
		int uploadQty = 0;
		if(CollectionUtils.isNotEmpty(list)){
			qty = list.size();
			for(StudioSlotSpuSendDetailDto dto : list){
				if(null != dto.getArriveState() && dto.getArriveState() == StudioSlotStudioArriveState.RECEIVED.getIndex().byteValue()){
					uploadQty ++ ;
				}
			}
		}
		slotVo.setQty(qty); 
		slotVo.setUploadQty(uploadQty);
	}
	/**
	 * 
	 * @param slotNo
	 * @return
	 */
	public List<StudioSlotSpuSendDetailDto> selectDetail(String slotNo) {
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.setFields("studio_slot_id,arrive_state,send_state");
		criteria.setOrderByClause("create_time");
		criteria.setPageNo(1);
		criteria.setPageSize(1000); 
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		return studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
	}

	@Override
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo) {
		List<StudioSlotSpuSendDetailVo> details = new ArrayList<StudioSlotSpuSendDetailVo>();
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.setOrderByClause("create_time");
		criteria.setPageNo(1);
		criteria.setPageSize(1000); 
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			for(StudioSlotSpuSendDetailDto dto : list){
				StudioSlotSpuSendDetailVo vo = convertDto(dto);
				details.add(vo);
			}
		}
		return details;
	}
	/**
	 * 转换
	 * @param dto
	 * @return
	 */
	private StudioSlotSpuSendDetailVo convertDto(StudioSlotSpuSendDetailDto dto) {
		StudioSlotSpuSendDetailVo vo = new StudioSlotSpuSendDetailVo();
		vo.setArriveState(dto.getArriveState());
		vo.setBrand(dto.getSupplierBrandName());
		vo.setItemCode(dto.getSupplierSpuModel());
		vo.setItemName(dto.getSupplierSpuName());
		vo.setOperator(dto.getUpdateUser());
//		vo.setStudioCode(dto.getSlotNo()+"-"+dto.getSlotSpuNo());
		vo.setTime(dto.getCreateTime());
		vo.setStudioCode(dto.getBarcode());
		return vo;
	}

	@Override
	public HubSlotSpuDto findSlotSpu(String slotSpuNo) {
		HubSlotSpuCriteriaDto criteria = new HubSlotSpuCriteriaDto();
		criteria.setFields("slot_spu_id");
		criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo);
		List<HubSlotSpuDto> list = hubSlotSpuGateWay.selectByCriteria(criteria );
		return list.get(0); 
	}

	@Override
	public HubSlotSpuSupplierDto findSlotSpuSupplier(String slotNo, String slotSpuNo) {
		HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo).andSlotSpuNoEqualTo(slotSpuNo).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		List<HubSlotSpuSupplierDto> list = hubSlotSpuSupplierGateway.selectByCriteria(criteria );
		return list.get(0);
	}

	@Override
	public StudioSlotSpuSendDetailDto selectSlotSpuSendDetail(String barcode) {
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.setFields("slot_no,slot_spu_no");
		criteria.createCriteria().andBarcodeEqualTo(barcode);
		List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public StudioSlotDto selectStudioSlot(String slotNo) {
		StudioSlotCriteriaDto creteria = new StudioSlotCriteriaDto();
		creteria.createCriteria().andSlotNoEqualTo(slotNo);
		List<StudioSlotDto> list = studioSlotGateWay.selectByCriteria(creteria );
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public StudioDto getStudio(Long studioId) {
		return studioGateWay.selectByPrimaryKey(studioId);
	} 

}
