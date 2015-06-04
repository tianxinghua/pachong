package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SpinnakerProductDTO;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductServiceImpl implements ProductSearchService {
    protected  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SkuMapper skuDAO;

    @Autowired
    SpuMapper spuDAO;


    @Override
    public Page<ProductDTO> findProductBySupplierAndTime(String supplier, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        return null;
    }

    @Override
    public AccountsExcelTemplate exportProduct(String templatePath, String supplier, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        return null;
    }

    @Override
    public StringBuffer exportProduct(String supplier, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        return null;
    }
}
