package com.shangpin.iog.levelgroup.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.levelgroup.dto.Product2;
import com.shangpin.iog.levelgroup.util.MyTxtUtil;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/11/13.
 */
@Component("levelgroup2")
public class FetchProduct2 {
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
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
            MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            loggerError.error("拉取数据失败！");
            e.printStackTrace();
        }
        //read .csv file
        List<Product2> list = null;
        try {
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            loggerError.error("解析文件失败！");
            e.printStackTrace();
        }
        logger.info("解析数据条数："+list.size());
        messMappingAndSave(list);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(List<Product2> list) {
        for(Product2 product:list) {
            ///////////////////////////////保存SKU//////////////////////////////////
            SkuDTO sku = new SkuDTO();
            String spuId = product.getVARIANT_SKU().substring(0, 10);
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSpuId(spuId);
                sku.setSkuId(product.getVARIANT_SKU());
                sku.setProductSize(product.getSIZE());
                sku.setMarketPrice(product.getPRICE());
                sku.setSalePrice(product.getSALEPRICE());
                sku.setSupplierPrice(product.getPRICE());
                sku.setColor(product.getCOLOR());
                sku.setProductDescription(product.getDESCRIPTION());
                sku.setStock(product.getSTOCK_LEVEL());
                sku.setProductCode("");
                sku.setSaleCurrency("euro");
                sku.setBarcode("");
                productFetchService.saveSKU(sku);

            } catch (ServiceException e) {
                try {
                    if (e.getMessage().equals("数据插入失败键重复")) {
                        System.out.println("saveSKU数据插入失败键重复");
                        //更新价格和库存
                        productFetchService.updatePriceAndStock(sku);
                    } else {
                        System.out.println("saveSKU异常");
                        e.printStackTrace();
                    }
                } catch (ServiceException e1) {
                    System.out.println("updatePriceAndStock异常");
                    e1.printStackTrace();
                }
            }

            ///////////////////////////////////保存图片///////////////////////////////////
            for(int i = 0;i<2;i++){
                ProductPictureDTO dto  = new ProductPictureDTO();
                String picUrl = product.getBUYURL();
                if (i == 1) picUrl = product.getIMAGEURL();
                dto.setPicUrl(picUrl);
                dto.setSupplierId(supplierId);
                dto.setId(UUIDGenerator.getUUID());
                dto.setSkuId(product.getVARIANT_SKU());
                try {
                    productFetchService.savePictureForMongo(dto);
                } catch (ServiceException e) {
                    System.out.println("savePictureForMongo异常");
                    e.printStackTrace();
                }
            }
            ///////////////////////////////////保存SPU/////////////////////////////////

            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(spuId);
                spu.setSpuName(product.getNAME());
                spu.setBrandName("Stuart Weitzman");
                spu.setCategoryName("Shoes");
                //spu.setSeasonId(skuDto.getSKU());
                spu.setMaterial(product.getMATERIAL());
                spu.setCategoryGender(product.getGENDER());
                spu.setProductOrigin("Spain");
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                System.out.println("saveSPU异常");
                e.printStackTrace();
            }
        }
    }
}
