package com.shangpin.iog.facade.dubbo.service;


import com.shangpin.framework.*;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.facade.dubbo.dto.*;
import com.shangpin.iog.facade.dubbo.dto.ServiceException;
import com.shangpin.iog.facade.dubbo.dto.ServiceMessageException;
import com.shangpin.iog.product.service.ProductFetchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/16.
 */
@Service("productionFacadeServiceImpl")
public class ProductionServiceImpl implements  ProductionService {


    private static org.apache.log4j.Logger logMongo = org.apache.log4j.Logger.getLogger("mongodb");
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");


    @Autowired
    ProductFetchService productFetchService;

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public Boolean saveProduct(ProductDTO productDTO) throws ServiceException {

        logger.info(" transfer object message = " + productDTO.toString());

        //验证产品信息
        filter(productDTO);

        //保存到mongodb

        Map<String,String> mongMap = new HashMap<>();

        mongMap.put("supplierId",productDTO.getSupplierId());
        mongMap.put("supplierName","acanfora");
        mongMap.put("result",productDTO.toString()) ;
        try {
            logMongo.info(mongMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpuDTO spuDTO = new SpuDTO();
        SkuDTO skuDTO = new SkuDTO();
        List<ProductPictureDTO> pictureDTOList = new ArrayList<>();

        try {
            spuDTO.setSpuId(productDTO.getSpuId());
            spuDTO.setMaterial(productDTO.getMaterial());

            spuDTO.setBrandName(productDTO.getBrandName());
            spuDTO.setCategoryName(productDTO.getCategoryName());
            spuDTO.setSubCategoryName(productDTO.getSubCategoryName());
            spuDTO.setCategoryGender(productDTO.getCategoryGender());

            spuDTO.setId(UUIDGenerator.getUUID());
            spuDTO.setProductOrigin(productDTO.getProductOrigin());
            spuDTO.setSeasonName(productDTO.getSeasonName());

            spuDTO.setSupplierId(productDTO.getSupplierId());


            productFetchService.saveSPU(spuDTO);

        } catch (Exception e) {

            if(ProductFetchServiceImpl.REPEAT_MESSAGE.equals(e.getMessage())){
              //重复插入不做处理
                loggerError.error("spu :" + spuDTO.getSpuId() + " 重复保存" );
            }else{
                loggerError.error("spu :" + spuDTO.getSpuId() + " 保存失败。失败原因：" + e.getMessage() );
                e.printStackTrace();
                throw new ServiceMessageException("save spu failed. please contact IT" );
            }

        }
        skuDTO.setId(UUIDGenerator.getUUID());
        skuDTO.setBarcode(productDTO.getBarcode());
        skuDTO.setProductCode(productDTO.getProductCode());
        skuDTO.setSupplierId(productDTO.getSupplierId());
        skuDTO.setSpuId(productDTO.getSpuId());
        skuDTO.setSkuId(productDTO.getSkuId());
        skuDTO.setColor(productDTO.getColor());
        skuDTO.setMarketPrice(productDTO.getMarketPrice().toString());
        BigDecimal salePrice = productDTO.getSalePrice();
        if(StringUtils.isNotBlank(String.valueOf(salePrice))) {
            skuDTO.setSalePrice(String.valueOf(salePrice));
        }
        skuDTO.setSupplierPrice(productDTO.getSupplierPrice().toString());
        skuDTO.setProductDescription(productDTO.getProductDescription());
        skuDTO.setProductName(productDTO.getProductName());
        skuDTO.setProductSize(productDTO.getSize());
        skuDTO.setStock(String.valueOf(productDTO.getStock()));
        skuDTO.setSaleCurrency(productDTO.getSaleCurrency());
        try {
            productFetchService.saveSKU(skuDTO);


        } catch (Exception e) {
            loggerError.error("sku:" + skuDTO.getSkuId() + " 保存失败。失败原因: " + e.getMessage());
            if(ProductFetchServiceImpl.REPEAT_MESSAGE.equals(e.getMessage())){
                throw new ServiceMessageException("repeat save sku." );
            }else{
                e.printStackTrace();
                throw new ServiceMessageException("save sku failed. please contact IT" );
            }


        }

        String imgUrl =productDTO.getSpuPicture();
        String[] imageUrlArray = imgUrl.split("\\|\\|");

        for(String  imageUrl:imageUrlArray){
            ProductPictureDTO pictureDTO = new ProductPictureDTO();
            pictureDTO.setSupplierId(productDTO.getSupplierId());
            pictureDTO.setPicUrl(imageUrl);
            pictureDTO.setSpuId(productDTO.getSpuId());
            try {
                productFetchService.savePictureForMongo(pictureDTO);
            } catch (Exception e) {
                loggerError.error("spu : " +  productDTO.getSpuId() + " 图片保存失败。失败原因: " + e.getMessage());
                e.printStackTrace();
                throw new ServiceMessageException("save spu(common) picture failed. please contact IT" );
            }
        }


        String skuImgUrl =productDTO.getSkuPicture();
        String[] skuImageUrlArray = skuImgUrl.split("\\|\\|");
        for(String  imageUrl:skuImageUrlArray){
            ProductPictureDTO pictureDTO = new ProductPictureDTO();
            pictureDTO.setSupplierId(productDTO.getSupplierId());
            pictureDTO.setPicUrl(imageUrl);
            pictureDTO.setSkuId(productDTO.getSkuId());
            try {
                productFetchService.savePictureForMongo(pictureDTO);
            } catch (Exception e) {
                loggerError.error("sku :" + productDTO.getSkuId() + " 图片保存失败。失败原因: " + e.getMessage());
                e.printStackTrace();
                throw new ServiceMessageException("save sku  picture failed. please contact IT" );
            }
        }


        return true;
    }

    /**
     * 验证产品信息
     * @param productDTO
     * @throws ServiceException
     */
    private void filter(ProductDTO productDTO) throws ServiceException{


        if(null==productDTO) throw new ServiceMessageException("transfer data is null,can't save");

        if(StringUtils.isBlank(productDTO.getSupplierId())){
            throw  new ServiceMessageException("supplierId not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getBrandName())){
            throw  new ServiceMessageException("brandName not allow zero length");
        }
        if(StringUtils.isBlank(productDTO.getCategoryName())){
            throw new ServiceMessageException("categoryName not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSpuId())){
            throw new ServiceMessageException("spuId not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSkuId())){
            throw new ServiceMessageException("skuId not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getColor())){
            throw new ServiceMessageException("color not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSize())){
            throw new ServiceMessageException("size not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSaleCurrency())){
            throw new ServiceMessageException("saleCurrency not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSaleCurrency())){
            throw new ServiceMessageException("saleCurrency not allow zero length");
        }



        if(StringUtils.isBlank(productDTO.getMaterial())){
            throw new ServiceMessageException("material not allow zero length");
        }

        if(StringUtils.isBlank(productDTO.getSpuPicture())&&StringUtils.isBlank(productDTO.getSkuPicture())){
            throw new ServiceMessageException("imageUrl not allow null");
        }

        if(null==productDTO.getSupplierPrice()){
            throw new ServiceMessageException("supplierPrice not allow null");
        }

        if(null==productDTO.getMarketPrice()){
            throw new ServiceMessageException("marketPrice not allow null");
        }



    }

    @Override
    public Boolean updateProduct(ProductUpdateDTO productUpdateDTO) throws ServiceException {
        return null;
    }

    public static void main(String[] args){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSupplierId("1234567890");
        productDTO.setSkuId("4107222");
        productDTO.setBarcode("1100219805");
        productDTO.setBrandName("Good Charma");
        productDTO.setCategoryGender("");
        productDTO.setColor("gold");
        productDTO.setCategoryName("Jewelry");
        productDTO.setMarketPrice(new BigDecimal("270.0"));
        productDTO.setSalePrice(new BigDecimal("99.0"));
        productDTO.setSupplierPrice(new BigDecimal("51.0"));
        productDTO.setMaterial("22K yellow gold-plated sterling silver and gold vermeil");
        productDTO.setProductCode("1000041072225");
        productDTO.setProductDescription("22K yellow gold-plated sterling silver pendant necklace with gold vermeil heart and hamsa pendant details<br>* Spring clasp closure<br><b><br>**Measurements:**</b> 18\" long, 0.5\" pendant drop");
        productDTO.setProductOrigin("USA");
        productDTO.setSaleCurrency("USD");
        productDTO.setProductName("Heart & Hamsa Pendant Necklace ");
        productDTO.setSeasonName("");
        productDTO.setSize("No size");
        productDTO.setSpuPicture("http;//www.shangpin.com/1.jpg||http://www.shangpincom/2.jpg");
        productDTO.setStock(10);
        productDTO.setSkuPicture("http;//www.shangpin.com/3.jpg");
    }
}
