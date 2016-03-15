package com.shangpin.iog.product.dao;


import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SpuDTO;

@Mapper
public interface SpuMapper extends IBaseDao<SpuDTO> {

	public void updateMaterial(SpuDTO spuDTO);

	public SpuDTO findSPUBySupplierAndSpuId(@Param("supplierId") String supplierId,@Param("spuId") String spuId) throws ServiceException;

}