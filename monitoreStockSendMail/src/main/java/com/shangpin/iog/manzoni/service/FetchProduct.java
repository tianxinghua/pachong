package com.shangpin.iog.manzoni.service;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.DTO.PurchaseOrderDetilApiDto;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.IcePrxHelper;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.OrderDetailService;
import com.shangpin.iog.service.OrderService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.UpdateStockService;

/**
 */
@Component("monitoreStockSendMail")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger logError = Logger.getLogger("error");
	private static int hour;
	private static ResourceBundle bdl = null;
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;
	private static String supplierId = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		hour = Integer.valueOf(bdl.getString("hour"));
		supplierId = bdl.getString("supplierId");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		from = bdl.getString("from");
		smtpHost = bdl.getString("smtpHost");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
	}
	  @Autowired
    SupplierService supplierService;
	@Autowired
	OrderDetailService orderService;
	@Autowired
	UpdateStockService updateStockService;
	@Autowired
	SupplierMapper supplierDAO;
	public void fetchProductAndSave() {
		List<StockUpdateDTO> all;
		try {
			all = updateStockService.getAll();
	    	for (StockUpdateDTO stockUpdateDTO : all) {
	    		long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
	    		long days = diff / (1000 * 60 * 60 * 24);
	    		long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	    		long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
	    		String status = stockUpdateDTO.getStatus();
	    		if(status!=null){
	    			if("1".equals(status)){
	    				String toEmail = null;
	    				String supplierId = stockUpdateDTO.getSupplierId();
	    				
	    				String supplierName = "";
		    			try {
		    				SupplierDTO supplier = supplierDAO.findBysupplierId(supplierId);
		    				supplierName = supplier.getSupplierName();
		    			} catch (Exception e) {
						}
	    				
	    				
	    				if(StringUtils.isNotBlank(stockUpdateDTO.getEmail())){
	    					toEmail = stockUpdateDTO.getEmail();
	    				}else{
	    					toEmail = to;
	    				}
	    	    		System.out.println("供应商:"+supplierName+supplierId+"已有"+hours+"小时未更新,邮箱："+toEmail);
		    			if (hours>=hour||days>=1) {
							SendMail.sendGroupMail(smtpHost, from, fromUserPassword, toEmail, subject,"供应商:"+supplierName+supplierId+"库存已有"+hours+"小时"+minutes+"分未更新，请检查", messageType);
						}
		    		}
	    		}
	    		
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmailToSupplier1() {
		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		}catch(Exception e){
			
		}
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = new Date();

		String endTime = dft.format(beginDate);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 24);
		String startTime = dft.format(date.getTime());
		System.out.println("startTime:"+startTime);
		System.out.println("endTime"+endTime);
		int iogTotal = getOrderTotal(supplierId,startTime,endTime);
		logger.info("iog totail order:"+iogTotal);
		System.out.println("iog totail order:"+iogTotal);
		int sopOrderTotal = getTotalNumberFromSopOrder(supplierId,servant,beginDate);
		logger.info("sop totail order:"+sopOrderTotal);
		System.out.println("sop totail order:"+sopOrderTotal);
	}
	
	private int getOrderTotal(String supplierId, String startTime,
			String endTime) {
		
		return orderService.getOrderTotalBySpPurchaseNo(supplierId,startTime,endTime);
		
	}


	private int getTotalNumberFromSopOrder(String supplierId,OpenApiServantPrx servant,Date endDate){

		List<PurchaseOrderDetail> orderDetails = null;
		boolean hasNext=true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String endTime = format.format(endDate);
		String startTime = format.format(getAppointDayFromSpecifiedDay(endDate, -2, "D"));
		System.out.println("sop startTime:"+startTime);
		System.out.println("sop endTime:"+endTime);
		List<java.lang.Integer> statusList = new ArrayList<>();
		for(int i=1;i<=7;i++){
			statusList.add(i);
		}
		int pageIndex=1,pageSize=20;
		PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
				,pageIndex,pageSize);
		int sum = 0;
		for(int i=0;i<2;i++){ 
			try {
				PurchaseOrderDetailPage orderDetailPage=
						servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);
				if(orderDetailPage!=null){
					i=2;
				}
				SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
				String cgdDate = format1.format(endDate);
				List<PurchaseOrderDetail>  detilApiDtos = null;
					detilApiDtos =  orderDetailPage.PurchaseOrderDetails;
					for (PurchaseOrderDetail orderDetail : detilApiDtos) {
						String sopPurchaseNo = orderDetail.SopPurchaseOrderNo;
						if(sopPurchaseNo.indexOf(cgdDate)>0){
							sum += 1;
						}
//						sopPurchaseMap.put(orderDetail.SkuNo,orderDetail.Count);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	public void sendEmailToSupplier2() {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = new Date();
		System.out.println(dft.format(beginDate));
		String endTime = dft.format(beginDate);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 5);
		System.out.println(dft.format(date.getTime()));
		String startTime = dft.format(date.getTime());
//		getOrderList(startTime,endTime,servant);
	}
	private  Date getAppointDayFromSpecifiedDay(Date today,int num,String type){
		Calendar c   =   Calendar.getInstance();
		c.setTime(today);

		if("Y".equals(type)){
			c.add(Calendar.YEAR, num);
		}else if("M".equals(type)){
			c.add(Calendar.MONTH, num);
		}else if(null==type||"".equals(type)||"D".equals(type))
			c.add(Calendar.DAY_OF_YEAR, num);
		else if("H".equals(type))
			c.add(Calendar.HOUR_OF_DAY,num);
		else if("m".equals(type))
			c.add(Calendar.MINUTE,num);
		else if("S".equals(type))
			c.add(Calendar.SECOND,num);
		return c.getTime();
	}
	
	
}
