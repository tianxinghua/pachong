package com.shangpin.ephub.product.business.ui.studio.openbox.service;

import com.shangpin.ephub.product.business.ui.studio.openbox.dto.OpenBoxQuery;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxVo;

public interface OpenBoxService {

	/**
	 * 根据页面传入的条件查询studio slot数据
	 * @param openBoxQuery
	 * @return
	 */
	public OpenBoxVo slotList(OpenBoxQuery openBoxQuery);
}
