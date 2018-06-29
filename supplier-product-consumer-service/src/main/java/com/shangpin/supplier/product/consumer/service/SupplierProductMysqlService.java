package com.shangpin.supplier.product.consumer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.ErrorReason;
import com.shangpin.ephub.client.data.mysql.enumeration.Isexistpic;
import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingNohandleReasonGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.product.business.mail.dto.ShangpinMail;
import com.shangpin.ephub.client.product.business.mail.gateway.ShangpinMailSenderGateWay;
import com.shangpin.supplier.product.consumer.conf.mail.ShangpinMailProperties;
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
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSpuPendingNohandleReasonGateWay nohandleReasonGateWay;


	@Autowired
	private HubSeasonDicGateWay hubSeasonDicGateWay;
	@Autowired
	ShangpinMailSenderGateWay shangpinMailSenderGateWay;
	/**
	 * 判断hubSpu是否存在或主要信息发生变化
	 * @param supplierNo
	 * @param supplierSpu      供货商的原始SPU
	 * @param hubSpuSel  从数据库查出的hubsupplierspu
	 * @param pendingSpu 把hubSpu发生变化了的信息记录到这个对象中
	 * @return
	 */
	public ProductStatus isHubSpuChanged(String supplierNo,HubSupplierSpuDto supplierSpu,HubSupplierSpuDto hubSpuSel,PendingSpu pendingSpu){
		try {	
//			hubSpu.setMemo("");//先在memo中不要保存数据了，避免超长报错
//			HubSupplierSpuDto hubSpuSel = hasHadTheHubSpu(hubSpu);
			if(null == hubSpuSel){
				supplierSpu.setCreateTime(new Date());
				Long spuId = hubSupplierSpuGateWay.insert(supplierSpu);
				supplierSpu.setSupplierSpuId(spuId);
				convertHubSpuToPendingSpu(supplierSpu,pendingSpu);
				return ProductStatus.NEW;
			}else{
				supplierSpu.setSupplierSpuId(hubSpuSel.getSupplierSpuId());
				HubSupplierSpuDto hubSpuUpdated = new HubSupplierSpuDto();
				boolean isChanged = comparisonHubSpu(supplierNo,supplierSpu, hubSpuSel, pendingSpu,hubSpuUpdated);
				/**
				 * 处理错误原因
				 */
				setMsgmissHandleState(hubSpuSel.getSupplierSpuId(), pendingSpu);
				
				if(isChanged){
					hubSpuUpdated.setUpdateTime(new Date()); 
					updateHubSpu(hubSpuUpdated);
					return ProductStatus.UPDATE;
				}else{
					return ProductStatus.NO_NEED_HANDLE;
				}
			}
		} catch (Exception e) {
			log.error("系统在保存待处理spu时发生异常：异常为"+e.getMessage());
			try {
//				HubSupplierSpuDto hubSpuSel = hasHadTheHubSpu(hubSpu);
				if(null!=hubSpuSel)	supplierSpu.setSupplierSpuId(hubSpuSel.getSupplierSpuId());
			} catch (Exception e1) {
				log.error(">>>>>>>>>>>>>>>>>异常为"+e1.getMessage(),e1);
			}
			
			return ProductStatus.NO_NEED_HANDLE;
		}			
	}
	
	private void setMsgmissHandleState(Long supplierSpuId, PendingSpu pendingSpu){
		try {
			/**
			 * 状态没设置成NO_PIC说明有图片
			 */
			if(null == pendingSpu.getPicState()){
				HubSpuPendingNohandleReasonDto reasonDto = selectNohandleReason(supplierSpuId);
				if(null != reasonDto){
					log.info(supplierSpuId+ " 开始图片问题供应商已解决。");
					updateMsgmissHandleState(reasonDto);
				}
			}
		} catch (Exception e) {
			log.error("发生错误："+e.getMessage(),e); 
		}
	}
	
	private void updateMsgmissHandleState(HubSpuPendingNohandleReasonDto reasonDto){
		/**
		 * 先更新pending表
		 */
		HubSpuPendingDto dto = new HubSpuPendingDto();
		dto.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
		dto.setSpuPendingId(reasonDto.getSpuPendingId());
		int update = hubSpuPendingGateWay.updateByPrimaryKeySelective(dto );
		log.info(reasonDto.getSpuPendingId()+ " 更新msg_miss_handle_state结果=="+update);
		/**
		 * 再更新错误原因表
		 */
		HubSpuPendingNohandleReasonDto nohandleReasonDto = new HubSpuPendingNohandleReasonDto();
		nohandleReasonDto.setDataState(DataState.DELETED.getIndex());
		nohandleReasonDto.setSpuPendingNohandleReasonId(reasonDto.getSpuPendingNohandleReasonId()); 
		int update2 = nohandleReasonGateWay.updateByPrimaryKeySelective(nohandleReasonDto );
		log.info(nohandleReasonDto.getSpuPendingNohandleReasonId()+" 更新data_state结果=="+update2);
	}

	private HubSpuPendingNohandleReasonDto selectNohandleReason(Long supplierSpuId) {
		HubSpuPendingNohandleReasonCriteriaDto criteria = new HubSpuPendingNohandleReasonCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andErrorReasonEqualTo(ErrorReason.WRONG_MAPPING_OF_CODE.getIndex());
		List<HubSpuPendingNohandleReasonDto> list = nohandleReasonGateWay.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}
	
