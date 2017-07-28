package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCategoryCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCategoryCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCategoryDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicCategoryGateWay;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioDicCategorySlotVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioDicCategoryInfo;
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
public class StudioDicCategoryManageService {
	@Autowired
	StudioGateWay studioGateWay;
	@Autowired
	StudioDicSlotGateWay studioDicSlotGateWay;
	@Autowired
	StudioDicCategoryGateWay studioDicCategoryGateWay;
	
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");

	public HubResponse<?> addStudioCategory(StudioManageQuery studioManageQuery) {
		log.info("addStudioCategory--------------start");
		log.info("添加品类摄影棚参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		if(studioManageQuery.getCategoryFirst()==null){
			return HubResponse.errorResp("categoryFirst不能为null!");
		}
		if(studioManageQuery.getCategorySecond()==null){
			return HubResponse.errorResp("CategorySecond不能为null!");
		}
		if(studioManageQuery.getStudioName()==null){
			return HubResponse.errorResp("StudioName不能为null!");
		}
		try {
			String[] studioNameArray = studioManageQuery.getStudioName().split("\\|");
			log.info("studioNameArray:"+studioNameArray);
			int size = studioNameArray.length;
			for(int i=0;i<size;i++){
				StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
				criteriaDto.createCriteria().andStudioNameEqualTo(studioNameArray[i]);
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
				log.info("studioDtoLists:"+studioDtoLists);
				if(studioDtoLists==null||studioDtoLists.size()==0){
					continue;
				}
				StudioDicCategoryDto dto = new StudioDicCategoryDto();
				dto.setCategoryFirst(studioManageQuery.getCategoryFirst());
				dto.setCategorySecond(studioManageQuery.getCategorySecond());
				dto.setStudioId(studioDtoLists.get(0).getStudioId());
				dto.setCreateTime(new Date());
				dto.setCreateUser(studioManageQuery.getCreateUser());
				dto.setUpdateTime(new Date());
				dto.setUpdateUser(studioManageQuery.getCreateUser());
				studioDicCategoryGateWay.insertSelective(dto);
			}
		} catch (Exception e) {
			log.error("新增摄影棚分类信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("新增摄影棚分类信息失败!");
		}
		log.info("addStudioCategory--------------end");
		return HubResponse.successResp("新增成功！");
	}
	
	public HubResponse<?> updateStudioCategory(StudioManageQuery studioManageQuery) {
		log.info("updateStudioCategory--------------start");
		log.info("编辑摄影棚分类信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		try {
			if(studioManageQuery.getStudioDicCategoryId()==null){
				return HubResponse.errorResp("新增摄影棚分类ID不能为null!");
			}
//			StudioDicCategoryDto dto = studioDicCategoryGateWay.selectByPrimaryKey(studioManageQuery.getStudioDicCategoryId());
//			if(dto==null){
//				return HubResponse.errorResp("studioId:"+studioManageQuery.getStudioId()+"不存在对应此编号的摄影棚!");
//			}
			// qa 调用mysql  selectByPrimaryKey服务有问题
			StudioDicCategoryCriteriaDto studioDicCategoryCriteriaDto = new StudioDicCategoryCriteriaDto();
			studioDicCategoryCriteriaDto.createCriteria().andStudioDicCategoryIdEqualTo(studioManageQuery.getStudioDicCategoryId());
			List<StudioDicCategoryDto> studioDicCategoryDtoLists = studioDicCategoryGateWay.selectByCriteria(studioDicCategoryCriteriaDto);
			if(studioDicCategoryDtoLists==null||studioDicCategoryDtoLists.size()==0){
				return HubResponse.errorResp("studioId:"+studioManageQuery.getStudioId()+"不存在对应此编号的摄影棚!");
			}
			StudioDicCategoryDto dto = studioDicCategoryDtoLists.get(0);
//			if(studioManageQuery.getCategoryFirst()!=null){
//				dto.setCategoryFirst(studioManageQuery.getCategoryFirst());
//			}
//			if(studioManageQuery.getCategorySecond()!=null){
//				dto.setCategorySecond(studioManageQuery.getCategorySecond());
//			}
			
			if(studioManageQuery.getStudioName()!=null){
				StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
				criteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioName());
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
				
				if(studioDtoLists==null||studioDtoLists.size()==0){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioName()+"摄影棚不存在!");
				}
				dto.setStudioId(studioDtoLists.get(0).getStudioId());
			}
			dto.setUpdateTime(new Date());
			dto.setUpdateUser(studioManageQuery.getUpdateUser());
			studioDicCategoryGateWay.updateByPrimaryKeySelective(dto);
		} catch (Exception e) {
			log.error("编辑摄影棚分类信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("编辑摄影棚分类信息失败!");
		}
		log.info("updateStudioCategory--------------end");
		return HubResponse.successResp("编辑成功！");
	}
	
