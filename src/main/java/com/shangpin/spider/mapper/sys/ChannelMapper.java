package com.shangpin.spider.mapper.sys;

import com.shangpin.spider.entity.Channel;

public interface ChannelMapper {
    int insert(Channel record);

    int insertSelective(Channel record);

    Channel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Channel record);

    int updateByPrimaryKey(Channel record);
}