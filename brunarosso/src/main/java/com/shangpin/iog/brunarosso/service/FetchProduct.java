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
                spu.setMaterial(product.getCOMPOSIZIONE_DETTAGLIATA());
                spu.setCategoryGender(product.getSETTORE());
                spu.setSpuId(product.getCODICE_MODELLO());
                spu.setSeasonId(product.getSIGLA_STAGIONE());
                spu.setSeasonName(product.getTIPO_STAGIONE());
                spu.setCategoryName(product.getGRUPPO_SUPER());
                spu.setSubCategoryId(product.getGRUPPO());
                sku.setId(UUIDGenerator.getUUID());
                sku.setProductSize(product.getSIZE_AND_FIT());
                sku.setColor(product.getCOLORE());
                sku.setSupplierPrice(product.getPREZZO_VENDITA_SENZA_IVA());
                sku.setSkuId(product.getID_ARTICOLO());
                sku.setSpuId(product.getCODICE_MODELLO());
                sku.setProductCode(product.getCODICE_MODELLO()+" "+product.getCODICE_VARIANTE());
               // sku.setSupplierId("2015070701319");
                try {
                    productFetchService.saveSKU(sku);
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
