package com.shangpin.spider.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.sys.SysRole;

public interface SysRoleMapper {
    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
	 * 根据用户ID获取相应角色
	 * @param userId
	 * @return
	 */
	List<SysRole> getRoleByUserId(@Param("userId")Integer userId);
	/**
	 * 获取所有角色
	 * @return
	 */
	List<SysRole> getRoleAll();
}