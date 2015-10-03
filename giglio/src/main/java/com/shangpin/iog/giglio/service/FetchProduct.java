package com.shangpin.iog.giglio.service;

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
@Component("giglio")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
//    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=null;
    private static String supplierId = "2015091801508";


    public void fetchProductAndSave(final String url) {

        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);

            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.") ;
//            logMongo.info(mongMap);

            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            final Reader reader = new InputStreamReader(IOUtils.toInputStream(result, "UTF-8"), "UTF-8");

            int count = 0;
            Pattern pss = Pattern.compile("(.+)\\((\\d+)\\)");
            try (CSVParser parser = new CSVParser(reader, csvFileFormat)) {
                for (final CSVRecord record : parser) {
                    if (record.size() <= 1) {
                        continue;
                    }
                    System.out.println("count : " + ++count);
                    String spuId = record.get("idProdotto");
                    if (spuId != null && !"".equals(spuId)) {
                        spuId = spuId.trim();
                    }

                    final String skuId = record.get("idSKU");
                    final String productCode = record.get("Codice Prodotto"); //货号
                    final String brand = record.get("Marca"); //品牌
                    final String name = record.get("Nome Modello"); // 产品名称
                    final String color = record.get("Colore ENG"); // 颜色
                    final String desc = record.get("Descrizione ENG"); // 描述
                    final String material = record.get("Materiale ENG"); // 材质
                    final String sex = record.get("Sesso(unisex, uomo, donna)"); // 性别
                    final String cate = record.get("Categoria"); // 类目
                    final String season = record.get("Collezione / Anno"); //上市季节
                    final String localPrice = record.get("Prezzo Et (€)"); // 当地价格(欧元)
                    final String supplierPrice = record.get("Price to WS (€)"); // 供应商价格
                    final String sizeStr = record.get("Taglie"); // 尺码(库存)
                    final String picsStr = record.get("Foto"); // 图片
                    String[] pics;
                    List<String> picsList = null;
                    if (picsStr != null && !"".equals(picsStr)) {
                        pics = picsStr.split(";");
                        picsList = Arrays.asList(pics);
                    }

                    Matcher m = pss.matcher(sizeStr);
                    String size = null, stock = null;
                    while (m.find()) {
                        if (m.groupCount() > 0) {
                            size = m.group(1);
                        }
                        if (m.groupCount() > 1) {
                            stock = m.group(2);
                        }
                    }

//                    System.out.println("supplierId : " + supplierId);
//                    System.out.println("skuId : " + skuId);
//                    System.out.println("spuId : " + spuId);
//                    System.out.println("productCode : " + productCode);
//                    System.out.println("brand : " + brand);
//                    System.out.println("name : " + name);
//                    System.out.println("color : " + color);
//                    System.out.println("desc : " + desc);
//                    System.out.println("material : " + material);
//                    System.out.println("sex : " + sex);
//                    System.out.println("cate : " + cate);
//                    System.out.println("season : " + season);
//                    System.out.println("localPrice : " + localPrice);
//                    System.out.println("supplierPrice : " + supplierPrice);
//                    System.out.println("size : " + size);
//                    System.out.println("stock : " + stock);
//                    System.out.println("picsList : " + picsList);

                    //SKU
                    SkuDTO sku = new SkuDTO();
                    sku.setProductName(name);
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSpuId(spuId);
                    sku.setSupplierId(supplierId);
                    sku.setSkuId(skuId);
                    sku.setProductSize(size);
                    sku.setMarketPrice(localPrice);
                    sku.setSupplierPrice(supplierPrice);
                    sku.setProductCode(productCode);
                    sku.setColor(color);
                    sku.setProductDescription(desc);
                    sku.setSaleCurrency("EUR");
                    sku.setStock(stock);
//                    sku.setMemo(picsStr); //临时保存图片

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
                    if (picsList != null) {
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
                    }

                    //SPU
                    SpuDTO spu = new SpuDTO();
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(spuId);
                    spu.setBrandName(brand);
                    spu.setCategoryName(cate);
                    spu.setCategoryGender(sex);
                    spu.setSpuName(name);
                    spu.setSeasonId(season);
                    spu.setMaterial(material);
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
