package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.date.DateTimeUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("delibertiServiceImpl")
public class DelibertiServiceImpl implements IOrderService {
	
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
	
   @Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
	private void createOrder(OrderDTO orderDTO) {
		
		try {
//			ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getDetail().split(":")[0]);
			BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			String price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			//if(product!=null){
				StringBuffer sb = new StringBuffer();
				String detail[] = orderDTO.getDetail().split(":");
			    if(null==detail) {
				}else{
					String skuDetail[]  = detail[0].split("-");
					if(null==skuDetail){
					}else{
						String json = HttpUtil45.get("http://gicos.it/outsrc.php?do=products&cart="+skuDetail[0], new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),null);
						logger.info(skuDetail[0]+"的库存查询结果:"+json);
						boolean flag = true;
						Map map = new HashMap();
						if(!json.equals(HttpUtil45.errorResult)){
							String array [] = json.split("\\|");
							for(int i=14;i<44;i++){
								if(array[i]!=null){
									String stock = null;
									String size = null;
									String si[] = array[i].split("~");
									if(si[0].equals("")){
										continue;
									}else{
										if(si[0].indexOf("x")!=-1){
											 size = si[0].replace("x", ".5");
										}else{
											size = si[0];
										}
									}
									stock = si[1];
									map.put(size, stock);
								}
							}
							logger.info("尺码"+skuDetail[1]+"的库存为:"+map.get(skuDetail[1]));
							if(map.get(skuDetail[1])!=null&& Integer.parseInt(map.get(skuDetail[1]).toString())<=0){
								flag = false;
							}
						}
						if(flag){
							String sku = skuDetail[0];
							String sizeValue = skuDetail[1].replace(".5", "x");
							String date = DateTimeUtil.convertFormat(new Date(),"dd/MM/yyyy");
							sb.append("1;13618;13618;shangpin;shangpin;wangsaying@shangpin.com;S;;0;123123;VIA CUPA;NAPOLI;80131;NA;3348053248;Italy;IT;2;")
									.append(date).append("|");
							sb.append("2;Via Leopardi 27, 22075 Lurate Caccivio (CO)|");
							sb.append("3;").append(detail[1]).append(";").append(sku).append(";");
							sb.append(sizeValue).append(";").append(price).append("|");
							//sb.append("3;1;235850;38;79|");
							sb.append("4;Totale Prodotti (iva inclusa):;").append(price).append("|");
							sb.append("4;Corriere Espresso SDA/DHL:;0|4;Contributo per contrassegno:;0|4;Totale Ordine (iva inclusa):;0|");
							sb.append("9;").append(orderDTO.getSpOrderId()).append("|");
							//sb.append("9;3400988|");
							String rtnData = null;
							String jsonValue = sb.toString();
							logger.info("推送参数 ：" + jsonValue);
							try {
								rtnData = HttpUtil45.operateData("post", "json",supplierProperties.getDeliberti().getUrl(), defaultConfig, null, jsonValue,null, "", "");
								logger.info("返回结果 ：" + rtnData);
								if("ok".equals(rtnData)){
									orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
									orderDTO.setConfirmTime(new Date());
								}else if("not ok".equals(rtnData)){//sell out
									orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
								}else{
									orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
									orderDTO.setDescription(rtnData);
								}
							} catch (Exception e1) {
								orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
								orderDTO.setDescription(e1.getMessage());
							}
						}else{
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setDescription("尺码"+skuDetail[1]+"的库存为:"+map.get(skuDetail[1]));
						}
						
					}
				}

			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription(e.getMessage());
		}
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLockStockTime(new Date());
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		createOrder(orderDTO);
	}
	public static void main(String[] args) {
		OrderDTO o = new OrderDTO();
		o.setPurchasePriceDetail("1");
		o.setPurchaseNo("CGDF2016121484493");
		o.setDetail("256227-37:1");
		o.setSpOrderId("201612145135570");
		new DelibertiServiceImpl().createOrder(o);
	}
	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
		deleteOrder.setCancelTime(new Date());
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API);
		deleteOrder.setRefundTime(new Date());
	}


}
