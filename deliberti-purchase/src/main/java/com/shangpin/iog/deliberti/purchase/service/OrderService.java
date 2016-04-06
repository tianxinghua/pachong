package com.shangpin.iog.deliberti.purchase.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.common.utils.DateTimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSearchService;
@Component
public class OrderService extends AbsOrderService {
	
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String url = null;
	
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
	
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
			supplierId = bdl.getString("supplierId");
			supplierNo = bdl.getString("supplierNo");
			url = bdl.getString("url");
		}
	}
	
	@Autowired
    ProductSearchService productSearchService;
	
	
	// 下单处理
	public void startWMS() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	// 订单确认处理
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}
	
	
	
	private void createOrder(OrderDTO orderDTO) {
		
		try {
//			ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getDetail().split(":")[0]);
			BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			String price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			//if(product!=null){
				StringBuffer sb = new StringBuffer();
				String detail[] = orderDTO.getDetail().split(":");
			    if(null==detail) {
					orderDTO.setExcState("1");
					orderDTO.setExcDesc("产品信息不正确");
					orderDTO.setExcTime(new Date());
				}else{
					String skuDetail[]  = detail[0].split("-");
					if(null==skuDetail){
						orderDTO.setExcState("1");
						orderDTO.setExcDesc("产品信息不正确");
						orderDTO.setExcTime(new Date());
					}else{


						String date = DateTimeUtil.convertFormat(new Date(),"dd/MM/yyyy");
						sb.append("1;13618;13618;shangpin;shangpin;wangsaying@shangpin.com;S;;0;123123;VIA CUPA;NAPOLI;80131;NA;3348053248;Italy;IT;2;")
								.append(date).append("|");
						sb.append("2;Via Leopardi 27, 22075 Lurate Caccivio (CO)|");
						sb.append("3;").append(detail[1]).append(";").append(skuDetail[0]).append(";");
						sb.append(skuDetail[1]).append(";").append(price).append("|");
						//sb.append("3;1;235850;38;79|");
						sb.append("4;Totale Prodotti (iva inclusa):;").append(price).append("|");
						sb.append("4;Corriere Espresso SDA/DHL:;0|4;Contributo per contrassegno:;0|4;Totale Ordine (iva inclusa):;0|");
						sb.append("9;").append(orderDTO.getSpOrderId()).append("|");
						//sb.append("9;3400988|");
						String rtnData = null;
						String jsonValue = sb.toString();
						logger.info("推送参数 ：" + jsonValue);
						try {
							rtnData = HttpUtil45.operateData("post", "json", url, defaultConfig, null, jsonValue, "", "");
							logger.info("返回结果 ：" + rtnData);
							if("ok".equals(rtnData)){
								orderDTO.setExcState("0");
								orderDTO.setStatus(OrderStatus.CONFIRMED);
							}else if("not ok".equals(rtnData)){//sell out

							}else{
								orderDTO.setExcState("1");
								orderDTO.setExcDesc(rtnData);
								orderDTO.setExcTime(new Date());;
							}
						} catch (ServiceException e1) {
							orderDTO.setExcState("1");
							orderDTO.setExcDesc(e1.getMessage());
							orderDTO.setExcTime(new Date());;
						}
					}


				}

//			}else{
//				
//			}
			
		} catch (Exception e) {
			orderDTO.setExcState("1");
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcTime(new Date());
		}
		
		
		
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setStatus(OrderStatus.PLACED);
		
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		createOrder(orderDTO);

		
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

}
