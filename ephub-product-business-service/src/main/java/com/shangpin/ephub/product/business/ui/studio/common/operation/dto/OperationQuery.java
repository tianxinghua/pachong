package com.shangpin.ephub.product.business.ui.studio.common.operation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxQuery</p>
 * <p>Description: studio operation页面查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午2:03:07
 *
 */
@Getter
@Setter
public class OperationQuery {
	/**
	 * 用来指定是那个页面
	 */
	private Integer operationQueryType;
	/**
	 * 摄影棚编号
	 */
	private String studioNo;

	/**
	 * 物流单号
	 */
	private String trackingNo;
	/**
	 * 批次名称
	 */
	private String slotName;
	/**
	 * 预计拍摄日期
	 */
	private List<String> operateDate;
	
	/**
	 * 分页
	 */
	private Integer pageIndex;
	/**
	 * 分页
	 */
    private Integer pageSize;
}
