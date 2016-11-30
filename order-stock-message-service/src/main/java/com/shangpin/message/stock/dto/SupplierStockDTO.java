/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierStockSyncDTO.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.stock.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierStockDTO implements Serializable {
	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = -1359049740908758745L;
	/**
	 * 消息唯一标识
	 */
	private String messageId;
	/**
	 * 同步业务类型：ShopCart、Settlement购物车或结算页同步库存
	 */
	private String syncType;
	/**
	 * 库存同步优先级别：购物车级别：0 ，结算页级别：2
	 */
	private Integer priority;
	/**
	 * 订单同步明细
	 */
	private List<SupplierStockDetailDTO> stockDetailSyncDtos;
}