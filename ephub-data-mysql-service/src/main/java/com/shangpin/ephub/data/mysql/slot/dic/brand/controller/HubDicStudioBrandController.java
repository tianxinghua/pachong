//package com.shangpin.ephub.data.mysql.slot.dic.brand.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.shangpin.ephub.data.mysql.slot.dic.brand.bean.HubDicStudioBrandCriteriaWithRowBounds;
//import com.shangpin.ephub.data.mysql.slot.dic.brand.bean.HubDicStudioBrandWithCriteria;
//import com.shangpin.ephub.data.mysql.slot.dic.brand.po.HubDicStudioBrand;
//import com.shangpin.ephub.data.mysql.slot.dic.brand.po.HubDicStudioBrandCriteria;
//import com.shangpin.ephub.data.mysql.slot.dic.brand.service.HubDicStudioBrandService;
//
//@RestController
//@RequestMapping("/hub-dic-studio-brand")
//public class HubDicStudioBrandController {
//	@Autowired
//	private HubDicStudioBrandService hubDicStudioBrandService;
//
//	@RequestMapping(value = "/count-by-criteria")
//    public int countByCriteria(@RequestBody HubDicStudioBrandCriteria criteria){
//    	return hubDicStudioBrandService.countByCriteria(criteria);
//    }
//	@RequestMapping(value = "/delete-by-criteria")
//    public int deleteByCriteria(@RequestBody HubDicStudioBrandCriteria criteria){
//    	return hubDicStudioBrandService.deleteByCriteria(criteria);
//    }
//	@RequestMapping(value = "/delete-by-primary-key")
//    public int deleteByPrimaryKey(Long skuId){
//    	return hubDicStudioBrandService.deleteByPrimaryKey(skuId);
//    }
//	@RequestMapping(value = "/insert")
//    public int insert(@RequestBody HubDicStudioBrand hubDicStudioBrand){
//    	return hubDicStudioBrandService.insert(hubDicStudioBrand);
//    }
//	@RequestMapping(value = "/insert-selective")
//    public int insertSelective(@RequestBody HubDicStudioBrand hubDicStudioBrand){
//    	return hubDicStudioBrandService.insertSelective(hubDicStudioBrand);
//    }
//	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
//    public List<HubDicStudioBrand> selectByCriteriaWithRowbounds(@RequestBody HubDicStudioBrandCriteriaWithRowBounds criteriaWithRowBounds){
//    	return hubDicStudioBrandService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
//    }
//	@RequestMapping(value = "/select-by-criteria")
//    public List<HubDicStudioBrand> selectByCriteria(@RequestBody HubDicStudioBrandCriteria criteria){
//    	return hubDicStudioBrandService.selectByCriteria(criteria);
//    }
//	@RequestMapping(value = "/select-by-primary-key/{skuId}")
//    public HubDicStudioBrand selectByPrimaryKey(@PathVariable(value = "skuId") Long skuId){
//    	return hubDicStudioBrandService.selectByPrimaryKey(skuId);
//    }
//	@RequestMapping(value = "/update-by-criteria-selective")
//    public int updateByCriteriaSelective(@RequestBody HubDicStudioBrandWithCriteria hubDicStudioBrandWithCriteria){
//    	return hubDicStudioBrandService.updateByCriteriaSelective(hubDicStudioBrandWithCriteria);
//    }
//	@RequestMapping(value = "/update-by-criteria")
//    public int updateByCriteria(@RequestBody HubDicStudioBrandWithCriteria hubDicStudioBrandWithCriteria){
//    	return hubDicStudioBrandService.updateByCriteria(hubDicStudioBrandWithCriteria);
//    }
//	@RequestMapping(value = "/update-by-primary-key-selective")
//    public int updateByPrimaryKeySelective(@RequestBody HubDicStudioBrand hubDicStudioBrand){
//    	return hubDicStudioBrandService.updateByPrimaryKeySelective(hubDicStudioBrand);
//    }
//	@RequestMapping(value = "/update-by-primary-key")
//    public int updateByPrimaryKey(@RequestBody HubDicStudioBrand hubDicStudioBrand){
//    	return hubDicStudioBrandService.updateByPrimaryKey(hubDicStudioBrand);
//    }
//}
