package com.shangpin.pending.product.consumer.supplier.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.enumeration.PicHandleState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.pending.product.consumer.common.enumeration.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubMaterialMappingGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemDto;
import com.shangpin.ephub.client.data.mysql.material.gateway.HubMaterialDicGateWay;
import com.shangpin.ephub.client.data.mysql.material.gateway.HubMaterialDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.supplier.dto.ColorDTO;
import com.shangpin.pending.product.consumer.supplier.dto.MaterialDTO;
import com.shangpin.pending.product.consumer.supplier.dto.SpuPending;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class DataServiceHandler {

	@Autowired
	private HubBrandDicGateway brandDicGateway;

	@Autowired
	private HubSupplierBrandDicGateWay supplierBrandDicGateWay;

	@Autowired
	private HubSpuGateWay hubSpuGateWay;

	@Autowired
	private HubGenderDicGateWay hubGenderDicGateWay;

	@Autowired
	private HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;

	@Autowired
	private HubColorDicItemGateWay hubColorDicItemGateWay;

	@Autowired
	private HubColorDicGateWay hubColorDicGateWay;

	@Autowired
	private HubSeasonDicGateWay hubSeasonDicGateWay;

	@Autowired
	private HubMaterialDicGateWay hubMaterialDicGateWay;

	@Autowired
	private HubMaterialDicItemGateWay hubMaterialDicItemGateWay;

	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;

	@Autowired
	private HubSkuPendingGateWay hubSkuPendingGateWay;

	@Autowired
	private HubBrandModelRuleGateWay hubBrandModelRuleGateWay;

	@Autowired
	private HubMaterialMappingGateWay hubMaterialMappingGateWay;

	@Autowired
	private HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

	@Autowired
	private HubSkuGateWay hubSkuGateWay;

	@Autowired
	private HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

	@Autowired
	private HubSupplierSkuGateWay supplierSkuGateWay;

	@Autowired
	private HubSpuPendingPicGateWay pendingPicGateWay;

	@Autowired
	private HubSupplierSpuGateWay supplierSpuGateWay;

	/**
	 * 查询所有有效的供应商品牌
	 * 
	 * @return
	 */
	public List<HubSupplierBrandDicDto> getEffectiveHubSupplierBrands() {
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		criteria.createCriteria().andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());
		criteria.setFields("supplier_id,supplier_brand,filter_flag");
		return supplierBrandDicGateWay.selectByCriteria(criteria);
	}

	public HubSupplierBrandDicDto getHubSupplierBrand(String supplierId, String supplierBrandName) {
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierBrandEqualTo(supplierBrandName);
		List<HubSupplierBrandDicDto> hubSupplierBrandDicDtos = supplierBrandDicGateWay.selectByCriteria(criteria);
		if (null != hubSupplierBrandDicDtos && hubSupplierBrandDicDtos.size() > 0) {
			return hubSupplierBrandDicDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveBrand(String supplierId, String supplierBrandName) throws Exception {
		// HubBrandDicDto brandDicDto = new HubBrandDicDto();
		// brandDicDto.setCreateTime(new Date());
		// brandDicDto.setSupplierBrand(supplierBrandName);
		// brandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		// brandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
		// int insert = brandDicGateway.insert(brandDicDto);

		if (null != getHubSupplierBrand(supplierId, supplierBrandName)) {// 重复不做处理
			return;
		}
		HubSupplierBrandDicDto supplierBrandDicDto = new HubSupplierBrandDicDto();
		supplierBrandDicDto.setSupplierId(supplierId);
		supplierBrandDicDto.setSupplierBrand(supplierBrandName);
		supplierBrandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		supplierBrandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
		try {
			supplierBrandDicGateWay.insert(supplierBrandDicDto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}

	}

	public List<HubBrandDicDto> getBrand() throws Exception {
		HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(ConstantProperty.MAX_BRANDK_MAPPING_QUERY_NUM);
		HubBrandDicCriteriaDto.Criteria criterion = criteria.createCriteria();

		return brandDicGateway.selectByCriteria(criteria);

	}

	public HubSpuDto getHubSpuByBrandNoAndProductModel(String brandNo, String spuModel) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		HubSpuCriteriaDto.Criteria criterion = criteria.createCriteria();

		criterion.andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel);

		List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(criteria);
		if (null != hubSpuDtos && hubSpuDtos.size() > 0) {
			return hubSpuDtos.get(0);
		} else {
			return null;
		}
	}

	public HubSpuDto getHubSpuByHubBrandNoAndProductModel(String brandNo, String spuModel) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		HubSpuCriteriaDto.Criteria criterion = criteria.createCriteria();

		criterion.andSpuModelEqualTo(spuModel).andBrandNoEqualTo(brandNo);

		List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(criteria);
		if (null != hubSpuDtos && hubSpuDtos.size() > 0) {
			return hubSpuDtos.get(0);
		} else {
			return null;
		}
	}

	public List<HubGenderDicDto> getHubGenderDicBySupplierId(String supplierId) {
		HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);

		HubGenderDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andPushStateEqualTo(DataBusinessStatus.PUSH.getIndex().byteValue());
		if (StringUtils.isNotBlank(supplierId)) {
			criterion.andSupplierIdEqualTo(supplierId);

		}
		return hubGenderDicGateWay.selectByCriteria(criteria);
	}

	public void saveHubGender(String supplierId, String supplierGender) throws Exception {
		if (StringUtils.isBlank(supplierGender)) {// 供货商的性别为空时 不做处理
			return;
		}
		// 如果存在 不再保存
		if (null != this.getHubGenderDicBySupplierIdAndSupplierGender(supplierId, supplierGender)) {
			return;
		}

		HubGenderDicDto hubGenderDicDto = new HubGenderDicDto();
		hubGenderDicDto.setCreateTime(new Date());
		hubGenderDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		hubGenderDicDto.setSupplierId(null);
		hubGenderDicDto.setSupplierGender(supplierGender);
		hubGenderDicDto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
		try {
			hubGenderDicGateWay.insert(hubGenderDicDto);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof DuplicateKeyException) {

			} else {
				throw e;
			}

		}
	}

	public HubGenderDicDto getHubGenderDicBySupplierIdAndSupplierGender(String supplierId, String supplierGender) {
		if (StringUtils.isBlank(supplierGender)) {
			return null;
		}

		HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		HubGenderDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		if (StringUtils.isNotBlank(supplierId)) {
			criterion.andSupplierIdEqualTo(supplierId);
		}

		criterion.andSupplierGenderEqualTo(supplierGender);
		List<HubGenderDicDto> hubGenderDicDtos = hubGenderDicGateWay.selectByCriteria(criteria);
		if (null != hubGenderDicDtos && hubGenderDicDtos.size() > 0) {
			return hubGenderDicDtos.get(0);
		} else {
			return null;
		}
	}

	public List<HubSupplierCategroyDicDto> getSupplierCategoryBySupplierId(String supplierId) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andSupplierIdEqualTo(supplierId)
				.andPushStateEqualTo(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
		return hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
	}

	public HubSupplierCategroyDicDto getSupplierCategoryBySupplierIdAndSupplierCategoryAndSupplierGender(
			String supplierId, String supplierCategory, String supplierGender) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andSupplierIdEqualTo(supplierId).andSupplierCategoryEqualTo(supplierCategory);
		if (StringUtils.isBlank(supplierCategory)) {
			criterion.andSupplierGenderIsNull();
		} else {
			criterion.andSupplierGenderEqualTo(supplierGender);
		}
		List<HubSupplierCategroyDicDto> hubSupplierCategroyDicDtos = hubSupplierCategroyDicGateWay
				.selectByCriteria(criteria);
		if (null != hubSupplierCategroyDicDtos && hubSupplierCategroyDicDtos.size() > 0) {
			return hubSupplierCategroyDicDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveHubCategory(String supplierId, String supplierCategory, String supplierGender) throws Exception {

		// 先获取性别字典的值 目的是品类映射表需要性别字典的主键
		HubGenderDicDto hubGenderDicDto = this.getHubGenderDicBySupplierIdAndSupplierGender(null, supplierGender);

		HubSupplierCategroyDicDto dto = new HubSupplierCategroyDicDto();

		if (null != getSupplierCategoryBySupplierIdAndSupplierCategoryAndSupplierGender(supplierId, supplierCategory,
				supplierGender)) {
			return;
		}

		dto.setSupplierId(supplierId);
		dto.setSupplierCategory(supplierCategory);
		dto.setSupplierGender(supplierGender);
		dto.setMappingState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
		dto.setGenderDicId(null == hubGenderDicDto ? null : hubGenderDicDto.getGenderDicId());
		dto.setCreateTime(new Date());
		try {
			hubSupplierCategroyDicGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}

	}

	/**
	 * 组装颜色数据
	 * 
	 * @return
	 */
	public List<ColorDTO> getColorDTO() {

		HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COLOR_ITEM_QUERY_NUM);
		criteria.setPageNo(1);
		List<HubColorDicItemDto> hubColorDicItemDtos = hubColorDicItemGateWay.selectByCriteria(criteria);

		HubColorDicCriteriaDto colorDicCriteriaDto = new HubColorDicCriteriaDto();
		colorDicCriteriaDto.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		colorDicCriteriaDto.setPageNo(1);
		List<HubColorDicDto> dicDtos = hubColorDicGateWay.selectByCriteria(colorDicCriteriaDto);
		Map<Long, HubColorDicDto> colorDicMap = new HashMap<>();
		for (HubColorDicDto colorDicDto : dicDtos) {
			colorDicMap.put(colorDicDto.getColorDicId(), colorDicDto);
		}

		List<ColorDTO> colorDTOS = new ArrayList<>();

		for (HubColorDicItemDto itemDto : hubColorDicItemDtos) {
			ColorDTO colorDTO = new ColorDTO();
			colorDTO.setColorItemId(itemDto.getColorDicItemId());
			colorDTO.setSupplierColor(itemDto.getColorItemName().trim());
			if (colorDicMap.containsKey(itemDto.getColorDicId())) {
				colorDTO.setColorDicId(itemDto.getColorDicId());
				colorDTO.setSupplierColor(itemDto.getColorItemName());
				colorDTO.setHubColorNo(colorDicMap.get(itemDto.getColorDicId()).getColorNo());
				colorDTO.setHubColorName(colorDicMap.get(itemDto.getColorDicId()).getColorName());
			}
			colorDTOS.add(colorDTO);
		}
		return colorDTOS;

	}

	public HubColorDicItemDto getHubColorDicItem(String supplierColor) {
		if (StringUtils.isBlank(supplierColor))
			return null;

		HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();
		criteria.createCriteria().andColorItemNameEqualTo(supplierColor);
		List<HubColorDicItemDto> hubColorDicItemDtos = hubColorDicItemGateWay.selectByCriteria(criteria);
		if (null != hubColorDicItemDtos && hubColorDicItemDtos.size() > 0) {
			return hubColorDicItemDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveColorItem(String supplierColor) throws Exception {
		// 供货商数据为空 不插入
		if (StringUtils.isBlank(supplierColor))
			return;

		// 查询是否存在
		if (null != this.getHubColorDicItem(supplierColor)) {
			return;
		}
		HubColorDicItemDto dto = new HubColorDicItemDto();
		dto.setCreateTime(new Date());
		dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		dto.setColorItemName(supplierColor);
		try {
			hubColorDicItemGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * 查询所有有效的供应商季节
	 * 
	 * @return
	 */
	public List<HubSeasonDicDto> getEffectiveHubSeasons() {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.createCriteria().andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());
		criteria.setFields("supplierid,supplier_season,filter_flag");
		return hubSeasonDicGateWay.selectByCriteria(criteria);
	}

	public List<HubSeasonDicDto> getHubSeasonDic() {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		HubSeasonDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		return hubSeasonDicGateWay.selectByCriteria(criteria);

	}

	public HubSeasonDicDto getHubSeasonDicBySupplierIdAndsupplierSeason(String supplierId, String supplierSeason) {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason);
		List<HubSeasonDicDto> hubSeasonDicDtos = hubSeasonDicGateWay.selectByCriteria(criteria);
		if (null != hubSeasonDicDtos && hubSeasonDicDtos.size() > 0) {
			return hubSeasonDicDtos.get(0);
		} else {
			return null;
		}

	}

	public void saveSeason(String supplierId, String supplierSeason) {
		// 先查询实付存在 存在不做处理
		if (null != this.getHubSeasonDicBySupplierIdAndsupplierSeason(supplierId, supplierSeason)) {
			return;
		}
		HubSeasonDicDto dto = new HubSeasonDicDto();
		dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		dto.setCreateTime(new Date());
		dto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
		dto.setSupplierid(supplierId);
		dto.setSupplierSeason(supplierSeason);
		try {
			hubSeasonDicGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public List<MaterialDTO> getMaterialDTO() {

		HubMaterialDicItemCriteriaDto criteria = new HubMaterialDicItemCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_MATERIAL_QUERY_NUM);
		HubMaterialDicItemCriteriaDto.Criteria criterio = criteria.createCriteria();
		criteria.setOrderByClause("memo");// 完全的最小 单个单词最大
		List<HubMaterialDicItemDto> hubMaterialDicItemDtos = hubMaterialDicItemGateWay.selectByCriteria(criteria);

		HubMaterialDicCriteriaDto dicCriteriaDto = new HubMaterialDicCriteriaDto();
		List<HubMaterialDicDto> dicDtos = hubMaterialDicGateWay.selectByCriteria(dicCriteriaDto);
		Map<Long, HubMaterialDicDto> materialDicMap = new HashMap<>();
		for (HubMaterialDicDto dto : dicDtos) {
			materialDicMap.put(dto.getMaterialDicId(), dto);
		}

		List<MaterialDTO> materialDTOS = new ArrayList<>();

		for (HubMaterialDicItemDto itemDto : hubMaterialDicItemDtos) {
			MaterialDTO materialDTO = new MaterialDTO();
			materialDTO.setMaterialItemId(itemDto.getMaterialDicItemId());
			materialDTO.setSupplierMaterial(itemDto.getMaterialItemName());
			if (materialDicMap.containsKey(itemDto.getMaterialDicId())) {
				materialDTO.setMaterialDicId(itemDto.getMaterialDicId());
				materialDTO.setHubMaterial(materialDicMap.get(itemDto.getMaterialDicId()).getMaterialName());
			}

			materialDTOS.add(materialDTO);
		}
		return materialDTOS;

	}

	public List<MaterialDTO> getMaterialMapping() {

		HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
		criteria.setOrderByClause("mapping_level");
		criteria.setPageSize(ConstantProperty.MAX_MATERIAL_QUERY_NUM);
		List<HubMaterialMappingDto> hubMaterialMappingDtos = hubMaterialMappingGateWay.selectByCriteria(criteria);
		List<MaterialDTO> materialDTOS = new ArrayList<>();

		for (HubMaterialMappingDto itemDto : hubMaterialMappingDtos) {
			MaterialDTO materialDTO = new MaterialDTO();
			if (StringUtils.isBlank(itemDto.getSupplierMaterial()) || StringUtils.isBlank(itemDto.getHubMaterial()))
				continue;

			materialDTO.setSupplierMaterial(itemDto.getSupplierMaterial());
			materialDTO.setHubMaterial(itemDto.getHubMaterial());

			materialDTOS.add(materialDTO);
		}
		return materialDTOS;

	}

	public void savePendingSpu(HubSpuPendingDto spuPending) throws Exception {
		// 替换材质中的html 代码
		if (StringUtils.isNotBlank(spuPending.getHubMaterial())) {
			spuPending.setHubMaterial(spuPending.getHubMaterial().replaceAll("<br />", "\r\n").replaceAll("<html>", "")
					.replaceAll("</html>", "").replaceAll("<br>","\r\n"));
		}
		if (null != spuPending.getIsCurrentSeason()) {
			if (SeasonType.SEASON_NOT_CURRENT.getIndex() == spuPending.getIsCurrentSeason().intValue()) {
				spuPending.setSpuState(SpuStatus.SPU_FILTER.getIndex().byteValue());
			}
		}
//		if (null != spuPending.getSpuBrandState()) {
//			if (PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex() == spuPending.getSpuBrandState().intValue()) {
//				spuPending.setSpuState(SpuStatus.SPU_FILTER.getIndex().byteValue());
//			}
//		}

		Long spuPendingId = hubSpuPendingGateWay.insert(spuPending);
		spuPending.setSpuPendingId(spuPendingId);

	}

	public void updatePendingSpu(Long spuKey, HubSpuPendingDto spuPending) throws Exception {
		try {

			HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
			criteria.createCriteria().andSpuPendingIdEqualTo(spuKey);
			spuPending.setUpdateTime(new Date());
			HubSpuPendingWithCriteriaDto updateByCriteriaSelective = new HubSpuPendingWithCriteriaDto(spuPending,
					criteria);
			hubSpuPendingGateWay.updateByCriteriaSelective(updateByCriteriaSelective);
//			hubSpuPendingGateWay.insert(spuPending);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public HubSpuPendingDto getSpuPendingDTO(String supplierId, String supplierSpuNo) {
		HubSpuPendingCriteriaDto criterial = new HubSpuPendingCriteriaDto();
		HubSpuPendingCriteriaDto.Criteria criterion = criterial.createCriteria();
		criterion.andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSpuPendingDto> hubSpuPendingDtos = hubSpuPendingGateWay.selectByCriteria(criterial);
		if (null != hubSpuPendingDtos && hubSpuPendingDtos.size() > 0) {
			return hubSpuPendingDtos.get(0);
		} else {

			return null;
		}
	}

	public void savePendingSku(HubSkuPendingDto skuPendingDto) throws Exception {
		try {
			Long skuPendingId = hubSkuPendingGateWay.insert(skuPendingDto);
			skuPendingDto.setSpuPendingId(skuPendingId);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public List<HubBrandModelRuleDto> getBrandModle(String hubBrandNo) {

		HubBrandModelRuleCriteriaDto criterial = new HubBrandModelRuleCriteriaDto();
		criterial.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criterial.createCriteria().andHubBrandNoEqualTo(hubBrandNo)
				.andRuleStateEqualTo(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

		return hubBrandModelRuleGateWay.selectByCriteria(criterial);

	}

	public HubSpuPendingDto getHubSpuPending(String supplierId, String supplierSpuNo) {
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSpuPendingDto> hubSpuPendingDtos = hubSpuPendingGateWay.selectByCriteria(criteria);
		if (null != hubSpuPendingDtos && hubSpuPendingDtos.size() > 0) {
			return hubSpuPendingDtos.get(0);
		} else {
			return null;
		}
	}

	public HubSpuPendingDto getSpuPendingById(Long  id) {
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.createCriteria().andSpuPendingIdEqualTo(id);
		List<HubSpuPendingDto> hubSpuPendingDtos = hubSpuPendingGateWay.selectByCriteria(criteria);
		if (null != hubSpuPendingDtos && hubSpuPendingDtos.size() > 0) {
			return hubSpuPendingDtos.get(0);
		} else {
			return null;
		}
	}

	public HubSkuPendingDto getHubSkuPending(String supplierId, String supplierSkuNo) {
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSkuPendingDto> hubSkuPendingDtos = hubSkuPendingGateWay.selectByCriteria(criteria);
		if (null != hubSkuPendingDtos && hubSkuPendingDtos.size() > 0) {
			return hubSkuPendingDtos.get(0);
		} else {
			return null;
		}
	}


	public void updateSkuPengding(HubSkuPendingDto skuPendingDto){		;
		hubSkuPendingGateWay.updateByPrimaryKeySelective(skuPendingDto);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingByType(Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getSupplierCommonSizeValueMapping() {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setOrderByClause("sort_val");
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(String supplierId,
			Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.setPageNo(1);
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public HubSkuDto getHubSku(String spuNo, String skuSizeId) {
		HubSkuCriteriaDto criteria = new HubSkuCriteriaDto();
		criteria.createCriteria().andSpuNoEqualTo(spuNo).andSkuSizeIdEqualTo(skuSizeId);
		List<HubSkuDto> hubSkuDtos = hubSkuGateWay.selectByCriteria(criteria);
		if (null != hubSkuDtos && hubSkuDtos.size() > 0) {
			return hubSkuDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveSkuSupplierMapping(SpuPending hubSpuPending, HubSkuPendingDto skuPendingDto, PendingSpu supplierSpu,
			PendingSku sku) {

		HubSkuSupplierMappingDto skuSupplierMapping = new HubSkuSupplierMappingDto();
		skuSupplierMapping.setBarcode(skuPendingDto.getSupplierBarcode());
		skuSupplierMapping.setCreateTime(skuPendingDto.getCreateTime());
		skuSupplierMapping.setUpdateTime(skuPendingDto.getCreateTime());
		skuSupplierMapping.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		skuSupplierMapping.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
		skuSupplierMapping.setSupplierSpuModel(supplierSpu.getSpuModel());
		skuSupplierMapping.setSupplierSkuNo(sku.getSupplierSkuNo());
		HubSupplierSkuDto supplierSkuDto = this.getSupplierSku(sku.getSupplierId(), sku.getSupplierSkuNo());
		if (null != supplierSkuDto) {
			skuSupplierMapping.setSupplierSkuId(supplierSkuDto.getSupplierSkuId());

		}
		skuSupplierMapping.setSupplierSelectState(SupplierSelectState.WAIT_SELECT.getIndex().byteValue());

		skuSupplierMappingGateWay.insert(skuSupplierMapping);

	}

	public HubSkuDto insertHubSku(String hubSpuNo, String color, Date date, HubSkuPendingDto hubSkuPending)
			throws Exception {
		HubSkuDto hubSku = new HubSkuDto();

		hubSku.setSpuNo(hubSpuNo);
		hubSku.setSkuNo(hubSkuGateWay.createSkuNo(hubSpuNo));
		hubSku.setColor(color);
		hubSku.setSkuSize(hubSkuPending.getHubSkuSize());
		hubSku.setSkuSizeId(hubSkuPending.getScreenSize());
		hubSku.setCreateTime(date);
		hubSku.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		hubSku.setUpdateTime(date);
		hubSkuGateWay.insert(hubSku);
		return hubSku;
	}

	private HubSupplierSkuDto getSupplierSku(String supplierId, String supplierSkuNo) {
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSupplierSkuDto> hubSupplierSkuDtos = supplierSkuGateWay.selectByCriteria(criteria);
		if (null != hubSupplierSkuDtos && hubSupplierSkuDtos.size() > 0) {
			return hubSupplierSkuDtos.get(0);
		} else {
			return null;
		}
	}

	public String getPicUrlBySupplierSpuId(Long supplierSpuId) {
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId)
				.andPicHandleStateEqualTo(PicHandleState.HANDLED.getIndex());

		List<HubSpuPendingPicDto> hubSpuPendingPicDtos = pendingPicGateWay.selectByCriteria(criteria);
		if (null != hubSpuPendingPicDtos && hubSpuPendingPicDtos.size() > 0) {
			return hubSpuPendingPicDtos.get(0).getSpPicUrl();
		} else {
			return "";
		}
	}

	public HubSupplierSpuDto getHubSupplierSpuBySupplierIdAndSupplierSpuNo(String supplierId,String supplierSpuNo){
	    HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
	    criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSupplierSpuDto> hubSupplierSpuDtos = supplierSpuGateWay.selectByCriteria(criteria);
		if(null!=hubSupplierSpuDtos&&hubSupplierSpuDtos.size()>0){
			return  hubSupplierSpuDtos.get(0);
		}
		return null;
	}

}
