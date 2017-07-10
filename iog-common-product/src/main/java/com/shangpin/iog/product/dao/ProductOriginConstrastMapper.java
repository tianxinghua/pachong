package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ProductOriginConstrastDTO;

@Mapper
public interface ProductOriginConstrastMapper extends IBaseDao<ProductOriginConstrastDTO> {

	/**
	 * 根据级别查找产地字典
	 * @param rank
	 * @return
	 */
	public List<ProductOriginConstrastDTO> findByRank(@Param("rank") int rank);
	
	public int findCount();
}
