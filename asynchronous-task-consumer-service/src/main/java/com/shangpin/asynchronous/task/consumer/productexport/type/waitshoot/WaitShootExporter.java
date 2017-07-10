package com.shangpin.asynchronous.task.consumer.productexport.type.waitshoot;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productexport.template.Template;
import com.shangpin.asynchronous.task.consumer.productexport.type.common.CommonExporter;
import com.shangpin.asynchronous.task.consumer.productimport.slot.dto.HubSlotSpuExcelDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;

@Service("waitShootExporter")
public class WaitShootExporter extends CommonExporter<PendingQuryDto,HubSlotSpuExcelDto> {

	@Override
	public String[] getExcelHeader() {
		return Template.WAIT_SHOOT_CH;
	}

	@Override
	public String[] getExcelRowKeys() {
		return Template.WAIT_SHOOT_EN;
	}

	@Override
	public int getTotalSize(PendingQuryDto t) {
		return t.getPageSize();
	}

	@Override
	public List<HubSlotSpuExcelDto> searchAndConvert(int pageIndex, Integer pageSize, PendingQuryDto t) {
		return null;
	}

	@Override
	public String getCreateUser(PendingQuryDto t) {
		return t.getCreateUser();
	}

	
	
}
