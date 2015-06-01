/**
 * 
 */
package com.shangpin.iog.webcontainer.front.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月1日
 */
public class PermissionFilter extends OncePerRequestFilter  {
	private Logger log = LoggerFactory.getLogger(PermissionFilter.class) ;
	static Long tokenOutTime=7200000L;//120min
	static List<String> resource=new ArrayList<>();
	static List<String> uris=new ArrayList<>();
	{
		String rstr="js,css,jpg,gif,bmp,png,doc,xls,docx,xlsx,ppt,pptx";
		for(String s:rstr.split(",")){
			resource.add(s);			
		}
		uris.add("/login");uris.add("/logout");uris.add("/loging");
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//filterChain.doFilter(request, response);
		log.info("----->Permision Filter:"+request.getRequestURI());
		String uri=request.getServletPath();
		HttpSession session=request.getSession();
		boolean filter=false;
		if(session.getAttribute(UAASUtil.TOKEN)==null && !passUrl(uri)	){
			filter=true;
		}
		log.info("filter result:"+filter);
		if(filter){
			request.getRequestDispatcher("/loging").forward(request,response);
			return;
		}else{
			long tokenTime=request.getSession().getCreationTime();
			if((new Date().getTime()-tokenTime)>tokenOutTime){
				touchToken((String)session.getAttribute(UAASUtil.TOKEN),session);
			}
			filterChain.doFilter(request, response);
		}
		//String token=(String)session.getAttribute(LoginController.TOKEN);
	}

	/**
	 * @param attribute
	 * @param session
	 */
	private void touchToken(String token, HttpSession session) {
		if(UAASUtil.delayTokenTime(token)){
			session.setAttribute(UAASUtil.TOKEN, token);
		}else{
			session.removeAttribute(UAASUtil.TOKEN);
		}
	}

	/**
	 * @param uri
	 * @return
	 */
	private boolean passUrl(String uri) {
		int pos=uri.lastIndexOf(".");
		if(pos==-1 && !uris.contains(uri)){
			return false;
		}
		String suffix=uri.substring(pos+1);
		if(resource.contains(suffix)||uris.contains(uri))
			return true;
		return false;
	}

}
