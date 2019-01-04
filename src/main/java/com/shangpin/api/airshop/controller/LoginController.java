package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.CaptchaInfo;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentList;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.exception.ReminderException;
import com.shangpin.api.airshop.service.PurOrderService;
import com.shangpin.api.airshop.service.StockService;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.common.utils.CaptchaCodeUtil;

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
public class LoginController {

    protected static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    PurOrderService purOrderService;
  
    @Autowired
	private StockService stockService;
    @Autowired
	private  RestTemplate restTemplate;
	
    //2015101001584  statusCheck
    @RequestMapping(value="/statusCheck")
    public JSONObject statusCheck(){
    	
    	try {
    		JSONObject result = null;
        	JSONObject param = new JSONObject();
    		param.put("SopUserNo", "2015101001584");//S0000514
    		param.put("SupplierSkuNo", "");
    		param.put("ProductModel", "");
    		param.put("SkuNo", "");
    		param.put("PageIndex", 1);
    		param.put("PageSize", 1);
    		String messageResult= restTemplate.postForObject(ApiServiceUrlConfig.getFindStockPage(),getHttpPostData(param.toJSONString()), String.class);
    		result=JSONObject.parseObject(messageResult);
    		if (result==null) {
    			return JSONObject.parseObject("{code:\"1\",msg:\"IP: "+ getLocalIP()+";数据结构返回异常\"}");
    		}else {
    			if (!result.getBooleanValue("IsSuccess")) {
    				return JSONObject.parseObject("{code:\"1\",msg:\"IP: "+ getLocalIP()+";数据返回异常\"}");
    			}
    		}
    		return JSONObject.parseObject("{code:\"0\",msg:\"IP: "+ getLocalIP()+";success\"}");
		} catch (Exception e) {
			return JSONObject.parseObject("{code:\"1\",msg:\"IP: "+getLocalIP()+"无响应Try捕获异常\"}");
		}
		
    }
    
    private String getLocalIP(){
    	Enumeration allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
	    	while (allNetInterfaces.hasMoreElements())
	    	{
		    	NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
		    	System.out.println(netInterface.getName());
		    	Enumeration addresses = netInterface.getInetAddresses();
		    	StringBuffer sb = new StringBuffer();
		    	while (addresses.hasMoreElements())
		    	{
			    	ip = (InetAddress) addresses.nextElement();
			    	
			    	sb.append( ip.getHostAddress()+" &");
			    	/*if (ip != null && ip instanceof Inet4Address)
			    	{
			    		return  ip.getHostAddress();
			    	} else {
						return "";
					}*/
		    	}
		    	return sb.toString();
	    	}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			return "ERROR"+e.getStackTrace();
		}
    	
