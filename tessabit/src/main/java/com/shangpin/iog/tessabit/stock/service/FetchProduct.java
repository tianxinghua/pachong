package com.shangpin.iog.tessabit.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.tessabit.stock.common.Constant;
import com.shangpin.iog.tessabit.stock.common.FtpUtil;
import com.shangpin.iog.tessabit.stock.dto.Item;
import com.shangpin.iog.tessabit.stock.dto.Items;
import com.shangpin.iog.tessabit.stock.dto.Product;
import com.shangpin.iog.tessabit.stock.dto.Products;
import org.apache.commons.lang.StringUtils;
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
@Component("tessabit")
public class FetchProduct {

    final Logger logger = Logger.getLogger("info");
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //拉取FTP文件
        logger.info("downLoad ftpFile begin......");
        FtpUtil.downLoad();
        logger.info("downLoad ftpFile end......");

        //入库处理
        logger.info("save products into DB begin......");
        Products products = null;
        try {
            // 将FTP拉取到的xml文件转换成模型数据
            products = ObjectXMLUtil.xml2Obj(Products.class, new File(Constant.LOCAL_FILE));
            System.out.println(products.getProducts().size());
        } catch(  JAXBException e  )  {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //映射数据并保存
        messMappingAndSave(products);
        logger.info("save products into DB end......");

    }

    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Products products) {
        List<Product> productList = products.getProducts();
        for(Product product:productList){
            SpuDTO spu = new SpuDTO();

            Items items = product.getItems();
            if(null==items){//判断SKU
                continue;
            }

            List<Item> itemList = items.getItems();
            if(null==itemList) continue;
            String skuId = "";
            for(Item item:itemList){
                SkuDTO sku  = new SkuDTO();
                try {
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(Constant.SUPPLIER_ID);

                    sku.setSpuId(product.getProductId());
                    skuId = item.getItem_id();
                    if(skuId.indexOf("?")>0){
                        skuId = skuId.replace("?","+");
                    }
                    sku.setSkuId(skuId);
                    sku.setProductSize(item.getItem_size());
                    sku.setMarketPrice(item.getMarket_price());
                    sku.setSalePrice(item.getSell_price());
                    sku.setSupplierPrice(item.getSupply_price());
                    sku.setColor(item.getColor());
                    sku.setProductDescription(item.getDescription());
                    sku.setStock(item.getStock());
                    sku.setBarcode(item.getBarcode());
                    sku.setProductCode(product.getProducer_id());
                    productFetchService.saveSKU(sku);

                    if(StringUtils.isNotBlank(item.getPicture())){
                        String[] picArray = item.getPicture().split("\\|");

//                            List<String> picUrlList = Arrays.asList(picArray);
                        for(String picUrl :picArray){
                            ProductPictureDTO dto  = new ProductPictureDTO();
                            dto.setPicUrl(picUrl);
                            dto.setSupplierId(Constant.SUPPLIER_ID);
                            dto.setId(UUIDGenerator.getUUID());
                            dto.setSkuId(item.getItem_id());
                            try {
//                                    productFetchService.savePicture(dto);
                                productFetchService.savePictureForMongo(dto);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
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
            }

            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(product.getProductId());
                spu.setBrandName(product.getProduct_brand());
                spu.setCategoryName(product.getCategory());
                spu.setSpuName(product.getProduct_name());
                spu.setSeasonId(product.getSeason_code());
                spu.setMaterial(product.getProduct_material());
                spu.setCategoryGender(product.getGender());
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

