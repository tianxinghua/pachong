package com.shangpin.iog.eleonorabonucci.service;

import com.shangpin.framework.ServiceException;

import com.shangpin.iog.common.utils.DateTimeUtil;
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
import com.shangpin.iog.service.ProductSearchService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.*;

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

    @Autowired
    ProductSearchService productSearchService;

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static int day;
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
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

//            System.out.println("result : " + result);
            logInfo.info("result="+ result);

            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }

            Date startDate,endDate= new Date();
            startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
            Map<String,SkuDTO> skuDTOMap = new HashedMap();
            try {
                skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
            } catch (ServiceException e) {
                e.printStackTrace();
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

                        if(skuDTOMap.containsKey(sku.getSkuId())){
                            skuDTOMap.remove(sku.getSkuId());
                        }
                        productFetchService.saveSKU(sku);




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

                    if(StringUtils.isNotBlank(item.getPicture())){
                        String[] picArray = item.getPicture().split("\\|");

                        List<String> picUrlList = Arrays.asList(picArray);
                        productFetchService.savePicture(supplierId, null, skuId, picUrlList);
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
                    try {
                        productFetchService.updateMaterial(spu);
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }
            }

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
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
