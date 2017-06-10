package com.shangpin.ephub.product.business.ui.studio.openbox.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxQuery</p>
 * <p>Description: OpenBox页面查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午2:03:07
 *
 */
@Getter
@Setter
public class OpenBoxQuery {

	private String trackingNo;
	private String slotName;
	private String operateStartDate;
	private String operateEndDate;
}
