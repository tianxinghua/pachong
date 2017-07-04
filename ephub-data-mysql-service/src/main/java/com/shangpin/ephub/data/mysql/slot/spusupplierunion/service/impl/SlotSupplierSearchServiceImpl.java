package com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.impl;

import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.mapper.SlotSpuSupplierMapper;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.po.SlotSpuSupplier;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.SlotSupplierSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotSupplierSearchServiceImpl implements SlotSupplierSearchService {

	@Autowired
    SlotSpuSupplierMapper slotSpuSupplierMapper;
	@Override
	public List<SlotSpuSupplier> findObjectByQuery(SpuSupplierQueryDto queryDto){
		return slotSpuSupplierMapper.findObjectByQuery(queryDto);
	}
	@Override
	public int countByQuery(SpuSupplierQueryDto queryDto){
		return slotSpuSupplierMapper.countByQuery(queryDto);
	}
}
