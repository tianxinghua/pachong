/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;



import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SupplierService;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;


/**
 */
@Controller
@RequestMapping("/download")
public class FileDownloadController {
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;
	
    @Autowired
    ProductSearchService productService;
    
    @Autowired
    SupplierService supplierService;

    @RequestMapping(value = "view")
    public ModelAndView viewPage() throws Exception {
        ModelAndView mv = new ModelAndView("iog");
        List<SupplierDTO> supplierDTOList = supplierService.findAllWithAvailable();

        mv.addObject("supplierDTOList",supplierDTOList);
        return mv;
    }


    @RequestMapping(value = "code")
    public void setCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String code =   request.getParameter("code");
        System.out.println("code = " +code );
        log.error("code =" + code);

        OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
        timeConfig.confRequestOutTime(1000*60);
        timeConfig.confSocketOutTime(1000*60);
        
        //An access token is returned as a JSON response along with the time (in seconds) till expiration.
        String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        log.error(kk);
        
        PrintWriter out = response.getWriter();
        out.println("GET ACCESS TOKEN SUCCESSFUL");
        out.println("code=="+code);
        out.println("token=="+kk);
    }
    

    @RequestMapping(value = "csv")
    public void downloadCsv(
                         HttpServletResponse response,
                         String queryJson) throws Exception {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {


            ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);

            if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();


            Date startDate  =null;
            if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
                startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
            }

            Date endDate = null;
            if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
                endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
            }

            StringBuffer productBuffer =productService.exportProduct(productSearchDTO.getSupplier(),startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize());

            response.reset();

            response.setContentType("text/csv;charset=gb2312");

            response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));

//            System.out.print("kk ----------------- " + productBuffer.toString());
            in = new BufferedInputStream(new ByteArrayInputStream(productBuffer.toString().getBytes("gb2312")));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }

//            response.getOutputStream().write(productBuffer.toString().getBytes("gb2312"));
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }



    }


    //文件下载 主要方法
    private  void download(HttpServletRequest request,
                                HttpServletResponse response, String storeName, String contentType
    ) throws Exception {


    }


}
