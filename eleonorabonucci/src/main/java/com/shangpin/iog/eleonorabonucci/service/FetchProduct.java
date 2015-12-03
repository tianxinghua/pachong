package com.shangpin.iog.eleonorabonucci.service;

import com.shangpin.framework.ServiceException;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.eleonorabonucci.dto.Item;
import com.shangpin.iog.eleonorabonucci.dto.Items;
import com.shangpin.iog.eleonorabonucci.dto.Product;
import com.shangpin.iog.eleonorabonucci.dto.Products;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("eleonorabonucci")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logInfo = Logger.getLogger("info");
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public void fetchProductAndSave(String url) {

        try {
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30, 1000*60*30,1000*60*30);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId",supplierId);
//            mongMap.put("supplierName","acanfora");
//            mongMap.put("result",result) ;
//            logMongo.info(mongMap);

            System.out.println("result : " + result);
            logInfo.info("result="+ result);

            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }

            Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();
            String skuId = "";
            String size="";
            for (Product product : productList) {
                SpuDTO spu = new SpuDTO();


                Items items = product.getItems();
                if (null == items) {//无SKU
                    continue;
                }

                System.out.println(product);

                List<Item> itemList = items.getItems();
                if (null == itemList) continue;

                for (Item item : itemList) {

                    //库存为0不进行入库
                    if (item.getStock() == null || "".equals(item.getStock().trim()) || "0".equals(item.getStock().trim())) {
                        continue;
                    }

                    SkuDTO sku = new SkuDTO();
                    try {
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSupplierId(supplierId);

                        sku.setSpuId(product.getProductId());
                        skuId = item.getItem_id();
                        if (skuId.indexOf("½") > 0) {
                            skuId = skuId.replace("½", "+");
                        }
                        sku.setSkuId(skuId);
                        size = item.getItem_size();
                        if(StringUtils.isNotBlank(size)){
                            if (size.indexOf("½") > 0) {
                                size = size.replace("½", ".5");
                            }
                        }
                        sku.setProductSize(size);
                        if(item.getMarket_price() != null) {
                            sku.setMarketPrice(item.getMarket_price().replaceAll(",", "."));
                        }

                        if (item.getSell_price() != null) {
                            sku.setSalePrice(item.getSell_price().replaceAll(",", "."));
                        }

                        if (item.getSupply_price() != null) {
                            sku.setSupplierPrice(item.getSupply_price().replaceAll(",", "."));
                        }

                        sku.setColor(item.getColor());
                        sku.setProductDescription(item.getDescription());
                        sku.setStock(item.getStock());
                        sku.setProductCode(product.getProducer_id());
                        sku.setProductDescription(product.getDescription());

                        if (item.getStock() != null && !"".equals(item.getStock()) && Integer.valueOf(item.getStock()) > 0) {
                            productFetchService.saveSKU(sku);
                            if (StringUtils.isNotBlank(item.getPicture())) {
                                String[] picArray = item.getPicture().split("\\|");
                                for (String picUrl : picArray) {
                                    ProductPictureDTO dto = new ProductPictureDTO();
                                    dto.setPicUrl(picUrl);
                                    dto.setSupplierId(supplierId);
                                    dto.setId(UUIDGenerator.getUUID());
                                    dto.setSkuId(sku.getSkuId());
                                    try {
                                        productFetchService.savePictureForMongo(dto);
                                    } catch (ServiceException e) {
                                        e.printStackTrace();
                                    }

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
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(product.getProductId());
                    spu.setBrandName(product.getProduct_brand());
                    spu.setCategoryName(product.getCategory());
                    spu.setSpuName(product.getProduct_name());
                    spu.setSeasonId(product.getSeason_code());
                    spu.setMaterial(product.getProduct_material());


                    spu.setCategoryGender(product.getGender());
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
