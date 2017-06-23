package com.shangpin.ephub.product.business.ui.studio.defective.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuGateWay;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.ui.studio.defective.dto.DefectiveQuery;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.service.impl.OpenBoxServiceImpl;
import com.shangpin.ephub.product.business.ui.studio.picture.PictureService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefectiveProductServiceImpl implements DefectiveProductService {
	
	@Autowired
	private OpenBoxServiceImpl openBoxService;
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	@Autowired
	private StudioSlotDefectiveSpuGateWay defectiveSpuGateWay;
	@Autowired
	private StudioSlotDefectiveSpuPicGateWay defectiveSpuPicGateWay;
	@Autowired
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	private PictureService pictureService;
	

	@Override
	public DefectiveProductVo list(DefectiveQuery defectiveQuery) {
		try {
			DefectiveProductVo product = new DefectiveProductVo();
			List<StudioSlotDto> studioSlots = findStudioSlot(defectiveQuery.getStudioNo());
			if(CollectionUtils.isNotEmpty(studioSlots)){
				List<String> slotNos = new ArrayList<String>();
				for(StudioSlotDto slotDto : studioSlots){
					slotNos.add(slotDto.getSlotNo());
				}
				StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
				criteria.setOrderByClause("create_time desc");
				if(null != defectiveQuery.getPageIndex() && null != defectiveQuery.getPageSize()){
					criteria.setPageNo(defectiveQuery.getPageIndex());
					criteria.setPageSize(defectiveQuery.getPageSize()); 
				}
				criteria.createCriteria().andSlotNoIn(slotNos);
				List<StudioSlotDefectiveSpuDto> list = defectiveSpuGateWay.selectByCriteria(criteria );
				product.setDefectiveSpus(list); 
			}
			return product;
		} catch (Exception e) {
			log.error("查询残次品列表失败："+e.getMessage(),e); 
		}
		return null;
	}
	
	private List<StudioSlotDto> findStudioSlot(String studioNo ) throws Exception{
		StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
		criteria.setOrderByClause("create_time desc"); 
		criteria.setFields("slot_no");
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		criteria.createCriteria().andArriveStatusEqualTo(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
		Long studioId = openBoxService.getStudioId(studioNo);
		if(null != studioId){
			criteria.createCriteria().andStudioIdEqualTo(studioId);
		}else{
			throw new Exception("未获得摄影棚编号");
		}
		return studioSlotGateWay.selectByCriteria(criteria);
	}

	@Override
	public StudioSlotDefectiveSpuDto add(String slotNoSpuId) {
		try {
			String slotNo = slotNoSpuId.substring(0, slotNoSpuId.indexOf("-"));
			String slotSpuNo = slotNoSpuId.substring(slotNoSpuId.indexOf("-") + 1);
			StudioSlotSpuSendDetailDto hubSpuPendingDto = getStudioSlotSpuSendDetailDto(slotNo,slotSpuNo);
			if(null != hubSpuPendingDto){
				StudioSlotDefectiveSpuDto defectiveSpuDto = new StudioSlotDefectiveSpuDto();
				defectiveSpuDto.setSlotNo(slotNo);
				defectiveSpuDto.setSupplierNo(hubSpuPendingDto.getSupplierNo());
				defectiveSpuDto.setSupplierId(hubSpuPendingDto.getSupplierId());
				defectiveSpuDto.setSpuPendingId(hubSpuPendingDto.getSpuPendingId());
				defectiveSpuDto.setSupplierSpuId(hubSpuPendingDto.getSupplierSpuId());
				Date date = new Date();
				defectiveSpuDto.setCreateTime(date);
				defectiveSpuDto.setUpdateTime(date); 
				Long studioSlotDefectiveSpuId = defectiveSpuGateWay.insert(defectiveSpuDto );
				defectiveSpuDto.setStudioSlotDefectiveSpuId(studioSlotDefectiveSpuId); 
				return defectiveSpuDto;
			}else{
				log.info("添加残次品时未在slot明细表中发现该商品，slotNoSpuId==="+slotNoSpuId); 
			}
		} catch (Exception e) {
			log.error("添加残次品时发生异常："+e.getMessage(),e); 
		}
		return null;
	}
	
	private StudioSlotSpuSendDetailDto getStudioSlotSpuSendDetailDto(String slotNo, String slotSpuNo){
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo).andSlotSpuNoEqualTo(slotSpuNo);
		List<StudioSlotSpuSendDetailDto> list  = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public Long insert(StudioSlotDefectiveSpuDto defctiveSouDot, String extension) {
		StudioSlotDefectiveSpuPicDto spuPicDto = new StudioSlotDefectiveSpuPicDto();
		spuPicDto.setStudioSlotDefectiveSpuId(defctiveSouDot.getStudioSlotDefectiveSpuId());
		spuPicDto.setSupplierNo(defctiveSouDot.getSupplierNo());
		spuPicDto.setSupplierId(defctiveSouDot.getSupplierId());
		spuPicDto.setCreateTime(new Date());
		spuPicDto.setPicExtension(extension); 
		return defectiveSpuPicGateWay.insert(spuPicDto);
	}

	@Override
	public boolean update(Long studioSlotDefectiveSpuPicId, String spPicUrl) {
		StudioSlotDefectiveSpuPicDto spuPicDto = new StudioSlotDefectiveSpuPicDto();
		spuPicDto.setStudioSlotDefectiveSpuPicId(studioSlotDefectiveSpuPicId);
		spuPicDto.setSpPicUrl(spPicUrl);
		spuPicDto.setDataState(DataState.NOT_DELETED.getIndex()); 
		defectiveSpuPicGateWay.updateByPrimaryKeySelective(spuPicDto );
		return true;
	}

	@Override
	public List<StudioSlotDefectiveSpuPicDto> selectDefectivePic(String studioSlotDefectiveSpuId) {
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.setOrderByClause("create_time"); 
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		criteria.createCriteria().andStudioSlotDefectiveSpuIdEqualTo(Long.valueOf(studioSlotDefectiveSpuId)).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		return defectiveSpuPicGateWay.selectByCriteria(criteria );
	}

	@Override
	public StudioSlotDefectiveSpuDto selectByPrimarykey(Long studioSlotDefectiveSpuId) {
		if(null != studioSlotDefectiveSpuId){
			return defectiveSpuGateWay.selectByPrimaryKey(studioSlotDefectiveSpuId);
		}else{
			return null;
		}
	}

	@Override
	public boolean hasDefectiveSpuPic(String spPicUrl) {
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo(spPicUrl);
		List<StudioSlotDefectiveSpuPicDto> list = defectiveSpuPicGateWay.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteDefectivePic(String spPicUrl) {
		if(StringUtils.isNotBlank(spPicUrl)){
			//先删除fsdfs上的资源
			
			//在删除数据库中的
			StudioSlotDefectiveSpuPicWithCriteriaDto withCriteria = new StudioSlotDefectiveSpuPicWithCriteriaDto();
			StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
			criteria.createCriteria().andSpPicUrlEqualTo(spPicUrl);
			withCriteria.setCriteria(criteria );
			StudioSlotDefectiveSpuPicDto studioSlotDefectiveSpuPicDto = new StudioSlotDefectiveSpuPicDto();
			studioSlotDefectiveSpuPicDto.setDataState(DataState.DELETED.getIndex()); 
			withCriteria.setStudioSlotDefectiveSpuPic(studioSlotDefectiveSpuPicDto );
			defectiveSpuPicGateWay.updateByCriteriaSelective(withCriteria );
			return true;
		}
		return false;
	}

}
