package com.shangpin.iog.product.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.SpuDTO;

@Mapper
public interface EventProductMapper extends IBaseDao<EventProductDTO> {

	public EventProductDTO selectEventIdBySku(@Param("skuId")String skuId,@Param("supplierId")String supplierId);

	public EventProductDTO checkEventSku(@Param("supplierId")String supplierId,@Param("sku")String sku);

	public EventProductDTO updateEvent(EventProductDTO dto);

	public List<EventProductDTO> selectEventList();
}