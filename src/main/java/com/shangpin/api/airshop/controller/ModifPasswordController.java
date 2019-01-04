package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.CaptchaInfo;
import com.shangpin.api.airshop.dto.ReturnWaybillTaskOrderDetail;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ApiContentStr;
import com.shangpin.api.airshop.dto.base.ResponseContentList;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.exception.ReminderException;
import com.shangpin.api.airshop.service.PurOrderService;
import com.shangpin.api.airshop.service.StockService;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.HttpUtil45;
import com.shangpin.api.airshop.util.OutTimeConfig;
import com.shangpin.common.utils.CaptchaCodeUtil;
import com.shangpin.common.utils.FastJsonUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录
 * Created by ZRS on 2016/1/13.
 */
@RestController
@SessionAttributes({Constants.SESSION_USER,Constants.SESSION_CAPTCHA})
public class ModifPasswordController {

    protected static Logger logger = LoggerFactory.getLogger(ModifPasswordController.class);
    @Autowired
    LoginController loginController;
    @Autowired
    PurOrderService purOrderService;
    
    @Autowired
	private StockService stockService;
    @Autowired
	private  RestTemplate restTemplate;

    /**
     * 登录
     * @param accountName 用户名
     * @param accountPwd  密码
     * @param captcha     验证码
     * @return 用户信息
     */
    @RequestMapping(value = "/check-username"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> login(
            @RequestParam("accountName") String accountName,
            @RequestParam("captcha") String captcha,
            @ModelAttribute(Constants.SESSION_CAPTCHA) CaptchaInfo captchaInfo,
            ModelMap model, HttpSession session) throws Exception {

        //不抛出异常 验证码通过
    	loginController.isCaptchaExpires(captcha,captchaInfo);

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("AccountName", accountName);
        //请求数据
        ResponseContentList<UserInfo> responseContent = purOrderService.requestAPI(ApiServiceUrlConfig.getLoginUrl(), paramMap, UserInfo.class);
        ResponseContentOne<UserInfo> responseContentOne = null;
        //登录成功更新session
        if ("0".equals(responseContent.getCode())) {
            //没有用户信息
            if(CollectionUtils.isEmpty(responseContent.getContent()) ){
                return ResponseContentOne.errorResp("99","Username Error !");
            }
            responseContentOne = new ResponseContentOne<>();
            responseContentOne.setCode(responseContent.getCode());
            responseContentOne.setMsg(responseContent.getMsg());
            responseContentOne.setContent(responseContent.getContent().get(0));
        }
        return responseContentOne;
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @RequestMapping(value = "/getVerificationCode1")
    public ResponseContentOne<UserInfo> getVerificationCode1(
    		@RequestParam("email") String email
    		,@RequestParam("accountName") String accountName) throws Exception {
    	String json = "{\"AccountName\":\""+accountName+"\","
    			+ "\"Email\":\""+email+"\","
    			+ "\"CodeType\":\"2\","
    			+ "\"UserFrom\":\"1\","
    					+ "\"CreateUser\":\""+accountName+"\"}";
		logger.info("查询批次明细传参："+json);
	    //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("AccountName", accountName);
        paramMap.put("Email", email);
        paramMap.put("CodeType", "2");
        paramMap.put("UserFrom", "1");
        paramMap.put("CreateUser", accountName);
        String requestJson = FastJsonUtil.serialize2String(paramMap);
        ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.getVerificationCode(), requestJson);
        if(responseContent!=null&&"200".equals(responseContent.getResCode())){
        	return ResponseContentOne.successResp(null);
        }else{
        	return ResponseContentOne.errorResp(responseContent.getMessageRes());
        }
    	
    }
    
    /**
     * 获取用户信息
     * @return 用户信息
     */
    @RequestMapping(value = "/getVerificationCode")
    public ResponseContentOne<UserInfo> getVerificationCode(
    		@RequestParam("email") String email,
    		@RequestParam("codeType") String codeType
    		,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws Exception {
    	String json = "{\"AccountName\":\""+userInfo.getAccountName()+"\","
    			+ "\"Email\":\""+email+"\","
    			+ "\"CodeType\":\""+codeType+"\","
    			+ "\"UserFrom\":\"1\","
    					+ "\"CreateUser\":\""+userInfo.getAccountName()+"\"}";
		logger.info("查询批次明细传参："+json);
	    //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("AccountName", userInfo.getAccountName());
        paramMap.put("Email", email);
        paramMap.put("CodeType", codeType);
        paramMap.put("UserFrom", "1");
        paramMap.put("CreateUser", userInfo.getAccountName());
        String requestJson = FastJsonUtil.serialize2String(paramMap);
        ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.getVerificationCode(), requestJson);
        if(responseContent!=null&&"200".equals(responseContent.getResCode())){
        	return ResponseContentOne.successResp(null);
        }else{
        	return ResponseContentOne.errorResp(responseContent.getMessageRes());
        }
    	
    }
  
    /**
     * 获取用户信息
     * @return 用户信息
     */
    @RequestMapping(value = "/checkVerCodeForFind")
    public ResponseContentOne<UserInfo> checkVerCodeForFind(@RequestParam("accountName") String accountName,@RequestParam("verificationCode") String verificationCode
    		) {
  
		try{
			 //组装参数
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("AccountName", accountName);
	        paramMap.put("VerificationCode",verificationCode);
	        paramMap.put("CodeType", "2");
	        paramMap.put("UserFrom", "1");
	        paramMap.put("CreateUser", accountName);
	        String requestJson = FastJsonUtil.serialize2String(paramMap);
	        logger.info("验证邮箱码："+requestJson);
	        ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.checkVerCodeForFind(), requestJson);
			if(responseContent!=null&&"200".equals(responseContent.getResCode())){
				return ResponseContentOne.successResp(null);
			}else{
				return ResponseContentOne.errorResp(responseContent.getMessageRes());
			}
	    	
		}catch(Exception e){
			
		}
		return ResponseContentOne.errorResp("system error");
    }
    
