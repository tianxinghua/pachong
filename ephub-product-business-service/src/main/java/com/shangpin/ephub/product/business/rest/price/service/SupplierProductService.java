package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.price.unionselect.dto.PriceQueryDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

@Service
public class SupplierProductService {
	
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired 
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	private HubSeasonDicGateWay hubSeasonDicGateWay;
	
	/**
	 * 根据接口传入条件生成数据库查询条件
	 * @param priceQueryDto
	 * @return
	 */
	public HubSupplierSkuCriteriaDto findCriteriaDto(PriceQueryDto priceQueryDto){
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setOrderByClause("create_time desc");
		criteriaDto.createCriteria().andSpSkuNoIsNotNull();
		if(CollectionUtils.isNotEmpty(priceQueryDto.getSpSkuIds())){
			criteriaDto.setPageNo(1);
			criteriaDto.setPageSize(1000); 
			criteriaDto.createCriteria().andSpSkuNoIn(priceQueryDto.getSpSkuIds());
			return criteriaDto;
		}else{
			if(null != priceQueryDto.getPageIndex() && null != priceQueryDto.getPageSize()){
				criteriaDto.setPageNo(priceQueryDto.getPageIndex());
				criteriaDto.setPageSize(priceQueryDto.getPageSize());
				if(!StringUtils.isEmpty(priceQueryDto.getSupplierId())){
					criteriaDto.createCriteria().andSupplierIdEqualTo(priceQueryDto.getSupplierId());
				}
				if(!StringUtils.isEmpty(priceQueryDto.getMarketSeason()) && !StringUtils.isEmpty(priceQueryDto.getMarketYear())){
					List<Long> values = findSupplierSpuId(findSupplierSeasons(priceQueryDto.getMarketSeason(),priceQueryDto.getMarketYear()));
					criteriaDto.createCriteria().andSupplierSpuIdIn(values);
				}else if(StringUtils.isEmpty(priceQueryDto.getMarketSeason()) && !StringUtils.isEmpty(priceQueryDto.getMarketYear())){
					List<Long> values = findSupplierSpuId(findSupplierSeasons("",priceQueryDto.getMarketYear()));
					criteriaDto.createCriteria().andSupplierSpuIdIn(values);
				}else if(!StringUtils.isEmpty(priceQueryDto.getMarketSeason()) && StringUtils.isEmpty(priceQueryDto.getMarketYear())){
					List<Long> values = findSupplierSpuId(findSupplierSeasons(priceQueryDto.getMarketSeason(),"")); 
					criteriaDto.createCriteria().andSupplierSpuIdIn(values);
				}
				return criteriaDto;
			}else{
				return null;
			}
		}
	}
	
