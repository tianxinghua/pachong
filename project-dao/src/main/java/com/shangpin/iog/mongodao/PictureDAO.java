package com.shangpin.iog.mongodao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongobase.BaseMongodbDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by loyalty on 15/6/26.
 */
@Repository
public interface PictureDAO extends BaseMongodbDAO<ProductPicture,String> {
    /**
     * 根据供货商ID和SKUID获取图片
     * @param supplierId 供货商ID
     * @param skuId  SKUID
     * @return
     * @throws ServiceException
     */
    public List<ProductPicture> findDistinctProductPictureBySupplierIdAndSkuId(String supplierId,String skuId) throws ServiceException;

    /**
     * 根据供货商和地址获取图片对象
     * @param supplierId
     * @param picUrl
     * @return
     * @throws ServiceException
     */
    public  List<ProductPicture> findDistinctProductPictureBySupplierIdAndPicUrl(String supplierId,String picUrl) throws ServiceException;


    /**
     * 根据供货商ID和SPUID获取图片
     * @param supplierId 供货商ID
     * @param spuId  spuId
     * @return
     * @throws ServiceException
     */
    public List<ProductPicture> findDistinctProductPictureBySupplierIdAndSpuIdAndSkuIdNull(String supplierId,String spuId) throws ServiceException;


    public void removePicture(String _id)throws ServiceException;
}
