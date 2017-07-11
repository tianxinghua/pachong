package com.shangpin.asynchronous.task.consumer.productexport.type.commited;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.asynchronous.task.consumer.productexport.template.Template;
import com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto.CommitedExcelDto;
import com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto.SlotSpuExportDto;
import com.shangpin.asynchronous.task.consumer.productexport.type.commited.dto.SlotSpuSupplierDto;
import com.shangpin.asynchronous.task.consumer.productexport.type.commited.enumeration.SpuState;
import com.shangpin.asynchronous.task.consumer.productexport.type.common.CommonExporter;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.client.product.business.studio.gateway.HubSlotSpuTaskGateWay;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: CommitedExporter</p>
 * <p>Description: 已提交页导出 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月11日 上午11:17:54
 *
 */
@Service("commitedExporter")
public class CommitedExporter extends CommonExporter<SpuSupplierQueryDto, CommitedExcelDto>{
	
	@Autowired
	private HubSlotSpuTaskGateWay hubSlotSpuTaskGateWay;

	@Override
	public String[] getExcelHeader() {
		return Template.COMMITED_CH;
	}

	@Override
	public String[] getExcelRowKeys() {
		return Template.COMMITED_EN;
	}

	@Override
	public int getTotalSize(SpuSupplierQueryDto t) {
		return t.getPageSize();
	}

	@Override
	public List<CommitedExcelDto> searchAndConvert(int pageIndex, Integer pageSize, SpuSupplierQueryDto t) {
		t.setPageIndex(pageIndex);
		t.setPageSize(pageSize); 
		HubResponse<?> response = hubSlotSpuTaskGateWay.commitedExport(t);
		List<SlotSpuExportDto> list = (List<SlotSpuExportDto>) response.getContent();
		if(CollectionUtils.isNotEmpty(list)){
			List<CommitedExcelDto> excelDtos = new ArrayList<CommitedExcelDto>();
			for(SlotSpuExportDto exportDto : list){
				for(SlotSpuSupplierDto supplierDto : exportDto.getSpuSupplierDtos()){
					CommitedExcelDto excelDto = convert(exportDto,supplierDto);
					excelDtos.add(excelDto);
				}
			}
			return excelDtos;
		}
		return null;
	}

	private CommitedExcelDto convert(SlotSpuExportDto exportDto, SlotSpuSupplierDto supplierDto) {
		CommitedExcelDto excelDto = new CommitedExcelDto();
		excelDto.setSpuModel(exportDto.getSpuModel());
		excelDto.setHubBrandName(exportDto.getHubBrandName());
		excelDto.setHubCategoryName(exportDto.getHubCategoryName());
		excelDto.setSupplierName(supplierDto.getSupplierName());
		excelDto.setStudioName(supplierDto.getStudioName());
		excelDto.setUpdateTime(exportDto.getUpdateTime());
		String relationState = SpuState.spuStates.get(exportDto.getSpuState());
		excelDto.setRelationState(!StringUtils.isEmpty(relationState) ? relationState : ""); 
		return excelDto;
	}

	@Override
	public String getCreateUser(SpuSupplierQueryDto t) {
		return t.getCreateUser();
	}

	
}
