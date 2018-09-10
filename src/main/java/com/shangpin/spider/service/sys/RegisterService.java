package com.shangpin.spider.service.sys;

import com.shangpin.spider.entity.sys.SysUser;

public interface RegisterService {
	/**
	 * 通过用户名查找用户是否存在
	 * @param username
	 * @return
	 */
	SysUser getByUserName(String username);
	/**
	 * 用户注册
	 * @param username
	 * @param password
	 */
	int doRegister (SysUser sysuser) throws Exception;

}
