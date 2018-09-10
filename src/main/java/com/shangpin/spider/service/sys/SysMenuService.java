package com.shangpin.spider.service.sys;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.shangpin.spider.entity.sys.SysMenu;

public interface SysMenuService {

	List<SysMenu> getPermissionByRoleId(Integer roleId);

	List<SysMenu> selectAll();

	List<SysMenu> selectByMenu(SysMenu menu);

	int update(SysMenu record);

	int save(SysMenu record);
	
	/**
	 * 获取所有权限
	 * @return
	 */
	public JSONArray getPermissionAll();

}
