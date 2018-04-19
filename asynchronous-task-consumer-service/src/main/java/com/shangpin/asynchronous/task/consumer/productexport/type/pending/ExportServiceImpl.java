package com.shangpin.asynchronous.task.consumer.productexport.type.pending;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.util.DownloadPicTool;
import com.shangpin.asynchronous.task.consumer.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.util.HubWaitSelectStateDto;
import com.shangpin.asynchronous.task.consumer.util.ImageUtils;
import com.shangpin.asynchronous.task.consumer.util.excel.ExcelDropdown;
import com.shangpin.ephub.client.data.mysql.enumeration.HubSpuPictureState;
import com.shangpin.ephub.client.data.mysql.enumeration.IsExportPic;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.gms.dto.SopSkuQueryDto;
import com.shangpin.ephub.client.product.business.gms.dto.SupplierDTO;
import com.shangpin.ephub.client.product.business.gms.gateway.GmsGateWay;
import com.shangpin.ephub.client.product.business.gms.result.BrandDom;
import com.shangpin.ephub.client.product.business.gms.result.FourLevelCategory;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.gms.result.SopSkuDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProductDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProducts;
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.TaskImportTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:ExportServiceImpl
 * </p>
 * <p>
 * Description: 生成Excel文件并上传ftp
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author lubaijiang
 * @date 2017年1月11日 下午2:48:16
 *
 */
@Service
@Slf4j
public class ExportServiceImpl {
	
	private static String comma = ",";
	@Autowired
	private FtpProperties ftpProperties;
	@Autowired
	private HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	private HubPendingSpuCheckGateWay hubPendingSpuClient;
	@Autowired
	private HubPendingSkuCheckGateWay hubPendingSkuClient;
	@Autowired
	HubWaitSelectGateWay hubWaitSelectGateWay;
	@Autowired
	private HubSpuPicGateWay hubSpuPicGateWay;
	@Autowired
	MatchSizeGateWay matchSizeGateWay;
	@Autowired
	GmsGateWay gmsGateWay;
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateWay;
	@Autowired
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired
	private ApiAddressProperties apiAddressProperties;

	private static final Integer PAGESIZE = 50;

	private static final Integer SKUPAGESIZE = 50;
	private static String localPath = null;
	@PostConstruct
    public void init(){
	 	localPath = ftpProperties.getLocalResultPath();
    }
	/**
	 * 待处理页面导出sku
	 * 
	 * @param taskNo
	 *            任务编号
	 * @param products
	 */
	public void exportSku(String taskNo, PendingQuryDto pendingQuryDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] row0 = TaskImportTemplate.getPendingSkuTemplate();
		for (int i = 0; i < row0.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(row0[i]);
			cell.setCellStyle(style);
		}
		String[] rowTemplate = TaskImportTemplate.getPendingSkuValueTemplate();
		int totalSize = pendingQuryDto.getPageSize();// 总记录数
		log.info("sku导出总记录数：" + totalSize);
		if (totalSize > 0) {
			List<PendingProducts> lists = new ArrayList<PendingProducts>();
			int pageCount = getPageCount(totalSize, SKUPAGESIZE);// 页数
			log.info("sku导出总页数：" + pageCount);
			for (int i = 1; i <= pageCount; i++) {
				long startTime = System.currentTimeMillis();
				pendingQuryDto.setPageIndex(i);
				pendingQuryDto.setPageSize(SKUPAGESIZE);
				PendingProducts products = hubPendingSkuClient.exportPengdingSku(pendingQuryDto);
				lists.add(products);
				log.info("查询"+i+"页"+SKUPAGESIZE+"条数据耗时：{}",System.currentTimeMillis()-startTime);
			}
	
			int j = 0;
			for (PendingProducts products : lists) {
				for (PendingProductDto product : products.getProduts()) {
					long startTime = System.currentTimeMillis();
					for (HubSkuPendingDto sku : product.getHubSkus()) {
						try {
							j++;
							row = sheet.createRow(j);
							insertProductSkuOfRow(row, product, sku, rowTemplate);
						} catch (Exception e) {
							log.error("insertProductSkuOfRow异常：" + e.getMessage(), e);
							j--;
						}
					}
					log.info("excel插入100条数据耗时：{}",System.currentTimeMillis()-startTime);
				}
			}
		}
		saveAndUploadExcel(taskNo, pendingQuryDto.getCreateUser(), wb);

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

	/**
	 * 待处理页面导出spu
	 * 
	 * @param taskNo
	 *            任务编号
	 * @param products
	 */
	public void exportSpu(String taskNo, PendingQuryDto pendingQuryDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		/**
		 * 第一个sheet：产品信息
		 */
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] row0 = TaskImportTemplate.getPendingSpuTemplate();
		for (int i = 0; i < row0.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(row0[i]);
			cell.setCellStyle(style);
			if(row0[i].startsWith("无法处理原因")){
				sheet.setColumnWidth(i, 6000); 
			}
		}
		/**
		 * 第二个sheet：隐藏
		 */
		ExcelDropdown.creatExcelHidePage(wb);
		
