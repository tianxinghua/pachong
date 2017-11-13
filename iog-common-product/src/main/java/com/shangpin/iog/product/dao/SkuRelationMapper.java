package com.shangpin.iog.product.dao;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface SkuRelationMapper extends IBaseDao<SkuRelationDTO> {


    /**
     * 获取供货商队对应的  关系列表
     * @param supplier 供货商门户ID
     * @param rowBounds 分页对象
     * @return
     */
    public List<SkuRelationDTO>  getSkuRelationListBySupplierId(@Param("supplier") String supplier,RowBounds rowBounds);


    /**
     * 获取供货商队对应的  关系列表
     * @param supplier 供货商门户ID
     * @return
     */
    public List<SkuRelationDTO>  getSkuRelationListBySupplierId(@Param("supplier") String supplier);
    /**
     * 根据尚品的SKUID获取对照关系
     * @param sopSkuId
     * @return
     */
    public SkuRelationDTO getSkuRelationBySopSkuId(@Param("sopSkuId") String sopSkuId);

    /**
     * 根据门户号和尚品的SKUID获取对照关系
     * @param supplierId   供货商门户编号
     * @param sopSkuId     尚品SKU编号
     * @return
     */
    public SkuRelationDTO   getSkuRelationBySupplierIdAndSkuId(@Param("supplierId") String supplierId,@Param("sopSkuId") String sopSkuId);

    /**
     * 获取对照关系
     * @param supplierId   供货商门户编号
     * @param supplierSkuNo 供货商SKU编号
     * @return  对照关系对象
     */
    public SkuRelationDTO getSkuRelationBySupplierSkuId(@Param("supplierId") String supplierId,@Param("skuNo") String supplierSkuNo);
    /**
     * 根据尚品sku编号和供应商门户编号去更新供应商sku编号
     * @param skuRelationDTO
     * @return
     */
    public int updateSupplierSkuNo(SkuRelationDTO skuRelationDTO);
    /**
     * 
     * @param supplierId
     * @param supplierSkuNo
     * @param spSkuId
     * @return
     */
    public int countSkuRelation(@Param("supplierId") String supplierId, @Param("supplierSkuNo") String supplierSkuNo, @Param("spSkuId") String spSkuId);

    /**
     * 删除记录
     * @param supplierId
     * @param spSkuId
     */
    public void deleteSkuRelation(@Param("supplierId") String supplierId, @Param("spSkuId") String spSkuId);
}