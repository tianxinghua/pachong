package com.shangpin.api.airshop.filter;

import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.common.utils.FastJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 跨站访问过滤（指定必须是尚品的网站才能访问），目前没有控制ip
 * @description 
 * @author 陈小峰
 * <br/>2015年9月10日
 */
public class LoginFiter extends OncePerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(LoginFiter.class);

	public LoginFiter() {
		super();
	}
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String reqUri=request.getRequestURI();
		
		logger.info("uri:"+reqUri);
		//登录,验证码 放行
		if (reqUri.endsWith("/login") || reqUri.endsWith("/captcha")|| reqUri.contains("/captcha/check/")
				|| reqUri.contains("/favicon.ico")
				|| reqUri.contains("/statusCheck")
				|| reqUri.contains("/check-username")
				|| reqUri.contains("/getVerificationCode")
				|| reqUri.contains("/checkVerCodeForFind")
				|| reqUri.contains("/changePassword")
					|| reqUri.contains("/setPwdForFind")
				
				|| reqUri.contains("/getTestMessage")||reqUri.contains("/wayprint/printPIDAndSupplierOrderNo")||reqUri.contains("/wayprint/print")
				) {
			logger.info("success");
			chain.doFilter(request, response);
			return;
		}
		HttpSession session = request.getSession();
		UserInfo user = (UserInfo) session.getAttribute(Constants.SESSION_USER);

//测试代码
		/*UserInfo user1 = new UserInfo();*/
//		user.setSopUserNo("2015121100228");
//		user.setSopUserNo("2015101000210");
//		user.setSopUserNo("2015101300211");
		session.setAttribute(Constants.SESSION_USER,user);

		if (user == null) {
			logger.info("请求{}未登录!",reqUri);
			String alert = FastJsonUtil.serialize2String(ResponseContentOne.errorResp("3","Not logged in !"));
			response.getWriter().print(alert);
			return;
		}
        chain.doFilter(request, response);
	}
	
}
