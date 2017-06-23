package com.shangpin.ephub.data.mysql.slot.spusupplierextend.service;

import com.shangpin.ephub.data.mysql.slot.spusupplierextend.bean.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierextend.mapper.HubSlotSpuSupplierExtendMapper;
import com.shangpin.ephub.data.mysql.slot.spusupplierextend.po.SlotSpuSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
