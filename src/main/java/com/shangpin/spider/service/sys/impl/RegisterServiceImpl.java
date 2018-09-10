package com.shangpin.spider.service.sys.impl;

import java.util.Date;
import java.util.Random;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shangpin.spider.entity.sys.SysRole;
import com.shangpin.spider.entity.sys.SysUser;
import com.shangpin.spider.entity.sys.SysUserRole;
import com.shangpin.spider.service.sys.RegisterService;
import com.shangpin.spider.service.sys.SysUserRoleService;
import com.shangpin.spider.service.sys.SysUserService;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月14日 上午11:41:42 
 * @version 1.0 
 * @parameter  
 */
@Service
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class RegisterServiceImpl implements RegisterService {

	private static Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	//普通会员
	private static final Integer ROLE_MEMBER = 7;
	
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
    @Override
    public int doRegister(SysUser sysuser) throws Exception{
    	LOG.info("---用户注册！");
      Random random = new Random();
      int salt = random.nextInt(999999)%900000+100000;
      String passwordXin = new Md5Hash(sysuser.getPassword(), sysuser.getUsername()+salt, 2).toString();
      sysuser.setPassword(passwordXin);
      sysuser.setSalt(salt+"");
      sysuser.setCreateTime(new Date());
      int rows = sysUserService.save(sysuser);
      Integer uid = sysuser.getId();
      //默认为:普通会员
      SysUserRole userRole = null;
      if(sysuser.getRoleList() != null && sysuser.getRoleList().size() > 0) {
    	  for(SysRole role : sysuser.getRoleList()) {
    		  userRole = new SysUserRole(uid,role.getId());
    		  sysUserRoleService.save(userRole);
    	  }
      }else {
    	  userRole = new SysUserRole(uid,ROLE_MEMBER);
    	  sysUserRoleService.save(userRole);
      }
      return rows;
    }

	@Override
	public SysUser getByUserName(String username) {
		return sysUserService.getByUserName(username);
	}

}
