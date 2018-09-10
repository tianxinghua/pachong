package com.shangpin.spider.service.sys;

import com.alibaba.fastjson.JSONArray;
import com.shangpin.spider.entity.sys.SysUser;

public interface SysUserService {
	
	/**
	 * 根据用户名查找用户的角色权限信息
	 * @param username
	 * @return
	 */
	SysUser getByUserName(String username);
	/**
	 * 根据用户名和密码查询
	 * @param username
	 * @param password
	 * @return
	 */
	SysUser getByUserNameAndPwd(String username, String password);
	/**
	 * 保存系统用户信息
	 * @param sysuser
	 * @return
	 */
	int save(SysUser sysuser);
	
	/**
	 * 用户管理
	 * @return
	 */
	JSONArray toUserAdmin();
	
	
	
}
