package com.shangpin.ephub.product.business.ui.hub.all.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.HubSpuState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetail;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetails;
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
	public HubProductDetails findProductDtails(Long spuId){
		HubSpuDto hubSpu = hubSpuClient.selectByPrimaryKey(spuId);
		if(null != hubSpu){
			HubProductDetails hubProductDetails = getHubProductDetailsFromHubSpu(hubSpu);
			List<HubProductDetail> details = new ArrayList<HubProductDetail>();
			
			HubSkuCriteriaDto criteriaDto = new HubSkuCriteriaDto();
			criteriaDto.createCriteria().andSpuNoEqualTo(hubSpu.getSpuNo());
			List<HubSkuDto> hubSkus =  hubSkuClient.selectByCriteria(criteriaDto);
			if(null != hubSkus && hubSkus.size()>0){
				for(HubSkuDto hubSku : hubSkus){
					List<HubProductDetail> productDetails = getProductDetailsByHubSku(hubSpu,hubSku);
					for(HubProductDetail hubProductDetail : productDetails){
						details.add(hubProductDetail);
					}
					
				}
				hubProductDetails.setHubDetails(details);
			}
			return hubProductDetails;
		}else{
			return null;
		}
	}
	@Override
	public boolean updateHubProductDetails(HubProductDetails hubProductDetails){
		try {			
			if(!StringUtils.isEmpty(hubProductDetails.getProductName())){
				HubSpuDto hubSpuDto = new HubSpuDto();
				hubSpuDto.setSpuId(hubProductDetails.getSpuId());
				hubSpuDto.setSpuName(hubProductDetails.getProductName()); 
				updateHubSpu(hubSpuDto);
			}			
			hubProductDetails.getProductName();
			List<HubProductDetail> hubProducts = hubProductDetails.getHubDetails();
			if(null != hubProducts && hubProducts.size()>0){
				for(HubProductDetail hubSku : hubProducts){
					if(!StringUtils.isEmpty(hubSku.getSkuId())){
						HubSkuSupplierMappingDto hubSkuSupplierMappingDto = new HubSkuSupplierMappingDto();
						hubSkuSupplierMappingDto.setSkuNo(hubSku.getSkuId());
						hubSkuSupplierMappingDto.setSupplierId(hubSku.getSupplierId());
						if(!StringUtils.isEmpty(hubSku.getSupplierSkuNo())){
							hubSkuSupplierMappingDto.setSupplierSkuNo(hubSku.getSupplierSkuNo());
						}
						if(!StringUtils.isEmpty(hubSku.getBarcode())){
							hubSkuSupplierMappingDto.setBarcode(hubSku.getBarcode());
						}
						updateHubSkuSupplierMapping(hubSkuSupplierMappingDto);
					}
					
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 根据主键更新hub_spu字段
	 * @param hubSpuDto
	 * @throws Exception
	 */
	private void updateHubSpu(HubSpuDto hubSpuDto) throws Exception{
		hubSpuClient.updateByPrimaryKeySelective(hubSpuDto);
	}
	
	/**
	 * 根据sku_no和供应商门户编号更新hub_sku_supplier_mapping中的供应商SkuNo或条码
	 * @param hubSkuSupplierMappingDto
	 * @throws Exception
	 */
	private void updateHubSkuSupplierMapping(HubSkuSupplierMappingDto hubSkuSupplierMappingDto) throws Exception{
		HubSkuSupplierMappingWithCriteriaDto hubSkuSupplierMappingWithCriteriaDto = new HubSkuSupplierMappingWithCriteriaDto();
		HubSkuSupplierMappingCriteriaDto criteria = new HubSkuSupplierMappingCriteriaDto();
		criteria.createCriteria().andSkuNoEqualTo(hubSkuSupplierMappingDto.getSkuNo()).andSupplierIdEqualTo(hubSkuSupplierMappingDto.getSupplierId());
		hubSkuSupplierMappingWithCriteriaDto.setCriteria(criteria);
		hubSkuSupplierMappingWithCriteriaDto.setHubSkuSupplierMapping(hubSkuSupplierMappingDto);
		hubSkuSupplierMappingClient.updateByCriteriaSelective(hubSkuSupplierMappingWithCriteriaDto);
	}
	
	/**
	 * 从hubSpu中提取详情页需要用的信息
	 * @param hubSpu
	 * @return
	 */
	private HubProductDetails getHubProductDetailsFromHubSpu(HubSpuDto hubSpu){
		HubProductDetails hubProductDetails = new HubProductDetails();
		hubProductDetails.setSpuId(hubSpu.getSpuId()); 
		hubProductDetails.setBrandNo(hubSpu.getBrandNo());
		hubProductDetails.setCategoryNo(hubSpu.getCategoryNo());
		hubProductDetails.setHomeMarketPrice("");//TODO
		hubProductDetails.setOriginalProductModle("");//TODO
		hubProductDetails.setProductName(hubSpu.getSpuName());
		hubProductDetails.setProductUnit("");//TODO
		return hubProductDetails;
	}
	
	/**
	 * 赋值转换
	 * @param hubSpu
	 * @param hubSku
	 * @return
	 */
	private List<HubProductDetail> getProductDetailsByHubSku(HubSpuDto hubSpu,HubSkuDto hubSku){
		
		List<HubProductDetail> productDetails = new ArrayList<HubProductDetail>();
		List<HubSkuSupplierMappingDto> mappings = findHubSkuSupplierMappingDtoByHubSkuNo(hubSku.getSkuId());
		for(HubSkuSupplierMappingDto mappingDto : mappings){
			HubProductDetail hubProuct = new HubProductDetail();
			JavaUtil.fatherToChild(mappingDto, hubProuct);			
			hubProuct.setSupplierName("");//TODO 
			hubProuct.setSkuId(hubSku.getSkuId());
			hubProuct.setSkuSize(hubSku.getSkuSize());
			hubProuct.setColor(hubSpu.getHubColor()); 	
			if(HubSpuState.ON_SALE.getIndex() == hubSpu.getSpuState()){
				hubProuct.setSpuState(HubSpuState.ON_SALE.getDescription());
			}	
			productDetails.add(hubProuct);
		}
		return productDetails;
	}
	
	/**
	 * 根据HubSkuNo查找供应商原始的一些信息
	 * @param hubSkuNo
	 * @return
	 */
	private List<HubSkuSupplierMappingDto> findHubSkuSupplierMappingDtoByHubSkuNo(Long hubSkuNo){
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSkuNoEqualTo(hubSkuNo);
		List<HubSkuSupplierMappingDto>  lists = hubSkuSupplierMappingClient.selectByCriteria(criteriaDto);
		if(null != lists && lists.size()>0){
			return lists;
		}else{
			return null;
		}
			
	}

}
