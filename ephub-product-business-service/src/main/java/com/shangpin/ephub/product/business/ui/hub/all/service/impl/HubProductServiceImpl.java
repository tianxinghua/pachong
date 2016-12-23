package com.shangpin.ephub.product.business.ui.hub.all.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.HubSpuState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProduct;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetail;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProducts;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.product.business.ui.hub.common.service.HubCommonProductService;
import com.shangpin.ephub.product.business.ui.pendingCrud.util.JavaUtil;

/**
 * <p>Title:HubProductServiceImpl </p>
 * <p>Description: hub页面service实现类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午6:25:26
 *
 */
@Service
public class HubProductServiceImpl implements IHubProductService {
	
	@Autowired
	private HubSpuGateWay hubSpuClient;
	@Autowired
	private HubSkuGateWay hubSkuClient;
	@Autowired
	private HubSkuSupplierMappingGateWay hubSkuSupplierMappingClient;
	@Autowired
	private HubCommonProductService hubCommonProductService;

	@Override
	public HubProducts findHubProductds(HubQuryDto hubQuryDto) {
		try {
			if(null != hubQuryDto){
				HubProducts hubProducts = new HubProducts();
				HubSpuCriteriaWithRowBoundsDto rowBoundsDto = hubCommonProductService.getHubSpuCriteriaWithRowBoundsByHubQuryDto(hubQuryDto);
				int total = hubSpuClient.countByCriteria(rowBoundsDto.getCriteria());
				if(total>0){
					List<HubSpuDto> hubSpus = hubSpuClient.selectByCriteriaWithRowbounds(rowBoundsDto);
					hubProducts.setHubProducts(hubSpus);
				}
				hubProducts.setTotal(total);
				return hubProducts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public HubProductDetail findProductDtails(Long spuId){
		HubSpuDto hubSpu = hubSpuClient.selectByPrimaryKey(spuId);
		if(null != hubSpu){
			HubProductDetail hubProductDetail = getHubProductDetailFromHubSpu(hubSpu);
			List<HubProduct> hubProducts = new ArrayList<HubProduct>();
			HubSkuCriteriaDto criteriaDto = new HubSkuCriteriaDto();
			criteriaDto.createCriteria().andSpuNoEqualTo(hubSpu.getSpuNo());
			List<HubSkuDto> hubSkus =  hubSkuClient.selectByCriteria(criteriaDto);
			if(null != hubSkus && hubSkus.size()>0){
				for(HubSkuDto hubSku : hubSkus){
					HubProduct hubProuct = covertHubSpuToHubProduct(hubSpu,hubSku);
					hubProducts.add(hubProuct);
				}
				hubProductDetail.setHubDetails(hubProducts);
			}
			return hubProductDetail;
		}else{
			return null;
		}
	}
	
	public boolean updateHubProductDetails(HubProductDetail hubProductDetail){
		try {
			hubProductDetail.getProductName();
			List<HubProduct> hubProducts = hubProductDetail.getHubDetails();
			if(null != hubProducts && hubProducts.size()>0){
				for(HubProduct hubSku : hubProducts){
					if(!StringUtils.isEmpty(hubSku.getSkuId())){
						HubProduct newHubSku = new HubProduct();
						newHubSku.setSkuId(hubSku.getSkuId());
						if(!StringUtils.isEmpty(hubSku.getSpuNoOrg())){}
						updateHubSkuDto(hubSku);
					}
					
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void updateHubSkuDto(HubSkuDto hubSkuDto) throws Exception{
		hubSkuClient.updateByPrimaryKeySelective(hubSkuDto);
	}
	
	/**
	 * 从hubSpu中提取详情页需要用的信息
	 * @param hubSpu
	 * @return
	 */
	private HubProductDetail getHubProductDetailFromHubSpu(HubSpuDto hubSpu){
		HubProductDetail hubProductDetail = new HubProductDetail();
		hubProductDetail.setBrandNo(hubSpu.getBrandNo());
		hubProductDetail.setCategoryNo(hubSpu.getCategoryNo());
		hubProductDetail.setHomeMarketPrice("");//TODO
		hubProductDetail.setOriginalProductModle("");//TODO
		hubProductDetail.setProductName(hubSpu.getSpuName());
		hubProductDetail.setProductUnit("");//TODO
		return hubProductDetail;
	}
	
	/**
	 * 赋值转换
	 * @param hubSpu
	 * @param hubSku
	 * @return
	 */
	private HubProduct covertHubSpuToHubProduct(HubSpuDto hubSpu,HubSkuDto hubSku){
		HubProduct hubProuct = new HubProduct();
		if(null != hubSpu){
			hubProuct.setSupplierName("");//TODO 
			hubProuct.setSupplierProductModle(findSupplierSpuModelByHubSkuNo(hubSku.getSkuId()));
			hubProuct.setColor(hubSpu.getHubColor());
			hubProuct.setMaterial("");//TODO 
			hubProuct.setProductOrigin("");//TODO 
			if(HubSpuState.ON_SALE.getIndex() == hubSpu.getSpuState()){
				hubProuct.setSpuState(HubSpuState.ON_SALE.getDescription());
			}
			
		}
		if(null != hubSku){
			JavaUtil.fatherToChild(hubSku, hubProuct);
		}
		return hubProuct;
	}
	
	/**
	 * 根据HubSkuNo查找供应商原始货号
	 * @param hubSkuNo
	 * @return
	 */
	private String findSupplierSpuModelByHubSkuNo(Long hubSkuNo){
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSkuNoEqualTo(hubSkuNo);
		List<HubSkuSupplierMappingDto>  lists = hubSkuSupplierMappingClient.selectByCriteria(criteriaDto);
		if(null != lists && lists.size()>0){
			return lists.get(0).getSupplierSpuModel();
		}else{
			return "";
		}
			
	}

}
