/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;



import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 */
@Controller
@RequestMapping("/download")
public class FileDownloadController {
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "viewpage")
    public String  viewoaddownPage() throws Exception {
        return "spinnaker";
    }


    @RequestMapping(value = "spinnaker")
    public void download(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


//        String realpath = request.getSession().getServletContext().getRealPath("/template/spinnaker.xls");
//        AccountsExcelTemplate template = AccountsExcelTemplate.newInstance(realpath);

        Date start =DateTimeUtil.convertFormat("2015-05-26 00:00:00","yyyy-MM-dd HH:mm:ss");

        productService.exportProduct("BORSA",start,new Date(),1,10);

        response.reset();
        response.setContentType("application/x-download;charset=GBK");
        response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("结算单_" + System.currentTimeMillis() + ".xls", "UTF-8"));
//        template.getWorkbook().write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }


    //文件下载 主要方法
    private  void download(HttpServletRequest request,
                                HttpServletResponse response, String storeName, String contentType
    ) throws Exception {


    }


}
