package com.shangpin.asynchronous.task.consumer.productexport.type.waitshoot;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productexport.template.Template;
import com.shangpin.asynchronous.task.consumer.productexport.type.common.CommonExporter;
import com.shangpin.asynchronous.task.consumer.productimport.slot.dto.HubSlotSpuExcelDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.PendingQuryDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProductDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.PendingProducts;
/**
 * <p>Title: WaitShootExporter</p>
 * <p>Description: 待拍照页面的导出 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月11日 上午11:25:21
 *
 */
@Service("waitShootExporter")
public class WaitShootExporter extends CommonExporter<PendingQuryDto,HubSlotSpuExcelDto> {
	
	@Autowired
	private HubPendingSpuCheckGateWay hubPendingSpuClient;

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
		t.setPageIndex(pageIndex);
		t.setPageSize(pageSize);
		PendingProducts products = hubPendingSpuClient.exportPengdingSpu(t);
		List<PendingProductDto> list = products.getProduts();
		if(CollectionUtils.isNotEmpty(list)){
			List<HubSlotSpuExcelDto> slotSpus = new ArrayList<HubSlotSpuExcelDto>();
			for(PendingProductDto product : list){
				HubSlotSpuExcelDto slotSpu = convert(product);
				slotSpus.add(slotSpu);
			}
			return slotSpus;
		}
		return null;
	}

	private HubSlotSpuExcelDto convert(PendingProductDto product) {
		HubSlotSpuExcelDto slotSpu = new HubSlotSpuExcelDto();
		slotSpu.setSpuPendingId(product.getSpuPendingId());
		slotSpu.setSupplierId(product.getSupplierId());
		slotSpu.setSupplierNo(product.getSupplierNo());
		slotSpu.setSupplierName(product.getSupplierName());
		slotSpu.setSupplierCategoryname(product.getSupplierCategoryname());
		slotSpu.setHubCategoryName(product.getHubCategoryName());
		slotSpu.setHubCategoryNo(product.getHubCategoryNo());
		slotSpu.setHubBrandNo(product.getHubBrandNo());
		slotSpu.setHubBrandName(product.getHubBrandName());
		slotSpu.setSpuModel(product.getSpuModel()); 
		return slotSpu;
	}

	@Override
	public String getCreateUser(PendingQuryDto t) {
		return t.getCreateUser();
	}

	
	
}
