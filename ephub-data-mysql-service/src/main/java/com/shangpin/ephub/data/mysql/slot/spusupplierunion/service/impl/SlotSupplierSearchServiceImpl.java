package com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.impl;

import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.mapper.SlotSpuSupplierMapper;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.po.SlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.SlotSupplierSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SlotSupplierSearchServiceImpl implements SlotSupplierSearchService {

	@Autowired
    SlotSpuSupplierMapper slotSpuSupplierMapper;
	@Override
	public List<SlotSpuSupplier> findObjectByQuery(SpuSupplierQueryDto queryDto){
		this.setValueToNull(queryDto);
		return slotSpuSupplierMapper.findObjectByQuery(queryDto);
	}
	@Override
	public int countByQuery(SpuSupplierQueryDto queryDto){
		this.setValueToNull(queryDto);
		log.debug("search obj = " + queryDto.toString());
		return slotSpuSupplierMapper.countByQuery(queryDto);
	}

	private  void setValueToNull(SpuSupplierQueryDto queryDto){
		// spuModel=, brandNo=, category=, state=
		if(null!=queryDto.getSpuModel()&&"".equals(queryDto.getSpuModel())){
			queryDto.setSpuModel(null);
		}
		if(null!=queryDto.getBrandNo()&&"".equals(queryDto.getBrandNo())){
			queryDto.setBrandNo(null);
		}
		if(null!=queryDto.getCategory()&&"".equals(queryDto.getCategory())){
			queryDto.setCategory(null);
		}
		if(null!=queryDto.getState()&&"".equals(queryDto.getState())){
			queryDto.setState(null);
		}

	}
}
