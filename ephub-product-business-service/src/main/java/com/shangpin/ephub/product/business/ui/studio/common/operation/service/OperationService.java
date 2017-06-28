package com.shangpin.ephub.product.business.ui.studio.common.operation.service;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
/**
 * <p>Title: OperationService</p>
 * <p>Description: 通用service </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月26日 下午4:27:12
 *
 */
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
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
	/**
	 * 将StudioSlotDto转换为StudioSlotVo
	 * @param studioSlotDto
	 * @return 转换后的StudioSlotVo对象
	 */
	public StudioSlotVo formatDto(StudioSlotDto studioSlotDto);
	/**
	 * 根据批次号查询该批次下的所有产品详情
	 * @param slotNo
	 * @return
	 */
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo);

}
