package com.shangpin.iog.webcontainer.front.controller;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by sunny on 2015/6/5.
 */
@Controller
@RequestMapping("/test")
public class ApennineTestController {
    @Autowired
    ApennineHttpUtil httpService;
    @RequestMapping(value="/insert")
    public int insert(){
        try {
            httpService.insertApennineProducts("http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233");
            System.out.println("success");
        } catch (ServiceException e) {
            e.printStackTrace();
            System.out.println("fail");
        }
        return 0;
    }
}
