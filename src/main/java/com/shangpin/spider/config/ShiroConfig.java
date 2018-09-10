package com.shangpin.spider.config;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import com.shangpin.spider.utils.shiro.MyShiroSessionListener;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月10日 上午10:55:03 
 * @version 1.0 
 * @parameter  
 */
@Configuration
public class ShiroConfig {
	
	//session过期时间，单位：分
	private static final int sessionTimeout = 30;
	/**
	 * 缓存管理器
	 * @return
	 */
	@Bean(name = "cacheShiroManager")
	public CacheManager getCacheManager(){
		return new EhCacheManager();
	}
	
	/**
	 * 生命周期处理器
	 * @return
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	
	/**
	 * hash加密处理
	 * @return
	 */
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher getHashedCredentialsMatcher(){
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");
		//散列的次数，比如散列两次，相当于 md5(md5(""));
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return 	credentialsMatcher;	
	}
	
	/**
	 * 浏览器会话的cookie管理
	 * @return
	 */
	/*@Bean(name = "sessionIdCookie")
	public SimpleCookie getSessionIdCookie(){
		SimpleCookie cookie = new SimpleCookie("sid");
		cookie.setHttpOnly(true);
		//浏览器关闭时失效此Cookie;
		cookie.setMaxAge(-1);
		return cookie;
	}*/
	
	/**
	 * 记住我的cookie管理
	 * @return
	 */
	/*@Bean(name = "rememberMeCookie")
	public SimpleCookie getRememberMeCookie(){
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		//如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
		cookie.setHttpOnly(true);
		//记住我的cookie有效期30天
		cookie.setMaxAge(2592000);
		return cookie;
	}*/
	
	/**
	 * 记住我cookie管理器
	 * @return
	 */
	/*@Bean
	public CookieRememberMeManager getRememberManager(){
		CookieRememberMeManager meManager = new CookieRememberMeManager();
		meManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		meManager.setCookie(getRememberMeCookie());
		return meManager;
	}*/
	
	/**
	 * session验证管理器
	 * @return
	 */
	@Bean(name = "sessionValidationScheduler")
	public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler(){
		ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
		/**
		 * 设置session验证时间,5分钟一次
		 */
		scheduler.setInterval(300000);
		return scheduler;
	}
	
	
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager getSessionManager(@Qualifier("cacheShiroManager") CacheManager cache
			){
		
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//过期时间30分钟
		sessionManager.setGlobalSessionTimeout(1000*60*sessionTimeout);
		//session定期验证
		sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
		sessionManager.setDeleteInvalidSessions(true);
		//会话cookie
		//sessionManager.setSessionIdCookie(getSessionIdCookie());
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//session监听
		LinkedList<SessionListener> list = new LinkedList<SessionListener>();
		list.add(new MyShiroSessionListener());
		sessionManager.setSessionListeners(list);
		//session的存储
		SessionDAO cacheSessionDao = new EnterpriseCacheSessionDAO();
		sessionManager.setCacheManager(cache);
		sessionManager.setSessionDAO(cacheSessionDao);
		//消除URL后面的JSESSIONID
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}
	/*@Bean(name = "sessionManager")
	public WebSessionManager getSessionManager(){
		return new ServletContainerSessionManager();
	}*/
	
	@Bean(name = "myRealm")
	public AuthorizingRealm  getShiroRealm(@Qualifier("cacheShiroManager") CacheManager cache,
			@Qualifier("hashedCredentialsMatcher")HashedCredentialsMatcher hashCredential){
		AuthorizingRealm  realm = new MyAuthorShiroRealm(cache,hashCredential);
		realm.setName("my_shiro_auth_cache");
		realm.setAuthenticationCache(cache.getCache(realm.getName()));
		realm.setAuthenticationTokenClass(UsernamePasswordToken.class);
		realm.setCredentialsMatcher(hashCredential);
		return realm;
	}
	
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getSecurityManager(@Qualifier("sessionManager") WebSessionManager sessionManager,@Qualifier("myRealm") AuthorizingRealm realm){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setCacheManager(realm.getCacheManager());
		securityManager.setRealm(realm);
		//securityManager.setRememberMeManager(getRememberManager());
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}
	
	/*@Bean
	public MethodInvokingFactoryBean getMethodInvokingFactoryBean(){
		MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
		factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		factoryBean.setArguments(new Object[]{getSecurityManager()});
		return factoryBean;
	}*/
	
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager sm){
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(sm);
		return advisor;
	}
	
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager sm){
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(sm);
		factoryBean.setLoginUrl("/login");
		factoryBean.setSuccessUrl("/");   //首页
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		//不拦截
		filterChainDefinitionMap.put("/f/**", "anon");  //所有免费接口
		filterChainDefinitionMap.put("/webSite", "anon");
		filterChainDefinitionMap.put("/login/**", "anon");  //和登录有关接口
		filterChainDefinitionMap.put("/register/**", "anon");  //和注册有关接口
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/manager/hello", "anon");
		//需认证
		//filterChainDefinitionMap.put("/manager/**", "authc");
		filterChainDefinitionMap.put("/**", "authc");
		
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return factoryBean;
	}
	
	/**
	 * thymeleaf 使用shiro标签
	 * @return
	 */
	@Bean
    public ShiroDialect shiroDialect(){  
        return new ShiroDialect();  
    }
	

}
