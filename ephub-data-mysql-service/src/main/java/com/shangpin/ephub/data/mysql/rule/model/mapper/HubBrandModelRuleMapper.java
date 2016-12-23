package com.shangpin.ephub.data.mysql.rule.model.mapper;

import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRule;
import com.shangpin.ephub.data.mysql.rule.model.po.HubBrandModelRuleCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubBrandModelRuleMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午8:38:22
 */
@Mapper
public interface HubBrandModelRuleMapper {
    int countByExample(HubBrandModelRuleCriteria example);

    int deleteByExample(HubBrandModelRuleCriteria example);

    int deleteByPrimaryKey(Long brandModelRuleId);

    int insert(HubBrandModelRule record);

    int insertSelective(HubBrandModelRule record);

    List<HubBrandModelRule> selectByExampleWithRowbounds(HubBrandModelRuleCriteria example, RowBounds rowBounds);

    List<HubBrandModelRule> selectByExample(HubBrandModelRuleCriteria example);

    HubBrandModelRule selectByPrimaryKey(Long brandModelRuleId);

    int updateByExampleSelective(@Param("record") HubBrandModelRule record, @Param("example") HubBrandModelRuleCriteria example);

    int updateByExample(@Param("record") HubBrandModelRule record, @Param("example") HubBrandModelRuleCriteria example);

    int updateByPrimaryKeySelective(HubBrandModelRule record);

    int updateByPrimaryKey(HubBrandModelRule record);
}