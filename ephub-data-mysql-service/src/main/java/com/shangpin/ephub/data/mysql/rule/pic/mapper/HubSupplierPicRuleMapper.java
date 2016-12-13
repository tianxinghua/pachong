package com.shangpin.ephub.data.mysql.rule.pic.mapper;

import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRule;
import com.shangpin.ephub.data.mysql.rule.pic.po.HubSupplierPicRuleCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface HubSupplierPicRuleMapper {
    int countByExample(HubSupplierPicRuleCriteria example);

    int deleteByExample(HubSupplierPicRuleCriteria example);

    int deleteByPrimaryKey(Long supplierPicRuleId);

    int insert(HubSupplierPicRule record);

    int insertSelective(HubSupplierPicRule record);

    List<HubSupplierPicRule> selectByExampleWithRowbounds(HubSupplierPicRuleCriteria example, RowBounds rowBounds);

    List<HubSupplierPicRule> selectByExample(HubSupplierPicRuleCriteria example);

    HubSupplierPicRule selectByPrimaryKey(Long supplierPicRuleId);

    int updateByExampleSelective(@Param("record") HubSupplierPicRule record, @Param("example") HubSupplierPicRuleCriteria example);

    int updateByExample(@Param("record") HubSupplierPicRule record, @Param("example") HubSupplierPicRuleCriteria example);

    int updateByPrimaryKeySelective(HubSupplierPicRule record);

    int updateByPrimaryKey(HubSupplierPicRule record);
}