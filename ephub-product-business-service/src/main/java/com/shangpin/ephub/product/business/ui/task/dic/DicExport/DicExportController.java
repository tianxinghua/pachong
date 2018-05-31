package com.shangpin.ephub.product.business.ui.task.dic.DicExport;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.gateway.HubMaterialDicGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.product.business.common.hubDic.category.HubCategoryDicService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.task.dic.DicExportDto.*;
import com.shangpin.ephub.product.business.ui.task.pending.export.service.ExportService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.shangpin.ephub.product.business.ui.task.dic.DicExportDto.SupplierCategroyDicCriteriaDto;


/**
 * <p>Title: ExportController</p>
 * <p>Description: 待处理页（待拍照）的导出 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月5日 上午11:21:42
 *
 */

@RestController
@RequestMapping("/dic-export")
@Slf4j
public class DicExportController {
	
	@Autowired
	private ExportService exportService;
	@Autowired
	private IPendingProductService pendingProductService;
	@Autowired
    private HubSlotSpuService slotSpuService;
	@Autowired
	HubMaterialDicGateWay hubMaterialDicGateWay;
	@Autowired
	private HubColorDicItemGateWay hubColorDicItemGateWay;
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	HubCategoryDicService hubCategoryDicService;
	@Autowired
	HubSupplierBrandDicGateWay hubSupplierBrandDicGateWay;



	/**
	 * 字典材质的导出
	 * @param
	 * @return
	 */
	@RequestMapping(value="/export-material",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody MaterialRequestDto materialRequestDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(materialRequestDto.getCreateUser(), remotePath , TaskType.EXPORT_MATERIAL);

			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_MATERIAL,materialRequestDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("材质的导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("材质导出异常");
	}

	/**
	 * 字典颜色的导出
	 * @param pendingColorDto
	 * @return
	 */

	@RequestMapping(value="/export-color",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody HubColorDic pendingColorDto){
		try {
			System.out.println(pendingColorDto.getPageSize());
			System.out.println(pendingColorDto.getSupplierColorName());
			String remotePath = "pending_export";
			//第一步创建任务
			System.out.println(pendingColorDto.getPageSize());
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(pendingColorDto.getCreateName(), remotePath , TaskType.EXPORT_COLOR);
			//第二步发送队列
			//HubColorDicItemCriteriaDto hubColorDicItemCriteriaDto = new HubColorDicItemCriteriaDto();
			/*hubColorDicItemCriteriaDto.setPageNo(pendingColorDto.getPageNo());
			hubColorDicItemCriteriaDto.setPageSize(pendingColorDto.getPageSize());
			if (pendingColorDto.getSupplierColorName()!=null){
				hubColorDicItemCriteriaDto.createCriteria().andColorItemNameEqualTo(pendingColorDto.getSupplierColorName());
			}
			if (pendingColorDto.getHubColorName()!=null){
				hubColorDicItemCriteriaDto.createCriteria().andColorDicIdIsNotNull();
			}
			int count = hubColorDicItemGateWay.countByCriteria(hubColorDicItemCriteriaDto);
			pendingColorDto.setPageSize(count);*/
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_COLOR, pendingColorDto);

			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("颜色导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("颜色导出异常");
	}

	/**
	 * 字典产地导出
	 * @param madeMappingDtoDto
	 * @return
	 */
	@RequestMapping(value="/export-made",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody HubSupplierMadeMappingDto madeMappingDtoDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(madeMappingDtoDto.getCreateUser(), remotePath , TaskType.EXPORT_ORIGIN);
			//第二步发送队列
			HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto = new HubSupplierValueMappingCriteriaDto();
			hubSupplierValueMappingCriteriaDto.createCriteria().andHubValTypeEqualTo(madeMappingDtoDto.getType());
			//获取总条数
			int total = hubSupplierValueMappingGateWay.countByCriteria(hubSupplierValueMappingCriteriaDto);
			madeMappingDtoDto.setPageSize(total);
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_ORIGIN, madeMappingDtoDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("产地导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("产地导出异常");
	}

	/**  字典品类的导出
	 * @param SupplierCategroyDicCriteriaDto
	 * @return
	 */
	@RequestMapping(value="/export-category",method=RequestMethod.POST)
	public HubResponse<?> exportWaitToShoot(@RequestBody SupplierCategroyDicCriteriaDto SupplierCategroyDicCriteriaDto){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(SupplierCategroyDicCriteriaDto.getCreateName(),remotePath,TaskType.EXPORT_CATEGORY);
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_CATEGORY, SupplierCategroyDicCriteriaDto);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("品类导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("品类导出异常");
	}

	/**
	 * 品牌导出
	 * @param brandRequestDTO
	 * @return
	 */
	@RequestMapping(value="/export-brand",method=RequestMethod.POST)
	public HubResponse<?> exportBrand(@RequestBody BrandRequestDTO brandRequestDTO){
		try {
			String remotePath = "pending_export";
			//第一步创建任务
			HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(brandRequestDTO.getCreateUser(), remotePath , TaskType.EXPORT_BRAND);
			//第二步发送队
			/*HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto =null ;
			hubSupplierBrandDicCriteriaDto.setPageNo(brandRequestDTO.getPageNo());
			hubSupplierBrandDicCriteriaDto.setPageSize(brandRequestDTO.getPageSize());
			if (brandRequestDTO.getSupplierBrand()!=null){
				hubSupplierBrandDicCriteriaDto.createCriteria().andSupplierBrandEqualTo(brandRequestDTO.getSupplierBrand());
			}

			int count = hubSupplierBrandDicGateWay.countByCriteria(hubSupplierBrandDicCriteriaDto);
			brandRequestDTO.setPageSize(count);
*/
			boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_BRAND, brandRequestDTO);
			if(bool){
				return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
			}
		} catch (Exception e) {
			log.error("品牌导出异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("品牌导出异常");
	}

}
