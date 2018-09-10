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
import com.shangpin.spider.entity.sys.SysMenu;
import com.shangpin.spider.mapper.sys.SysMenuMapper;
import com.shangpin.spider.service.sys.SysMenuService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
@CacheConfig(cacheNames="menus")
public class SysMenuServiceImpl implements SysMenuService{
	private static final Logger LOG = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	@Autowired
	private SysMenuMapper menuMapper;
	
	@Override
	@CachePut
	public int save(SysMenu record) {
		return menuMapper.insertSelective(record);
	}
	
	@CachePut
	@Override
	public int update(SysMenu record) {
		return menuMapper.updateByPrimaryKeySelective(record);
	}

	
	@Override
	@Cacheable  
	public List<SysMenu> getPermissionByRoleId(Integer roleId) {
		return menuMapper.getPermissionByRoleId(roleId);
	}

	@Override
	@Cacheable  
	public List<SysMenu> selectAll() {
		return menuMapper.selectAll();
	}

	@Override
	@Cacheable  
	public List<SysMenu> selectByMenu(SysMenu menu) {
		return menuMapper.selectByMenu(menu);
	}
	
	@Override
	public JSONArray getPermissionAll() {
		JSONArray array = new JSONArray();
		List<SysMenu> perList = menuMapper.selectAll();
		if (perList != null && perList.size() > 0) {
			for (SysMenu sysper : perList) {
				JSONObject obj = new JSONObject();
				obj.put("perId", sysper.getId());
				obj.put("perName", sysper.getName());
				array.add(obj);
			}

		}
		LOG.info("----获取所有的权限！");
		return array;
	}
	
}
