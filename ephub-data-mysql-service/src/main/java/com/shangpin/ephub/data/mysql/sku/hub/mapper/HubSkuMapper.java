package com.shangpin.ephub.data.mysql.sku.hub.mapper;

import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSkuMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月24日 下午2:57:18
 */
@Mapper
public interface HubSkuMapper {
    int countByExample(HubSkuCriteria example);

    int deleteByExample(HubSkuCriteria example);

    int deleteByPrimaryKey(Long skuId);

    int insert(HubSku record);

    int insertSelective(HubSku record);

    List<HubSku> selectByExampleWithRowbounds(HubSkuCriteria example, RowBounds rowBounds);

    List<HubSku> selectByExample(HubSkuCriteria example);

    HubSku selectByPrimaryKey(Long skuId);

    int updateByExampleSelective(@Param("record") HubSku record, @Param("example") HubSkuCriteria example);

    int updateByExample(@Param("record") HubSku record, @Param("example") HubSkuCriteria example);

    int updateByPrimaryKeySelective(HubSku record);

    int updateByPrimaryKey(HubSku record);
}