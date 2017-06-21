package com.shangpin.ephub.product.business.ui.studio.incomingslots.service;

import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.ConfirmQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.IncomingSlotsQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.vo.IncomingSlotsVo;

/**
 * <p>Title: IncomingSlotsService</p>
 * <p>Description: 样品收货页面所有的业务逻辑 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月20日 下午3:28:32
 *
 */
public interface IncomingSlotsService {

	/**
	 * 根据页面传参查询所有在途批次
	 * @param query
	 * @return
	 */
	public IncomingSlotsVo list(IncomingSlotsQuery query);
	/**
	 * 确认到货
	 * @param ids
	 * @return
	 */
	public boolean confirm(ConfirmQuery confirmQuery);
}
