package com.shangpin.ephub.product.business.ui.studio.defective.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
//import com.shangpin.ephub.client.data.studio.enumeration.ArriveState;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuGateWay;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.service.impl.OpenBoxServiceImpl;

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
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private StudioSlotDefectiveSpuPicGateWay defectiveSpuPicGateWay;
	

	@Override
	public DefectiveProductVo list(String studioNo) {
		try {
			DefectiveProductVo product = new DefectiveProductVo();
			List<StudioSlotDto> studioSlots = findStudioSlot(studioNo);
			if(CollectionUtils.isNotEmpty(studioSlots)){
				List<String> slotNos = new ArrayList<String>();
				for(StudioSlotDto slotDto : studioSlots){
					slotNos.add(slotDto.getSlotNo());
				}
				StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
				criteria.setOrderByClause("create_time desc");
				criteria.setPageNo(1);
				criteria.setPageSize(1000); 
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
		//criteria.createCriteria().andArriveStatusEqualTo(ArriveState.ARRIVED.getIndex().byteValue());
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
			Long spuPendingId = Long.valueOf(slotNoSpuId.substring(slotNoSpuId.indexOf("-") + 1));
			HubSpuPendingDto hubSpuPendingDto = hubSpuPendingGateWay.selectByPrimaryKey(spuPendingId);
			if(null != hubSpuPendingDto){
				StudioSlotDefectiveSpuDto defectiveSpuDto = new StudioSlotDefectiveSpuDto();
				defectiveSpuDto.setSlotNo(slotNo);
				defectiveSpuDto.setSupplierNo(hubSpuPendingDto.getSupplierNo());
				defectiveSpuDto.setSupplierId(hubSpuPendingDto.getSupplierId());
				defectiveSpuDto.setSpuPendingId(spuPendingId);
				defectiveSpuDto.setSupplierSpuId(hubSpuPendingDto.getSupplierSpuId());
				Date date = new Date();
				defectiveSpuDto.setCreateTime(date);
				defectiveSpuDto.setUpdateTime(date); 
				Long studioSlotDefectiveSpuId = defectiveSpuGateWay.insert(defectiveSpuDto );
				defectiveSpuDto.setStudioSlotDefectiveSpuId(studioSlotDefectiveSpuId); 
				return defectiveSpuDto;
			}else{
				log.info("添加残次品时未在pending表中发现该商品，spuPendingId==="+spuPendingId); 
			}
		} catch (Exception e) {
			log.error("添加残次品时发生异常："+e.getMessage(),e); 
		}
		return null;
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
		defectiveSpuPicGateWay.updateByPrimaryKeySelective(spuPicDto );
		return true;
	}

}
