package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.DiscountHistoryDTO;
import com.shangpin.iog.product.dao.DiscountHistoryMapper;
import com.shangpin.iog.service.DiscountHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
@Service("DiscountHistoryService")
public class DiscountHistoryServiceImp implements DiscountHistoryService {

    @Autowired
    DiscountHistoryMapper discountHistoryDAO;

    @Override
    public List<DiscountHistoryDTO> findAllBySupplierID(String supplierId) throws ServiceException{

        List<DiscountHistoryDTO> discountHistoryDTO = new ArrayList<>();
        discountHistoryDTO = discountHistoryDAO.findAllBySupplierID(supplierId);
        return discountHistoryDTO;
    }
}
