package com.shangpin.spider.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/*import com.constants.Constants;
import com.xr.gather.api.CategoryInfoService;
import com.xr.gather.api.NewsGroupCategoryService;
import com.xr.gather.api.NewsGroupService;
import com.xr.gather.api.NewsInfoService;
import com.xr.gather.api.NewsTopInfoService;
import com.xr.gather.api.SpiderRuleService;
import com.xr.gather.dto.commons.Result;
import com.xr.gather.model.CategoryInfo;
import com.xr.gather.model.NewsGroup;
import com.xr.gather.model.NewsInfo;
import com.xr.sys.model.SysUser;
import com.xr.sys.shiro.util.ContentRole;*/

/** 
 * @author  njt 
 * @date 创建时间：2017年12月1日 下午4:10:59 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("/news")
public class NewsController {/*
	private Logger log = LoggerFactory.getLogger(NewsController.class);
	@Autowired
	private NewsInfoService newsInfoService;
	@Autowired
	private SpiderRuleService spiderInfoService;
	@Autowired
	private NewsGroupService newsGroupService;
	@Autowired
	private NewsTopInfoService newsTopInfoService;
	@Autowired
	private CategoryInfoService newsCategoryService;
	@Autowired
	private NewsGroupCategoryService newsGroupCategoryService;
	
	
	@RequestMapping("toNewsInfo")
	public String toNewsInfo(Model model){
		List<CategoryInfo> categoryArray = newsCategoryService.selectAll();
		JSONArray whiteArray = newsInfoService.getWhiteInfo();
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		model.addAttribute("categoryArray", categoryArray);
		model.addAttribute("whiteArray", whiteArray);
		return "news/newsInfo";
	}
	
	@RequestMapping("getNewsInfoList")
	@ResponseBody
	public Result<NewsInfo> getNewsInfoList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer rows, Long whiteId, String newsTitle, String startTime, String endTime ,Long category, int status){
		Result<NewsInfo> result = newsInfoService.getNewsInfoList(page,rows,whiteId,newsTitle,startTime,endTime,category,status);
		return result;
	}
	
	@RequestMapping("editNewsInfo")
	@ResponseBody
	public JSONObject editNewsInfo(String oper,NewsInfo newsInfo){
		JSONObject resultObj = new JSONObject();
		if (Constants.DEL.equals(oper)) {
		      try {
		    	//newsInfoService.delNewsInfo(newsInfo);
		        resultObj.put("status", "success");
		      } catch (Exception e) {
		        resultObj.put("status", "error");
		        e.printStackTrace();
		        log.error("----删除新闻数据有误！id="+newsInfo.getId());
		      }
		}
		return resultObj;

	}
	
	@RequestMapping("forbidNewsInfo")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public JSONObject forbidNewsInfo(NewsInfo newsInfo){
		JSONObject resultObj = new JSONObject();
		try {
			newsInfoService.forbidNewsInfo(newsInfo.getId());
			resultObj.put("status", "success");
		} catch (Exception e) {
			resultObj.put("status", "error");
	        e.printStackTrace();
	        log.error("----删除新闻数据有误！id="+newsInfo.getId());
		}
		return resultObj;
	}
	
	//------------------------------资讯板块
	@RequestMapping("toNewsGroup")
	public String toNewsGroup(Model model){
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "news/newsGroup";
	}
	
	@RequestMapping("getNewsGroup")
	@ResponseBody
	public List<NewsGroup> getNewsGroup(){
		List<NewsGroup> groupList = newsGroupService.getNewsGroupList();
		return groupList;
	}
	
	@RequestMapping("editNewsGroup")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public JSONObject editNewsGroup(String oper,NewsGroup newsGroup){
		JSONObject resultObj = new JSONObject();
		if (Constants.ADD.equals(oper)) {
		      try {
		    	String msg = newsGroupService.addNewsGroup(newsGroup);
		        resultObj.put("status", msg);
		      } catch (Exception e) {
		        resultObj.put("status", "error");
		        e.printStackTrace();
		        log.error("----添加资讯板块有误！");
		      }
		}
		return resultObj;
	}
	
	@RequestMapping("stopNewsGroup")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public int stopNewsGroup(int id,int status){
		try {
			newsGroupService.stopNewsGroup(id,status);
			return 1;
		} catch (Exception e) {
			log.info("停用板块出错!"+e.getMessage());
			return 0;
		}
		
		
	}
	
	@RequestMapping("startNewsGroup")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public int startNewsGroup(int id){
		try {
			int msg = newsGroupService.startNewsGroup(id);
			return msg;
		} catch (Exception e) {
			log.info("启动板块出错"+e.getMessage());
			return 0;
		}
		
		
	}
	
	@RequestMapping("queryGategoryByGroupId")
	@ResponseBody
	public JSONObject queryGategoryByGroupId(int id){
		try {
			JSONObject obj =  newsGroupService.queryGategoryByGroupId(id);
			return obj;
		} catch (Exception e) {
			log.info("查询板块相关分类出错"+e.getMessage());
		}
		return null;
		
	}
	
	@RequestMapping("delCategoryGroupId")
	@ResponseBody
	public String delCategoryGroupId(int id){
		String msg = newsGroupCategoryService.delCategoryGroupId(id);
		return msg;
	}
	
	@RequestMapping("addCategoryGroupId")
	@ResponseBody
	public String addCategoryGroupId(int groupId,String categoryIdStr){
		String msg = newsGroupCategoryService.addCategoryGroupId(groupId,categoryIdStr);
		return msg;
	}
	
	//------------------------------------分类库
	
	@RequestMapping("toNewsCategory")
	public String toNewsCategory(Model model){
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "news/newsCategory";
	}
	
	@RequestMapping("getNewsCategoryList")
	@ResponseBody
	public Result<CategoryInfo> getNewsCategoryList(@RequestParam(defaultValue = "1")Integer page, 
			@RequestParam(defaultValue = "10")Integer rows, String categoryName){
		Result<CategoryInfo> result = newsCategoryService.getCategoryList(page,rows,categoryName);
		return result;
	}
	
	@RequestMapping("editCategory")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public JSONObject editCategory(String oper, CategoryInfo categoryInfo){
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		categoryInfo.setAddUserId(user.getId());
		categoryInfo.setAddUsername(user.getName());
		JSONObject resultObj = new JSONObject();
	    if (Constants.EDIT.equals(oper)) {
	      try {
	    	String msg = newsCategoryService.updateCategory(categoryInfo);
	        resultObj.put("status", msg);
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        log.error("----修改分类有误！");
	      }
	    }

	    if (Constants.ADD.equals(oper)) {
	      try {
	    	String msg = newsCategoryService.addCategory(categoryInfo);
	        resultObj.put("status", msg);
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        log.error("----新增分类有误！");
	      }
	    }
	    return resultObj;
	}
	
	@RequestMapping("editCategoryStatus")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	@ResponseBody
	public String editCategoryStatus(Long categoryId,int status){
		try {
			return newsCategoryService.editCategoryStatus(categoryId,status);
		} catch (Exception e) {
			log.info("更改分类状态出错!"+e.getMessage());
			return Constants.FAIL;
		}
		
		
	}
	
	//------------------------------------置顶资讯列表
	@RequestMapping("toTopNewsInfo")
	@RequiresRoles(value = {"super_admin","admin"},logical = Logical.OR)
	public String toTopNewsInfo(Integer id,String name,Model model){
		JSONObject obj = newsInfoService.getTopNewsInfoByGroupId(id);
		model.addAttribute("id", id);
		model.addAttribute("name", name);
		if(obj==null){
			model.addAttribute("obj", "");
		}else{
			model.addAttribute("obj", obj);
		}
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "news/topNewsInfo";
	}
	
	
	@RequestMapping("getTopNewsInfoList")
	@ResponseBody
	public Result<NewsInfo> getTopNewsInfoList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer rows, String newsTitle, Long groupId){
		Result<NewsInfo> result = newsInfoService.getTopNewsInfoList(page,rows,newsTitle,groupId);
		return result;
	}
	
	@RequestMapping("setTopNewsInfo")
	@ResponseBody
	public String setTopNewsInfo(Integer groupId,Long newsInfoId){
		String msg = newsTopInfoService.setTopNewsInfo(groupId,newsInfoId);
		return msg;
	}
	
	@RequestMapping("delTopNewsInfo")
	@ResponseBody
	public String delTopNewsInfo(Integer groupId){
		String msg = newsTopInfoService.delTopNewsInfo(groupId);
		return msg;
	}
	
	
	
*/}
