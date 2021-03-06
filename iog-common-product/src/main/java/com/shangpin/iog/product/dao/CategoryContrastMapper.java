package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.CategoryContrastDTO;

@Mapper
public interface CategoryContrastMapper extends IBaseDao<CategoryContrastDTO> {

	public int  findCount();
}