	public HubResponse<?> selectStudioCategory(StudioManageQuery studioManageQuery) {
		log.info("selectStudioCategory--------------start");
		log.info("查询摄影棚分类信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		StudioDicCategorySlotVo vo = new StudioDicCategorySlotVo();
		try {
			StudioDicCategoryCriteriaDto criteriaDto = new StudioDicCategoryCriteriaDto();
			Criteria criteria = criteriaDto.createCriteria();
			
			if(studioManageQuery.getCategoryFirst()!=null){
				criteria.andCategoryFirstEqualTo(studioManageQuery.getCategoryFirst());
			}
			if(studioManageQuery.getCategorySecond()!=null){
				criteria.andCategorySecondEqualTo(studioManageQuery.getCategorySecond());
			}
			if(studioManageQuery.getStudioName()!=null){
				StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
				studioCriteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioName());
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(studioCriteriaDto);
				if(studioDtoLists==null||studioDtoLists.size()==0){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioName()+"摄影棚不存在!");
				}
				criteria.andStudioIdEqualTo(studioDtoLists.get(0).getStudioId());
			}
			if(studioManageQuery.getPageNo()!=null){
				criteriaDto.setPageNo(studioManageQuery.getPageNo());
			}
			if(studioManageQuery.getPageSize()!=null){
				criteriaDto.setPageSize(studioManageQuery.getPageSize());
			}
			List<StudioDicCategoryInfo> studioDicCategoryInfoLists = new ArrayList<>();
			List<StudioDicCategoryDto> studioDicCategoryDtoLists = studioDicCategoryGateWay.selectByCriteria(criteriaDto);
			int count = studioDicCategoryGateWay.countByCriteria(criteriaDto);
			for(StudioDicCategoryDto studioDicCategoryDto : studioDicCategoryDtoLists){
				StudioDicCategoryInfo info = new StudioDicCategoryInfo();
				info.setCategoryFirst(studioDicCategoryDto.getCategoryFirst());
				info.setCategorySecond(studioDicCategoryDto.getCategorySecond());
				info.setStudioDicCategoryId(studioDicCategoryDto.getStudioDicCategoryId());
				info.setUpdateUser(studioDicCategoryDto.getUpdateUser());
				info.setUpdateTime(sdfomat.parse(sdfomat.format(studioDicCategoryDto.getUpdateTime())));
				if(studioManageQuery.getStudioName()!=null){
					info.setStudioName(studioManageQuery.getStudioName());
				}else{
					StudioDto studiodto = studioGateWay.selectByPrimaryKey(studioDicCategoryDto.getStudioId());
					if(studiodto==null){
						continue;
					}
					info.setStudioName(studiodto.getStudioName());
				}
				studioDicCategoryInfoLists.add(info);
			}
			vo.setTotal(count);
			vo.setStudioDicCategoryDtoList(studioDicCategoryInfoLists);
		} catch (Exception e) {
			log.error("查询摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("查询摄影棚分类信息失败!");
		}
		log.info("selectStudioCategory--------------end");
		return HubResponse.successResp(vo);
	}
	
	public static void main(String[] args){
		String test = "test1";
		String[] array = test.split("\\|");
		System.out.println(array.length);
	}
}
