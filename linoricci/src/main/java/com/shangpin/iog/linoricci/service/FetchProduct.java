package com.shangpin.iog.linoricci.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.linoricci.common.Constant;
import com.shangpin.iog.linoricci.common.MyFtpClient;
import com.shangpin.iog.linoricci.dto.*;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("linoricci")
public class FetchProduct {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private Disponibilitas disponibilits = null;
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        logger.info("downLoad ftpFile begin......");
        boolean isOk = new MyFtpClient().downLoad();
        logger.info("downLoad ftpFile end......result:"+isOk);
        if (!isOk) return;
        Prodottis prodottis = null;
        Riferimentis riferimentis = null;
        try {
            prodottis = ObjectXMLUtil.xml2Obj(Prodottis.class, new File(Constant.LOCAL_ITEMS_FILE));
            riferimentis = ObjectXMLUtil.xml2Obj(Riferimentis.class, new File(Constant.LOCAL_IMAGE_FILE));
            disponibilits = ObjectXMLUtil.xml2Obj(Disponibilitas.class, new File(Constant.LOCAL_STOCK_FILE));
        } catch (JAXBException e) {
            loggerError.error("JAXBException:"+e.getMessage());
        } catch (FileNotFoundException e) {
            loggerError.error("FileNotFoundException:" + e.getMessage());
        }
        System.out.println(prodottis.getProdottiList().size());
        logger.info("save spu and sku into DB,Data quantity is " + prodottis.getProdottiList().size());
        messMappingAndSave(prodottis);
        logger.info("save pictrue into DB,pictrue quantity is " + riferimentis.getRiferimentiList().size());
        picMappingAndSave(riferimentis);

    }

    /**
     * save items into DB
     * **/
    private void picMappingAndSave(Riferimentis riferimentis) {

        for (Riferimenti riferimenti: riferimentis.getRiferimentiList()) {
            ProductPictureDTO dto = new ProductPictureDTO();
            dto.setPicUrl(riferimenti.getRIFERIMENTO());
            dto.setSupplierId(Constant.SUPPLIER_ID);
            dto.setId(UUIDGenerator.getUUID());
            dto.setSpuId(riferimenti.getRF_RECORD_ID());
            try {
                productFetchService.savePictureForMongo(dto);
            } catch (ServiceException e) {
                loggerError.error("ServiceException:" + e.getMessage());
            }
        }

    }
    /**
     * save items into DB
     * **/
    private void messMappingAndSave(Prodottis prodottis) {
        List<Disponibilita> dList = disponibilits.getDisponibilitaList();
        for (Prodotti prodotti : prodottis.getProdottiList()) {
            SkuDTO sku = new SkuDTO();
            try {
                sku.setSupplierId(Constant.SUPPLIER_ID);
                sku.setSpuId(prodotti.getID_ARTICOLO());
                sku.setMarketPrice(prodotti.getPREZZO_VENDITA());
                sku.setSalePrice(prodotti.getPREZZO_VENDITA());
                sku.setSupplierPrice(prodotti.getPREZZO_VENDITA());
                sku.setColor(prodotti.getCOLORE());
                sku.setProductDescription(prodotti.getDESCRIZIONE());
                sku.setProductCode(prodotti.getCODICE_MODELLO());//code
                sku.setProductName(prodotti.getDESCRIZIONE_MODELLO());
                for (Disponibilita disponibilita : dList){
                    if (sku.getSpuId().equals(disponibilita.getID_ARTICOLO())){
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSkuId(new StringBuffer().append(disponibilita.getMM_TAGLIA()).append("Y")
                                .append(disponibilita.getID_ARTICOLO()).append("Y").append(disponibilita.getNE_SIGLA()).toString());
                        sku.setBarcode(disponibilita.getBARCODEEAN());
                        sku.setStock(disponibilita.getESI());
                        sku.setProductSize(disponibilita.getMM_TAGLIA());//size
                        productFetchService.saveSKU(sku);
                    }
                }
            } catch (ServiceException e) {
                try {
                    if (e.getMessage().equals("数据插入失败键重复")) {
                        //更新价格和库存
                        productFetchService.updatePriceAndStock(sku);
                    } else {
                        loggerError.error("ServiceException:" + e.getMessage());
                    }
                } catch (ServiceException e1) {
                    loggerError.error("ServiceException:" + e.getMessage());
                }
            }

            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(prodotti.getID_ARTICOLO());
                spu.setBrandName(prodotti.getBRAND());
                spu.setCategoryName(prodotti.getCATEGORIA());
                spu.setSpuName(prodotti.getDESCRIZIONE_MODELLO());
                spu.setSeasonId(prodotti.getSIGLA_STAGIONE());
                spu.setMaterial(prodotti.getDESCRIZIONE_BREVE());
                spu.setCategoryGender(prodotti.getSETTORE());
                spu.setProductOrigin(prodotti.getPAESE_PRODUZIONE());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                loggerError.error("ServiceException:" + e.getMessage());
            }
        }
    }
}

