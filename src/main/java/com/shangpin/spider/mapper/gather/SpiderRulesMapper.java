package com.shangpin.spider.mapper.gather;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.gather.SpiderRules;

public interface SpiderRulesMapper {
    int insert(SpiderRules record);

    int insertSelective(SpiderRules record);

    SpiderRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpiderRules record);

    int updateByPrimaryKey(SpiderRules record);

	SpiderRules getRuleById(@Param("id")Long id);
    
}