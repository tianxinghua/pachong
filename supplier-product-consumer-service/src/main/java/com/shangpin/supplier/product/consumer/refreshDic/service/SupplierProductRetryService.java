package com.shangpin.supplier.product.consumer.refreshDic.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.manager.SupplierProductRetryManager;
import com.shangpin.supplier.product.consumer.service.SupplierProductMysqlService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSendToPending;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
import com.shangpin.supplier.product.consumer.service.dto.Spu;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:SupplierProductRetryService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月2日 下午12:45:06
 */
@Service
@Slf4j
public class SupplierProductRetryService {

	@Autowired
	private SupplierProductRetryManager supplierProductPictureManager;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	SupplierProductMysqlService supplierProductMysqlService;
	@Autowired
	HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	private static final Integer PAGESIZE = 100;
	
	/**
	 * 处理供应商商品
	 * @param picDtos
	 */
	public void processProduct(Byte state) throws Exception{
		long start = System.currentTimeMillis();
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.createCriteria().andInfoStateEqualTo(state);
		criteria.setPageNo(1);
		criteria.setPageSize(PAGESIZE);
		List<HubSupplierSpuDto> products = supplierProductPictureManager.findSupplierProduct(criteria);
		if(products!=null&&products.size()>0){
			log.info("========系统扫描到infoState："+state+"需要重新推送的数据:"+products.size()+"===");
			for(HubSupplierSpuDto spu : products){
				if(state==5){
					loopProduct(spu, state, true);
				}else{
					loopProduct(spu,state,null);	
				}
				updateSupplierInfoState(spu);
			}
			log.info("=====系统扫描到需要重新推送的数据结束,耗时{}毫秒======",System.currentTimeMillis()-start);
		}
	}
	
	public void sendSupplierSpu(int total,HubSupplierSpuCriteriaDto criteria,byte state) throws Exception{
		int pageCount = getPageCount(total, PAGESIZE);// 页数
		log.info("刷新总页数：" + pageCount);
		for (int i = 1; i <= pageCount; i++) {
			long start = System.currentTimeMillis();
			criteria.setPageNo(i);
			criteria.setPageSize(PAGESIZE);
			List<HubSupplierSpuDto> products = hubSupplierSpuGateWay.selectByCriteria(criteria);
			log.info("========系统扫描到需要重新推送的数据:" + products.size() + "===");
			for (HubSupplierSpuDto spu : products) {
				loopProduct(spu, state, null);
			}
			log.info("=====系统扫描到需要重新推送的数据结束,耗时{}毫秒======", System.currentTimeMillis() - start);
		}
	}
	
