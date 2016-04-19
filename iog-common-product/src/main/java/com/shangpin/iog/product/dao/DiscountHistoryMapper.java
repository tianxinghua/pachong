package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.DiscountDTO;
import com.shangpin.iog.dto.DiscountHistoryDTO;

import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
@Mapper
public interface DiscountHistoryMapper extends IBaseDao<DiscountHistoryDTO> {

    /**
     * 查询DISCOUNT_HISTORY表
     * @param (supplierId)
     */
    List<DiscountHistoryDTO> findAllBySupplierID(String supplierId);
}
