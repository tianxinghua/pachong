package com.shangpin.iog.cirillomoda.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
      private static Logger logger = Logger.getLogger("info");
      private static Logger error = Logger.getLogger("error");
//    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    private static ResourceBundle bdl=null;
    private static String supplierId; //测试
    private static int day;
    private static String path = null;
//    private static String supplierId = ""; //正式

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        path = bdl.getString("path");
    }

    public void fetchProductAndSave(final String url) {

        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*20, 1000*60*20,1000*60*20);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = "";
            if(StringUtils.isNotBlank(path)){
				File file = new File(path);
				BufferedReader reader = null;
				try {
					
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;					
					// 一次读入一行，直到读入null为文件结束
					while ((tempString = reader.readLine()) != null) {
						 result += tempString;
						 result += "\n";
					}
//					result = result.replaceAll("<br />\n", "").replaceAll("<br />\r", ""); 
//					System.out.println(result);
//					reader.close();
					logger.info(result); 
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e1) {
						}
					}
				}
            }else{
            	result = HttpUtil45.get(url, timeConfig, null);
            	HttpUtil45.closePool();
            }        

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.") ;
//            logMongo.info(mongMap);

            
            Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
            Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

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
            String origin = "";
            String season = "";
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
                        
//                        String Prezzo_scontato = record.get("Prezzo scontato");
                        
                        category = record.get("Categoria");
                        description = record.get("description_it_it");
//                        String description_en_gb = record.get("description_en_gb");
//                        String description_en_us = record.get("description_en_us");
//                        String description_de_de = record.get("description_de_de");
                        status = record.get("Stato");
                        try{
                        	origin = record.get("ORIGIN");
                            season = record.get("SEASON");                            
                        }catch(Exception e){
                        	
                        } 
                        //保存SPU
                        SpuDTO spu = new SpuDTO();
                        //SPU 必填
                        spu.setId(spuId);
                        spu.setSpuId(spuId);
                        spu.setSupplierId(supplierId);
                        spu.setCategoryName(category);
                        spu.setBrandName(brand);
                        String material = "";
                        description = description.replaceAll("\n", "").replaceAll("\r", "");
                        String[] des = description.split("<br />");                        
                        for(String str:des){
                        	if(StringUtils.isNotBlank(str) && str.contains("%")){
                        		material = str;
                        		break;
                        	}
                        }
                        spu.setMaterial(material);
//                        spu.setProductOrigin(description);
                        spu.setProductOrigin(origin);
                        spu.setSeasonId(season); 

                        spu.setCategoryGender(category);

                        //SPU 选填
                        spu.setSpuName(title);

                        try {
                            productFetchService.saveSPU(spu);
                        } catch (ServiceException e) {
                        	try {
                        		logger.info(spu.getSpuId() +"  "+spu.getSeasonId()+"  "+spu.getSeasonName()+"  "+spu.getProductOrigin());
        						productFetchService.updateMaterial(spu);
        					} catch (ServiceException e1) {
        						e1.printStackTrace();
        						error.error(e1); 
        					}
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

                        String skuId = spuId + "-" + size;
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
        }

    }

}
