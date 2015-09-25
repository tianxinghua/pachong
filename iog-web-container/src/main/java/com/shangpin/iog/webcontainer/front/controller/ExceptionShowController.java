/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.service.OrderService;

/**
 * @description 
 * @author 侯锟
 * <br/>2015年9月23日
 */
@Controller
@RequestMapping
public class ExceptionShowController {
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value="/showException")
	public String showException(Model model,String supplierId) throws Exception{
	
		List<OrderDTO> exceptionOrders = orderService.getExceptionOrder();
		model.addAttribute("orders", exceptionOrders);
		return "showOrderException";
	}
	
}
