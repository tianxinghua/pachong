package com.shangpin.ep.order.module.orderapiservice.impl.prestashop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.order.service.impl.PriceService;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Address;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Associations;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Cart;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.CartRow;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.CartRows;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Customer;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Order;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.OrderHistory;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.OrderRow;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.OrderRows;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Prestashop;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.xml.ObjectXMLUtil;

@Component("coccolebimbiOrderImpl")
public class CoccolebimbiOrderImpl implements IOrderService {

	@Autowired
	LogCommon logCommon;
	@Autowired
	SupplierProperties supplierProperties;
	@Autowired
	HandleException handleException;
	@Autowired
	OpenApiService openApiService;
	@Autowired
	IShangpinRedis iShangpinRedis;
	@Autowired
	PriceService priceService;
	
	private static String url = "https://www.coccolebimbi.com";
	private static String token = "PD7ISG5F4EVQM11KEJ1CJF8N469B9WWK";
	private static String secure_key = "2176c4be0df583510672d42c006dd3d1";
	private static String customerId = "1071";
	private static String addressId = "1267";
	
	private static String CONFIRMED = "28";
	private static String RETURNED = "30";
	
	/**
	 * 锁库存
	 */
	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		try {
			orderDTO.setLockStockTime(new Date());
			String cartId = getCartId(orderDTO.getSupplierSkuNo());
			orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
			orderDTO.setLogContent("锁库返回结果："+cartId+",参数："+orderDTO.getSupplierSkuNo());
			orderDTO.setSupplierOrderNo(cartId);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			orderDTO.setLogContent("锁库返回结果："+e.getMessage()+",参数："+orderDTO.getSupplierSkuNo());
		}
	}

	/**
	 * 推送订单
	 */
	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

		try {
			if(orderDTO.getSupplierOrderNo()!=null){
				Order order = getOrder(orderDTO.getSupplierOrderNo(),orderDTO.getSupplierSkuNo());
				if(order!=null){
					updateOrderHistoryStatus(order.getId(),CONFIRMED);
					orderDTO.setLogContent("confirm返回的结果=" + order.getId() + ",推送的参数=" + orderDTO.getSupplierSkuNo());
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
					orderDTO.setSupplierOrderNo(order.getId());
					orderDTO.setConfirmTime(new Date());
				}
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO, e);
			orderDTO.setLogContent("推送订单异常=" + e.getMessage() + ",推送的参数=" + orderDTO.getSupplierSkuNo());
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}

	}

	/**
	 * 解除库存锁
	 */
	@SuppressWarnings("static-access")
	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date());
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
		deleteOrder.setLogContent("------取消锁库结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
	}

	/**
	 * 退款
	 */
	@SuppressWarnings("static-access")
	@Override
	public void handleRefundlOrder(OrderDTO orderDTO) {
		try {
			String id = updateOrderHistoryStatus(orderDTO.getSupplierOrderNo(),RETURNED);
			orderDTO.setLogContent("refund返回的结果=" + id + ",推送的订单id=" + orderDTO.getSupplierOrderNo());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
			orderDTO.setPushStatus(PushStatus.REFUNDED);
			orderDTO.setRefundTime(new Date());
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
			orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
			handleException.handleException(orderDTO, e);
			orderDTO.setLogContent("退款订单异常========= " + e.getMessage()+ ",推送的订单id=" + orderDTO.getSupplierOrderNo());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		}
	}


	public String handleException(OrderDTO orderDTO, String url, String json, Throwable e) {
		handleException.handleException(orderDTO, e);
		return null;
	}

	/**
	 * 更新订单状态
	 * @param id
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 */
	private  String updateOrderHistoryStatus(String orderId,String orderStatus) throws Exception {
		Prestashop pre = new Prestashop();
		String xml = null;
		 OrderHistory history = new OrderHistory();
		 history.setId_order_state(orderStatus);
		 history.setId_order(orderId);
		pre.setOrderHistory(history);
		xml = ObjectXMLUtil.obj2Xml(pre);
		String json = getString(xml,url + "/api/order_histories");
		System.out.println(json);
		pre = ObjectXMLUtil.xml2Obj(Prestashop.class, json);
		if(pre!=null){
			return pre.getOrderHistory().getId();	
		}
		return null;
	}
	private String getDateFormat(){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		return sim.format(new Date());
		
	}
	/**
	 * 创建购物车，得到购物车Id
	 * @param supplierSkuNo
	 * @return
	 * @throws Exception
	 */
	private  String getCartId(String supplierSkuNo) throws Exception{
		
		String skuId = "";
		String spuId = "";
		if(supplierSkuNo!=null){
			//spuId-skuId-stockId ==>spuId、skuId推送订单，stockId更新库存
			String [] arr = supplierSkuNo.split("-");
			if(arr.length==3){
				spuId = arr[0];
				skuId = arr[1];
			}
		}
		
		Prestashop pre = new Prestashop();
		String xml = null;
		Cart cart = new Cart();
		cart.setId_currency("1");
		cart.setId_lang("1");
		Associations associations = new Associations();
		CartRows cartRows = new CartRows();
		List<CartRow> cart_rows = new ArrayList<CartRow>();
		CartRow cartRow = new CartRow();
		cartRow.setId_product_attribute(skuId);
		cartRow.setId_product(spuId);
		cartRow.setId_address_delivery(addressId);
		cartRow.setQuantity("1");
		cart_rows.add(cartRow);
		associations.setCart_rows(cartRows);
		cartRows.setCart_row(cart_rows);
		cart.setAssociations(associations);
		cart.setSecure_key(secure_key);
		cart.setId_address_delivery(addressId);
		cart.setId_address_invoice(addressId);
		cart.setId_customer(customerId);
		cart.setId_carrier("3");
		cart.setDate_add(getDateFormat());
		cart.setDate_upd(getDateFormat());
		pre.setCart(cart);
		xml = ObjectXMLUtil.obj2Xml(pre);
		String json = getString(xml,url+ "/api/carts");
		pre = ObjectXMLUtil.xml2Obj(Prestashop.class, json);
		if(pre!=null){
			return pre.getCart().getId();	
		}else{
			return null;
		}
	}

	/**
	 * 推送订单，得到订单Id
	 * @param cartId
	 * @param supplierSkuNo
	 * @return
	 * @throws Exception
	 */
	private  Order getOrder(String cartId,String supplierSkuNo) throws Exception{
		
		String skuId = "";
		String spuId = "";
		if(supplierSkuNo!=null){
			//spuId-skuId-stockId ==>spuId、skuId推送订单，stockId更新库存
			String [] arr = supplierSkuNo.split("-");
			if(arr.length==3){
				spuId = arr[0];
				skuId = arr[1];
			}
		}
		
		Prestashop pre = new Prestashop();
		String xml = null;
		Order order = new Order();
		order.setId_address_delivery(addressId);
		order.setId_address_invoice(addressId);
		order.setId_cart(cartId);
		order.setId_currency("1");
		order.setId_lang("1");
		order.setId_customer(customerId);
		order.setId_carrier("3");
		order.setModule("cashondelivery");
		order.setPayment("Cash on delivery");
		order.setSecure_key(secure_key);
		order.setTotal_paid("1");
		order.setTotal_paid_real("1");
		order.setTotal_paid_tax_excl("1");
		order.setTotal_paid_tax_incl("2");
		order.setTotal_products("1");
		order.setTotal_products_wt("1");
		order.setTotal_shipping("1");
		order.setTotal_shipping_tax_excl("1");
		order.setTotal_shipping_tax_incl("1");
		order.setConversion_rate("1");
		order.setValid("0");
		order.setCurrent_state("");
		order.setTotal_discounts("1");
		order.setTotal_discounts_tax_excl("1");
		order.setTotal_discounts_tax_incl("1");
		Associations associations = new Associations();
		OrderRows orderRows = new OrderRows();
		List<OrderRow> order_row = new ArrayList<OrderRow>();
		OrderRow orderRow = new OrderRow();
		orderRow.setId(cartId);
		orderRow.setProduct_attribute_id(skuId);
		orderRow.setProduct_id(spuId);
		orderRow.setProduct_quantity("1");
		order_row.add(orderRow);
		orderRows.setOrder_row(order_row);
		associations.setOrder_rows(orderRows);
		order.setAssociations(associations);
		pre.setOrder(order);
		xml = ObjectXMLUtil.obj2Xml(pre);
		String json = getString(xml,url+"/api/orders");
		pre = ObjectXMLUtil.xml2Obj(Prestashop.class, json);
		if(pre!=null){
			return pre.getOrder();	
		}else{
			return null;
		}
	}
	
	private  String getString(String xml,String url) throws Exception{
		return HttpUtil45.operateData("post","soap", url, new OutTimeConfig(1000 * 60 * 3,
				1000 * 60 * 30, 1000 * 60 * 30), null,xml,null,  token, null);
	}
	
	/**
	 * 创建用户，本地运行一次就可以，保存secure_key、customerId两个参数
	 * @return
	 * @throws Exception
	 */
	private  String createCustomer() throws Exception{
		Prestashop pre = new Prestashop();
		String xml = null;
		Customer customer = new Customer();
		customer.setEmail("zhaogenchun@shangpin.com");
		customer.setFirstname("shangpin");
		customer.setId_default_group("1");
		customer.setLastname("shangpin");
		customer.setPasswd("shangpin@123");
		pre.setCustomer(customer);
		xml = ObjectXMLUtil.obj2Xml(pre);
		String json = getString(xml,url+ "/api/customers");
		pre = ObjectXMLUtil.xml2Obj(Prestashop.class, json);
		if(pre!=null){
			secure_key = pre.getCustomer().getSecure_key();
			return pre.getCustomer().getId();	
		}else{
			return null;
		}
	}
	/**
	 * 创建收货地址，本地运行一次就可以，保存addressId参数
	 * @return
	 * @throws Exception
	 */
	private  String getAddressId() throws Exception{
		Prestashop pre = new Prestashop();
		String xml = null;
		Address address = new Address();
		address.setId_customer(customerId);
		address.setId_country("10");
		address.setAlias("Patrizia");
		address.setAddress1(" Via Leopardi 27");
		address.setAddress2("22075 Lurate Caccivio (CO)");
		address.setLastname("Valle");
		address.setFirstname("Patrizia");
		address.setCity("milano");
		address.setPhone_mobile("0039 031 4149625");
		address.setPostcode("20100");
		address.setDate_add(getDateFormat());
		address.setDate_upd(getDateFormat());
		pre.setAddress(address);
		xml = ObjectXMLUtil.obj2Xml(pre);
		String json = getString(xml,url+"/api/addresses");
		pre = ObjectXMLUtil.xml2Obj(Prestashop.class, json);
		if(pre!=null){
			return pre.getAddress().getId();	
		}else{
			return null;
		}
	}
}