	/**
	 * 获取总页数
	 * 
	 * @param totalSize
	 *            总计路数
	 * @param pagesize
	 *            每页记录数
	 * @return
	 */
	public Integer getPageCount(Integer totalSize, Integer pageSize) {
		if (totalSize % pageSize == 0) {
			return totalSize / pageSize;
		} else {
			return (totalSize / pageSize) + 1;
		}
	}
	public void loopProduct(HubSupplierSpuDto spu, byte state, HubSupplierSkuDto hubSupplierSku) throws Exception {

		HubSeasonDicDto season = supplierProductPictureManager.findCurrentSeason(spu.getSupplierId(),spu.getSupplierSeasonname());
		if (season == null) {
			log.info("====" + spu.getSupplierId() + ":" + spu.getSupplierSpuId() + ":" + spu.getSupplierSeasonname()
					+ "非当季");
			return;
		}

		HubSupplierValueMappingDto supplier = supplierProductPictureManager
				.findHubSupplierValueMapping(spu.getSupplierId());
		if (supplier == null) {
			log.info("====" + spu.getSupplierId() + "未找到供应商名称");
			return;
		}

		Spu spuHead = new Spu();
		spuHead.setSupplierId(spu.getSupplierId());
		spuHead.setSpuNo(spu.getSupplierSpuNo());
		spuHead.setStatus(state);

		PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierNo(supplier.getHubValNo());
		pendingProduct.setSupplierId(spu.getSupplierId());
		pendingProduct.setSupplierName(supplier.getHubVal());

		PendingSpu pendingSpu = new PendingSpu();
		supplierProductMysqlService.convertHubSpuToPendingSpu(spu, pendingSpu);
		pendingSpu.setSupplierNo(supplier.getHubValNo());
		pendingProduct.setData(pendingSpu);

		if (hubSupplierSku!=null) {
			// 开始构造sku消息头
			List<PendingSku> skus = new ArrayList<PendingSku>();
			List<Sku> headSkus = new ArrayList<Sku>();
			try {
				Sku headSku = new Sku();
				PendingSku pendingSku = new PendingSku();
				// 开始保存hubSku到数据库
				convertHubSkuToPendingSku(hubSupplierSku, pendingSku);
				skus.add(pendingSku);
				headSku.setSupplierId(spu.getSupplierId());
				headSku.setSkuNo(hubSupplierSku.getSupplierSkuNo());
				headSku.setStatus(state);
				headSkus.add(headSku);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			pendingSpu.setSkus(skus);
			spuHead.setSkus(headSkus);
		}

		pendingProduct.setData(pendingSpu);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
		supplierProductSendToPending.dispatchSupplierProduct(pendingProduct, headers);

	}
	public boolean updateHubSpuImportByTaskNo(int status, String taskNo, String processInfo, String resultFilePath) {
		HubSpuImportTaskWithCriteriaDto dto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskDto hubSpuImportTaskDto = new HubSpuImportTaskDto();

		hubSpuImportTaskDto.setUpdateTime(new Date());
		if (processInfo != null) {
			hubSpuImportTaskDto.setProcessInfo(processInfo);
		}
		if (resultFilePath != null) {
			hubSpuImportTaskDto.setResultFilePath(resultFilePath);
		}
		hubSpuImportTaskDto.setTaskState((byte) status);
		dto.setHubSpuImportTask(hubSpuImportTaskDto);
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		hubSpuImportTaskCriteriaDto.createCriteria().andTaskNoEqualTo(taskNo);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		spuImportGateway.updateByCriteriaSelective(dto);
		return true;
	}

	private void updateSupplierInfoState(HubSupplierSpuDto spu) {
		HubSupplierSpuDto hubSupplierSpu = new HubSupplierSpuDto();
    	hubSupplierSpu.setSupplierSpuId(spu.getSupplierSpuId());
    	hubSupplierSpu.setInfoState((byte)0);
    	hubSupplierSpu.setUpdateTime(new Date());
    	supplierProductPictureManager.updateSupplierSpu(hubSupplierSpu);		
	}

	private void loopProduct(HubSupplierSpuDto spu,byte state,boolean flag) throws Exception{
		
		HubSeasonDicDto season = supplierProductPictureManager.findCurrentSeason(spu.getSupplierId(),spu.getSupplierSeasonname());
		if(season==null){
			log.info("===="+spu.getSupplierId()+":"+spu.getSupplierSpuId()+":"+spu.getSupplierSeasonname()+"非当季");
			return;
		}
	
		HubSupplierValueMappingDto supplier = supplierProductPictureManager.findHubSupplierValueMapping(spu.getSupplierId());
		if(supplier==null){
			log.info("===="+spu.getSupplierId()+"未找到供应商名称");
			return;
		}
 
    	Spu spuHead = new Spu();
		spuHead.setSupplierId(spu.getSupplierId());
		spuHead.setSpuNo(spu.getSupplierSpuNo());
		spuHead.setStatus(state);
		
	   	PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierNo(supplier.getHubValNo()); 
		pendingProduct.setSupplierId(spu.getSupplierId());
		pendingProduct.setSupplierName(supplier.getHubVal());
		
		PendingSpu pendingSpu = new PendingSpu();
		supplierProductMysqlService.convertHubSpuToPendingSpu(spu, pendingSpu);
		pendingSpu.setSupplierNo(supplier.getHubValNo());
		pendingProduct.setData(pendingSpu);
		
		if(flag){
			//开始构造sku消息头
			List<PendingSku> skus = new ArrayList<PendingSku>();
			List<HubSupplierSkuDto> hubSkus = supplierProductMysqlService.findSupplierSku(spu.getSupplierSpuId());
			List<Sku> headSkus = new ArrayList<Sku>();		
			if(hubSkus != null && hubSkus.size()>0){
				for(HubSupplierSkuDto hubSku : hubSkus){
					try {					
						Sku headSku = new Sku();
						PendingSku pendingSku = new PendingSku();
						//开始保存hubSku到数据库
						hubSku.setSupplierSpuId(pendingSpu.getSupplierSpuId()); //在这里回写supplierSpuId
						convertHubSkuToPendingSku(hubSku,pendingSku);
						skus.add(pendingSku);
						headSku.setSupplierId(spu.getSupplierId());
						headSku.setSkuNo(hubSku.getSupplierSkuNo());
						headSku.setStatus(state);
						headSkus.add(headSku);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}				
				}
			}else{
				log.info("===="+spu.getSupplierId()+":"+spu.getSupplierSpuId()+"无sku信息");
			}
			pendingSpu.setSkus(skus);
			spuHead.setSkus(headSkus);	
		}
		
		pendingProduct.setData(pendingSpu);	
		Map<String,String> headers = new HashMap<String,String>();	
    	headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(spuHead));
    	supplierProductSendToPending.dispatchSupplierProduct(pendingProduct, headers);
    	
	}

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
}
