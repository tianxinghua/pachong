/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.util.List;

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
@RequestMapping
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
    	model.addAttribute("allData", all);
		return "showStockException";
    }
    
}