    	return "";
    }
    
    
    @RequestMapping(value="/getTestMessage")
    public JSONObject getTestMessage(){
    	
    	try {
    		JSONObject result = null;
        	JSONObject param = new JSONObject();
    		param.put("SopUserNo", "2015101001584");//S0000514
    		param.put("SupplierSkuNo", "");
    		param.put("ProductModel", "");
    		param.put("SkuNo", "");
    		param.put("PageIndex", 1);
    		param.put("PageSize", 1);
    		String messageResult= restTemplate.postForObject(ApiServiceUrlConfig.getFindStockPage(),getHttpPostData(param.toJSONString()), String.class);
    		result=JSONObject.parseObject(messageResult);
    		if (result==null) {
    			return JSONObject.parseObject("{code:\"1\",msg:\"数据结构返回异常\"}");
    		}else {
    			if (!result.getBooleanValue("IsSuccess")) {
    				return JSONObject.parseObject("{code:\"1\",msg:\"数据返回异常\"}");
    			}
    		}
    		return JSONObject.parseObject("{code:\"0\",msg:\"success\"}");
		} catch (Exception e) {
			return JSONObject.parseObject("{code:\"1\",msg:\"无响应Try捕获异常\"}");
		}
		
    }
    
  //包装Http请求参数
  	private HttpEntity<String> getHttpPostData(String param){
  		HttpHeaders headers = new HttpHeaders();
  		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
  		HttpEntity<String> paramEntity  = new HttpEntity<String>("=" +param,headers);
  		return paramEntity;
  	}
    
    /**
     * 登录
     * @param accountName 用户名
     * @param accountPwd  密码
     * @param captcha     验证码
     * @return 用户信息
     */
    @RequestMapping(value = "/login"/*, method = {RequestMethod.POST}*/)
    public ResponseContentOne<UserInfo> login(
            @RequestParam("accountName") String accountName,
            @RequestParam("accountPwd") String accountPwd,
            @RequestParam("captcha") String captcha,
            @ModelAttribute(Constants.SESSION_CAPTCHA) CaptchaInfo captchaInfo,
            ModelMap model, HttpSession session) throws Exception {

        //不抛出异常 验证码通过
        isCaptchaExpires(captcha,captchaInfo);

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
                return ResponseContentOne.errorResp("Username Error !");
            }

            UserInfo userInfo = responseContent.getContent().get(0);

            //比较密码 DT
            if (!DigestUtils.md5Hex(accountPwd).equalsIgnoreCase(userInfo.getAccountPwd())) {
                return ResponseContentOne.errorResp("PassWord Error !");
            }

            //隐藏掉密码
            userInfo.setAccountPwd("******");
           
            //返回的list 改成 one
            responseContentOne = new ResponseContentOne<>();
            responseContentOne.setCode(responseContent.getCode());
            responseContentOne.setMsg(responseContent.getMsg());
            responseContentOne.setContent(userInfo);

            //此处算登录成功 加入session
            model.addAttribute(Constants.SESSION_USER, userInfo);

            logger.info("sessionId={},{}登录成功", session.getId(), userInfo.getAccountName());
            
            if(!StringUtils.isEmpty(userInfo.getNeedChangePwd())&&"1".equals(userInfo.getNeedChangePwd())){
            	if("1".equals(userInfo.getNeedBindEmail())){
            		return ResponseContentOne.errorResp("98","You Need to bind the mailbox to modify the password");
            	}else{
            		return ResponseContentOne.errorResp("99","You Need to modify the password");
            	}
            }
        }
        return responseContentOne;
    }

    
    
    /**
     * 获取用户信息
     * @return 用户信息
     */
    @RequestMapping(value = "/getUser")
    public ResponseContentOne<UserInfo> getUser(@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws Exception {
    	userInfo.setAccountPwd("******");
    	return ResponseContentOne.successResp(userInfo);
    }

    /**
     * 登出
     *
     * @return 用户信息
     */
    @RequestMapping(value = "/logout")
    public ResponseContentOne<UserInfo> logout(ModelMap model, HttpSession session) throws Exception {

        //此方法清不掉 httpSesson中的数据
        model.remove(Constants.SESSION_USER);

        session.removeAttribute(Constants.SESSION_USER);
        return ResponseContentOne.successResp(null);
    }

    /**
     * 生成验证码
     * @param response res
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void writeImage(HttpServletResponse response, ModelMap model, HttpSession session) throws IOException {

        OutputStream os = response.getOutputStream();
        String code = CaptchaCodeUtil.img2Stream(os);

        CaptchaInfo captchaInfo = new CaptchaInfo();
        captchaInfo.setCode(code);
        captchaInfo.setTimestamp(System.currentTimeMillis());

        model.addAttribute(Constants.SESSION_CAPTCHA, captchaInfo);
        logger.info("sessionId={},生成验证码 captchaInfo={}",session.getId(),captchaInfo);
        os.flush();
        os.close();
    }

    /**
     * 验证验证码
     * @param captcha 验证码
     */
    @RequestMapping(value = "/captcha/check/{captcha}", method = RequestMethod.GET)
    public ResponseContentOne<String> checkCaptchaCode(@PathVariable("captcha") String captcha,
                 @ModelAttribute(Constants.SESSION_CAPTCHA) CaptchaInfo captchaInfo) throws Exception {

        //不抛出异常 验证通过
        isCaptchaExpires(captcha,captchaInfo);

        return ResponseContentOne.successResp(null);
    }

    /**
     * 校验验证码是否过期
     * @param captcha 校验码
     * @param captchaInfo 标准
     * @return 校验结果
     */
    public void isCaptchaExpires(String captcha,CaptchaInfo captchaInfo) throws Exception {

        logger.info("校验验证码 checkCaptcha=" + captcha + ",缓存验证码 captchaInfo=" + captchaInfo);

        //验证码过期
        if (StringUtils.isEmpty(captchaInfo) || !isCaptchaExpires(captchaInfo.getTimestamp())) {
            throw new ReminderException("Verification code has been expired !");
        }

        //验证码错误
        if (!captcha.equalsIgnoreCase(captchaInfo.getCode())) {
            throw new ReminderException("Verification code input errors !");
        }
    }

    /**
     * 校验验证码是否过期
     * @param timestamp 时间戳
     * @return 校验结果
     */
    private boolean isCaptchaExpires(Long timestamp){
        return timestamp != null && System.currentTimeMillis() - timestamp < Constants.CAPTCHA_EXPIRES ;
    }
    

}
