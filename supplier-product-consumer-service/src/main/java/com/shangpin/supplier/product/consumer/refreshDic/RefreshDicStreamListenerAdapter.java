package com.shangpin.supplier.product.consumer.refreshDic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
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
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.manager.SupplierProductRetryManager;
import com.shangpin.supplier.product.consumer.service.SupplierProductMysqlService;
import com.shangpin.supplier.product.consumer.service.SupplierProductRetryService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSendToPending;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
import com.shangpin.supplier.product.consumer.service.dto.Spu;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class RefreshDicStreamListenerAdapter {
	
	private static final Integer PAGESIZE = 100;
	@Autowired
	SupplierProductRetryService supplierProductRetryService;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	private SupplierProductRetryManager supplierProductPictureManager;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	SupplierProductMysqlService supplierProductMysqlService;

	/**
	 * 数据监听方法
	 * 
	 * @param message
	 *            消息体
	 * @param headers
	 *            消息头
	 */
	public void refreshDicTask(Task message, Map<String, Object> headers) throws Exception {
		String taskNo = message.getTaskNo();
		updateHubSpuImportByTaskNo(TaskState.HANDLEING.getIndex(), taskNo, null, null);
		if (TaskType.REFRESH_DIC.getIndex().equals(message.getType())) {
			String data = message.getData();
			JSONObject json = JSONObject.parseObject(data);
			String dicType = json.get("refreshDicType").toString();
			if (dicType.equals("4")) {
				refreshCategory(json, (byte) 4);
			}else if (dicType.equals("6")) {
				refreshSize(json, (byte) 6);
			}
		}
	}
	
	private void refreshSize(JSONObject json, byte state) throws Exception{
		
		String supplierId = json.get("supplierId").toString();
		String supplierVal = json.get("supplierVal").toString();
		int type = Integer.parseInt(json.get("type").toString());
		HubSupplierSkuCriteriaDto criteriaSku = new HubSupplierSkuCriteriaDto();
		criteriaSku.setPageNo(1);
		criteriaSku.setPageSize(10000);
		if(StringUtils.isBlank(supplierId)){
			criteriaSku.createCriteria().andSupplierSkuSizeLike("%"+supplierVal+"%");
		}else{
			if("quanju".equals(supplierId)){
				criteriaSku.createCriteria().andSupplierSkuSizeLike("%"+supplierVal+"%");	
			}else{
				criteriaSku.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuSizeEqualTo(supplierVal);
			}
			
		}
		int total = hubSupplierSkuGateWay.countByCriteria(criteriaSku);
		if(total>0){
			sendSize(total, criteriaSku, state);
		}
	}

	private void refreshCategory(JSONObject json, byte state) throws Exception {
		String supplierId = json.get("supplierId").toString();
		String spplierCategory = json.get("supplierCategory").toString();
		String supplierGender = json.get("supplierGender").toString();
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierCategorynameEqualTo(spplierCategory)
				.andSupplierGenderEqualTo(supplierGender);
		int total = hubSupplierSpuGateWay.countByCriteria(criteria);
		if (total > 0) {
			sendCategory(total, criteria, state);
		}
	}

	private void sendCategory(int total,HubSupplierSpuCriteriaDto criteria,byte state) throws Exception{
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
	
	private void sendSize(int total,HubSupplierSkuCriteriaDto criteriaSku,byte state) throws Exception{
		int pageCount = getPageCount(total, PAGESIZE);// 页数
		log.info("刷新总页数：" + pageCount);
		long start = System.currentTimeMillis();
		for (int i = 1; i <= pageCount; i++) {
			criteriaSku.setPageNo(i);
			criteriaSku.setPageSize(PAGESIZE);
			List<HubSupplierSkuDto> listSku = hubSupplierSkuGateWay.selectByCriteria(criteriaSku);
			for(HubSupplierSkuDto sku:listSku){
				if(sku.getSupplierSpuId()!=null){
					HubSupplierSpuDto spu = hubSupplierSpuGateWay.selectByPrimaryKey(sku.getSupplierSpuId());
					loopProduct(spu, state, sku);	
				}
			}
		}
		log.info("=====系统扫描到需要重新推送的数据结束,耗时{}毫秒======", System.currentTimeMillis() - start);
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

	/**
	 * 获取总页数
	 * 
	 * @param totalSize
	 *            总计路数
	 * @param pagesize
	 *            每页记录数
	 * @return
	 */
	private Integer getPageCount(Integer totalSize, Integer pageSize) {
		if (totalSize % pageSize == 0) {
			return totalSize / pageSize;
		} else {
			return (totalSize / pageSize) + 1;
		}
	}

	private void loopProduct(HubSupplierSpuDto spu, byte state, HubSupplierSkuDto hubSku) throws Exception {

		HubSeasonDicDto season = supplierProductPictureManager.findCurrentSeason(spu.getSupplierId());
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

		if (hubSku!=null) {
			// 开始构造sku消息头
			List<PendingSku> skus = new ArrayList<PendingSku>();
			List<Sku> headSkus = new ArrayList<Sku>();
			try {
				Sku headSku = new Sku();
				PendingSku pendingSku = new PendingSku();
				// 开始保存hubSku到数据库
				convertHubSkuToPendingSku(hubSku, pendingSku);
				skus.add(pendingSku);
				headSku.setSupplierId(spu.getSupplierId());
				headSku.setSkuNo(hubSku.getSupplierSkuNo());
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