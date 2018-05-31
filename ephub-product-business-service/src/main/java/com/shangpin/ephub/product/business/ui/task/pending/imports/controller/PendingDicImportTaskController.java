package com.shangpin.ephub.product.business.ui.task.pending.imports.controller;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.product.business.ui.task.dic.DicExportDto.BrandRequestDTO;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskListRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.dto.HubImportTaskRequestDto;
import com.shangpin.ephub.product.business.ui.task.spuimport.vo.HubTaskProductResponseWithPageDTO;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p>
 * @author zhaogenchun
 * @date 2016年12月17日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/dic-task")
@Slf4j
public class PendingDicImportTaskController {
	@Autowired
	TaskStreamSender productImportTaskStreamSender;
	@Autowired
	TaskImportService pendingImportTaskService;
	/**
	 * 颜色导入
	 *
	 * @return
	 */
	@RequestMapping(value = "/import-color",method = RequestMethod.POST)
	public HubResponse importColor(@RequestBody HubImportTaskRequestDto hubImportTaskRequestDto){

		try {
			log.info("颜色上传参数：{}",hubImportTaskRequestDto);
			return pendingImportTaskService.uploadFileAndSave(hubImportTaskRequestDto,TaskType.IMPORT_COLOR);
		} catch (Exception e) {
			log.error("颜色上传文件失败",e);
			e.printStackTrace();
			return HubResponse.errorResp("颜色上传文件失败，请重新上传");
		}
	}

	/**
	 * 产地导入
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/import-made",method = RequestMethod.POST)
	public HubResponse importSku(@RequestBody HubImportTaskRequestDto dto){

		try {
			log.info("产地上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskType.IMPORT_ORIGIN);
		} catch (Exception e) {
			return HubResponse.errorResp("产地上传文件失败，请重新上传");
		}
	}

	@RequestMapping(value = "/import-material",method = RequestMethod.POST)
	public HubResponse importMaterial(@RequestBody HubImportTaskRequestDto dto){

		try {
			log.info("材质上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskType.IMPORT_MATERIAL);
		} catch (Exception e) {
			return HubResponse.errorResp("材质上传文件失败，请重新上传");
		}
	}
	@RequestMapping(value = "/import-category",method = RequestMethod.POST)
	public HubResponse importCateGroy(@RequestBody HubImportTaskRequestDto dto){

		try {
			log.info("品类上传参数：{}",dto);
			return pendingImportTaskService.uploadFileAndSave(dto,TaskType.IMPORT_CATEGORY);
		} catch (Exception e) {
			return HubResponse.errorResp("品类上传文件失败，请重新上传");
		}
	}




}





