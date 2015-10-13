package com.shangpin.iog.monnierfreres.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component("monnierfreres")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
//    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl = null;
    private static String supplierId;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }


    public void fetchProductAndSave(final String url) {

        try {
//            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig =new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "monnierfreres");
//            mongMap.put("result", "stream data.") ;
//            logMongo.info(mongMap);

            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter('|');
            final Reader reader = new InputStreamReader(IOUtils.toInputStream(result, "UTF-8"), "UTF-8");

            int count = 0;
            try (CSVParser parser = new CSVParser(reader, csvFileFormat)) {
                for (final CSVRecord record : parser) {
                    if (record.size() <= 1) {
                        continue;
                    }
                    System.out.println("count : " + ++count);

                    String productId = record.get("product_id");
                    String spuId = record.get("sku");
                    String skuId = spuId + "-" + productId;
                    String stock = record.get("qty");
                    String marketPrice = record.get("price");
                    String image1 = record.get("image_url_1");
                    String image2 = record.get("image_url_2");
                    String image3 = record.get("image_url_3");
                    String image4 = record.get("image_url_4");
                    String image5 = record.get("image_url_5");
                    String name = record.get("name");
                    String description = record.get("description");
                    String size = record.get("box_type");
                    String brand = record.get("brand");
                    String color = record.get("color");
                    String material = record.get("material");
                    String category = record.get("type");

                    System.out.println("productId : " + productId);
                    System.out.println("spuId : " + spuId);
                    System.out.println("skuId : " + skuId);
                    System.out.println("stock : " + stock);
                    System.out.println("marketPrice : " + marketPrice);
                    System.out.println("image1 : " + image1);
                    System.out.println("image2 : " + image2);
                    System.out.println("image3 : " + image3);
                    System.out.println("image4 : " + image4);
                    System.out.println("image5 : " + image5);
                    System.out.println("name : " + name);
                    System.out.println("description : " + description);
                    System.out.println("size : " + size);
                    System.out.println("brand : " + brand);
                    System.out.println("color : " + color);
                    System.out.println("material : " + material);
                    System.out.println("category : " + category);

                    List<String> picsList = new ArrayList<>();
                    if (image1 != null && !"".equals(image1.trim())) {
                        picsList.add(image1);
                    }
                    if (image2 != null && !"".equals(image2.trim())) {
                        picsList.add(image2);
                    }
                    if (image3 != null && !"".equals(image3.trim())) {
                        picsList.add(image3);
                    }
                    if (image4 != null && !"".equals(image4.trim())) {
                        picsList.add(image4);
                    }
                    if (image5 != null && !"".equals(image5.trim())) {
                        picsList.add(image5);
                    }

                    //库存为0不进行入库
                    if (stock == null || "".equals(stock.trim()) || "0".equals(stock.trim())) {
                        continue;
                    }


                    //SKU
                    SkuDTO sku = new SkuDTO();
                    //SKU 必填
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(supplierId);
                    sku.setSkuId(skuId);
                    sku.setSpuId(spuId);
                    sku.setMarketPrice(marketPrice);
                    sku.setColor(color);
                    sku.setProductSize(size);
                    sku.setStock(stock);

                    //SKU 选填
                    sku.setProductName(name);
                    sku.setProductDescription(description);
                    sku.setProductCode(spuId);

                    //保存SKU
                    try {
                        productFetchService.saveSKU(sku);
                    } catch (ServiceException e) {
                        try {
                            if (e.getMessage().equals("数据插入失败键重复")) {
                                productFetchService.updatePriceAndStock(sku);
                            } else {
                                e.printStackTrace();
                            }
                        } catch (ServiceException e1) {
                            e1.printStackTrace();
                        }
                    }

                    //保存SKU的图片
                    for (String pic : picsList) {
                        ProductPictureDTO dto = new ProductPictureDTO();
                        dto.setPicUrl(pic);
                        dto.setSupplierId(supplierId);
                        dto.setId(UUIDGenerator.getUUID());
                        dto.setSkuId(skuId);
                        try {
                            productFetchService.savePictureForMongo(dto);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }

                    //SPU
                    SpuDTO spu = new SpuDTO();
                    //SPU 必填
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSpuId(spuId);
                    spu.setSupplierId(supplierId);
                    spu.setCategoryName(category);
                    spu.setBrandName(brand);

                    //如果材质为空,保存描述,描述中可能会有材质信息
                    if (material != null && !"".equals(material.trim())) {
                        spu.setMaterial(material);
                    } else {
                        spu.setMaterial(description);
                    }

                    //全部为女,刘芳已确认
                    spu.setCategoryGender("female");


                    //SPU选填
                    //全部2015,刘芳已确认
                    spu.setSeasonName("2015");

                    try {
                        productFetchService.saveSPU(spu);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
