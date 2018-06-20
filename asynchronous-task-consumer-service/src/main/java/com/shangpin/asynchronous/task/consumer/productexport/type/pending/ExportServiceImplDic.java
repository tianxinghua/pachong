package com.shangpin.asynchronous.task.consumer.productexport.type.pending;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto.*;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.util.DownloadPicTool;
import com.shangpin.asynchronous.task.consumer.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.util.HubWaitSelectStateDto;
import com.shangpin.asynchronous.task.consumer.util.ImageUtils;
import com.shangpin.asynchronous.task.consumer.util.excel.ExcelDropdown;
import com.shangpin.ephub.client.data.mysql.brand.dto.*;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.*;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubMaterialMappingGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.*;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.processing.RoundEnvironment;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ExportServiceImplDic {

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
	@Autowired
	HubColorDicItemGateWay hubColorDicItemGateWay;
	@Autowired
	HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;
	@Autowired
	HubMaterialMappingGateWay hubMaterialMappingGateWay;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	HubSupplierBrandDicGateWay hubSupplierBrandDicGateWay;
	@Autowired
	HubColorDicGateWay hubColorDicGateWay;
	@Autowired
	private HubBrandDicGateway brandDicGateway;
	@Autowired
    GmsGateWay getGmsGateWay;
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	private static final Integer PAGESIZE = 50;

	private static final Integer SKUPAGESIZE = 50;
	private static String localPath = null;
	@PostConstruct
    public void init(){
	 	localPath = ftpProperties.getLocalResultPath();
    }

	/**
	 * 获取总页数
	 *
	 * @param totalSize
	 *            总计路数
	 * @param pageSize
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
	 * 字典品牌的导出
	 */
	public void exportBrand (String taskNo,BrandRequestDTO brandRequestDTO) throws Exception {
		/*HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("品牌信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] brandTemplate = TaskImportTemplate2.getBrandTemplate();
		for (int i = 0; i < brandTemplate .length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(brandTemplate[i]);
		}
		String[] brandValueTemplate = TaskImportTemplate2.getBrandValueTemplate();
*/
		if(Integer.parseInt(brandRequestDTO.getType())==0){
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("品牌信息");
			HSSFRow row = sheet.createRow(0);
			HSSFCellStyle style = wb.createCellStyle();
			String[] brandTemplate = TaskImportTemplate2.getBrandTemplate();
			for (int i = 0; i < brandTemplate .length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(brandTemplate[i]);
			}
			String[] brandValueTemplate = TaskImportTemplate2.getBrandValueTemplate();
			//查询hubSupplierBrand表
			HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
			HubSupplierBrandDicCriteriaDto.Criteria criteria = hubSupplierBrandDicCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(brandRequestDTO.getSupplierId())){
				criteria.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
				criteria.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
				criteria.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
				criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getEndTime());
				Calendar calendar   = Calendar.getInstance();
				calendar.setTime(parse);
				calendar.add(calendar.DAY_OF_MONTH,1);
				criteria.andUpdateTimeLessThan(calendar.getTime());
			}
			criteria.andPushStateEqualTo((byte)0);

			HubSupplierBrandDicCriteriaDto.Criteria criteria2 = hubSupplierBrandDicCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(brandRequestDTO.getSupplierId())){
				criteria2.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
				criteria2.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
				criteria2.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
				criteria2.andUpdateTimeGreaterThanOrEqualTo(parse);
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getEndTime());
				Calendar calendar   = Calendar.getInstance();
				calendar.setTime(parse);
				calendar.add(calendar.DAY_OF_MONTH,1);
				criteria2.andUpdateTimeLessThan(calendar.getTime());
			}
			hubSupplierBrandDicCriteriaDto.or(criteria2.andPushStateIsNull());

			//总条数
			int totalSize = hubSupplierBrandDicGateWay.countByCriteria(hubSupplierBrandDicCriteriaDto);
			if (totalSize>0){
				//总页数
				int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
				log.info("导出总页数：" + pageCount);
				ArrayList<List<HubSupplierBrandDicDto>> lists = new ArrayList<>();
				for(int i=1;i<=pageCount;i++){
					HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto1 = new HubSupplierBrandDicCriteriaDto();
					HubSupplierBrandDicCriteriaDto.Criteria criteria3 = hubSupplierBrandDicCriteriaDto.createCriteria();
					hubSupplierBrandDicCriteriaDto1.setPageNo(i);
					hubSupplierBrandDicCriteriaDto1.setPageSize(PAGESIZE);
					if(StringUtils.isNotBlank(brandRequestDTO.getSupplierId())){
						criteria3.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
						criteria3.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
						criteria3.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
						criteria3.andUpdateTimeGreaterThan(parse);
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getEndTime()+" 23:59:59");
						criteria3.andUpdateTimeLessThanOrEqualTo(parse);
					}
					criteria3.andPushStateEqualTo((byte)0);

					HubSupplierBrandDicCriteriaDto.Criteria criteria4 = hubSupplierBrandDicCriteriaDto.createCriteria();
					if(StringUtils.isNotBlank(brandRequestDTO.getSupplierId())){
						criteria4.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
						criteria4.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
						criteria4.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
						criteria4.andUpdateTimeGreaterThan(parse);
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getEndTime()+" 23:59:59");
						criteria4.andUpdateTimeLessThanOrEqualTo(parse);
					}
					hubSupplierBrandDicCriteriaDto1.or(criteria4.andPushStateIsNull());

					List<HubSupplierBrandDicDto> hubSupplierBrandDicDtos=hubSupplierBrandDicGateWay.selectByCriteria(hubSupplierBrandDicCriteriaDto1);
					lists.add(hubSupplierBrandDicDtos);
					int j=0;
					for(List<HubSupplierBrandDicDto> list:lists){
						for(HubSupplierBrandDicDto brandDto:list){
							j++;
							HSSFRow row1 = sheet.createRow(j);
							insertProductBrandOfRow(row1,brandDto,brandValueTemplate);
						}
					}

					saveAndUploadExcel(taskNo,"brand",wb);
				}
			}
		}



		if(Integer.parseInt(brandRequestDTO.getType())==1){
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("品牌信息");
			HSSFRow row = sheet.createRow(0);
			HSSFCellStyle style = wb.createCellStyle();
			String[] brandTemplate = TaskImportTemplate2.getBrandTemplate();
			for (int i = 0; i < brandTemplate .length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(brandTemplate[i]);
			}
			String[] brandValueTemplate = TaskImportTemplate2.getBrandValueTemplate();
			//查询hubBrandDic表
			//return getHubBrandDic(brandRequestDTO);
			HubBrandDicCriteriaDto cruteria = new HubBrandDicCriteriaDto();
			HubBrandDicCriteriaDto.Criteria criteria = cruteria.createCriteria();
			if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
				criteria.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
				criteria.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
			}
			if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
				criteria.andUpdateTimeGreaterThanOrEqualTo(parse);

			}
			if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(brandRequestDTO.getEndTime());
				Calendar calendar   = Calendar.getInstance();
				calendar.setTime(parse);
				calendar.add(calendar.DAY_OF_MONTH,1);
				criteria.andUpdateTimeLessThan(calendar.getTime());
			}
			int totalSize = brandDicGateway.countByCriteria(cruteria);
			if (totalSize>0){
				//总页数
				int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
				log.info("导出总页数：" + pageCount);
				ArrayList<List<HubBrandDicDto>> lists = new ArrayList<>();
				for(int i=1;i<=pageCount;i++){
					HubBrandDicCriteriaDto hubBrandDicCriteriaDto = new HubBrandDicCriteriaDto();
					HubBrandDicCriteriaDto.Criteria hubBrandDicCriteria = hubBrandDicCriteriaDto.createCriteria();
					if(StringUtils.isNotBlank(brandRequestDTO.getSupplierBrand())){
						hubBrandDicCriteria.andSupplierBrandLike("%"+brandRequestDTO.getSupplierBrand()+"%");
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getHubBrand())){
						hubBrandDicCriteria.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getStartTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
						hubBrandDicCriteria.andUpdateTimeGreaterThanOrEqualTo(parse);
					}
					if(StringUtils.isNotBlank(brandRequestDTO.getEndTime())){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date parse = format.parse(brandRequestDTO.getEndTime());
						Calendar calendar   = Calendar.getInstance();
						calendar.setTime(parse);
						calendar.add(calendar.DAY_OF_MONTH,1);
						hubBrandDicCriteria.andUpdateTimeLessThan(calendar.getTime());
					}
					hubBrandDicCriteriaDto.setPageNo(i);
					hubBrandDicCriteriaDto.setPageSize(PAGESIZE);
					//cruteria.setOrderByClause("update_time desc");


					List<HubBrandDicDto> hubBrandDicDtos = brandDicGateway.selectByCriteria(hubBrandDicCriteriaDto);
					lists.add(hubBrandDicDtos);


				}
				int j=0;
				for(List<HubBrandDicDto> list:lists){
					for(HubBrandDicDto hubBrandDicDto:list){
						j++;
						HSSFRow row1 = sheet.createRow(j);
						insertProductBrandOfRow2(row1,hubBrandDicDto,brandValueTemplate);
					}
				}

				saveAndUploadExcel(taskNo,"brand",wb);







			}


		}

		/*HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
		HubSupplierBrandDicCriteriaDto.Criteria  criteria=hubSupplierBrandDicCriteriaDto.createCriteria();

		hubSupplierBrandDicCriteriaDto.setPageSize(brandRequestDTO.getPageSize());
		hubSupplierBrandDicCriteriaDto.setPageNo(brandRequestDTO.getPageNo());
		if (brandRequestDTO.getSupplierBrand()!=null){
			criteria.andSupplierBrandLike("%"+ brandRequestDTO.getSupplierBrand() +"%");
		}
		if (brandRequestDTO.getHubBrand()!=null){
			criteria.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
		}
		if (brandRequestDTO.getSupplierId()!=null){
			criteria.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
		}
		*//*if (brandRequestDTO.getType()!=null){
			criteria.andPushStateEqualTo(Byte.parseByte(brandRequestDTO.getType()));
		}*//*

		if (Integer.parseInt(brandRequestDTO.getType())!=1){
			criteria.andPushStateIsNull();
		}
		if (brandRequestDTO.getStartTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
			criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
*//*
		hubSupplierBrandDicCriteriaDto.or(criteria.andPushStateIsNull());
*//*
		if (brandRequestDTO.getEndTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(brandRequestDTO.getEndTime());
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria.andUpdateTimeLessThan(calendar.getTime());
		}

		//求取总条数
		int totalSize = hubSupplierBrandDicGateWay.countByCriteria(hubSupplierBrandDicCriteriaDto);
		if (totalSize>0){
			int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
			log.info("导出总页数：" + pageCount);
			ArrayList<List<HubSupplierBrandDicDto>> lists = new ArrayList<>();
			for(int i=1;i<=pageCount;i++){
				HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto1 = new HubSupplierBrandDicCriteriaDto();
				HubSupplierBrandDicCriteriaDto.Criteria  criteria1=hubSupplierBrandDicCriteriaDto1.createCriteria();

				hubSupplierBrandDicCriteriaDto1.setPageNo(i);
				hubSupplierBrandDicCriteriaDto1.setPageSize(PAGESIZE);
				if (brandRequestDTO.getSupplierBrand()!=null){
					criteria1.andSupplierBrandLike("%"+ brandRequestDTO.getSupplierBrand() +"%");
				};
				if (brandRequestDTO.getSupplierId()!=null){
					criteria1.andSupplierIdEqualTo(brandRequestDTO.getSupplierId());
				}
				if (brandRequestDTO.getHubBrand()!=null){
					criteria1.andHubBrandNoEqualTo(brandRequestDTO.getHubBrand());
				}
				if (Integer.parseInt(brandRequestDTO.getType())!=1){
					criteria1.andPushStateIsNull();
				}
*//*
				hubSupplierBrandDicCriteriaDto1.or(criteria1.andPushStateIsNull());
*//*
				if (brandRequestDTO.getStartTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(brandRequestDTO.getStartTime()+" 00:00:00");
					criteria1.andUpdateTimeGreaterThanOrEqualTo(parse);
				}
				if (brandRequestDTO.getEndTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(brandRequestDTO.getEndTime());
					Calendar calendar   = Calendar.getInstance();
					calendar.setTime(parse);
					calendar.add(calendar.DAY_OF_MONTH,1);
					criteria1.andUpdateTimeLessThan(calendar.getTime());
				}
				List<HubSupplierBrandDicDto> hubSupplierBrandDicDtos = hubSupplierBrandDicGateWay.selectByCriteria(hubSupplierBrandDicCriteriaDto1);
				lists.add(hubSupplierBrandDicDtos);
			}
			int j=0;
			for(List<HubSupplierBrandDicDto> list:lists){
				for(HubSupplierBrandDicDto brandDto:list){
					j++;
					HSSFRow row1 = sheet.createRow(j);
					insertProductBrandOfRow(row1,brandDto,brandValueTemplate);
				}
			}
		}
		try {
			saveAndUploadExcel(taskNo,"brand",wb);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	//品牌行的插入
	private void insertProductBrandOfRow2(HSSFRow row,HubBrandDicDto brandDto, String[] rowTemplate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<? extends HubBrandDicDto> aClass = brandDto.getClass();
		Method fieldSetMet = null;
		Object value = null;
		for(int i=0;i<rowTemplate.length;i++){
			if ("supplierBrandDicId".equals(rowTemplate[i])){
				setBrandDicIdOfRow2(row,brandDto,aClass,i);
			}else if ("supplierBrand".equals(rowTemplate[i])){
				setsupplierBrand2(row,brandDto,aClass,i);
			}else if ("hubBrandNo".equals(rowTemplate[i])){
				sethubBrandNo2(row,brandDto,aClass,i);
			}/*else if ("supplierId".equals(rowTemplate[i])){
				setsupplierId(row,brandDto,aClass,i);
			}*/else if ("createTime".equals(rowTemplate[i])){
				setBrandcreateTime2(row,brandDto,aClass,i);
			}else if ("updateTime".equals(rowTemplate[i])){
				setBrandupdateTime2(row,brandDto,aClass,i);
			}else if ("hubBrand".equals(rowTemplate[i])){


				sethubBrand2(row,brandDto,aClass,i);
			}
			else {
				if ("updateUser".equals(rowTemplate[i])){
					setBrandupdateUser2(row,brandDto,aClass,i);
				}
			}
		}
	}


	private void insertProductBrandOfRow(HSSFRow row,HubSupplierBrandDicDto brandDto, String[] rowTemplate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<? extends HubSupplierBrandDicDto> aClass = brandDto.getClass();
		Method fieldSetMet = null;
		Object value = null;
		for(int i=0;i<rowTemplate.length;i++){
			if ("supplierBrandDicId".equals(rowTemplate[i])){
				setBrandDicIdOfRow(row,brandDto,aClass,i);
			}else if ("supplierBrand".equals(rowTemplate[i])){
				setsupplierBrand(row,brandDto,aClass,i);
			}else if ("hubBrandNo".equals(rowTemplate[i])){
				sethubBrandNo(row,brandDto,aClass,i);
			}else if ("supplierId".equals(rowTemplate[i])){
				setsupplierId(row,brandDto,aClass,i);
			}else if ("createTime".equals(rowTemplate[i])){
				setBrandcreateTime(row,brandDto,aClass,i);
			}else if ("updateTime".equals(rowTemplate[i])){
				setBrandupdateTime(row,brandDto,aClass,i);
			}else if ("hubBrand".equals(rowTemplate[i])){


                sethubBrand(row,brandDto,aClass,i);
			}
			else {
				if ("updateUser".equals(rowTemplate[i])){
					setBrandupdateUser(row,brandDto,aClass,i);
				}
			}
		}
	}


	//获取品牌
    private void sethubBrand(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(brandDto.getHubBrandNo()!=null){
            BrandDom brand = getGmsGateWay.findBrand(brandDto.getHubBrandNo());
            if (brand!=null && brand.getBrandEnName()!=null){
				row.createCell(i).setCellValue(brand.getBrandEnName());

			}
			else {

				row.createCell(i).setCellValue(" ");
			}

        }}
	private void sethubBrand2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		if(brandDto.getHubBrandNo()!=null){
			BrandDom brand = getGmsGateWay.findBrand(brandDto.getHubBrandNo());
			if (brand!=null && brand.getBrandEnName()!=null){
				row.createCell(i).setCellValue(brand.getBrandEnName());
			}
			else {
				row.createCell(i).setCellValue(" ");
			}
		}
	}



	private void setBrandDicIdOfRow(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierBrandDicId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setBrandDicIdOfRow2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getBrandDicId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}

	private void setsupplierId(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void sethubBrandNo(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getHubBrandNo";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void sethubBrandNo2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getHubBrandNo";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setsupplierBrand(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierBrand";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setsupplierBrand2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierBrand";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setBrandcreateTime(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Date value = (Date)fieldSetMet.invoke(brandDto);
		if (value==null)return;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = format.format(value);
	//	if (s==null)return;
		row.createCell(i).setCellValue(s);
	}
	private void setBrandcreateTime2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Date value = (Date)fieldSetMet.invoke(brandDto);
		if (value==null)return;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = format.format(value);
		//	if (s==null)return;
		row.createCell(i).setCellValue(s);
	}
	private void setBrandupdateTime(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setBrandupdateTime2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setBrandupdateUser(HSSFRow row, HubSupplierBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateUser";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setBrandupdateUser2(HSSFRow row, HubBrandDicDto brandDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateUser";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(brandDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}


	/**
	 *
	 * 字典材质的导出
	 */
	public void exportMaterial (String taskNo,MaterialRequestDto materialRequestDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("材质信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] materialTemplate = TaskImportTemplate2.getMaterialTemplate();
		for (int i = 0; i < materialTemplate .length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(materialTemplate[i]);
		}
		String[] materialValueTemplate = TaskImportTemplate2.getMaterialValueTemplate();

			//获取总页数
		HubMaterialMappingCriteriaDto hubMaterialMappingCriteriaDto1 =new HubMaterialMappingCriteriaDto() ;
		HubMaterialMappingCriteriaDto.Criteria  criteria=hubMaterialMappingCriteriaDto1.createCriteria();
		hubMaterialMappingCriteriaDto1.setPageNo(materialRequestDto.getPageNo());
		hubMaterialMappingCriteriaDto1.setPageSize(materialRequestDto.getPageSize());
		if (materialRequestDto.getSupplierMaterial()!=null){
			criteria.andSupplierMaterialLike("%"+ materialRequestDto.getSupplierMaterial() +"%");
		}
		if (materialRequestDto.getHubMaterial()!=null){
			criteria.andHubMaterialLike("%"+ materialRequestDto.getHubMaterial() +"%");
		}
		if (materialRequestDto.getStartTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(materialRequestDto.getStartTime()+" 00:00:00");
			criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
		if (materialRequestDto.getEndTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(materialRequestDto.getEndTime());
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria.andUpdateTimeLessThan(calendar.getTime());
		}
		if (materialRequestDto.getMappingLevel()!=null){
			criteria.andMappingLevelEqualTo(Byte.parseByte(materialRequestDto.getMappingLevel()));
		}
          //总条数的方法
		int totalSize = hubMaterialMappingGateWay.countByCriteria(hubMaterialMappingCriteriaDto1);

		if (totalSize>0){
			int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
			log.info("导出总页数：" + pageCount);
			ArrayList<List<HubMaterialMappingDto>> lists = new ArrayList<>();

			for (int i = 1; i<= pageCount; i++) {
				HubMaterialMappingCriteriaDto hubMaterialMappingCriteriaDto =new HubMaterialMappingCriteriaDto() ;
				HubMaterialMappingCriteriaDto.Criteria  Criteria1=hubMaterialMappingCriteriaDto.createCriteria();

				hubMaterialMappingCriteriaDto.setPageNo(i);
				hubMaterialMappingCriteriaDto.setPageSize(PAGESIZE);
				if (materialRequestDto.getHubMaterial()!=null){
					Criteria1.andHubMaterialLike("%"+ materialRequestDto.getHubMaterial() +"%");
				}
				if (materialRequestDto.getSupplierMaterial()!=null){
					Criteria1.andSupplierMaterialLike("%"+ materialRequestDto.getSupplierMaterial() +"%");
				}
				if (materialRequestDto.getMappingLevel()!=null){
					Criteria1.andMappingLevelEqualTo(Byte.parseByte(materialRequestDto.getMappingLevel()));
				}
				if (materialRequestDto.getStartTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(materialRequestDto.getStartTime()+" 00:00:00");
					Criteria1.andUpdateTimeGreaterThanOrEqualTo(parse);
				}
				if (materialRequestDto.getEndTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(materialRequestDto.getEndTime());
					Calendar calendar   = Calendar.getInstance();
					calendar.setTime(parse);
					calendar.add(calendar.DAY_OF_MONTH,1);
					Criteria1.andUpdateTimeLessThan(calendar.getTime());
				}
				List<HubMaterialMappingDto> hubMaterialMappingDtos = hubMaterialMappingGateWay.selectByCriteria(hubMaterialMappingCriteriaDto);
				lists.add(hubMaterialMappingDtos);
			}
			int j=0;
			for (List<HubMaterialMappingDto> list:lists){
				for(HubMaterialMappingDto hubMaterialMappingDto:list){
					j++;
					HSSFRow row1 = sheet.createRow(j);
					insertProductMaterialOfRow(row1, hubMaterialMappingDto, materialValueTemplate);
				}
			}
		}
		saveAndUploadExcel(taskNo,"material" ,wb);
	}

	/**
	 *
	 * 字典产地的导出
	 * @param taskNo
	 * @param hubSupplierMadeMappingDto
	 */
	public void exportMade(String taskNo,HubSupplierMadeMappingDto hubSupplierMadeMappingDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("产地信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] madeTemplate = TaskImportTemplate2.getMadeTemplate();
		for (int i = 0; i < madeTemplate.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(madeTemplate[i]);
		}
		 String[] madeValueTemplate = TaskImportTemplate2.getMadeValueTemplate();
		//获取总条数
		    HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto2 = new HubSupplierValueMappingCriteriaDto();
		HubSupplierValueMappingCriteriaDto.Criteria  criteria= hubSupplierValueMappingCriteriaDto2.createCriteria();
			if (hubSupplierMadeMappingDto.getSupplierVal()!=null){
				criteria.andSupplierValLike("%"+ hubSupplierMadeMappingDto.getSupplierVal() +"%");
			}

			if (hubSupplierMadeMappingDto.getHubVal()!=null){
				criteria.andHubValEqualTo(hubSupplierMadeMappingDto.getHubVal());
			}
			if (hubSupplierMadeMappingDto.getStartTime()!=null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = format.parse(hubSupplierMadeMappingDto.getStartTime()+" 00:00:00");
				criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
			}
		   if (hubSupplierMadeMappingDto.getEndTime()!=null){
			   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   Date parse = format.parse(hubSupplierMadeMappingDto.getEndTime());
			   Calendar calendar   = Calendar.getInstance();
			   calendar.setTime(parse);
			   calendar.add(calendar.DAY_OF_MONTH,1);
			   criteria.andUpdateTimeLessThan(calendar.getTime());
		   }
		       criteria.andHubValTypeEqualTo(hubSupplierMadeMappingDto.getType());
		    if (hubSupplierMadeMappingDto.getMappingType()!=null){
			   criteria.andMappingTypeEqualTo(Byte.parseByte(hubSupplierMadeMappingDto.getMappingType()));
		    }

		//获取总条数
			int totalSize = hubSupplierValueMappingGateWay.countByCriteria(hubSupplierValueMappingCriteriaDto2);

		if (totalSize>0){
			int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
			log.info("导出总页数：" + pageCount);
			ArrayList<List<HubSupplierValueMappingDto>> lists= new ArrayList<>();
			for (int i = 1; i <= pageCount; i++) {
				HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto = new HubSupplierValueMappingCriteriaDto();
				hubSupplierValueMappingCriteriaDto.setPageNo(i);
				hubSupplierValueMappingCriteriaDto.setPageSize(PAGESIZE);
				HubSupplierValueMappingCriteriaDto.Criteria  criteria2= hubSupplierValueMappingCriteriaDto.createCriteria();
				if (hubSupplierMadeMappingDto.getSupplierVal()!=null){
					criteria2.andSupplierValLike("%"+ hubSupplierMadeMappingDto.getSupplierVal() +"%");
				}
				if (hubSupplierMadeMappingDto.getHubVal()!=null){
					criteria2.andHubValEqualTo(hubSupplierMadeMappingDto.getHubVal());
				}
				if (hubSupplierMadeMappingDto.getStartTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(hubSupplierMadeMappingDto.getStartTime()+" 00:00:00");
					criteria2.andUpdateTimeGreaterThanOrEqualTo(parse);
				}
				if (hubSupplierMadeMappingDto.getEndTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(hubSupplierMadeMappingDto.getEndTime());
					Calendar calendar   = Calendar.getInstance();
					calendar.setTime(parse);
					calendar.add(calendar.DAY_OF_MONTH,1);
					criteria2.andUpdateTimeLessThan(calendar.getTime());
				}

				criteria2.andHubValTypeEqualTo(hubSupplierMadeMappingDto.getType());

				if (hubSupplierMadeMappingDto.getMappingType()!=null){
					criteria2.andMappingTypeEqualTo(Byte.parseByte(hubSupplierMadeMappingDto.getMappingType()));
				}

				List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = hubSupplierValueMappingGateWay.selectByCriteria(hubSupplierValueMappingCriteriaDto);
				lists.add(hubSupplierValueMappingDtos);

			}
			int j = 0;
			for (List<HubSupplierValueMappingDto> list :lists){
				for (HubSupplierValueMappingDto hubSupplierValueMappingDto:list){
					j++;
					HSSFRow row1 = sheet.createRow(j);
					insertProductMadeOfRow(row1,hubSupplierValueMappingDto,madeValueTemplate);

				}
			}
		}
		saveAndUploadExcel(taskNo,"origin",wb);

	}


	/**
	 * 字典品类导出
	 *
	 * @param taskNo
	 * @param categroyDicCriteriaDto
	 */
	public void exportCategroy(String taskNo,SupplierCategroyDicCriteriaDto categroyDicCriteriaDto) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("品类信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] categoryTemplate = TaskImportTemplate2.getCategoryTemplate();
		for (int i = 0; i < categoryTemplate.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(categoryTemplate[i]);
			cell.setCellStyle(style);
		}
		String[] categoryValueTemplate = TaskImportTemplate2.getCategoryValueTemplate();

		HubSupplierCategroyDicCriteriaDto hubSupplierCategroyDicCriteriaDto1 = new HubSupplierCategroyDicCriteriaDto();
		HubSupplierCategroyDicCriteriaDto.Criteria  criteria=hubSupplierCategroyDicCriteriaDto1.createCriteria();

		hubSupplierCategroyDicCriteriaDto1.setPageNo(categroyDicCriteriaDto.getPageNo());
		hubSupplierCategroyDicCriteriaDto1.setPageSize(categroyDicCriteriaDto.getPageSize());


		if(Integer.parseInt(categroyDicCriteriaDto.getSupplierCategoryType())!=0){
			if(Integer.parseInt(categroyDicCriteriaDto.getSupplierCategoryType())==5){
				criteria.andCategoryTypeIsNull();
			}else{
				criteria.andCategoryTypeEqualTo(Byte.parseByte(categroyDicCriteriaDto.getSupplierCategoryType()));
			}
		}
		if (categroyDicCriteriaDto.getSupplierId()!=null){
			criteria.andSupplierIdEqualTo(categroyDicCriteriaDto.getSupplierId());
		}
		if (categroyDicCriteriaDto.getSupplierGender()!=null){
			criteria.andSupplierGenderEqualTo(categroyDicCriteriaDto.getSupplierGender());
		}
		if (categroyDicCriteriaDto.getSupplierCategory()!=null){
			criteria.andSupplierCategoryLike("%"+categroyDicCriteriaDto.getSupplierCategory()+"%");
		}

		if (categroyDicCriteriaDto.getStartTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(categroyDicCriteriaDto.getStartTime()+" 00:00:00");
			criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
		if (categroyDicCriteriaDto.getEndTime()!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(categroyDicCriteriaDto.getEndTime());
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria.andUpdateTimeLessThan(calendar.getTime());
		}
		//总条数
		int totalSize = hubSupplierCategroyDicGateWay.countByCriteria(hubSupplierCategroyDicCriteriaDto1);
		if (totalSize > 0) {
			int pageCount = getPageCount(totalSize, PAGESIZE);// 总页数
			log.info("导出总页数：" + pageCount);
			HubSupplierCategroyDicCriteriaDto suppliercategroyDicCriteriaDto=new HubSupplierCategroyDicCriteriaDto();
			HubSupplierCategroyDicCriteriaDto.Criteria criteria1=suppliercategroyDicCriteriaDto.createCriteria();
			HubSupplierCategroyDicCriteriaDto.Criteria criteria12=suppliercategroyDicCriteriaDto.createCriteria();
			ArrayList<List<HubSupplierCategroyDicDto>> lists = new ArrayList<>();
			for (int i = 1; i<= pageCount; i++) {
				suppliercategroyDicCriteriaDto.setPageNo(i);
				suppliercategroyDicCriteriaDto.setPageSize(PAGESIZE);
				String supplierId = categroyDicCriteriaDto.getSupplierId();
				if (supplierId!=null){
					criteria1.andSupplierIdEqualTo(categroyDicCriteriaDto.getSupplierId());
				}
				if (categroyDicCriteriaDto.getSupplierCategory()!=null){
					criteria1.andSupplierCategoryLike("%"+ categroyDicCriteriaDto.getSupplierCategory() +"%");
				}
				if(Integer.parseInt(categroyDicCriteriaDto.getSupplierCategoryType())!=0){
					if(Integer.parseInt(categroyDicCriteriaDto.getSupplierCategoryType())==5){
						criteria1.andCategoryTypeIsNull();
					}else{
						criteria1.andCategoryTypeEqualTo(Byte.parseByte(categroyDicCriteriaDto.getSupplierCategoryType()));
					}
				}
				if (categroyDicCriteriaDto.getSupplierGender()!=null){
					criteria1.andSupplierGenderEqualTo(categroyDicCriteriaDto.getSupplierGender());
				}
				if (categroyDicCriteriaDto.getStartTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(categroyDicCriteriaDto.getStartTime()+" 00:00:00");
					criteria1.andUpdateTimeGreaterThanOrEqualTo(parse);
				}
				if (categroyDicCriteriaDto.getEndTime()!=null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(categroyDicCriteriaDto.getEndTime());
					Calendar calendar   = Calendar.getInstance();
					calendar.setTime(parse);
					calendar.add(calendar.DAY_OF_MONTH,1);
					criteria1.andUpdateTimeLessThan(calendar.getTime());
				}

				List<HubSupplierCategroyDicDto>	 hubSupplierCategroyDicDtos = hubSupplierCategroyDicGateWay.selectByCriteria(suppliercategroyDicCriteriaDto);
				lists.add(hubSupplierCategroyDicDtos);
			}
			int j = 0;
			for (List<HubSupplierCategroyDicDto> list:lists){
				for (HubSupplierCategroyDicDto hubSupplierCategroyDicDto:list){
					try {
						j++;
						row = sheet.createRow(j);
						insertProductCategoryOfRow(row,hubSupplierCategroyDicDto,categoryValueTemplate);
					} catch (Exception e) {
						log.error("insertProductCategoryOfRow异常：" + e.getMessage(),e);
						j--;
						throw e;
					}
				}
			}
		}
		saveAndUploadExcel(taskNo,"category",wb);
	}

	/**
	 * 字典颜色导出
	 *
	 * @param taskNo
	 *            任务编号
	 * @param
	 */

	public void exportColor(String taskNo,HubColorDic hubColorDic) throws Exception {
		System.out.println("start----"+hubColorDic.getStartTime());
		System.out.println("end----"+hubColorDic.getEndTime());
		HSSFWorkbook wb = new HSSFWorkbook();
		/**
		 * 第一个sheet：产品信息
		 */
		HSSFSheet sheet = wb.createSheet("颜色信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] row0= TaskImportTemplate2.getColorTemplate();

		for (int i = 0; i < row0.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(row0[i]);
			cell.setCellStyle(style);
		}
		/**
		 * 第二个sheet：隐藏
		 */
		//ExcelDropdown.creatExcelHidePage(wb);
		String[] rowTemplate= TaskImportTemplate2.getColorValueTemplate();
		HubColorDicItemCriteriaDto hubColorDicItemCriteriaDto =new HubColorDicItemCriteriaDto();
		HubColorDicItemCriteriaDto.Criteria criteria1 =hubColorDicItemCriteriaDto.createCriteria();

		hubColorDicItemCriteriaDto.setPageNo(hubColorDic.getPageNo());
		hubColorDicItemCriteriaDto.setPageSize(hubColorDic.getPageSize());
		if (hubColorDic.getSupplierColorName()!=null){
			criteria1.andColorItemNameLike("%"+hubColorDic.getSupplierColorName()+"%");
		}
		if (hubColorDic.getHubColorName()!=null){
			HubColorDicCriteriaDto hubColorDicCriteriaDto =new HubColorDicCriteriaDto();
			HubColorDicCriteriaDto.Criteria c=hubColorDicCriteriaDto.createCriteria();
		      c.andColorNameEqualTo(hubColorDic.getHubColorName());
			List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(hubColorDicCriteriaDto);
			HubColorDicDto hubColorDicDto = hubColorDicDtos.get(0);
			System.out.println("hubColorDicDto.getColorDicId()"+hubColorDicDto.getColorDicId());
			criteria1.andColorDicIdEqualTo(hubColorDicDto.getColorDicId());

		}


		if (Integer.parseInt(hubColorDic.getType())!=1){
			criteria1.andPushStateIsNull();
		}
		System.out.println("byte"+hubColorDic.getType());

		if(!StringUtils.isEmpty(hubColorDic.getStartTime())){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(hubColorDic.getStartTime()+" 00:00:00");
			criteria1.andUpdateTimeGreaterThan(parse);
		}
		if(!StringUtils.isEmpty(hubColorDic.getEndTime())){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(hubColorDic.getEndTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria1.andUpdateTimeLessThan(calendar.getTime());
		}


		//查询总条数
		int totalSize= hubColorDicItemGateWay.countByCriteria(hubColorDicItemCriteriaDto);
		System.out.println("总条数"+totalSize);

		log.info("总记录数：" + totalSize);
		if (totalSize > 0) {
			int pageCount = getPageCount(totalSize, PAGESIZE);//总页数
			log.info("导出总页数：" + pageCount);
			//HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();

			HubColorDicItemCriteriaDto hubColorDicItemCriteriaDto1 =new HubColorDicItemCriteriaDto();
			HubColorDicItemCriteriaDto.Criteria criteria2 =hubColorDicItemCriteriaDto1.createCriteria();

			List<List<HubColorDicItemDto>> lists = new ArrayList<List<HubColorDicItemDto>>();
			for (int i = 1; i <= pageCount; i++) {
				hubColorDicItemCriteriaDto1.setPageNo(i);
				hubColorDicItemCriteriaDto1.setPageSize(PAGESIZE);
				if (hubColorDic.getSupplierColorName()!= null) {
					criteria2.andColorItemNameLike("%"+hubColorDic.getSupplierColorName()+"%");
				}
				if (Integer.parseInt(hubColorDic.getType())!=1){
					criteria2.andPushStateIsNull();
				}
				if (hubColorDic.getHubColorName()!=null){
					HubColorDicCriteriaDto hubColorDicCriteriaDto =new HubColorDicCriteriaDto();
					HubColorDicCriteriaDto.Criteria c=hubColorDicCriteriaDto.createCriteria();
					c.andColorNameEqualTo(hubColorDic.getHubColorName());
					List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(hubColorDicCriteriaDto);
					HubColorDicDto hubColorDicDto = hubColorDicDtos.get(0);
					System.out.println("hubColorDicDto.getColorDicId()"+hubColorDicDto.getColorDicId());
					criteria2.andColorDicIdEqualTo(hubColorDicDto.getColorDicId());
				}


				if(!org.springframework.util.StringUtils.isEmpty(hubColorDic.getStartTime())){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(hubColorDic.getStartTime()+" 00:00:00");
					criteria2.andUpdateTimeGreaterThan(parse);

				}
				if(!org.springframework.util.StringUtils.isEmpty(hubColorDic.getEndTime())){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date parse = format.parse(hubColorDic.getEndTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(parse);
					calendar.add(calendar.DAY_OF_MONTH,1);
					criteria2.andUpdateTimeLessThan(calendar.getTime());
				}

				List<HubColorDicItemDto> ColorDicItemDto = hubColorDicItemGateWay.selectByCriteria(hubColorDicItemCriteriaDto1);
				lists.add(ColorDicItemDto);
			}
			int j = 0;
			for (List<HubColorDicItemDto> list :lists){
				for (HubColorDicItemDto product:list){
					try {
						j++;
						row = sheet.createRow(j);
						insertProductColorOfRow(row, product, rowTemplate);
					} catch (Exception e) {
						log.error("insertProductColorOfRow异常：" + e.getMessage(), e);
						j--;
						throw e;
					}
				}
			}
		}
		saveAndUploadExcel(taskNo,"color",wb);
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
			file = new File(localPath +createUser + "_" + taskNo + ".xls");
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
		}/* finally {
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
		}*/
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
	 * 添加export 材质的每一行
	 * @param row
	 * @param hubMaterialMappingDto
	 * @param rowTemplate
	 */

	private void insertProductMaterialOfRow(HSSFRow row,HubMaterialMappingDto hubMaterialMappingDto, String[] rowTemplate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<?> cls =hubMaterialMappingDto.getClass();
		StringBuffer buffer = new StringBuffer();
		Method fieldSetMet = null;
		Object value = null;
		for (int i=0;i<rowTemplate.length;i++){
			if ("materialMappingId".equals(rowTemplate[i])){
				setRowOfmaterialMappingId(row,hubMaterialMappingDto,cls,i);
			}else  if ("hubMaterial".equals(rowTemplate[i])){
				setRowOfhubMaterial(row,hubMaterialMappingDto,cls,i);
			}else  if ("supplierMaterial".equals(rowTemplate[i])){
				setRowOfsupplierMaterial(row,hubMaterialMappingDto,cls,i);
			}
			else  if ("createTime".equals(rowTemplate[i])){
				setRowOfcreateTime(row,hubMaterialMappingDto,cls,i);
			}else  if ("updateTime".equals(rowTemplate[i])){
				setRowOfupdateTime(row,hubMaterialMappingDto,cls,i);
			}else  if ("updateUser".equals(rowTemplate[i])){
				setRowOfupdateUser(row,hubMaterialMappingDto,cls,i);

			}else {
                if("mappingLevel".equals(rowTemplate[i])){

					setRowOfmappingLevel(row,hubMaterialMappingDto,cls,i);


                	//row.createCell(i).setCellValue("");



				}
			}
		}
	}


	/**
	 *
	 * @param row
	 * @param hubMaterialMappingDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfmappingLevel(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getMappingLevel";
		Method fieldSetMet = clazz.getMethod(fileName);
		Byte value =(Byte)fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		if (value==1){
			row.createCell(i).setCellValue("全匹配");
		}
		if (value==2){
			row.createCell(i).setCellValue("词组匹配");
		}
		if (value==3){
			row.createCell(i).setCellValue("单词匹配");
		}
		if (value==4){
			row.createCell(i).setCellValue("替换匹配");
		}
	}
	//材质 set cell updateUser
	private void setRowOfupdateTime(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	private void setRowOfupdateUser(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateUser";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}

	//set 材质 cell createTime
	private void setRowOfcreateTime(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Date value = (Date)fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = format.format(value);
		row.createCell(i).setCellValue(s);
	}

	/**
	 * set cell supplierMaterial供应商材质
	 * @param row
	 * @param hubMaterialMappingDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfsupplierMaterial(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierMaterial";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	/**
	 * set cell HubMaterial尚品材质
	 * @param row
	 * @param hubMaterialMappingDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfhubMaterial(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getHubMaterial";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	/**
	 * set cell materialMappingId
	 * @param row
	 * @param hubMaterialMappingDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfmaterialMappingId(HSSFRow row, HubMaterialMappingDto hubMaterialMappingDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getMaterialMappingId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(hubMaterialMappingDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}




	/**
	 * set excel 产地每一行
	 * @param row
	 * @param madeDicDto
	 * @param rowTemplate
	 */
	private void insertProductMadeOfRow(HSSFRow row,HubSupplierValueMappingDto madeDicDto, String[] rowTemplate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<?> cls = madeDicDto.getClass();
		StringBuffer buffer = new StringBuffer();
		Method fieldSetMet = null;
		Object value = null;
		for (int i=0;i<rowTemplate.length;i++){
			if ("hubSupplierValMappingId".equals(rowTemplate[i])){
				sethubSupplierValMappingId(row,madeDicDto,cls,i);
			}else  if ("supplierVal".equals(rowTemplate[i])){
				setsupplierVal(row,madeDicDto,cls,i);
			}else if ("hubVal".equals(rowTemplate[i])){
				setHubVal(row,madeDicDto,cls,i);
			}else if ("createTime".equals(rowTemplate[i])){
				setRowOfCreateTime(row,madeDicDto,cls,i);
			}else  if ("updateTime".equals(rowTemplate[i])){
				setRowOfUpdateTime(row,madeDicDto,cls,i);
			}else {
				if ("updateUser".equals(rowTemplate[i])){
					setRowOfUpdateUser(row,madeDicDto,cls,i);
				}
			}
		}
	}

	/**
	 * set cell 修改人
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfUpdateUser(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateUser";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}

	/**
	 * set cell UpdateTime
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfUpdateTime(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getUpdateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	/**
	 * set cell 创建时间
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setRowOfCreateTime(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getCreateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Date value = (Date)fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = format.format(value);
		row.createCell(i).setCellValue(s);
	}

	/**
	 * set cell 尚品产地
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setHubVal(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getHubVal";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}

	/**
	 * set cell 供货商产地
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void setsupplierVal(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierVal";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}

	/**
	 *set cell HubSupplierValMappingId
	 * @param row
	 * @param madeDicDto
	 * @param clazz
	 * @param i
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void sethubSupplierValMappingId(HSSFRow row, HubSupplierValueMappingDto madeDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getHubSupplierValMappingId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(madeDicDto);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	/**
	 * 将字典品类信息插入Excel的一行
	 */
	private void insertProductCategoryOfRow(HSSFRow row,HubSupplierCategroyDicDto categroyDicDto, String[] rowTemplate) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Class<?> cls = categroyDicDto.getClass();
		StringBuffer buffer = new StringBuffer();
		Method fieldSetMet = null;
		Object value = null;
		for (int i = 0;i<rowTemplate.length;i++) {
         if ("supplierCategoryDicId".equals(rowTemplate[i])){
			 setcategroyDicItemId(row,categroyDicDto,cls,i);
		 }else  if ("supplierCategory".equals(rowTemplate[i])){
			 setsupplierCategoryName(row,categroyDicDto,cls,i);
		 }
		 else  if ("supplierGender".equals(rowTemplate[i])){
			 setsupplierGender(row,categroyDicDto,cls,i);
		 }
		 else  if ("mappingState".equals(rowTemplate[i])){
			 setmappingState(row,categroyDicDto,cls,i);
		 }
		 else  if ("categoryType".equals(rowTemplate[i])){
			 setcategoryType(row,categroyDicDto,cls,i);

		 }else  if ("hubCategoryNo".equals(rowTemplate[i])){
			 sethubCategoryNo(row,categroyDicDto,cls,i);
		 }else  if ("createTime".equals(rowTemplate[i])){
			 setcategroycreateTime(row,categroyDicDto, cls,i);
		 }
		 else  if ("updateTime".equals(rowTemplate[i])){
			 setcategroyupdateTime(row,categroyDicDto, cls,i);
		 }
		 else  if ("updateUser".equals(rowTemplate[i])){
			 setcategroyupdateUser(row,categroyDicDto,cls,i);
		 }
		 else  if ("supplierId".equals(rowTemplate[i])){
			 setcategroysupplierId(row,categroyDicDto,cls,i);
		 }
		 else  if ("supplierName".equals(rowTemplate[i])){
			 row.createCell(i).setCellValue("");
		 }
		 else  if ("genderDicId".equals(rowTemplate[i])){
			 setgenderDicId(row,categroyDicDto, cls,i);
		 }
		}
	}

	/**
	 * set cell GenderDicId
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void  setgenderDicId(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getGenderDicId";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * set cell  supplierId
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void  setcategroysupplierId(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getSupplierId";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null)return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * set cell  updateUser
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void  setcategroyupdateUser(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getUpdateUser";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 *set cell  updateTime
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void  setcategroyupdateTime(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getUpdateTime";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 *   createTime
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void setcategroycreateTime(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getCreateTime";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Date value = (Date)fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = format.format(value);
			row.createCell(i).setCellValue(s);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * CategoryNo
	 *
	 */
	private void sethubCategoryNo(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getHubCategoryNo";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 品类级别
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void setcategoryType(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getCategoryType";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 映射状态1 或2
	 *
	 */
	private void setmappingState(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getMappingState";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * set excel supplierGender
	 *
	 */
	private void setsupplierGender(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getSupplierGender";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * set excel CategoryName
	 *
	 */
	private void setsupplierCategoryName(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i){
		String fileName = "getSupplierCategory";
		try {
			Method fieldSetMet = clazz.getMethod(fileName);
			Object value = fieldSetMet.invoke(categroyDicDto);
			if (value==null) return;
			row.createCell(i).setCellValue(value.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 *set excel  CategroyDicid
	 * @param row
	 * @param categroyDicDto
	 * @param clazz
	 * @param i
	 */
	private void setcategroyDicItemId(HSSFRow row, HubSupplierCategroyDicDto categroyDicDto, Class<?> clazz, int i) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		String fileName = "getSupplierCategoryDicId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(categroyDicDto);
		if (value==null) return;
		row.createCell(i).setCellValue(value.toString());
	}

	/**
	 * 将字典颜色信息插入Excel的一行
	 *
	 * @param row
	 * @param product
	 * @param rowTemplate
	 *            导入模板
	 * @throws Exception
	 */
	private void insertProductColorOfRow(HSSFRow row, HubColorDicItemDto product, String[] rowTemplate)
			throws Exception {
		Class<?> cls = product.getClass();
		StringBuffer buffer = new StringBuffer();
		Method fieldSetMet = null;
		Object value = null;
		for (int i = 0; i < rowTemplate.length; i++) {
			if (rowTemplate[i].equals("colorItemName")){
				setRowOfcolor_item_name(row,product, cls, i);

			}
			else if ("colorDicItemId".equals(rowTemplate[i])) {
				setcolorDicItemId(row, product, cls, i);
			}
			else if ("createTime".equals(rowTemplate[i])) {
				setcreateTime(row, product, cls, i);
			}
			else if ("updateTime".equals(rowTemplate[i])) {
				setupdateTime(row, product, cls, i);
			}
			else if ("updateUser".equals(rowTemplate[i])) {
				setupdateUser(row, product, cls, i);
			}else {
				if ("colorDicId".equals(rowTemplate[i])){
					//row.createCell(i).setCellValue("");
					//sethubDicColor(row, product, cls, i);
					Long colorDicId = product.getColorDicId();
					System.out.println(colorDicId);
					if (colorDicId==null)return;
					HubColorDicCriteriaDto hubColorDicCriteriaDto =new HubColorDicCriteriaDto() ;
					HubColorDicCriteriaDto.Criteria criteria = hubColorDicCriteriaDto.createCriteria();
					criteria.andColorDicIdEqualTo(colorDicId);
					List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(hubColorDicCriteriaDto);
					for(HubColorDicDto hubColorDicDto:hubColorDicDtos){
                          if (hubColorDicDto.getColorDicId()==product.getColorDicId()){
							  hubColorDicDto.getColorName();
							  row.createCell(i).setCellValue(hubColorDicDto.getColorName());
						  }

					 }

					/*HubColorDicCriteriaDto hubColorDicCriteriaDto =new HubColorDicCriteriaDto() ;
					HubColorDicCriteriaDto.Criteria criteria=hubColorDicCriteriaDto.createCriteria();
					criteria.andColorDicIdEqualTo(colorDicId);

					List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(hubColorDicCriteriaDto);
					HubColorDicDto hubColorDicDto = hubColorDicDtos.get(0);
					hubColorDicDto.getColorName();
					row.createCell(i).setCellValue(hubColorDicDto.getColorName());
*/
				}
			}
		}
	}

	private void sethubDicColor(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		/*String fileName = "getColorDicId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		if (value==null)return;*/
		HubColorDicCriteriaDto criteria = new HubColorDicCriteriaDto();
		if (product.getColorDicId() != null) {
			criteria.createCriteria().andColorNameEqualTo(product.getColorDicId().toString());
		}
		List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(criteria);
		HubColorDicDto hubColorDicDto = hubColorDicDtos.get(0);
		hubColorDicDto.getColorName();
		if (hubColorDicDto.getColorName()!=null){
			row.createCell(i).setCellValue(hubColorDicDto.getColorName());
		}
	}
	/**
	 * set excel 修改人
	 *
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 */
	private void setupdateUser(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fileName = "getUpdateUser";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
	}
	/**
	 * set excel 修改时间
	 *
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 */
	private void setupdateTime(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fileName = "getUpdateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());

	}

	/**
	 * set excel 创建时间
	 *
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 */
	private void setcreateTime(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fileName = "getCreateTime";
		Method fieldSetMet = clazz.getMethod(fileName);
		Date value = (Date)fieldSetMet.invoke(product);
		if (value==null)return;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = format.format(value);
		row.createCell(i).setCellValue(s);
	}

	/**
	 *
	 * set excel 颜色 id 列
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void setcolorDicItemId(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fileName = "getColorDicItemId";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());

	}
	/**
	 * set excel 颜色 name 列
	 *
	 * @param row
	 * @param product
	 * @param clazz
	 * @param i
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void setRowOfcolor_item_name(HSSFRow row, HubColorDicItemDto product, Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fileName = "getColorItemName";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		if (value==null)return;
		row.createCell(i).setCellValue(value.toString());
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



	public static void main(String[] args) {
		String spSkuNo = "30790817005";
		String spSpuNo = spSkuNo.substring(0,spSkuNo.length()-3);
		System.out.println(spSpuNo);
	}

}
