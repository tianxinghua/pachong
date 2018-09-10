package com.shangpin.spider.service.sys.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.spider.entity.sys.SysMenu;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.service.sys.AuthorityService;
import com.shangpin.spider.service.sys.SysMenuService;
import com.shangpin.spider.service.sys.SysRoleService;
import com.shangpin.spider.service.sys.SysUserService;

/**
 * @author njt
 * @date 创建时间：2017年11月10日 下午4:03:31
 * @version 1.0
 * @parameter
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

	private Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysMenuService sysMenuService;

	@Override
	public SysUser getByUserName(String username) {
		log.info("查找用户信息——AuthorityServiceImpl.findByUsername()");
		SysUser user = sysUserService.getByUserName(username);
		if (user == null) {
			return null;
		}
		List<SysRole> listRole = getRoleByUserId(user.getId());
		if (listRole != null) {
			for (SysRole sysRole : listRole) {
				/*if(sysRole.getRoleType().equals(RoleEnum.ROLE_TYPE_0.getType()) 
						|| sysRole.getRoleType().equals(RoleEnum.ROLE_TYPE_SUPER.getType())) {
					sysRole.setMenu(sysMenuService.selectAll());
					break;
				}*/
				log.info("查找用户角色权限信息——AuthorityServiceImpl.getPermissionByRoleId()");
				List<SysMenu> listPermission = sysMenuService.getPermissionByRoleId(sysRole
						.getId());
				if (listPermission != null) {
					sysRole.setMenuList(listPermission);
				} else {
					log.warn(sysRole.getId() + "无权限！");
				}
			}
			user.setRoleList(listRole);
		} else {
			log.warn(user.getId() + "无角色！");
		}

		return user;
	}
	
	public List<SysRole> getRoleByUserId(Integer uid) {
		log.info("查找用户角色信息——AuthorityServiceImpl.getRoleByUserId()");
		return sysRoleService.getRoleByUserId(uid);
	}
	
}
