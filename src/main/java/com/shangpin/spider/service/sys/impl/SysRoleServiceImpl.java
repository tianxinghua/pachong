package com.shangpin.spider.service.sys.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.mapper.sys.SysRoleMapper;
import com.shangpin.spider.service.sys.SysRoleService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
@CacheConfig(cacheNames="roles")
public class SysRoleServiceImpl implements SysRoleService{
	private static final Logger LOG = LoggerFactory.getLogger(SysRoleServiceImpl.class);

	@Autowired
	private SysRoleMapper roleMapper;
	
	/*@Autowired
	private SysMenuService sysMenuService;*/
	
	@Cacheable
	@Override
	public List<SysRole> getRoleByUserId(Integer userId) {
		return roleMapper.getRoleByUserId(userId);
	}
	@Override
	@CachePut
	public int save(SysRole record) {
		return roleMapper.insertSelective(record);
	}
	
	@CachePut
	@Override
	public int update(SysRole record) {
		return roleMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public JSONArray getRoleAll() {
		JSONArray array = new JSONArray();
		List<SysRole> roleList = roleMapper.getRoleAll();
		if (roleList != null && roleList.size() > 0) {
			for (SysRole sysRole : roleList) {
				JSONObject obj = new JSONObject();
				obj.put("roleId", sysRole.getId());
				obj.put("roleName", sysRole.getName());
				array.add(obj);
			}

		}
		LOG.info("----获取所有角色！");
		return array;
	}
	
	@Override
	public JSONArray toRolePage() {
		JSONArray array = new JSONArray();
		/*List<SysRole> roleList = roleMapper.getRoleAll();
		for (SysRole role : roleList) {
			JSONObject obj = new JSONObject();
			obj.put("roleId", role.getId());
			obj.put("roleName", role.getName());
			List<SysMenu> perList = sysMenuService.getPermissionByRoleId(role
					.getId());
			if (perList != null && perList.size() > 0) {
				StringBuffer perStr = new StringBuffer();
				int i = 0;
				for (SysMenu sysmenu : perList) {
					i++;
					perStr.append("<span>");
					perStr.append(sysmenu.getName());
					perStr.append("</span>&nbsp;&nbsp;|");
					if (i % 5 == 0) {
						perStr.append("<br>");
					}
				}
				obj.put("per", perStr.toString());
			} else {
				obj.put("per", "无");
			}
			array.add(obj);

		}*/
		return array;
	}
}
