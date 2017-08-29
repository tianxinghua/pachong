package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioVo;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title: OpenBoxController
 * </p>
 * <p>
 * Description: 摄影棚基础信息管理接口
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
public class StudioManageService {
	@Autowired
	StudioGateWay studioGateWay;
	@Autowired
	StudioDicSlotGateWay studioDicSlotGateWay;

	public HubResponse<?> addStudio(StudioManageQuery studioManageQuery) {
		log.info("addStudio--------------start");
		log.info("添加摄影棚基础信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		if(studioManageQuery.getStudioName()==null||studioManageQuery.getStudioName().equals("")){
			return HubResponse.errorResp("studioName不能为null!");
		}
//		if(studioManageQuery.getStudioNo()==null){
//			return HubResponse.errorResp("studioNo不能为null!");
//		}
		if(studioManageQuery.getPeriod()==null){
			return HubResponse.errorResp("Period不能为null!");
		}
		
		StudioCriteriaDto criteriadto = new StudioCriteriaDto();
		criteriadto.setOrderByClause(" studio_id desc ");
		List<StudioDto> lists = studioGateWay.selectByCriteria(criteriadto);
		String studioNo = "";
		if(lists==null||lists.size()==0){
			studioNo = "S001";
		}else{
			int num = Integer.parseInt(lists.get(0).getStudioNo().substring(3));
			studioNo = "S00"+ (num+1);
		}
		try {
			StudioDto dto = new StudioDto();
			dto.setStudioName(studioManageQuery.getStudioName());
			dto.setStudioNo(studioNo);
			dto.setPeriod(studioManageQuery.getPeriod());
			dto.setStudioType(studioManageQuery.getStudioType());
			dto.setStudioStatus(studioManageQuery.getStudioStatus());
			dto.setStudioContacts(studioManageQuery.getStudioContacts());
			dto.setContactInfo(studioManageQuery.getContactInfo());
			dto.setTelephone(studioManageQuery.getTelephone());
			dto.setEmail(studioManageQuery.getEmail());
			dto.setPeriod(studioManageQuery.getPeriod());
			dto.setCountry(studioManageQuery.getCountry());
			dto.setAddress(studioManageQuery.getAddress());
			dto.setMemo(studioManageQuery.getMemo());
			dto.setSupplierId(studioManageQuery.getSupplierId());
			dto.setSupplierNo(studioManageQuery.getSupplierNo());
			dto.setTimeLag(studioManageQuery.getTimeLag());
			dto.setCreateTime(new Date());
			dto.setCreateUser(studioManageQuery.getCreateUser());
			dto.setUpdateTime(new Date());
			dto.setUpdateUser(studioManageQuery.getCreateUser());
			studioGateWay.insertSelective(dto);
			
			StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
			criteriaDto.createCriteria().andStudioNoEqualTo(studioNo);
			List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
			
			StudioDicSlotDto studioDicSlotDto = new StudioDicSlotDto();
			studioDicSlotDto.setStudioId(studioDtoLists.get(0).getStudioId());
			studioDicSlotDto.setSlotNumber(50);
			studioDicSlotDto.setSlotMinNumber(30);
			studioDicSlotDto.setSlotEfficiency(0);
			studioDicSlotDto.setCreateTime(new Date());
			studioDicSlotDto.setCreateUser(studioManageQuery.getCreateUser());
			studioDicSlotDto.setUpdateTime(new Date());
			studioDicSlotDto.setUpdateUser(studioManageQuery.getCreateUser());
			studioDicSlotGateWay.insertSelective(studioDicSlotDto);
		} catch (Exception e) {
			log.error("新增摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("新增摄影棚信息失败!");
		}
		log.info("addStudio--------------end");
		return HubResponse.successResp("新增成功！");
	}
	
	public HubResponse<?> updateStudio(StudioManageQuery studioManageQuery) {
		log.info("updateStudio--------------start");
		log.info("编辑摄影棚信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		if(studioManageQuery.getStudioId()!=null){
			HubResponse.errorResp("studioId不能为null!");
		}
		try {
			StudioCriteriaDto criteriadto = new StudioCriteriaDto();
			criteriadto.createCriteria().andStudioIdEqualTo(studioManageQuery.getStudioId());
			List<StudioDto> lists = studioGateWay.selectByCriteria(criteriadto);
			if(lists==null||lists.size()==0){
				return HubResponse.errorResp("studioId:"+studioManageQuery.getStudioId()+"不存在对应此编号的摄影棚!");
			}
			StudioDto dto = lists.get(0);
			if(studioManageQuery.getPeriod()!=null){
				dto.setPeriod(studioManageQuery.getPeriod());
			}
			if(studioManageQuery.getStudioType()!=null){
				dto.setStudioType(studioManageQuery.getStudioType());
			}
			if(studioManageQuery.getStudioStatus()!=null){
				dto.setStudioStatus(studioManageQuery.getStudioStatus());
			}
			if(studioManageQuery.getStudioContacts()!=null&&!studioManageQuery.getStudioContacts().equals("")){
				dto.setStudioContacts(studioManageQuery.getStudioContacts());
			}
			if(studioManageQuery.getContactInfo()!=null&&!studioManageQuery.getContactInfo().equals("")){
				dto.setContactInfo(studioManageQuery.getContactInfo());
			}
			if(studioManageQuery.getTelephone()!=null&&!studioManageQuery.getTelephone().equals("")){
				dto.setTelephone(studioManageQuery.getTelephone());
			}
			if(studioManageQuery.getEmail()!=null&&!studioManageQuery.getEmail().equals("")){
				dto.setEmail(studioManageQuery.getEmail());
			}
			if(studioManageQuery.getCountry()!=null&&!studioManageQuery.getCountry().equals("")){
				dto.setCountry(studioManageQuery.getCountry());
			}
			if(studioManageQuery.getAddress()!=null&&!studioManageQuery.getAddress().equals("")){
				dto.setAddress(studioManageQuery.getAddress());
			}
			if(studioManageQuery.getMemo()!=null&&!studioManageQuery.getMemo().equals("")){
				dto.setMemo(studioManageQuery.getMemo());
			}
			if(studioManageQuery.getSupplierId()!=null&&!studioManageQuery.getSupplierId().equals("")){
				dto.setSupplierId(studioManageQuery.getSupplierId());
			}
			if(studioManageQuery.getSupplierNo()!=null&&!studioManageQuery.getSupplierNo().equals("")){
				dto.setSupplierNo(studioManageQuery.getSupplierNo());
			}
			if(studioManageQuery.getTimeLag()!=null){
				dto.setTimeLag(studioManageQuery.getTimeLag());
			}
			if(studioManageQuery.getUpdateUser()!=null&&!studioManageQuery.getUpdateUser().equals("")){
				dto.setUpdateUser(studioManageQuery.getUpdateUser());
			}
			dto.setUpdateTime(new Date());
			studioGateWay.updateByPrimaryKeySelective(dto);
		} catch (Exception e) {
			log.error("编辑摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("编辑摄影棚信息失败!");
		}
		log.info("updateStudio--------------end");
		return HubResponse.successResp("编辑成功！");
	}
	
	public HubResponse<?> selectStudio(StudioManageQuery studioManageQuery) {
		log.info("selectStudio--------------start");
		log.info("查询摄影棚信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		StudioVo vo = new StudioVo();
		try {
			StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
			Criteria criteria = criteriaDto.createCriteria();
			
			if(studioManageQuery.getStudioName()!=null&&!studioManageQuery.getStudioName().equals("")){
				criteria.andStudioNameEqualTo(studioManageQuery.getStudioName());
			}
			if(studioManageQuery.getStudioNo()!=null&&!studioManageQuery.getStudioNo().equals("")){
				criteria.andStudioNameEqualTo(studioManageQuery.getStudioNo());
			}
			criteriaDto.setPageSize(20);
			List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
			if(studioDtoLists==null||studioDtoLists.size()==0){
				return HubResponse.errorResp("不存在摄影棚基础信息!");
			}
			
			vo.setStudioDtoList(studioDtoLists);
		} catch (Exception e) {
			log.error("查询摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("查询摄影棚信息失败!");
		}
		log.info("selectStudio--------------end");
		return HubResponse.successResp(vo);
	}
}