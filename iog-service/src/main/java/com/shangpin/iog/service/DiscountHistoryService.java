package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.DiscountHistoryDTO;

import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
public interface DiscountHistoryService {

    /**
     * 查询DISCOUNT_HISTORY表
     * @param (supplierId)
     */
    List<DiscountHistoryDTO> findAllBySupplierID(String supplierId) throws ServiceException;
}
