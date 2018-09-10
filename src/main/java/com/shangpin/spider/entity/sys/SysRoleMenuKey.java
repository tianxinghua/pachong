package com.shangpin.spider.entity.sys;

import java.io.Serializable;

public class SysRoleMenuKey implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long roleId;

    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}