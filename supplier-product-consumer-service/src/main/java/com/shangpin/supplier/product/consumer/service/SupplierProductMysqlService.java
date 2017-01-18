package com.shangpin.supplier.product.consumer.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SupplierProductMysqlService </p>
 * <p>Description: Supplier表的增删改查Service</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 下午7:24:08
 *
 */
@Service
@Slf4j
public class SupplierProductMysqlService {

	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired 
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	/**
	 * 判断hubSpu是否存在或主要信息发生变化
	 * @param supplierNo
	 * @param hubSpu
	 * @param pendingSpu 把hubSpu发生变化了的信息记录到这个对象中
	 * @return
	 */
	public ProductStatus isHubSpuChanged(String supplierNo,HubSupplierSpuDto hubSpu,PendingSpu pendingSpu) throws EpHubSupplierProductConsumerException{
		try {	
			log.info("要保存的数据======="+JsonUtil.serialize(hubSpu));
			HubSupplierSpuDto hubSpuSel = hasHadTheHubSpu(hubSpu);
			if(null == hubSpuSel){
				hubSpu.setCreateTime(new Date());
				Long spuId = hubSupplierSpuGateWay.insert(hubSpu);
				hubSpu.setSupplierSpuId(spuId); 
				convertHubSpuToPendingSpu(hubSpu,pendingSpu);
				return ProductStatus.NEW;
			}else{
				hubSpu.setSupplierSpuId(hubSpuSel.getSupplierSpuId()); 
				HubSupplierSpuDto hubSpuUpdated = new HubSupplierSpuDto();
				boolean isChanged = comparisonHubSpu(supplierNo,hubSpu, hubSpuSel, pendingSpu,hubSpuUpdated);
				if(isChanged){
					hubSpuUpdated.setUpdateTime(new Date()); 
					updateHubSpu(hubSpuUpdated);
					return ProductStatus.UPDATE;
				}else{
					return ProductStatus.NO_NEED_HANDLE;
				}
			}
		} catch (Exception e) {
			/*System.out.println("supplierid============="+hubSpu.getSupplierId());
			System.out.println(hubSpu.getSupplierMaterial());*/
			throw new EpHubSupplierProductConsumerException("系统在保存待处理spu时发生异常：异常为"+e.getMessage(), e);
		}			
	}	
	
	/**
	 * 判断hubSku价格是否发生变化
	 * @param hubSku
	 * @param pendingSku 把hubSku发生变化的价格信息记录到这个对象中
	 * @return
	 */
	public ProductStatus isHubSkuChanged(HubSupplierSkuDto hubSku,PendingSku pendingSku) throws EpHubSupplierProductConsumerException{
		try {
			HubSupplierSkuDto hubSkuSel = hasHadTheHubSku(hubSku);
			if(null == hubSkuSel){
				hubSku.setCreateTime(new Date());
				Long skuId = hubSupplierSkuGateWay.insert(hubSku);
				hubSku.setSupplierSkuId(skuId); 
				convertHubSkuToPendingSku(hubSku,pendingSku);
				return ProductStatus.NEW;
			}else{
				hubSku.setSupplierSkuId(hubSkuSel.getSupplierSkuId()); 
				HubSupplierSkuDto hubSkuUpdated = new HubSupplierSkuDto();
				log.info("");
				boolean isChanged = comparisonHubSku(hubSku,hubSkuSel,pendingSku,hubSkuUpdated);
				if(isChanged){
					hubSkuUpdated.setUpdateTime(new Date()); 
					updateHubSku(hubSkuUpdated);
					return ProductStatus.MODIFY_PRICE;
				}else{
					return ProductStatus.NO_NEED_HANDLE;
				}
			}
		} catch (Exception e) {
			throw new EpHubSupplierProductConsumerException("系统在保存待处理sku时发生异常：异常为"+e.getMessage(), e);
		}
		
	}	

