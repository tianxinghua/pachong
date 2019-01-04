/**
 * 
 */
package com.shangpin.iog.web.conf.interceptor;

import com.shangpin.framework.page.SystemContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年2月4日
 */
public class PageInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String p=request.getParameter("page");if(StringUtils.isNotBlank(p)) SystemContext.setPage(Integer.parseInt(p));
		String pz=request.getParameter("pageSize");if(StringUtils.isNotBlank(pz))
            SystemContext.setPage(Integer.parseInt(pz));

		return true;
	}
}
