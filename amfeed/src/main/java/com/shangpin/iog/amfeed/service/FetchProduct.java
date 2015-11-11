package com.shangpin.iog.amfeed.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.amfeed.common.MyCsvUtil;
import com.shangpin.iog.amfeed.dto.Product;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.shangpin.iog.service.ProductFetchService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/11/09.
 */
@Component("amfeed")
public class FetchProduct {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String localPath;

    @Autowired
    private ProductFetchService productFetchService;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
            localPath = bdl.getString("path");
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
        List<Product> list = null;
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
    private void messMappingAndSave(List<Product> list) {
        for(Product product:list){
            SpuDTO spu = new SpuDTO();
            String spuId = product.getSku().split("-")[0];
            SkuDTO sku  = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSpuId(spuId);
                sku.setSkuId(product.getSku());
                sku.setProductSize(product.getSize());
                sku.setMarketPrice(product.getPrice());
                sku.setSalePrice(product.getPrice());
                sku.setSupplierPrice(product.getPrice());
                sku.setColor(product.getColor());
                sku.setProductDescription(product.getDescription());
                sku.setStock(product.getQty());
                //sku.setProductCode(product.getBrand());
                productFetchService.saveSKU(sku);
                //
                for(int i = 1;i<5;i++){
                    ProductPictureDTO dto  = new ProductPictureDTO();
                    String picUrl = product.getImage1();
                    if (i == 2) picUrl = product.getImage2();
                    else if (i == 3) picUrl = product.getImage3();
                    else if (i == 4) picUrl = product.getImage4();
                    dto.setPicUrl(picUrl);
                    dto.setSupplierId(supplierId);
                    dto.setId(UUIDGenerator.getUUID());
                    dto.setSkuId(product.getSku());
                    try {
                        productFetchService.savePictureForMongo(dto);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }

            } catch (ServiceException e) {
                try {
                    if(e.getMessage().equals("数据插入失败键重复")){
                        //更新价格和库存
                        productFetchService.updatePriceAndStock(sku);
                    } else{
                        e.printStackTrace();
                    }

                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            }

            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(spuId);
                spu.setBrandName(product.getBrand());
                spu.setCategoryName(product.getCategrory());
                //spu.setSpuName(spuId);
                //spu.setSeasonId(spuId);
                spu.setMaterial(product.getMaterials());
                //spu.setCategoryGender(spuId);
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
}
