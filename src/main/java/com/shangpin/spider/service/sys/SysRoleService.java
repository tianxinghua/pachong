package com.shangpin.spider.service.sys;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.shangpin.spider.entity.sys.SysRole;

public interface SysRoleService {

	List<SysRole> getRoleByUserId(Integer userId);

	int save(SysRole record);

	int update(SysRole record);
	
	/**
	 * 获取所有角色
	 * @return
	 */
	public JSONArray getRoleAll();
	
	/**
	 * 角色管理
	 * @return
	 */
	public JSONArray toRolePage();

}
