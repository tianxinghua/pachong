package com.shangpin.ephub.data.mysql.sp.shippingpolicy.mapper;

import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicy;
import com.shangpin.ephub.data.mysql.sp.shippingpolicy.po.SpShippingPolicyCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface SpShippingPolicyMapper {
    int countByExample(SpShippingPolicyCriteria example);

    int deleteByExample(SpShippingPolicyCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(SpShippingPolicy record);

    int insertSelective(SpShippingPolicy record);

    List<SpShippingPolicy> selectByExampleWithRowbounds(SpShippingPolicyCriteria example, RowBounds rowBounds);

    List<SpShippingPolicy> selectByExample(SpShippingPolicyCriteria example);

    SpShippingPolicy selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SpShippingPolicy record, @Param("example") SpShippingPolicyCriteria example);

    int updateByExample(@Param("record") SpShippingPolicy record, @Param("example") SpShippingPolicyCriteria example);

    int updateByPrimaryKeySelective(SpShippingPolicy record);

    int updateByPrimaryKey(SpShippingPolicy record);
}