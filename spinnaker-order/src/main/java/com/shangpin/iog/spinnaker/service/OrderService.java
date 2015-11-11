package com.shangpin.iog.spinnaker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.shangpin.iog.spinnaker.dto.Order;
import com.shangpin.iog.spinnaker.dto.Parameters;
import com.shangpin.iog.spinnaker.dto.RequestObject;
import com.shangpin.iog.spinnaker.dto.ResponseObject;

import net.sf.json.JSONArray;
import sun.jdbc.odbc.OdbcDef;
@Component
public class OrderService extends AbsOrderService {
	
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String url = null;
	private static String dBContext = null;
	private static String ordQty = null;
	private static Map<String,String> param = null;
	
	
	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
	
    private OutTimeConfig defaultConfig=new OutTimeConfig(1000*2,1000*60,1000*60);
    
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("param");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		url = bdl.getString("url");
		dBContext = bdl.getString("dBContext");
	}
	
	//下单处理
	public void startWMS() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}
	
	//订单确认处理
	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}
	
	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		//先判断库存,库存不足时锁库存失败
//		String stock = null;
//		stock = HttpUtil45.get(url, null, param);
		
//		if(ordQty){
//			
//		}else {
//			orderDTO.setStatus(OrderStatus.PLACED);
//		}
		orderDTO.setStatus(OrderStatus.PLACED);
		
		//orderDTO.setExcState("0");
		//createOrder(OrderStatus.PLACED,orderDTO);
		
	}
	
	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		createOrder(OrderStatus.CONFIRMED, orderDTO);
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
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
	public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private void createOrder(String status,OrderDTO orderDTO){
    	
        //获取订单信息
		Parameters order = getOrder(status,orderDTO);
        Gson gson = new Gson();
       //Order order = new Order();
        
        String json = gson.toJson(order,Parameters.class);
        System.out.println("request json == "+json);
        String rtnData = null;
        logger.info("推送的数据："+json);
        System.out.println("rtnData=="+json);
        try {
        	rtnData = HttpUtil45.get(url, defaultConfig , param);
            //rtnData = HttpUtil45.operateData("get", "json",  url, null, null, json, "", "");
            //{"error":"发生异常错误"}
            logger.info("推送"+status+"订单返回结果=="+rtnData);
            System.out.println("推送订单返回结果=="+rtnData);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	orderDTO.setExcState("1");
                orderDTO.setExcDesc(rtnData);
            	return;
            }
            logger.info("Response ：" + rtnData + ", shopOrderId:"+order.getBarcode());

            ResponseObject responseObject = gson.fromJson(rtnData,ResponseObject.class);
            if ("ko".equals(responseObject.getStatus())){
                orderDTO.setExcState("1");
                orderDTO.setExcDesc(responseObject.getMessage().toString());
            } else if (OrderStatus.PLACED.equals(status)){
                orderDTO.setStatus(OrderStatus.CONFIRMED);
            }
//        } catch (ServiceException e) {
//            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getBarcode());
//            orderDTO.setExcState("1");
//            orderDTO.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getBarcode());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } 
    }
	
	
	 private Parameters getOrder(String status,OrderDTO orderDTO){
	    	
	        String detail = orderDTO.getDetail();
	        
	        String[] details = detail.split(",");
			logger.info("detail数据格式:"+detail);
			int num = 0;
			String skuNo = null;
			for (int i = 0; i < details.length; i++) {
				// detail[i]数据格式==>skuId:数量
				num = Integer.parseInt(details[i].split(":")[1]);
				skuNo = details[i].split(":")[0];
			}
			String markPrice = null;
			try {
				Map tempmap = skuPriceService.getNewSkuPriceBySku(supplierId, skuNo);
				Map map =(Map) tempmap.get(supplierId);
				markPrice =(String) map.get(skuNo);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
//	        ItemDTO[] itemsArr = new ItemDTO[1];
//	        ItemDTO item = new ItemDTO();
//	        item.setQty(num);
//	        item.setSku(skuNo);
//	        double totalPrice = 0;
//	        if(!"-1".equals(markPrice)){
//	        	String price = markPrice.split("\\|")[0];
//	        	item.setPrice(price);	
//	        	totalPrice = (Double.parseDouble(price))*num;
//	        }else{
//	        	logger.info("没有市场价");
//	        }
//	        item.setCur(1);
//	        itemsArr[0] = item;
//	        
//	        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
//	        //运费需要得到
//	        double fees = 0;
//	        shippingInfo.setFees(String.valueOf(fees));
//	        //地址写死就行
//	        AddressDTO address = new AddressDTO();
//	        address.setFirstname("Filippo ");
//	        address.setLastname("Troina");
//	        address.setCompanyname("Genertec Italia S.r.l.");
//	        address.setStreet("VIA G.LEOPARDI 27");	
//	        address.setHn("22075 ");
//	        address.setZip("22075");
//	        address.setCity("LURATE CACCIVIO ");
//	        address.setProvince("como");
//	        address.setState("Italy");
//	        shippingInfo.setAddress(address);
//	        
//	        BillingInfoDTO billingInfo = new BillingInfoDTO();
//	        //fees and the orderTotalPrice
//	        billingInfo.setTotal(totalPrice+fees);
//	        //1:PayPal,2:postal order,3:bank check,4:Visa / Mastercard credit card,5:American Express credit card,6:cash on delivery,7:bank transfer
//	        billingInfo.setPaymentMethod(7);
//	        billingInfo.setAddress(address);
	        
			Parameters order = new Parameters();
	        order.setDBContext(dBContext);
//	        order.setToken(Constant.TOKEN);
//	        order.setShopOrderId(orderDTO.getSpOrderId());
//	        order.setOrderTotalPrice(totalPrice);
//	        order.setStatus(status);
//	        order.setStatusDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
//	        order.setOrderDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
//	        order.setItems(itemsArr);
//	        order.setShippingInfo(shippingInfo);
//	        order.setBillingInfo(billingInfo);
	        return  order;
	    }

}
