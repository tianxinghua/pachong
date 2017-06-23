package com.shangpin.ephub.product.business.ui.studio.incomingslots.vo;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: IncomingSlotsVo</p>
 * <p>Description: 样品收货页面返回实体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月20日 下午5:35:12
 *
 */
@Getter
@Setter
public class IncomingSlotsVo {

	/**
	 * 优先批次
	 */
	private List<StudioSlotDto> prioritySlots;
	/**
	 * 次要批次
	 */
	private List<StudioSlotDto> secondarySlots;
}
