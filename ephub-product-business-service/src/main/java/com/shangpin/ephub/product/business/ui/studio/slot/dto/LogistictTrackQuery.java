package com.shangpin.ephub.product.business.ui.studio.slot.dto;

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
public class LogistictTrackQuery {

	private String trackName;
	private String trackNo;
	private String masterId;
	private Integer quantity;
	private Integer actualNumber;
}
