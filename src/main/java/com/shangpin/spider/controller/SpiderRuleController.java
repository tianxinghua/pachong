package com.shangpin.spider.controller;

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
import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;
import com.shangpin.spider.service.gather.SpiderRulesService;
import com.shangpin.spider.service.gather.SpiderWhiteService;
import com.shangpin.spider.utils.shiro.ContentRole;


/** 
 * @author  njt 
 * @date 创建时间：2017年11月17日 上午9:49:21 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("/spider")
public class SpiderRuleController {
	private Logger log = LoggerFactory.getLogger(SpiderRuleController.class);
	
	@Autowired
	private SpiderRulesService spiderRulesService;
	@Autowired
	private SpiderWhiteService spiderWhiteService;
	/*@Autowired
	private SpiderProxyInfoService spiderProxyService;*/
	
	/*@Autowired
	private CategoryInfoService categoryService;*/
	
	@RequestMapping("toWebSitePage")
	public String toWebSitePage(Model model){
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "spider/webSite";
	}
	
	@RequestMapping("getWebSiteList")
	@ResponseBody
	public Result<SpiderWhiteInfo> getWebSiteList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10")Integer rows){
		Result<SpiderWhiteInfo> result = spiderWhiteService.getWebSiteList(page,rows);
		return result;
	}
	
	@RequestMapping("getRuleById")
	@ResponseBody
	public JSONObject getRuleById(Long id){
		JSONObject obj = spiderRulesService.getRuleById(id);
		/*JSONArray proxyArray = spiderProxyService.getProxyInfo();
		List<CategoryInfo> categoryArray = categoryService.selectAll();
		if(proxyArray!=null){
			obj.put("proxyArray", proxyArray);
		}else{
			obj.put("proxyArray", "");
		}
		if(categoryArray!=null){
			obj.put("categoryArray", categoryArray);
		}else{
			obj.put("categoryArray", "");
		}*/
		return obj;
	}
	
	@RequestMapping("saveWebRule")
	@ResponseBody
	public int saveWebRule(SpiderRules spiderRuleInfo,Long whiteInfoId,Long spiderRuleId){
		int result = spiderRulesService.saveWebRule(spiderRuleInfo,whiteInfoId,spiderRuleId);
		return result;
	}
	
	@RequestMapping("queryRuleId")
	@ResponseBody
	public JSONObject queryRuleId(Long id){
		JSONObject object = spiderRulesService.queryRuleId(id);
		return object;
	}
	
	@RequestMapping("editWebSite")
	@ResponseBody
	public JSONObject editWebSite(String oper,SpiderWhiteInfo whiteInfo){
		JSONObject resultObj = new JSONObject();
		
		if (Constants.EDIT.equals(oper)) {
		      try {
		    	  spiderWhiteService.updateWhiteInfo(whiteInfo);
		        resultObj.put("status", "success");
		      } catch (Exception e) {
		        resultObj.put("status", "error");
		        e.printStackTrace();
		        log.error("----修改源网站有误！");
		      }
		}
		
		if (Constants.ADD.equals(oper)) {
			try {
				resultObj.put("status", "success");
				spiderWhiteService.addWhiteInfo(whiteInfo);
			} catch (Exception e) {
				resultObj.put("status", "error");
				e.printStackTrace();
				log.error("----添加源网站有误！");
			}
		}

		return resultObj;

	}
	
	/*@RequestMapping("toIpProxy")
	public String toIpProxy(){
		return "spider/ipProxy";
	}
	
	@RequestMapping("ipProxyList")
	@ResponseBody
	public Result<SpiderProxyInfo> ipProxyList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10")Integer rows){
		Result<SpiderProxyInfo> result = spiderProxyService.ipProxyList(page,rows);
		return result;
	}
	
	@RequestMapping("editIpProxy")
	@ResponseBody
	public JSONObject editIpProxy(String oper, SpiderProxyInfo proxyInfo){
		JSONObject resultObj = new JSONObject();
	    if (Constants.EDIT.equals(oper)) {
	      try {
	    	  spiderProxyService.updateIpProxy(proxyInfo);
	        resultObj.put("status", "success");
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        log.error("----修改IP代理有误！");
	      }
	    }

	    if (Constants.ADD.equals(oper)) {
	      try {
	    	  spiderProxyService.addIpProxy(proxyInfo);
	    	  resultObj.put("status", "success");
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        log.error("----新增IP代理有误！");
	      }
	    }
	    return resultObj;
	}
	*/
}
