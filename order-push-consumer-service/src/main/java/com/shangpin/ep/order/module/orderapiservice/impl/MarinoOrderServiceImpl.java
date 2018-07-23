package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.AtelierOrderHandler;
import com.shangpin.ep.order.module.spu.bean.HubSupplierSpu;
import com.shangpin.ep.order.module.spu.service.SupplierSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("marinoOrderService")
@Slf4j
public class MarinoOrderServiceImpl extends AtelierOrderHandler {
	
	@Autowired
	private SupplierProperties properties;

	@Autowired
	SupplierSpuService supplierSpuService;

	@Override
	public String getApiUrl() {
		return properties.getMarino().getUrl();
	}

	@Override
	public String getUserName() {
		return properties.getMarino().getUser();
	}

	@Override
	public String getPassword() {
		return properties.getMarino().getPassword();
	}

	@Override
	public String getGetItemStockInterface() {
		return properties.getMarino().getGetItemStockInterface();
	}

	@Override
	public String getCreateOrderInterface() {
		return properties.getMarino().getCreateOrderInterface();
	}

	@Override
	public String getSetStatusInterface() {
		return properties.getMarino().getSetStatusInterface();
	}



	@Override
	public ShangpinMail getEmailMessage() {
		ShangpinMail shangpinMail = new ShangpinMail();
        shangpinMail.setFrom("chengxu@shangpin.com");
        shangpinMail.setSubject("Mancini-order-shangpin");
        shangpinMail.setTo("nicolamonte.mancini@gmail.com");

        List<String> addTo = new ArrayList<>();
		addTo.add("customer@mancinijunior.com");
//		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		return shangpinMail;

	}




	@Override
	public String getEmailContent(OrderDTO orderDTO, String size) {

		try {

			String supplierSkuNo = orderDTO.getSupplierSkuNo();
			int index = supplierSkuNo.lastIndexOf("-");
			String item_id = null;
			String barcode = null;
			if (index > 0) {
				item_id = supplierSkuNo.substring(0, index);
				barcode = supplierSkuNo.substring(index + 1);
			}
			HubSupplierSpu supplierSpu = supplierSpuService.getSupplierSpuBySupplierSpuNo(orderDTO.getSupplierId(), item_id);
			if (null != supplierSpu) {
				//采购单号 尺码  skuId 货号  barcode 数量
				String messageText = "Shangpin OrderNo: " + orderDTO.getPurchaseNo() + "<br>" +
						"ProductSize: " + size + "<br>" +
						"SpuId-SkuId: " + orderDTO.getSupplierSkuNo() + "<br>" +
						"StyleCode-ColorCode: " + (null != supplierSpu.getSupplierSpuModel() ? supplierSpu.getSupplierSpuModel() : "") + "<br>" +
						"Barcode: " + barcode + "<br>" +
						"Qty: " + orderDTO.getQuantity() + "<br>" +
						"Status: confirmed";
                return  messageText;
			}
		} catch (Exception e) {
              log.error("获取邮件内容失败.Reason :" + e.getMessage());
		}

        return "";

	}

}
