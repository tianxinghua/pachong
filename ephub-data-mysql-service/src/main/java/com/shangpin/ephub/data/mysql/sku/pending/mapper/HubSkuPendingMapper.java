package com.shangpin.ephub.data.mysql.sku.pending.mapper;

import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSkuPendingMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月20日 下午5:05:49
 */
@Mapper
public interface HubSkuPendingMapper {
    int countByExample(HubSkuPendingCriteria example);

    int deleteByExample(HubSkuPendingCriteria example);

    int deleteByPrimaryKey(Long skuPendingId);

    int insert(HubSkuPending record);

    int insertSelective(HubSkuPending record);

    List<HubSkuPending> selectByExampleWithRowbounds(HubSkuPendingCriteria example, RowBounds rowBounds);

    List<HubSkuPending> selectByExample(HubSkuPendingCriteria example);

    HubSkuPending selectByPrimaryKey(Long skuPendingId);

    int updateByExampleSelective(@Param("record") HubSkuPending record, @Param("example") HubSkuPendingCriteria example);

    int updateByExample(@Param("record") HubSkuPending record, @Param("example") HubSkuPendingCriteria example);

    int updateByPrimaryKeySelective(HubSkuPending record);

    int updateByPrimaryKey(HubSkuPending record);
}