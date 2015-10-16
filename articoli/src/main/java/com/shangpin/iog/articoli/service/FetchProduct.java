package com.shangpin.iog.articoli.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.articoli.dto.Item;
import com.shangpin.iog.articoli.dto.Product;
import com.shangpin.iog.articoli.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by huxia on 2015/10/15.
 */
@Component("articoli")
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
        String result = null;
        String url = null;
        Gson gson = new Gson();
        try{
            result = HttpUtil45.get(url,new OutTimeConfig(),null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result!=null&&!result.isEmpty()){
            Products productList = null;
            try{
                productList = gson.fromJson(result, new TypeToken<Products>() {}.getType());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(productList!=null&&productList.getProducts()!=null){
                for(Product product:productList.getProducts()){
                    SpuDTO spuDTO = new SpuDTO();
                    spuDTO.setId(UUIDGenerator.getUUID());
                    spuDTO.setSpuId(product.getProductId());
                    spuDTO.setSupplierId(supplierId);
                    spuDTO.setBrandName(product.getProduct_brand());
                    spuDTO.setCategoryGender(product.getGender());
                    spuDTO.setCategoryName(product.getCategory());
                    spuDTO.setMaterial(product.getProduct_material());
                    spuDTO.setSeasonId(product.getSeason_code());
                    spuDTO.setSpuName(product.getProduct_name());
                    spuDTO.setPicUrl(product.getUrl());
                    spuDTO.setCreateTime(new Date());
                    try {
                        pfs.saveSPU(spuDTO);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }

                    for(Item sku:product.getItems().getItem()){
                        SkuDTO skuDTO = new SkuDTO();
                        ProductPictureDTO productPic = new ProductPictureDTO();
                        skuDTO.setId(UUIDGenerator.getUUID());
                        skuDTO.setSkuId(sku.getItem_id());
                        skuDTO.setSpuId(product.getProductId());
                        skuDTO.setSupplierId(supplierId);
                        skuDTO.setProductCode(product.getProducer_id());
                        skuDTO.setBarcode(sku.getBarcode());
                        skuDTO.setColor(sku.getColor());
                        skuDTO.setMarketPrice(sku.getMarket_price());
                        skuDTO.setSupplierPrice(sku.getSupply_price());
                        skuDTO.setSalePrice(sku.getSell_price());
                        skuDTO.setProductSize(sku.getItem_size());
                        skuDTO.setStock(sku.getStock());
                        skuDTO.setCreateTime(new Date());

                        productPic.setId(UUIDGenerator.getUUID());
                        productPic.setSupplierId(supplierId);
                        productPic.setSpuId(product.getProductId());
                        productPic.setSkuId(sku.getItem_id());
                        productPic.setPicUrl(sku.getPicture());
                        try{
                            pfs.saveSKU(skuDTO);
                            pfs.savePictureForMongo(productPic);
                        }catch (ServiceException e) {
                            try {
                                if(e.getMessage().equals("数据插入失败键重复")){
                                    //更新价格和库存
                                    pfs.updatePriceAndStock(skuDTO);
                                } else{
                                    e.printStackTrace();
                                }

                            } catch (ServiceException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        HttpUtil45.closePool();
    }
}
