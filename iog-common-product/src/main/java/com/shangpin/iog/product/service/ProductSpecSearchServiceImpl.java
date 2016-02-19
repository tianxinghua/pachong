package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductOfSpecDTO;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.service.ProductSpecSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lizhongren on 2016/2/17.
 */
@Service
public class ProductSpecSearchServiceImpl implements ProductSpecSearchService{

    @Autowired
    ProductsMapper productDAO;

    @Override
    public ProductOfSpecDTO findProductBySupplierIdAndSkuId(String supplierId, String skuId) throws ServiceException {
        ProductOfSpecDTO  productOfSpecDTO =  productDAO.findProductOfSpecBySupplierIdAndSkuId(supplierId, skuId);
        if(null==productOfSpecDTO){
              productOfSpecDTO = new ProductOfSpecDTO();
            productOfSpecDTO.setSupplierId(supplierId);
            productOfSpecDTO.setSkuId(skuId);
            productOfSpecDTO.setBarCode("");
        }
        return productOfSpecDTO;
    }
}
