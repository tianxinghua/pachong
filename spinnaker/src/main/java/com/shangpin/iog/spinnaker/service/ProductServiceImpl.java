package com.shangpin.iog.spinnaker.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.service.ProductService;
import com.shangpin.iog.spinnaker.dao.ProductsMapper;
import com.shangpin.iog.spinnaker.domain.Product;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductsMapper productsDAO;

    @Override
    public void fetchProduct() throws ServiceException {




    }

    @Override
    public Page<ProductDTO> findProduct(String categoryId,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {

        List<Product> productList = productsDAO.findListByCategoryAndLastDate(categoryId,startDate,endDate, new RowBounds(pageIndex, pageSize));

        return null;
    }

    @Override
    public AccountsExcelTemplate exportProduct(String categoryId,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {

        this.findProduct(categoryId,startDate,endDate,1,10);
        return null;
    }
}
