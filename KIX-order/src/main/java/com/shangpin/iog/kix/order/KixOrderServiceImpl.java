package com.shangpin.iog.kix.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.kix.dto.Customer;
import com.shangpin.iog.kix.dto.Data;
import com.shangpin.iog.kix.dto.Order;
import com.shangpin.iog.kix.dto.OrderDetail;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.TokenService;
@Component
public class KixOrderServiceImpl extends AbsOrderService{
	@Autowired
	SkuPriceService skuPriceService;
	
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
	}
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		Gson gson = new Gson();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
		String qty = orderDTO.getDetail().split(",")[0].split(":")[1];
		String jsonValue = genOrderStr("pending", qty, skuId);
		logger.info("发送订单信息为"+jsonValue);
		try {
			String operateData = HttpUtil45.operateData("post", "json", url, outTimeConf , null, jsonValue , null, null);
			logger.info("返回订单信息为:"+operateData);
			System.out.println(operateData);
			orderDTO.setExcState("0");
			orderDTO.setStatus(OrderStatus.PLACED);
			Data fromJson = gson.fromJson(operateData, Data.class);
			orderDTO.setSupplierOrderNo(fromJson.getOrder().getId());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			if (e.getMessage().contains("422")) {
				logger.info("库存不足，创建订单失败。订单信息："+jsonValue);
				System.out.println("库存不足，创建订单失败。订单信息："+jsonValue);
				orderDTO.setExcDesc("订单失败，库存不足"+e.getMessage());
				orderDTO.setExcTime(new Date());
				orderDTO.setExcState("1");
			}else{
				logger.info("网络原因，创建订单失败。订单信息："+jsonValue);
				System.out.println("网络原因，创建订单失败。订单信息："+jsonValue);
			}
		}
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+orderDTO.getSupplierOrderNo()+".json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String jsonValue = "{\"order\":{\"id\":"+orderDTO.getSupplierOrderNo()+",\"note\":\""+orderDTO.getSpPurchaseNo()+"have been paid\"}}";
		logger.info("付款修改订单信息为"+jsonValue);
		try {
			//不会有库存不足的情况，全部是网络异常支付失败，先判断异常状态
			if (!orderDTO.getExcState().equals("1")) {
				String operateData = HttpUtil45.operateData("put", "json", url, outTimeConf , null, jsonValue , null, null);
				System.out.println(operateData);
				logger.info("付款修改成功，返回信息"+operateData);
				//确认订单成功
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.CONFIRMED);
			}
		} catch (ServiceException e) {
			//确认订单失败
			logger.info("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+e.getMessage());
			orderDTO.setExcDesc("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+e.getMessage());
			orderDTO.setExcTime(new Date());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+deleteOrder.getSupplierOrderNo()+"/cancel.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Map<String, String> param = new HashMap<String, String>();
		param.put("restock", "true");
		param.put("reason", "customer cancle order");
		String post = "";
		try {
			post = HttpUtil45.post(url, param , outTimeConf);
			//取消订单成功
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.CANCELLED);
			logger.info("取消订单成功，返回信息:"+post);
		} catch (Exception e) {
			//取消订单失败
			logger.info("post请求取消订单失败,orderNo:"+deleteOrder.getSupplierOrderNo()+e.getMessage());
			deleteOrder.setExcDesc("post请求取消订单失败,orderNo:"+deleteOrder.getSupplierOrderNo());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			e.printStackTrace();
		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
//		/admin/orders/#{id}/transactions.json
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+deleteOrder.getSupplierOrderNo()+"/transactions.json";
		String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
		SkuDTO skuDTO = null;
		try {
			skuDTO = skuPriceService.findSupplierPrice(supplierId, skuId);
		} catch (ServiceException e) {
			logger.info("退款查询价格失败，sku："+skuId+"错误信息："+e.toString());
		}
		String price = skuDTO.getMarketPrice();
//		String price = "0";
		String transfaction = "{\"transaction\":{\"kind\":\"refund\",\"amount\":\""+price+"\"}}";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		try {
			String operateData = HttpUtil45.operateData("put", "json", url, outTimeConf , null, transfaction , null, null);
			logger.info("退款成功，返回信息："+operateData);
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.REFUNDED);
			//退款取消订单成功
		} catch (ServiceException e) {
			logger.info("退款失败，错误信息："+e.toString());
			deleteOrder.setExcDesc("退款取消订单失败"+e.toString());
			deleteOrder.setStatus(OrderStatus.REFUNDED);
			deleteOrder.setExcTime(new Date());
			deleteOrder.setExcState("1");
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	//采购异常处理
	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
		String result = setPurchaseOrderExc(orderDTO);
		if("-1".equals(result)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}
	
	private void sendMail(OrderDTO orderDTO) {
		logger.info("处理采购异常 orderNo:"+orderDTO.getSupplierOrderNo());
		try{
			long tim = 60l;
			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){ 
				
				String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}else{
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info("订单超时" + x.getMessage());
		}
		
	}
	private String genOrderStr(String status,String qty,String id){
		Customer customer = new Customer("2692871043");
		
		Order order = new Order();
		order.setInventory_behaviour("decrement_obeying_policy");
		order.setFinancial_status(status);
		order.setCustomer(customer);
		List<OrderDetail> line_items = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setQuantity(qty);
		orderDetail.setVariant_id(id);
		line_items.add(orderDetail);
		order.setLine_items(line_items );
		
		Data data = new Data(order);
		Gson gson = new Gson();
		String json = gson.toJson(data);
		return json;
	}
	private void getOrderdata(String orderId){
//		/admin/orders/#{id}.json
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+orderId+".json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String string = HttpUtil45.get(url, outTimeConf, null);
		System.out.println(string);
	}
	private void cancleOrder(String id){
		///admin/orders/#{id}/cancel.json
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+id+"/cancel.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Map<String, String> param = new HashMap<String, String>();
		param.put("restock", "true");
		param.put("reason", "teststeststeststeststeststeststests");
		String post = HttpUtil45.post(url, param , outTimeConf);
		System.out.println(post);
		
	}
	private void modifyOrderStatus(String orderId){
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+orderId+".json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String jsonValue = "{\"order\":{\"id\":"+orderId+",\"note\":\"have been paid\"}}";
		try {
			String operateData = HttpUtil45.operateData("put", "json", url, outTimeConf , null, jsonValue , null, null);
			System.out.println(operateData);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	private void createNewCustomer(){
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/customers/2692871043.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String jsonValue ="{\"customer\":{\"last_name\":\"shangpin\"}}";
		System.out.println(jsonValue);
		try {
			String operateData = HttpUtil45.operateData("put", "json", url, outTimeConf , null, jsonValue , null, null);
			System.out.println(operateData);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("16344216579:1,");
//		new KixOrderServiceImpl().handleSupplierOrder(orderDTO );
		
		ReturnOrderDTO returnOrderDTO = new ReturnOrderDTO();
		returnOrderDTO.setSupplierOrderNo("2536954115");
		new KixOrderServiceImpl().handleRefundlOrder(returnOrderDTO);
		
//		String str = new KixOrderServiceImpl().genOrderStr("pending", "1", "16002102659");
//		System.out.println(str);
//		new KixOrderServiceImpl().getOrderdata("2536551811");
//		new KixOrderServiceImpl().cancleOrder("2536950851"); 
//		new KixOrderServiceImpl().createNewCustomer();
//		new KixOrderServiceImpl().modifyOrderStatus("2536551811");
		
		
/*		Data data = new Data();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/2530910979/cancel.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Order order = new Order();
		Gson gson = new Gson();
		data.setOrder(order);
		String json = gson.toJson(data);
		order.setRestock("ture");
		order.setReason("testtesttesttesttest");
		try {
			String operateData = HttpUtil45.operateData("post", "json", url, outTimeConf , null, json , null, null);
			System.out.println(operateData);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		*/
//		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders.json";
//		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
//		String jsonValue = new KixOrderServiceImpl().genOrderStr("pending", "1", "16344216579");
//		System.out.println(jsonValue);
//		try {
//			String operateData = HttpUtil45.operateData("post", "json", url, outTimeConf , null, jsonValue , null, null);
//			System.out.println(operateData);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
	}
}
