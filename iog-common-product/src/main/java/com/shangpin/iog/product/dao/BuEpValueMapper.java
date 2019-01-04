package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.BuEpValueDTO;

@Mapper
public interface BuEpValueMapper extends IBaseDao<BuEpValueDTO> {

	/**
	 * 根据buId和是否导出查找value集合
	 * @param buId
	 * @param isExport 1为导出，0为不导出
	 * @return
	 */
	public List<String> findValueByBuIdAndIsExport(@Param("buId") String buId, @Param("isExport") String isExport);
}
