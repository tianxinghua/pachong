package com.shangpin.iog.web.conf.interceptor;

import com.shangpin.iog.common.utils.json.JsonUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2015-3-26 上午10:44:57 <br/>
 * Description: 描述文档作用<br/>
 * 
 * @author 陈小峰
 * @version
 * @since JDK 1.7
 */
public class MySimpleMappingExceptionResolver extends
		SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		System.out.println("resolver ...");
		String viewName = determineViewName(ex, request);
ex.printStackTrace();
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
			.getHeader("X-Requested-With") != null && request
			.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				try {
					PrintWriter writer = response.getWriter();
					Map<String,Object> map = new HashMap<>();
					map.put("success", false);
					map.put("msg", ex.getMessage());
					writer.write(JsonUtil.getJsonString4JavaPOJO(map));
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			return null;
		}

		// return super.doResolveException(request, response, handler, ex);
	}
}