    @RequestMapping(value = "/changePassword"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> login(
    		
            @RequestParam("accountPwd") String accountPwd,
            @RequestParam("newPwd") String newPwd,
            @RequestParam("pwdConfirm")  String pwdConfirm,
            @RequestParam("verificationCode")  String verificationCode,
            		@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
    	
    	try{
    		//组装参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("AccountName", userInfo.getAccountName());
            paramMap.put("OriAccountPwd", DigestUtils.md5Hex(accountPwd).toUpperCase());
            paramMap.put("AccountPwd", DigestUtils.md5Hex(newPwd).toUpperCase());
            paramMap.put("PwdConfirm",DigestUtils.md5Hex(pwdConfirm).toUpperCase() );
            paramMap.put("VerificationCode",verificationCode);
            paramMap.put("CodeType", "0");
            paramMap.put("UserFrom", "1");
            paramMap.put("CreateUser", userInfo.getAccountName());
            String requestJson = FastJsonUtil.serialize2String(paramMap);
            logger.info("验证邮箱码："+requestJson);
            ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.changePassword(), requestJson);
    		if(responseContent!=null&&"200".equals(responseContent.getResCode())){
    			return ResponseContentOne.successResp(null);
    		}
    		return ResponseContentOne.errorResp(responseContent.getMessageRes());
    	}catch(Exception e){
    		
    	}
    	return ResponseContentOne.errorResp("system error");
    	
    }
    
    //找回密码设置新密码
    @RequestMapping(value = "/setPwdForFind"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> SetPwdForFind(
    		
            @RequestParam("accountName") String accountName,
            @RequestParam("accountPwd") String accountPwd,
            @RequestParam("pwdConfirm") String pwdConfirm) {
    	try{
    		//组装参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("AccountName", accountName);
            paramMap.put("AccountPwd", DigestUtils.md5Hex(accountPwd).toUpperCase());
            paramMap.put("PwdConfirm", DigestUtils.md5Hex(pwdConfirm).toUpperCase());
            paramMap.put("UserFrom", "1");
            paramMap.put("CreateUser", accountName);
            String requestJson = FastJsonUtil.serialize2String(paramMap);
            logger.info("找回密码设置新密码："+requestJson);
            ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.setPwdForFind(), requestJson);
    		if(responseContent!=null&&"200".equals(responseContent.getResCode())){
    			return ResponseContentOne.successResp(null);
    		}
    		return ResponseContentOne.errorResp(responseContent.getMessageRes());
    	}catch(Exception e){
    	}
		return ResponseContentOne.errorResp("system error");
    }
    
