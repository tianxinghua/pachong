package com.shangpin.ephub.product.business.ui.hub.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.all.service.impl.HubProductServiceImpl;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetails;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductPicParam;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductQuery;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductResult;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductSpuModel;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubProductController </p>
 * <p>Description: hub页面</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:16:03
 *
 */
@RestController
@RequestMapping("/hub-product")
@Slf4j
public class HubProductController {
	
	private static String resultSuccess = "success";
	private static String resultFail = "fail";
	
	@Autowired
	private IHubProductService hubProductService;
	@Autowired
	private HubSpuGateWay hubSpuGateWay;

	@RequestMapping(value="/list",method=RequestMethod.POST)
	public HubResponse<?> hubList(@RequestBody HubQuryDto hubQuryDto){	
		
		return HubResponse.successResp(hubProductService.findHubProductds(hubQuryDto));
	}
	
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	public HubResponse<?> productDetail(@RequestBody String id){
		
		return HubResponse.successResp(hubProductService.findProductDtails(id));
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public HubResponse<?> editProductDetail(@RequestBody HubProductDetails hubProductDetails){
		boolean ifSucc = hubProductService.updateHubProductDetails(hubProductDetails);
		if(ifSucc){
			return HubResponse.successResp(resultSuccess);
		}else{
			return HubResponse.errorResp(resultFail);
		}
		
	}
	
	@RequestMapping(value="/add-pic",method=RequestMethod.POST)
	public HubResponse<?> addPic(@RequestBody HubProductPicParam hubProductPicParam){
		
		if(StringUtils.isBlank(hubProductPicParam.getSpuId()))
			return HubResponse.errorResp("spuId不能为null!");
		HubSpuDto hubSpuDto = hubSpuGateWay.selectByPrimaryKey(Long.parseLong(hubProductPicParam.getSpuId()));
		if(hubSpuDto==null)
			return HubResponse.errorResp("spu不存在!");
		if(StringUtils.isBlank(hubProductPicParam.getSpPicUrl()))
			return HubResponse.errorResp("图片url不能为null!");
		try {
			return HubResponse.successResp(hubProductService.addPicBySpuId(hubProductPicParam));
		} catch (Exception e) {
			log.error(e.getMessage());
			return HubResponse.errorResp("添加失败!");
		}
	}
	
	@RequestMapping(value="/delete-pic",method=RequestMethod.POST)
	public HubResponse<?> deletePic(@RequestBody HubProductPicParam hubProductPicParam){
		if(StringUtils.isBlank(hubProductPicParam.getSpuPicId()))
			return HubResponse.errorResp("spuPicId不能为null!");
		try {
			hubProductService.deletePicBySpuId(hubProductPicParam);
			return HubResponse.successResp("删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage());
			return HubResponse.errorResp("删除失败!");
		}
	}
	
	@RequestMapping(value="/getSpuModel",method=RequestMethod.POST)
	public HubResponse<?> getSpuModel(@RequestBody HubProductQuery hubProductQuery){
		if(StringUtils.isBlank(hubProductQuery.getBrandNo()))
			return HubResponse.errorResp("品牌编号不能为null!");
		HubSpuCriteriaDto hubSpuCriteriaDto = new HubSpuCriteriaDto();
		hubSpuCriteriaDto.createCriteria().andBrandNoEqualTo(hubProductQuery.getBrandNo());
		if(hubProductQuery.getPageIndex()==null||hubProductQuery.getPageSize()==null) {
			hubSpuCriteriaDto.setPageNo(1);
			hubSpuCriteriaDto.setPageSize(20);
		}else {
			hubSpuCriteriaDto.setPageNo(hubProductQuery.getPageIndex());
			hubSpuCriteriaDto.setPageSize(hubProductQuery.getPageSize());
		}
		List<HubSpuDto> hubSpuDtoList = hubSpuGateWay.selectByCriteria(hubSpuCriteriaDto);
		int count = hubSpuGateWay.countByCriteria(hubSpuCriteriaDto);
		if(hubSpuDtoList!=null) {
			List<HubProductSpuModel> spuModelList = new ArrayList<>();
			for(HubSpuDto hubSpuDto : hubSpuDtoList) {
				HubProductSpuModel hubProductSpuModel = new HubProductSpuModel();
				hubProductSpuModel.setSpuModel(hubSpuDto.getSpuModel());
				hubProductSpuModel.setSpuNo(hubSpuDto.getSpuNo());
				spuModelList.add(hubProductSpuModel);
			}
			HubProductResult result = new HubProductResult();
			result.setHubProductSpuModel(spuModelList);
			result.setTotal(count);
			return HubResponse.successResp(result);
		}
		return null;
	}
}
