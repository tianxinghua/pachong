package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by ZRS on 2016/1/13.
 */
@Getter
@Setter
public class UserInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JSONField(name = "SopUserNo")
    private String sopUserNo; //系统用户编码; //Int; //N
    @JSONField(name = "SupplierNo")
    private String supplierNo; //供应商编码; //String(20); //N
    @JSONField(name = "AccountName")
    private String accountName; //用户名; //String(50); //N

    //密码不可返回给前端
    @JSONField(name = "AccountPwd", serialize = false)
    private String accountPwd; //密码; //String(50); //N
    @JSONField(name = "AccountState")
    private String accountState; //门户账号状态:1未启用 2已启用 3已停用; //Int; //N
    @JSONField(name = "UserPower")
    private String userPower; //账户类型   1主账户   2子账户; //Int; //N
    @JSONField(name = "MobilePhone")
    private String mobilePhone; //手机号码; //String(20); //Y
    @JSONField(name = "Telephone")
    private String telephone; //电话号码; //String(20); //Y
    @JSONField(name = "QQ")
    private String qq; //QQ号码; //String(20); //Y
    @JSONField(name = "Email")
    private String email; //电子邮箱; //String(50); //Y
    @JSONField(name = "LastLoginTime")
    private String lastLoginTime; //最后一次登录时间; //String(20); //Y
    @JSONField(name = "LastLoginIp")
    private String lastLoginIp; //最后登录IP; //String(20); //Y
    @JSONField(name = "DirectFlag")
    private String directFlag; //最后登录IP; //String(20); //YpurchaseType
    
    @JSONField(name = "NeedChangePwd")
    private String needChangePwd; //是否需要强制修改密码 [0 or ""]=不需要，1=需要
    @JSONField(name = "NeedBindEmail")
    private String needBindEmail; //是否需要强制绑定邮箱 [0 or ""]=不需要，1=需要

    @JSONField(name = "PurchaseType")
    private String purchaseType;


    
}
