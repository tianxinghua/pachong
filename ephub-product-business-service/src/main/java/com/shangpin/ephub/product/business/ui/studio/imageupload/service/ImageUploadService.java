package com.shangpin.ephub.product.business.ui.studio.imageupload.service;

import java.util.List;

import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
/**
 * <p>Title: ImageUploadService</p>
 * <p>Description: 图片上传页面所有的业务逻辑 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月27日 上午11:07:22
 *
 */
public interface ImageUploadService {

	/**
	 * 图片上传页面的列表
	 * @param operationQuery
	 * @return
	 */
	public List<StudioSlotVo> list(OperationQuery operationQuery);
	
	/**
	 * 根据批次号查询该批次下的所有产品详情
	 * @param slotNo
	 * @return
	 */
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo);
}
