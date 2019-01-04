package com.shangpin.iog.prestashop.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.prestashop.order.Address;
import com.shangpin.iog.prestashop.order.Cart;
import com.shangpin.iog.prestashop.order.CartRow;
import com.shangpin.iog.prestashop.order.CartRows;
import com.shangpin.iog.prestashop.order.Customer;
import com.shangpin.iog.prestashop.order.Order;
import com.shangpin.iog.prestashop.order.OrderHistory;
import com.shangpin.iog.prestashop.order.OrderRow;
import com.shangpin.iog.prestashop.order.OrderRows;

/**
 * Created by 赵根春 on 2015/9/25.
 */
public class OrderService {
	
//	private static String url = "https://phpstack-58351-275778.cloudwaysapps.com";
//	private static String token = "F18KZ74JCMHJYRT8S9G3NZPJZPY3ZP8G";
//	private static String secure_key = "2b9c6a88d913d8d2bb9dfd65b6e1db92";
//	private static String customerId = "1013";
//	private static String addressId = "1031";
//	private static String spuId = "9987";
//	private static String skuId = "43497";
//	8888f2fbae973bfaad82ee637a7e135b
	
	private static String url = "https://www.coccolebimbi.com";
	private static String token = "PD7ISG5F4EVQM11KEJ1CJF8N469B9WWK";
	private static String secure_key = "2176c4be0df583510672d42c006dd3d1";
	private static String customerId = "1071";
	private static String addressId = "1267";
	private static String spuId = "28468";
	private static String skuId = "64646";
	public static void main(String[] args) throws Exception {
		
		System.out.println(getAddressId());
		String cartId = null;
		cartId = getCartId();
		System.out.println(cartId);
		Order order = getOrder(cartId);
		System.out.println(order);
		updateOrderHistoryStatus("706");
	}
	
	
	private static String updateOrderHistoryStatus(String id) throws Exception {
		try{
			com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
			String xml = null;
			 OrderHistory history = new OrderHistory();
			 history.setId_order_state("29");
			 history.setId_order(id);
			pre.setOrderHistory(history);
			xml = ObjectXMLUtil.obj2Xml(pre);
			String json = getString(xml,url + "/api/order_histories");
			System.out.println(json);
			pre = ObjectXMLUtil.xml2Obj(com.shangpin.iog.prestashop.order.Prestashop.class, json);
			if(pre!=null){
				return pre.getOrderHistory().getId();	
			}else{
				return null;
			}
		}catch(Exception e){
			updateOrderHistoryStatus(id);
		}
		return id;
		
	}
	private static String getCartId() throws Exception{
		com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
		String xml = null;
		Cart cart = new Cart();
		cart.setId_currency("1");
		cart.setId_lang("1");
		com.shangpin.iog.prestashop.order.Associations associations = new com.shangpin.iog.prestashop.order.Associations();
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
		cart.setDate_add("2017-07-25");
		cart.setDate_upd("2017-07-25");
		pre.setCart(cart);
		xml = ObjectXMLUtil.obj2Xml(pre);
		url = url+ "/api/carts";
		String json = getString(xml,url);
//		System.out.println(json);
		pre = ObjectXMLUtil.xml2Obj(com.shangpin.iog.prestashop.order.Prestashop.class, json);
		if(pre!=null){
			return pre.getCart().getId();	
		}else{
			return null;
		}
	}

	private static Order getOrder(String cartId) throws Exception{
		com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
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
		com.shangpin.iog.prestashop.order.Associations associations = new com.shangpin.iog.prestashop.order.Associations();
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
		url = "http://phpstack-58351-275778.cloudwaysapps.com/api/orders";
		xml = ObjectXMLUtil.obj2Xml(pre);
		System.out.println(xml);
		String json = getString(xml,url);
		System.out.println(json);
		pre = ObjectXMLUtil.xml2Obj(com.shangpin.iog.prestashop.order.Prestashop.class, json);
		if(pre!=null){
			return pre.getOrder();	
		}else{
			return null;
		}
	}
	
	
	private static String updateOrderStatus(String orderId) throws Exception{
		com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
		String xml = null;
		Order order = new Order();
		order.setCurrent_state("29");
		order.setId(orderId);
		pre.setOrder(order);
		url = "http://phpstack-58351-275778.cloudwaysapps.com/api/orders/"+order.getId();
		xml = ObjectXMLUtil.obj2Xml(pre);
		System.out.println(xml);
		String json = getString(xml,url);
		System.out.println(json);
		return json;
	}
	
	private static String getString(String xml,String url) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "text/xml; charset=UTF-8");
		return HttpUtil45.operateData("post","soap", url, new OutTimeConfig(1000 * 60 * 3,
				1000 * 60 * 30, 1000 * 60 * 30), map, xml, token, null);
	}
	
	
	private static String createCustomer() throws Exception{
		com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
		String xml = null;
		Customer customer = new Customer();
		customer.setEmail("zhaogenchun@shangpin.com");
		customer.setFirstname("shangpin");
		customer.setId_default_group("1");
		customer.setLastname("shangpin");
		customer.setPasswd("shangpin@123");
		pre.setCustomer(customer);
		xml = ObjectXMLUtil.obj2Xml(pre);
		url = url+ "/api/customers";
//		System.out.println(xml);
		String json = getString(xml,url);
		System.out.println(json);
		pre = ObjectXMLUtil.xml2Obj(com.shangpin.iog.prestashop.order.Prestashop.class, json);
		if(pre!=null){
			secure_key = pre.getCustomer().getSecure_key();
			return pre.getCustomer().getId();	
		}else{
			return null;
		}
	}
	private static String getAddressId() throws Exception{
		com.shangpin.iog.prestashop.order.Prestashop pre = new com.shangpin.iog.prestashop.order.Prestashop();
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
		address.setDate_add("2017-07-26");
		address.setDate_upd("2017-07-26");
		pre.setAddress(address);
		xml = ObjectXMLUtil.obj2Xml(pre);
		url = url+"/api/addresses";
//		System.out.println(xml);
		String json = getString(xml,url);
		System.out.println(json);
		pre = ObjectXMLUtil.xml2Obj(com.shangpin.iog.prestashop.order.Prestashop.class, json);
		if(pre!=null){
			return pre.getAddress().getId();	
		}else{
			return null;
		}
	}
}
