package com.shangpin.spider.mapper.sys;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.sys.SysUserRole;

public interface SysUserRoleMapper {
    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

	void delRoleByUid(@Param("userId")Integer uid);
}