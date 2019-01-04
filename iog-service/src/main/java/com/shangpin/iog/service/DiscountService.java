package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.DiscountDTO;

import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
public interface DiscountService {

    /**
     * 查询DISCOUNT表
     * @param (supplierId)
     */
    List<DiscountDTO> findAllBySupplierID(String supplierId) throws ServiceException;
}
