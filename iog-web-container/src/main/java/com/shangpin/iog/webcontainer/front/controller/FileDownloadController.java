/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;



import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
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

    @RequestMapping(value = "view")
    public String  viewPage() throws Exception {
        return "spinnaker";
    }


    @RequestMapping(value = "spinnaker")
    public void download(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String queryJson) throws Exception {

        ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);;
        if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();


        String realPath = request.getSession().getServletContext().getRealPath("/template/common.xls");

        Date startDate  =null;
        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
        }

        Date endDate = null;
        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
        }

        AccountsExcelTemplate template =   productService.exportProduct(realPath ,productSearchDTO.getCategory(),startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize());



        response.reset();
        response.setContentType("application/x-download;charset=UTF-8");//GBK
        response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("product" + System.currentTimeMillis() + ".xls", "UTF-8"));
        template.getWorkbook().write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }


    //文件下载 主要方法
    private  void download(HttpServletRequest request,
                                HttpServletResponse response, String storeName, String contentType
    ) throws Exception {


    }


}
