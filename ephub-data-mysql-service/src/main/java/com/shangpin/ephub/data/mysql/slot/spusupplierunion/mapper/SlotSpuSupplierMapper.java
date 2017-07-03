package com.shangpin.ephub.data.mysql.slot.spusupplierunion.mapper;


import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.po.SlotSpuSupplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SlotSpuSupplierMapper {

	public List<SlotSpuSupplier> findObjectByQuery(SpuSupplierQueryDto queryDto);
	
	public int countByQuery(SpuSupplierQueryDto queryDto);
}
