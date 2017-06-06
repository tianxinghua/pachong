package com.shangpin.asynchronous.task.consumer.productexport.pending.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProductDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProducts;
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.TaskImportTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllProductServiceImpl {
	
	private static final Integer PAGESIZE = 50;
	private static SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	@Autowired
	private HubPendingSkuCheckGateWay hubPendingSkuClient;
	@Autowired
	private MatchSizeGateWay matchSizeGateWay;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;

	public void exportproductAll(String taskNo, PendingQuryDto pendingQuryDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] row0 = TaskImportTemplate.getProductAllTemplate();
		for (int i = 0; i < row0.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(row0[i]);
			cell.setCellStyle(style);
		}
		String[] rowTemplate = TaskImportTemplate.getProductAllValueTemplate();
		int totalSize = pendingQuryDto.getPageSize();// 总记录数
		log.info("全部商品导出总记录数：" + totalSize);
		if (totalSize > 0) {
			List<PendingProducts> lists = new ArrayList<PendingProducts>();
			int pageCount = exportServiceImpl.getPageCount(totalSize, PAGESIZE);// 页数
			log.info("全部商品导出总页数：" + pageCount);
			for (int i = 1; i <= pageCount; i++) {
				pendingQuryDto.setPageIndex(i);
				pendingQuryDto.setPageSize(PAGESIZE);
				PendingProducts products = hubPendingSkuClient.exportPengdingSku(pendingQuryDto);
				lists.add(products);
			}
			int j = 0;
			for (PendingProducts products : lists) {
				for (PendingProductDto product : products.getProduts()) {
					for (HubSkuPendingDto sku : product.getHubSkus()) {
						try {
							j++;
							row = sheet.createRow(j);
							insertProductOfRow(row, product, sku, rowTemplate);
						} catch (Exception e) {
							log.error("insertProductSkuOfRow异常：" + e.getMessage(), e);
							j--;
						}
					}
				}
			}
		}
		exportServiceImpl.saveAndUploadExcel(taskNo, pendingQuryDto.getCreateUser(), wb);
	}
	
	private void insertProductOfRow(HSSFRow row, PendingProductDto product, HubSkuPendingDto sku,
			String[] rowTemplate) throws Exception {
		/**
		 * 价格、库存、最后拉去时间从供应商原始数据中获取
		 */
		HubSupplierSkuDto supplierSku = selectSupplierSku(sku.getSupplierId(),sku.getSupplierSkuNo());
		if(null != supplierSku){
			sku.setMarketPrice(supplierSku.getMarketPrice());
			sku.setSalesPrice(supplierSku.getSalesPrice());
			sku.setSupplyPrice(supplierSku.getSupplyPrice());
			sku.setStock(supplierSku.getStock()); 
		}
		
		Class<?> spuClazz = product.getClass();
		Class<?> skuClazz = sku.getClass();
		Method fieldSetMet = null;
		Object value = null;
		MatchSizeResult matchSizeResult = null;
		if (StringUtils.isBlank(sku.getHubSkuSizeType())||"排除".equals(sku.getHubSkuSizeType())) {
			MatchSizeDto match = new MatchSizeDto();
			match.setHubBrandNo(product.getHubBrandNo());
			match.setHubCategoryNo(product.getHubCategoryNo());
			match.setSize(sku.getHubSkuSize());
			matchSizeResult = matchSizeGateWay.matchSize(match);
		}
		for (int i = 0; i < rowTemplate.length; i++) {
			try {
				String fileName = exportServiceImpl.parSetName(rowTemplate[i]);
				if ("supplierSkuNo".equals(rowTemplate[i]) || "skuName".equals(rowTemplate[i])
						|| "supplierBarcode".equals(rowTemplate[i]) || "supplyPrice".equals(rowTemplate[i])
						|| "supplyPriceCurrency".equals(rowTemplate[i]) || "marketPrice".equals(rowTemplate[i])
						|| "marketPriceCurrencyorg".equals(rowTemplate[i]) || "hubSkuSize".equals(rowTemplate[i])
						|| "spSkuNo".equals(rowTemplate[i]) || "stock".equals(rowTemplate[i])) {
					// 所有sku的属性|| "hubSkuSizeType".equals(rowTemplate[i])
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				}else if("lastPullTime".equals(rowTemplate[i])){
					row.createCell(i).setCellValue((null != supplierSku && supplierSku.getLastPullTime() != null)? format.format(supplierSku.getLastPullTime()) : "");
				}else if ("hubSkuSizeType".equals(rowTemplate[i])) {
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					if (value != null&&!"排除".equals(value)) {
						row.createCell(i).setCellValue(null != value ? value.toString() : "");
					} else {
						if (matchSizeResult != null) {
							if (matchSizeResult.isPassing()) {
								String sizeType = matchSizeResult.getSizeType();
								row.createCell(i).setCellValue(sizeType);
							} else if (matchSizeResult.isFilter()) {
								row.createCell(i).setCellValue("排除");
							}
						}
					}
				} else if ("seasonYear".equals(rowTemplate[i])) {
					exportServiceImpl.setRowOfSeasonYear(row, product, spuClazz, i);
				} else if ("seasonName".equals(rowTemplate[i])) {
					exportServiceImpl.setRowOfSeasonName(row, product, spuClazz, i);
				} else if ("specification".equals(rowTemplate[i])) {
					fieldSetMet = skuClazz.getMethod("getHubSkuSizeType");
					value = fieldSetMet.invoke(sku);
					if ("尺寸".equals(value)) {
						row.createCell(i).setCellValue("尺寸");
					} else {
						if (matchSizeResult != null) {
							if (matchSizeResult.isMultiSizeType()) {
								row.createCell(i).setCellValue("多个模板");
							}
							if (matchSizeResult.isNotTemplate()) {
								row.createCell(i).setCellValue("没有模板");
							}
						} else {
							row.createCell(i).setCellValue("");
						}
					}
				} else if ("originalProductSizeValue".equals(rowTemplate[i])) {
					// 原尺码类型 原尺码值 从哪取值？
					row.createCell(i).setCellValue("");
				} else if ("spuState".equals(rowTemplate[i])) {
					if (SpuState.INFO_PECCABLE.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("待处理");
					} else if (SpuState.INFO_IMPECCABLE.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("待复核");
					} else if (SpuState.HANDLED.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("已处理");
					} else if (SpuState.FILTER.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("过滤不处理");
					} else if (SpuState.UNABLE_TO_PROCESS.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("无法处理");
					} else if (SpuState.HANDLING.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("审核中");
					} else if(SpuState.EXISTED_IN_HUB.getIndex() == product.getSpuState()){
						row.createCell(i).setCellValue("SOP已存在");
					} else if(SpuState.ALL_EXISTED_IN_HUB.getIndex() == product.getSpuState()){
						row.createCell(i).setCellValue("SPU下所有的SKU都在尚品已生成");
					}
				} else if("supplierUrl".equals(rowTemplate[i])){ 
					row.createCell(i).setCellValue(setSupplierUrls(product.getSupplierUrls()));
				} else {
					// 所有spu的属性
					fieldSetMet = spuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				}
			} catch (Exception e) {
				log.error("待处理页导出sku时异常：" + e.getMessage());
				throw e;
			}
		}
	}
	
	private String setSupplierUrls(List<String> supplierUrls){
		String sep = "\\|";
		if(CollectionUtils.isNotEmpty(supplierUrls)){
			StringBuffer buffer = new StringBuffer();
			for(String url : supplierUrls){
				buffer.append(url).append(sep);
			}
			return buffer.toString();
		}
		return "";
	}
	
	private HubSupplierSkuDto selectSupplierSku(String supplierId,String supplierSkuNo){
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setFields("market_price,sales_price,supply_price,stock,last_pull_time");
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSupplierSkuDto> skus = hubSupplierSkuGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(skus)){
			return skus.get(0);
		}else{
			return null;
		}
	}
}
