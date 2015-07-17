package com.shangpin.iog.brunarosso.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.brunarosso.dto.Product;
import com.shangpin.iog.brunarosso.dto.Products;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by sunny on 2015/7/10.
 */
@Component("brunarosso")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProductFetchService productFetchService;
    public void fetchProductAndSave(String url) {
        String supplierId = "2015070301312";
        //String result = HttpUtils.get(url, false, 360000);
        try {
            /*Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();*/
            List<org.jdom2.Element>list  = XmlReader.getElement("");
            /*for (Product product:productList){
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
               // sku.setSupplierId("2015070701319");*/
                for (org.jdom2.Element element:list){
                    SpuDTO spu = new SpuDTO();
                    SkuDTO sku = new SkuDTO();
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setBrandName(element.getChildText("BRAND"));
                    spu.setMaterial(element.getChildText("COMPOSIZIONE_DETTAGLIATA"));
                    spu.setCategoryGender(element.getChildText("SETTORE"));
                    spu.setSpuId(element.getChildText("CODICE_MODELLO"));
                    spu.setSeasonId(element.getChildText("SIGLA_STAGIONE"));
                    spu.setSeasonName(element.getChildText("TIPO_STAGIONE"));
                    spu.setCategoryName(element.getChildText("GRUPPO_SUPER"));
                    spu.setSubCategoryId(element.getChildText("GRUPPO"));
                    spu.setSupplierId("2015071600027");
                    if (element.getChildText("SIZE_AND_FIT").length()>200){
                        System.out.println(element.getChildText("SIZE_AND_FIT")+": " +element.getChildText("SIZE_AND_FIT").length());
                    }
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setProductSize(element.getChildText("SIZE_AND_FIT"));
                    sku.setColor(element.getChildText("COLORE"));
                    sku.setSupplierPrice(element.getChildText("PREZZO_VENDITA_SENZA_IVA"));
                    sku.setSkuId(element.getChildText("ID_ARTICOLO"));
                    sku.setSpuId(element.getChildText("CODICE_MODELLO"));
                    sku.setProductCode(element.getChildText("CODICE_MODELLO")+" "+element.getChildText("CODICE_VARIANTE"));
                    sku.setSupplierId("2015071600027");
                try {
                    productFetchService.saveSKU(sku);
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) {

    }
}
