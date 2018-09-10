package com.shangpin.spider.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.sys.SysUser;

public interface SysUserMapper {
    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    
    /**
	 * 通过用户名获取用户信息
	 * @param username
	 * @return
	 */
    SysUser getByUsername(@Param("username")String username);
    /**
     * 根据用户名和密码获取用户信息
     * @param loginName
     * @param password
     * @return
     */
	SysUser getByUsernameAndPwd(@Param("username")String username, @Param("passwd")String password);
	/**
	 * 获取所有用户
	 * @return
	 */
	List<SysUser> selectAll();
}