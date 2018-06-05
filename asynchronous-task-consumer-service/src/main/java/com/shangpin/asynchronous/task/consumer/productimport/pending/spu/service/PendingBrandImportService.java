package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubBrandImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubMadeImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubNohandleReasonGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingHandleGateWay;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierBrandDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierSizeDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.gateway.DicRefreshGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.TaskImportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 任务队列消费
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
@Slf4j
public class PendingBrandImportService {
	

	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubNohandleReasonGateWay nohandleGateWay;
	@Autowired
	HubPendingHandleGateWay pendingHandleGateWay;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	HubSupplierBrandDicGateWay hubSupplierBrandDicGateWay;
	@Autowired
	DicRefreshGateWay dicRefreshGateWay;
    @Autowired
	HubBrandDicGateway hubBrandDicGateway;
	private static String[] brandValueTemplate=null;
	static {
		brandValueTemplate = TaskImportTemplate2.getBrandValueTemplate();
	}

	public String handMessage(Task task) throws Exception {
		
		//从ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		task.setData(filePath);
		
		InputStream in = taskService.downFileFromFtp(task);
		
		//excel转对象
		List<HubBrandImportDTO> hubBrandImportDTO = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			hubBrandImportDTO = handlePendingBrandXls(in, task,"brand");
		} else if ("xlsx".equals(fileFormat)) {
			hubBrandImportDTO = handlePendingBrandXlsx(in, task, "brand");
		}

		//校验数据并把校验结果写入excel
		return checkAndsaveHubPendingProduct(task.getTaskNo(), hubBrandImportDTO,createUser);
	}
	
	//开始校验数据
	public String checkAndsaveHubPendingProduct(String taskNo, List<HubBrandImportDTO> hubBrandImportDTO,String createUser)
			throws Exception {
		
		if (hubBrandImportDTO == null) {
			return null;
		}
		
		//记录单条数据的校验结果

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	
		for (HubBrandImportDTO productImport : hubBrandImportDTO) {
			if (productImport == null ) {
				continue;
			}
			Map<String, String>	map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			//首先判断是否人工排除
			filterBrand(productImport,createUser,map);
			listMap.add(map);
		}
		// 处理的结果以excel文件上传ftp，并更新任务表的任务状态和结果文件在ftp的路径
		return taskService.convertExcelBrand(listMap, taskNo);
	}
	private Map<String, String> filterBrand(HubBrandImportDTO productImport,String createUser,Map<String, String> map) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (productImport.getSupplierBrandDicId()!=null){
        	//数据库先查，后面比较刷新
			HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto =new HubSupplierBrandDicCriteriaDto() ;
			 hubSupplierBrandDicCriteriaDto.createCriteria().andSupplierBrandDicIdEqualTo(Long.parseLong(productImport.getSupplierBrandDicId()));

			List<HubSupplierBrandDicDto> list = hubSupplierBrandDicGateWay.selectByCriteria(hubSupplierBrandDicCriteriaDto);
			HubSupplierBrandDicDto hubSupplierBrandDicDto1 = list.get(0);
			HubSupplierBrandDicDto hubSupplierBrandDicDto = new HubSupplierBrandDicDto();
			hubSupplierBrandDicDto.setSupplierBrandDicId(Long.parseLong(productImport.getSupplierBrandDicId()));
			if (productImport.getSupplierBrand()!=null){
				hubSupplierBrandDicDto.setSupplierBrand(productImport.getSupplierBrand());
				map.put("supplierBrand",productImport.getSupplierBrand());
			}if (productImport.getHubBrandNo()!=null){
				hubSupplierBrandDicDto.setHubBrandNo(productImport.getHubBrandNo());
				map.put("hubBrandNo",productImport.getHubBrandNo());
			}
			hubSupplierBrandDicDto.setUpdateTime(new Date());
			int i = hubSupplierBrandDicGateWay.updateByPrimaryKeySelective(hubSupplierBrandDicDto);
			if (i==1){
				map.put("task","修改品牌数据成功");
			}else {
				map.put("task","校验失败");
			}
			    //对比修改前后的品牌类型是否相同
				if (productImport.getHubBrandNo()!=null){
					if (hubSupplierBrandDicDto1.getHubBrandNo()==null || !hubSupplierBrandDicDto1.getHubBrandNo().equals(productImport.getHubBrandNo())){
						HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto = new HubSupplierBrandDicRequestDto();
						hubSupplierBrandDicRequestDto.setSupplierBrandDicId(Long.parseLong(productImport.getSupplierBrandDicId()));
						hubSupplierBrandDicRequestDto.setHubBrandNo(productImport.getHubBrandNo());
						if (productImport.getSupplierBrand()!=null){
							hubSupplierBrandDicRequestDto.setSupplierBrand(productImport.getSupplierBrand());
						}
						dicRefreshGateWay.brandRefresh(hubSupplierBrandDicRequestDto);
					}
				}

			return map;
		}else {
			HubSupplierBrandDicDto hubSupplierBrandDicDto =new HubSupplierBrandDicDto() ;
			if (productImport.getSupplierBrand()!=null){
				hubSupplierBrandDicDto.setSupplierBrand(productImport.getSupplierBrand());
				map.put("supplierBrand",productImport.getSupplierBrand());
			}if (productImport.getHubBrandNo()!=null){
				hubSupplierBrandDicDto.setHubBrandNo(productImport.getHubBrandNo());
				map.put("hubBrandNo",productImport.getHubBrandNo());
			}
			hubSupplierBrandDicDto.setCreateTime(new Date());
			hubSupplierBrandDicGateWay.insert(hubSupplierBrandDicDto);
			map.put("task","添加品牌数据成功");
			HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto = new HubSupplierBrandDicRequestDto();
			if (productImport.getSupplierBrand()!=null){
				hubSupplierBrandDicRequestDto.setSupplierBrand(productImport.getSupplierBrand());
			}if (productImport.getHubBrandNo()!=null){
				hubSupplierBrandDicRequestDto.setHubBrandNo(productImport.getHubBrandNo());
			}
			dicRefreshGateWay.brandRefresh(hubSupplierBrandDicRequestDto);

			return map;
		}

	}
	

	



	private List<HubBrandImportDTO> handlePendingBrandXls(InputStream in, Task task, String type)
			throws Exception {
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubBrandImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubBrandImportDTO product = convertBrandDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}
		}
		return listHubProduct;
	}
	




	private static HubBrandImportDTO convertBrandDTO(HSSFRow xssfRow) throws Exception{
		HubBrandImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubBrandImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < brandValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + brandValueTemplate[i].toUpperCase().charAt(0)
								+ brandValueTemplate[i].substring(1);
						Method setMethod = c.getDeclaredMethod(setMethodName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}
			} catch (Exception ex) {
				ex.getStackTrace();
				throw ex;
			}
		}
		return item;
	}
	private List<HubBrandImportDTO> handlePendingBrandXlsx(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubBrandImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubBrandImportDTO product = convertBrandDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}
	private static HubBrandImportDTO convertBrandDTO(XSSFRow xssfRow)  throws Exception{
		HubBrandImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubBrandImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < brandValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + brandValueTemplate[i].toUpperCase().charAt(0)
								+ brandValueTemplate[i].substring(1);
						Method setMethod = c.getDeclaredMethod(setMethodName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}
			} catch (Exception ex) {
				ex.getStackTrace();
				throw ex;
			}
		}
		return item;
	}

}
