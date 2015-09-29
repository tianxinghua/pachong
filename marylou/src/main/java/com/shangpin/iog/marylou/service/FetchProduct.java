package com.shangpin.iog.marylou.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.marylou.dto.Item;
import com.shangpin.iog.marylou.dto.Items;
import com.shangpin.iog.marylou.dto.Product;
import com.shangpin.iog.marylou.dto.Products;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
@Component("marylou")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    private ProductFetchService productFetchService;

    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){

        //获取产品信息
        logMongo.info("get product starting....");
        String json = new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
        logMongo.info("get product over");
        //解析产品信息
        Products products = null;
        try {
            products = ObjectXMLUtil.xml2Obj(Products.class, json);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(products.getProducts().size());
        //映射数据并保存
        logMongo.info("save product into DB begin");
        messMappingAndSave(products);
        logMongo.info("save product into DB success");
 
        //System.out.println(json);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Products products) {
        List<Product> productList = products.getProducts();
        for (Product product : productList) {
            SpuDTO spu = new SpuDTO();

            Items items = product.getItems();
            if (null == items) {//判断SKU
                continue;
            }

            List<Item> itemList = items.getItems();
            if (null == itemList) continue;
            String skuId = "";
            for (Item item : itemList) {
                SkuDTO sku = new SkuDTO();
                try {
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(Constant.SUPP_ID_MARYLOU);

                    sku.setSpuId(product.getProductId());
                    skuId = item.getItemId();
                    if(skuId.indexOf("½")>0){
                        skuId = skuId.replace("½","+");
                    }

                    sku.setSkuId(skuId);

                    String itemSize = item.getItem_size();
                    if(itemSize.indexOf("½")>0){
                    	itemSize = itemSize.replace("½","+");
                    }
                    sku.setProductSize(itemSize);
                    sku.setSaleCurrency(item.getPrice_currency());
                    sku.setSupplierPrice(item.getSupply_price());
                    sku.setColor(product.getColor());
                    sku.setProductDescription(product.getDescription());
                    sku.setStock(item.getStock());
                    productFetchService.saveSKU(sku);

                    if (StringUtils.isNotBlank(item.getPicture())) {
                        String[] picArray = item.getPicture().split("\\|");

//                            List<String> picUrlList = Arrays.asList(picArray);
                        for (String picUrl : picArray) {
                            ProductPictureDTO dto = new ProductPictureDTO();
                            dto.setPicUrl(picUrl);
                            dto.setSupplierId(Constant.SUPP_ID_MARYLOU);
                            dto.setId(UUIDGenerator.getUUID());
                            dto.setSkuId(item.getItemId());
                            try {
//                                    productFetchService.savePicture(dto);
                                productFetchService.savePictureForMongo(dto);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (ServiceException e) {
                    try {
                        if (e.getMessage().equals("数据插入失败键重复")) {
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else {
                            e.printStackTrace();
                        }

                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPP_ID_MARYLOU);
                spu.setSpuId(product.getProductId());
                spu.setBrandName(product.getBrand());
                spu.setCategoryName(product.getCategory());
                spu.setSpuName(product.getName());
                spu.setSeasonId(product.getSeason());
                spu.setCategoryGender(product.getGender());
                spu.setMaterial(product.getMaterial());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
