
package com.shangpin.iog.webcontainer.front.conf.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Date:     2015-2-5 下午10:18:42 <br/>
 * Description: 描述文档作用<br/>
 * @author   陈小峰
 * @version  
 * @since    JDK 1.6	 
 */
public class TokenInterceptor implements HandlerInterceptor {
	private static Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
	private static final String MVC_TOKEN = "_token_";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		 HandlerMethod handlerMethod = (HandlerMethod) handler;
         Method method = handlerMethod.getMethod();
         Token tk=method.getAnnotation(Token.class);
         if(tk!=null){
        	 if(tk.consumer()){//处理action验证是否重复提交
        		 if(repeatSubmit(request)){
        			 log.warn("repeat submit,url:"+request.getServletPath());
        			 request.getRequestDispatcher("/WEB-INF/error/repeat.jsp").forward(request, response);
        			 return false;
        		 }
        		 request.getSession().removeAttribute(MVC_TOKEN);
        	 }
        	 if(tk.producer()){//处理action设置token
        		 request.getSession().setAttribute(MVC_TOKEN,UUID.randomUUID().toString());
        	 }
         }
		return true;
	}

	private boolean repeatSubmit(HttpServletRequest request) {
		String token=(String)request.getSession().getAttribute(MVC_TOKEN);
		if(token==null){
			 return true;
		 }
		String requestTkn=request.getParameter(MVC_TOKEN);
		if(token==null||requestTkn==null||!token.equals(requestTkn)){
			return true;
		}
		 
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

