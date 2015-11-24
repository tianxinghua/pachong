package com.shangpin.iog.levelgroup.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.levelgroup.dto.*;
import com.shangpin.iog.levelgroup.util.MyCsvUtil;
import com.shangpin.iog.service.ProductFetchService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/11/13.
 */
@Component("levelgroup2")
public class FetchProduct2 {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static String supplierId;
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
    }
    /**
     * 主处理
     */
    public void fetchProductAndSave(){
        //download
        try {
            MyCsvUtil.csvDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //read .csv file
        List<SKUDto> list = null;
        try {
            list = MyCsvUtil.readCSVFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mapping and save into db
        messMappingAndSave(list);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(List<SKUDto> list) {
        for(SKUDto skuDto:list) {
            SkuDTO sku = new SkuDTO();
            String spuId = skuDto.getSKU().substring(0,10);
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSpuId(spuId);
                sku.setSkuId(skuDto.getSKU());
                sku.setProductSize(skuDto.getFORMAT());
                sku.setMarketPrice(skuDto.getPRICE());
                sku.setSalePrice(skuDto.getSALEPRICE());
                sku.setSupplierPrice(skuDto.getPRICE());
                sku.setColor(skuDto.getLABEL());
                sku.setProductDescription(skuDto.getDESCRIPTION());
                sku.setStock("0");//TODO
                sku.setProductCode(skuDto.getSKU());
                sku.setSaleCurrency(skuDto.getCURRENCY());
                sku.setBarcode(skuDto.getUPC());
                productFetchService.saveSKU(sku);

                /////////////////////////////////////////////////////
                for(int i = 0;i<2;i++){
                    ProductPictureDTO dto  = new ProductPictureDTO();
                    String picUrl = skuDto.getBUYURL();
                    if (i == 1) picUrl = skuDto.getIMAGEURL();
                    dto.setPicUrl(picUrl);
                    dto.setSupplierId(supplierId);
                    dto.setId(UUIDGenerator.getUUID());
                    dto.setSkuId(skuDto.getSKU());
                    try {
                        productFetchService.savePictureForMongo(dto);
                    } catch (ServiceException e) {
                        e.printStackTrace();
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
            ////////////////////////////////////////////////////////////////////

            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(spuId);
                spu.setSpuName(skuDto.getNAME());
                spu.setBrandName("Stuart Weitzman");
                spu.setCategoryName("Shoes");
                //spu.setSeasonId(skuDto.getSKU());
                spu.setMaterial(skuDto.getMATERIAL());
                spu.setCategoryGender(skuDto.getGENDER());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
}
