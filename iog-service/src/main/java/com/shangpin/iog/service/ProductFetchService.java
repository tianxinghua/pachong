package com.shangpin.iog.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * Created by loyalty on 15/6/4.
 * 产品
 */
public interface ProductFetchService {
	 public Map<String,String> findPictureBySupplierIdAndSpuId(String supplierId, String spuId);
	 public Map<String,String> findPictureBySupplierIdAndSkuId(String supplierId, String skuId);
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
     * 更新价格和库存
     * @param skuDTO
     * @throws ServiceException
     */
    public void updatePriceAndStock(SkuDTO skuDTO) throws ServiceException;
    
    /**
     * 更新材质
     * @param spuDTO
     * @throws ServiceException
     */
    public void updateMaterial(SpuDTO spuDTO) throws ServiceException;

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

    /**
     * 保存照片到mongodb中
     * @param productPictureDTO
     * @throws ServiceException
     */
    public void savePictureForMongo(ProductPictureDTO productPictureDTO) throws ServiceException;
   
    /**
     * 保存照片到mongodb中,如果以spu方式保存，则skuId设为null,反之，spuId为null,
     * @param supplierId
     * @param spuId
     * @param skuId
     * @param picUrl集合
     */
	public void savePicture(String supplierId, String spuId, String skuId,
			Collection<String> pic);



}
