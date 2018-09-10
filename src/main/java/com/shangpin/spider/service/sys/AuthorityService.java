package com.shangpin.spider.service.sys;

import com.shangpin.spider.entity.sys.SysUser;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月10日 下午4:02:54 
 * @version 1.0 
 * @parameter  
 */

public interface AuthorityService {
	/**
	 * 通过用户名 获取用户信息
	 * @param username
	 * @return
	 */
	SysUser getByUserName(String username);

}
