package com.shangpin.iog.itemInfo_purchase.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.itemInfo.utils.SoapXmlUtil;

/**
 * 下订单类，并且完成在线验证
 * @author sunny
 *
 */
@Component("alducaOrder")
public class OrderSreviceImpl extends AbsOrderService {
	
	@Autowired
    com.shangpin.iog.service.OrderService productOrderService;
	
	private static String serviceUrl = "";
	private static String sopAction = "";
	private static String contentType = ""; //或者    application/soap+xml; charset=utf-8
	private static String identifier = "";
	
	private static String fromEmail = "";
	private static String toEmail = "";
	private static String emailPass = "";
	
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
    
    private static String deleteOrderUrl = "";
    private static String deleteOrderSop = "";
   
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        serviceUrl = bdl.getString("serviceUrl");
        sopAction = bdl.getString("sopAction");
        contentType = bdl.getString("contentType");
        identifier = bdl.getString("identifier");
        fromEmail = bdl.getString("fromEmail");
        toEmail = bdl.getString("toEmail");
        emailPass = bdl.getString("emailPass");
        
        deleteOrderUrl = bdl.getString("deleteOrderUrl");
        deleteOrderSop = bdl.getString("deleteOrderSop");
        
    }
    
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
	
    /**
	 * 下订单
	 */
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO spOrder) {
		spOrder.setStatus(OrderStatus.PLACED);
	}
	

	/**
	 * 在线推送订单，已支付
	 */
	@Override
	public void handleConfirmOrder(OrderDTO spOrder) {
		String order = spOrder.getSpPurchaseNo();
		try{
			
			String[] sku_stocks = spOrder.getDetail().split(",");
			for(String sku_stock : sku_stocks){
				String[] tem = sku_stock.split(":");
				String soapRequestData =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
									"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
									"  <soap12:Body>"+
									"    <InsertOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
									"      <Identifier>"+identifier+"</Identifier>"+
									"      <Order>"+order+"</Order>"+     //"+order+"
									"      <SkuID>"+tem[0].trim()+"</SkuID>"+
									"      <Quantity>"+tem[1].trim()+"</Quantity>"+  //
									"    </InsertOrder>"+
									"  </soap12:Body>"+
									"</soap12:Envelope>";
				logger.info("推送的订单=========="+soapRequestData);
				try{
					
					String str = SoapXmlUtil.getSoapXml(serviceUrl, sopAction, contentType, soapRequestData);
					logger.info("返回的结果========" +str);
					if(StringUtils.isNotBlank(str)){ 
						String startStr = "<InsertOrderResult>";
						String endStr = "</InsertOrderResult>";
						String retMessage = str.substring(str.indexOf(startStr)+startStr.length(), str.indexOf(endStr));
						logger.info("retMessage===="+retMessage);						
						if(retMessage.toUpperCase().equals("OK")){//下单成功
							spOrder.setStatus(OrderStatus.CONFIRMED);
						}else{//下单失败，无库存
							spOrder.setExcState("0");
							spOrder.setExcDesc("下单失败："+retMessage);
							doOrderExc(spOrder);
						}
					}else{
						spOrder.setExcState("0");
						spOrder.setExcDesc("采购单："+order+" 在请求验证时发生错误，未返回响应信息");
						loggerError.error("采购单："+order+" 在请求验证时发生错误，未返回响应信息");
						doOrderExc(spOrder);
					}
					
				}catch(Exception e){
					//下单失败
					spOrder.setExcState("0");
					spOrder.setExcDesc("发生异常:"+e.getMessage());
		            loggerError.error("发生异常:"+e.getMessage());
		            e.printStackTrace();
		            doOrderExc(spOrder);
				}
			}
			
		}catch(Exception ex){
			//下单失败
			spOrder.setExcState("0");
			spOrder.setExcDesc(ex.getMessage());
            loggerError.error(ex);
            ex.printStackTrace();
            doOrderExc(spOrder);
		}
		
	}
	
	/**
	 * 采购异常处理
	 * @param orderDTO
	 */
	public void doOrderExc(OrderDTO orderDTO){
		String reResult = setPurchaseOrderExc(orderDTO);
		if("-1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setStatus(OrderStatus.CANCELLED);
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {		
		
		//发邮件
//		try {
//			String content = "退款订单编号："+deleteOrder.getSpOrderId()+"\n"
//							+"采购单号："+deleteOrder.getSpPurchaseNo()+"\n"
//							+"时间："+deleteOrder.getCreateTime()+"\n"
//							+"供货商的订单编号："+deleteOrder.getSupplierOrderNo();
//			SendMail.sendGroupMail("smtp.shangpin.com",fromEmail,emailPass,toEmail,
//					"alducadaosta退款订单",
//					content,"text/html;charset=utf-8");
//		} catch (MessagingException e) {
//			loggerError.error(e);
//			e.printStackTrace();
//		}
		
		String order = deleteOrder.getSpPurchaseNo();		
		
		try{
			String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
			String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
					"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
					"<soap:Body>"+
					"<DeleteOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
					"<Identifier>"+identifier+"</Identifier>"+
					"<Order>"+order+"</Order>"+
					"<SkuID>"+skuId+"</SkuID>"+
					"</DeleteOrder>"+
					"</soap:Body>"+
					"</soap:Envelope>";
			logger.info("退款订单=========="+soapRequestData);
			String str = SoapXmlUtil.getSoapXml(deleteOrderUrl, deleteOrderSop, contentType, soapRequestData);
			logger.info("退单返回信息===="+str);
			String startStr = "<DeleteOrderResult>";
			String endStr = "</DeleteOrderResult>";
			String retMessage = str.substring(str.indexOf(startStr)+startStr.length(), str.indexOf(endStr));
			logger.info("截取的信息===="+retMessage);						
			if(retMessage.toUpperCase().equals("OK")){//退款成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);
			}else{//退款失败
				deleteOrder.setExcDesc(retMessage);
				deleteOrder.setExcState("1");
				deleteOrder.setExcTime(new Date());
			}
			
		}catch(Exception ex){ 
			deleteOrder.setExcDesc(ex.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			loggerError.error(ex);
		}
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}
	
//	public static void main(String[] args){
//		OrderSreviceImpl o = new OrderSreviceImpl();
//		ReturnOrderDTO spOrder = new ReturnOrderDTO();
//		spOrder.setSpPurchaseNo("CGD2016021300");
//		spOrder.setDetail("203275406502:1");
//		o.handleRefundlOrder(spOrder);
//		
//	}
}
