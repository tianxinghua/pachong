package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.DiscountDTO;
import com.shangpin.iog.product.dao.DiscountMapper;
import com.shangpin.iog.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxia on 2015/9/6.
 */
@Service("DiscountService")
public class DiscountServiceImp implements DiscountService{

    @Autowired
    DiscountMapper discountDAO;

    @Override
    public List<DiscountDTO> findAllBySupplierID(String supplierId) throws ServiceException{

        List<DiscountDTO> discountDTOList = new ArrayList<>();
        discountDTOList = discountDAO.findAllBySupplierID(supplierId);
        return discountDTOList;
    }

}
