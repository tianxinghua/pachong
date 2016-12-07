package com.shangpin.supplier.product.message.original.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.supplier.product.message.original.conf.message.SupplierProduct;
import com.shangpin.supplier.product.message.original.conf.sender.OriginalProductStreamSender;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:OriginalProductService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午3:13:05
 */
@Service
@Slf4j
public class OriginalProductService {
	
	public static final String SPINNAKER = "spinnaker";
	
	public static final String OSTORE = "ostore";
	
	public static final String BRUNAROSSO = "brunarosso";
	
	public static final String STEFANIA = "stefania";
	
	public static final String GEB = "geb";
	
	public static final String COLTORTI = "coltorti";
	
	public static final String TONY = "tony";
	
	public static final String BIONDIONI = "biondioni";

	@Autowired
	private OriginalProductStreamSender originalProductStreamSender;
	/**
	 * 往供货商队列分发消息
	 * @param supplierProduct 供货商商品
	 */
	public boolean dispatchOriginalProduct(SupplierProduct supplierProduct) {
		boolean flag = false;
		String supplierName = supplierProduct.getSupplierName();
		if (StringUtils.isBlank(supplierName)) {
			log.error("系统检接收到的供应商名称为空");
			throw new RuntimeException("系统检接收到的供应商名称为空");
		}else if (SPINNAKER.equals(supplierName)){
			flag = originalProductStreamSender.spinnakerStream(supplierProduct);
		}else if (OSTORE.equals(supplierName)){
			flag = originalProductStreamSender.ostoreStream(supplierProduct);
		} else if (BRUNAROSSO.equals(supplierName)){
			flag = originalProductStreamSender.brunarossoStream(supplierProduct);
		} else if (STEFANIA.equals(supplierName)){
			flag = originalProductStreamSender.stefaniaStream(supplierProduct);
		} else if (GEB.equals(supplierName)){
			flag = originalProductStreamSender.gebStream(supplierProduct);
		} else if (COLTORTI.equals(supplierName)){
			flag = originalProductStreamSender.coltortiStream(supplierProduct);
		} else if (TONY.equals(supplierName)){
			flag = originalProductStreamSender.tonyStream(supplierProduct);
		} else if (BIONDIONI.equals(supplierName)){
			flag = originalProductStreamSender.biondioniStream(supplierProduct);
		} else {
			log.error("系统接收到的供应商名称非法");
			throw new  RuntimeException("系统接收到的供应商名称非法");
		}
		return flag;
	}
}
