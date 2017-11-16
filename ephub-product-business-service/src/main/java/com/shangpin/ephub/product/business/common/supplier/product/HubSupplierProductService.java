package com.shangpin.ephub.product.business.common.supplier.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.airshop.supplier.product.dto.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.client.data.airshop.supplier.product.gateway.HubSupplierProductGateWay;
import com.shangpin.ephub.client.data.airshop.supplier.product.result.HubSupplierProductDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: hua商品校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubSupplierProductService{
	
	@Autowired
	HubSupplierProductGateWay hubSupplierProductGateWay;
	@Autowired
	HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
	
	public List<ProductDTO> selectProductList(HubSupplierProductRequestWithPage criteria){
		List<HubSupplierProductDto> sku = hubSupplierProductGateWay.selectByCriteriaWithRowbounds(criteria);
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		for(HubSupplierProductDto dto:sku){
			HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();
			criteriaPic.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSpuIdEqualTo(dto.getSupplierSpuId());
			List<HubSpuPendingPicDto> picList = hubSpuPendingPicGateWay.selectByCriteria(criteriaPic);
			
			ProductDTO product = new ProductDTO();
			if(picList!=null&&picList.size()>1){
				product.setSkuPicture(picList.get(0).getPicUrl());
				product.setSpuPicture(picList.get(0).getPicUrl());
			}
			product.setBarcode(dto.getSupplierBarcode());
			product.setBrandName(dto.getSupplierBrandname());
			product.setCategoryGender(dto.getSupplierGender());
			product.setCategoryName(dto.getSupplierCategoryname());
			product.setColor(dto.getSupplierSpuColor());
			product.setMaterial(dto.getSupplierMaterial());
			product.setProductCode(dto.getSupplierSpuModel());
			product.setProductDescription(dto.getSupplierSpuDesc());
			product.setProductName(dto.getSupplierSpuName());
			product.setProductOrigin(dto.getSupplierOrigin());
			product.setSeasonName(dto.getSupplierSeasonname());
			product.setSize(dto.getSupplierSkuSize());
			product.setSizeClass(dto.getSupplierSkuSizeType());
			product.setSkuId(dto.getSupplierSkuNo());
			product.setSpSkuId(dto.getSpSkuNo());
			product.setSupplierSpuId(dto.getSupplierSpuId());
			product.setSupplierSkuId(dto.getSupplierSkuId());
			productList.add(product);
		}
		return productList;
	}

	public int count(HubSupplierProductRequestWithPage pendingQuryDto) {
		return hubSupplierProductGateWay.count(pendingQuryDto);
	}
}
