package com.shangpin.spider.mapper.sys;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.sys.SysRoleMenuKey;

public interface SysRoleMenuMapper {
    int insert(SysRoleMenuKey record);

    int insertSelective(SysRoleMenuKey record);

	/**
	 * 删除该角色下的所有权限
	 * @param roleId
	 */
	int delPerByRoleId(@Param("roleId")Integer roleId);
	/**
	 * 添加该角色的权限
	 * @param roleId
	 * @param perId
	 */
	void addPermissionRole(@Param("roleId")Integer roleId, @Param("perId")Integer perId);
}