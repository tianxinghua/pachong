
package com.shangpin.iog.productmanager.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import lombok.Delegate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.CsvAttributeInfoDTO;
import com.shangpin.iog.dto.CsvSupplierInfoDTO;
import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.strategy.sepStrategy.ISepStrategy;
import com.shangpin.iog.generater.strategy.sepStrategy.SepStrategyContext;
import com.shangpin.iog.generater.util.Csv2DTO;
import com.shangpin.iog.generater.util.DataListToMap;
import com.shangpin.iog.generater.util.Xml2DTO;
import com.shangpin.iog.productmanager.AbsSaveProductImpl;
import com.shangpin.iog.productmanager.StartUp;
import com.shangpin.iog.service.CsvSupplierService;

@Component("taskController")
public class TaskController {
	
	
//	private static TaskController taskController  = new TaskController();
//	private TaskController(){};
//	public static TaskController getTaskController(){
//		return taskController;
//	}
	
//	private  Map<String,Thread> recordMap = new HashMap<String, Thread>();
	private  Map<String,ScheduledFuture> recordMap = new HashMap<String, ScheduledFuture>();
	@Autowired
	CsvSupplierService csvSupplierService;
	
	/**
	 * 检查任务
	 * @param suppliers
	 */
	public void checkTask(List<CsvSupplierInfoDTO> suppliers){
		for(CsvSupplierInfoDTO supplierDTO : suppliers){
			try {
				
				if(!recordMap.containsKey(supplierDTO.getSupplierId())){
					supplierDTO.setState("3");
					csvSupplierService.updateCsvSupplierInfo(supplierDTO);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 启动任务
	 * @param suppliers
	 */
	public void startTask(List<CsvSupplierInfoDTO> suppliers){
		
		for(CsvSupplierInfoDTO supplierDTO : suppliers){
			
			try {
				
				excuteTask(supplierDTO);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 关闭任务
	 * @param suppliers
	 */
	public void stopTask(List<CsvSupplierInfoDTO> suppliers){
		for(CsvSupplierInfoDTO csvSupplierInfoDTO : suppliers){	
			try {
				if (recordMap.containsKey(csvSupplierInfoDTO.getSupplierId())){
					System.out.println("停止++++++++++++++++");
					recordMap.get(csvSupplierInfoDTO.getSupplierId()).cancel(true);
					recordMap.remove(csvSupplierInfoDTO.getSupplierId());
					csvSupplierInfoDTO.setState("3"); 
					csvSupplierService.updateCsvSupplierInfo(csvSupplierInfoDTO);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
	}
	
	//public void resetTask(Map<String, CsvSupplierInfoDTO> changedMap){
//		System.out.println(recordMap.toString());
//		System.out.println("================================");
//		for (Entry<String, CsvSupplierInfoDTO> entry : changedMap.entrySet()) {
//			// 判断状态,如果停止 关闭相应线程
//			if (recordMap.containsKey(entry.getKey())) {
//				if (entry.getValue().getState().equals(TaskState.STOP)) {
//					System.out.println("停止++++++++++++++++");
//					recordMap.get(entry.getKey()).cancel(true);
//					recordMap.remove(entry.getKey());
//				}
//			}else{
//				if (entry.getValue().getState().equals(TaskState.START)) {
//					System.out.println("开始++++++++++++++++");
//					excuteTask(entry.getValue());
//				}
//				
//			}
//		}
	//}
	//TODO 
//	private Worker getWorker(String supplierId){
//		Worker worker = new Worker();
//		worker.setSupplierId(supplierId);
//		worker.setRecordMap(recordMap);
//		return worker;
//	}
	
	private void excuteTask(final CsvSupplierInfoDTO csvSupplier){
		
		if(StringUtils.isBlank(csvSupplier.getCrontime())){
			System.out.println("任务的定时时间为空，创建任务失败。。。");
			return;
		}
		
		final Task task = new Task();
		task.setCronExpression(csvSupplier.getCrontime());
		Trigger trigger = task.getTrigger();
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(2);
		scheduler.initialize();
		System.out.println(csvSupplier.getSupplierId() +"开始运行"+new Date().toLocaleString());
		ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
			public void run() {
				try {
					
					Map<String, Object> map = null;
					
					System.out.println(csvSupplier.getSupplierId() +"运行中"+new Date().toLocaleString());
					List<CsvAttributeInfoDTO> attributes = csvSupplierService.findCsvAttributeBySupplierId(csvSupplier.getSupplierId());
					
					if("csv".equals(csvSupplier.getDataType())){
						ProductDTO attriValueClzz = new ProductDTO();
						for(CsvAttributeInfoDTO attribute : attributes){
							java.lang.reflect.Field field = attriValueClzz.getClass().getDeclaredField(attribute.getAttriName());
							field.setAccessible(true);
							field.set(attriValueClzz, attribute.getAttriValue());
						}
						ProductDTO attriRuleClzz = new ProductDTO();
						for(CsvAttributeInfoDTO attribute : attributes){
							java.lang.reflect.Field field = attriValueClzz.getClass().getDeclaredField(attribute.getAttriName());
							field.setAccessible(true);
							field.set(attriRuleClzz, attribute.getAttriRule());
						}
						
						String[] needColsNo = new String[]{attriValueClzz.getSizeandstock(),attriValueClzz.getSpuId(),
								attriValueClzz.getSkuId(),attriValueClzz.getGender(),attriValueClzz.getCategory(),
								attriValueClzz.getBrand(),attriValueClzz.getSeason(),attriValueClzz.getPicurl(),
								attriValueClzz.getOrigin(),attriValueClzz.getMaterial(),attriValueClzz.getProductName(),
								attriValueClzz.getMarketPrice(),attriValueClzz.getSalePrice(),attriValueClzz.getSupplierPrice(),
								attriValueClzz.getBarcode(),attriValueClzz.getColor(),attriValueClzz.getCurrency(),
								attriValueClzz.getSize(),attriValueClzz.getStock(),attriValueClzz.getProductCode(),
								attriValueClzz.getDescription()};//new String[]{"","0","2","14","22","3","","9,10,11,12,8","","16","4","","23","","","15","23","19","20","1","5"};
						//策略组
						String[] strategys = new String[]{attriRuleClzz.getSizeandstock(),attriRuleClzz.getSpuId(),
								attriRuleClzz.getSkuId(),attriRuleClzz.getGender(),attriRuleClzz.getCategory(),
								attriRuleClzz.getBrand(),attriRuleClzz.getSeason(),attriRuleClzz.getPicurl(),
								attriRuleClzz.getOrigin(),attriRuleClzz.getMaterial(),attriRuleClzz.getProductName(),
								attriRuleClzz.getMarketPrice(),attriRuleClzz.getSalePrice(),attriRuleClzz.getSupplierPrice(),
								attriRuleClzz.getBarcode(),attriRuleClzz.getColor(),attriRuleClzz.getCurrency(),
								attriRuleClzz.getSize(),attriRuleClzz.getStock(),attriRuleClzz.getProductCode(),
								attriRuleClzz.getDescription()};//new String[]{"","","","","","","","more% %0%;","","","","","sin% %0%\"\"","","","","sin% %0%\"\"","","","",""};
						
						//csv
						ISepStrategy[] iSepStrategies = new SepStrategyContext().operate(strategys);
						List<ProductDTO> productList = new Csv2DTO().toDTO(csvSupplier.getFetchUrl(), csvSupplier.getFilePath(), csvSupplier.getSep(),
								needColsNo, iSepStrategies, ProductDTO.class);
						map = DataListToMap.toMap(csvSupplier.getToMapCondition(), productList,csvSupplier.getSupplierId(), strategys);						
												
					}else if("xml".equals(csvSupplier.getDataType())){
						//TODO 
//						Map<String,String> tagNameMap = new HashMap<String,String>();
//						for(CsvAttributeInfoDTO attribute : attributes){
//							tagNameMap.put(attribute.getAttriValue(), attribute.getAttriName());
//						}
//						List<ProductDTO> productList = Xml2DTO.toDTO(csvSupplier.getFilePath(), csvSupplier.getXmlSkuTag(), csvSupplier.getXmlSpuTag(), tagNameMap);
						
					}else if("json".equals(csvSupplier.getDataType())){
						//TODO 
					}
					
					if(null != map && map.size()>0){
						System.out.println("====================开始保存信息======================");
						AbsSaveProductImpl abs = (AbsSaveProductImpl)StartUp.getApplicationContext().getBean("abssaveproduct");	
						abs.handleData(csvSupplier.getPicFlag(), csvSupplier.getSupplierId(), 90, csvSupplier.getPicPath(),map);
						//将执行成功的供货商状态置成2
						csvSupplier.setState("2"); 
						csvSupplierService.updateCsvSupplierInfo(csvSupplier);
					}						
										
				} catch (Exception e) {
					e.printStackTrace();
					//将执行失败的供货商发邮件
					try {
						
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		}, trigger);
		recordMap.put(csvSupplier.getSupplierId(), future);
	}
	
	public void transCSVInfoToProDTO(List<CsvAttributeInfoDTO> csvList,ProductDTO pro) throws Exception{
		Class<? extends ProductDTO> clz = pro.getClass();
		for (CsvAttributeInfoDTO csv : csvList) {
			java.lang.reflect.Field field = clz.getDeclaredField(csv.getAttriName());
		field.setAccessible(true);
		field.set(pro, csv.getAttriValue());
		}
		System.out.println(pro.toString());
	}
	
}

