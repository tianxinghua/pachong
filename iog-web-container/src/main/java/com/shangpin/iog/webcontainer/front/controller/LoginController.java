/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.iog.webcontainer.front.conf.MenuDTO;
import com.shangpin.iog.webcontainer.front.conf.UAASUtil;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月1日
 */
@Controller
@RequestMapping
public class LoginController {
	
	@RequestMapping(value="/login",method={RequestMethod.POST})
	public ModelAndView login(@RequestParam String token,HttpSession session){
		ModelAndView mv = new ModelAndView("redirect:download/view");
		boolean valid=UAASUtil.isValid(token);
		if(valid){
			session.setAttribute(UAASUtil.TOKEN, token);
			session.setAttribute(UAASUtil.APPMENUSET, toUrISet(UAASUtil.findAPPMenus(token)));
			session.setMaxInactiveInterval(UAASUtil.TOKEN_OUT_SEC);
		}else{
			mv.setViewName("redirect:"+UAASUtil.redirectUAAS(null));
		}
		return mv;
	}
	
	/**
	 * 获取授权的uri
	 * @param menus
	 * @return
	 */
	private Set<String> toUrISet(List<MenuDTO> menus) {
		Set<String> set = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(menus))
			for (Iterator<MenuDTO> iterator = menus.iterator(); iterator
					.hasNext();) {
				MenuDTO dto = iterator.next();
				set.add(dto.getUrl());
			}
		return set;
	}

	@RequestMapping("/logout")
	public String loginOut(HttpSession session){
		UAASUtil.logout((String)session.getAttribute(UAASUtil.TOKEN));
		session.removeAttribute(UAASUtil.TOKEN);
		return "redirect:"+UAASUtil.redirectUAAS(null);
	}
}
