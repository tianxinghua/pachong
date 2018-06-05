package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.spu.bean.HubSupplierSpu;
import com.shangpin.ep.order.module.spu.service.SupplierSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;

import java.util.ArrayList;
import java.util.List;

@Component("mengottiServiceImpl")
public class MengottiServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Autowired
	SupplierSpuService supplierSpuService;

	@Override
	public String getApiUrl() {
		return properties.getMengotti().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getMengotti().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getMengotti().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getMengotti().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getMengotti().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getMengotti().getSetStatusInterface();
	}

	@Override
	public ShangpinMail getEmailMessage() {

		String subject = "mengotti Snc-order-shangpin";
		ShangpinMail shangpinMail = new ShangpinMail();
		shangpinMail.setFrom("chengxu@shangpin.com");
		shangpinMail.setSubject(subject);
		shangpinMail.setTo("info@mengotti-online.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("ordini@mengotti-online.com ");
		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		return shangpinMail;
	}

	@Override
	public String getEmailContent(OrderDTO orderDTO, String size) {

		String supplierSkuNo = orderDTO.getSupplierSkuNo();
		int index = supplierSkuNo.lastIndexOf("-");
		String item_id = null;
		String barcode = null;
		if(index>0){
			item_id = supplierSkuNo.substring(0,index);
			barcode = supplierSkuNo.substring(index+1);
		}

		HubSupplierSpu supplierSpu  = supplierSpuService.getSupplierSpuBySupplierSpuNo(orderDTO.getSupplierId(), item_id);
		if(null!=supplierSpu) return "";
		String messageText ="Shangpin OrderNo: "+orderDTO.getPurchaseNo()+"<br>"+
				"ProductSize: "+size+"<br>"+
				"SpuId-SkuId: "+orderDTO.getSupplierSkuNo()+"<br>"+
				"StyleCode-ColorCode: "+(null != supplierSpu.getSupplierSpuModel()? supplierSpu.getSupplierSpuModel():"")+"<br>"+
				"Barcode: "+barcode+"<br>"+
				"Qty: "+orderDTO.getQuantity()+"<br>"+
				"Status: confirmed";
		return  messageText;
	}

}
