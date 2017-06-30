package com.shangpin.ephub.product.business.ui.studio.slot.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.SlotManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.IStudioSlotService;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PendingProductService </p>
 * <p>Description: 待处理页面Service实现类</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:17:57
 *
 */
@Service
@Slf4j
public class StudioSlotServiceImpl implements IStudioSlotService{
	
	@Autowired
	StudioSlotGateWay studioSlotGateWay;
	@Autowired
	StudioGateWay studioGateWay;
    @Autowired 
	private HubSpuImportTaskGateWay spuImportGateway;
    @Autowired 
    private TaskStreamSender tastSender;
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public HubResponse<?> exportStudio(SlotManageQuery slotManageQuery,TaskType taskType){
    	try {
			HubSpuImportTaskDto taskDto = saveTaskIntoMysql(slotManageQuery.getUserName(),taskType.getIndex());
			sendMessageToTask(taskDto.getTaskNo(),taskType.getIndex(),JsonUtil.serialize(slotManageQuery)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":studio_" + taskDto.getTaskNo()+".xls");
		} catch (Exception e) {
			log.error("导出spu失败，服务器发生错误:"+e.getMessage(),e);
			return HubResponse.errorResp("导出失败，服务器发生错误");
		}
    }
    
    /**
     * 将任务记录保存到数据库
     * @param createUser
     * @return
     */
    protected HubSpuImportTaskDto saveTaskIntoMysql(String createUser,int taskType){
    	HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
    	Date date = new Date();
		hubSpuTask.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		hubSpuTask.setTaskState((byte)TaskState.HANDLEING.getIndex());
		hubSpuTask.setCreateTime(date);
		hubSpuTask.setUpdateTime(date);
		hubSpuTask.setImportType((byte)taskType);
		hubSpuTask.setCreateUser(null != createUser ? createUser : ""); 
		hubSpuTask.setTaskFtpFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		hubSpuTask.setSysFileName(createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		hubSpuTask.setResultFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		Long spuImportTaskId = spuImportGateway.insert(hubSpuTask);
		hubSpuTask.setSpuImportTaskId(spuImportTaskId);
		return hubSpuTask;
    }
    
    /**
	 * 构造消息体，并发送消息队列
	 * @param taskNo
	 * @param type
	 * @param data
	 */
    protected void sendMessageToTask(String taskNo,int type,String data){
    	Task productImportTask = new Task();
    	productImportTask.setMessageId(UUID.randomUUID().toString());
    	productImportTask.setTaskNo(taskNo);
    	productImportTask.setMessageDate(DateTimeUtil.getTime(new Date())); 
    	productImportTask.setData(data);
    	productImportTask.setType(type);
    	tastSender.productExportTaskStream(productImportTask, null);
    }
}
