package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSupplierGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.StudioManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioDicSupplierVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioDicSupplierInfo;
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
public class StudioDicSupplierManageService {
	@Autowired
	StudioGateWay studioGateWay;
	@Autowired
	StudioDicSupplierGateWay studioDicSupplierGateWay;
	@Autowired
	private IShangpinRedis shangpinRedis;

	public HubResponse<?> addStudioDicSupplier(StudioManageQuery studioManageQuery) {
		log.info("addStudioDicSupplier--------------start");
		log.info("添加摄影棚供应商关系参数:"+JsonUtil.serialize(studioManageQuery)); 
		if((studioManageQuery.getSupplierId()==null||studioManageQuery.getSupplierId().equals(""))||(studioManageQuery.getSupplierNo()==null||studioManageQuery.getSupplierNo().equals(""))){
			return HubResponse.errorResp("供应商supplierId和supplierNo不能为null!");
		}
		try {
			StudioDicSupplierCriteriaDto studioDicSupplierCriteriaDto = new StudioDicSupplierCriteriaDto();
			studioDicSupplierCriteriaDto.createCriteria().andSupplierIdEqualTo(studioManageQuery.getSupplierId()).andSupplierNoEqualTo(studioManageQuery.getSupplierNo());
			List<StudioDicSupplierDto> studioDicSupplierDtoLists = studioDicSupplierGateWay.selectByCriteria(studioDicSupplierCriteriaDto);
			if(studioDicSupplierDtoLists!=null&&studioDicSupplierDtoLists.size()>0){
				return HubResponse.errorResp("supplierNo"+studioManageQuery.getSupplierNo()+"supplierId"+studioManageQuery.getSupplierId()+"此供应商已经维护摄影棚和供应商关系!"); 
			}
			String studioIds = "";
			if(studioManageQuery.getStudioNameFirst()==null){
				return HubResponse.errorResp("第一摄影棚不能为null!");
			}else{
				StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
				criteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameFirst());
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
				if(studioDtoLists==null||studioDtoLists.size()==0){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameFirst()+"第一摄影棚不存在!");
				}
				if(studioDtoLists.get(0).getStudioStatus()!=1){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameFirst()+"第一摄影棚不是营业中!");
				}
				studioIds = studioDtoLists.get(0).getStudioId().toString();
			}
			if(studioManageQuery.getStudioNameSecond()!=null){
				StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
				criteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameSecond());
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
				if(studioDtoLists==null||studioDtoLists.size()==0){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第二摄影棚不存在!");
				}
				if(studioDtoLists.get(0).getStudioStatus()!=1){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第二摄影棚不是营业中!");
				}
				if(studioManageQuery.getStudioNameFirst()==studioManageQuery.getStudioNameSecond()){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第一摄影棚和第二摄影棚重复!");
				}
				studioIds = studioIds + "," + studioDtoLists.get(0).getStudioId().toString();
			}else{
				studioIds = studioIds + "," + "-1";
			}
			if(studioManageQuery.getStudioNameThree()!=null){
				StudioCriteriaDto criteriaDto = new StudioCriteriaDto();
				criteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameThree());
				List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(criteriaDto);
				if(studioDtoLists==null||studioDtoLists.size()==0){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameThree()+"第二摄影棚不存在!");
				}
				if(studioDtoLists.get(0).getStudioStatus()!=1){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameThree()+"第二摄影棚不是营业中!");
				}
				if(studioManageQuery.getStudioNameSecond()!=null){
					if(studioManageQuery.getStudioNameThree()==studioManageQuery.getStudioNameSecond()){
						return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第三摄影棚和第二摄影棚重复!");
					}
				}
				if(studioManageQuery.getStudioNameThree()==studioManageQuery.getStudioNameFirst()){
					return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第三摄影棚和第一摄影棚重复!");
				}
				studioIds = studioIds + "," + studioDtoLists.get(0).getStudioId().toString();
			}else{
				studioIds = studioIds + "," + "-1";
			}
		    String[] studioIdArray = studioIds.split(",");
		    for(int i=0;i<studioIdArray.length;i++){
				StudioDicSupplierDto dto = new StudioDicSupplierDto();
				dto.setSupplierId(studioManageQuery.getSupplierId());
				dto.setSupplierNo(studioManageQuery.getSupplierNo());
				dto.setStudioId(Long.parseLong(studioIdArray[i]));
				dto.setStudioIndex((byte) i);
				dto.setCreateTime(new Date());
				dto.setCreateUser(studioManageQuery.getCreateUser());
				dto.setUpdateTime(new Date());
				dto.setUpdateUser(studioManageQuery.getCreateUser());
				studioDicSupplierGateWay.insertSelective(dto);
		    }
		} catch (Exception e) {
			log.error("新增摄影棚供应商关系信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("新增摄影棚供应商关系信息失败!");
		}
		log.info("addStudioDicSupplier--------------end");
		return HubResponse.successResp("新增成功！");
	}
	
	public HubResponse<?> updateStudioDicSupplier(StudioManageQuery studioManageQuery) {
		log.info("updateStudioDicSupplier--------------start");
		log.info("编辑摄影棚信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		try {
			if((studioManageQuery.getSupplierId()==null||studioManageQuery.getSupplierId().equals(""))||(studioManageQuery.getSupplierNo()==null||studioManageQuery.getSupplierNo().equals(""))){
				return HubResponse.errorResp("供应商supplierId和supplierNo不能为null!");
			}
			StudioDicSupplierCriteriaDto studioDicSupplierCriteriaDto = new StudioDicSupplierCriteriaDto();
			studioDicSupplierCriteriaDto.createCriteria().andSupplierIdEqualTo(studioManageQuery.getSupplierId()).andSupplierNoEqualTo(studioManageQuery.getSupplierNo());
			List<StudioDicSupplierDto> studioDicSupplierDtoLists = studioDicSupplierGateWay.selectByCriteria(studioDicSupplierCriteriaDto);

			if(studioDicSupplierDtoLists==null||studioDicSupplierDtoLists.size()==0){
				return HubResponse.errorResp("StudioDicSupplierId"+studioManageQuery.getStudioDicSupplierId()+"此供应商和摄影棚关系不存在!");
			}
			for(StudioDicSupplierDto studioDicSupplierDto: studioDicSupplierDtoLists){
				if(studioDicSupplierDto.getStudioIndex()==0){
					if(studioManageQuery.getStudioNameFirst()!=null){
						StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
						studioCriteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameFirst());
						List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(studioCriteriaDto);
						if(studioDtoLists==null||studioDtoLists.size()==0){
							log.info("StudioNameFirst:"+studioManageQuery.getStudioNameFirst()+"摄影棚不存在!");
							continue;
						}
						studioDicSupplierDto.setStudioId(studioDtoLists.get(0).getStudioId());
					}
					studioDicSupplierDto.setUpdateTime(new Date());
					studioDicSupplierDto.setUpdateUser(studioManageQuery.getUpdateUser());
					studioDicSupplierGateWay.updateByPrimaryKeySelective(studioDicSupplierDto);
				}
				if(studioDicSupplierDto.getStudioIndex()==1){
					if(studioManageQuery.getStudioNameSecond()!=null){
						if(studioManageQuery.getStudioNameFirst()!=null){
							if(studioManageQuery.getStudioNameFirst()==studioManageQuery.getStudioNameSecond()){
								return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第一摄影棚和第二摄影棚重复!");
							}
						}
						StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
						studioCriteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameSecond());
						List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(studioCriteriaDto);
						if(studioDtoLists==null||studioDtoLists.size()==0){
							log.info("StudioNameSecond:"+studioManageQuery.getStudioNameSecond()+"摄影棚不存在!");
							continue;
						}
						studioDicSupplierDto.setStudioId(studioDtoLists.get(0).getStudioId());
					}
					studioDicSupplierDto.setUpdateTime(new Date());
					studioDicSupplierDto.setUpdateUser(studioManageQuery.getUpdateUser());
					studioDicSupplierGateWay.updateByPrimaryKeySelective(studioDicSupplierDto);
				}
				if(studioDicSupplierDto.getStudioIndex()==2){
					if(studioManageQuery.getStudioNameThree()!=null){
						if(studioManageQuery.getStudioNameFirst()!=null){
							if(studioManageQuery.getStudioNameFirst()==studioManageQuery.getStudioNameThree()){
								return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第一摄影棚和第二摄影棚重复!");
							}
						}
						if(studioManageQuery.getStudioNameSecond()!=null){
							if(studioManageQuery.getStudioNameSecond()==studioManageQuery.getStudioNameThree()){
								return HubResponse.errorResp("studioName:"+studioManageQuery.getStudioNameSecond()+"第一摄影棚和第二摄影棚重复!");
							}
						}
						StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
						studioCriteriaDto.createCriteria().andStudioNameEqualTo(studioManageQuery.getStudioNameThree());
						List<StudioDto> studioDtoLists = studioGateWay.selectByCriteria(studioCriteriaDto);
						if(studioDtoLists==null||studioDtoLists.size()==0){
							log.info("StudioNameThree:"+studioManageQuery.getStudioNameThree()+"摄影棚不存在!");
							continue;
						}
						studioDicSupplierDto.setStudioId(studioDtoLists.get(0).getStudioId());
					}
					studioDicSupplierDto.setUpdateTime(new Date());
					studioDicSupplierDto.setUpdateUser(studioManageQuery.getUpdateUser());
					studioDicSupplierGateWay.updateByPrimaryKeySelective(studioDicSupplierDto);
				}
				
			}
		} catch (Exception e) {
			log.error("编辑摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("编辑摄影棚信息失败!");
		}
		log.info("updateStudioDicSupplier--------------end");
		return HubResponse.successResp("编辑成功！");
	}
	
	public HubResponse<?> selectStudioDicSupplier(StudioManageQuery studioManageQuery) {
		log.info("selectStudioDicSupplier--------------start");
		log.info("查询摄影棚信息参数:"+JsonUtil.serialize(studioManageQuery)); 
		
		
		StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
		if (!shangpinRedis.exists("shangpinstudioslot")) {
			List<StudioDto> studioDtoList = studioGateWay.selectByCriteria(studioCriteriaDto);
			for (StudioDto studiotDto : studioDtoList) {
				shangpinRedis.set("studioName" + studiotDto.getStudioId(), studiotDto.getStudioName());
				shangpinRedis.set("period" + studiotDto.getStudioId(), studiotDto.getPeriod().toString());
				shangpinRedis.set("studio_no" + studiotDto.getStudioId(), studiotDto.getStudioNo());
			}
			// 摄影棚基础数据初始化到redis 用于判断
			shangpinRedis.setex("shangpinstudioslot", 60*10, "studioSlot");
		}
		
		StudioDicSupplierVo vo = new StudioDicSupplierVo();
		try {
			StudioDicSupplierCriteriaDto criteriaDto = new StudioDicSupplierCriteriaDto();
			Criteria criteria = criteriaDto.createCriteria();
			
			if(studioManageQuery.getSupplierId()!=null&&!studioManageQuery.getSupplierId().equals("")){
				criteria.andSupplierIdEqualTo(studioManageQuery.getSupplierId());
			}
			if(studioManageQuery.getSupplierNo()!=null&&!studioManageQuery.getSupplierNo().equals("")){
				criteria.andSupplierNoEqualTo(studioManageQuery.getSupplierNo());
			}
			if(studioManageQuery.getPageNo()!=null){
				criteriaDto.setPageNo(studioManageQuery.getPageNo());
			}
			if(studioManageQuery.getPageSize()!=null){
				criteriaDto.setPageSize(studioManageQuery.getPageSize());
			}
			criteriaDto.setDistinct(true);
			criteriaDto.setFields(" supplier_id,supplier_no  ");
			
			List<StudioDicSupplierDto> studioDicSupplierDtoLists = studioDicSupplierGateWay.selectByCriteria(criteriaDto);
			int count = studioDicSupplierGateWay.countByCriteria(criteriaDto)/3;
			List<StudioDicSupplierInfo> studioDicSupplierInfoList = new ArrayList<>();
			for(StudioDicSupplierDto studioDicSupplierDto : studioDicSupplierDtoLists){
				StudioDicSupplierCriteriaDto studioDicSupplierCriteriaDto = new StudioDicSupplierCriteriaDto();
				studioDicSupplierCriteriaDto.createCriteria().andSupplierIdEqualTo(studioDicSupplierDto.getSupplierId()).andSupplierNoEqualTo(studioDicSupplierDto.getSupplierNo());
				List<StudioDicSupplierDto> lists = studioDicSupplierGateWay.selectByCriteria(studioDicSupplierCriteriaDto);
				StudioDicSupplierInfo info = new StudioDicSupplierInfo();
				for(StudioDicSupplierDto dto : lists){
					if(dto.getStudioIndex()==0){
						info.setSupplierId(dto.getSupplierId());
						info.setUpdateTime(dto.getUpdateTime());
						info.setUpdateUser(dto.getUpdateUser());
						info.setStudioNameFirst(shangpinRedis.get("studioName"+dto.getStudioId()));
					}
					if(dto.getStudioIndex()==1){
						info.setStudioNameSecond(shangpinRedis.get("studioName"+dto.getStudioId()));
					}
					if(dto.getStudioIndex()==2){
						info.setStudioNameThree(shangpinRedis.get("studioName"+dto.getStudioId()));
					}
				}
				studioDicSupplierInfoList.add(info);
			}
			vo.setStudioDicSupplierInfoList(studioDicSupplierInfoList);
			vo.setTotal(count);
		} catch (Exception e) {
			log.error("查询摄影棚信息失败! "+e.getMessage(),e); 
			return HubResponse.errorResp("查询摄影棚信息失败!");
		}
		log.info("selectStudioDicSupplier--------------end");
		return HubResponse.successResp(vo);
	}
}
