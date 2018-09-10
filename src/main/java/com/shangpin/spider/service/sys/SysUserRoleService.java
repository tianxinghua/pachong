package com.shangpin.spider.service.sys;

import java.util.Map;

import com.shangpin.spider.entity.sys.SysUserRole;

public interface SysUserRoleService {
	/**
	 * 保存用户角色信息
	 * @param userRole
	 * @return
	 */
	int save(SysUserRole userRole);
	
	/**
	 * 编辑角色
	 * @param uid
	 * @param role
	 * @param roleMap
	 * @throws Exception 
	 */
	public void editRole(Integer uid, String role, Map<String, Integer> roleMap) throws Exception;
	
}
