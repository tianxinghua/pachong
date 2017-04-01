package com.shangpin.ephub.product.business.rest.price.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
