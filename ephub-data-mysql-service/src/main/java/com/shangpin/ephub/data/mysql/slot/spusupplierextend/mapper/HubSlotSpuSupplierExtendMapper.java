package com.shangpin.ephub.data.mysql.slot.spusupplierextend.mapper;

import com.shangpin.ephub.data.mysql.slot.spusupplierextend.bean.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierextend.po.SlotSpuSupplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HubSlotSpuSupplierExtendMapper {

	List<SlotSpuSupplier> findObjectByQuery(SlotSpuSupplierQueryDto priceQueryDto);
	
	int countByQuery(SlotSpuSupplierQueryDto priceQueryDto);
}
