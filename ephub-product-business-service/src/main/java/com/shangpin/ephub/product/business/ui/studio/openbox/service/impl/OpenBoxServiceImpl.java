package com.shangpin.ephub.product.business.ui.studio.openbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
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
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	private OperationService operationService;
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;

	@Override
	public OpenBoxVo slotList(OperationQuery openBoxQuery) {
		try {
			openBoxQuery.setOperationQueryType(OperationQueryType.OPEN_BOX.getIndex());
			OpenBoxVo openBoxVo = new OpenBoxVo();
			log.info("开箱质检页面接受到的查询参数："+JsonUtil.serialize(openBoxQuery)); 
			List<StudioSlotDto>  list = operationService.slotList(openBoxQuery);
			log.info("开箱质检页面共查询到："+list.size()+"条数据。");  
			String today = DateTimeUtil.format(new Date());
			if(CollectionUtils.isNotEmpty(list)){
				List<StudioSlotVo> prioritySlots = new ArrayList<StudioSlotVo>();
				List<StudioSlotVo> secondarySlots = new ArrayList<StudioSlotVo>();
				for(StudioSlotDto studioSlotDto : list){
					String slotDate = DateTimeUtil.format(studioSlotDto.getSlotDate());
					if(today.equals(slotDate)){
						StudioSlotVo slotVo = operationService.formatDto(studioSlotDto);
						prioritySlots.add(slotVo);
					}else{
						StudioSlotVo slotVo = operationService.formatDto(studioSlotDto);
						secondarySlots.add(slotVo);
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
	
	@Override
	public OpenBoxDetailVo slotDetail(String slotNo) {
		log.info("查找该批次号下的所以产品详情====="+slotNo);
		OpenBoxDetailVo openBoxDetailVo = new OpenBoxDetailVo();
		List<StudioSlotSpuSendDetailVo> list = operationService.slotDetail(slotNo);
		log.info("返回的产品详情size========"+list.size()); 
		openBoxDetailVo.setDetails(list); 
		return openBoxDetailVo; 
	}
	
	@Override
	public boolean slotDetailCheck(String slotNoSpuId) {
		try {
//			String slotNo = slotNoSpuId.substring(0, slotNoSpuId.indexOf("-"));
//			String slotSpuNo = slotNoSpuId.substring(slotNoSpuId.indexOf("-") + 1);
			StudioSlotSpuSendDetailWithCriteriaDto withCriteria = new StudioSlotSpuSendDetailWithCriteriaDto();
			StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
			criteria.createCriteria().andBarcodeEqualTo(slotNoSpuId);
			withCriteria.setCriteria(criteria );
			StudioSlotSpuSendDetailDto studioSlotSpuSendDetailDto = new StudioSlotSpuSendDetailDto();
			studioSlotSpuSendDetailDto.setArriveState(StudioSlotStudioArriveState.RECEIVED.getIndex().byteValue());
			studioSlotSpuSendDetailDto.setArriveTime(new Date());
			withCriteria.setStudioSlotSpuSendDetail(studioSlotSpuSendDetailDto );
			studioSlotSpuSendDetailGateWay.updateByCriteriaSelective(withCriteria);
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
			StudioSlotDto studioSlotDtoSelect = selectStudioSlotDto(slotCriteria);
			slotWithCriteria.setCriteria(slotCriteria );
			StudioSlotDto studioSlotDto =  new StudioSlotDto();
			Date date = new Date();
			long today = DateTimeUtil.convertDayDate(date).getTime();
			long planShootTime = DateTimeUtil.convertDayDate(null != studioSlotDtoSelect ? studioSlotDtoSelect.getPlanShootTime() : date).getTime();
			if(planShootTime == today){
				studioSlotDto.setShotStatus(StudioSlotShootState.NORMAL.getIndex().byteValue());
			}else if(planShootTime > today){
				studioSlotDto.setShotStatus(StudioSlotShootState.DELAY_SHOOT.getIndex().byteValue());
			}else{
				studioSlotDto.setShotStatus(StudioSlotShootState.AHEAD_TIME.getIndex().byteValue());
			}
			studioSlotDto.setShootTime(date); 
			slotWithCriteria.setStudioSlot(studioSlotDto );
			studioSlotGateWay.updateByCriteriaSelective(slotWithCriteria);
			//TODO 暂时没有盘盈
			//下面是盘亏
			StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
			criteria.setOrderByClause("create_time");
			criteria.setPageNo(1);
			criteria.setPageSize(1000); 
			criteria.createCriteria().andSlotNoEqualTo(slotNo).andArriveStateEqualTo(StudioSlotStudioArriveState.NOT_ARRIVE.getIndex().byteValue()); 
			List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
			if(CollectionUtils.isNotEmpty(list)){
				List<StudioSlotSpuSendDetailVo> details = new ArrayList<StudioSlotSpuSendDetailVo>();
				for(StudioSlotSpuSendDetailDto dto : list){
					StudioSlotSpuSendDetailVo vo = convertDto(dto);
					details.add(vo);
				}
				checkDetailVo.setInventoryLosses(details); 
			}
			return checkDetailVo;
		} catch (Exception e) {
			log.info("确认批次时异常："+e.getMessage(),e); 
		}
		return null;
	}

	private StudioSlotDto selectStudioSlotDto(StudioSlotCriteriaDto slotCriteria) {
		slotCriteria.setFields("plan_shoot_time");
		List<StudioSlotDto> list = studioSlotGateWay.selectByCriteria(slotCriteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
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
		vo.setStudioCode(dto.getBarcode());
		vo.setTime(dto.getCreateTime());
		return vo;
	}

}
