package com.shangpin.iog.studio69.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl.Authenticator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrder;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResponse;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResult_type0;
import com.shangpin.iog.studio69.util.SoapXmlUtil;

/**
 * Created by lubaijiang
 */

@Component
public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLog = Logger.getLogger("error");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);

	private static ResourceBundle bdl=null;
	private static String supplierId = "";
	private static String supplierNo = "";

	private static String url = null;
	private static String username = null;
	private static String password = null;

	static {
		if(null==bdl)
			bdl=ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");

		url = bdl.getString("url");
		username = bdl.getString("username");
		password = bdl.getString("password");
	}

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
	public void handleSupplierOrder(OrderDTO orderDTO) {

		try {
			// TODO 锁库存逻辑


		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}

	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

		try {
			// TODO 支付逻辑
			String param = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Body>"
					+ "<CreateNewOrder xmlns=\"http://tempuri.org/\">"
					+ "<orderID>20160901001010</orderID>"
					+ "<buyerInfo>"
					+ "<Name>test</Name>"
					+ "<Address>shangpin</Address>"
					+ "<Mobile>15101515421</Mobile>"
					+ "<zipcode>100000</zipcode>"
					+ "<Country>china</Country>"
					+ "</buyerInfo>"
					+ "<goodsList>"
					+ "<Good>"
					+ "<ID>32400</ID>"
					+ "<Size>48</Size>"
					+ "<Qty>1</Qty>"
					+ "<Price>305</Price>"
					+ "</Good>"
					+ "</goodsList>"
					+ "</CreateNewOrder>" + "</soap12:Body>" + "</soap12:Envelope>";
			System.out.println("下单参数=================\n"+param);
			logger.info("下单参数=================\n"+param);
			String json = null;
			Map<String, String> map = new HashMap<String, String>();
			map.put("SOAPAction", "http://tempuri.org/CreateNewOrder");
			map.put("Content-Type", "application/soap+xml; charset=utf-8");
			System.out.println("=================tables fetch begin====================================");
			try {
				json = HttpUtil45
						.operateData(
								"post",
								"soap",
								"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=CreateNewOrder",
								new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
										1000 * 60 * 10), map, param, "SHANGPIN", "2MWWKgNSxgf");
				System.out.println("返回的结果===============\n"+json);
				logger.info("返回的结果===============\n"+json);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}

	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {

		try {
			// TODO 退单逻辑


		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}

	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {

		try {
			// TODO 退款逻辑


		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
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

	public static void main(String[] args) {
//		OrderService o =  new OrderService();
//		o.handleConfirmOrder(null);

		try {
			API_STUDIO69Stub stub = new API_STUDIO69Stub();
			CreateNewOrder createNewOrder = new CreateNewOrder();
			String buyerInfo = "<buyerInfo>"
					+ "<Name>test</Name>"
					+ "<Address>shangpin</Address>"
					+ "<zipcode>100000</zipcode>"
					+ "<Corriere></Corriere>"
					+ "<Notes></Notes>"
					+ "</buyerInfo>";
			String goodsList = "<GoodsList>"
					+ "<Good>"
					+ "<ID>32400</ID>"
					+ "<Size>48</Size>"
					+ "<Qty>1</Qty>"
					+ "<Price>305</Price>"
					+ "</Good>"
					+ "</GoodsList>";
			createNewOrder.setBuyerInfo(buyerInfo);
			createNewOrder.setGoodsList(goodsList);
			createNewOrder.setOrderID("20160901001010");
			CreateNewOrderResponse  response = stub.createNewOrder(createNewOrder);
			OMElement   ddd = response.getCreateNewOrderResult().getExtraElement();
			System.out.println(ddd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
