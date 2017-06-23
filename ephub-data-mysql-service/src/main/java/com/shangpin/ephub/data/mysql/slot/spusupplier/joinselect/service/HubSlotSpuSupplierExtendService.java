package com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.service;

import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.bean.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.mapper.HubSlotSpuSupplierExtendMapper;
import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.po.SlotSpuSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HubSlotSpuSupplierExtendService {

	@Autowired
	private HubSlotSpuSupplierExtendMapper mapper;
	
	public List<SlotSpuSupplier> findObjectByQuery(SlotSpuSupplierQueryDto queryDto){
		return mapper.findObjectByQuery(queryDto);
	}
	
	public int countByQuery(SlotSpuSupplierQueryDto queryDto){
		return mapper.countByQuery(queryDto);
	}
}