	/**
	 * 对比两个HubSupplierSkuDto对象的价格
	 * @param hubSku 新抓取的对象
	 * @param hubSkuSel 从数据库中查出来的对象（作比较用）
	 * @param pendingSku 待更新的对象，推送消息给Pending用
	 * @param hubSkuUpdated 待更新的对象，用来更新本地库
	 * @return
	 */
	private boolean comparisonHubSku(HubSupplierSkuDto hubSku, HubSupplierSkuDto hubSkuSel, PendingSku pendingSku,HubSupplierSkuDto hubSkuUpdated) throws Exception {
		boolean isChanged = false;
		pendingSku.setSupplierId(hubSku.getSupplierId());
		pendingSku.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		
		hubSkuUpdated.setSupplierId(hubSku.getSupplierId());
		hubSkuUpdated.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		hubSkuUpdated.setSupplierSkuId(hubSkuSel.getSupplierSkuId()); 
		
		if(!StringUtils.isEmpty(hubSku.getSupplyPrice()) && !hubSku.getSupplyPrice().equals(hubSkuSel.getSupplyPrice())){
			pendingSku.setSupplyPrice(hubSku.getSupplyPrice());
			hubSkuUpdated.setSupplyPrice(hubSku.getSupplyPrice());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSku.getSalesPrice()) && !hubSku.getSalesPrice().equals(hubSkuSel.getSalesPrice())){
			pendingSku.setSalesPrice(hubSku.getSalesPrice());
			hubSkuUpdated.setSalesPrice(hubSku.getSalesPrice());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSku.getMarketPrice()) && !hubSku.getMarketPrice().equals(hubSkuSel.getMarketPrice())){
			pendingSku.setMarketPrice(hubSku.getMarketPrice());
			hubSkuUpdated.setMarketPrice(hubSku.getMarketPrice()); 
			isChanged = true;
		}
		return isChanged;
	}
	/**
	 * 将hubSku转换成pendingSku
	 * @param hubSku
	 * @param pendingSku
	 */
	private void convertHubSkuToPendingSku(HubSupplierSkuDto hubSku, PendingSku pendingSku) throws Exception {
		pendingSku.setSupplierId(hubSku.getSupplierId());
		pendingSku.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		pendingSku.setHubSkuSize(hubSku.getSupplierSkuSize());
		pendingSku.setMarketPrice(hubSku.getMarketPrice());
		pendingSku.setMarketPriceCurrencyorg(hubSku.getMarketPriceCurrencyorg());
		pendingSku.setSalesPrice(hubSku.getSalesPrice());
		pendingSku.setSalesPriceCurrency(hubSku.getSalesPriceCurrency());
		pendingSku.setSkuName(hubSku.getSupplierSkuName());
		pendingSku.setStock(hubSku.getStock());
		pendingSku.setSupplierBarcode(hubSku.getSupplierBarcode());
		pendingSku.setSupplyPrice(hubSku.getSupplyPrice());
		pendingSku.setSupplyPriceCurrency(hubSku.getSupplyPriceCurrency());
	}
	/**
	 * 查找hubSku是否存在，如果存在则返回查询结果
	 * @param hubSku
	 * @return
	 */
	private HubSupplierSkuDto hasHadTheHubSku(HubSupplierSkuDto hubSku) throws Exception {
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.createCriteria().andSupplierIdEqualTo(hubSku.getSupplierId()).andSupplierSkuNoEqualTo(hubSku.getSupplierSkuNo());
		List<HubSupplierSkuDto> hubSkus = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
		if(null == hubSkus || hubSkus.size() == 0){
			return null;
		}else{
			return hubSkus.get(0);
		}
	}
	/**
	 * 查找hubSpu是否存在，如果存在则返回查询结果
	 * @param hubSpu
	 * @return
	 */
	private HubSupplierSpuDto hasHadTheHubSpu(HubSupplierSpuDto hubSpu) throws Exception {
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.createCriteria().andSupplierIdEqualTo(hubSpu.getSupplierId()).andSupplierSpuNoEqualTo(hubSpu.getSupplierSpuNo());
		List<HubSupplierSpuDto> hubSpus = hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
		if(null == hubSpus || hubSpus.size() == 0){
			return null;
		}else{
			return hubSpus.get(0);
		}
	}
	/**
	 * 更新
	 * @param hubSpuUpdated
	 */
	private void updateHubSpu(HubSupplierSpuDto hubSpuUpdated)  throws Exception{
		HubSupplierSpuWithCriteriaDto criteriaDto = new HubSupplierSpuWithCriteriaDto();
		HubSupplierSpuCriteriaDto hubSupplierSpuCriteriaDto = new HubSupplierSpuCriteriaDto();
		hubSupplierSpuCriteriaDto.createCriteria().andSupplierIdEqualTo(hubSpuUpdated.getSupplierId()).andSupplierSpuNoEqualTo(hubSpuUpdated.getSupplierSpuNo());
		criteriaDto.setCriteria(hubSupplierSpuCriteriaDto);
		criteriaDto.setHubSupplierSpu(hubSpuUpdated);
		hubSupplierSpuGateWay.updateByCriteriaSelective(criteriaDto);
	}
	/**
	 * 更新
	 * @param hubSkuUpdated
	 */
	private void updateHubSku(HubSupplierSkuDto hubSkuUpdated) throws Exception {
		HubSupplierSkuWithCriteriaDto criteriaDto = new HubSupplierSkuWithCriteriaDto();
		HubSupplierSkuCriteriaDto hubSupplierSkuCriteriaDto = new HubSupplierSkuCriteriaDto();
		hubSupplierSkuCriteriaDto.createCriteria().andSupplierIdEqualTo(hubSkuUpdated.getSupplierId()).andSupplierSkuNoEqualTo(hubSkuUpdated.getSupplierSkuNo());
		criteriaDto.setHubSupplierSku(hubSkuUpdated);
		criteriaDto.setCriteria(hubSupplierSkuCriteriaDto);
		hubSupplierSkuGateWay.updateByCriteriaSelective(criteriaDto);		
	}
	/**
	 * 将hubSpu转换成pendingSpu
	 * @param hubSpu
	 * @param pendingSpu
	 */
	private void convertHubSpuToPendingSpu(HubSupplierSpuDto hubSpu, PendingSpu pendingSpu) throws Exception {
		pendingSpu.setSupplierId(hubSpu.getSupplierId());
		pendingSpu.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		pendingSpu.setSupplierSpuId(hubSpu.getSupplierSpuId());
		pendingSpu.setHubBrandNo(hubSpu.getSupplierBrandname());
		pendingSpu.setHubCategoryNo(hubSpu.getSupplierCategoryname());
		pendingSpu.setHubColor(hubSpu.getSupplierSpuColor());
		pendingSpu.setHubGender(hubSpu.getSupplierGender());
		pendingSpu.setHubMaterial(hubSpu.getSupplierMaterial());
		pendingSpu.setHubOrigin(hubSpu.getSupplierOrigin());
		pendingSpu.setHubSeason(hubSpu.getSupplierSeasonname());
		pendingSpu.setSpuDesc(hubSpu.getSupplierSpuDesc());
		pendingSpu.setSpuModel(hubSpu.getSupplierSpuModel());
		pendingSpu.setSpuName(hubSpu.getSupplierSpuName());
		
	}
	
	/**
	 * 比对两个HubSupplierSpuDto对象
	 * @param hubSpu 新抓取的对象
	 * @param hubSpuSel 数据库中查找出来的对象
	 * @param pendingSpu 带更新的对象，发送消息用
	 * @param hubSpuUpdated 待更新的对象 更新本地库用
	 * @return 发生变化则返回true,否则false
	 */
	private boolean comparisonHubSpu(String supplierNo,HubSupplierSpuDto hubSpu,HubSupplierSpuDto hubSpuSel,PendingSpu pendingSpu,HubSupplierSpuDto hubSpuUpdated) throws Exception{
		boolean isChanged = false;	
		pendingSpu.setSupplierId(hubSpu.getSupplierId());
		pendingSpu.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		pendingSpu.setSupplierSpuId(hubSpuSel.getSupplierSpuId());
		pendingSpu.setSupplierNo(supplierNo); 
		
		hubSpuUpdated.setSupplierId(hubSpu.getSupplierId());
		hubSpuUpdated.setSupplierSpuId(hubSpu.getSupplierSpuId());
		hubSpuUpdated.setSupplierSpuNo(hubSpuSel.getSupplierSpuNo());
		
		if(!StringUtils.isEmpty(hubSpu.getSupplierSpuModel()) && !hubSpu.getSupplierSpuModel().equals(hubSpuSel.getSupplierSpuModel())){
			pendingSpu.setSpuModel(hubSpu.getSupplierSpuModel());
			hubSpuUpdated.setSupplierSpuModel(hubSpu.getSupplierSpuModel());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierSpuColor()) && !hubSpu.getSupplierSpuColor().equals(hubSpuSel.getSupplierSpuColor())){
			pendingSpu.setHubColor(hubSpu.getSupplierSpuColor());
			hubSpuUpdated.setSupplierSpuColor(hubSpu.getSupplierSpuColor());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierGender()) && !hubSpu.getSupplierGender().equals(hubSpuSel.getSupplierGender())){
			pendingSpu.setHubGender(hubSpu.getSupplierGender());
			hubSpuUpdated.setSupplierGender(hubSpu.getSupplierGender());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierCategoryname()) && !hubSpu.getSupplierCategoryname().equals(hubSpuSel.getSupplierCategoryname())){
			pendingSpu.setHubCategoryNo(hubSpu.getSupplierCategoryname());
			hubSpuUpdated.setSupplierCategoryname(hubSpu.getSupplierCategoryname());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierBrandname()) && !hubSpu.getSupplierBrandname().equals(hubSpuSel.getSupplierBrandname())){
			pendingSpu.setHubBrandNo(hubSpu.getSupplierBrandname());
			hubSpuUpdated.setSupplierBrandname(hubSpu.getSupplierBrandname()); 
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierSeasonno()) && hubSpu.getSupplierSeasonno().equals(hubSpuSel.getSupplierSeasonno())){
			pendingSpu.setHubSeason(hubSpu.getSupplierSeasonno());
			hubSpuUpdated.setSupplierSeasonno(hubSpu.getSupplierSeasonno());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierSeasonname()) && !hubSpu.getSupplierSeasonname().equals(hubSpuSel.getSupplierSeasonname())){
			pendingSpu.setHubSeason(hubSpu.getSupplierSeasonname());
			hubSpuUpdated.setSupplierSeasonname(hubSpu.getSupplierSeasonname());
			isChanged = true;
		}		
		if(!StringUtils.isEmpty(hubSpu.getSupplierMaterial()) && !hubSpu.getSupplierMaterial().equals(hubSpuSel.getSupplierMaterial())){
			pendingSpu.setHubMaterial(hubSpu.getSupplierMaterial());
			hubSpuUpdated.setSupplierMaterial(hubSpu.getSupplierMaterial());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierOrigin()) && !hubSpu.getSupplierOrigin().equals(hubSpuSel.getSupplierOrigin())){
			pendingSpu.setHubOrigin(hubSpu.getSupplierOrigin());
			hubSpuUpdated.setSupplierOrigin(hubSpu.getSupplierOrigin()); 
			isChanged = true;
		}
		return isChanged;
	}
	
}
