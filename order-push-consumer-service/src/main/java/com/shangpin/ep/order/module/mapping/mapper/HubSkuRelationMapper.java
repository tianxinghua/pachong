package com.shangpin.ep.order.module.mapping.mapper;

import com.shangpin.ep.order.module.mapping.bean.HubSkuRelation;
import com.shangpin.ep.order.module.mapping.bean.HubSkuRelationCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSkuRelationMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:52:40
 */
@Mapper
public interface HubSkuRelationMapper {
    int countByExample(HubSkuRelationCriteria example);

    int deleteByExample(HubSkuRelationCriteria example);

    int insert(HubSkuRelation record);

    int insertSelective(HubSkuRelation record);

    List<HubSkuRelation> selectByExampleWithRowbounds(HubSkuRelationCriteria example, RowBounds rowBounds);

    List<HubSkuRelation> selectByExample(HubSkuRelationCriteria example);

    int updateByExampleSelective(@Param("record") HubSkuRelation record, @Param("example") HubSkuRelationCriteria example);

    int updateByExample(@Param("record") HubSkuRelation record, @Param("example") HubSkuRelationCriteria example);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}