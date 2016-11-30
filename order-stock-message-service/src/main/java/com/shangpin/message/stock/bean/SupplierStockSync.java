/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierStockSync.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.stock.bean;

import java.io.Serializable;
import java.util.List;

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
public class SupplierStockSync implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3117116406130046539L;
	/**
	 * 消息唯一标识
	 */
	private String messageId;
	/**
	 * 同步业务类型：ShopCart、Settlement购物车或结算页同步库存
	 */
	private String syncType;
	/**
	 * 库存同步明细
	 */
	private List<SupplierStockDetailSync> stockDetailSyncs;
	/**
	 * 消息发送时间
	 */
	private String messageDate;
}