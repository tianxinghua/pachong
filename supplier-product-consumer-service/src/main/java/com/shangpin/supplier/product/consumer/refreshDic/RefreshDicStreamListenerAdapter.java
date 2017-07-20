package com.shangpin.supplier.product.consumer.refreshDic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.supplier.product.consumer.refreshDic.service.SupplierProductRetryService;
import com.shangpin.supplier.product.consumer.service.SupplierProductMysqlService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSendToPending;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class RefreshDicStreamListenerAdapter {
	
	private static final Integer PAGESIZE = 100;
	@Autowired
	SupplierProductRetryService supplierProductRetryService;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	SupplierProductSendToPending supplierProductSendToPending;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	SupplierProductMysqlService supplierProductMysqlService;
	
	/**
	 * 字典刷新
	 * 
	 * @param message
	 *            消息体
	 * @param headers
	 *            消息头
	 */
	public void refreshDicTask(Task message, Map<String, Object> headers) throws Exception {
		log.info("刷新字典接受到参数：{}",message);
		String taskNo = message.getTaskNo();
		supplierProductRetryService.updateHubSpuImportByTaskNo(TaskState.HANDLEING.getIndex(), taskNo, null, null);
		if (TaskType.REFRESH_DIC.getIndex().equals(message.getType())) {
			String data = message.getData();
			JSONObject json = JSONObject.parseObject(data);
			Integer dicType = Integer.parseInt(json.get("refreshDicType").toString());
			if (dicType==InfoState.RefreshCategory.getIndex()) {
				refreshCategory(json, InfoState.RefreshCategory.getIndex());
			}else if (dicType==InfoState.RefreshSize.getIndex()) {
				refreshSize(json, InfoState.RefreshSize.getIndex());
			}else if(dicType==InfoState.RefreshColor.getIndex()){
				refreshColor(json, InfoState.RefreshColor.getIndex());
			}else if(dicType==InfoState.RefreshSeason.getIndex()){
				refreshSeason(json, InfoState.RefreshSeason.getIndex());
			}else if(dicType==InfoState.RefreshBrand.getIndex()){
				refreshBrand(json, InfoState.RefreshBrand.getIndex());
			}else if(dicType==InfoState.RefreshOrigin.getIndex()){
				refreshOrigin(json, InfoState.RefreshOrigin.getIndex());
			}
		}
		supplierProductRetryService.updateHubSpuImportByTaskNo(TaskState.ALL_SUCCESS.getIndex(), taskNo, null, null);
	}
	
	private void refreshOrigin(JSONObject json, byte state) throws Exception{
		log.info("刷新产地接受到参数：{}",json);
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		if(json.get("supplierVal")!=null){
			String supplierOrigin = json.get("supplierVal").toString();
			criteria.createCriteria().andSupplierOriginEqualTo(supplierOrigin);
			int total = hubSupplierSpuGateWay.countByCriteria(criteria);
			log.info("待刷新产地:"+supplierOrigin+"总数total:"+total);
			if(total>0){
				supplierProductRetryService.sendSupplierSpu(total, criteria, state,false);
			}
		}
	}
	private void refreshBrand(JSONObject json, byte state) throws Exception{
		log.info("刷新品牌接受到参数：{}",json);
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		if(json.get("supplierBrand")!=null){
			String supplierBrand = json.get("supplierBrand").toString();
			criteria.createCriteria().andSupplierBrandnameEqualTo(supplierBrand);
			int total = hubSupplierSpuGateWay.countByCriteria(criteria);
			log.info("待刷新品牌:"+supplierBrand+"总数total:"+total);
			if(total>0){
				supplierProductRetryService.sendSupplierSpu(total, criteria, state,false);
			}
		}
	}

	/**
	 * 刷新季节
	 * @param json
	 * @param state
	 * @throws Exception
	 */
	private void refreshSeason(JSONObject json, byte state)  throws Exception{
		log.info("刷新季节接受到参数：{}",json);
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		if(json.get("supplierId")!=null&&json.get("supplierSeason")!=null){
			String supplierId = json.get("supplierId").toString();
			String supplierSeason = json.get("supplierSeason").toString();
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSeasonnameEqualTo(supplierSeason);
			int total = hubSupplierSpuGateWay.countByCriteria(criteria);
			log.info("待刷新季节:"+supplierSeason+"总数total:"+total);
			if(total>0){
				supplierProductRetryService.sendSupplierSpu(total, criteria, state,true);
			}
		}
	}
	/**
	 * 刷新颜色
	 * @param json
	 * @param state
	 * @throws Exception
	 */
	private void refreshColor(JSONObject json, byte state)  throws Exception{
		log.info("刷新颜色接受到参数：{}",json);
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		String supplierColor = json.get("supplierColor").toString();
		criteria.createCriteria().andSupplierSpuColorEqualTo(supplierColor);;
		int total = hubSupplierSpuGateWay.countByCriteria(criteria);
		log.info("待刷新颜色:"+supplierColor+"总数total:"+total);
		if(total>0){
			supplierProductRetryService.sendSupplierSpu(total, criteria, state,false);
		}
	}

	/**
	 * 刷新尺码
	 * @param json
	 * @param state
	 * @throws Exception
	 */
	private void refreshSize(JSONObject json, byte state) throws Exception{
		log.info("刷新尺码接受到参数：{}",json);
		String supplierId = null;
		if(json.get("supplierId")!=null){
			supplierId = json.get("supplierId").toString();
		}
		String supplierVal = json.get("supplierVal").toString();
		HubSupplierSkuCriteriaDto criteriaSku = new HubSupplierSkuCriteriaDto();
		criteriaSku.setPageNo(1);
		criteriaSku.setPageSize(10000);
		if(StringUtils.isBlank(supplierId)){
			criteriaSku.createCriteria().andSupplierSkuSizeLike("%"+supplierVal+"%");
		}else{
			if("quanju".equals(supplierId)){
				criteriaSku.createCriteria().andSupplierSkuSizeEqualTo(supplierVal);	
			}else{
				criteriaSku.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuSizeEqualTo(supplierVal);
			}
		}
		int total = hubSupplierSkuGateWay.countByCriteria(criteriaSku);
		log.info("待刷新尺码total:"+total);
		if(total>0){
			int pageCount = supplierProductRetryService.getPageCount(total, PAGESIZE);// 页数
			log.info("刷新总页数：" + pageCount);
			long start = System.currentTimeMillis();
			for (int i = 1; i <= pageCount; i++) {
				criteriaSku.setPageNo(i);
				criteriaSku.setPageSize(PAGESIZE);
				List<HubSupplierSkuDto> listSku = hubSupplierSkuGateWay.selectByCriteria(criteriaSku);
				for(HubSupplierSkuDto sku:listSku){
					if(sku.getSupplierSpuId()!=null){
						HubSupplierSpuDto spu = hubSupplierSpuGateWay.selectByPrimaryKey(sku.getSupplierSpuId());
						List<HubSupplierSkuDto> skus = new ArrayList<HubSupplierSkuDto>();
						skus.add(sku);
						supplierProductRetryService.loopProduct(spu, state, skus);	
					}
				}
			}
			log.info("=====系统扫描到需要重新推送的数据结束,耗时{}毫秒======", System.currentTimeMillis() - start);
		}
	}
	/**
	 * 刷新品类
	 * @param json
	 * @param state
	 * @throws Exception
	 */
	private void refreshCategory(JSONObject json, byte state) throws Exception {
		log.info("刷新品类接受到参数：{}",json);
		String supplierId = json.get("supplierId").toString();
		String spplierCategory = json.get("supplierCategory").toString();
		String supplierGender = json.get("supplierGender").toString();
		HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierCategorynameEqualTo(spplierCategory)
				.andSupplierGenderEqualTo(supplierGender);
		int total = hubSupplierSpuGateWay.countByCriteria(criteria);
		log.info("待刷新品类total:"+total);
		if (total > 0) {
			supplierProductRetryService.sendSupplierSpu(total, criteria, state,false);
		}
	}
	
}