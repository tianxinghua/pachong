package com.shangpin.ephub.product.business.ui.task.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.ui.task.common.util.FTPClientUtil;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseDTO;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseWithPageDTO;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: task
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
public class TaskImportService {
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    HubSpuImportTaskGateWay spuImportGateway;
    @Autowired
    ProductImportTaskStreamSender productImportTaskStreamSender;
    public HubResponse uploadFileAndSave(HubImportTaskRequestDto task,TaskImportTpye importType) throws Exception{

    	String name  = new String(task.getFileName().getBytes("UTF-8"));
        String []fileName = name.split("\\.");
        if(fileName!=null&&fileName.length==2){
            if("xlsx".equals(fileName[1])||"xls".equals(fileName[1])){

                SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                Date date = new Date();
                String taskNo = sim.format(date);
                String systemFileName = taskNo +"."+fileName[1];
                //第一步 ： 上传ftp
                String ftpPath = FTPClientUtil.uploadFile(task.getUploadfile(),systemFileName);
                //第二步 ： 保存数据库
                saveTask(task,taskNo,ftpPath,systemFileName,importType.getIndex());
                //TODO 第三步 ：发送到hub消息队列
                sendTaskMessage(taskNo,ftpPath+systemFileName,importType);
                return HubResponse.successResp(null);
            }
        }
        log.info("上传文件为"+task.getFileName()+"，格式有误，请下载模板");
        return HubResponse.errorResp("文件格式有误，请下载模板");
    }
    private void sendTaskMessage(String taskNo,String ftpFilePath,TaskImportTpye importType){
        ProductImportTask productImportTask = new ProductImportTask();
        productImportTask.setMessageDate(new SimpleDateFormat(dateFormat).format(new Date()));
        productImportTask.setMessageId(UUID.randomUUID().toString());
        productImportTask.setTaskNo(taskNo);
        productImportTask.setType(importType.getIndex());
        productImportTask.setData("{\"taskFtpFilePath\":\""+ftpFilePath+"\"}");
        log.info("推送任务的参数：{}",productImportTask);
        if(TaskImportTpye.HUB_PRODUCT.getIndex()==importType.getIndex()){
        	productImportTaskStreamSender.hubProductImportTaskStream(productImportTask, null);
        	return;
        }
        productImportTaskStreamSender.pendingProductImportTaskStream(productImportTask, null);
    }
    private boolean saveTask(HubImportTaskRequestDto task,String taskNo,String ftpPath,String systemFileName,int importType) throws Exception{
        // TODO Auto-generated method stub
        HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
        hubSpuTask.setTaskNo(taskNo);
        hubSpuTask.setTaskFtpFilePath(ftpPath+systemFileName);
        hubSpuTask.setSysFileName(systemFileName);
        hubSpuTask.setLocalFileName(task.getFileName());
        hubSpuTask.setCreateTime(new Date());
        hubSpuTask.setCreateUser(task.getCreateUser());
        hubSpuTask.setTaskState((byte) TaskState.NO_HANDLE.getIndex());
        hubSpuTask.setImportType((byte)importType);
        spuImportGateway.insert(hubSpuTask);
        return true;
    }

    public HubTaskProductResponseWithPageDTO findHubTaskList(HubImportTaskListRequestDto param) {

        HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
        hubSpuImportTaskCriteriaDto.setPageNo(param.getPageNo());
        hubSpuImportTaskCriteriaDto.setPageSize(param.getPageSize());
        Criteria criteria = hubSpuImportTaskCriteriaDto.createCriteria();
        if(param.getTaskState()!=null){
            criteria.andTaskStateEqualTo(param.getTaskState());
        }
        if(param.getImportType()!=null){
            criteria.andImportTypeEqualTo(param.getImportType());
        }
        if(!StringUtils.isEmpty(param.getLocalFileName())){
            criteria.andLocalFileNameEqualTo(param.getLocalFileName());
        }
        if(!StringUtils.isEmpty(param.getStartDate())){
            criteria.andCreateTimeBetween(DateTimeUtil.convertFormat(param.getStartDate(),dateFormat),DateTimeUtil.convertFormat(param.getEndDate(),dateFormat));
        }
        int total = spuImportGateway.countByCriteria(hubSpuImportTaskCriteriaDto);
        log.info("hub任务列表查询到数量："+total);
        if(total<1){
            return null;
        }
        hubSpuImportTaskCriteriaDto.setOrderByClause("create_time desc");
        List<HubSpuImportTaskDto>  list = spuImportGateway.selectByCriteria(hubSpuImportTaskCriteriaDto);
        HubTaskProductResponseWithPageDTO hubTaskProductResponseWithPageDTO = new HubTaskProductResponseWithPageDTO();
        List<HubTaskProductResponseDTO> responseList = convertTaskDTO2ResponseDTO(list);
        hubTaskProductResponseWithPageDTO.setTotal(total);
        hubTaskProductResponseWithPageDTO.setTaskNoList(responseList);
        return hubTaskProductResponseWithPageDTO;
    }

    private List<HubTaskProductResponseDTO> convertTaskDTO2ResponseDTO(List<HubSpuImportTaskDto> list) {

        List<HubTaskProductResponseDTO> responseList = null;
        if(list!=null&&list.size()>0){
            responseList = new ArrayList<HubTaskProductResponseDTO>();
            for(HubSpuImportTaskDto dto : list){
                HubTaskProductResponseDTO response = new HubTaskProductResponseDTO();
                BeanUtils.copyProperties(dto,response);
                response.setCreateTime(DateTimeUtil.getTime(dto.getCreateTime()));
                response.setUpdateTime(DateTimeUtil.getTime(dto.getCreateTime()));
                responseList.add(response);
            }
        }
        return responseList;
    }

}
