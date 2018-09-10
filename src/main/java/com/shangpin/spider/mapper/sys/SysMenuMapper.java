package com.shangpin.spider.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.sys.SysMenu;

public interface SysMenuMapper {
    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    /**
	 * 根据角色得到相应权限
	 * @param roleId
	 * @return
	 */
	List<SysMenu> getPermissionByRoleId(@Param("roleId")Integer roleId);
	/**
	 * 获取所有权限
	 * @return
	 */
	List<SysMenu> selectAll();
	/**
	 * 根据菜单信息查询菜单列表
	 * @param menu
	 * @return
	 */
	List<SysMenu> selectByMenu(SysMenu menu);
}