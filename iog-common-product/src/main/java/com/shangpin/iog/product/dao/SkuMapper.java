package com.shangpin.iog.product.dao;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Mapper
public interface SkuMapper extends IBaseDao<SkuDTO> {

    /**
     * 更新价格和库存
     * @param skuDTO
     * @throws ServiceException
     */
    public void updatePriceAndStock(SkuDTO skuDTO) throws ServiceException;

    /**
     * 查询新的价格
     * @param supplierId
     */
    public List<NewPriceDTO> findNewPrice(@Param("supplierId") String supplierId) throws ServiceException;
    /**
     * 查询进货价
     * @param supplierId
     */
    public SkuDTO findSupplierPrice(@Param("supplierId") String supplierId,@Param("skuId") String skuId) throws ServiceException;
    
    
    /**
     * 通过supplierid和skuid查询最新价格
     */
    public NewPriceDTO findNewPriceBySku(@Param("supplierId") String supplierId,@Param("skuId") String skuId) throws ServiceException;
    
    /**
     * 更新价格
     * @param skuDTO
     * @throws ServiceException
     */
    public void updatePrice(SkuDTO skuDTO) throws ServiceException;


    /**
     * 根据供货商标识和最后修改时间获取产品列表
     * @param supplier   供货商ID或NO 唯一标示
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<SkuDTO> findListBySupplierAndLastDate(@Param("supplier") String supplier,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);
    
    public SkuDTO findSKUBySupplierAndSkuId(@Param("supplierId") String supplierId,@Param("skuId") String skuId) throws ServiceException; 
    /**
     * 更新备注，last_time
     */
    public void updateSkuMemo(@Param("supplierId") String supplierId,@Param("skuId") String skuId,@Param("memo") String memo,@Param("lastTime") Date date);

	public void updateSkuMemoList(@Param("list") List<String> idList, @Param("supplierId")String supplierId);

	public String findBarCodeBySkuId(@Param("supplierId")String supplierId, @Param("skuId") String skuId);
	
	/**
	 * 根据供应商编号和供应商skuId更新商品skuId
	 * @param supplierId
	 * @param supplierSkuId
	 * @param spSkuId
	 */
	public void updateSpSkuIdBySupplier(@Param("supplierId") String supplierId, @Param("supplierSkuId") String supplierSkuId, @Param("spSkuId") String spSkuId,@Param("skuStatus") String skuStatus,@Param("spProductCode") String spProductCode) throws ServiceException;

	public List<SkuDTO> findSpSkuIdsBySupplier(@Param("supplierId") String supplierId);
	
}