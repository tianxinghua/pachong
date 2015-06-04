package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by loyalty on 15/6/4.
 */
@Service
public class ProductFetchImpl implements ProductFetchService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SkuMapper skuDAO;

    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper pictureDAO;

    @Override
    public void saveSPU(List<SpuDTO> spuDTOList) throws ServiceException {
        try {
            spuDAO.saveList(spuDTOList);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }
    }

    @Override
    public void saveSPU(SpuDTO spuDTO) throws ServiceException {
        try {
            spuDAO.save(spuDTO);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }

    }

    @Override
    public void saveSKU(List<SkuDTO> skuDTOList) throws ServiceException {

        try {
            skuDAO.saveList(skuDTOList);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }

    }

    @Override
    public void saveSKU(SkuDTO skuDTO) throws ServiceException {

        try {
            skuDAO.save(skuDTO);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }
    }

    @Override
    public void savePicture(ProductPictureDTO pictureDTO) throws ServiceException {
        try {
            pictureDAO.save(pictureDTO);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }

    }

    @Override
    public void savePicture(List<ProductPictureDTO> pictureDTOList) throws ServiceException {
        try {
            pictureDAO.saveList(pictureDTOList);
        } catch (SQLException e) {
            throw new ServiceMessageException("数据插入失败");
        }

    }
}
