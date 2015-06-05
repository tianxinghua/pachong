package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

import java.util.List;

/**
 * Created by sunny on 2015/6/5.
 */
public interface ApennineProductService {
    /**
     * 插入SKU
     * @param dto
     * @return
     * @throws ServiceException
     */
    public long insertSKU(SkuDTO dto)throws ServiceException;

    /**
     * 插入SKU列表
     * @param skuDTOList
     * @return
     * @throws ServiceException
     */
    public int insertSKU(List<SkuDTO> skuDTOList)throws ServiceException;

    /**
     * 插入SPU
     * @param dto
     * @return
     * @throws ServiceException
     */
    public long insertSPU(SpuDTO dto)throws ServiceException;

    /**
     * 插入SPU列表
     * @param spuDTOList
     * @return
     * @throws ServiceException
     */
    public int insertSPU(List<SpuDTO>spuDTOList)throws ServiceException;

    /**
     * 插入照片
     * @param dto
     * @return
     * @throws ServiceException
     */
    public long insertPicture(ProductPictureDTO dto)throws ServiceException;

    /**
     * 插入照片列表
     * @param pictureDTOList
     * @return
     * @throws ServiceException
     */
    public int insertPicture(List<ProductPictureDTO> pictureDTOList) throws ServiceException;
}
