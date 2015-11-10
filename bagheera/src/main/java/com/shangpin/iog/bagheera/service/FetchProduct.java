package com.shangpin.iog.bagheera.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.bagheera.dto.BagheeraDTO;
import com.shangpin.iog.bagheera.utils.DownloadAndReadExcel;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/9/8.
 */
@Component("bagheera")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
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
    public void fetchProductAndSave(){
    	Map<String, String> mongMap = new HashMap<>();

        OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
//		String result = HttpUtil45.get(url, timeConfig, null);
		mongMap.put("supplierId", supplierId);
		mongMap.put("supplierName", "bagheera");
//		mongMap.put("result", result);
		logMongo.info(mongMap);
        try {
            List<BagheeraDTO> list=DownloadAndReadExcel.readLocalExcel();
            for (BagheeraDTO dto:list){
                SpuDTO spu = new SpuDTO();
                SkuDTO sku = new SkuDTO();
                ProductPictureDTO picture = new ProductPictureDTO();
                String size = dto.getSIZE();
                if(size.indexOf("½")>0){
                    size=size.replace("½","+");
                }
                sku.setId(UUIDGenerator.getUUID());
                sku.setProductCode(dto.getCODE().substring(0,dto.getCODE().length()-2));
                sku.setSupplierId(supplierId);
                sku.setSkuId(dto.getSUPPLIER_CODE()+"-"+size);
                sku.setSpuId(dto.getSUPPLIER_CODE());
                sku.setProductDescription(dto.getDESCRIPTION());
                sku.setColor(dto.getCOLOR());
                sku.setProductSize(size);
                sku.setSupplierPrice(dto.getLASO_Price());
                sku.setSaleCurrency(dto.getCURRENCY());
                sku.setMarketPrice(dto.getRETAIL_PRICE());
                sku.setStock(dto.getSTOCK());
                sku.setProductName(dto.getITEM_GROUP());
                
                spu.setCategoryName(dto.getCATEGORY());
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(dto.getSUPPLIER_CODE());
                spu.setSupplierId(supplierId);
                spu.setMaterial(dto.getMATERIAL());
                spu.setProductOrigin(dto.getMADE());
                spu.setBrandName(dto.getITEM_GROUP());
                spu.setSeasonName(dto.getCOLLECTION());
                spu.setCategoryGender(dto.getDEPT().substring(0, dto.getDEPT().indexOf(" ")));
                picture.setSupplierId(supplierId);
                picture.setId(UUIDGenerator.getUUID());
                picture.setSkuId(dto.getSUPPLIER_CODE()+"-"+dto.getSIZE());
                picture.setPicUrl(dto.getIMAGE_URL());
                try {
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                try {
                    productFetchService.saveSKU(sku);
                    productFetchService.savePictureForMongo(picture);
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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public static void main(String[] args) {
        Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        String date=matter1.format(dt).replaceAll("-","").trim();
        System.out.println(date);
        *//*System.out.println(System.currentTimeMillis());*//*
    }*/
}
