package com.shangpin.iog.monnierfreres.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.monnierfreres.dto.Item;
import com.shangpin.iog.monnierfreres.dto.Product;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;

@Component("monnierfreres")
public class FetchProduct {

    private static ResourceBundle bdl = null;
    private static String supplierId;
    private static String url;
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    @Autowired
    private ProductFetchService productFetchService;

    public void fetchProductAndSave() {
        try {

            List<Product> list = DownloadAndReadCSV.readLocalCSV();
            for (Product product : list) {
                SpuDTO spu = new SpuDTO();

                List<Item> items = product.getItems();

                if (items.size() == 0) {
                    continue;
                }
                for (Item item : items) {
                    SkuDTO sku = new SkuDTO();

                    try {
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSkuId(item.getItemCode());
                        sku.setSupplierId(supplierId);
                        sku.setSpuId(product.getProductCode());
                        sku.setProductCode(product.getProductCode());
                        sku.setColor(product.getColor());
                        sku.setSaleCurrency("USD");
                        sku.setMarketPrice(item.getPrice());
                        sku.setStock(item.getStock());
                        sku.setBarcode(item.getBarCode());
                        sku.setProductSize(item.getSize());
                        sku.setProductName(product.getProductName());
                        sku.setProductDescription(product.getDescription());
                        productFetchService.saveSKU(sku);


                    } catch (ServiceException e) {
                        try {
                            if (e.getMessage().equals("数据插入失败键重复")) {
                                // 更新价格和库存
                                productFetchService.updatePriceAndStock(sku);
                            } else {
                                e.printStackTrace();
                            }

                        } catch (ServiceException e1) {
                            e1.printStackTrace();
                        }
                    }
                    String[] images = product.getImage_url();
                    productFetchService.savePicture(supplierId, null, item.getItemCode(), Arrays.asList(images));
              
                }

                try {
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSpuId(product.getProductCode());
                    spu.setSupplierId(supplierId);
                    spu.setBrandName(product.getBrand());
                    spu.setCategoryName(product.getCategory());
                    spu.setSubCategoryName(product.getSubCategory());
                    spu.setCategoryGender("female");
                    spu.setMaterial(product.getMaterial());
                    spu.setProductOrigin(product.getMade());
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                	try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
    }
}
