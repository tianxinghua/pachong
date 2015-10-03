package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.SkuRelationDTO;

import java.util.Date;
import java.util.List;

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
     * 保存SKU关系对照对象
     * @param skuRelationDTO
     * @throws ServiceException
     */
    public void saveSkuRelateion(SkuRelationDTO skuRelationDTO) throws ServiceException;





}
