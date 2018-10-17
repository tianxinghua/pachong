package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.CommonService;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
/**
 * <p>Title: WiseOrderService</p>
 * <p>Description: wise供应商订单api对接 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月22日 上午10:17:10
 *
 */
@Component("wiseServiceImpl")
public class WiseOrderService implements IOrderService {

	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
    private WiseServiceImpl wiseService;
    @Autowired
    private CommonService commonService;
    
    /**
     * 给对方推送数据
     * @param url
     * @param param
     * @param outTimeConf
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public String wisePost(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order) throws Exception{
    	return HttpUtil45.postAuth(url, param, outTimeConf, userName, password);
    }
    /**
     * 异常回调函数
     * @param url
     * @param param
     * @param outTimeConf
     * @param userName
     * @param password
     * @param order
     * @param e
     * @return
     */
    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
    	handleException.handleException(order, e); 
		return null;
    }
	
	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String productSize ="";
		String size ="";
		try {
			
			String spOrderId = orderDTO.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			long id_order_mrkp = Long.valueOf(spOrderId);
			String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];


			int index = skuId.lastIndexOf("-");
			String item_id = null;
			String barcode = null;
			if(index>0){
				item_id = skuId.substring(0,index);
				barcode = skuId.substring(index+1);
			}



			int qty = Integer.valueOf(orderDTO.getDetail().split(",")[0].split(":")[1]);
			//先通过查询库存接口查询库存,如果库存大于0则下单,否则采购异常
			productSize = commonService.getProductSize(orderDTO.getSupplierId(),skuId);
			if(StringUtils.isNotBlank(productSize)){
				 size = productSize.replaceAll("\\+", "½");
				//查询对方库存接口
				orderDTO.setLogContent("查询库存参数============" + item_id); 
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				String stockData = getItemStockBySizeMarketPlace(item_id,orderDTO);
				orderDTO.setLogContent("查询库存返回结果======="+stockData);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				if(!HttpUtil45.errorResult.equals(stockData)){
					int stock = 0;
					String prex = "<string xmlns=\"http://tempuri.org/\">";
					String end = "</string>";
					String stocks = stockData.substring(stockData.indexOf(prex)+prex.length(), stockData.indexOf(end));
					String supplierSize = "";
					for(String size_stock : stocks.split("\\|")){
						if(StringUtils.isNotBlank(size_stock)){
							supplierSize = "";
							supplierSize = size_stock.split(";")[0];
							supplierSize = supplierSize.replaceAll("\\+", "½");
							if(size.equals(supplierSize)){
								stock = Integer.parseInt(size_stock.split(";")[1]);
								orderDTO.setLogContent("查询到的供货商的库存为============"+stock);
								logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
								break;
							}
						}
					}
					//如果库存大于0,则下单
					if(stock > 0){
						//下单
						String returnData = newOrderMarketPlace(id_order_mrkp,barcode,qty,orderDTO);
						
						if (returnData.contains("OK")) {
							orderDTO.setConfirmTime(new Date()); 
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
						} else {
							//推送订单失败
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
							orderDTO.setDescription("下单失败：" + returnData);
						}
					}else{
//						orderDTO.setConfirmTime(new Date());
						orderDTO.setPushStatus(PushStatus.NO_STOCK);
					}
				}else{
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
					orderDTO.setDescription("查询对方库存接口失败,对方返回的信息是："+stockData);
				}
			}else{
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
				orderDTO.setDescription("查询数据库失败,未找到该商品 "+skuId);
				orderDTO.setLogContent("查询数据库失败,未找到该商品=========== "+skuId);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			}
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		/**
		 * 发份邮件
		 */
		wiseService.pushConfirmOrder(orderDTO,size);
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			String spOrderId = deleteOrder.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			String returnData = setStatusOrderMarketplace(spOrderId,"CANCELED",deleteOrder);
			if(returnData.contains("OK")){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				/**
				 * 发邮件
				 * */


				wiseService.handleRefundlOrder(deleteOrder);
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(returnData);
			}				
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("退款发生异常============"+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);			
		}
		
	}

	/**
	 * 根据spu查询供货商库存
	 * @param item_id
	 * @return
	 */
	private String getItemStockBySizeMarketPlace(String item_id,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ITEM_ID", item_id);		
		String returnData = wisePost(supplierProperties.getWise().getUrl()+supplierProperties.getWise().getGetItemStockInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getWise().getUser(),supplierProperties.getWise().getPassword(),orderDTO);
		return returnData;
	}
	
	/**
	 * 下订单新接口
	 * @param id_order_mrkp 订单号
	 * @param barcode Barcode
	 * @param qty 库存
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String newOrderMarketPlace(long id_order_mrkp, String barcode, int qty,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ID_ORDER_MRKP", orderDTO.getSpOrderDetailNo());//String.valueOf(id_order_mrkp)
		param.put("BARCODE", barcode);
		param.put("QTY", String.valueOf(qty));
		orderDTO.setLogContent("下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = wisePost(supplierProperties.getWise().getUrl()+supplierProperties.getWise().getCreateOrderInterface(), param, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1),supplierProperties.getWise().getUser(),supplierProperties.getWise().getPassword(),orderDTO);
		orderDTO.setLogContent("下订单返回结果======="+returnData+" 下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		return returnData;
	}
	
	/**
	 * 设置订单状态 
	 * @param code 订单号
	 * @param status 可取值为:NEW, PROCESSING, SHIPPED, CANCELED
	 */
	@SuppressWarnings("static-access")
	private String setStatusOrderMarketplace(String code, String status,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", code);
		param.put("STATUS", status);//NEW PROCESSING SHIPPED CANCELED (for delete ORDER)
		orderDTO.setLogContent("设置订单参数======="+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		String returnData = wisePost(supplierProperties.getWise().getUrl()+supplierProperties.getWise().getSetStatusInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getWise().getUser(),supplierProperties.getWise().getPassword(),orderDTO);
		orderDTO.setLogContent("设置订单状态返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		return returnData;
	}
	
	/**
	 * 获取订单状态
	 * @param code 订单编号
	 * @return
	 */
//	private String getStatusOrderMarketplace(String code) throws Exception {
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("CODE", code);
//		logger.info("查询的订单号为======"+code);
//		String returnData = HttpUtil45.postAuth(url+getStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
//		logger.info("查询返回结果======="+returnData);
//		return returnData;
//	}

}