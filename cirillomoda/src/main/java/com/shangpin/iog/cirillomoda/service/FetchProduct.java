package com.shangpin.iog.cirillomoda.service;

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
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component("cirillomoda")
public class FetchProduct {
//    final Logger logger = Logger.getLogger(this.getClass());
//    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=null;
    private static String supplierId; //测试
//    private static String supplierId = ""; //正式

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public void fetchProductAndSave(final String url) {

        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*20, 1000*60*20,1000*60*20);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.") ;
//            logMongo.info(mongMap);

            System.out.println(result);

            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            final Reader reader = new InputStreamReader(IOUtils.toInputStream(result, "UTF-8"), "UTF-8");

            int count = 0;
            String spuId = "";
            String title = "";
            String brand = "";
            String price = "";
            String category = "";
            String description = "";
            String status = "";
            try (CSVParser parser = new CSVParser(reader, csvFileFormat)) {
                for (final CSVRecord record : parser) {
                    if (record.size() <= 1) {
                        continue;
                    }

                    String type = record.get("parent/child");

                    if ("parent".equals(type)) { //SPU
                        spuId = record.get("SKU");
                        title = record.get("Titolo");
                        brand = record.get("Marca");
                        price = record.get("Prezzo");
                        category = record.get("Categoria");
                        description = record.get("description_it_it");
                        status = record.get("Stato");

                        //保存SPU
                        SpuDTO spu = new SpuDTO();
                        //SPU 必填
                        spu.setId(spuId);
                        spu.setSpuId(spuId);
                        spu.setSupplierId(supplierId);
                        spu.setCategoryName(category);
                        spu.setBrandName(brand);
                        spu.setMaterial(description);
//                        spu.setProductOrigin(description);

                        spu.setCategoryGender(category);

                        //SPU 选填
                        spu.setSpuName(title);

                        try {
                            productFetchService.saveSPU(spu);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }

                    } else if ("child".equals(type)) { //SKU
                        System.out.println("count : " + ++count);
//                        System.out.println("------------------");

                        String size = record.get("attribute_size");
                        String stock = record.get("attribute_size:quantity");

                        //库存为0不进行入库
                        if (stock == null || "".equals(stock.trim()) || "0".equals(stock.trim())) {
                            continue;
                        }

                        String skuId = spuId + size;
                        List<String> pics = new ArrayList<>();
                        String photo1 = record.get("Photo 1");
                        if (photo1 != null && !"".equals(photo1)) {
                            pics.add(photo1);
                        }
                        String photo2 = record.get("Photo 2");
                        if (photo2 != null && !"".equals(photo2)) {
                            pics.add(photo2);
                        }
                        String photo3 = record.get("Photo 3");
                        if (photo3 != null && !"".equals(photo3)) {
                            pics.add(photo3);
                        }
                        String photo4 = record.get("Photo 4");
                        if (photo4 != null && !"".equals(photo4)) {
                            pics.add(photo4);
                        }
                        String photo5 = record.get("Photo 5");
                        if (photo5 != null && !"".equals(photo5)) {
                            pics.add(photo1);
                        }
                        String photo6 = record.get("Photo 6");
                        if (photo6 != null && !"".equals(photo6)) {
                            pics.add(photo6);
                        }
                        String photo7 = record.get("Photo 7");
                        if (photo7 != null && !"".equals(photo7)) {
                            pics.add(photo7);
                        }
                        String photo8 = record.get("Photo 8");
                        if (photo8 != null && !"".equals(photo8)) {
                            pics.add(photo8);
                        }

//                        System.out.println("spuId : " + spuId);
//                        System.out.println("title : " + title);
//                        System.out.println("brand : " + brand);
//                        System.out.println("price : " + price);
//                        System.out.println("category : " + category);
//                        System.out.println("description : " + description);
//                        System.out.println("status : " + status);
//                        System.out.println("size : " + size);
//                        System.out.println("stock : " + stock);
//                        System.out.println("skuId : " + skuId);
//                        System.out.println("pics : " + pics);

                        SkuDTO sku = new SkuDTO();
                        //SKU 必填
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSupplierId(supplierId);
                        sku.setSkuId(skuId);
                        sku.setSpuId(spuId);
                        sku.setMarketPrice(price);
//                        sku.setColor(); //没有颜色

                        sku.setProductSize(size);
                        sku.setStock(stock);
                        sku.setProductDescription(description);

                        try {
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

                        for (String pic : pics) {
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
