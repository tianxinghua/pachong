package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.BrandSpDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.BrandSpMapper;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.BrandSpSearchService;
import com.shangpin.iog.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huxia on 15/6/9.
 */
@Service
public class BrandSpSearchImpl implements BrandSpSearchService {


    @Autowired
    BrandSpMapper brandSpDAO;

    @Override
    public List<BrandSpDTO> findAll() throws ServiceException {

        List<BrandSpDTO> BrandSpDTOList = brandSpDAO.findAll();
        return BrandSpDTOList;
    }
}