    //登录后的修改密码
    @RequestMapping(value = "/changePwdAS"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> changePwdAS(
            @RequestParam("oriAccountPwd") String oriAccountPwd,
            @RequestParam("accountPwd") String accountPwd,
            @RequestParam("pwdConfirm") String pwdConfirm
            ,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
    	try{
        	//组装参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("AccountName", userInfo.getAccountName());
            paramMap.put("OriAccountPwd", DigestUtils.md5Hex(oriAccountPwd).toUpperCase());
            paramMap.put("AccountPwd",  DigestUtils.md5Hex(accountPwd).toUpperCase());
            paramMap.put("PwdConfirm",  DigestUtils.md5Hex(pwdConfirm).toUpperCase());
            paramMap.put("CodeType", "0");
            paramMap.put("UserFrom", "1");
            paramMap.put("CreateUser", userInfo.getAccountName());
            String requestJson = FastJsonUtil.serialize2String(paramMap);
            logger.info("找回密码设置新密码："+requestJson);
            ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.changePwdAS(), requestJson);
    		if(responseContent!=null&&"200".equals(responseContent.getResCode())){
    			return ResponseContentOne.successResp(null);	
    		}
    		return ResponseContentOne.errorResp(responseContent.getMessageRes());
    	}catch(Exception e){
    	}
    	return ResponseContentOne.errorResp("system error");
    }
    //登录后的修改邮箱
    @RequestMapping(value = "/changeEmailAS"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> changeEmailAS(
            @RequestParam("oriEmail") String oriEmail,
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode
            ,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
    	
    	try{
    		//组装参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("AccountName", userInfo.getAccountName());
            paramMap.put("OriEmail", oriEmail);
            paramMap.put("Email", email);
            paramMap.put("VerificationCode", verificationCode);
            paramMap.put("CodeType", "1");
            paramMap.put("UserFrom", "1");
            paramMap.put("CreateUser", userInfo.getAccountName());
            String requestJson = FastJsonUtil.serialize2String(paramMap);
            logger.info("找回密码设置新密码："+requestJson);
            ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.changeEmailAS(), requestJson);
    		if(responseContent!=null&&"200".equals(responseContent.getResCode())){
    		 	return ResponseContentOne.successResp(null);
    		}
    		//修改失败，则改回原邮箱
    		userInfo.setEmail(oriEmail);
    		return ResponseContentOne.errorResp(responseContent.getMessageRes());
    	}catch(Exception e){
    	}
    	return ResponseContentOne.errorResp("system error");
    }
    
    //登录后的修改密码
    @RequestMapping(value = "/bindEmailAndChangePwd"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> bindEmailAndChangePwd(
            @RequestParam("oriAccountPwd") String oriAccountPwd,
            @RequestParam("accountPwd") String accountPwd,
            @RequestParam("verificationCode") String verificationCode,
            @RequestParam("email") String email,
            @RequestParam("pwdConfirm") String pwdConfirm,
            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
    	
    	try{
    		//组装参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("AccountName", userInfo.getAccountName());
            paramMap.put("OriAccountPwd", DigestUtils.md5Hex(oriAccountPwd).toUpperCase());
            paramMap.put("AccountPwd",  DigestUtils.md5Hex(accountPwd).toUpperCase());
            paramMap.put("PwdConfirm",  DigestUtils.md5Hex(pwdConfirm).toUpperCase());
            paramMap.put("VerificationCode", verificationCode);
            paramMap.put("Email", email);
            paramMap.put("CodeType", "1");
            paramMap.put("UserFrom", "1");
            paramMap.put("CreateUser", userInfo.getAccountName());
            String requestJson = FastJsonUtil.serialize2String(paramMap);
            logger.info("找回密码设置新密码："+requestJson);
            ApiContentStr responseContent = purOrderService.postApi(ApiServiceUrlConfig.bindEmailAndChangePwd(), requestJson);
    		if(responseContent!=null&&"200".equals(responseContent.getResCode())){
    			return ResponseContentOne.successResp(null);	
    		}
    		return ResponseContentOne.errorResp(responseContent.getMessageRes());
    	}catch(Exception e){
    	}
    	return ResponseContentOne.errorResp("system error");
    }
    
}
