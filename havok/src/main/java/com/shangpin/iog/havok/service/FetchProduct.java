package com.shangpin.iog.havok.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.havok.dto.Style;
import com.shangpin.iog.havok.dto.Product;
import com.shangpin.iog.havok.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.havok.dto.Styles;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by huxia on 2015/10/15.
 */
@Component("havok")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String supplierId;

    @Autowired
    private ProductFetchService pfs;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
    }

    public void fetchAndSave(){
        String resultJson = null;
        String skuJson = null;
        String spuUrl ="http://webserv.havok.it/stock/v1/style.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin";
        String skuUrl = "http://webserv.havok.it/stock/v1/product.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin";
        Gson gson = new Gson();
        try{
            resultJson = HttpUtil45.get(spuUrl,new OutTimeConfig(),null);
            //skuJson = HttpUtil45.get(skuUrl,new OutTimeConfig(),null);
            skuJson = "{\"products\":[{\"SPUID\":1,\"SKUID\":\"7777000069971\",\"productName\":\"FW15 314742 EFW0Y\",\"marketPrice\":245.00,\"barcode\":\"7777000069971\",\"productCode\":\"FW15 314742 EFW0Y\",\"color\":\"5801\",\"productDescription\":\"BRACELET\",\"saleCurrency\":245,\"productSize\":\"U\",\"stock\":5},{\"SPUID\":1,\"SKUID\":\"7777000010737\",\"productName\":\"FW15 390020 Y578J\",\"marketPrice\":1990.00,\"barcode\":\"7777000010737\",\"productCode\":\"FW15 390020 Y578J\",\"color\":\"1073 GRIGIO\",\"productDescription\":\"CAPPOTTO GRIGIO QUADRI DOPPIO\",\"saleCurrency\":1990,\"productSize\":\"48\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000010744\",\"productName\":\"FW15 390020 Y578J\",\"marketPrice\":1990.00,\"barcode\":\"7777000010744\",\"productCode\":\"FW15 390020 Y578J\",\"color\":\"1073 GRIGIO\",\"productDescription\":\"CAPPOTTO GRIGIO QUADRI DOPPIO\",\"saleCurrency\":1990,\"productSize\":\"50\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000010751\",\"productName\":\"FW15 390020 Y578J\",\"marketPrice\":1990.00,\"barcode\":\"7777000010751\",\"productCode\":\"FW15 390020 Y578J\",\"color\":\"1073 GRIGIO\",\"productDescription\":\"CAPPOTTO GRIGIO QUADRI DOPPIO\",\"saleCurrency\":1990,\"productSize\":\"52\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000135829\",\"productName\":\"FW15 394457 BJ50J\",\"marketPrice\":1690.00,\"barcode\":\"7777000135829\",\"productCode\":\"FW15 394457 BJ50J\",\"color\":\"6920\",\"productDescription\":\"BAG\",\"saleCurrency\":1690,\"productSize\":\"U\",\"stock\":2},{\"SPUID\":1,\"SKUID\":\"7777000112400\",\"productName\":\"FW15 395729 Q1VSU\",\"marketPrice\":1495.00,\"barcode\":\"7777000112400\",\"productCode\":\"FW15 395729 Q1VSU\",\"color\":\"6219\",\"productDescription\":\"DRESS\",\"saleCurrency\":1495,\"productSize\":\"S\",\"stock\":2},{\"SPUID\":1,\"SKUID\":\"7777000112394\",\"productName\":\"FW15 395729 Q1VSU\",\"marketPrice\":1495.00,\"barcode\":\"7777000112394\",\"productCode\":\"FW15 395729 Q1VSU\",\"color\":\"6219\",\"productDescription\":\"DRESS\",\"saleCurrency\":1495,\"productSize\":\"XS\",\"stock\":2},{\"SPUID\":1,\"SKUID\":\"7777000055011\",\"productName\":\"FW15 8BL124 5C4\",\"marketPrice\":1550.00,\"barcode\":\"7777000055011\",\"productCode\":\"FW15 8BL124 5C4\",\"color\":\"66T\",\"productDescription\":\"BY THE WAY\",\"saleCurrency\":1550,\"productSize\":\"U\",\"stock\":2},{\"SPUID\":1,\"SKUID\":\"7777000137502\",\"productName\":\"FW15 AGNES PAT\",\"marketPrice\":425.00,\"barcode\":\"7777000137502\",\"productCode\":\"FW15 AGNES PAT\",\"color\":\"BLACK\",\"productDescription\":\"SHOES\",\"saleCurrency\":425,\"productSize\":\"35+\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000167943\",\"productName\":\"FW15 AGNES PAT\",\"marketPrice\":425.00,\"barcode\":\"7777000167943\",\"productCode\":\"FW15 AGNES PAT\",\"color\":\"BLACK\",\"productDescription\":\"SHOES\",\"saleCurrency\":425,\"productSize\":\"39\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000104191\",\"productName\":\"FW15 BO0079 ALVY 15H002S\",\"marketPrice\":680.00,\"barcode\":\"7777000104191\",\"productCode\":\"FW15 BO0079 ALVY 15H002S\",\"color\":\"01BK\",\"productDescription\":\"BOOTS\",\"saleCurrency\":680,\"productSize\":\"36\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000104207\",\"productName\":\"FW15 BO0079 ALVY 15H002S\",\"marketPrice\":680.00,\"barcode\":\"7777000104207\",\"productCode\":\"FW15 BO0079 ALVY 15H002S\",\"color\":\"01BK\",\"productDescription\":\"BOOTS\",\"saleCurrency\":680,\"productSize\":\"37\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000104214\",\"productName\":\"FW15 BO0079 ALVY 15H002S\",\"marketPrice\":680.00,\"barcode\":\"7777000104214\",\"productCode\":\"FW15 BO0079 ALVY 15H002S\",\"color\":\"01BK\",\"productDescription\":\"BOOTS\",\"saleCurrency\":680,\"productSize\":\"38\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000025106\",\"productName\":\"FW15 FZZ698 1WT\",\"marketPrice\":770.00,\"barcode\":\"7777000025106\",\"productCode\":\"FW15 FZZ698 1WT\",\"color\":\"11C\",\"productDescription\":\"PULL\",\"saleCurrency\":770,\"productSize\":\"38\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000101657\",\"productName\":\"FW15 MHT0695 MILEM 15A024I\",\"marketPrice\":580.00,\"barcode\":\"7777000101657\",\"productCode\":\"FW15 MHT0695 MILEM 15A024I\",\"color\":\"30LU LIGHT BLUE\",\"productDescription\":\"CAMICIA SENZA COLLO\",\"saleCurrency\":580,\"productSize\":\"34\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000101664\",\"productName\":\"FW15 MHT0695 MILEM 15A024I\",\"marketPrice\":580.00,\"barcode\":\"7777000101664\",\"productCode\":\"FW15 MHT0695 MILEM 15A024I\",\"color\":\"30LU LIGHT BLUE\",\"productDescription\":\"CAMICIA SENZA COLLO\",\"saleCurrency\":580,\"productSize\":\"36\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000101671\",\"productName\":\"FW15 MHT0695 MILEM 15A024I\",\"marketPrice\":580.00,\"barcode\":\"7777000101671\",\"productCode\":\"FW15 MHT0695 MILEM 15A024I\",\"color\":\"30LU LIGHT BLUE\",\"productDescription\":\"CAMICIA SENZA COLLO\",\"saleCurrency\":580,\"productSize\":\"38\",\"stock\":1},{\"SPUID\":1,\"SKUID\":\"7777000130299\",\"productName\":\"FW15 WIZE CAP X\",\"marketPrice\":70.00,\"barcode\":\"7777000130299\",\"productCode\":\"FW15 WIZE CAP X\",\"color\":\"BLACK\",\"productDescription\":\"HAT\",\"saleCurrency\":70,\"productSize\":\"U\",\"stock\":20}]}";
            System.out.println("resultJson======"+resultJson);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(resultJson!=null&&!resultJson.isEmpty()) {
            Styles productList = null;
            Products skuList = null;
            try {
                productList = gson.fromJson(resultJson, new TypeToken<Styles>() {
                }.getType());
                skuList = gson.fromJson(skuJson, new TypeToken<Products>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(productList.getStyles().length+"----------1---------");
            System.out.println(skuList.getProducts().length+"---------2----------");
/*            if (productList != null && productList.getSpu() != null) {
                for (Style product : productList.getSpu()) {
                    SpuDTO spuDTO = new SpuDTO();
                    spuDTO.setId(UUIDGenerator.getUUID());
                    spuDTO.setSpuId(product.getSPUID());
                    spuDTO.setSupplierId(supplierId);
                    spuDTO.setBrandId(product.getBrandID());
                    spuDTO.setBrandName(product.getBrandName());
                    spuDTO.setCategoryGender(product.getCategoryGender());
                    spuDTO.setCategoryName(product.getCategoryName());
                    spuDTO.setSubCategoryId(product.getSubcategoryID());
                    spuDTO.setSubCategoryName(product.getSubcategoryName());
                    spuDTO.setMaterial(product.getMaterial());
                    spuDTO.setSeasonId(product.getSeasonID());
                    spuDTO.setSpuName(product.getSpuName());
                    spuDTO.setPicUrl(product.getPicUrl());
                    spuDTO.setProductOrigin(product.getProductOrigin());
                    spuDTO.setCreateTime(new Date());
                    try {
                        pfs.saveSPU(spuDTO);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }

        if (skuJson != null && !skuJson.isEmpty()) {
            Products skuList = null;
            try {
                skuJson.replace("[\"{","[{");
                System.out.println("skuJson======"+skuJson);
                skuList = gson.fromJson(skuJson, new TypeToken<Products>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(skuList.getProducts().length+"---------2----------");
/*            if (skuList != null && skuList.getSkus() != null) {
                for (Product sku : skuList.getSkus()) {
                    SkuDTO skuDTO = new SkuDTO();
                    ProductPictureDTO productPic = new ProductPictureDTO();
                    skuDTO.setId(UUIDGenerator.getUUID());
                    skuDTO.setSkuId(sku.getSKUID());
                    skuDTO.setSpuId(sku.getSPUID());
                    skuDTO.setSupplierId(supplierId);
                    skuDTO.setProductCode(sku.getProductCode());
                    skuDTO.setBarcode(sku.getBarcode());
                    skuDTO.setColor(sku.getColor());
                    skuDTO.setMarketPrice(sku.getMarketPrice());
                    skuDTO.setSaleCurrency(sku.getSaleCurrency());
                    //skuDTO.setSalePrice(sku);
                    skuDTO.setProductSize(sku.getProductSize());
                    skuDTO.setStock(sku.getStock());
                    skuDTO.setProductDescription(sku.getProductDescription());
                    skuDTO.setCreateTime(new Date());

                    productPic.setId(UUIDGenerator.getUUID());
                    productPic.setSupplierId(supplierId);
                    productPic.setSpuId(sku.getSPUID());
                    productPic.setSkuId(sku.getSKUID());
                    //productPic.setPicUrl(sku.getPicture());
                    try {
                        pfs.saveSKU(skuDTO);
                        pfs.savePictureForMongo(productPic);
                    } catch (ServiceException e) {
                        try {
                            if (e.getMessage().equals("数据插入失败键重复")) {
                                //更新价格和库存
                                pfs.updatePriceAndStock(skuDTO);
                            } else {
                                e.printStackTrace();
                            }
                        } catch (ServiceException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }*/
        }
        HttpUtil45.closePool();
    }
}
