package com.shangpin.ephub.product.business.ui.studio.common.operation.service;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
/**
 * <p>Title: OperationService</p>
 * <p>Description: 通用service </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月26日 下午4:27:12
 *
 */
public interface OperationService {
	/**
	 * Studio Operation页面查询列表
	 * @param operationQuery
	 * @return
	 */
	public List<StudioSlotDto> slotList(OperationQuery operationQuery) throws Exception ;
	/**
	 * 根据摄影棚编号获取主键
	 * @param studioNo
	 * @return
	 */
	public Long getStudioId(String studioNo);

}
