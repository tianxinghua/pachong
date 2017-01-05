package com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.TaskImportTemplate;

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
public class PendingSkuImportService {

	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;

	@Autowired
	PengdingToHubGateWay pengdingToHubGateWay;
	
	private static String[] pendingSkuTemplate = null;
	static {
		pendingSkuTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}

	public void handMessage(ProductImportTask task) throws Exception {

		// 1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(), task.getTaskNo(), null);
		log.info("任务编号：" + task.getTaskNo() + "状态更新成正在处理");


		// 2、从ftp下载文件并校验模板
		XSSFSheet xssfSheet = taskService.checkExcel(task.getTaskFtpFilePath(),task.getTaskNo(),"sku");
		// 3、excel解析成java对象
		
		// 2、从ftp下载文件并解析成对象
		List<HubPendingProductImportDTO> listHubProduct = handleHubExcel(xssfSheet);

		// 3、公共类校验hub数据并把校验结果写入excel
		checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProduct);

		
	}

	// 校验数据以及保存到hub表
	private void checkAndsaveHubPendingProduct(String taskNo,
			List<HubPendingProductImportDTO> listHubProduct)  throws Exception {

		if (listHubProduct == null) {
			return;
		}
		
		//校验全通过为true
		boolean flag = true;
		
		HubPendingSkuCheckResult hubPendingSkuCheckResult = null;
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubPendingProductImportDTO product : listHubProduct) {
			map = new HashMap<String, String>();
			// 校验sku信息
			hubPendingSkuCheckResult = handlePendingSku(product);

			map.put("taskNo", taskNo);
			map.put("spuModel", product.getSpuModel());
			boolean isPassing = false;
			if (hubPendingSkuCheckResult.isPassing() == true) {
				log.info(taskNo + ":sku校验通过");
				// 校验spu信息
				HubPendingSpuCheckResult hubPendingSpuCheckResult = handlePendingSpu(product);
				if (hubPendingSpuCheckResult.isPassing() == true) {
					product.setSpuModel(hubPendingSpuCheckResult.getResult());
					log.info(taskNo + "spu校验通过");
					//更新pending并推送到hub
					isPassing = true;
					map.put("taskState", "校验成功");
					map.put("processInfo", "校验通过");
				} else {
					log.info(taskNo + "spu校验不通过");
					flag = false;
					map.put("taskState", "校验失败");
					map.put("processInfo", hubPendingSpuCheckResult.getResult());
				}
			} else {
				log.info(taskNo + "sku校验不通过");
				map.put("taskState", "校验失败");
				map.put("processInfo", hubPendingSkuCheckResult.getResult());
			}
			saveHubPendingProduct(product,isPassing);
			listMap.add(map);
		}
		
		// 4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		taskService.convertExcel(listMap,taskNo,flag);
	}

	private void saveHubPendingProduct(HubPendingProductImportDTO product,boolean isPassing) {

		SpuModelDto spuModelDto = null;
		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(product.getSupplierId(),
				product.getSupplierSkuNo());
		HubSkuPendingDto hubSkuPendingDto = convertHubPendingProduct2Sku(product);
		HubSpuPendingDto hubSpuPendingDto = convertHubPendingProduct2PendingSpu(product);
		if(hubSkuPendingTempDto!=null){
			log.info(hubSkuPendingTempDto.getSupplierId()+"sku存在"+"，更新操作");
			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
			hubSkuPendingDto.setUpdateTime(new Date());
			hubSkuPendingDto.setSkuState((byte)SkuState.INFO_IMPECCABLE.getIndex());
			hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
			
			hubSpuPendingDto.setUpdateTime(new Date());
			hubSpuPendingDto.setSpuPendingId(hubSkuPendingTempDto.getSpuPendingId());
			if(isPassing){
				hubSpuPendingDto.setSpuState((byte)SpuState.HANDLED.getIndex());	
			}else{
				hubSpuPendingDto.setSpuState((byte)SpuState.INFO_PECCABLE.getIndex());
			}
			hubSpuPendingGateWay.updateByPrimaryKeySelective(hubSpuPendingDto);
		}else{
			
			HubSpuPendingCriteriaDto HubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
			HubSpuPendingCriteriaDto.createCriteria().andSupplierSpuNoEqualTo(product.getSupplierSpuNo()).andSupplierIdEqualTo(product.getSupplierId());
			
			List<HubSpuPendingDto> listSpu = hubSpuPendingGateWay.selectByCriteria(HubSpuPendingCriteriaDto);
			
			if(isPassing){
				hubSpuPendingDto.setSpuState((byte)SpuState.HANDLED.getIndex());	
			}else{
				hubSpuPendingDto.setSpuState((byte)SpuState.INFO_PECCABLE.getIndex());
			}
			
			Long pendingSpuId = null;
			if(listSpu!=null&&listSpu.size()>0){
				log.info(product.getSupplierId()+"spu存在、sku不存在"+"，更新插入操作");
				pendingSpuId = listSpu.get(0).getSpuPendingId();
				hubSpuPendingDto.setSpuPendingId(pendingSpuId);
				hubSpuPendingDto.setUpdateTime(new Date());
				
				hubSpuPendingGateWay.updateByPrimaryKeySelective(hubSpuPendingDto);
			}else{
				log.info(product.getSupplierId()+"spu、sku不存在"+"，插入操作");
				hubSpuPendingDto.setCreateTime(new Date());
				hubSpuPendingDto.setSpuSeasonState((byte)1);
				pendingSpuId = hubSpuPendingGateWay.insert(hubSpuPendingDto);
			}
			
			
			hubSkuPendingDto.setSpuPendingId(pendingSpuId);
			hubSkuPendingDto.setCreateTime(new Date());
			hubSkuPendingDto.setDataState((byte)1);
			hubSkuPendingGateWay.insert(hubSkuPendingDto);
		}
		if(isPassing){
			spuModelDto = new SpuModelDto();
			spuModelDto.setBrandNo(product.getHubBrandNo());
			spuModelDto.setSpuModel(product.getSpuModel());
			pengdingToHubGateWay.auditPending(spuModelDto);
			log.info("pendingToHub推送参数:{}",spuModelDto);
		}
	}

	private HubPendingSkuCheckResult handlePendingSku(HubPendingProductImportDTO product) {
		HubSkuPendingDto HubPendingSkuDto = convertHubPendingProduct2PendingSku(product);
		return pendingSkuCheckGateWay.checkSku(HubPendingSkuDto);
	}

	private HubPendingSpuCheckResult handlePendingSpu(HubPendingProductImportDTO product) {
		// 校验spu信息
		HubSpuPendingDto HubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		return pendingSpuCheckGateWay.checkSpu(HubPendingSpuDto);

	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingProductImportDTO product) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear()+"_"+product.getSeasonName());
		return HubPendingSpuDto;
	}

	private HubSkuPendingDto convertHubPendingProduct2PendingSku(HubPendingProductImportDTO product) {
		HubSkuPendingDto HubPendingSkuDto = new HubSkuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSkuDto);
		return HubPendingSkuDto;
	}


	private HubSkuPendingDto findHubSkuPending(String supplierId, String supplierSkuNo) {

		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criteria);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private HubSkuPendingDto convertHubPendingProduct2Sku(HubPendingProductImportDTO product) {
		HubSkuPendingDto hubSkuPendingDto = new HubSkuPendingDto();
		BeanUtils.copyProperties(product, hubSkuPendingDto);
		return hubSkuPendingDto;
	}

	// 解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubExcel(XSSFSheet xssfSheet) throws Exception {

		if(xssfSheet==null){
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}

	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String[] hubValueTemplate = item.getHubProductTemplate();
				Class cls = item.getClass();
				  Field[] fields = cls.getDeclaredFields();  
				  int i=0;
			        for (Field field : fields) {  
			            try {  
			                String fieldSetName = parSetName(field.getName());  
			                @SuppressWarnings("unchecked")
							Method fieldSetMet = cls.getMethod(fieldSetName,  
			                        field.getType());  
			                if(!hubValueTemplate[i].equals(field.getName())){
			                	return null;
			                }
			                xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			                String value = xssfRow.getCell(i).toString();  
			                if (null != value && !"".equals(value)) {  
			                    String fieldType = field.getType().getSimpleName();  
			                    if ("String".equals(fieldType)) {  
			                        fieldSetMet.invoke(item, value);  
			                    } else if ("Integer".equals(fieldType)  
			                            || "int".equals(fieldType)) {  
			                        Integer intval = Integer.parseInt(value);  
			                        fieldSetMet.invoke(item, intval);  
			                    } else if ("Long".equalsIgnoreCase(fieldType)) {  
			                        Long temp = Long.parseLong(value);  
			                        fieldSetMet.invoke(item, temp);  
			                    } else if ("Double".equalsIgnoreCase(fieldType)) {  
			                        Double temp = Double.parseDouble(value);  
			                        fieldSetMet.invoke(item, temp);  
			                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {  
			                        Boolean temp = Boolean.parseBoolean(value);  
			                        fieldSetMet.invoke(item, temp);  
			                    }else if ("BigDecimal".equalsIgnoreCase(fieldType)) {  
			                    	BigDecimal temp =new BigDecimal(value);  
			                        fieldSetMet.invoke(item, temp);  
			                    }  else {  
			                        log.info("not supper type" + fieldType);  
			                    }  
			                }  
			            } catch (Exception e) {  
			                continue;  
			            }  
			            i++;
			        }  

			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}
   
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
    public static String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "set"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
	private static boolean checkFileTemplet(XSSFRow xssfRow) {

		boolean flag = true;
		for (int i = 0; i < pendingSkuTemplate.length; i++) {
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if (!pendingSkuTemplate[i].equals(fieldName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
