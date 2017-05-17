package com.shangpin.ephub.product.business.rest.gms.controller;

import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.HubResponseDto;
import com.shangpin.ephub.product.business.rest.gms.service.BrandService;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.rest.gms.service.SizeService;
import com.shangpin.ephub.product.business.rest.gms.service.SopSkuService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuDto;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuQueryDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 匹配尺码类型
 * @author zhaogenchun
 * @date 2017年2月4日 下午1:51:24
 */
@RestController
@RequestMapping(value = "/gms")
@Slf4j
public class GmsController {
	
	@Autowired
	BrandService brandService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	SizeService sizeService;
	@Autowired
	SopSkuService sopSkuService;
	@Autowired
	SupplierService supplierService;
	/**
	 * @param  brandNo 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/select-brand/{brandNo}")
	public BrandDom findBrand(@PathVariable("brandNo") String brandNo){
		BrandDom brand = brandService.getGmsBrand(brandNo);
		return brand;
	}
	
	/**
	 * @param  categoryNo 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/select-category/{categoryNo}")
	public FourLevelCategory findCategory(@PathVariable("categoryNo") String categoryNo){
		return categoryService.getGmsCateGory(categoryNo);
	}
	
	/**
	 *
	 * @return 校验结果
	 */
	@RequestMapping(value = "/select-size/{hubBrandNo}/{hubCategoryNo}")
	public CategoryScreenSizeDom findSize(@PathVariable("hubBrandNo") String hubBrandNo,@PathVariable("hubCategoryNo") String hubCategoryNo){
		log.info(GmsController.class.getName()+".findSize接收到的参数为:{}", hubBrandNo.toString());
		return sizeService.getGmsSize(hubBrandNo, hubCategoryNo);
	}
	
	/**
	 * @param queryDto 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/select-sopSkuNo")
	public HubResponseDto<SopSkuDto> findSopSkuNo(@RequestBody SopSkuQueryDto queryDto){
		try {
			return sopSkuService.querySpSkuNoFromScm(queryDto);
		} catch (JsonProcessingException e) {
			log.error("从scm获取sopSkuNo失败,{}",e);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param supplierNo 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/select-supplier/{supplierNo}")
	public SupplierDTO getSupplierDto(@PathVariable("supplierNo") String supplierNo){
		return supplierService.getSupplier(supplierNo);
	}
}
