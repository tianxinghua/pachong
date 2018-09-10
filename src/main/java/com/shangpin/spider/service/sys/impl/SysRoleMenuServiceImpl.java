package com.shangpin.spider.service.sys.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shangpin.spider.mapper.sys.SysRoleMenuMapper;
import com.shangpin.spider.service.sys.SysRoleMenuService;


@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SysRoleMenuServiceImpl implements SysRoleMenuService{
	private static final Logger LOG = LoggerFactory.getLogger(SysRoleMenuServiceImpl.class);
	@Autowired
	private SysRoleMenuMapper sysRoleMenuMapper;
	
	@Override
	public void editRolePermission(Integer roleId, String per,
			Map<String, Integer> permissionMap) throws Exception{
		sysRoleMenuMapper.delPerByRoleId(roleId);
		if (!"".equals(per)) {
			String[] perIds = per.split(",");
			for (String perIdStr : perIds) {
				Integer perId = permissionMap.get(perIdStr);
				sysRoleMenuMapper.addPermissionRole(roleId, perId);
			}
		}
		LOG.info("----修改角色权限！");

	}
}
