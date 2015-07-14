package com.shangpin.iog.brunarosso.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.brunarosso.dto.Product;
import com.shangpin.iog.brunarosso.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by sunny on 2015/7/10.
 */
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProductFetchService productFetchService;
    public void fetchProductAndSave(String url) {
        String supplierId = "2015070301312";

        String result = HttpUtils.get(url, false, 360000);
        try {
            Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();
            for (Product product:productList){
                SpuDTO spu = new SpuDTO();
                SkuDTO sku = new SkuDTO();
                spu.setId(UUIDGenerator.getUUID());
                spu.setBrandName(product.getBRAND());
                spu.setCategoryGender(product.getSETTORE());
                spu.setSpuId(product.getCODICE_MODELLO());
                spu.setSeasonId(product.getSIGLA_STAGIONE());
                spu.setSeasonName(product.getTIPO_STAGIONE());

            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
