package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductFetchService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/6/4.
 */
@Service
public class ProductFetchServiceImpl implements ProductFetchService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SkuMapper skuDAO;

    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper productPictureMapper;

    @Autowired
    PictureDAO pictureDAO;


    @Override
    public void saveSPU(List<SpuDTO> spuDTOList) throws ServiceException {
        try {
            spuDAO.saveList(spuDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException("数据插入失败键重复");
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }
    }

    @Override
    public void saveSPU(SpuDTO spuDTO) throws ServiceException {
        try {
            spuDAO.save(spuDTO);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException("数据插入失败键重复");
        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void saveSKU(List<SkuDTO> skuDTOList) throws ServiceException {

        try {

            skuDAO.saveList(skuDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException("数据插入失败键重复");
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void saveSKU(SkuDTO skuDTO) throws ServiceException {

        try {
            if(StringUtils.isBlank(skuDTO.getNewMarketPrice())) skuDTO.setNewMarketPrice(skuDTO.getMarketPrice());
            if(StringUtils.isBlank(skuDTO.getNewSalePrice())) skuDTO.setNewSalePrice(skuDTO.getSalePrice());
            if(StringUtils.isBlank(skuDTO.getNewSupplierPrice())) skuDTO.setNewSupplierPrice(skuDTO.getSupplierPrice());
            skuDAO.save(skuDTO);
        } catch ( Exception e) {
        	if(e instanceof DuplicateKeyException)
        	throw new ServiceMessageException("数据插入失败键重复");
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }
    }

    @Override
    public void updatePriceAndStock(SkuDTO skuDTO) throws ServiceException {
        try {
            if(null==skuDTO.getUpdateTime()) skuDTO.setUpdateTime(new Date());
            skuDAO.updatePriceAndStock(skuDTO);
        } catch ( Exception e) {

            throw new ServiceMessageException("数据更新失败"+e.getMessage());
        }
    }

    @Override
    public void savePicture(ProductPictureDTO pictureDTO) throws ServiceException {
        try {
            productPictureMapper.save(pictureDTO);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException("数据插入失败键重复");
        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void savePicture(List<ProductPictureDTO> pictureDTOList) throws ServiceException {
        try {
            productPictureMapper.saveList(pictureDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException("数据插入失败键重复");
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void savePictureForMongo(ProductPictureDTO productPictureDTO) throws ServiceException {
        ProductPicture productPicture = new ProductPicture();
        if(null!=productPictureDTO) {
            try {
                InVoke.setValue(productPictureDTO,productPicture,null,null);
                pictureDAO.save(productPicture);
            } catch (Exception e) {
            	if(e instanceof DuplicateKeyException)
            		throw new ServiceMessageException("数据插入失败键重复");
                throw new ServiceMessageException("数据插入失败"+e.getMessage());
            }
        }
    }
}
