package com.shangpin.spider.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.service.sys.RegisterService;
import com.shangpin.spider.utils.shiro.Base64;

/**
 * @author njt
 * @date 创建时间：2017年11月9日 上午10:23:46
 * @version 1.0
 * @parameter
 */
@Controller
@RequestMapping("/")
public class LoginController {
	private Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RegisterService registerService;

	@GetMapping("/login")
	public String login(Model model) {
		log.info("进入登录页面");
		return "login";
	}

	@RequestMapping("/logout")
	public String logout() {
		log.info("退出成功");
		SecurityUtils.getSubject().logout();
		return "login";
	}

	@PostMapping("/login/submit")
	public String loginSubmit(HttpServletRequest request,String username, String password, @RequestParam(required = false) String code,
			Model model) {
		log.info("登录认证");

		if (StringUtils.isEmpty(username)) {
			log.info("用户名为空");
			model.addAttribute("msg", "请输入用户名");
			return "login";
		}

		if (StringUtils.isEmpty(password)) {
			log.info("密码为空");
			model.addAttribute("msg", "请输入密码");
			return "login";
		}
		UsernamePasswordToken token = new UsernamePasswordToken(username, Base64.decoder(password));

		try {
			Subject currentUser = SecurityUtils.getSubject();

			if (currentUser == null) {
				model.addAttribute("msg", "无此用户");
				return "login";
			}
			currentUser.login(token);
			
//			request.getSession().setAttribute("test_session", 111111);
			
			if (currentUser.isAuthenticated()) {
				log.info("登录成功！");
				return "redirect:/manager/index";
			}
		} catch (UnknownAccountException e) {
			log.error("对用户[{}]进行登录验证..验证未通过,未知账户: {}",username,e.getMessage());
			model.addAttribute("msg", "未知账户");
		} catch (IncorrectCredentialsException e) {
			log.error("对用户[{}]进行登录验证..验证未通过,错误的凭证:{}",username,e.getMessage());
			model.addAttribute("msg", "密码错误");
		} catch (LockedAccountException e) {
			log.error("对用户[{}]进行登录验证..验证未通过,账户已锁定:{}",username,e.getMessage());
			model.addAttribute("msg", "账户已锁定");
		} catch (ExcessiveAttemptsException e) {
			log.error("对用户[{}]进行登录验证..验证未通过,错误次数过多:{}",username,e.getMessage());
			model.addAttribute("msg", "错误次数过多，请稍后重试");
		} catch (AuthenticationException e) {
			// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			log.error("对用户[{}]进行登录验证..验证未通过,堆栈轨迹如下:{}",username,e.getMessage());
			model.addAttribute("msg", "用户名或密码错误");
		}
		return "login";

	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register/submit")
	public String registerSubmit(SysUser sysuser, Model model) {
		try{
			SysUser user = registerService.getByUserName(sysuser.getUsername());
			if (user != null) {
				model.addAttribute("msg", "用户已经存在，不允许注册！");
			}
			sysuser.setPassword(Base64.decoder(sysuser.getPassword()));
			int rows = registerService.doRegister(sysuser);
			if (rows > 0) {
				log.info("注册成功！");
				model.addAttribute("msg", "注册成功！");
			}
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("msg", "用户注册失败！");
		}
		return "register";
	}

}
