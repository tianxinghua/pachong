package com.shangpin.spider.service.sys.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shangpin.spider.entity.sys.SysUserRole;
import com.shangpin.spider.mapper.sys.SysUserRoleMapper;
import com.shangpin.spider.service.sys.SysUserRoleService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
@Cacheable("ur:")
public class SysUserRoleServiceImpl implements  SysUserRoleService{

	private static final Logger LOG = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);
	
	@Autowired
	private SysUserRoleMapper UserRoleMapper;
	
	@Override
	public int save(SysUserRole userRole) {
		return UserRoleMapper.insertSelective(userRole);
	}
	
	@Override
	public void editRole(Integer uid, String role, Map<String, Integer> roleMap) throws Exception{
		UserRoleMapper.delRoleByUid(uid);
		if (!"".equals(role)) {
			String[] roleIds = role.split(",");
			for (String roleIdStr : roleIds) {
				Integer roleId = roleMap.get(roleIdStr);
				SysUserRole userRole = new SysUserRole(uid,roleId);
				save(userRole);
			}
		}
		LOG.info("----修改用户角色！");
	}

}
