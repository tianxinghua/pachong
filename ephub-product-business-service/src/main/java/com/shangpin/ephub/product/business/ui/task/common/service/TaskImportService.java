package com.shangpin.ephub.product.business.ui.task.common.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.ui.task.common.enumeration.TaskStatus;
import com.shangpin.ephub.product.business.ui.task.common.util.FTPClientUtil;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseDTO;
import com.shangpin.ephub.product.business.util.DateTimeUtil;
import com.shangpin.ephub.response.HubResponse;

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
public class TaskImportService {
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired 
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	ProductImportTaskStreamSender productImportTaskStreamSender;
	public HubResponse uploadFileAndSave(HubImportTaskRequestDto task,int importType) throws Exception{
		
		String []fileName = task.getFileName().split("\\.");
		if(fileName!=null&&fileName.length==2){
			if("xlsx".equals(fileName[1])||"xls".equals(fileName[1])){
				
				SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				Date date = new Date();
				String taskNo = sim.format(date);
				String systemFileName = taskNo +"."+fileName[1];
				
				//第一步 ： 上传ftp
				String ftpPath = FTPClientUtil.uploadFile(task.getUploadfile(),systemFileName);
				//第二步 ： 保存数据库
				saveTask(task,taskNo,ftpPath,systemFileName,importType);
				//TODO 第三步 ：发送到hub消息队列
				ProductImportTask productImportTask = new ProductImportTask();
				productImportTask.setMessageDate(new SimpleDateFormat(dateFormat).format(new Date()));
				productImportTask.setMessageId(UUID.randomUUID().toString());
				productImportTask.setTaskNo(taskNo);
				productImportTask.setTaskFtpFilePath(ftpPath+systemFileName);
				productImportTaskStreamSender.hubProductImportTaskStream(productImportTask, null);
				return HubResponse.successResp(null);
			}
		}
		return HubResponse.errorResp("文件格式有误，请下载模板");
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
	    hubSpuTask.setTaskState((byte) TaskStatus.NO_HANDLE.getIndex());
	    hubSpuTask.setImportType((byte)importType);
	    spuImportGateway.insert(hubSpuTask);
		return true;
	}
	
	public List<HubTaskProductResponseDTO> findHubTaskList(HubImportTaskListRequestDto param,List<Byte> listImportType) {
		
		HubSpuImportTaskCriteriaWithRowBoundsDto dto = new HubSpuImportTaskCriteriaWithRowBoundsDto();
		if(!StringUtils.isEmpty(param.getPageNo()) && !StringUtils.isEmpty(param.getPageSize())){
			RowBoundsDto rowBounds = new RowBoundsDto(param.getPageNo(),param.getPageSize());
			dto.setRowBounds(rowBounds);
		}
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		Criteria criteria = hubSpuImportTaskCriteriaDto.createCriteria();
		if(!StringUtils.isEmpty(param.getTaskState())){
			criteria.andTaskStateNotEqualTo((byte)param.getTaskState());
		}
		if(!StringUtils.isEmpty(param.getLocalFileName())){
			criteria.andLocalFileNameEqualTo(param.getLocalFileName());
		}
		if(!StringUtils.isEmpty(param.getStartDate())){
			criteria.andCreateTimeBetween(DateTimeUtil.convertFormat(param.getStartDate(),dateFormat),DateTimeUtil.convertFormat(param.getEndDate(),dateFormat));
		}
		criteria.andImportTypeIn(listImportType);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		List<HubSpuImportTaskDto>  list = spuImportGateway.selectByCriteriaWithRowbounds(dto);
		List<HubTaskProductResponseDTO> responseList = convertTaskDTO2ResponseDTO(list);
		return responseList;
	}
	
	private List<HubTaskProductResponseDTO> convertTaskDTO2ResponseDTO(List<HubSpuImportTaskDto> list) {
		
		List<HubTaskProductResponseDTO> responseList = null;
		if(list!=null&&list.size()>0){
			responseList = new ArrayList<HubTaskProductResponseDTO>();
			for(HubSpuImportTaskDto dto : list){
				HubTaskProductResponseDTO response = new HubTaskProductResponseDTO();
				BeanUtils.copyProperties(dto,response);
				responseList.add(response);
			}
		}
		return responseList;
	}
	public HubResponse<byte[]> downResultFile(String resultFilePath) throws Exception{
		// TODO Auto-generated method stub
		InputStream in = FTPClientUtil.downFile(resultFilePath);
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据 
		int rc = 0; 
		while ((rc = in.read(buff, 0, 100)) > 0) { 
		swapStream.write(buff, 0, rc); 
		} 
		swapStream.flush();
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果 
		return HubResponse.successResp(in_b);
	}

}
