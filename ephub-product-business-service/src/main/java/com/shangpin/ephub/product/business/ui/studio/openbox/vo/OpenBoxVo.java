package com.shangpin.ephub.product.business.ui.studio.openbox.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxVo</p>
 * <p>Description: 开箱质检页面的返回视图 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月12日 上午11:58:18
 *
 */
@Getter
@Setter
public class OpenBoxVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8240550154587635315L;

	/**
	 * 优先批次
	 */
	private List<StudioSlotVo> prioritySlots;
	/**
	 * 次要批次
	 */
	private List<StudioSlotVo> secondarySlots;
}
