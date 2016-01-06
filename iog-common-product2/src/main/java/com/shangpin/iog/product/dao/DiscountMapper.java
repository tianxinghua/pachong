package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.DiscountDTO;

import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
@Mapper
public interface DiscountMapper extends IBaseDao<DiscountDTO>{

    /**
     * 查询DISCOUNT表
     * @param (supplierId)
     */
     List<DiscountDTO> findAllBySupplierID(String supplierId);

}