		String[] rowTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
		int totalSize = pendingQuryDto.getPageSize();// 总记录数
		log.info("spu导出总记录数：" + totalSize);
		if (totalSize > 0) {
			List<PendingProducts> lists = new ArrayList<PendingProducts>();
			int pageCount = getPageCount(totalSize, PAGESIZE);// 页数
			log.info("spu导出总页数：" + pageCount);
			for (int i = 1; i <= pageCount; i++) {
				pendingQuryDto.setPageIndex(i);
				pendingQuryDto.setPageSize(PAGESIZE);
				PendingProducts products = hubPendingSpuClient.exportPengdingSpu(pendingQuryDto);
				lists.add(products);
			}
			int j = 0;
			for (PendingProducts products : lists) {
				for (PendingProductDto product : products.getProduts()) {
					try {
						j++;
						row = sheet.createRow(j);
						if (pendingQuryDto.getIsExportPic() == IsExportPic.YES.getIndex()) {
							row.setHeight((short) 1500);
							sheet.setColumnWidth(0, (36 * 150));
						}
						insertProductSpuOfRow(pendingQuryDto.getIsExportPic(), row, product, rowTemplate);
					} catch (Exception e) {
						log.error("insertProductSpuOfRow异常：" + e.getMessage(), e);
						j--;
						throw e;
					}
				}
			}
		}
		saveAndUploadExcel(taskNo, pendingQuryDto.getCreateUser(), wb);
	}

	/**
	 * 上传Excel到ftp
	 * 
	 * @param wb
	 */
	public boolean saveAndUploadExcel(String taskNo, String createUser, HSSFWorkbook wb) throws Exception{
		FileOutputStream fout = null;
		File file = null;
		boolean is_upload_success = false;//主要作用是判断当上传ftp成功后删除源文件
		try {
			file = new File(localPath + createUser + "_" + taskNo + ".xls");
			fout = new FileOutputStream(file);
			wb.write(fout);
			log.info(file.getName() + " 生成文件成功！");
			FTPClientUtil.uploadToExportPath(file, file.getName());
			log.info(taskNo+"上传成功！");
			updateHubSpuImportTask(taskNo);
			log.info(taskNo+" 更新任务状态成功！");
			is_upload_success = true;
		} catch (Exception e) {
			is_upload_success = false;
			log.error(taskNo+" 保存并上传ftp时异常：" + e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
				if (is_upload_success && null != file && file.exists()) {
					file.delete();
				}
			} catch (Exception e2) {
				is_upload_success = false;
				throw e2;
			}
		}
		return is_upload_success;
	}
	
	/**
	 * studio上传Excel到ftp
	 * 
	 * @param wb
	 */
	public boolean saveAndUploadStudioExcel(String taskNo, String createUser, HSSFWorkbook wb) throws Exception{
		FileOutputStream fout = null;
		File file = null;
		boolean is_upload_success = false;//主要作用是判断当上传ftp成功后删除源文件
		try {
			file = new File(localPath + createUser + "_" + taskNo + ".xls");
			fout = new FileOutputStream(file);
			wb.write(fout);
			log.info(file.getName() + " 生成文件成功！");
			FTPClientUtil.uploadToStudioPath(file, file.getName());
			log.info(taskNo+"上传成功！");
			updateHubSpuImportTask(taskNo);
			log.info(taskNo+" 更新任务状态成功！");
			is_upload_success = true;
		} catch (Exception e) {
			is_upload_success = false;
			log.error(taskNo+" 保存并上传ftp时异常：" + e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
				if (is_upload_success && null != file && file.exists()) {
					file.delete();
				}
			} catch (Exception e2) {
				is_upload_success = false;
				throw e2;
			}
		}
		return is_upload_success;
	}

	/**
	 * 更新任务的状态
	 * 
	 * @param taskNo
	 */
	private void updateHubSpuImportTask(String taskNo) {
		HubSpuImportTaskWithCriteriaDto taskDto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskCriteriaDto criteria = new HubSpuImportTaskCriteriaDto();
		criteria.createCriteria().andTaskNoEqualTo(taskNo);
		taskDto.setCriteria(criteria);
		HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
		hubSpuTask.setTaskState((byte) TaskState.ALL_SUCCESS.getIndex());
		hubSpuTask.setUpdateTime(new Date());
		taskDto.setHubSpuImportTask(hubSpuTask);
		spuImportGateway.updateByCriteriaSelective(taskDto);
	}

	/**
	 * 将sku信息插入Excel的一行
	 * 
	 * @param row
	 * @param product
	 * @throws Exception
	 */
	private void insertProductSkuOfRow(HSSFRow row, PendingProductDto product, HubSkuPendingDto sku,
			String[] rowTemplate) throws Exception {
		/**
		 * 查出供应商原始尺码 supplierSkuSize
		 */
		HubSupplierSkuDto supplierSkuNo = selectSupplierSku(sku.getSupplierId(), sku.getSupplierSkuNo());
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
				String fileName = parSetName(rowTemplate[i]);
				if ("supplierSkuNo".equals(rowTemplate[i]) || "skuName".equals(rowTemplate[i])
						|| "supplierBarcode".equals(rowTemplate[i]) || "supplyPrice".equals(rowTemplate[i])
						|| "supplyPriceCurrency".equals(rowTemplate[i]) || "marketPrice".equals(rowTemplate[i])
						|| "marketPriceCurrencyorg".equals(rowTemplate[i])) {
					// 所有sku的属性|| "hubSkuSizeType".equals(rowTemplate[i])
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				} else if ("hubSkuSizeType".equals(rowTemplate[i])) {
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
				}else if("supplierSkuSize".equals(rowTemplate[i])){
					row.createCell(i).setCellValue(null != supplierSkuNo ? supplierSkuNo.getSupplierSkuSize() : "");
				} else if ("hubSkuSize".equals(rowTemplate[i])) {
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					if(matchSizeResult!=null){
						value = matchSizeResult.getSizeValue();
					}
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				} else if ("seasonYear".equals(rowTemplate[i])) {
					setRowOfSeasonYear(row, product, spuClazz, i);
				} else if ("seasonName".equals(rowTemplate[i])) {
					setRowOfSeasonName(row, product, spuClazz, i);
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
	
	private HubSupplierSkuDto selectSupplierSku(String supplierId, String supplierSkuNo){
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setFields("supplier_sku_size");
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSupplierSkuDto>  skus = hubSupplierSkuGateWay.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(skus)){
			return skus.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 将spu信息插入Excel的一行
	 * 
	 * @param row
	 * @param product
	 * @param rowTemplate
	 *            导入模板
	 * @throws Exception
	 */
	private void insertProductSpuOfRow(int isExportPic, HSSFRow row, PendingProductDto product, String[] rowTemplate)
			throws Exception {
		String[] errorReasons = null;
		if(StringUtils.isNotBlank(product.getErrorReason())){
			errorReasons = product.getErrorReason().split(",");
		}
		
		Class<?> cls = product.getClass();
		StringBuffer buffer = new StringBuffer();
		Method fieldSetMet = null;
		Object value = null;
		for (int i = 0; i < rowTemplate.length; i++) {
			try {
				if ("spPicUrl".equals(rowTemplate[i])) {
					if (isExportPic == IsExportPic.YES.getIndex()) {
						insertImageToExcel(product.getSpPicUrl(), row, (short) i);
					}
				} else if ("seasonYear".equals(rowTemplate[i])) {
					setRowOfSeasonYear(row, product, cls, i);
				} else if ("seasonYear".equals(rowTemplate[i])) {
					setRowOfSeasonYear(row, product, cls, i);
				}else if ("totalStock".equals(rowTemplate[i])) {
					setStockTotal(row, product, cls, i);
				}else if ("seasonName".equals(rowTemplate[i])) {
					setRowOfSeasonName(row, product, cls, i);
				} else if ("memo".equals(rowTemplate[i])) {
					if (null == product.getSpuModelState() || 1 != product.getSpuModelState()) {
						buffer = buffer.append("货号").append(comma);
					}
					if (null == product.getCatgoryState() || 1 != product.getCatgoryState()) {
						buffer = buffer.append("品类编号").append(comma);
					}
					if (null == product.getPicState() || 1 != product.getPicState()) {
						buffer = buffer.append("图片").append(comma);
					}
					if (null == product.getSpuBrandState() || 1 != product.getSpuBrandState()) {
						buffer = buffer.append("品牌编号").append(comma);
					}
					if (null == product.getSpuGenderState() || 1 != product.getSpuGenderState()) {
						buffer = buffer.append("适应性别").append(comma);
					}
					if (null == product.getSpuSeasonState() || 1 != product.getSpuSeasonState()) {
						buffer = buffer.append("上市季节").append(comma);
					}
					if (null == product.getSpuColorState() || 1 != product.getSpuColorState()) {
						buffer = buffer.append("颜色").append(comma);
					}
					if (null == product.getSpSkuSizeState() || 1 != product.getSpSkuSizeState()) {
						buffer = buffer.append("尺码").append(comma);
					}
					String memo = product.getMemo();
					if (!StringUtils.isEmpty(memo)) {
						buffer = buffer.append(memo).append(comma);
					}
					if (!StringUtils.isEmpty(product.getAuditOpinion())) {
						buffer = buffer.append(product.getAuditOpinion());
					}
					row.createCell(i).setCellValue(buffer.toString());
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
						row.createCell(i).setCellValue("SPU在HUB已存在");
					}
				} else if("productInfoUrl".equals(rowTemplate[i])){
					row.createCell(i).setCellValue(apiAddressProperties.getPendingProductInfoUrl()+product.getSpuPendingId());
				} else if(rowTemplate[i].startsWith("reason1")){
					row.createCell(i).setCellValue(null != errorReasons && errorReasons.length>0 ? errorReasons[0] : "");
					ExcelDropdown.setDataValidation(row, i); 
				} else if(rowTemplate[i].startsWith("reason2")){
					row.createCell(i).setCellValue(null != errorReasons && errorReasons.length>1 ? errorReasons[1] : "");
					ExcelDropdown.setDataValidation(row, i); 
				} else if(rowTemplate[i].startsWith("reason3")){
					row.createCell(i).setCellValue(null != errorReasons && errorReasons.length>2 ? errorReasons[2] : "");
					ExcelDropdown.setDataValidation(row, i); 
				}  else if(rowTemplate[i].startsWith("reason4")){
					row.createCell(i).setCellValue(null != errorReasons && errorReasons.length>3 ? errorReasons[3] : "");
					ExcelDropdown.setDataValidation(row, i); 
				}  else if(rowTemplate[i].startsWith("picRetry")){
					row.createCell(i).setCellValue("");
				}else {
					if ("specificationType".equals(rowTemplate[i])) {
						continue;
					}
					String fileName = parSetName(rowTemplate[i]);
					fieldSetMet = cls.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				}
			} catch (Exception e) {
				log.error("待处理页导出spu时异常：" + e.getMessage());
				continue;
			}
		}
	}

	private void setStockTotal(HSSFRow row, PendingProductDto product, Class<?> clazz, int i) {
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setFields("stock");
		criteria.createCriteria().andSupplierIdEqualTo(product.getSupplierId()).andSupplierSpuIdEqualTo(product.getSupplierSpuId()).andStockGreaterThan(0); 
		criteria.setPageNo(1);
		criteria.setPageSize(10000);
		List<HubSupplierSkuDto> list = hubSupplierSkuGateWay.selectByCriteria(criteria);
		int totalStock = 0;
		if(list!=null&&list.size()>0){
			log.info("supplierSpuId："+product.getSupplierSpuId()+"下sku数量："+list.size());
			for(HubSupplierSkuDto sku:list){
				totalStock+=sku.getStock().intValue();
			}
		}
		row.createCell(i).setCellValue(totalStock);
	}

	/**
	 * 设置导出上市季节的值，这个字段比较特殊，是从hubSeason字段拆解出来的
	 * 
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void setRowOfSeasonName(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i).setCellValue((null != value && value.toString().contains("_"))
				? value.toString().split("_")[1] : (null != value ? value.toString() : ""));
	}

	/**
	 * 设置导出上市年份的值，这个字段比较特殊，是从hubSeason字段拆解出来的
	 * 
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void setRowOfSeasonYear(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i)
				.setCellValue((null != value && value.toString().contains("_")) ? value.toString().split("_")[0] : "");
	}

	/**
	 * 插入图片
	 * 
	 * @param url
	 *            图片链接
	 * @param row
	 *            Excel的一行
	 * @param startColumn
	 *            图片插入开始列
	 */
	private void insertImageToExcel(String url, HSSFRow row, short startColumn) {
		BufferedImage bufferImg = null;
		try {
			log.info("url: " + url);
			if (!StringUtils.isEmpty(url)) {
				byte[] bytes = DownloadPicTool.downImage(url);
				if (null != bytes) {
					ByteArrayInputStream input = new ByteArrayInputStream(bytes);
					bufferImg = ImageUtils.singleScale2OfByteArrayInputStream(input, 122, 87, false);
					ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
					ImageIO.write(bufferImg, "jpg", byteArrayOut);
					HSSFPatriarch patriarch = row.getSheet().createDrawingPatriarch();
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 500, 255, startColumn, row.getRowNum(),
							startColumn, row.getRowNum());
					patriarch.createPicture(anchor, row.getSheet().getWorkbook().addPicture(byteArrayOut.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));
				} else {
					row.createCell((int) startColumn).setCellValue("图片错误");
				}
			} else {
				row.createCell((int) startColumn).setCellValue("无图片");
			}
		} catch (Exception e) {
			log.error("插入图片异常：" + e.getMessage());
			bufferImg = null;
			row.createCell((int) startColumn).setCellValue("图片错误");
		}
	}

	/**
	 * 构造属性的get方法，比如传入name，返回getName
	 * 
	 * @param fieldName
	 * @return
	 */
	public String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	public void exportHubSelected(Task message) throws Exception{
		HubWaitSelectRequestWithPageDto pendingQuryDto = JsonUtil.deserialize(message.getData(),
				HubWaitSelectRequestWithPageDto.class);
		HSSFWorkbook workbook = exportProduct(pendingQuryDto);
		if(workbook!=null){
			saveAndUploadExcel(message.getTaskNo(), pendingQuryDto.getCreateUser(), workbook);	
		}
	}

	public HSSFWorkbook exportProduct(HubWaitSelectRequestWithPageDto dto) throws Exception{
		long startTime = System.currentTimeMillis();
		dto.setPageNo(0);
		dto.setPageSize(100000);
		log.info("导出查询商品请求参数：{}", dto);
		List<HubWaitSelectResponseDto> list = hubWaitSelectGateWay.selectByPage(dto);
		if (list == null || list.size() <= 0) {
			return null;
		}
		log.info("导出查询商品耗时：" + (System.currentTimeMillis() - startTime)+",总记录数："+list.size());
		HSSFWorkbook workbook = exportExcel(list);
		return workbook;
	}

	public HSSFWorkbook exportExcel(List<HubWaitSelectResponseDto> list) throws Exception {
		String[] headers = { "尚品Sku编号", "门户Sku编号", "供应商SKU", "商品名称", "品类", "品牌", "品牌中文", "品牌编号", "货号", "商品状态", "生效价格",
				"价格状态", "操作人", "供价*", "供价币种*", "阶段供价", "阶段供价生效时间", "阶段供价失效时间", "市场价", "市场价币种" };
		String[] columns = { "spSkuNo", "skuNo", "supplierSkuNo", "spuName", "categoryName", "brandName", "brandChName",
				"brandNo", "spuModel", "productState", "param1", "param1", "param1", "supplyPrice", "supplyCurry",
				"param1", "param1", "param1", "marketPrice", "marketCurry" };
		
		
		String[] allHeaders = { "供应商supplierNo","供应商名称","尚品Sku编号", "门户Sku编号", "供应商SKU", "商品名称", "品类", "品牌", "品牌中文", "品牌编号","尺码类型","尺码","颜色","材质", "货号", "商品状态", "生效价格",
				"价格状态", "操作人", "供价*", "供价币种*", "阶段供价", "阶段供价生效时间", "阶段供价失效时间", "市场价", "市场价币种" };
		String[] allColumns = {"supplierNo","supplierName", "spSkuNo", "skuNo", "supplierSkuNo", "spuName", "categoryName", "brandName", "brandChName",
				"brandNo","sizeType","size","color","material", "spuModel", "productState", "param1", "param1", "param1", "supplyPrice", "supplyCurry",
				"param1", "param1", "param1", "marketPrice", "marketCurry" };

		Map<String, String> map = null;

		// 拆分数据，以供应商为维度
		Map<String, List<HubWaitSelectResponseDto>> mapSupplier = new HashMap<String, List<HubWaitSelectResponseDto>>();
		for (HubWaitSelectResponseDto response : list) {
			
			String supplierNo = response.getSupplierNo();
			if(StringUtils.isBlank(supplierNo)){
				continue;
			}
			if (mapSupplier.containsKey(supplierNo)) {
				mapSupplier.get(supplierNo).add(response);
			} else {
				List<HubWaitSelectResponseDto> listSupplier = new ArrayList<HubWaitSelectResponseDto>();
				listSupplier.add(response);
				mapSupplier.put(supplierNo, listSupplier);
			}
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		List<Map<String, String>> allResult = new ArrayList<Map<String, String>>();
		for (Map.Entry<String, List<HubWaitSelectResponseDto>> entry : mapSupplier.entrySet()) {
			
			StringBuffer supplierName = new StringBuffer();
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			String supplierNo = entry.getKey();
			SupplierDTO supplierDto = null;
			try{
				supplierDto = gmsGateWay.getSupplierDto(supplierNo);
			}catch(Exception e){
				log.error("===获取供应商信息报错：{}",e);
			}
			
			List<HubWaitSelectResponseDto> listSupp = entry.getValue();
			for (HubWaitSelectResponseDto response : listSupp) {
				map = new HashMap<String, String>();
				if(supplierDto!=null){
					convertTOExcel(response, map,supplierDto.getSupplierName());	
				}else{
					convertTOExcel(response, map,supplierNo);
				}
				
				result.add(map);
			}
			Map<String,String> temMap = new HashMap<>();
			if (supplierDto != null && supplierDto.getSupplierName() != null) {
				String name = supplierDto.getSupplierName();
				String reg = "[A-Za-z0-9]+";
				Pattern pattern = Pattern.compile(reg);
				Matcher matcher = pattern.matcher(name);
				while (matcher.find()) {
					String fqdnId = matcher.group();
					supplierName.append(fqdnId).append(" ");
				}
			} else {
				supplierName.append(supplierNo);
			}
			if (StringUtils.isBlank(supplierName.toString())) {
				supplierName.append(supplierNo);
			}
			allResult.addAll(result);
			if(temMap.containsKey(supplierName.toString())){
				supplierName= supplierName.append("-").append(supplierNo).append(new Random().nextInt(10) + 1);
			}
			log.info("sheetName:"+supplierName.toString());
			temMap.put(supplierName.toString(), "");	
			ExportExcelUtils.createSheet(workbook, supplierName.toString(), headers,  columns, result);
		}
		log.info("allResult:"+allResult.size());
		ExportExcelUtils.createSheet(workbook, "all", allHeaders,allColumns, allResult);
		return workbook;
	}
	private void convertTOExcel(HubWaitSelectResponseDto response, Map<String, String> map,String supplierName) {
		if (response.getSupplierSkuId() != null) {

			HubSupplierSkuDto listSku = hubSupplierSkuGateWay.selectByPrimaryKey(response.getSupplierSkuId());
			BigDecimal supplyPrice = null;
			BigDecimal marketPrice = null;
			String supplyCurry = null;
			String marketCurry = null;
			if (listSku != null) {
				supplyPrice = listSku.getSupplyPrice();
				marketPrice = listSku.getMarketPrice();
				supplyCurry = listSku.getSupplyPriceCurrency();
				marketCurry = listSku.getMarketPriceCurrencyorg();
			}
			BrandDom brandDom = getBrand(response.getBrandNo());
		
			String categoryName = getCategoryName(response.getCategoryNo());
			
			if(brandDom!=null){
				map.put("brandName", brandDom.getBrandEnName());
				map.put("brandChName", brandDom.getBrandCnName());
				map.put("brandNo", brandDom.getBrandNo());
			}else{
				map.put("brandNo", response.getBrandNo());
			}
			
			if (supplyPrice != null) {
				map.put("supplyPrice", supplyPrice + "");
			}
			if (marketPrice != null) {
				map.put("marketPrice", marketPrice + "");
			}
			map.put("supplyCurry", supplyCurry);
			map.put("marketCurry", marketCurry);
			map.put("spSkuNo", response.getSpSkuNo());
			map.put("spuName", response.getSpuName());
			map.put("supplierSkuNo", response.getSupplierSkuNo());
			map.put("spuModel", response.getSpuModel());
			map.put("categoryName", categoryName);
			// map.put("color", response.getHubColor());
			// map.put("material", response.getMaterial());
			// map.put("origin", response.getOrigin());
			// map.put("gender", response.getGender());
			map.put("supplierNo", response.getSupplierNo());
			// map.put("size", response.getSkuSize());
			map.put("updateTime", DateTimeUtil.getTime(response.getUpdateTime()));

			String skuNo = getSopSkuNo(response.getSupplierId(), response.getSupplierSkuNo());
			map.put("skuNo", skuNo);
			
			//新增
			map.put("supplierNo",response.getSupplierNo());
			map.put("supplierName",supplierName);
			map.put("sizeType",response.getSkuSizeType());
			map.put("size",response.getSkuSize());
			map.put("color",response.getHubColor());
			map.put("material",response.getMaterial());
			
			
		}
	}

	private String getSopSkuNo(String supplierId, String supplierSkuNo) {
		SopSkuQueryDto queryDto = new SopSkuQueryDto();
		queryDto.setSopUserNo(supplierId);
		List<String> list = new ArrayList<String>();
		list.add(supplierSkuNo);
		queryDto.setLstSupplierSkuNo(list);
		HubResponseDto<SopSkuDto> sopSkuResponseDto = null;
		try {
			sopSkuResponseDto = gmsGateWay.findSopSkuNo(queryDto);
			log.info("sopSkuResponseDto:{}",sopSkuResponseDto);
			if (sopSkuResponseDto != null && sopSkuResponseDto.getIsSuccess()
					&& sopSkuResponseDto.getResDatas().size() > 0) {
				return sopSkuResponseDto.getResDatas().get(0).getSopSkuNo();
			}
		} catch (Exception e) {
			log.error("===获取spSkuNo信息报错：{}",e);
		}
		return null;
	}

	private String getCategoryName(String categoryNo) {
		
		try{
			FourLevelCategory category = gmsGateWay.findCategory(categoryNo);
			if (category != null) {
				return category.getFourthName();
			} 
		}catch(Exception e){
			log.error("===获取category信息报错：{}",e);
		}
		return categoryNo;
	}

	private BrandDom getBrand(String brandNo) {
		
		try{
			BrandDom brand = gmsGateWay.findBrand(brandNo);
			if (brand != null) {
				return brand;
			}
		}catch(Exception e){
			log.error("===获取brandNo信息报错：{}",e);
		}
		return null;
	}

	public void exportSupplierPic(Task message) throws Exception  {
		
		HubWaitSelectRequestWithPageDto pendingQuryDto = JsonUtil.deserialize(message.getData(),
				HubWaitSelectRequestWithPageDto.class);
		
		pendingQuryDto.setPageNo(0);
		pendingQuryDto.setPageSize(100000);
//		List<HubWaitSelectResponseDto> list = hubWaitSelectGateWay.selectByPage(pendingQuryDto);
		Map<Long,String> mapTemp = new HashMap<Long,String>();
		
		HSSFWorkbook workbook = exportSupplierPicExcel(pendingQuryDto,mapTemp);
		
		saveAndUploadExcel(message.getTaskNo(),pendingQuryDto.getCreateUser(), workbook);
	}
	public void exportHubPicSelected(Task message) throws Exception  {

		HubWaitSelectRequestWithPageDto pendingQuryDto = JsonUtil.deserialize(message.getData(),
				HubWaitSelectRequestWithPageDto.class);
		HSSFWorkbook workbook = exportPic(pendingQuryDto);
		saveAndUploadExcel(message.getTaskNo(),pendingQuryDto.getCreateUser(), workbook);
	}
	public void exportHubPicSelected2(Task message) throws Exception  {

		HubWaitSelectRequestWithPageDto pendingQuryDto = JsonUtil.deserialize(message.getData(),
				HubWaitSelectRequestWithPageDto.class);
		pendingQuryDto.setPictureState(HubSpuPictureState.UNHANDLED.getIndex());
		pendingQuryDto.setPageNo(0);
		List<Byte> selectList = new ArrayList<Byte>();
		selectList.add((byte)2);
		pendingQuryDto.setSupplierSelectState(selectList);
		pendingQuryDto.setPageSize(100000);
		List<HubWaitSelectResponseDto> list = hubWaitSelectGateWay.selectByPage(pendingQuryDto);
		Map<Long,String> mapTemp = new HashMap<Long,String>();
		HSSFWorkbook workbook = exportPicExcel(list,mapTemp);
		//如果上传成功，则更显数据库pictureState状态
		if(saveAndUploadExcel(message.getTaskNo(),pendingQuryDto.getCreateUser(), workbook)){
			HubSpuDto hubSpu = new HubSpuDto();
			hubSpu.setPictureState(HubSpuPictureState.HANDLED.getIndex());
			hubSpu.setPictureExportTime(new Date());
			hubSpu.setPictureExportUser(pendingQuryDto.getCreateUser());
			for(Map.Entry<Long,String> entry:mapTemp.entrySet()){
				hubSpu.setSpuId(entry.getKey());
				hubSpuGateWay.updateByPrimaryKeySelective(hubSpu);
			}
		}
	}
	public void exportHubCheckPicSelected(Task message)  throws Exception{
		Gson gson = new Gson();
		List<HubWaitSelectStateDto> list = gson.fromJson(message.getData(),  new TypeToken<ArrayList<HubWaitSelectStateDto>>()
        {}.getType());
		HSSFWorkbook workbook = exportSelectPicExcel(list);
		saveAndUploadExcel(message.getTaskNo(),list.get(0).getCreateUser(), workbook);
	}
	public HSSFWorkbook exportSelectPicExcel(List<HubWaitSelectStateDto> list) throws Exception {

		String[] headers = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String[] columns = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String title = "选中图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		Map<String, String> mapTemp = new HashMap<String,String>();
		for (HubWaitSelectStateDto response : list) {
			map = new HashMap<String, String>();
			
			String spuNo = response.getSpuNo();
			if(mapTemp.containsKey(spuNo)){
				continue;
			}
			Long hubSpuId = response.getSpuId();
			Long hubSkuId = response.getSkuId();
			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			int i = 1;
			if (listPic == null || listPic.size() <= 0) {
				continue;
			}
			mapTemp.put(spuNo, null);
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				i++;
			}
			HubSkuDto skuDto = hubSkuGateWay.selectByPrimaryKey(hubSkuId);
			map.put("spSkuNo", skuDto.getSpSkuNo());
			map.put("hubSpuNo", "HUB-"+skuDto.getSpuNo());
			result.add(map);
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		ExportExcelUtils.createSheet(workbook,title, headers, columns, result);
		return workbook;
	}
	private HSSFWorkbook exportPic(HubWaitSelectRequestWithPageDto pendingQuryDto) throws Exception  {
		pendingQuryDto.setPageNo(0);
		pendingQuryDto.setPageSize(100000);
		List<HubWaitSelectResponseDto> list = hubWaitSelectGateWay.selectByPage(pendingQuryDto);
		Map<Long,String> mapTemp = new HashMap<Long,String>();
		HSSFWorkbook wb = exportPicExcel(list,mapTemp);
		return wb;
	}
	
	public HSSFWorkbook exportPicExcel(List<HubWaitSelectResponseDto> list,Map<Long,String> mapTemp)  throws Exception  {

		String[] headers = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10","操作人","导出时间" };
		String[] columns = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" ,"pictureExportUser","pictureExportTime"};
		String title = "图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		log.info("list总大小："+list.size());
		long start = System.currentTimeMillis();
		for (HubWaitSelectResponseDto response : list) {
			log.info("");
			Long hubSpuId = response.getSpuId();
			if(mapTemp.containsKey(hubSpuId)){
				continue;
			}
			
			map = new HashMap<String, String>();
			long start1 = System.currentTimeMillis();
			String spSkuNo = response.getSpSkuNo();
			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			log.info("查询图片时间："+(System.currentTimeMillis()-start1));
			int i = 1;
			if (listPic == null || listPic.size() <= 0) {
				continue;
			}
			mapTemp.put(hubSpuId, null);
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				
				i++;
			}
			map.put("spSkuNo", spSkuNo);
			map.put("hubSpuNo", "HUB-"+response.getSpuNo());
			map.put("pictureExportUser",response.getPictureExportUser());
			if(response.getPictureExportTime()!=null){
				map.put("pictureExportTime",DateTimeUtil.getTime(response.getPictureExportTime()));	
			}
			result.add(map);
		}
		log.info("生成excel时间："+(System.currentTimeMillis()-start));
		HSSFWorkbook workbook = new HSSFWorkbook();
		ExportExcelUtils.createSheet(workbook,title, headers, columns, result);
		log.info("导出时间："+(System.currentTimeMillis()-start));
		return workbook;
	}
	public static void main(String[] args) {
		String spSkuNo = "30790817005";
		String spSpuNo = spSkuNo.substring(0,spSkuNo.length()-3);
		System.out.println(spSpuNo);
	}
	public HSSFWorkbook exportSupplierPicExcel(HubWaitSelectRequestWithPageDto pendingQuryDto,Map<Long,String> mapTemp)  throws Exception  {
		
		String[] headers = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10","操作人","导出时间" };
		String[] columns = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" ,"pictureExportUser","pictureExportTime"};
		String title = "图片导出";
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String spSkuNos = pendingQuryDto.getSpSkuNo();
		List<String> list = new ArrayList<String>();
		Map<String,String> mapSpSkuNo = null;
		Map<String, String> map = null;
		if(spSkuNos!=null){
			mapSpSkuNo = new HashMap<String,String>();
			String [] arr = spSkuNos.split("\n");
			for(String spSkuNo:arr){
				String spSpuNo = spSkuNo.substring(0,spSkuNo.length()-3);//
				if(!mapSpSkuNo.containsKey(spSpuNo)){
					HubSkuCriteriaDto criteria = new HubSkuCriteriaDto();
					criteria.createCriteria().andSpSkuNoEqualTo(spSkuNo);
					List<HubSkuDto> listHubSku = hubSkuGateWay.selectByCriteria(criteria);
					if(listHubSku!=null){
						mapSpSkuNo.put(spSpuNo,spSkuNo);
						list.add(listHubSku.get(0).getSpSkuNo());
					}
				}
			}
			
			HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
			for(String spSkuNo:list){
				criteria.createCriteria().andSpSkuNoEqualTo(spSkuNo);
				List<HubSupplierSkuDto> listMapp = hubSupplierSkuGateWay.selectByCriteria(criteria);
				if(listMapp!=null){
					for(HubSupplierSkuDto mapp:listMapp){
						Long supplierSpuId = mapp.getSupplierSpuId();
						HubSpuPendingPicCriteriaDto spuPiccriteria = new HubSpuPendingPicCriteriaDto();
						spuPiccriteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
						List<HubSpuPendingPicDto> listPic = hubSpuPendingPicGateWay.selectByCriteria(spuPiccriteria);
						if(listPic!=null&&listPic.size()>0){
							map = new HashMap<String, String>();
							int i = 1;
							for(HubSpuPendingPicDto spuPic:listPic){
								String picUrl = spuPic.getPicUrl();
								if (i == 11) {
									break;
								}
								map.put("url" + i, picUrl);
								i++;
							}
							map.put("spSkuNo", spSkuNo);
							map.put("hubSpuNo", "HUB-");
							map.put("pictureExportUser",null);
							map.put("pictureExportTime",null);	
							result.add(map);
						}
					}
				}
			}
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		ExportExcelUtils.createSheet(workbook,title, headers, columns, result);
		return workbook;
	}


}
