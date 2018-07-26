package com.shangpin.ephub.product.business.rest.hubpending.spu.controller;

import java.util.ArrayList;
import java.util.List;

import com.shangpin.ephub.client.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.HubSpuPendingCheckProperty;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.service.check.PropertyCheck;
import com.shangpin.ephub.product.business.service.check.property.BrandCheck;
import com.shangpin.ephub.product.business.service.check.property.CategoryCheck;
import com.shangpin.ephub.product.business.service.check.property.ColorCheck;
import com.shangpin.ephub.product.business.service.check.property.GenderCheck;
import com.shangpin.ephub.product.business.service.check.property.MaterialCheck;
import com.shangpin.ephub.product.business.service.check.property.OriginCheck;
import com.shangpin.ephub.product.business.service.check.property.SeasonCheck;
import com.shangpin.ephub.product.business.service.check.property.SpuModelCheck;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HubPendingSpu入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/pending-spu")
@Slf4j
public class HubPendingSpuCheckController {
	@Autowired
	BrandCheck brandCheck;
	@Autowired
	CategoryCheck categoryCheck;
	@Autowired
	ColorCheck colorCheck;
	@Autowired
	GenderCheck genderCheck;
	@Autowired
	MaterialCheck materialCheck;
	@Autowired
	OriginCheck originCheck;
	@Autowired
	SeasonCheck seasonCheck;
	@Autowired
	SpuModelCheck spuModelCheck;
	@Autowired
	private HubPendingSpuCheckService hubCheckRuleService;
	@Autowired
	private IPendingProductService pendingProductService;
	@Autowired
	PropertyCheck commonCheckBase;
	@RequestMapping(value = "/check-spu")
	public HubPendingSpuCheckResult checkSpu(@RequestBody HubSpuPendingDto dto){
		log.info("pendingSpu校验接受到数据：{}",JsonUtil.serialize(dto));
		HubPendingSpuCheckResult returnStr = hubCheckRuleService.checkHubPendingSpu(dto);
		log.info("pendingSpu校验结果：{}",returnStr);
		return returnStr;
	}
	@RequestMapping(value = "/export")
	public PendingProducts exportPengdingSpu(@RequestBody PendingQuryDto pendingQuryDto){
		log.info("pendingSpu导出接受到数据：{}",JsonUtil.serialize(pendingQuryDto));
		PendingProducts products = new PendingProducts();
		products.setCreateUser(pendingQuryDto.getCreateUser());
    	List<PendingProductDto> productList = pendingProductService.findPengdingSpu(pendingQuryDto);
    	products.setProduts(productList); 
    	return products;
	}

	@RequestMapping(value = "/check-spu-property")
	public HubSpuPendingDto checkSpuProperty(@RequestBody HubSpuPendingCheckProperty property){
		HubSpuPendingDto hubSpuPendingIsExist = property.getDto();
		try {
			List<CommonCheckBase> list = new ArrayList<CommonCheckBase>();
			if(property!=null){
				if(property.isHubSpuModel()){
					list.add(spuModelCheck);	
				}
				if(property.isHubBrand()){
					list.add(brandCheck);
				}
				if(property.isHubCategory()){
					list.add(categoryCheck);					
				}
				if(property.isHubColor()){
					list.add(colorCheck);
				}
				if(property.isHubGender()){
					list.add(genderCheck);
				}
				if(property.isHubMaterial()){
					list.add(materialCheck);
				}
				if(property.isHubOrigin()){
					list.add(originCheck);
				}
				if(property.isHubSeason()){
					list.add(seasonCheck);
				}
				commonCheckBase.setAllPropertyCheck(list);
				if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getUpdateUser()!=null){
					commonCheckBase.handleConvertOrCheck(hubSpuPendingIsExist,hubSpuPendingIsExist);	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hubSpuPendingIsExist;
	}
	
}