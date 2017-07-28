package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioDicSlotVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioDicSlotInfo;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title: OpenBoxController
 * </p>
 * <p>
 * Description: 批次基础信息管理接口
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author wangchao
 * @date 2017年7月24日
 *
 */
@Slf4j
@Service
public class StudioDicSlotManageService {
	@Autowired
	StudioGateWay studioGateWay;
	@Autowired
	StudioDicSlotGateWay studioDicSlotGateWay;
	
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");

	public HubResponse<?> updateStudioDicSlot(StudioManageQuery studioManageQuery) {
		log.info("updateStudioDicSlot--------------start");
		log.info("编辑摄影棚批次基础信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		try {
//			StudioDicSlotDto dto = studioDicSlotGateWay.selectByPrimaryKey(studioManageQuery.getStudioDicSlotId());
//			if(dto==null){
//				return HubResponse.errorResp("studioDicSlotId:"+studioManageQuery.getStudioDicSlotId()+"不存在对应此编号的摄影棚基础批次信息!");
//			}
			//qa 调用mysql服务有问题I直接调用selectByPrimaryKey找不到
			StudioDicSlotCriteriaDto studioDicSlotCriteriaDto = new StudioDicSlotCriteriaDto();
			com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto.Criteria studioDicSlotcriteria = studioDicSlotCriteriaDto.createCriteria();
			studioDicSlotcriteria.andStudioDicSlotIdEqualTo(studioManageQuery.getStudioDicSlotId());
			List<StudioDicSlotDto> StudioDicSlotDtoLists = studioDicSlotGateWay.selectByCriteria(studioDicSlotCriteriaDto);
            if(StudioDicSlotDtoLists==null||StudioDicSlotDtoLists.size()==0){
            	return HubResponse.errorResp("studioDicSlotId:"+studioManageQuery.getStudioDicSlotId()+"不存在对应此编号的摄影棚基础批次信息!");
            }
			
            StudioDicSlotDto dto = StudioDicSlotDtoLists.get(0);
			if(studioManageQuery.getSlotNumber()!=null){
				dto.setSlotNumber(studioManageQuery.getSlotNumber());
			}
			if(studioManageQuery.getSlotMinNumber()!=null){
				dto.setSlotMinNumber(studioManageQuery.getSlotMinNumber());
			}
			if(studioManageQuery.getSlotEfficiency()!=null){
				dto.setSlotEfficiency(studioManageQuery.getSlotEfficiency());
			}
			if(studioManageQuery.getUpdateUser()!=null){
				dto.setUpdateUser(studioManageQuery.getUpdateUser());
			}
			dto.setUpdateTime(new Date());
			studioDicSlotGateWay.updateByPrimaryKeySelective(dto);
		} catch (Exception e) {
			log.error("编辑摄影棚批次基础信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("编辑摄影棚批次基础信息失败!");
		}
		log.info("updateStudioDicSlot--------------end");
		return HubResponse.successResp("编辑成功！");
	}
	
	public HubResponse<?> selectStudioDicSlot(StudioManageQuery studioManageQuery) {
		log.info("selectStudioDicSlot--------------start");
		log.info("查询摄影棚批次基础信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		StudioDicSlotVo vo = new StudioDicSlotVo();
		List<StudioDicSlotInfo> dicSlotDtoLists = new ArrayList<>();
		try {
			StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
			Criteria criteria = criteriaDto.createCriteria();
			
			if(studioManageQuery.getStudioName()!=null){
				criteria.andStudioNameEqualTo(studioManageQuery.getStudioName());
			}
			criteriaDto.setPageSize(20);
			List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
			if(studioDtoLists==null||studioDtoLists.size()==0){
				return HubResponse.errorResp("不存在摄影棚批次基础信息!");
			}
			for(StudioDto studioDto : studioDtoLists){
				StudioDicSlotCriteriaDto studioDicSlotCriteriaDto = new StudioDicSlotCriteriaDto();
				com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto.Criteria studioDicSlotcriteria = studioDicSlotCriteriaDto.createCriteria();
				studioDicSlotcriteria.andStudioIdEqualTo(studioDto.getStudioId());
				List<StudioDicSlotDto> StudioDicSlotDtoLists = studioDicSlotGateWay.selectByCriteria(studioDicSlotCriteriaDto);
				if(StudioDicSlotDtoLists!=null&&StudioDicSlotDtoLists.size()!=0){
					StudioDicSlotInfo info = new StudioDicSlotInfo();
					info.setStudioDicSlotId(StudioDicSlotDtoLists.get(0).getStudioDicSlotId());
					info.setStudioName(studioDto.getStudioName());
					info.setSlotNumber(StudioDicSlotDtoLists.get(0).getSlotNumber());
					info.setSlotMinNumber(StudioDicSlotDtoLists.get(0).getSlotMinNumber());
					info.setSlotEfficiency(StudioDicSlotDtoLists.get(0).getSlotEfficiency());
					info.setUpdateTime(sdfomat.parse(sdfomat.format(new Date())));
					info.setUpdateUser(studioManageQuery.getUpdateUser());
					dicSlotDtoLists.add(info);
				}
			}
			vo.setStudioDicSlotDtoList(dicSlotDtoLists);
		} catch (Exception e) {
			log.error("查询摄影棚批次基础信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("查询摄影棚批次基础信息失败!");
		}
		log.info("selectStudioDicSlot--------------end");
		return HubResponse.successResp(vo);
	}
}
