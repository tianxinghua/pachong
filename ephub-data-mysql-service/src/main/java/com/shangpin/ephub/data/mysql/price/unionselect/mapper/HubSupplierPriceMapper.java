package com.shangpin.ephub.data.mysql.price.unionselect.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shangpin.ephub.data.mysql.price.unionselect.bean.PriceQueryDto;
import com.shangpin.ephub.data.mysql.price.unionselect.po.HubSupplierPrice;

@Mapper
public interface HubSupplierPriceMapper {

	public List<HubSupplierPrice> selectByPriceQuery(PriceQueryDto priceQueryDto);
	
	public int countByQuery(PriceQueryDto priceQueryDto);
}
