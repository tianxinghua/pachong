package com.shangpin.iog.monnierfreres.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
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
import com.shangpin.iog.service.ProductSearchService;

@Component("monnierfreres")
public class FetchProduct {

    private static ResourceBundle bdl = null;
    private static String supplierId;
    private static String url;
    private static int day;
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        day = Integer.valueOf(bdl.getString("day"));
    }
    @Autowired
    private ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    public void fetchProductAndSave() {
        try {
        	
        	Date startDate,endDate= new Date();
 			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
            Map<String,SkuDTO> skuDTOMap = new HashedMap();
 			try {
 				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
 			} catch (ServiceException e) {
 				e.printStackTrace();
 			}

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
                        
                        if(skuDTOMap.containsKey(sku.getSkuId())){
    						skuDTOMap.remove(sku.getSkuId());
    					}
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
            
            //更新网站不再给信息的老数据
            for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
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
