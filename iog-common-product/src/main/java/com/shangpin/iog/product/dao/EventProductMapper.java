package com.shangpin.iog.product.dao;


import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.SpuDTO;

@Mapper
public interface EventProductMapper extends IBaseDao<EventProductDTO> {

	public EventProductDTO selectEventIdBySku(@Param("skuId")String skuId,@Param("supplierId")String supplierId);
}