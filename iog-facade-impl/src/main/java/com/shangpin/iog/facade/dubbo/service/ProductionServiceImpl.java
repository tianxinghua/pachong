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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/16.
 */
@Service("productionFacadeServiceImpl")
public class ProductionServiceImpl implements  ProductionService {

    Logger logger = LoggerFactory.getLogger(ProductionServiceImpl.class);

    private static org.apache.log4j.Logger logMongo = org.apache.log4j.Logger.getLogger("mongodb");



    @Autowired
    ProductFetchService productFetchService;

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public Boolean saveProduct(ProductDTO productDTO) throws ServiceException {
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

        } catch (com.shangpin.framework.ServiceException e) {

            if(ProductFetchServiceImpl.REPEAT_MESSAGE.equals(e.getMessage())){
              //重复插入不做处理

            }else{
                e.printStackTrace();
                throw new ServiceMessageException("save failed");
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
        skuDTO.setSalePrice(productDTO.getSalePrice().toString());
        skuDTO.setSupplierPrice(productDTO.getSupplierPrice().toString());
        skuDTO.setProductDescription(productDTO.getProductDescription());
        skuDTO.setProductName(productDTO.getProductName());
        skuDTO.setProductSize(productDTO.getSize());
        skuDTO.setStock(String.valueOf(productDTO.getStock()));
        skuDTO.setSaleCurrency(productDTO.getSaleCurrency());
        try {
            productFetchService.saveSKU(skuDTO);
        } catch (com.shangpin.framework.ServiceException e) {
            e.printStackTrace();
        }

        SpuPictureDTO spuPictureDTO =  productDTO.getSpuPictureDTO();
        String imgUrl =spuPictureDTO.getImageUrl();
        String[] imageUrlArray = imgUrl.split("||");

        for(String  imageUrl:imageUrlArray){
            ProductPictureDTO pictureDTO = new ProductPictureDTO();
            pictureDTO.setSupplierId(productDTO.getSupplierId());
            pictureDTO.setPicUrl(imageUrl);
            pictureDTO.setSpuId(spuPictureDTO.getSpuId());
            try {
                productFetchService.savePictureForMongo(pictureDTO);
            } catch (com.shangpin.framework.ServiceException e) {
                e.printStackTrace();
            }
        }


        SkuPictureDTO skuPictureDTO = productDTO.getSkuPictureDTO();
        String skuImgUrl =skuPictureDTO.getImageUrl();
        String[] skuImageUrlArray = skuImgUrl.split("||");
        for(String  imageUrl:skuImageUrlArray){
            ProductPictureDTO pictureDTO = new ProductPictureDTO();
            pictureDTO.setSupplierId(productDTO.getSupplierId());
            pictureDTO.setPicUrl(imageUrl);
            pictureDTO.setSkuId(skuPictureDTO.getSkuId());
            try {
                productFetchService.savePictureForMongo(pictureDTO);
            } catch (com.shangpin.framework.ServiceException e) {
                e.printStackTrace();
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

        if(null==productDTO.getSkuPictureDTO()&&null==productDTO.getSpuPictureDTO()){
            throw new ServiceMessageException("imageUrl not allow null");
        }else if(null!=productDTO.getSkuPictureDTO()&&StringUtils.isBlank(productDTO.getSkuPictureDTO().getImageUrl())){
            throw new ServiceMessageException("if SkuPictureDTO object is not null,imageUrl property not allow zero length");

        }else if(null!=productDTO.getSpuPictureDTO()&&StringUtils.isBlank(productDTO.getSpuPictureDTO().getImageUrl())){
            throw new ServiceMessageException("if SpuPictureDTO object is not null,imageUrl property not allow zero length");
        }

        if(null==productDTO.getSupplierPrice()){
            throw new ServiceMessageException("supplierPrice not allow null");
        }



    }

    @Override
    public Boolean updateProduct(ProductUpdateDTO productUpdateDTO) throws ServiceException {
        return null;
    }
}
