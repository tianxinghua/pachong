package com.shangpin.ephub.product.business.ui.task.pendingimport.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.common.dto.RowBoundsDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.config.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.util.FTPClientUtil;
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
public class PendingImportTaskService {
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	@Autowired 
	HubSpuImportTaskGateWay spuImportGateway;
	private static String ftpPath = "F://";
	public HubResponse uploadFileAndSave(HubImportTaskRequestDto task) throws Exception{
		
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		String systemFileName = ftpPath+sim.format(date)+task.getFileName().split(".")[1];
		//第一步 ： 上传ftp
		HubResponse flag = FTPClientUtil.uploadFile(task.getUploadfile(),ftpPath,systemFileName);
		//第二步 ： 保存数据库
		if("0".equals(flag.getCode())){
			saveTask(task);
			//第三步 ： 发送到hub消息队列
			
		}
		return flag;
	}
	private boolean saveTask(HubImportTaskRequestDto task) throws Exception{
		// TODO Auto-generated method stub
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
		Date date = new Date();
		hubSpuTask.setTaskNo(sim.format(date));
		hubSpuTask.setTaskFtpFilePath(ftpPath+sim.format(date)+task.getFileName().split(".")[1]);
		hubSpuTask.setSysFileName(sim.format(date)+task.getFileName().split(".")[1]);
	    hubSpuTask.setLocalFileName(task.getFileName());
	    hubSpuTask.setCreateTime(date);
	    hubSpuTask.setCreateUser(task.getCreateUser());
	    hubSpuTask.setTaskState((byte) 0);
	    spuImportGateway.insert(hubSpuTask);
		return true;
	}
	
	public List<HubTaskProductResponseDTO> findHubTaskList(HubImportTaskListRequestDto param) {
		
		HubSpuImportTaskCriteriaWithRowBoundsDto dto = new HubSpuImportTaskCriteriaWithRowBoundsDto();
		if(!StringUtils.isEmpty(param.getPageIndex()) && !StringUtils.isEmpty(param.getPageSize())){
			RowBoundsDto rowBounds = new RowBoundsDto(param.getPageIndex(),param.getPageSize());
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

}
