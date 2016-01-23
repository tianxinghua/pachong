package com.shangpin.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.UpdateStockService;
@Component
public class StockMoniTask{
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;
	private static ResourceBundle bdl = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
	}
	@Autowired
	UpdateStockService updateStockService;
	@Autowired
	SupplierService supplierService;
	
	public void sendMail() {
		String errString = getErrString();
		if (StringUtils.isNotBlank(errString)) {
			try {
				SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject, errString, messageType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public String getErrString(){
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		Long l =0l;
		List<StockUpdateDTO> lists = null;
		List<SupplierDTO> supplierLists = null;
		Map<String,String> suppliernameMap = new HashMap<String,String>();
		try {
			supplierLists = supplierService.findByState(null);
			lists = updateStockService.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (SupplierDTO supplierDTO : supplierLists) {
			suppliernameMap.put(supplierDTO.getSupplierId(), supplierDTO.getSupplierName());
		}
		for (StockUpdateDTO stockUpdateDTO : lists) {
			Date time = stockUpdateDTO.getUpdateTime();
			l = date.getTime()-time.getTime();
			if (l/1000d/60d/60d-2.0>=0) {
				sb.append(suppliernameMap.get(stockUpdateDTO.getSupplierId()))
					.append("上次更新时间:").append(stockUpdateDTO.getUpdateTime().toLocaleString())
					.append(",").append("\r\n");
			}
		}
		return sb.toString();
	}
}
