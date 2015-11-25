/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.OrderService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.UpdateStockService;

/**
 * @description 
 * @author 侯锟
 * <br/>2015年11月24日
 */
@Controller
@RequestMapping("/download")
public class UpdatePriceController {
    
    @Autowired
    SkuPriceService skuPriceService;

    @RequestMapping(value = "/getSkuIds")
    public String getData(HttpServletRequest request,Model model){
    	String supplierId = request.getParameter("supplierId");
    	model.addAttribute("supplierId", supplierId);
    	return "updatePrice";
    }
    @RequestMapping(value = "/updatePrice")
    public void showStockUpdateException(HttpServletRequest request,HttpServletResponse response){
    	String supplierId = request.getParameter("supplierId");
    	String skuIds = request.getParameter("skuIds");
    	if (skuIds!=null) {
	    	skuIds = skuIds.replace("，", ",");
	    	int flag = 0;
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(skuIds);
            skuIds = m.replaceAll("");
            String[] skuArr = skuIds.split(",");
            StringBuffer sb = new StringBuffer();
            try {
            	for (String skuId : skuArr) {
            		SkuDTO skuDTO = new SkuDTO();
            		skuDTO.setSupplierId(supplierId);
            		skuDTO.setSkuId(skuId);
            		NewPriceDTO newPriceDTO = skuPriceService.getNewPriceDTO(supplierId, skuId);
            		if (newPriceDTO!=null) {
            			skuDTO.setNewMarketPrice(newPriceDTO.getNewMarketPrice());
            			skuDTO.setNewSalePrice(newPriceDTO.getNewSalePrice());
            			skuDTO.setNewSupplierPrice(newPriceDTO.getNewSupplierPrice());
            			skuPriceService.synchPrice(skuDTO);
            		}else {
            			sb.append(skuId).append(",");
            		}
            	}
            } catch (ServiceException e) {
            	flag = 1;
            }
            try {
            	response.setContentType("text/html;charset=utf-8");
            	if (flag==1) {
            		response.getWriter().print("<script>alert('更新失败');</script>");
            	}
            	if (StringUtils.isNotBlank(sb.toString())) {
            		response.getWriter().write("<html><h2>"+sb.toString()+"sku未找到"+"</h2></html>");
            	}
            } catch (IOException e) {
            	e.printStackTrace();
            }
         }
    }
}
