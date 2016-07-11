package com.shangpin.iog.product.dao;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SpuDTO;

@Mapper
public interface SpuMapper extends IBaseDao<SpuDTO> {

	public void updateMaterial(SpuDTO spuDTO);

	public SpuDTO findSPUBySupplierAndSpuId(@Param("supplierId") String supplierId,@Param("spuId") String spuId) throws ServiceException;
	
	public SpuDTO findPartBySupAndSpuId(@Param("supplierId") String supplierId,@Param("spuId") String spuId);
	
	public void updateSpuMemo(@Param("supplierId") String supplierId,@Param("spuId") String spuId,@Param("memo") String memo,@Param("lastTime") Date date);

	public void updateSpuMemoList(@Param("list") List<SpuDTO> spuList);
	
	public void updateSeason(SpuDTO spuDTO);

	public List<SpuDTO> findPartSPUListBySupplierId(@Param("supplierId") String supplierId);

}