package com.shangpin.iog.bagheera.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.bagheera.dto.BagheeraDTO;
import com.shangpin.iog.bagheera.utils.DownloadAndReadExcel;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by sunny on 2015/9/8.
 */
@Component("bagheera")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    /*DownloadAndReadExcel excelHelper ;*/
    @Autowired
    ProductFetchService productFetchService;
    String supplierId = "201509091712";
    public void fetchProductAndSave(){
        try {
            List<BagheeraDTO> list=DownloadAndReadExcel.readLocalExcel();
            for (BagheeraDTO dto:list){
                SpuDTO spu = new SpuDTO();
                SkuDTO sku = new SkuDTO();
                ProductPictureDTO picture = new ProductPictureDTO();
                sku.setId(UUIDGenerator.getUUID());
                sku.setProductCode(dto.getCODE());
                sku.setSupplierId(supplierId);
                sku.setSkuId(dto.getSUPPLIER_CODE()+"-"+dto.getSIZE());
                sku.setSpuId(dto.getSUPPLIER_CODE());
                sku.setProductDescription(dto.getDESCRIPTION());
                sku.setColor(dto.getCOLOR());
                sku.setProductSize(dto.getSIZE());
                sku.setSupplierPrice(dto.getLASO_Price());
                sku.setSaleCurrency(dto.getCURRENCY());
                sku.setMarketPrice(dto.getLIST_PRICE());
                sku.setStock(dto.getSTOCK());
                sku.setProductName(dto.getITEM_GROUP());
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(dto.getSUPPLIER_CODE());
                spu.setSupplierId(supplierId);
                spu.setMaterial(dto.getMATERIAL());
                spu.setProductOrigin(dto.getMADE());
                spu.setBrandName(dto.getITEM_GROUP());
                spu.setSeasonName(dto.getCOLLECTION());
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
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
