package com.shangpin.ep.order.module.order.mapper;

import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.HubOrderDetailCriteria;
import java.util.List;

import com.shangpin.ep.order.module.order.bean.SkuCountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubOrderDetailMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午4:55:15 
 */
@Mapper
public interface HubOrderDetailMapper {
    int countByExample(HubOrderDetailCriteria example);

    int deleteByExample(HubOrderDetailCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(HubOrderDetail record);

    int insertSelective(HubOrderDetail record);

    List<HubOrderDetail> selectByExampleWithRowbounds(HubOrderDetailCriteria example, RowBounds rowBounds);

    List<HubOrderDetail> selectByExample(HubOrderDetailCriteria example);

    HubOrderDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HubOrderDetail record, @Param("example") HubOrderDetailCriteria example);

    int updateByExample(@Param("record") HubOrderDetail record, @Param("example") HubOrderDetailCriteria example);

    int updateByPrimaryKeySelective(HubOrderDetail record);

    int updateByPrimaryKey(HubOrderDetail record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/

    /**
     * 非订单对接的供货商未扣减库存的订单数
     * @param suppleriId
     * @return
     */
    List<SkuCountDTO> getSkuCountOfNoOrderApiBySupplierId(@Param("supplierId") String suppleriId);

    /**
     * 有订单对接的供货商未扣减库存的订单数
     * @param suppleriId
     * @return
     */
    List<SkuCountDTO> getSkuCountOfOrderApiBySupplierId(@Param("supplierId") String suppleriId);

    /**
     * 有订单对接的供货商未扣减库存的订单数
     * @param suppleriId
     * @param skuNoList 尚品的SKU
     * @return
     */
    List<SkuCountDTO> getSkuCountOfOrderApiBySupplierIdAndSpSkuNo(@Param("supplierId") String suppleriId,@Param("skuNoList") List<String> skuNoList);
    /**
     * 有订单对接的供货商未扣减库存的订单数
     * @param suppleriId
     * @param skuNoList 供货商的SKU
     * @return
     */
    List<SkuCountDTO> getSkuCountOfOrderApiBySupplierIdAndSupplierSkuNo(@Param("supplierId") String suppleriId,@Param("skuNoList") List<String> skuNoList);


}