package com.shangpin.iog.service;

import java.util.List;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.SkuRelationDTO;

/**
 * Created by loyalty on 15/10/2.
 * 处理供货商SKU和SOPSKU的关系
 */
public interface SkuRelationService {

    /**
     * 获取SKU对照关系表分页信息
     * @param supplier 供货商门户ID
     * @param pageIndex  页码
     * @param pageSize   每页显示行数
     * @return
     * @throws ServiceException
     */
    public Page<SkuRelationDTO> findSkuRelateionPageBySupplierId(String supplier,Integer pageIndex,Integer pageSize) throws ServiceException;

    /**
     * 获取SKU对照关系列表
     * @param supplier 供货商门户ID
     * @return
     */
    public  List<SkuRelationDTO> findListBySupplierId(String supplier) throws ServiceException;

    /**
     * 根据尚品SKUID 获取对照关系
     * @param skuId ：尚品SKUID
     * @return
     * @throws ServiceException
     */
    public SkuRelationDTO  getSkuRelationBySkuId(String skuId)  throws  ServiceException;



    /**
     * 根据门户编号 和 尚品SKUID 获取对照关系
     * @param supplier :门户编号
     * @param skuId ：尚品SKUID
     * @return
     * @throws ServiceException
     */
    public SkuRelationDTO  getSkuRelationBySupplierIdAndSkuId(String supplier,String skuId)  throws  ServiceException;

    /**
     * 根据供货商门户ID和供货商SKUNO获取对照关系
     * @param supplier
     * @param supplierSkuNo
     * @return
     * @throws ServiceException
     */
    public SkuRelationDTO  getSkuRelationBySupplierIdAndSupplierSkuNo(String supplier,String supplierSkuNo)  throws  ServiceException;

    /**
     * 保存SKU关系对照对象
     * @param skuRelationDTO
     * @throws ServiceException
     */
    public void saveSkuRelateion(SkuRelationDTO skuRelationDTO) throws ServiceException;
    /**
     * 更新SKU关系对照对象
     * @param skuRelationDTO
     * @throws ServiceException
     */
    public void updateSkuRelateion(SkuRelationDTO skuRelationDTO) throws Exception;
    /**
     * 根据尚品sku编号和供应商门户编号去更新供应商sku编号
     * @param skuRelationDTO
     * @throws Exception
     */
    public void updateSupplierSkuNo(SkuRelationDTO skuRelationDTO) throws Exception;
    /**
     * 根据门户账号，供应商sku编号，尚品sku编号查找
     * @param supplierId 门户编号
     * @param supplierSkuNo 供应商sku编号
     * @param spSkuId 尚品sku编号
     * @return
     */
    public int countSkuRelation(String supplierId, String supplierSkuNo, String spSkuId);
    /**
     * 根据门户和尚品sku编号删除
     * @param supplierId
     * @param spSkuId
     */
    public void deleteSkuRelation(String supplierId, String spSkuId);





}