//	private void updateHubSpuMemo(HubSupplierSpuDto hubSpuUpdated) {
//		HubSupplierSpuWithCriteriaDto criteriaDto = new HubSupplierSpuWithCriteriaDto();
//		HubSupplierSpuCriteriaDto hubSupplierSpuCriteriaDto = new HubSupplierSpuCriteriaDto();
//		hubSupplierSpuCriteriaDto.createCriteria().andSupplierIdEqualTo(hubSpuUpdated.getSupplierId()).andSupplierSpuNoEqualTo(hubSpuUpdated.getSupplierSpuNo());
//		criteriaDto.setCriteria(hubSupplierSpuCriteriaDto);
//		criteriaDto.setHubSupplierSpu(hubSpuUpdated);
//		hubSupplierSpuGateWay.updateByCriteriaSelective(criteriaDto);
//	}

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
				Date nowTime = new Date();
				hubSku.setCreateTime(nowTime);
				hubSku.setLastPullTime(nowTime); 
				Long skuId = hubSupplierSkuGateWay.insert(hubSku);
				hubSku.setSupplierSkuId(skuId); 
				convertHubSkuToPendingSku(hubSku,pendingSku);
				return ProductStatus.NEW;
			}else{
				hubSku.setSupplierSkuId(hubSkuSel.getSupplierSkuId()); 
				HubSupplierSkuDto hubSkuUpdated = new HubSupplierSkuDto();
				boolean isChanged = comparisonHubSku(hubSku,hubSkuSel,pendingSku,hubSkuUpdated);
				Date nowTime = new Date();
				if(isChanged){
					hubSkuUpdated.setUpdateTime(nowTime); 
					hubSkuUpdated.setLastPullTime(nowTime); 
					updateHubSku(hubSkuUpdated);
					return ProductStatus.MODIFY_PRICE;
				}else{
					updateLastPullTime(hubSkuUpdated.getSupplierId(),hubSkuUpdated.getSupplierSkuNo(),nowTime);
					return ProductStatus.NO_NEED_HANDLE;
				}
			}
		} catch (Exception e) {
			throw new EpHubSupplierProductConsumerException("系统在保存待处理sku时发生异常：异常为"+e.getMessage());
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
		pendingSku.setSupplierSkuId(hubSku.getSupplierSkuId()); 
		
		hubSkuUpdated.setSupplierId(hubSku.getSupplierId());
		hubSkuUpdated.setSupplierSkuNo(hubSku.getSupplierSkuNo());
		hubSkuUpdated.setSupplierSkuId(hubSkuSel.getSupplierSkuId()); 
		BigDecimal supplierPrice = null;
		if(hubSku.getSupplyPrice()!=null){
			supplierPrice = hubSku.getSupplyPrice().setScale(2,BigDecimal.ROUND_HALF_UP); 
			if(!supplierPrice.equals(hubSkuSel.getSupplyPrice())){
				pendingSku.setSupplyPrice(supplierPrice);
				hubSkuUpdated.setSupplyPrice(supplierPrice);
				isChanged = true;
			}
		}
		BigDecimal salesPrice = null;
		if(hubSku.getSalesPrice()!=null){
			salesPrice = hubSku.getSalesPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
			if(!salesPrice.equals(hubSkuSel.getSalesPrice())){
				pendingSku.setSalesPrice(salesPrice);
				hubSkuUpdated.setSalesPrice(salesPrice);
				isChanged = true;
			}
			
		}
		BigDecimal marketPrice = null;
		if(hubSku.getMarketPrice()!=null){
			marketPrice =  hubSku.getMarketPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
			if(!marketPrice.equals(hubSkuSel.getMarketPrice())){
				pendingSku.setMarketPrice(marketPrice);
				hubSkuUpdated.setMarketPrice(marketPrice); 
				isChanged = true;
			}
		}
		if(hubSku.getStock()!=null){
			if(hubSku.getStock()!=hubSkuSel.getStock()){
				Log.info("供应商supplierSkuNo:"+hubSku.getSupplierSkuNo()+"库存发生变化："+hubSkuSel.getStock()+"=>"+hubSku.getStock());
				pendingSku.setStock(hubSku.getStock());
				hubSkuUpdated.setStock(hubSku.getStock()); 
				isChanged = true;
			}
		}
		if(hubSku.getSupplierSkuSize()!=null){
			if(!hubSku.getSupplierSkuSize().equals(hubSkuSel.getSupplierSkuSize())){
				hubSkuUpdated.setSupplierSkuSize(hubSku.getSupplierSkuSize()); 
				isChanged = true;
				//当sku尺码发生变化，并且老的sku已经生成尚品sku编号，发送邮件通知
				if(hubSkuSel.getSpSkuNo()!=null&&!hubSkuSel.getSpSkuNo().equals("")) {
					sendMail("尚品skuNo原始尺码发生变化","供应商id:"+hubSkuSel.getSupplierId()+"尚品skuNo:"+hubSkuSel.getSpSkuNo()+"原始尺码"+hubSkuSel.getSupplierSkuSize()+"更新为"+hubSku.getSupplierSkuSize());
				}
			}
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
		pendingSku.setSupplierSkuId(hubSku.getSupplierSkuId()); 
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
	public HubSupplierSpuDto hasHadTheHubSpu(HubSupplierSpuDto hubSpu) throws Exception {
		if(StringUtils.isEmpty(hubSpu.getSupplierId()) || StringUtils.isEmpty(hubSpu.getSupplierSpuNo())){
			throw new Exception("产品信息异常。"); 
		}
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
	 * 更新最后拉去时间
	 * @param supplierId
	 * @param supplierSkuNo
	 * @param lastPullTime
	 */
	private void updateLastPullTime(String supplierId,String supplierSkuNo,Date lastPullTime){
		HubSupplierSkuDto hubSkuUpdated = new HubSupplierSkuDto();
		hubSkuUpdated.setSupplierId(supplierId);
		hubSkuUpdated.setSupplierSkuNo(supplierSkuNo);
		hubSkuUpdated.setLastPullTime(lastPullTime); 
		HubSupplierSkuWithCriteriaDto criteriaDto = new HubSupplierSkuWithCriteriaDto();
		HubSupplierSkuCriteriaDto hubSupplierSkuCriteriaDto = new HubSupplierSkuCriteriaDto();
		hubSupplierSkuCriteriaDto.createCriteria().andSupplierIdEqualTo(hubSkuUpdated .getSupplierId()).andSupplierSkuNoEqualTo(hubSkuUpdated.getSupplierSkuNo());
		criteriaDto.setHubSupplierSku(hubSkuUpdated);
		criteriaDto.setCriteria(hubSupplierSkuCriteriaDto);
		hubSupplierSkuGateWay.updateByCriteriaSelective(criteriaDto);
	}
	
	/**
	 * 将hubSpu转换成pendingSpu
	 * @param hubSpu
	 * @param pendingSpu
	 */
	public void convertHubSpuToPendingSpu(HubSupplierSpuDto hubSpu, PendingSpu pendingSpu) throws Exception {
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
		if(!StringUtils.isEmpty(hubSpu.getSupplierSpuName()) && !hubSpu.getSupplierSpuName().equals(hubSpuSel.getSupplierSpuName())){
			pendingSpu.setSpuName(hubSpu.getSupplierSpuName());
			hubSpuUpdated.setSupplierSpuName(hubSpu.getSupplierSpuName());
			isChanged = true;
		}
		if(!StringUtils.isEmpty(hubSpu.getSupplierSpuDesc()) && !hubSpu.getSupplierSpuDesc().equals(hubSpuSel.getSupplierSpuDesc())){
			pendingSpu.setSpuDesc(hubSpu.getSupplierSpuDesc());
			hubSpuUpdated.setSupplierSpuDesc(hubSpu.getSupplierSpuDesc());
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
//		if(!StringUtils.isEmpty(hubSpu.getSupplierSeasonno()) && hubSpu.getSupplierSeasonno().equals(hubSpuSel.getSupplierSeasonno())){
//			pendingSpu.setHubSeason(hubSpu.getSupplierSeasonno());
//			hubSpuUpdated.setSupplierSeasonno(hubSpu.getSupplierSeasonno());
//			isChanged = true;
//		}
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
		if(null != hubSpuSel.getIsexistpic() && hubSpuSel.getIsexistpic() == Isexistpic.YES.getIndex()){
			pendingSpu.setPicState(PicState.HANDLED.getIndex());
		}
		
		return isChanged;
	}

	/**
	 * 根据supplierSpuId查找supplierSku
	 * @param supplierSpuId
	 * @return
	 */
	public List<HubSupplierSkuDto> findSupplierSku(Long supplierSpuId) {
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		criteria.setPageNo(1);
		criteria.setPageSize(10000);
		return hubSupplierSkuGateWay.selectByCriteria(criteria);
	}


	/**
	 * 根据supplierSpuId查找该spu下所有尚品sku编号不为空的sku
	 * @param supplierSpuId
	 * @return
	 * @throws Exception
	 */
	public List<HubSupplierSkuDto> findSupplierSkusSpSkuNoIsNotNull(Long supplierSpuId) throws Exception{
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setPageNo(1);
		criteriaDto.setPageSize(1000);
		criteriaDto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andSpSkuNoIsNotNull().andSpSkuNoNotEqualTo("");
		return hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
	}

	/**
	 * 根据supplierId和供应商spu编号查询商品
	 * @param supplierSpuDto
	 * @return
	 * @throws Exception
	 */
	private  List<HubSupplierSpuDto> findHubSupplierSpuDtos(HubSupplierSpuDto supplierSpuDto) throws Exception{
		if(StringUtils.isEmpty(supplierSpuDto.getSupplierId()) || StringUtils.isEmpty(supplierSpuDto.getSupplierSpuNo())){
			return null;
		}
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.setFields("supplier_spu_id,supplier_seasonname,supplier_categoryname,supplier_brandname");
		criteriaDto.createCriteria().andSupplierIdEqualTo(supplierSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(supplierSpuDto.getSupplierSpuNo());
		return hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
	}

	/**
	 *
	 * @param supplierId
	 * @param supplierSpuNo
	 * @return
	 */
	public HubSupplierSpuDto getSupplierSpuBySupplierIdAndSupplierSpuNo(String supplierId,String supplierSpuNo) {
		if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(supplierSpuNo)){
			return null;
		}
		HubSupplierSpuCriteriaDto criteriaDto = new HubSupplierSpuCriteriaDto();
		criteriaDto.setFields("supplier_spu_id,supplier_seasonname,supplier_categoryname,supplier_brandname");
		criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSupplierSpuDto> hubSupplierSpuDtos = hubSupplierSpuGateWay.selectByCriteria(criteriaDto);
		if(null == hubSupplierSpuDtos || hubSupplierSpuDtos.size() == 0){
			return null;
		}else{
			return hubSupplierSpuDtos.get(0);

		}
	}


	/**
	 * 判断供价是否变化
	 * @param skuVO
	 * @param skuDtoMap
	 * @return
	 */
	public boolean isSupplyPriceChanged(HubSupplierSkuDto skuVO, Map<String,HubSupplierSkuDto> skuDtoMap) throws Exception{

		if(skuDtoMap.containsKey(skuVO.getSupplierSkuNo())){
			HubSupplierSkuDto hubSkuSel = skuDtoMap.get(skuVO.getSupplierSkuNo());
			skuVO.setSpSkuNo(null != hubSkuSel.getSpSkuNo() ? hubSkuSel.getSpSkuNo() : "");
			BigDecimal supplierPrice = null;
			if(null!=skuVO.getSupplyPrice()){
				supplierPrice = skuVO.getSupplyPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
				if(!supplierPrice.equals(hubSkuSel.getSupplyPrice())){
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * 根据供应商门户编号和供应商sku编号查询
	 * @param supplierId
	 * @param supplierSkuNo
	 * @return
	 */
	private List<HubSupplierSkuDto> selectHubSupplierSku(String supplierId,String supplierSkuNo) {
		HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
		criteriaDto.setFields("supply_price,market_price,sp_sku_no");
		criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo).andSpSkuNoIsNotNull().andSpSkuNoNotEqualTo("");
		List<HubSupplierSkuDto> hubSkus = hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
		return hubSkus;
	}


	/**
	 * 判断市场价是否变化
	 * @param skuVO
	 * @param  skuDtoMap
	 * @return
	 * @throws Exception
	 */
	public boolean isMarketPriceChanged(HubSupplierSkuDto skuVO, Map<String,HubSupplierSkuDto> skuDtoMap) throws Exception{

		if(skuDtoMap.containsKey(skuVO.getSupplierSkuNo())){
			HubSupplierSkuDto hubSkuSel = skuDtoMap.get(skuVO.getSupplierSkuNo());
			skuVO.setSpSkuNo(null != hubSkuSel.getSpSkuNo() ? hubSkuSel.getSpSkuNo() : "");
			BigDecimal marketPrice = null;
			if(skuVO.getMarketPrice()!=null){
				marketPrice =  skuVO.getMarketPrice().setScale(2,BigDecimal.ROUND_HALF_UP);
				if(!marketPrice.equals(hubSkuSel.getMarketPrice())){
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 根据供应商门户编号和供应商季节名称查询季节字典表
	 * @param supplierId
	 * @param supplierSeason
	 * @return
	 */
	public HubSeasonDicDto findHubSeason(String supplierId, String supplierSeason){
		HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto();
		criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason);
		List<HubSeasonDicDto> lists = hubSeasonDicGateWay.selectByCriteria(criteriaDto);
		if(CollectionUtils.isNotEmpty(lists)){
			return lists.get(0);
		}else{
			return null;
		}
	}

	public void sendMail(String subject,String text){
		try {
			ShangpinMail shangpinMail = new ShangpinMail();
			shangpinMail.setFrom("chengxu@shangpin.com");
			shangpinMail.setSubject(subject);
			shangpinMail.setText(text);
			shangpinMail.setTo("andraw.chen@shangpin.com");
			List<String> addTo = new ArrayList<>();
			addTo.add("lizhongren@shangpin.com");
			addTo.add("Terry.Zhao@shangpin.com");
			shangpinMail.setAddTo(addTo );
			shangpinMailSenderGateWay.send(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e); 
		}
	}



}
