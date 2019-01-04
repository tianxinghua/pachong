package com.shangpin.api.airshop.controller;

import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/** 
 * Date:     2016年1月12日 <br/> 
 * @author   陈小峰
 * @since    JDK 7
 */
@Controller("globalError")
@RequestMapping
@RestController
public class DefaultError implements ErrorController {


	@ResponseBody
    @RequestMapping("/error")
    public ResponseContentOne<String> error(HttpServletResponse response) throws Throwable {
    	return ResponseContentOne.errorResp("2", "系统异常---");
    }

	@Override
	public String getErrorPath() {
		return null;
	}
	    
}
