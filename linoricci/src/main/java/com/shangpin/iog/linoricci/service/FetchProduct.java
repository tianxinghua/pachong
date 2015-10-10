package com.shangpin.iog.linoricci.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.linoricci.common.Constant;
import com.shangpin.iog.linoricci.common.MyFtpClient;
import com.shangpin.iog.linoricci.common.MyStringUtil;
import com.shangpin.iog.linoricci.dto.Prodotti;
import com.shangpin.iog.linoricci.dto.Prodottis;
import com.shangpin.iog.linoricci.dto.Riferimenti;
import com.shangpin.iog.linoricci.dto.Riferimentis;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("linoricci")
public class FetchProduct {

    final Logger logger = Logger.getLogger("info");
    MyStringUtil util = new MyStringUtil();
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //拉取FTP文件
        logger.info("downLoad ftpFile begin......");
        //new MyFtpClient().downLoad();
        logger.info("downLoad ftpFile end......");
        Prodottis prodottis = null;
        Riferimentis riferimentis = null;
        try {
            prodottis = ObjectXMLUtil.xml2Obj(Prodottis.class, new File(Constant.LOCAL_ITEMS_FILE));
            riferimentis = ObjectXMLUtil.xml2Obj(Riferimentis.class, new File(Constant.LOCAL_IMAGE_FILE));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(prodottis.getProdottiList().size());
        //get items string
        //String items = util.parseXml2Str(Constant.LOCAL_ITEMS_FILE);
        //save items into DB
        messMappingAndSave(prodottis);
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
            dto.setSkuId(riferimenti.getRF_RECORD_ID());
            try {
                productFetchService.savePictureForMongo(dto);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * save items into DB
     * **/
    private void messMappingAndSave(Prodottis prodottis) {
        String stocks = util.getStockByFile(Constant.LOCAL_STOCK_FILE);
        String pictrues = util.parseXml2Str(Constant.LOCAL_IMAGE_FILE);
        String skuId = "";
        // System.out.println(pictrues);

/*        for (String item : items) {
            String[] fields = item.split(";");
            System.out.println();
            for (int i = 0; i < fields.length; i++) {
                System.out.print("; fields[" + i + "]=" + fields[i]);
            }
        }*/

        //items = new String[0];

        for (Prodotti prodotti : prodottis.getProdottiList()) {
            skuId = prodotti.getID_ARTICOLO();
            SkuDTO sku = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(Constant.SUPPLIER_ID);
                sku.setSpuId(prodotti.getID_ARTICOLO());
                sku.setSkuId(skuId);
                sku.setProductSize(prodotti.getSIZE_AND_FIT());
                sku.setMarketPrice(prodotti.getPREZZO_VENDITA());
                sku.setSalePrice(prodotti.getPREZZO_VENDITA());
                sku.setSupplierPrice(prodotti.getPREZZO_VENDITA());
                sku.setColor(prodotti.getCOLORE());
                sku.setProductDescription(prodotti.getDESCRIZIONE());
                sku.setStock(MyStringUtil.getStockBySkuId(skuId,stocks));
                sku.setBarcode(prodotti.getID_ARTICOLO());
                sku.setProductCode(prodotti.getCODICE_MODELLO());
                sku.setProductName(prodotti.getDESCRIZIONE_MODELLO());
                productFetchService.saveSKU(sku);

                String skuPic = null;
                if (pictrues.contains(prodotti.getID_ARTICOLO())) {
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

            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(prodotti.getCODICE_MODELLO());
                spu.setBrandName(prodotti.getBRAND());
                spu.setCategoryName(prodotti.getSETTORE());
                //spu.setSpuName(fields[0]);
                spu.setSeasonId(prodotti.getSIGLA_STAGIONE());
                spu.setMaterial(prodotti.getCOMPOSIZIONE_DETTAGLIATA());
                spu.setCategoryGender(prodotti.getSETTORE());
                spu.setProductOrigin(prodotti.getPAESE_PRODUZIONE());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }

}

