package com.shangpin.iog.creative99.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.TokenService;
@Component
public class CreativeOrderServiceImpl extends AbsOrderService{
	@Autowired
	SkuPriceService skuPriceService;
	
	@Autowired
	TokenService tokenService;
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
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {

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
	
	public static void main(String[] args) {
		//创建订单
		Map<String, String> param = new HashMap<String, String>();
		param.put("ID_ORDER_WEB", "20160309110701");
		//TODO 参数设置？
		param.put("ID_Customer_Web", "201603091107");
		param.put("DESTINATIONROW1", "1");
		param.put("DESTINATIONROW2", "2");
		param.put("DESTINATIONROW3", "3");
		param.put("BARCODE", "2103322576302");
		param.put("QTY", "1");
		param.put("PRICE", "178");
		OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10);
		String url="http://79.60.136.177/ws_sito/ws_sito_p15.asmx/";
		String postAuth = HttpUtil45.postAuth(url+"NewOrder", param, outTimeConfig, "shangpin", "creative99");
		System.out.println(postAuth);

	}
}
