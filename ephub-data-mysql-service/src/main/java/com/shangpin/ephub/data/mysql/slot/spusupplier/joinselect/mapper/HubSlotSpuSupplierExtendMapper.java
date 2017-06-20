package com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.mapper;

import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.bean.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplier.joinselect.po.SlotSpuSupplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HubSlotSpuSupplierExtendMapper {

	public List<SlotSpuSupplier> findObjectByQuery(SlotSpuSupplierQueryDto priceQueryDto);
	
	public int countByQuery(SlotSpuSupplierQueryDto priceQueryDto);
}
