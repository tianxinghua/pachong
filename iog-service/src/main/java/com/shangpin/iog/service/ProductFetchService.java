package com.shangpin.iog.service;

import java.util.List;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * Created by loyalty on 15/6/4.
 * 产品
 */
public interface ProductFetchService {
    /**
     * 保存SPU列表
     * @param spuDTOList spu对象列表
     * @throws ServiceException 自定义异常
     */
    public void saveSPU(List<SpuDTO> spuDTOList) throws ServiceException;

    /**
     * 保存单个spu
     * @param spuDTO
     * @throws ServiceException
     */
    public void saveSPU(SpuDTO spuDTO) throws ServiceException;

    /**
     * 保存SKU列表
     * @param skuDTOList  sku对象列表
     * @throws ServiceException
     */
    public void saveSKU(List<SkuDTO> skuDTOList) throws ServiceException;


    /**
     * 保存SKU
     * @param skuDTO  sku对象
     * @throws ServiceException
     */
    public void saveSKU(SkuDTO skuDTO) throws ServiceException;

    /**
     * 保存照片
     * @param pictureDTO 照片对象
     * @throws ServiceException
     */
    public void savePicture(ProductPictureDTO pictureDTO) throws ServiceException;

    /**
     * 保存照片列表
     * @param pictureDTOList  照片对象列表
     * @throws ServiceException
     */
    public void savePicture(List<ProductPictureDTO> pictureDTOList) throws ServiceException;



}
