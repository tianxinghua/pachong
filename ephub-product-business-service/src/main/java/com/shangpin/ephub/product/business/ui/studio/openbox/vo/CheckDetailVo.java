package com.shangpin.ephub.product.business.ui.studio.openbox.vo;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckDetailVo {

	/**
	 * 盘盈商品
	 */
	private List<StudioSlotSpuSendDetailDto> inventoryProfit;
	/**
	 * 盘亏商品
	 */
	private List<StudioSlotSpuSendDetailDto> inventoryLosses;
}
