package com.shangpin.iog.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * Created by loyalty on 15/6/4.
 * 产品
 */
public interface ProductFetchService {
	 public Map<String,String> findPictureBySupplierIdAndSpuId(String supplierId, String spuId);
	 public Map<String,String> findPictureBySupplierIdAndSkuId(String supplierId, String skuId);
	 public SkuDTO findSKUBySupplierIdAndSkuId(String supplierId, String spuId);
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
     * 
     * @param supplierId
     * @param spuId
     * @param skuId
     * @param picUrl集合
     */
	public void savePicture(String supplierId, String spuId, String skuId,
			Collection<String> pic);

	
	   /**
     * 
     * @param supplierId
     * @param spuId
     * @param skuId
     * @param picUrl集合
     */
	
	/**
	 * 
	 * @param supplierId
	 * @param skuId
	 * @return
	 * @throws ServiceException
	 */
	public SkuDTO findSupplierPrice(String supplierId, String skuId)
			throws ServiceException ;
	public SpuDTO findSPUBySupplierIdAndSpuId(String supplierId, String spuId);
	public List<SkuRelationDTO> selectAllRelation()  throws ServiceException;
	
	public List<ProductDTO> selectSkuByDay() throws ServiceException;
	public List<ProductDTO> selectSpuByDay() throws ServiceException;
	public List<ProductDTO> selectAllSku() throws ServiceException;
	public List<ProductDTO> selectAllSpu() throws ServiceException;
	public void saveSkuRelation(SkuRelationDTO sku) throws ServiceException;
	public List<SkuRelationDTO> selectRelationDayFromHK() throws ServiceException;
	/**
	 * @param supplierId
	 * @param id 对应的skuid or spuid
	 * @param picUrl
	 * @param flag sku表示用skuid保存 spu表示用spuid保存
	 * @return
	 */
	public List<String> saveAndCheckPicture(String supplierId, String id,Collection<String> picUrl,String flag);
	//更新spu或sku
	public void updateSpuOrSkuMemoAndTime(String supplierId, String id, String memo,String flag);
	//更新skuList的memo
	public void updateSkuListMemo(String supplierId, List<String> idList);
	//更新spuList的memo
	public void updateSpuListMemo(List<SpuDTO> spuList);
	//检查更新stefaniamode的图片
	public abstract List<String> saveAndCheckPictureForSteFaniamode(String supplierId, String id, Collection<String> picUrl, String flag);
	public String findBarCodeBySupplierIdAndSkuId(String supplierId,
			String skuId);
	//获取mongo
	Map<String, String> findPictureBySupplierIdAndSkuIdOrSpuId(String supplierId, String skuId, String spuId);
	public List<ProductDTO> findSkuBySupplierId(String supplier);
	public List<ProductDTO> findProductByDate(String startDate, String endDate);
	
	/**
	 * 根据供应商编号和供应商skuId更新商品skuId
	 * @param supplierId
	 * @param supplierSkuId
	 * @param spSkuId
	 */
	public void updateSpSkuIdBySupplier(String supplierId,String supplierSkuId,String spSkuId,String skuStatus) throws ServiceException;
	public void update(SkuDTO sku);
	public void updateSpSkuIdBySupplier(String supplierId,String supplierSkuId,String spSkuId,String skuStatus,String spProductCode) throws ServiceException;
}
