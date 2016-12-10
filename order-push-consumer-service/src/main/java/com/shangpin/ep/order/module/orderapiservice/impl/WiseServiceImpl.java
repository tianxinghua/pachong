package com.shangpin.ep.order.module.orderapiservice.impl;


//import com.shangpin.framework.ServiceException;
//import com.shangpin.iog.common.utils.SendMail;
//import com.shangpin.iog.dto.OrderDTO;
//import com.shangpin.iog.dto.ProductDTO;
//import com.shangpin.iog.dto.ReturnOrderDTO;
//import com.shangpin.iog.ice.dto.OrderStatus;
//import com.shangpin.iog.service.ProductSearchService;
//import com.shangpin.iog.service.SkuPriceService;
import org.springframework.stereotype.Component;

		import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
		import com.shangpin.ep.order.module.orderapiservice.IOrderService;

@Component("wiseServiceImpl")
public class WiseServiceImpl implements IOrderService {

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	/*private static Logger logger = Logger.getLogger("info");
	private static Logger errorLogs = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;

	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
//	private static String subject = null;
	private static String messageType = null;
//	@Autowired
//	SkuPriceService skuPriceService;
	
//	@Autowired
//    ProductSearchService productSearchService;

//	@Autowired
//	com.shangpin.iog.service.OrderService productOrderService;

	private static Map<String, String> map = new HashMap<String, String>();
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("wise");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		fromUserPassword = bdl.getString("fromUserPassword");
		from = bdl.getString("from");
		to = bdl.getString("to");
//		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
		smtpHost = bdl.getString("smtpHost");
	}

//	public void loopExecute() {
//		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
//	}
//
//	public void confirmOrder() {
//		this.confirmOrder(supplierId);
//
//	}

	*//**
	 * 锁库存
	 *//*
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.PLACED);
		orderDTO.setPushStatus(OrderStatus.PLACED);
		orderDTO.setLockStockTime(new Date());
	}

	*//**
	 * 推送订单
	 *//*
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {	
			logger.info("开始推送的订单==="+orderDTO.getSpPurchaseNo());
			sendMail(orderDTO);
			orderDTO.setExcState("0");	
			orderDTO.setStatus(OrderStatus.CONFIRMED);
			orderDTO.setConfirmTime(new Date());
			orderDTO.setPushStatus(OrderStatus.CONFIRMED);
			logger.info("=============订单推送成功===========");
		} catch (Exception e) {
			e.printStackTrace();
			errorLogs.error(e);
			orderDTO.setExcState("1");
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcTime(new Date());
			errorLogs.error("推送失败的订单======="+orderDTO.getSpPurchaseNo()); 
		}
		
	}

	*//**
	 * 解除库存锁
	 *//*
	@Override
	public void handleCancelOrder(final ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		// 超过一天 不需要在做处理 订单状态改为其它状体
		deleteOrder.setStatus(OrderStatus.CANCELLED);
//		sendMailOfReturnedOrder(deleteOrder);
	}

	*//**
	 * 获取真正的供货商编号
	 *
	 * @param skuMap
	 *            key skuNo ,value supplerSkuNo
	 * @return
	 * @throws ServiceException
	 *//*
//	@Override
//	public void getSupplierSkuId(Map<String, String> skuMap)
//			throws ServiceException {
//
//	}

	
	 * detail数据格式： skuId:数量,skuId:数量
	 

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub		
		try {
			
			logger.info("开始推送退单++++"+deleteOrder.getSpPurchaseNo()); 
			sendMailOfReturnedOrder(deleteOrder);
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.REFUNDED);
			logger.info("+++++++++++退单推送成功+++++++++++++"); 
		} catch (Exception e) {
			e.printStackTrace();
			deleteOrder.setExcState("1");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcTime(new Date());
			errorLogs.error(e);
			errorLogs.error("推送失败的退单+++++++++"+deleteOrder.getSpPurchaseNo()); 
		}
	}

//	@Override
//	public void handleEmail(OrderDTO orderDTO) {
//	}
	
	*//**
	 * 推送订单
	 * @param orderDTO
	 *//*
	private void sendMail(OrderDTO orderDTO) throws Exception {
		
		String detail = orderDTO.getDetail();
		String string = detail.split(",")[0];
		String skuId = string.split(":")[0];
		ProductDTO product = productSearchService.findProductForOrder(supplierId,skuId);
		String subject = "wise-order-shangpin";
		//采购单号 尺码  skuId 货号  barcode 数量    
		String messageText ="Shangpin OrderNo: "+orderDTO.getSpPurchaseNo()+"<br>"+
							"ProductSize: "+product.getSize()+"<br>"+
							"SpuId-SkuId: "+skuId+"<br>"+
							"StyleCode-ColorCode: "+(null != product.getProductCode()? product.getProductCode():"")+"<br>"+
							"Barcode: "+skuId.substring(skuId.indexOf("-")+1)+"<br>"+
							"Qty: "+string.split(":")[1]+"<br>"+
							"Status: confirmed";
		SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, messageText , messageType);
			
	}
	
	*//**
	 * 推动退单
	 * @param orderDTO
	 *//*
	private void sendMailOfReturnedOrder(ReturnOrderDTO deleteOrder) throws Exception {
		
		String detail = deleteOrder.getDetail();
		String string = detail.split(",")[0];
		String skuId = string.split(":")[0];
		ProductDTO product = productSearchService.findProductForOrder(supplierId,skuId);
		String subject = "wise-cancelled order-shangpin";
		//采购单号 尺码  skuId 货号  barcode 数量    
		String messageText ="Shangpin OrderNo: "+deleteOrder.getSpPurchaseNo()+"<br>"+
							"ProductSize: "+product.getSize()+"<br>"+
							"SpuId-SkuId: "+skuId+"<br>"+
							"StyleCode-ColorCode: "+(null != product.getProductCode()? product.getProductCode():"")+"<br>"+
							"Barcode: "+skuId.substring(skuId.indexOf("-")+1)+"<br>"+
							"Qty: "+string.split(":")[1]+"<br>"+
							"Status: cancelled";;
		SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, messageText , messageType);
		
	}
	public static void main(String[] args) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("asdf:1,");
		orderDTO.setSpPurchaseNo("CGD201607120001");
	}*/
}
