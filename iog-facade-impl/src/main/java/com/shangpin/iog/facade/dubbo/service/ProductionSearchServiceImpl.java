package com.shangpin.iog.facade.dubbo.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.facade.dubbo.dto.ProductDTO;
import com.shangpin.iog.service.ProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;

/**
 * Created by lizhongren on 2016/1/20.
 */
@Service("productionSearchServiceImpl")
public class ProductionSearchServiceImpl implements ProductionSearchService {
    private Logger logger = LoggerFactory.getLogger(ProductionSearchServiceImpl.class);

    @Autowired
    ProductSearchService productSearchService;

    @Override
    public ProductDTO findProductForOrder(String supplierId, String skuId) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSupplierId(supplierId);
        productDTO.setSkuId(skuId);
        try {

            com.shangpin.iog.dto.ProductDTO tmpDTO = productSearchService.findProductBySupplierIdAndSkuId(supplierId,skuId);
            if(null!=tmpDTO){
                productDTO.setSpuId(tmpDTO.getSpuId());
                productDTO.setBrandName(tmpDTO.getBrandName());
                productDTO.setProductOrigin(tmpDTO.getProductOrigin());
                productDTO.setBarcode(tmpDTO.getBarcode());
                productDTO.setCategoryGender(tmpDTO.getCategoryGender());
                productDTO.setColor(tmpDTO.getColor());
                productDTO.setCategoryName(tmpDTO.getCategoryName());
                productDTO.setSubCategoryName(tmpDTO.getSubCategoryName());
                productDTO.setSize(tmpDTO.getSize());
                productDTO.setProductCode(tmpDTO.getProductCode());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return productDTO;
    }
}
