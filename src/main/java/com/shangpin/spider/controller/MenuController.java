package com.shangpin.spider.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/*
import com.xr.gather.gather.task.TaskManager;
import com.xr.gather.model.SpiderTaskInfo;
import com.xr.sys.model.SysMenu;
import com.xr.sys.service.SysMenuService;*/

/**
 * 菜单相关接口
 * @author lenovo
 */
@Controller
@RequestMapping("/menu")
public class MenuController {/*
	private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private SysMenuService sysMenuService;
	
	@GetMapping("/list")
	public String list(SysMenu menu,Model model){
		List<SysMenu> list = sysMenuService.selectByMenu(menu);
		model.addAttribute("menuList", list);
		return "menu/list";
	}
		
	class MenuNode{
		private SysMenu parent;
		private SysMenu prev;
		private SysMenu current;
		private SysMenu next;
		private List<SysMenu> children;
	}
*/}
