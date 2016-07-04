package com.shangpin.iog.webcontainer.front.controller;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.CsvAttributeInfoDTO;
import com.shangpin.iog.dto.CsvSupplierInfoDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.service.CsvSupplierService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.webcontainer.front.dto.CsvAttriDTO;
import com.shangpin.iog.webcontainer.front.dto.Usr;

/**
 * 处理csv文件的控制器
 * @author sunny
 * @version 1.0 
 *  
 */
@Controller
@RequestMapping("/api")
public class CsvController {
	
	@Autowired
	CsvSupplierService csvSupplierService;
	
	@Autowired
	SupplierService supplierService;

	@RequestMapping(value="csv")
    @ResponseBody
	public ModelAndView welcomeCsv(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("csv");
		return modelAndView;
	}
	
	@RequestMapping(value="CsvSuppliers")
    @ResponseBody
	public ModelAndView getCsvSuppliers(){
		try {
			List<CsvSupplierInfoDTO> list = csvSupplierService.findAllCsvSuppliers();
//			System.out.println(list.size()); 
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("rows", list); 
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="save_user")
	public void save(@ModelAttribute Usr usr,HttpServletRequest request, HttpServletResponse response){
		try {
			if(null != usr && usr.getSupplierId() !=null && usr.getSupplierNo() != null){
				CsvSupplierInfoDTO csvSupplierInfoDTO = new CsvSupplierInfoDTO();
				csvSupplierInfoDTO.setSupplierId(usr.getSupplierId());
				csvSupplierInfoDTO.setSupplierNo(usr.getSupplierNo());
				csvSupplierInfoDTO.setCrontime(usr.getCrontime());
				csvSupplierInfoDTO.setFetchUrl(usr.getFetchUrl());
				csvSupplierInfoDTO.setState(StringUtils.isBlank(usr.getStatus())? "0":usr.getStatus()); 
				Class<ProductDTO> clazz = ProductDTO.class;
				for(Field field :clazz.getDeclaredFields()){
					CsvAttributeInfoDTO csvAttributeInfoDTO = new CsvAttributeInfoDTO();
					csvAttributeInfoDTO.setId(UUIDGenerator.getUUID()); 
					csvAttributeInfoDTO.setSupplierId(usr.getSupplierId());
					csvAttributeInfoDTO.setAttriName(field.getName());
					csvSupplierService.saveCsvAttributeInfo(csvAttributeInfoDTO); 
				}
				csvSupplierService.saveCsvSupplierInfo(csvSupplierInfoDTO);
				
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"success\":true }");
				response.getWriter().flush();
			}else{
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":保存失败}");
				response.getWriter().flush();
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":发生错误："+e.getMessage()+"}");
				response.getWriter().flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="updateCsv")
	public void updateCsv(@ModelAttribute Usr usr, HttpServletRequest request, HttpServletResponse response){
		try {
			if(null != usr && usr.getSupplierId() !=null && usr.getSupplierNo() != null){
				CsvSupplierInfoDTO csvSupplierInfoDTO = new CsvSupplierInfoDTO();
				csvSupplierInfoDTO.setSupplierId(usr.getSupplierId());
				csvSupplierInfoDTO.setSupplierNo(usr.getSupplierNo());
				csvSupplierInfoDTO.setCrontime(usr.getCrontime());
				csvSupplierInfoDTO.setFetchUrl(usr.getFetchUrl());
				csvSupplierInfoDTO.setState(StringUtils.isBlank(usr.getStatus())? "0":usr.getStatus()); 				
				csvSupplierService.updateCsvSupplierInfo(csvSupplierInfoDTO);
				
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"success\":true }");
				response.getWriter().flush();
			}else{
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":保存失败}");
				response.getWriter().flush();
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":发生错误："+e.getMessage()+"}");
				response.getWriter().flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="removeCsv")
	public void removeCsv(HttpServletRequest request, HttpServletResponse response){
		String supplierId = request.getParameter("supplierId");
		try {
			if(StringUtils.isNotBlank(supplierId)){
				csvSupplierService.deleteCsvSupplierInfo(supplierId); 
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"success\":true }");
				response.getWriter().flush();
			}
		} catch (Exception e) {
			try {
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":发生错误："+e.getMessage()+"}");
				response.getWriter().flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="csvDetail")
    @ResponseBody
	public ModelAndView gotoCsvDetail(HttpServletRequest request, HttpServletResponse response) throws ServiceException{
		String supplierId = request.getParameter("supplierId");
//		List<CsvAttributeInfoDTO> list = csvSupplierService.findCsvAttributeBySupplierId(supplierId);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("supplierId",supplierId);
		modelAndView.setViewName("csvDetail");
		return modelAndView;
	}
	
	@RequestMapping(value="getcsvDetail")
    @ResponseBody
	public ModelAndView getcsvDetail(HttpServletRequest request, HttpServletResponse response) throws ServiceException{
		String supplierId = request.getParameter("supplierId");
		List<CsvAttributeInfoDTO> list = csvSupplierService.findCsvAttributeBySupplierId(supplierId);
		SupplierDTO supplierDTO = supplierService.findBysupplierId(supplierId);
		List<CsvAttriDTO> csvList = new ArrayList<CsvAttriDTO>();
		for(CsvAttributeInfoDTO dto :list){
			CsvAttriDTO csvAttriDTO = new CsvAttriDTO();
			csvAttriDTO.setId(dto.getId()); 
			csvAttriDTO.setSupplierId(dto.getSupplierId());
			if(null != supplierDTO){
				csvAttriDTO.setSupplierName(StringUtils.isNotBlank(supplierDTO.getSupplierName())? supplierDTO.getSupplierName() : supplierDTO.getSupplierId());
			}else{
				csvAttriDTO.setSupplierName(supplierId);
			}			
			csvAttriDTO.setAttriName(dto.getAttriName());
			csvAttriDTO.setAttriRule(dto.getAttriRule());
			csvAttriDTO.setAttriValue(dto.getAttriValue()); 
			csvList.add(csvAttriDTO);
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("rows",csvList);
		return modelAndView;
	}
	
	@RequestMapping(value="saveCsvDetail")
	public void saveCsvDetail(@ModelAttribute CsvAttriDTO csvAttriDTO,HttpServletRequest request, HttpServletResponse response) throws SQLException{
		
		CsvAttributeInfoDTO csvAttributeInfoDTO = new CsvAttributeInfoDTO();
		csvAttributeInfoDTO.setId(UUIDGenerator.getUUID());
		csvAttributeInfoDTO.setSupplierId(request.getParameter("supplierId"));
		csvAttributeInfoDTO.setAttriName(csvAttriDTO.getAttriName());
		csvAttributeInfoDTO.setAttriRule(csvAttriDTO.getAttriRule());
		csvAttributeInfoDTO.setAttriValue(csvAttriDTO.getAttriValue());
		csvSupplierService.saveCsvAttributeInfo(csvAttributeInfoDTO);		
		
	}
	
	@RequestMapping(value="updateCsvDetail")
	public void updateCsvDetail(@ModelAttribute CsvAttriDTO csvAttriDTO) throws SQLException{ 
		CsvAttributeInfoDTO csvAttributeInfoDTO = new CsvAttributeInfoDTO();
		csvAttributeInfoDTO.setId(csvAttriDTO.getId());
		csvAttributeInfoDTO.setSupplierId(csvAttriDTO.getSupplierId()); 
		csvAttributeInfoDTO.setAttriName(csvAttriDTO.getAttriName());
		csvAttributeInfoDTO.setAttriRule(csvAttriDTO.getAttriRule());
		csvAttributeInfoDTO.setAttriValue(csvAttriDTO.getAttriValue());
		csvSupplierService.updateCsvAttributeInfo(csvAttributeInfoDTO);
	}
	
	@RequestMapping(value="destroyCsvDetail")
	public void destroyCsvDetail(HttpServletRequest request, HttpServletResponse response){
		try {
			String id = request.getParameter("id"); 
			csvSupplierService.delete(id);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("{\"success\":true }");
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.setCharacterEncoding("utf-8");
				response.getWriter().write("{\"msg\":发生错误："+e.getMessage()+"}");
				response.getWriter().flush();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
		
	}
	
	
}
