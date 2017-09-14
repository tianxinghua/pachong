package com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.mapper;

import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetail;
import com.shangpin.ephub.data.mysql.sp.spshippingpolicydetail.po.SpShippingPolicyDetailCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SpShippingPolicyDetailMapper {
    int countByExample(SpShippingPolicyDetailCriteria example);

    int deleteByExample(SpShippingPolicyDetailCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(SpShippingPolicyDetail record);

    int insertSelective(SpShippingPolicyDetail record);

    List<SpShippingPolicyDetail> selectByExampleWithRowbounds(SpShippingPolicyDetailCriteria example, RowBounds rowBounds);

    List<SpShippingPolicyDetail> selectByExample(SpShippingPolicyDetailCriteria example);

    SpShippingPolicyDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SpShippingPolicyDetail record, @Param("example") SpShippingPolicyDetailCriteria example);

    int updateByExample(@Param("record") SpShippingPolicyDetail record, @Param("example") SpShippingPolicyDetailCriteria example);

    int updateByPrimaryKeySelective(SpShippingPolicyDetail record);

    int updateByPrimaryKey(SpShippingPolicyDetail record);
}