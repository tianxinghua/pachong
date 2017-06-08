package com.shangpin.ephub.product.business.ui.purchase.service;

import com.shangpin.ephub.product.business.ui.purchase.vo.Product;
import com.shangpin.ephub.product.business.ui.purchase.vo.QueryDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
public interface IProductService {

    public List<Product> getProductList(QueryDto dto);
}
