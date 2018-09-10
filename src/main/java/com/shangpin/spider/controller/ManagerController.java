package com.shangpin.spider.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.sys.SysMenu;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.service.sys.SysMenuService;
import com.shangpin.spider.service.sys.SysRoleMenuService;
import com.shangpin.spider.service.sys.SysRoleService;
import com.shangpin.spider.service.sys.SysUserRoleService;
import com.shangpin.spider.service.sys.SysUserService;

/**
 * @author njt
 * @date 创建时间：2017年11月9日 上午10:23:46
 * @version 1.0
 * @parameter
 */
@Controller
@RequestMapping("manager")
@RequiresAuthentication
public class ManagerController {
	private Logger log = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	private static Map<String, Integer> roleMap = new HashMap<String, Integer>();

	private static Map<String, Integer> menuMap = new HashMap<String, Integer>();
	
//	private static String currentRole = "";
	/**
	 * @RequiresPermissions(value = { "index" })
	 **/
	@GetMapping("/index")
	public String index(Model model) {
		try {
			log.info("进入管理页面");
			Subject subject = SecurityUtils.getSubject();
			SysUser user = (SysUser) subject.getPrincipal();

			String username = user.getUsername();
			log.info("当前用户为:" + username);
			Set<SysMenu> set = new HashSet<SysMenu>();
			for(SysRole role : user.getRoleList()) {
				if(role.getMenuList()!= null && role.getMenuList().size() > 0) {
					set.addAll(role.getMenuList());
				}
			}

			model.addAttribute("user", user);
			model.addAttribute("menuList", set);
			return "index";
		} catch (Exception e) {
			log.info("授权失败,请登录");
			return "redirect:/login";
		}

	}

	@RequestMapping("userAdmin")
	@RequiresPermissions("userAdmin")
	public String toUserAdmin(Model model) {
		JSONArray array = sysUserService.toUserAdmin();
		JSONArray arrayRole = sysRoleService.getRoleAll();
		for (Object object : arrayRole) {
			JSONObject obj2 = JSONObject.parseObject(object + "");
			Integer roleId = Integer.parseInt(obj2.get("roleId") + "");
			String roleName = obj2.get("roleName") + "";
			roleMap.put(roleName, roleId);
		}
		model.addAttribute("array", array);
		model.addAttribute("arrayRole", arrayRole);
		return "userPage";
	}

	@RequestMapping("editUserRole")
	@RequiresPermissions("userAdmin")
	@ResponseBody
	public JSONObject editUserRole(String oper, Integer userId, String role) {
		JSONObject resultObj = new JSONObject();
		try {
			if (Constants.EDIT.equals(oper)) {
				sysUserRoleService.editRole(userId, role, roleMap);
			}
			resultObj.put("status", "success");
		} catch (Exception e) {
			resultObj.put("status", "error");
			e.printStackTrace();
		}
		return resultObj;
	}

	@RequestMapping("roleAdmin")
	@RequiresPermissions("roleAdmin")
	public String toRoleAdmin(Model model) {
		JSONArray array = sysRoleService.toRolePage();
		JSONArray arrayPermission = sysMenuService.getPermissionAll();
		for (Object object : arrayPermission) {
			JSONObject obj2 = JSONObject.parseObject(object + "");
			Integer perId = Integer.parseInt(obj2.get("perId") + "");
			String perName = obj2.get("perName") + "";
			menuMap.put(perName, perId);
		}
		model.addAttribute("array", array);
		model.addAttribute("arrayPermission", arrayPermission);
		return "rolePage";
	}

	@RequestMapping("editRolePermission")
	@RequiresPermissions("roleAdmin")
	@ResponseBody
	public JSONObject editRolePermission(String oper, String roleId, String per) {
		JSONObject resultObj = new JSONObject();
		try {
			if (Constants.EDIT.equals(oper)) {
				Integer roleIdInt = Integer.parseInt(roleId);
				sysRoleMenuService.editRolePermission(roleIdInt, per, menuMap);
			}
			resultObj.put("status", "success");
		} catch (Exception e) {
			resultObj.put("status", "error");
			e.printStackTrace();
		}
		return resultObj;
	}
	
}
