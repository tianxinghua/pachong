package com.shangpin.iog.product.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SpecialSkuDTO;

@Mapper
public interface SpecialSkuMapper  {

	public void saveList(List<SpecialSkuDTO> stockUpdateDTO);

	public List<SpecialSkuDTO> findListSkuBySupplierId(@Param("supplierId") String supplierId);

	public void delete(SpecialSkuDTO dto);

	public SpecialSkuDTO checkBySupplierIdAndSkuId(@Param("supplierId") String supplierId,
			@Param("skuId") String skuId);


}