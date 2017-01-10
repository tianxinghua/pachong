package com.shangpin.supplier.product.message.original.service;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
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
	
	public static final String METHOD_SUFFIX = "Stream";

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
			throw new RuntimeException("系统检接收到的供应商名称为空，系统无法发送消息至队列中！");
		} else {
			flag = (Boolean) execute(supplierName, supplierProduct);
		}
		return flag;
	}
	/**
	 * 执行调用
	 * @return
	 */
	private Object execute(String name, SupplierProduct supplierProduct){
		try {
			Class<OriginalProductStreamSender> clazz = OriginalProductStreamSender.class;
			Method method = clazz.getMethod(new StringBuilder(name).append(METHOD_SUFFIX).toString(), SupplierProduct.class);
			return method.invoke(originalProductStreamSender, supplierProduct);
		} catch (Throwable e) {
			log.error("调度执行方法时因为供应商名称不存在而发生异常:"+e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException("调度执行方法时因为供应商名称不存在而发生异常", e);
		}
	}
}
