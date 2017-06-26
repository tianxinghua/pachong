package com.shangpin.ephub.product.business.ui.studio.openbox.vo;

import java.util.List;

import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckDetailVo {

	/**
	 * 盘盈商品
	 */
	private List<StudioSlotSpuSendDetailVo> inventoryProfit;
	/**
	 * 盘亏商品
	 */
	private List<StudioSlotSpuSendDetailVo> inventoryLosses;
}
