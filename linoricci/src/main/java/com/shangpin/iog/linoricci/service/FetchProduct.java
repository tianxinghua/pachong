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
import com.shangpin.iog.linoricci.dto.Prodottis;
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
        try {
            prodottis = ObjectXMLUtil.xml2Obj(Prodottis.class, new File("E:/linoricci/Prodotti_EN.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(prodottis.getProdottiList().size());
/*        //get items string
        String items = util.parseXml2Str(Constant.LOCAL_ITEMS_FILE);
        //save items into DB
        messMappingAndSave(items.split("\\n"));*/

    }

    /**
     * save items into DB
     * **/
    private void messMappingAndSave(String[] items) {
        String stocks = util.parseXml2Str(Constant.LOCAL_STOCK_FILE);
        String pictrues = util.parseXml2Str(Constant.LOCAL_IMAGE_FILE);
        // System.out.println(pictrues);

/*        for (String item : items) {
            String[] fields = item.split(";");
            System.out.println();
            for (int i = 0; i < fields.length; i++) {
                System.out.print("; fields[" + i + "]=" + fields[i]);
            }
        }*/

        //items = new String[0];
        for (String item : items) {
            String[] fields = item.split(";");
            String skuId = fields[0];

            SkuDTO sku = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(Constant.SUPPLIER_ID);
                sku.setSpuId(skuId);
                sku.setSkuId(skuId);
                sku.setProductSize(fields[4]);
                sku.setMarketPrice(fields[16]);
                sku.setSalePrice(fields[16]);
                sku.setSupplierPrice(fields[16]);
                sku.setColor(fields[10]);
                sku.setProductDescription(fields[15]);
                String stock = "0";
                String barCode = "";
                if (stocks.contains(skuId)) {
                    int index = stocks.indexOf(skuId);
                    stock = MyStringUtil.getStockBySkuId(stocks.substring(index, index + 20));
                    barCode = MyStringUtil.getBarcodeBySkuId(stocks.substring(index, index + 50));
                }
                sku.setStock(stock);
                sku.setBarcode(barCode);
                sku.setProductCode(fields[0]);
                sku.setProductName(fields[15]);
                productFetchService.saveSKU(sku);

                String skuPic = null;
                if (pictrues.contains(skuId)) {
                    skuPic = pictrues.substring(pictrues.indexOf(skuId), pictrues.lastIndexOf(skuId) + 16);
                }
                if (StringUtils.isNotBlank(skuPic)) {
                    String[] picArray = MyStringUtil.getPicUrl(skuId, skuPic);
//                            List<String> picUrlList = Arrays.asList(picArray);
                    for (int i = 1; i < picArray.length; i++) {
                        ProductPictureDTO dto = new ProductPictureDTO();
                        dto.setPicUrl(picArray[i].split(";")[0]);
                        dto.setSupplierId(Constant.SUPPLIER_ID);
                        dto.setId(UUIDGenerator.getUUID());
                        dto.setSkuId(skuId);
                        try {
                            productFetchService.savePictureForMongo(dto);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
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

            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(skuId);
                spu.setBrandName(fields[2]);
                spu.setCategoryName(fields[13]);
                //spu.setSpuName(fields[0]);
                spu.setSeasonId(fields[6]);
                spu.setMaterial(fields[11]);
                spu.setCategoryGender(fields[5]);
                spu.setProductOrigin(fields[13]);
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