	private List<Long> findSupplierSpuId(List<String> values){
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto(); 
		criteriaDto.setFields("supplier_spu_id");
		criteriaDto.setPageNo(1);
		criteriaDto.setPageSize(10000); 
		criteriaDto.createCriteria().andSupplierSeasonnameIn(values);
		List<HubSupplierSpuDto> lists = hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
		List<Long> supplierSpuIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(lists)){
			for(HubSupplierSpuDto spu : lists){
				supplierSpuIds.add(spu.getSupplierSpuId());
			}
		}
		return supplierSpuIds;
	}

	/**
	 * 根据supplierId和供应商spu编号查询商品
	 * @param supplierSpuDto
	 * @return
	 * @throws Exception
	 */
	public List<HubSupplierSpuDto> findHubSupplierSpuDtos(HubSupplierSpuDto supplierSpuDto) throws Exception{
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.setFields("supplier_spu_id,supplier_seasonname,supplier_categoryname,supplier_brandname");
		criteriaDto.createCriteria().andSupplierIdEqualTo(supplierSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(supplierSpuDto.getSupplierSpuNo());
		return hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
	}
	/**
	 * 根据主键查询
	 * @param supplierSpuId
	 * @return
	 * @throws Exception
	 */
	public HubSupplierSpuDto findHubSupplierSpuDtos(Long supplierSpuId) throws Exception{
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.setFields("supplier_spu_id,supplier_seasonname,supplier_categoryname,supplier_brandname");
		criteriaDto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		List<HubSupplierSpuDto> lists = hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
		return lists.get(0);
	}

	/**
	 * 查询季节是否发生变化
	 * @param hubSpu
	 * @return
	 * @throws Exception
	 */
	public HubSupplierSpuDto isSupplierSeasonNameChanged(HubSupplierSpuDto supplierSpuDto) throws Exception {
		List<HubSupplierSpuDto> hubSpus = findHubSupplierSpuDtos(supplierSpuDto);
		if(null == hubSpus || hubSpus.size() == 0){
			return null;
		}else{
			if(supplierSpuDto.getSupplierSeasonname().equals(hubSpus.get(0).getSupplierSeasonname())){
				return null;
			}else{
				return hubSpus.get(0);
			}
		}
	} 
	/**
	 * 查询价格是否变化
	 * @param hubSku
	 * @return
	 */
	public boolean isPriceChanged(HubSupplierSkuDto hubSku) throws Exception{
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setFields("supply_price,market_price,sp_sku_no");
		criteriaDto.createCriteria().andSupplierIdEqualTo(hubSku.getSupplierId()).andSupplierSkuNoEqualTo(hubSku.getSupplierSkuNo());
		List<HubSupplierSkuDto> hubSkus = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
		if(CollectionUtils.isNotEmpty(hubSkus)){
			HubSupplierSkuDto hubSkuSel = hubSkus.get(0);
			hubSku.setSpSkuNo(hubSkuSel.getSpSkuNo()); 
			BigDecimal supplierPrice = null;
			if(hubSku.getSupplyPrice()!=null){
				supplierPrice = hubSku.getSupplyPrice().setScale(2,BigDecimal.ROUND_HALF_UP); 
				if(!supplierPrice.equals(hubSkuSel.getSupplyPrice())){
					return true;
				}
			}
			BigDecimal marketPrice = null;
			if(hubSku.getMarketPrice()!=null){
				marketPrice =  hubSku.getMarketPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
				if(!marketPrice.equals(hubSkuSel.getMarketPrice())){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 根据supplierSpuId查找该spu下的所有sku
	 * @param supplierSpuId
	 * @return
	 * @throws Exception
	 */
	public List<HubSupplierSkuDto> findSupplierSkus(Long supplierSpuId) throws Exception{
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setPageNo(1);
		criteriaDto.setPageSize(1000); 
		criteriaDto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		return hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
	}
	/**
	 * 根据供应商门户编号和供应商季节名称查询季节字典表
	 * @param supplierId
	 * @param supplierSeason
	 * @return
	 */
	public HubSeasonDicDto findHubSeason(String supplierId,String supplierSeason){
		HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto(); 
		criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason);
		List<HubSeasonDicDto> lists = hubSeasonDicGateWay.selectByCriteria(criteriaDto);
		if(CollectionUtils.isNotEmpty(lists)){
			return lists.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 根据尚品季节或尚品上市时间查询供应商季节
	 * @param hubSeason
	 * @param hubMarketTime
	 * @return
	 */
	private List<String> findSupplierSeasons(String hubSeason,String hubMarketTime){
		HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto(); 
		criteriaDto.setPageNo(1);criteriaDto.setPageSize(1000); 
		criteriaDto.setFields("supplier_season");
		if(!StringUtils.isEmpty(hubSeason)){
			criteriaDto.createCriteria().andHubSeasonEqualTo(hubSeason);
		}
		if(!StringUtils.isEmpty(hubMarketTime)){
			criteriaDto.createCriteria().andHubMarketTimeEqualTo(hubMarketTime);
		}
		List<HubSeasonDicDto> lists = hubSeasonDicGateWay.selectByCriteria(criteriaDto);
		List<String> supplierSeasons = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(lists)){
			for(HubSeasonDicDto dic : lists){
				supplierSeasons.add(dic.getSupplierSeason());
			}
		}
		return supplierSeasons;
	}
}
