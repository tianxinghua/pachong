package com.shangpin.spider.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
/*import com.xr.gather.gather.task.TaskManager;
import com.xr.gather.model.SpiderTaskInfo;
import com.xr.sys.shiro.util.ContentRole;*/

/** 
 * @author  njt 
 * @date 创建时间：2017年12月5日 下午2:59:17 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("task")
public class TaskController {/*
	private Logger log = LoggerFactory.getLogger(TaskController.class);
	@Autowired
	private TaskManager taskManager;
	
	@RequestMapping("toTaskList")
	public String toTaskList(Model model){
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "spider/taskList";
	}
	
	@RequestMapping("getTaskList")
	@ResponseBody
	public List<SpiderTaskInfo> getTaskList(){
		log.info("----获取爬虫列表");
		List<SpiderTaskInfo> taskList = null;
		try {
			taskList = taskManager.taskList();
		} catch (Exception e) {
			log.info("----获取爬虫列表出错！"+e.getMessage());
		}
		return taskList;
	}
	
	@RequestMapping("deleteTask")
	@ResponseBody
	public int deleteTask(String name){
		log.info("----删除相应爬虫name:"+name);
		try {
			taskManager.deleteTask(name);
			return 1;
		} catch (Exception e) {
			log.info("----删除相应爬虫name:"+name+e.getMessage());
			return 0;
		}
		
	}
	
	@RequestMapping("editTask")
	@ResponseBody
	public int editTask(String uuid,String name){
		log.info("----编辑相应爬虫uuid："+uuid+"  name:"+name);
		try {
			taskManager.edit(uuid, name);
			return 1;
		} catch (Exception e) {
			log.info("----编辑相应爬虫uuid："+uuid+"  name:"+name+"出错---"+e.getMessage());
			return 0;
		}
		
	}
	
	@RequestMapping("forceQuitTask")
	@ResponseBody
	public int forceQuitTask(String uuid,String name){
		log.info("----强制退出爬虫uuid："+uuid+"  name:"+name);
		try {
			taskManager.forceQuit(uuid, name);
			return 1;
		} catch (Exception e) {
			log.info("----强制退出爬虫uuid："+uuid+"  name:"+name+"出错---"+e.getMessage());
			return 0;
		}
		
	}
	
*/}
