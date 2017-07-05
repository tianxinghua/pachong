package com.shangpin.ephub.product.business.ui.studio.common.operation.service;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;

/**
 * <p>Title: OperationService</p>
 * <p>Description: studio相关通用service </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月26日 下午4:27:12
 *
 */
public interface OperationService {
	
	/**
	 * 根据批次号查找批次
	 * @param slotNo
	 * @return
	 */
	public StudioSlotDto selectStudioSlot(String slotNo);
	/**
	 * Studio Operation页面查询列表
	 * @param operationQuery
	 * @return
	 */
	public List<StudioSlotDto> slotList(OperationQuery operationQuery) throws Exception ;
	/**
	 * 根据主键查找摄影棚信息
	 * @param studioId
	 * @return
	 */
	public StudioDto getStudio(Long studioId);
	
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
	/**
	 * 根据slotNo查找
	 * @param slotNo
	 * @return
	 */
	public HubSlotSpuDto findSlotSpu(String slotSpuNo);
	/**
	 * 
	 * @param slotNo
	 * @param slotSpuNo
	 * @return
	 */
	public HubSlotSpuSupplierDto findSlotSpuSupplier(String slotNo, String slotSpuNo);
	/**
	 * 查找该批次下边所有的产品详情
	 * @param slotNo 批次号
	 * @return
	 */
	public List<StudioSlotSpuSendDetailDto> selectDetail(String slotNo);
	/**
	 * 根据扫码查找该产品
	 * @param barcode
	 * @return
	 */
	public StudioSlotSpuSendDetailDto selectSlotSpuSendDetail(String barcode);

}
