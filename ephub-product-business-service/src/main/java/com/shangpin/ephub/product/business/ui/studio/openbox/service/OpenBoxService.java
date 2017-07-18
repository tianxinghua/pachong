package com.shangpin.ephub.product.business.ui.studio.openbox.service;

import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.CheckDetailVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxDetailVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxVo;

public interface OpenBoxService {
	
	/**
	 * 根据页面传入的条件查询studio slot数据
	 * @param openBoxQuery
	 * @return
	 */
	public OpenBoxVo slotList(OperationQuery openBoxQuery);
	/**
	 * 详情页
	 * @param slotNo 批次号
	 * @return
	 */
	public OpenBoxDetailVo slotDetail(String slotNo);
	/**
	 * 扫码质检
	 * @param slotNoSpuId 批次号和spuPendingId的组合
	 * @param arriveUser质检人
	 * @return
	 */
	public boolean slotDetailCheck(String slotNoSpuId ,String arriveUser);
	/**
	 * 盘盈盘亏
	 * @param slotNo 批次号
	 * @param arriveUser 到货签收人
	 * @return
	 */
	public CheckDetailVo checkResult(String slotNo,String arriveUser);
}
