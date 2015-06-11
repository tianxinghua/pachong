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

import java.sql.SQLException;
import java.util.*;

/**
 * Created by huxia on 15/6/9.
 */
@Service
public class BrandSpSearchImpl implements BrandSpSearchService {


    @Autowired
    BrandSpMapper brandSpDAO;

    @Override
    public List<BrandSpDTO> findAll() throws ServiceException {

        List<BrandSpDTO> BrandSpDTOList = new ArrayList<>();
        try {
            BrandSpDTOList = brandSpDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BrandSpDTOList;
    }
}