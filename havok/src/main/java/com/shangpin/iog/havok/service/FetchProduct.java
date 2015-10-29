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
import java.util.ResourceBundle;

/**
 * Created by huxia on 2015/10/15.
 */
@Component("hovok")
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
            skuJson = HttpUtil45.get(skuUrl,new OutTimeConfig(),null);
            System.out.println("shuju======"+resultJson);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(resultJson!=null&&!resultJson.isEmpty()) {
            Styles productList = null;
            try {
                productList = gson.fromJson(resultJson, new TypeToken<Products>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (productList != null && productList.getSpu() != null) {
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
            }
        }

        if (skuJson != null && !skuJson.isEmpty()) {
            Products skuList = null;
            try {
                skuList = gson.fromJson(skuJson, new TypeToken<Product>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (skuList != null && skuList.getSkus() != null) {
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
            }
        }
        HttpUtil45.closePool();
    }
}
