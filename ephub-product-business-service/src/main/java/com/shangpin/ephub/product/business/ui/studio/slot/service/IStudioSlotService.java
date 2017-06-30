package com.shangpin.ephub.product.business.ui.studio.slot.service;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.product.business.ui.studio.slot.dto.SlotManageQuery;
import com.shangpin.ephub.response.HubResponse;

/**
 * @author wangchao
 * @date 2017年06月26日 下午3:59:51
 *
 */
public interface IStudioSlotService {
	/**
	 * StudioSlot导出处理
	 * @param exportSpu
	 * @return
	 */
	public HubResponse<?> exportStudio(SlotManageQuery slotManageQuery,TaskType taskType);
}
