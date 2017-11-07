package com.shangpin.ephub.product.business.ui.studio.slot.vo.export;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: StudioSlotSendDetailInfo</p>
 * <p>Description:此类作为解析导入导出excel的实体类，<br>
 * 其字段不可随意修改，字段的顺序也不能随意修改，必须和excel表头保持一致 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年10月26日 上午11:25:03
 *
 */

@Setter
@Getter
public class SlotDetailDto {
	
	private String slotSpuSupplierId;
	private String supplierSpuId;
	private String spuPendingId;
	private String slotSpuNo;
	private String slotNo;
	private String supplierSpuModel;
	private String supplierBrandName;
	private String season;
	private String gender;
	private String barcode;
	private String colour;
	private String madeIn;
	private String composition;
	private String description;
	
	
}