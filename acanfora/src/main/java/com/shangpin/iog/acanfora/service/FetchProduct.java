package com.shangpin.iog.acanfora.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.acanfora.dto.Item;
import com.shangpin.iog.acanfora.dto.Items;
import com.shangpin.iog.acanfora.dto.Product;
import com.shangpin.iog.acanfora.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("acanfora")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url){


        try {
            String result =  HttpUtils.get(url);
            Products products= ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();
            for(Product product:productList){
                SpuDTO spu = new SpuDTO();


                Items items = product.getItems();
                if(null==items){//无SKU
                    continue;
                }

                List<Item> itemList = items.getItems();
//                for(Item item:itemList){
//                    SkuDTO sku  = new SkuDTO();
//                    try {
//                        sku.setId(UUIDGenerator.getUUID());
//                        sku.setSupplierId("00000001");
//                        sku.setSpuId(product.getProductId());
//                        sku.setSkuId(item.getItem_id());
//                        sku.setProductSize(item.getItem_size());
//                        sku.setSupplierPrice(item.getSupply_price());
//                        sku.setColor(item.getColor());
//                        sku.setProductDescription(item.getDescription());
//                        sku.setStock(item.getStock());
//                        productFetchService.saveSKU(sku);
//                    } catch (ServiceException e) {
//                        e.printStackTrace();
//                    }
//                }

                try {
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId("00000001");
                    spu.setSpuId(product.getProductId());
                    spu.setBrandName(product.getProduct_brand());
                    spu.setCategoryName(product.getCategory());
                    spu.setSpuName(product.getProduct_name());
                    spu.setSeasonId(product.getSeason_code());
                    spu.setMaterial(product.getProduct_material());
                    spu.setPicUrl(product.getUrl());
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }


            }


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
