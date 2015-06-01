/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangpin.iog.webcontainer.front.conf.UAASUtil;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月1日
 */
@Controller
@RequestMapping
public class LoginController {
	
	@RequestMapping("/loging")
	public String loginPage(){
		return "login";
	}
	
	@RequestMapping(value="/login",method={RequestMethod.POST})
	@ResponseBody
	public Map<String,String> login(@RequestParam(required=false) String userName,
			@RequestParam(required=false) String password,HttpSession session){
		Map<String,String> rtn= new HashMap<>();
		String token=UAASUtil.login(userName, password);
		if(token!=null){
			rtn.put("url", "download/view");
			session.setAttribute(UAASUtil.TOKEN, token);
		}else
			rtn.put("err", "登录失败");
		return rtn;
	}
	@RequestMapping("/logout")
	public String loginOut(HttpSession session){
		UAASUtil.logout((String)session.getAttribute(UAASUtil.TOKEN));
		session.removeAttribute(UAASUtil.TOKEN);
		return "redirect:loging";
	}
}
