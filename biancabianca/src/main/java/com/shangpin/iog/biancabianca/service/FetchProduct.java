package com.shangpin.iog.biancabianca.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.biancabianca.dto.Product;
import com.shangpin.iog.biancabianca.util.MyTxtUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
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
@Component("biancabianca")
public class FetchProduct {
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
            MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //read .csv file
        List<Product> list = null;
        try {
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mapping and save into db
        messMappingAndSave(list);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(List<Product> list) {
        for(Product product:list) {
            SkuDTO sku = new SkuDTO();
            String spuId = product.getMASTER_SKU().substring(0, 10);
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSpuId(spuId);
                sku.setSkuId(product.getMASTER_SKU());
                sku.setProductSize(product.getSIZE());
                sku.setMarketPrice(product.getPRICE());
                sku.setSalePrice(product.getSALEPRICE());
                sku.setSupplierPrice(product.getPRICE());
                sku.setColor(product.getCOLOR());
                sku.setProductDescription(product.getDESCRIPTION());
                sku.setStock(product.getSTOCK_LEVEL());
                sku.setProductCode(product.getMASTER_SKU());
                sku.setSaleCurrency(product.getMASTER_SKU());
                sku.setBarcode(product.getMASTER_SKU());
                productFetchService.saveSKU(sku);

                /////////////////////////////////////////////////////
                for(int i = 0;i<2;i++){
                    ProductPictureDTO dto  = new ProductPictureDTO();
                    String picUrl = product.getBUYURL();
                    if (i == 1) picUrl = product.getIMAGEURL();
                    dto.setPicUrl(picUrl);
                    dto.setSupplierId(supplierId);
                    dto.setId(UUIDGenerator.getUUID());
                    dto.setSkuId(product.getMASTER_SKU());
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
                spu.setSpuName(product.getNAME());
                spu.setBrandName("Stuart Weitzman");
                spu.setCategoryName("Shoes");
                //spu.setSeasonId(skuDto.getSKU());
                spu.setMaterial(product.getMATERIAL());
                spu.setCategoryGender(product.getGENDER());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
}
