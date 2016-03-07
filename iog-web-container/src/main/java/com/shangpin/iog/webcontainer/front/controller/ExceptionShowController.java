/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.OrderService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.UpdateStockService;

/**
 * @description 
 * @author 侯锟
 * <br/>2015年9月23日
 */
@Controller
@RequestMapping("/download")
public class ExceptionShowController {
    
    @Autowired
    SupplierService supplierService;
	@Autowired
	OrderService orderService;
	@Autowired
	UpdateStockService updateStockService;
	
	@RequestMapping(value="/showException")
	public String showException(Model model,String supplierId) throws Exception{
	
		List<OrderDTO> exceptionOrders = orderService.getExceptionOrder();
		model.addAttribute("orders", exceptionOrders);
		return "showOrderException";
	}
    @RequestMapping(value = "/ExceptionView")
    public ModelAndView viewPage() throws Exception {
        ModelAndView mv = new ModelAndView("exception");
        List<SupplierDTO> supplierDTOList = supplierService.findAllWithAvailable();

        mv.addObject("supplierDTOList",supplierDTOList);
        return mv;
    }

    @RequestMapping(value = "/stockUpdateException")
    public String showStockUpdateException(Model model) throws Exception {
    	List<StockUpdateDTO> all = updateStockService.getAll();
    	List<StockUpdateDTO> redList = new ArrayList<StockUpdateDTO>();
    	List<StockUpdateDTO> greenList = new ArrayList<StockUpdateDTO>();
    	List<SupplierDTO> availableSupplierDTOList = supplierService.findAllWithAvailable();
    	List<SupplierDTO> supplierDTOList = supplierService.findByState(null);
    	Map<String, String> nameMap = new HashMap<String, String>();
    	for (SupplierDTO supplierDTO : supplierDTOList) {
			nameMap.put(supplierDTO.getSupplierId(), supplierDTO.getSupplierName());
		}
    	for (StockUpdateDTO stockUpdateDTO : all) {
    		long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
    		long days = diff / (1000 * 60 * 60 * 24);
    		long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
    		long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
    		stockUpdateDTO.setDif(days+"天"+hours+"小时"+minutes+"分");
    		stockUpdateDTO.setSupplierName(nameMap.get(stockUpdateDTO.getSupplierId()));
    		if (hours<=2&&days<1) {
    			redList.add(stockUpdateDTO);
			}else {
				greenList.add(stockUpdateDTO);
			}
    	}
    	model.addAttribute("greenList", greenList);
    	model.addAttribute("redList", redList);
    	model.addAttribute("supplierDTOList", availableSupplierDTOList);
		return "iog";
    }
    
}
