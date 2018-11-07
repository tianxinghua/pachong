package com.shangpin.supplier.product.consumer.service.stock;


import java.util.Date;
import java.util.List;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.original.body.SupplierStock;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: SupplierStockService</p>
 * <p>Description: 更新ephub库存的服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月24日 下午3:44:15
 *
 */
@Service
@Slf4j
public class SupplierStockService {
	
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	private HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;

	public void updateStock(SupplierStock supplierStock){
		try {
			if(null == supplierStock || StringUtils.isEmpty(supplierStock.getSupplierId()) || StringUtils.isEmpty(supplierStock.getSupplierSkuNo())){
				log.info("消息格式不正确！"); 
				return;
			}else{
				int result1 = updateSupplierStock(supplierStock);
				int result2 = updatePendingStock(supplierStock);
				if(result2>0){
					updatePendingSpuStockState(supplierStock);
				}
				if(result1<0 ){
					log.info("SUPPLIERID："+supplierStock.getSupplierId()+" SUPPLIERSKUNO："+supplierStock.getSupplierSkuNo()+" STOCK："+supplierStock.getStock()+"更新库存失败");
				}
			}
		} catch (Exception e) {
			log.error("更新库存异常，supplierId："+supplierStock.getSupplierId()+"supplierSkuNo："+supplierStock.getSupplierSkuNo()+" EXCEPTION："+e.getMessage()); 
		}
	}
	
	/**
	 * 更新原始表的库存
	 * @param supplierStock
	 * @return
	 * @throws Exception
	 */
	private int updateSupplierStock(SupplierStock supplierStock) throws Exception{
		HubSupplierSkuWithCriteriaDto withCriteria = new HubSupplierSkuWithCriteriaDto();
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierStock.getSupplierId()).andSupplierSkuNoEqualTo(supplierStock.getSupplierSkuNo());
		withCriteria.setCriteria(criteria );
		HubSupplierSkuDto hubSupplierSku = new HubSupplierSkuDto();
		hubSupplierSku.setStock(supplierStock.getStock());
//		hubSupplierSku.setUpdateTime(new Date());  // BI统计可能用的此致 不做修改
//		hubSupplierSku.setMemo("OPENAPI更新库存"); // BI统计可能用的此致 不做修改
		withCriteria.setHubSupplierSku(hubSupplierSku );
		return hubSupplierSkuGateWay.updateByCriteriaSelective(withCriteria );
	}
	/**
	 * 更新pending库的库存
	 * @param supplierStock
	 * @return
	 */
	private int updatePendingStock(SupplierStock supplierStock){
		HubSkuPendingWithCriteriaDto withCriteria = new HubSkuPendingWithCriteriaDto();
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierStock.getSupplierId()).andSupplierSkuNoEqualTo(supplierStock.getSupplierSkuNo());
		withCriteria.setCriteria(criteria );
		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		hubSkuPending.setStock(supplierStock.getStock());
		withCriteria.setHubSkuPending(hubSkuPending);
		return hubSkuPendingGateWay.updateByCriteriaSelective(withCriteria );
	}
	/**
	 * 更新pendingSpu库存状态
	 * @param supplierStock
	 * @return
	 */
	private int updatePendingSpuStockState(SupplierStock supplierStock){
		int result = 0;
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierStock.getSupplierId()).andSupplierSkuNoEqualTo(supplierStock.getSupplierSkuNo());
		List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)) {
			HubSpuPendingDto dto = new HubSpuPendingDto();
			dto.setSpuPendingId(list.get(0).getSpuPendingId());
			if(supplierStock.getStock() > 0){
				dto.setStockState(StockState.HANDLED.getIndex());
			}else if(0==supplierStock.getStock()){
				int total = hubSkuPendingGateWay.sumStockBySpuPendingId(list.get(0).getSpuPendingId());
				if(total>0){
					dto.setStockState(StockState.HANDLED.getIndex());
				}else{
					dto.setStockState(StockState.NOSTOCK.getIndex());
				}
			}else{
				dto.setStockState(StockState.NOSTOCK.getIndex());
			}
			result = hubSpuPendingGateWay.updateByPrimaryKeySelective(dto );
	    }
		return result;
	}
}
