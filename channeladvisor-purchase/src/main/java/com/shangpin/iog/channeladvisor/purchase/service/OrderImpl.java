package com.shangpin.iog.channeladvisor.purchase.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.channeladvisor.purchase.dto.Item;
import com.shangpin.iog.channeladvisor.purchase.dto.Promotion;
import com.shangpin.iog.channeladvisor.purchase.utils.UtilOfChannel;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;

@Configuration("order")
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String createOrderUrl = null;
	private static String updateOrderUrl = null;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
			supplierId = bdl.getString("supplierId");
			supplierNo = bdl.getString("supplierNo");
			createOrderUrl = bdl.getString("createOrderUrl");
			updateOrderUrl = bdl.getString("updateOrderUrl");
		}
	}

	/**
	 * 下订单
	 */
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, false);
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}

	// public void updateEvent() {
	//
	// }

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

		try{
			
			OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			String newStoken = UtilOfChannel.getNewToken(timeConfig);//获取新的访问令牌
			
			Map<String, String> param = getRequestParam(orderDTO);//构造下订单所需要的参数
			param.put("access_token", newStoken);			
			String result = HttpUtil45.post(createOrderUrl,param, timeConfig);
			System.out.println(result);
			if(StringUtils.isNotBlank(result)){
				JSONObject json = JSONObject.fromObject(result);
				orderDTO.setSupplierId(json.getString("ID"));
			}
			
			
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		
	}

	/**
	 * 构造下订单所需要的参数
	 * @param orderDTO
	 * @return
	 */
	public Map<String, String> getRequestParam(OrderDTO orderDTO) {

		Map<String, String> param = new HashMap<String, String>();

		String detail = orderDTO.getDetail();
		String[] details = detail.split(",");
		int num = 0;
		String skuNo = null;
		for (int i = 0; i < details.length; i++) {
			num = Integer.parseInt(details[i].split(":")[1]);
			skuNo = details[i].split(":")[0];
		}
		String markPrice = null;
		try {
			Map tempmap = skuPriceService
					.getNewSkuPriceBySku(supplierId, skuNo);
			Map map = (Map) tempmap.get(supplierId);
			markPrice = (String) map.get(skuNo);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		List<Item> items = new ArrayList<>();
		Item item = new Item();
		item.setSku(skuNo);
		item.setQuantity(num);
		item.setShippingPrice("0.00");
		List<Promotion> promotions = new ArrayList<>();
		Promotion ppp = new Promotion();
		ppp.setCode("freebie");
		ppp.setShippingAmount("-2");
		promotions.add(ppp);
		item.setPromotions(promotions);
		double totalPrice = 0;
		if (!"-1".equals(markPrice)) {
			String price = markPrice.split("\\|")[0];
			item.setUnitPrice(price);
			totalPrice = (Double.parseDouble(price)) * num;
		} else {
			logger.info("产品sku= " + skuNo + " 没有市场价");
		}
		items.add(item);
		param.put("ProfileID", "qwmmx12wu7ug39a97uter3dz29jbij3j");
		param.put("SiteName", "shangpin");
		param.put("BuyerEmailAddress", "buyer@example.com");
		param.put("TotalShippingPrice", "0");
		param.put("TotalPrice", String.valueOf(totalPrice));
		param.put("Items", JSONArray.fromObject(items).toString());

		return param;
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		//Update an order
		//PATCH https://api.channeladvisor.com/v1/Orders(123456)
		try{
			OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			updateOrderUrl = updateOrderUrl+"("+orderDTO.getSupplierOrderNo()+")";
			Map<String,String> param = new HashMap<String,String>();
			param.put("SellerOrderID", orderDTO.getSpOrderId());
			param.put("access_token", UtilOfChannel.getNewToken(timeConfig));
			String result = HttpUtil45.operateData("patch", null, updateOrderUrl, timeConfig, param, null, null, null);
			System.out.println(result);
			
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			ex.printStackTrace();
		}
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		//Mark an order as not exported
		//DELETE https://api.channeladvisor.com/v1/Orders(123456)/Export

	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub

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
	
	public static void main(String[] args){
		OrderImpl order = new OrderImpl();
		OrderDTO oo = new OrderDTO();
		oo.setDetail("NY-15000:1");
		String str = JSONObject.fromObject(order.getRequestParam(oo)).toString();
		System.out.println(str);
	}

}
