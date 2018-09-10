package com.shangpin.spider.service.sys.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.mapper.sys.SysUserMapper;
import com.shangpin.spider.service.sys.SysRoleService;
import com.shangpin.spider.service.sys.SysUserService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SysUserServiceImpl implements SysUserService {
	private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Override
	public SysUser getByUserName(String username) {
		return sysUserMapper.getByUsername(username);
	}
	
	@Override
	public SysUser getByUserNameAndPwd(String username,String password) {
		return sysUserMapper.getByUsernameAndPwd(username,password);
	}

	@Override
	public int save(SysUser sysuser) {
		return sysUserMapper.insertSelective(sysuser);
	}
	
	@Override
	public JSONArray toUserAdmin() {
		JSONArray array = new JSONArray();
		List<SysUser> userList = null;
		userList = sysUserMapper.selectAll();
		for (SysUser userInfo : userList) {
			JSONObject obj = new JSONObject();
			obj.put("userId", userInfo.getId());
			obj.put("username", userInfo.getUsername());
			List<SysRole> roleList = sysRoleService.getRoleByUserId(userInfo.getId());
			if (roleList != null && roleList.size() > 0) {
				String role = "";
				for (SysRole sysRole : roleList) {
					role += "," + sysRole.getName();
				}
				obj.put("role", role.substring(1, role.length()));
			} else {
				obj.put("role", "无");
			}
			array.add(obj);

		}
		LOG.info("---用户管理！");
		return array;
	}
}
