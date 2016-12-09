package com.shangpin.ep.order.module.sku.mapper;

import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.bean.HubSkuCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSkuMapper.java </p>
 * <p>Description: HUB订单系统sku数据访问层接口规范</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:37:37
 */
@Mapper
public interface HubSkuMapper {
    int countByExample(HubSkuCriteria example);

    int deleteByExample(HubSkuCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(HubSku record);

    int insertSelective(HubSku record);

    List<HubSku> selectByExampleWithRowbounds(HubSkuCriteria example, RowBounds rowBounds);

    List<HubSku> selectByExample(HubSkuCriteria example);

    HubSku selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") HubSku record, @Param("example") HubSkuCriteria example);

    int updateByExample(@Param("record") HubSku record, @Param("example") HubSkuCriteria example);

    int updateByPrimaryKeySelective(HubSku record);

    int updateByPrimaryKey(HubSku record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}