package com.shangpin.supplier.product.consumer.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
/**
 * <p>Title:SupplierProductMysqlService </p>
 * <p>Description: Supplier表的增删改查Service</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 下午7:24:08
 *
 */
@Component
public class SupplierProductMysqlService {

	/**
	 * 判断hubSpu是否存在或主要信息发生变化
	 * @param hubSpu
	 * @param pendingSpu 把hubSpu发生变化了的信息记录到这个对象中
	 * @return
	 */
	public ProductStatus isHubSpuChanged(HubSupplierSpu hubSpu,PendingSpu pendingSpu){
					
		HubSupplierSpu hubSpuSel = hasHadTheHubSpu(hubSpu);
		if(null == hubSpuSel){
			//TODO insert(hubSpu)
			convertHubSpuToPendingSpu(hubSpu,pendingSpu);
			return ProductStatus.NEW;
		}else{
			HubSupplierSpu hubSpuUpdated = new HubSupplierSpu();
			boolean isChanged = comparisonHubSpu(hubSpu, hubSpuSel, pendingSpu,hubSpuUpdated);
			if(isChanged){
				updateHubSpu(hubSpuUpdated);
				return ProductStatus.UPDATE;
			}else{
				return ProductStatus.NO_NEED_HANDLE;
			}
		}
			
	}	
	
	/**
	 * 判断hubSku价格是否发生变化
	 * @param hubSku
	 * @param pendingSku 把hubSku发生变化的价格信息记录到这个对象中
	 * @return
	 */
	public ProductStatus isHubSkuChanged(HubSupplierSku hubSku,PendingSku pendingSku){
		HubSupplierSku hubSkuSel = hasHadTheHubSku(hubSku);
		if(null == hubSkuSel){
			//TODO insert(hubSku)
			convertHubSkuToPendingSku(hubSku,pendingSku);
			return ProductStatus.NEW;
		}else{
			HubSupplierSku hubSkuUpdated = new HubSupplierSku();
			boolean isChanged = comparisonHubSku(hubSku,hubSkuSel,pendingSku,hubSkuUpdated);
			if(isChanged){
				updateHubSku(hubSkuUpdated);
				return ProductStatus.MODIFY_PRICE;
			}else{
				return ProductStatus.NO_NEED_HANDLE;
			}
		}
	}	

	/**
	 * 对比两个HubSupplierSku对象的价格
	 * @param hubSku 新抓取的对象
	 * @param hubSkuSel 从数据库中查出来的对象（作比较用）
	 * @param pendingSku 待更新的对象，推送消息给Pending用
	 * @param hubSkuUpdated 待更新的对象，用来更新本地库
	 * @return
	 */
	private boolean comparisonHubSku(HubSupplierSku hubSku, HubSupplierSku hubSkuSel, PendingSku pendingSku,HubSupplierSku hubSkuUpdated) {
		boolean isChanged = false;
		pendingSku.setSupplierId(hubSku.getSupplierId());
		pendingSku.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		
		hubSkuUpdated.setSupplierId(hubSku.getSupplierId());
		hubSkuUpdated.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		hubSkuUpdated.setSupplierSkuId(hubSku.getSupplierSkuId()); 
		
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
	private void convertHubSkuToPendingSku(HubSupplierSku hubSku, PendingSku pendingSku) {
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
	private HubSupplierSku hasHadTheHubSku(HubSupplierSku hubSku) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 查找hubSpu是否存在，如果存在则返回查询结果
	 * @param hubSpu
	 * @return
	 */
	private HubSupplierSpu hasHadTheHubSpu(HubSupplierSpu hubSpu) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 更新
	 * @param hubSpuUpdated
	 */
	private void updateHubSpu(HubSupplierSpu hubSpuUpdated) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 更新
	 * @param hubSkuUpdated
	 */
	private void updateHubSku(HubSupplierSku hubSkuUpdated) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 将hubSpu转换成pendingSpu
	 * @param hubSpu
	 * @param pendingSpu
	 */
	private void convertHubSpuToPendingSpu(HubSupplierSpu hubSpu, PendingSpu pendingSpu) {
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
	 * 比对两个HubSupplierSpu对象
	 * @param hubSpu 新抓取的对象
	 * @param hubSpuSel 数据库中查找出来的对象
	 * @param pendingSpu 带更新的对象，发送消息用
	 * @param hubSpuUpdated 待更新的对象 更新本地库用
	 * @return 发生变化则返回true,否则false
	 */
	private boolean comparisonHubSpu(HubSupplierSpu hubSpu,HubSupplierSpu hubSpuSel,PendingSpu pendingSpu,HubSupplierSpu hubSpuUpdated){
		boolean isChanged = false;	
		pendingSpu.setSupplierId(hubSpu.getSupplierId());
		pendingSpu.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		pendingSpu.setSupplierSpuId(hubSpu.getSupplierSpuId());
		
		hubSpuUpdated.setSupplierId(hubSpu.getSupplierId());
		hubSpuUpdated.setSupplierSpuId(hubSpu.getSupplierSpuId());
		hubSpuUpdated.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		
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
