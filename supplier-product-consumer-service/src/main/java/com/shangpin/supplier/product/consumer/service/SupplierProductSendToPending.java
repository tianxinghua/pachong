package com.shangpin.supplier.product.consumer.service;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PendingProductStreamSender;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SupplierProductSaveAndSendToPending </p>
 * <p>Description: 各个供应商保存数据并且发消息给Pending</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月15日 下午2:23:47
 *
 */
@Service
@Slf4j
public class SupplierProductSendToPending {
	
	public static final String METHOD_SUFFIX = "PendingProductStream";
	@Autowired
	private PendingProductStreamSender pendingProductStreamSender;
	/**
	 * 往供货商队列分发消息
	 * @param supplierProduct 供货商商品
	 */
	public boolean dispatchSupplierProduct(PendingProduct pendingProduct,Map<String,String> headers) {
		boolean flag = false;
		String supplierName = pendingProduct.getSupplierName();
		if (StringUtils.isBlank(supplierName)) {
			log.error("系统检接收到的供应商名称为空");
			throw new RuntimeException("系统检接收到的供应商名称为空，系统无法发送消息至队列中！");
		} else {
			flag = (Boolean) execute(supplierName,headers, pendingProduct);
		}
		return flag;
	}
	/**
	 * 执行调用
	 * @return
	 */
	private Object execute(String name,Map<String,String> headers,PendingProduct pendingProduct){
		try {
			Class<PendingProductStreamSender> clazz = PendingProductStreamSender.class;
			Method method = clazz.getMethod(new StringBuilder(name).append(METHOD_SUFFIX).toString(), PendingProduct.class,Map.class);
			return method.invoke(pendingProductStreamSender, pendingProduct,headers);
		} catch (Throwable e) {
			log.error("调度执行方法时因为供应商名称不存在而发生异常:"+e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException("调度执行方法时因为供应商名称不存在而发生异常", e);
		}
	}
}
