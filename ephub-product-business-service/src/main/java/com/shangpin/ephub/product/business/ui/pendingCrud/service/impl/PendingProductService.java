package com.shangpin.ephub.product.business.ui.pendingCrud.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.SupplierDTO;
import com.shangpin.ephub.product.business.ui.pendingCrud.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pendingCrud.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProducts;
import com.shangpin.ephub.product.business.util.DateTimeUtil;
/**
 * <p>Title:PendingProductService </p>
 * <p>Description: 待处理页面Service实现类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:17:57
 *
 */
@Service
public class PendingProductService implements IPendingProductService{
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	private HubSupplierCategroyDicGateWay categroyDicGateWay;
	@Autowired
	private HubSupplierBrandDicGateWay brandDicGateWay;
	@Autowired
	private RestTemplate httpClient;

	@Override
	public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto){
		PendingProducts pendingProducts = new PendingProducts();
		List<PendingProductDto> products = new ArrayList<PendingProductDto>();
		if(null !=pendingQuryDto){
			HubSpuPendingCriteriaDto rowBoundsDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
			int total = hubSpuPendingGateWay.countByCriteria(rowBoundsDto);
			if(total>0){
				List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteria(rowBoundsDto);
				for(HubSpuPendingDto pendingSpu : pendingSpus){
					PendingProductDto pendingProduct = convertHubSpuPendingDto2PendingProductDto(pendingSpu);
					pendingProduct.setHubCategoryName(getHubCategoryName(pendingProduct.getSupplierId(),pendingProduct.getHubCategoryNo()));
					pendingProduct.setHubBrandName(getHubBrandName(pendingProduct.getSupplierId(),pendingProduct.getHubBrandNo()));
					List<HubSkuPendingDto> hubSkus = findPendingSkuBySpuPendingId(pendingSpu.getSpuPendingId());
					pendingProduct.setHubSkus(hubSkus);
					products.add(pendingProduct);
				}
				pendingProducts.setProduts(products);
			}
			pendingProducts.setTotal(total); 
		}
		return pendingProducts;
	}	
	@Override
	public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId){
		HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
		criteriaDto.setFields("sku_pending_id,hub_sku_size,sp_sku_size_state");
		return hubSkuPendingGateWay.selectByCriteria(criteriaDto);		
	}
	@Override
	public boolean updatePendingProduct(PendingProductDto pendingProductDto){
		try {
			if(null != pendingProductDto){
				//TODO 先验证，验证通过则更新
				hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
				List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
				if(null != pengdingSkus && pengdingSkus.size()>0){
					for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){
						//TODO 先验证，验证通过则更新
						int result = hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	@Override
	public boolean batchUpdatePendingProduct(List<PendingProductDto> pendingProducts){
		try {
			if(null != pendingProducts && pendingProducts.size()>0){
				for(PendingProductDto pendingProductDto : pendingProducts){
					updatePendingProduct(pendingProductDto);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	@Override
	public boolean updatePendingProductToUnableToProcess(Long spuPendingId){
		try {
			if(!StringUtils.isEmpty(spuPendingId)){
				HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
				hubSpuPendingDto.setSpuPendingId(spuPendingId);
				hubSpuPendingDto.setSpuState(SpuState.UNABLE_TO_PROCESS.getIndex());
				hubSpuPendingGateWay.updateByPrimaryKeySelective(hubSpuPendingDto);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	@Override
	public boolean batchUpdatePendingProductToUnableToProcess(List<Long> spuPendingIds){
		try {
			if(null != spuPendingIds && spuPendingIds.size()>0){
				for(Long spuPendingId : spuPendingIds){
					updatePendingProductToUnableToProcess(spuPendingId);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	/***************************************************************************************************************************/
						//以下为内部调用私有方法
	/**************************************************************************************************************************/
	/**
	 * 根据门户id和品牌编号查找品牌名称
	 * @param supplierId
	 * @param hubBrandNo
	 * @return
	 */
	private String getHubBrandName(String supplierId,String hubBrandNo){
		if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(hubBrandNo)){
			return "";
		}
		HubSupplierBrandDicCriteriaDto brandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
		brandDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubBrandNoEqualTo(hubBrandNo);
		List<HubSupplierBrandDicDto> brandNames = brandDicGateWay.selectByCriteria(brandDicCriteriaDto);
		if(null != brandNames && brandNames.size()>0){
			return brandNames.get(0).getSupplierBrand();
		}else{
			return "";
		}
	}
	/**
	 * 根据供应商门户id和品类编号查找品类名称
	 * @param supplierId
	 * @param categoryNo
	 * @return
	 */
	private String getHubCategoryName(String supplierId,String categoryNo){
		if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(categoryNo)){
			return "";
		}
		HubSupplierCategroyDicCriteriaDto categroyDicCriteriaDto = new HubSupplierCategroyDicCriteriaDto();
		categroyDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubCategoryNoEqualTo(categoryNo);
		List<HubSupplierCategroyDicDto> categoryNames = categroyDicGateWay.selectByCriteria(categroyDicCriteriaDto);
		if(null != categoryNames && categoryNames.size()>0){
			return categoryNames.get(0).getSupplierCategory();
		}else{
			return "";
		}
	}
	
	/**
	 * 将pendingSpu转化为pendingProduct
	 * @param pendingSpu
	 * @return
	 */
	private PendingProductDto convertHubSpuPendingDto2PendingProductDto(HubSpuPendingDto pendingSpu){
		PendingProductDto pendingProduct = new PendingProductDto();	
		pendingProduct.setSpuPendingId(pendingSpu.getSpuPendingId());
		pendingProduct.setSupplierId(pendingSpu.getSupplierId());
		pendingProduct.setSupplierSpuNo(pendingSpu.getSupplierSpuNo());
		pendingProduct.setSpuModel(pendingSpu.getSpuModel());
		pendingProduct.setSpuName(pendingSpu.getSpuName());
		pendingProduct.setHubGender(pendingSpu.getHubGender());
		pendingProduct.setHubCategoryNo(pendingSpu.getHubCategoryNo());
		pendingProduct.setHubBrandNo(pendingSpu.getHubBrandNo());
		pendingProduct.setHubSeason(pendingSpu.getHubSeason());
		pendingProduct.setSpSkuSizeState(pendingSpu.getSpSkuSizeState());
		pendingProduct.setSpuState(pendingSpu.getSpuState());
		pendingProduct.setPicState(pendingSpu.getPicState());
		pendingProduct.setIsCurrentSeason(pendingSpu.getIsCurrentSeason());
		pendingProduct.setIsNewData(pendingSpu.getIsNewData());
		pendingProduct.setHubMaterial(pendingSpu.getHubMaterial());
		pendingProduct.setHubOrigin(pendingSpu.getHubOrigin());
		pendingProduct.setCreateTime(pendingSpu.getCreateTime());
		pendingProduct.setUpdateTime(pendingSpu.getUpdateTime());
		pendingProduct.setSpuDesc(pendingSpu.getSpuDesc());
		pendingProduct.setHubSpuNo(pendingSpu.getHubSpuNo());
		pendingProduct.setSpuModelState(pendingSpu.getSpuModelState());
		pendingProduct.setCatgoryState(pendingSpu.getCatgoryState());
		pendingProduct.setSupplierSpuId(pendingSpu.getSupplierSpuId());
		pendingProduct.setMemo(pendingSpu.getMemo());
		pendingProduct.setDataState(pendingSpu.getDataState());
		pendingProduct.setVersion(pendingSpu.getVersion());
		pendingProduct.setSpuBrandState(pendingSpu.getSpuBrandState());
		pendingProduct.setSpuGenderState(pendingSpu.getSpuGenderState());
		pendingProduct.setSpuSeasonState(pendingSpu.getSpuSeasonState());
		pendingProduct.setHubColorNo(pendingSpu.getHubColorNo());
		pendingProduct.setHubColor(pendingSpu.getHubColor());
		pendingProduct.setSpuColorState(pendingSpu.getSpuColorState());
//		JavaUtil.fatherToChild(pendingSpu, pendingProduct); 
		return pendingProduct;
	}
	
	/**
	 * 将UI查询条件转换成数据库查询条件对象
	 * @param pendingQuryDto UI查询条件对象
	 * @return
	 */
	private HubSpuPendingCriteriaDto findhubSpuPendingCriteriaFromPendingQury(PendingQuryDto pendingQuryDto){
		
		HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
		if(!StringUtils.isEmpty(pendingQuryDto.getPageIndex()) && !StringUtils.isEmpty(pendingQuryDto.getPageSize())){
			hubSpuPendingCriteriaDto.setPageNo(pendingQuryDto.getPageIndex());
			hubSpuPendingCriteriaDto.setPageSize(pendingQuryDto.getPageSize());
		}
		Criteria criteria = hubSpuPendingCriteriaDto.createCriteria();
		
		if(!StringUtils.isEmpty(pendingQuryDto.getSupplierNo())){
			String supplierId = getSupplierIdBySupplierNo(pendingQuryDto.getSupplierNo());
			if(!StringUtils.isEmpty(supplierId)){
				criteria = criteria.andSupplierIdEqualTo(supplierId);
			}
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getSpuModel())){
			criteria = criteria.andSpuModelEqualTo(pendingQuryDto.getSpuModel());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubCategoryNo())){
			criteria = criteria.andHubCategoryNoLike(pendingQuryDto.getHubCategoryNo());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubBrandNo())){
			criteria = criteria.andHubBrandNoLike(pendingQuryDto.getHubBrandNo());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && !StringUtils.isEmpty(pendingQuryDto.getHubYear())){
			criteria = criteria.andHubSeasonEqualTo(pendingQuryDto.getHubYear()+"_"+pendingQuryDto.getHubSeason());
		}else if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && StringUtils.isEmpty(pendingQuryDto.getHubYear())){
			criteria = criteria.andHubSeasonLike(pendingQuryDto.getHubSeason());
		}else if(StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && !StringUtils.isEmpty(pendingQuryDto.getHubYear())){
			criteria = criteria.andHubSeasonLike(pendingQuryDto.getHubYear());
		}		
		if(!StringUtils.isEmpty(pendingQuryDto.getStatTime())){
			criteria = criteria.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.convertFormat(pendingQuryDto.getStatTime(), dateFormat));
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getEndTime())){
			criteria = criteria.andUpdateTimeLessThan(DateTimeUtil.convertFormat(pendingQuryDto.getEndTime(),dateFormat));
		}			
		return hubSpuPendingCriteriaDto;
	}
	/**
	 * 跟据不符合項，筛选不符合的产品
	 * @param pendingQuryDto UI查询条件
	 * @param pendingProduct 待验证的产品，需要验证图片/品牌/颜色/货号等等是否不符合，如果不符合则需要返回
	 * @param products 不符合项需要添加的List
	 */
	private void screenProduct(PendingQuryDto pendingQuryDto,PendingProductDto pendingProduct,List<PendingProductDto> products){
		if(null != pendingQuryDto.getInconformities() && pendingQuryDto.getInconformities().size()>0){
			for(Integer item : pendingQuryDto.getInconformities()){
				if(item == ProductState.PICTURE_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_MODEL_STATE.getIndex()){
					
				}else if(item == ProductState.CATGORY_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_BRAND_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_GENDER_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_SEASON_STATE.getIndex()){
					
				}else if(item == ProductState.SPU_COLOR_STATE.getIndex()){
					
				}else if(item == ProductState.MATERIAL_STATE.getIndex()){

				}else if(item == ProductState.ORIGIN_STATE.getIndex()){

				}else if(item == ProductState.SIZE_STATE.getIndex()){
					
				}
			}
		}
	}
	
	/**
	 * 通过api接口查询供应商门户id
	 * @param supplierNo
	 * @return
	 */
	private String getSupplierIdBySupplierNo(String supplierNo){
		String url = "http://scm.shangpin.com/scms/Supplier/GetSupplierInfoListByNo?supplierNo="+supplierNo;
		SupplierDTO supplierDto = httpClient.getForEntity(url, SupplierDTO.class).getBody();
		if(null != supplierDto){
			return supplierDto.getSupplierNo();
		}
		return "";
	}
}
