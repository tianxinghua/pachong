package com.shangpin.asynchronous.task.consumer.productimport.supplier.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.asynchronous.task.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.asynchronous.task.consumer.conf.rpc.RpcConf;
import com.shangpin.asynchronous.task.consumer.conf.rpc.SupplierApiProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.supplier.dto.CsvDTO;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingHandleGateWay;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import lombok.extern.slf4j.Slf4j;
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
public class SupplierDataImportService {

	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	HubPendingHandleGateWay hubPendingHandleGateWay;
	@Autowired
    ApiAddressProperties apiAddressProperties;
	@Autowired
    SupplierApiProperties supplierApiProperties;
	private Gson gson = new Gson();
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsss");
	/**
	 * 处理消息
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public String handMessage(Task task) throws Exception {

		// ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
//		String createUser = json.get("createUser").toString();
		task.setData(filePath);
		InputStream in = taskService.downFileFromFtp(task);
		
		//解析excel
		List<CsvDTO> listCsvDTO = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listCsvDTO = handleHubXlsExcel(in, task, "supplierData");
		} else if ("xlsx".equals(fileFormat)) {
			listCsvDTO = handleHubXlsxExcel(in, task, "supplierData");
		}
		if(listCsvDTO==null)
			return null;
		
		HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("SupplierData");  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  

        String [] temp = {"执行结果","gender","brand","category","spu","barCode","size","proName","marketPrice","salePrice","qty","made","desc","pics","detailLink","demo1",
				"demo2","demo3", "demo4", "demo5","demo6","demo7","demo8","demo9","demo10","demo11","demo12","demo13"};
        for(int i=0;i<temp.length;i++) {
        	HSSFCell cell = row.createCell(i);  
            cell.setCellValue(temp[i]);  
            cell.setCellStyle(style);  
        }
        int i = 1;
		for(CsvDTO csvDTO : listCsvDTO){
			String result = "SUCCESS";
			try {
				pushMessage(gson.toJson(csvDTO));
			} catch (Exception e) {
				log.info(e.getMessage());
				e.printStackTrace();
				result = e.getMessage();
			}
			HSSFRow rowNew = sheet.createRow(i);  
	        rowNew.createCell(0).setCellValue(result); 
	        for(int g =1;g<temp.length;g++){
	        		String fieldGetName = "get" + temp[g].toUpperCase().charAt(0)
							+ temp[i].substring(1);
	        		Class cls = csvDTO.getClass();
					Method getMethod = cls.getDeclaredMethod(fieldGetName);
					Object object = getMethod.invoke(csvDTO);
	        		rowNew.createCell(g).setCellValue(object.toString());
	        }
	        i++;
		}
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		 String uploadFtp = "";
	        try {
				wb.write(os);
				InputStream newIn = new ByteArrayInputStream(os.toByteArray());  
				FTPClientUtil.uploadNewFile("/hub" , format.format(new Date())+".xls", newIn);
				uploadFtp = "/hub/"+format.format(new Date())+".xls";
			} catch (Exception e) {
				e.printStackTrace();
			}
		return uploadFtp;
	}

	public void pushMessage(String json) throws Exception {
		SupplierProduct supp = new SupplierProduct();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		supp.setMessageType("json");
		supp.setSupplierName(supplierApiProperties.getSupplierName());
		supp.setSupplierId(supplierApiProperties.getSupplierId());
		supp.setSupplierNo(supplierApiProperties.getSupplierNo());
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		supp.setMessageId(str.replace("-", ""));
		supp.setMessageDate(sim.format(new Date()));
		supp.setData(json);
		RpcConf c = new RpcConf();
		RestTemplate restTemplate = c.restTemplate();
		JSONObject supplierDto = restTemplate
				.postForEntity(apiAddressProperties.getHubProductUrl(), supp, JSONObject.class).getBody();
		log.info(supp.getSupplierName() + "==" + supplierDto.toString());
	}
	

	// 校验数据以及上传excel执行结果
	private String checkSupplierData(String taskNo, List<CsvDTO> listCsvDTO,String createUser)
			throws Exception {

		if (listCsvDTO == null) {
			return null;
		}
		// 4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		return "";
	}

	// 解析excel转换为对象
	private List<CsvDTO> handleHubXlsxExcel(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<CsvDTO> listCsvDTO = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			CsvDTO product = convertSpuDTO(xssfRow);
			if (product != null) {
				listCsvDTO.add(product);
			}

		}
		return listCsvDTO;
	}

	// 解析excel转换为对象
	private List<CsvDTO> handleHubXlsExcel(InputStream in, Task task, String type)
			throws Exception {

		HSSFSheet hSSFSheet = taskService.checkXlsExcel(in, task, type);
		if (hSSFSheet == null) {
			return null;
		}
		List<CsvDTO> listSupplierData = new ArrayList<>();
		for (int rowNum = 1; rowNum <= hSSFSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hSSFSheet.getRow(rowNum);
			CsvDTO product = convertSpuDTO(hssfRow);
			listSupplierData.add(product);
		}
		return listSupplierData;
	}

	@SuppressWarnings("unchecked")
	private static CsvDTO convertSpuDTO(XSSFRow xssfRow) throws Exception{
		CsvDTO item = null;
		if (xssfRow != null) {
			try {
				item = new CsvDTO();
				String[] csvDTOTemplate = item.getCsvDTO();
				Class cls = item.getClass();
				for (int i=0;i<csvDTOTemplate.length;i++) {
					if(xssfRow.getCell(i)!=null){
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String fieldSetName = "set" + csvDTOTemplate[i].toUpperCase().charAt(0)
								+ csvDTOTemplate[i].substring(1);
						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
			
	}
		return item;
	}
	@SuppressWarnings("unchecked")
	private static CsvDTO convertSpuDTO(HSSFRow xssfRow) throws Exception{
		CsvDTO item = null;
		if (xssfRow != null) {
			try {
				item = new CsvDTO();
				String[] csvDTOTemplate = item.getCsvDTO();
				Class cls = item.getClass();
				
				for (int i=0;i<csvDTOTemplate.length;i++) {
					if(xssfRow.getCell(i)!=null){
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String fieldSetName = "set" + csvDTOTemplate[i].toUpperCase().charAt(0)
								+ csvDTOTemplate[i].substring(1);
						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}

	public static boolean checkSetMet(Method[] methods, String fieldSetMet) throws Exception{
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public static String parSetName(String fieldName) throws Exception{
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}
}
