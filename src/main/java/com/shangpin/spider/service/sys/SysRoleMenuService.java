package com.shangpin.spider.service.sys;

import java.util.Map;

public interface SysRoleMenuService {
	
	/**
	 * 编辑角色权限
	 * @param roleId
	 * @param per
	 * @param permissionMap
	 * @throws Exception 
	 */
	void editRolePermission(Integer roleIdInt, String per,
			Map<String, Integer> permissionMap) throws Exception;

}
