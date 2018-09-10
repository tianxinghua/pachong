package com.shangpin.spider.utils.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONArray;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;

/** 
 * @author  njt 
 * @date 创建时间：2018年1月12日 上午11:42:11 
 * @version 1.0 
 * @parameter  
 */

public class ContentRole {
	public static JSONArray queryRole(){
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
		List<SysRole> roleList = user.getRoleList();
		JSONArray roleArray = new JSONArray();
		for (SysRole sysRole : roleList) {
			roleArray.add(sysRole.getRole());
		}
		return roleArray;
	}
}
