package com.shangpin.ephub.product.business.ui.hub.selected.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectRequestWithPageDto;
import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.hub.gateway.HubWaitSelectGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.ui.hub.selected.service.HubSelectedService;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponse;
import com.shangpin.ephub.product.business.ui.hub.waitselected.vo.HubWaitSelectedResponseWithPage;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-selected")
@Slf4j
public class HubSelectedController {
	@Autowired
	HubWaitSelectGateWay HubWaitSelectGateWay;
	@Autowired
	HubSpuPicGateWay hubSpuPicGateWay;
	@Autowired
	HubSelectedService HubSelectedService;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
    @Autowired
    TaskStreamSender productImportTaskStreamSender;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectList(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			long startTime = System.currentTimeMillis();
			HubWaitSelectRequestDto hubWaitSelectRequest = new HubWaitSelectRequestDto();
			BeanUtils.copyProperties(dto, hubWaitSelectRequest);
			Long total = HubWaitSelectGateWay.count(hubWaitSelectRequest);
			log.info("已选品查询到总数："+total);
			if(total>0){
				int pageNo = dto.getPageNo();
				int pageSize = dto.getPageSize();
				dto.setPageNo(pageSize*(pageNo-1));
				dto.setPageSize(pageSize);
				log.info("已选品请求参数：{}",dto);
				List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
				
				List<HubWaitSelectedResponse> arr = new ArrayList<HubWaitSelectedResponse>();
				if(list==null||list.size()<=0){
					return HubResponse.successResp("列表页为空");
				}
				log.info("已选品查询耗时："+(System.currentTimeMillis()-startTime)+",总记录数："+list.size());
				for(HubWaitSelectResponseDto hubWaitSelectResponseDto:list){
					HubWaitSelectedResponse HubWaitSelectResponse = new HubWaitSelectedResponse();
					BeanUtils.copyProperties(hubWaitSelectResponseDto, HubWaitSelectResponse);
					if(StringUtils.isNotBlank(hubWaitSelectResponseDto.getSkuSizeType())){
						HubWaitSelectResponse.setSkuSize(hubWaitSelectResponseDto.getSkuSizeType()+":"+hubWaitSelectResponseDto.getSkuSize());	
					}else{
						HubWaitSelectResponse.setSkuSize(hubWaitSelectResponseDto.getSkuSize());
					}
					HubWaitSelectResponse.setOperateUser(hubWaitSelectResponseDto.getUpdateUser());
					HubWaitSelectResponse.setUpdateTime(DateTimeUtil.getTime(hubWaitSelectResponseDto.getUpdateTime()));
					arr.add(HubWaitSelectResponse);
				}
				HubWaitSelectedResponseWithPage HubWaitSelectedResponseWithPageDto = new HubWaitSelectedResponseWithPage();
				HubWaitSelectedResponseWithPageDto.setTotal(Integer.parseInt(String.valueOf(total)));
				HubWaitSelectedResponseWithPageDto.setList(arr);
				log.info("已选品查询总耗时："+(System.currentTimeMillis()-startTime));
				return HubResponse.successResp(HubWaitSelectedResponseWithPageDto);
			}else{
				return HubResponse.successResp("列表页为空");
			}
			
		} catch (Exception e) {
			log.error("已选品获取列表失败：{}",e);
		}
		return HubResponse.errorResp("获取列表失败");
    }
	/**
	 * 导出查询商品
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-product",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse exportProduct(@RequestBody HubWaitSelectRequestWithPageDto dto){
	        	
		try {
			
			log.info("导出已选品参数：{}",dto);
			HubSpuImportTaskDto task=saveTaskIntoMysql(dto.getCreateUser(),TaskType.EXPORT_HUB_SELECTED.getIndex());
			sendMessageToTask(task.getTaskNo(),TaskType.EXPORT_HUB_SELECTED.getIndex(),JsonUtil.serialize(dto));
			log.info("导出已选品参数");
			return HubResponse.successResp(task.getTaskNo());
		} catch (Exception e) {
			log.error("导出查询商品失败：{}",e);
		}
		return HubResponse.errorResp("导出异常");
    }
	
	/**
	 * 导出查询图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-select-pic",method = RequestMethod.POST)
    public HubResponse exportSelectPic(@RequestBody HubWaitSelectRequestWithPageDto dto,HttpServletResponse response){
	        	
		
		try {
			HubSpuImportTaskDto task=saveTaskIntoMysql(dto.getCreateUser(),TaskType.EXPORT_HUB_PIC.getIndex());
			sendMessageToTask(task.getTaskNo(),TaskType.EXPORT_HUB_PIC.getIndex(),JsonUtil.serialize(dto));
			return HubResponse.successResp(task.getTaskNo());
		} catch (Exception e) {
			log.error("导出查询商品失败：{}",e);
		}
		return HubResponse.errorResp("导出异常");
    }
	

	/**
	 * 导出查询图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-not-handle-pic",method = RequestMethod.POST)
    public HubResponse exportNotSelectPic(@RequestBody HubWaitSelectRequestWithPageDto dto,HttpServletResponse response){
	        	
		try {
			HubSpuImportTaskDto task=saveTaskIntoMysql(dto.getCreateUser(),TaskType.EXPORT_HUB_NOT_HANDLE_PIC.getIndex());
			sendMessageToTask(task.getTaskNo(),TaskType.EXPORT_HUB_NOT_HANDLE_PIC.getIndex(),JsonUtil.serialize(dto));
			return HubResponse.successResp(task.getTaskNo());
		} catch (Exception e) {
			log.error("导出查询商品失败：{}",e);
		}
		return HubResponse.errorResp("导出异常");
    }
	
	/**
	 * 导出勾选图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-check-pic",method = RequestMethod.POST)
    public HubResponse exportCheckPic(@RequestBody List<HubWaitSelectStateDto> dto,HttpServletResponse response){
	        	
		try {
			if(dto==null||dto.size()<=0){
				return HubResponse.errorResp("数信息为空");
			}
			HubSpuImportTaskDto task=saveTaskIntoMysql(dto.get(0).getCreateUser(),TaskType.EXPORT_HUB_CHECK_PIC.getIndex());
			sendMessageToTask(task.getTaskNo(),TaskType.EXPORT_HUB_CHECK_PIC.getIndex(),JsonUtil.serialize(dto));
			return HubResponse.successResp(task.getTaskNo());
		} catch (Exception e) {
			log.error("导出图片失败：{}",e);
		}
		return HubResponse.errorResp("导出异常");
    }
	/**
	 * 导出勾选图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-check-pic1",method = RequestMethod.POST)
    public void exportCheckPic1(@RequestBody List<HubWaitSelectStateDto> dto,HttpServletResponse response){
	        	
		try {
			log.info("导出勾选图片请求参数：{}",dto);
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-Disposition", "attachment;filename="+"selected_product_" + System.currentTimeMillis()+".xls");    
			OutputStream ouputStream = response.getOutputStream();
			HubSelectedService.exportSelectPicExcel(dto,ouputStream);
		} catch (Exception e) {
			log.error("导出勾选图片失败：{}",e);
		}
    }
	 private void sendMessageToTask(String taskNo,int type,String data){
	    	Task productImportTask = new Task();
	    	productImportTask.setMessageId(UUID.randomUUID().toString());
	    	productImportTask.setTaskNo(taskNo);
	    	productImportTask.setMessageDate(DateTimeUtil.getTime(new Date())); 
	    	productImportTask.setData(data);
	    	productImportTask.setType(type);
	    	productImportTaskStreamSender.productExportTaskStream(productImportTask, null);
	 }
		
	  private HubSpuImportTaskDto saveTaskIntoMysql(String createUser,int taskType){
	    	HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
	    	Date date = new Date();
			hubSpuTask.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
			hubSpuTask.setTaskState((byte)TaskState.HANDLEING.getIndex());
			hubSpuTask.setCreateTime(date);
			hubSpuTask.setUpdateTime(date);
			hubSpuTask.setImportType((byte)taskType);
			hubSpuTask.setCreateUser(createUser); 
			hubSpuTask.setTaskFtpFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
			hubSpuTask.setSysFileName(createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
			hubSpuTask.setResultFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
			Long spuImportTaskId = spuImportGateway.insert(hubSpuTask);
			log.info("====导出已选品任务保存入库："+spuImportTaskId);
			hubSpuTask.setSpuImportTaskId(spuImportTaskId);
			return hubSpuTask;
	    }
   @SuppressWarnings("unused")
private HubSpuImportTaskDto saveTaskIntoMysql(int taskType){
   	HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
   	Date date = new Date();
		hubSpuTask.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		hubSpuTask.setTaskState((byte)TaskState.HANDLEING.getIndex());
		hubSpuTask.setCreateTime(date);
		hubSpuTask.setUpdateTime(date);
		hubSpuTask.setImportType((byte)taskType);
		Long spuImportTaskId = spuImportGateway.insert(hubSpuTask);
		hubSpuTask.setSpuImportTaskId(spuImportTaskId);
		return hubSpuTask;
   }
	@RequestMapping(value = "/export-product1",method ={RequestMethod.POST,RequestMethod.GET})
   public void exportProduct(@RequestBody HubWaitSelectRequestWithPageDto dto,HttpServletResponse response){
	        	
		try {
			long startTime  = System.currentTimeMillis();
			dto.setPageNo(0);
			dto.setPageSize(100000);
			log.info("导出查询商品请求参数：{}",dto);
			List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
			log.info("导出查询商品耗时："+(System.currentTimeMillis()-startTime));
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-Disposition", "attachment;filename="+"selected_product_" + System.currentTimeMillis()+".xls");    
			OutputStream ouputStream = response.getOutputStream();
			if(list==null||list.size()<=0){
				return ;
			}
			HubSelectedService.exportExcel(list,ouputStream);
			
		} catch (Exception e) {
			log.error("导出查询商品失败：{}",e);
		}
		
   }
	/**
	 * 导出查询图片
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/export-select-pic1",method = RequestMethod.POST)
    public void exportSelectPic1(@RequestBody HubWaitSelectRequestWithPageDto dto,HttpServletResponse response){
	        	
		try {
			dto.setPageNo(0);
			dto.setPageSize(100000);
			log.info("导出查询图片请求参数：{}",dto);
			List<HubWaitSelectResponseDto> list = HubWaitSelectGateWay.selectByPage(dto);
			log.info("导出查询图片请求返回结果======{}",list.toString());
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-Disposition", "attachment;filename="+"selected_product_" + System.currentTimeMillis()+".xls");    
			OutputStream ouputStream = response.getOutputStream();
			if(list==null||list.size()<=0){
				return ;
			}
			HubSelectedService.exportPicExcel(list,ouputStream);
		} catch (Exception e) {
			log.error("导出查询图片获取失败：{}",e);
		}
		
		log.info("===============导出查询图片请求结束===========");
    }
}
