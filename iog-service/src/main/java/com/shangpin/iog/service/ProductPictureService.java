package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductPictureDTO;

import java.util.List;

/**
 * Created by loyalty on 15/6/5.
 */
public interface ProductPictureService {
    /**
     * 获取图片信息
     * @param supplier 供应商编号标识
     * @param sku      库存标识
     * @return         图片列表
     * @throws ServiceException
     */
    public List<ProductPictureDTO> findBySupplierAndSku(String supplier,String sku) throws ServiceException;
}
