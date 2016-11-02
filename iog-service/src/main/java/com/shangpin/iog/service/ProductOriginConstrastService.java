package com.shangpin.iog.service;

import java.util.List;

import com.shangpin.iog.dto.ProductOriginConstrastDTO;

public interface ProductOriginConstrastService {

	/**
	 * 根据级别查找产地字典
	 * @param rank
	 * @return
	 */
	public List<ProductOriginConstrastDTO> findByRank(int rank);
}
