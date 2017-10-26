package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto.HubSlotSpuFixedPropertyDto;
import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.gateway.HubSlotSpuFixedPropertyGateway;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.export.SlotDetailDto;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.export.SlotsSendDetailVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SlotHistoryService {
	
	@Autowired
	StudioSlotSpuSendDetailGateWay slotSpuSendDetailGateWay;
	@Autowired
	HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired
	HubSlotSpuFixedPropertyGateway propertyGateway;

	/**
	 * 根据slot编号查找详情
	 * @param slotNo
	 * @return
	 */
	public SlotsSendDetailVo selectDetail(String slotNo){
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(1000); 
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		List<StudioSlotSpuSendDetailDto> list = slotSpuSendDetailGateWay.selectByCriteria(criteria );
		SlotsSendDetailVo vo = new SlotsSendDetailVo();
		if(CollectionUtils.isNotEmpty(list)){
			List<SlotDetailDto> lists = new ArrayList<>();
			for(StudioSlotSpuSendDetailDto detailDto : list){
				SlotDetailDto info = new SlotDetailDto();
				info.setSlotSpuSupplierId(detailDto.getSlotSpuSupplierId());
				info.setSupplierSpuId(detailDto.getSupplierSpuId());
				info.setSpuPendingId(detailDto.getSpuPendingId());
				info.setSlotSpuNo(detailDto.getSlotSpuNo());
				info.setBarcode(detailDto.getBarcode());
				info.setSupplierBrandName(detailDto.getSupplierBrandName());
				info.setSupplierSpuModel(detailDto.getSupplierSpuModel());
				info.setSlotNo(detailDto.getSlotNo());
				setSeasonGender(info,detailDto.getSupplierSpuId());
				lists.add(info);
			}
			vo.setDetails(lists);
		}
		return vo;
	}
	
	private void setSeasonGender(SlotDetailDto info, Long supplierSpuId){
		HubSupplierSpuDto spuDto = hubSupplierSpuGateWay.selectByPrimaryKey(supplierSpuId);
		if(null != spuDto && StringUtils.isNotBlank(spuDto.getSupplierSeasonname())){
			info.setSeason(spuDto.getSupplierSeasonname()); 
			info.setGender(spuDto.getSupplierGender()); 
		}
	}
	
	public boolean importExcel(SlotsSendDetailVo detailVo){
		try {
			if(null != detailVo && CollectionUtils.isNotEmpty(detailVo.getDetails())){
				detailVo.getDetails().forEach(detail -> insertFixedProperty(detail));
				return true;
			}
		} catch (Exception e) {
			log.error("插入出错："+e.getMessage(),e); 
		}
		return false;
		
	}

	private void insertFixedProperty(SlotDetailDto detail) {
		HubSlotSpuFixedPropertyDto dto = new HubSlotSpuFixedPropertyDto();
		dto.setSlotSpuSupplierId(detail.getSlotSpuSupplierId());
		dto.setSupplierSpuId(detail.getSupplierSpuId());
		dto.setSpuPendingId(detail.getSpuPendingId());
		dto.setSlotNo(detail.getSlotNo());
		dto.setSlotSpuNo(detail.getSlotSpuNo());
		dto.setBarcode(detail.getBarcode());
		dto.setColor(detail.getColour());
		dto.setMaterial(detail.getComposition());
		dto.setOrigin(detail.getMadeIn());
		dto.setCreateTime(new Date());
		propertyGateway.insert(dto );
	}
}
