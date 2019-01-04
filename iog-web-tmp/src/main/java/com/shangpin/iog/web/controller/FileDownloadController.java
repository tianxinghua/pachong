/**
 * 
 */
package com.shangpin.iog.web.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 */
@Controller
@RequestMapping("/code")
public class FileDownloadController {
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;


    @RequestMapping(value = "fetchcode")
    public void setCode(HttpServletRequest request) throws Exception {
        String code =   request.getParameter("code");
        System.out.println("code = " +code );
        log.error("code =" + code);

    }




}
