package com.shangpin.picture.product.consumer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;

/**
 * <p>Title:SupplierProductPictureManager.java </p>
 * <p>Description: 负责外部系统调用的管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午12:44:02
 */
@Component
public class SupplierProductPictureManager {
	
	@Autowired
	private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;

}
