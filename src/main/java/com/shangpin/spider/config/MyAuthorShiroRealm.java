package com.shangpin.spider.config;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.sys.SysMenu;
import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.service.sys.AuthorityService;


/** 
 * @author  njt 
 * @date 创建时间：2017年11月10日 下午2:23:53 
 * @version 1.0 
 * @parameter  
 */
public class MyAuthorShiroRealm extends AuthorizingRealm {
	private Logger log = LoggerFactory.getLogger(MyAuthorShiroRealm.class);
	
	@Resource
    private AuthorityService authorityService;
	
	public MyAuthorShiroRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
		super(cacheManager, matcher);
	}
	
	/**
	 * 链接权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		log.info("进入doGetAuthorizationInfo");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		for(SysRole role:user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(SysMenu menu:role.getMenuList()){
                authorizationInfo.addStringPermission(menu.getPermission());
            }
        }
		System.out.println("结束doGetAuthorizationInfo");
        return authorizationInfo;
        
	}
	
	/**
	 * 登录认证实现
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken user = (UsernamePasswordToken) token;
		String username = user.getUsername();
		String password = user.getPassword().toString();
		 
		if(StringUtils.isEmpty(username)){
			throw new IncorrectCredentialsException("用户名不能为空");
		}
		if(StringUtils.isEmpty(password)){
			throw new IncorrectCredentialsException("密码不能为空");
		}
		
		SysUser sysUser = authorityService.getByUserName(username);
		//无须自定义验证，交给shiro
		//String passwordXin = new Md5Hash(sysUser.getPassword(), sysUser.getLoginName()+sysUser.getSalt(), 2).toString();
		if(sysUser == null){
			log.error("用户不存在"+username);
			throw new IncorrectCredentialsException("用户名或者密码不正确");
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(),
				ByteSource.Util.bytes(sysUser.getCredentialsSalt()),getName());
		return authenticationInfo;
	}

}
