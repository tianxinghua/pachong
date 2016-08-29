package com.shangpin.iog.webcontainer.front.controller;

/**
 * 选择BU导出
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.service.ProductSearchService;

@Controller
@RequestMapping("/download")
public class ExportController {
	
	private Logger log = LoggerFactory.getLogger(ExportController.class) ;

	@Autowired
    ProductSearchService productService;
	
	@RequestMapping(value="buexport")
	public void buExport(HttpServletResponse response,String queryJson){
		
		BufferedInputStream in = null;
        BufferedOutputStream out = null;
        StringBuffer productBuffer =null;
		try {
			log.error("==============进入主方法===============");
			response.reset();
	        response.setContentType("text/csv;charset=gb2312");
	        ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);
	        if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();
	        String supplier = null;
	        if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
	        	supplier = productSearchDTO.getSupplier();
	        }
	        Date startDate  =null;
	        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
	            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
	        }
	        Date endDate = null;
	        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
	            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
	        }
	        Integer pageIndex = -1;
	        if(null !=productSearchDTO.getPageIndex()){
	        	pageIndex = productSearchDTO.getPageIndex();
	        }
	        Integer pageSize = -1;
	        if(null != productSearchDTO.getPageSize()){
	        	pageSize = productSearchDTO.getPageSize();
	        }
	        String bu = productSearchDTO.getBu();
	        log.error("=====得到bu==================="+bu); 
	        if(StringUtils.isEmpty(bu) || "-1".equals(bu)){
 	        	return;
	        }
	        if("womanshoe".equals(bu.toLowerCase())){
	        	log.error("==============走这里 女鞋===============");
	        	productBuffer =productService.shoeExportProduct(bu, supplier, startDate, endDate, pageIndex, pageSize);
	        }else{
	        	log.error("==============走这里 非女鞋===============");
	        	productBuffer =productService.buExportProduct(bu, supplier, startDate, endDate, pageIndex, pageSize);
	        }	        
	        response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode((StringUtils.isBlank(supplier) ? "All":productSearchDTO.getSupplierName())+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
	        in = new BufferedInputStream(new ByteArrayInputStream(productBuffer.toString().getBytes("gb2312")));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (in != null) {
	                in.close();
	            }
	            if (out != null) { 
	                out.close();
	            }
			} catch (Exception e2) {
				e2.printStackTrace();
			}
            
        }
        
	}
}
