package com.shangpin.ephub.client.product.business.gms.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.gms.dto.HubResponseDto;
import com.shangpin.ephub.client.product.business.gms.dto.SopSkuQueryDto;
import com.shangpin.ephub.client.product.business.gms.dto.SupplierDTO;
import com.shangpin.ephub.client.product.business.gms.result.BrandDom;
import com.shangpin.ephub.client.product.business.gms.result.CategoryScreenSizeDom;
import com.shangpin.ephub.client.product.business.gms.result.FourLevelCategory;
import com.shangpin.ephub.client.product.business.gms.result.SopSkuDto;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB品牌型号规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface GmsGateWay {
	
	@RequestMapping(value = "/gms/select-brand/{brandNo}", method = RequestMethod.POST,consumes = "application/json")
	public BrandDom findBrand(@PathVariable("brandNo") String brandNo);
	
	@RequestMapping(value = "/gms/select-category/{categoryNo}", method = RequestMethod.POST,consumes = "application/json")
	public FourLevelCategory findCategory(@PathVariable("categoryNo") String categoryNo);
	
	@RequestMapping(value = "/gms/select-size/{hubBrandNo}/{hubCategoryNo}", method = RequestMethod.POST,consumes = "application/json")
	public CategoryScreenSizeDom findSize(@PathVariable("hubBrandNo") String hubBrandNo,@PathVariable("hubCategoryNo") String hubCategoryNo);
	
	@RequestMapping(value = "/gms/select-sopSkuNo", method = RequestMethod.POST,consumes = "application/json")
	public HubResponseDto<SopSkuDto> findSopSkuNo(SopSkuQueryDto brandNo);
	
	@RequestMapping(value = "/gms/select-supplier/{supplierNo}", method = RequestMethod.POST,consumes = "application/json")
	public SupplierDTO getSupplierDto(@PathVariable("supplierNo") String supplierNo);

}
