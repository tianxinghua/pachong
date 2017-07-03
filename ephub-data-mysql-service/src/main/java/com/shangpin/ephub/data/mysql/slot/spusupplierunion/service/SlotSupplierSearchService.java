package com.shangpin.ephub.data.mysql.slot.spusupplierunion.service;

import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;

import com.shangpin.ephub.data.mysql.slot.spusupplierunion.po.SlotSpuSupplier;

import java.util.List;


public interface SlotSupplierSearchService {


	
	public List<SlotSpuSupplier> findObjectByQuery(SpuSupplierQueryDto queryDto);

	
	public int countByQuery(SpuSupplierQueryDto queryDto);
}
