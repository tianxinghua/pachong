package com.shangpin.asynchronous.task.consumer.productexport.pending.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

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

import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.util.DownloadPicTool;
import com.shangpin.asynchronous.task.consumer.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.util.ImageUtils;
import com.shangpin.ephub.client.data.mysql.enumeration.IsExportPic;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.gms.dto.HubResponseDto;
import com.shangpin.ephub.client.product.business.gms.dto.SopSkuQueryDto;
import com.shangpin.ephub.client.product.business.gms.dto.SupplierDTO;
import com.shangpin.ephub.client.product.business.gms.gateway.GmsGateWay;
import com.shangpin.ephub.client.product.business.gms.result.BrandDom;
import com.shangpin.ephub.client.product.business.gms.result.FourLevelCategory;
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
	MatchSizeGateWay matchSizeGateWay;
	@Autowired
	GmsGateWay gmsGateWay;
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;

	private static final Integer PAGESIZE = 50;

	private static final Integer SKUPAGESIZE = 50;

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
				pendingQuryDto.setPageIndex(i);
				pendingQuryDto.setPageSize(SKUPAGESIZE);
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
							insertProductSkuOfRow(row, product, sku, rowTemplate);
						} catch (Exception e) {
							log.error("insertProductSkuOfRow异常：" + e.getMessage(), e);
							j--;
						}
					}
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
	private Integer getPageCount(Integer totalSize, Integer pageSize) {
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
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] row0 = TaskImportTemplate.getPendingSpuTemplate();
		for (int i = 0; i < row0.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(row0[i]);
			cell.setCellStyle(style);
		}
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
	private void saveAndUploadExcel(String taskNo, String createUser, HSSFWorkbook wb) {
		FileOutputStream fout = null;
		File file = null;
		try {
			file = new File(ftpProperties.getLocalResultPath() + createUser + "_" + taskNo + ".xls");
			fout = new FileOutputStream(file);
			wb.write(fout);
			log.info(file.getName() + " 生成文件成功！");
			FTPClientUtil.uploadToExportPath(file, file.getName());
			log.info("上传成功！");
			updateHubSpuImportTask(taskNo);
			log.info("更新任务状态成功！");
		} catch (Exception e) {
			log.error("保存并上传ftp时异常：" + e.getMessage(), e);
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
				if (null != file && file.exists()) {
					file.delete();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
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
		Class<?> spuClazz = product.getClass();
		Class<?> skuClazz = sku.getClass();
		Method fieldSetMet = null;
		Object value = null;
		MatchSizeResult matchSizeResult = null;
		if (StringUtils.isBlank(sku.getHubSkuSizeType())) {
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
						|| "marketPriceCurrencyorg".equals(rowTemplate[i]) || "hubSkuSize".equals(rowTemplate[i])) {
					// 所有sku的属性|| "hubSkuSizeType".equals(rowTemplate[i])
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				} else if ("hubSkuSizeType".equals(rowTemplate[i])) {
					fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					if (value != null) {
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
			}
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
				} else if ("seasonName".equals(rowTemplate[i])) {
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
						buffer = buffer.append(memo);
					}
					row.createCell(i).setCellValue(buffer.toString());
				} else if ("spuState".equals(rowTemplate[i])) {
					if (SpuState.INFO_PECCABLE.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("信息待完善");
					} else if (SpuState.INFO_IMPECCABLE.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("信息已完善");
					} else if (SpuState.HANDLED.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("已处理");
					} else if (SpuState.FILTER.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("过滤不处理");
					} else if (SpuState.UNABLE_TO_PROCESS.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("无法处理");
					} else if (SpuState.HANDLING.getIndex() == product.getSpuState()) {
						row.createCell(i).setCellValue("审核中");
					}
				} else {
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
	private void setRowOfSeasonName(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
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
	private void setRowOfSeasonYear(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
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
	private String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	public void exportHubSelected(ProductImportTask message) {
		HubWaitSelectRequestWithPageDto pendingQuryDto = JsonUtil.deserialize(message.getData(),
				HubWaitSelectRequestWithPageDto.class);
		HSSFWorkbook workbook = exportProduct(pendingQuryDto);
		saveAndUploadExcel(message.getTaskNo(), "hubSelected", workbook);
	}

	public HSSFWorkbook exportProduct(HubWaitSelectRequestWithPageDto dto) {

		try {
			long startTime = System.currentTimeMillis();
			dto.setPageNo(0);
			dto.setPageSize(100000);
			log.info("导出查询商品请求参数：{}", dto);
			List<HubWaitSelectResponseDto> list = hubWaitSelectGateWay.selectByPage(dto);
			log.info("导出查询商品耗时：" + (System.currentTimeMillis() - startTime));
			if (list == null || list.size() <= 0) {
				return null;
			}
			HSSFWorkbook workbook = exportExcel(list);
			return workbook;
		} catch (Exception e) {
			log.error("导出查询商品失败：{}", e);
		}
		return null;
	}

	public HSSFWorkbook exportExcel(List<HubWaitSelectResponseDto> list) throws Exception {
		String[] headers = { "尚品Sku编号", "门户Sku编号", "供应商SKU", "商品名称", "品类", "品牌", "品牌中文", "品牌编号", "货号", "商品状态", "生效价格",
				"价格状态", "操作人", "供价*", "供价币种*", "阶段供价", "阶段供价生效时间", "阶段供价失效时间", "市场价", "市场价币种" };
		String[] columns = { "spSkuNo", "skuNo", "supplierSkuNo", "spuName", "categoryName", "brandName", "brandChName",
				"brandNo", "spuModel", "productState", "param1", "param1", "param1", "supplyPrice", "supplyCurry",
				"param1", "param1", "param1", "marketPrice", "marketCurry" };

		Map<String, String> map = null;

		// 拆分数据，以供应商为维度
		Map<String, List<HubWaitSelectResponseDto>> mapSupplier = new HashMap<String, List<HubWaitSelectResponseDto>>();
		for (HubWaitSelectResponseDto response : list) {
			String supplierNo = response.getSupplierNo();
			if (mapSupplier.containsKey(supplierNo)) {
				mapSupplier.get(supplierNo).add(response);
			} else {
				List<HubWaitSelectResponseDto> listSupplier = new ArrayList<HubWaitSelectResponseDto>();
				listSupplier.add(response);
				mapSupplier.put(supplierNo, listSupplier);
			}
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		for (Map.Entry<String, List<HubWaitSelectResponseDto>> entry : mapSupplier.entrySet()) {
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			String supplierNo = entry.getKey();
			List<HubWaitSelectResponseDto> listSupp = entry.getValue();
			for (HubWaitSelectResponseDto response : listSupp) {
				map = new HashMap<String, String>();
				convertTOExcel(response, map);
				result.add(map);
			}
			StringBuffer supplierName = new StringBuffer();

			SupplierDTO supplierDto = gmsGateWay.getSupplierDto(supplierNo);
			if (supplierDto != null && supplierDto.getSupplierName() != null) {
				String name = supplierDto.getSupplierName();
				String reg = "[A-Za-z]+";
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
			ExportExcelUtils.createSheet(workbook, supplierName.toString(), headers, columns, result);
		}
		// workbook.write(ouputStream);
		return workbook;
	}

	private void convertTOExcel(HubWaitSelectResponseDto response, Map<String, String> map) {
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
			map.put("brandName", brandDom.getBrandEnName());
			map.put("brandChName", brandDom.getBrandCnName());
			map.put("brandNo", brandDom.getBrandNo());
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
			log.info("supplierSkuId：" + response.getSupplierSkuId() + "查询sopSkuNo:" + skuNo);
			map.put("skuNo", skuNo);

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
			if (sopSkuResponseDto != null && sopSkuResponseDto.getIsSuccess()
					&& sopSkuResponseDto.getResDatas().size() > 0) {
				return sopSkuResponseDto.getResDatas().get(0).getSopSkuNo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getCategoryName(String categoryNo) {
		FourLevelCategory category = gmsGateWay.findCategory(categoryNo);
		if (category != null) {
			return category.getFourthName();
		} else {
			return categoryNo;
		}
	}

	private BrandDom getBrand(String brandNo) {
		BrandDom brand = gmsGateWay.findBrand(brandNo);
		if (brand != null) {
			return brand;
		} else {
			return null;
		}
	}
}
