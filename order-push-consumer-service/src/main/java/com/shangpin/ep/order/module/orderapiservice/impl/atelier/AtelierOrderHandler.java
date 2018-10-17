package com.shangpin.ep.order.module.orderapiservice.impl.atelier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
/**
 * atelier系统处理订单通用类
 * <p>Title: AtelierOrderHandler</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月27日 下午5:27:36
 *
 */
@Service
public abstract class AtelierOrderHandler implements IOrderService {
	
	private static OutTimeConfig getStockOutTimeConf = new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10);
	private static OutTimeConfig createOrderOutTimeConf = new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1);	

	@Autowired
    private LogCommon logCommon;    
    @Autowired
    private HandleException handleException;
    @Autowired
    private CommonService commonService;

    @Autowired
	private ShangpinMailSender shangpinMailSender;

    /**
     * 获取api url
     * @return
     */
    public abstract String getApiUrl();
    /**
     * 获取用户名
     * @return
     */
    public abstract String getUserName();
    /**
     * 获取密码
     * @return
     */
    public abstract String getPassword();
    /**
     * 获取查看库存接口
     * @return
     */
    public abstract String getGetItemStockInterface();
    /**
     * 获取创建订单接口
     * @return
     */
    public abstract String getCreateOrderInterface();
    /**
     * 获取退单接口
     * @return
     */
    public abstract String getSetStatusInterface();


	/**
	 * 获取产品信息
	 * @return
	 */
	public abstract ShangpinMail getEmailMessage();

    /*
     获取发送给供货商的邮件内容
     */
    public abstract String getEmailContent(OrderDTO orderDTO, String size);
    
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
    public String atelierPost(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password) throws Exception{
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
		
		try {
			String spOrderId = null;
			if(orderDTO.getPurchaseNo()!=null){
//				spOrderId = orderDTO.getPurchaseNo();
//				spOrderId = spOrderId.replace("CGDF","");
                spOrderId = orderDTO.getSpOrderDetailNo();
			}else{
				spOrderId = orderDTO.getSpOrderId();
			}
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
			String productSize = commonService.getProductSize(orderDTO.getSupplierId(),skuId);
			if(StringUtils.isNotBlank(productSize)){
				String size = productSize.replaceAll("\\+", "½");				
				//查询对方库存接口
				orderDTO.setLogContent("查询库存参数============" + item_id); 
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				String stockData = getItemStockBySizeMarketPlace(item_id);
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
//					stock =1;//不在判断库存
					if(stock > 0){
						//下单
						String returnData = newOrderMarketPlace(id_order_mrkp,barcode,qty,orderDTO);
						
						if (returnData.contains("OK")) {
							orderDTO.setConfirmTime(new Date()); 
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
							//下单成功 发送邮件
							String content = this.getEmailContent(orderDTO,size);
							if(StringUtils.isNotBlank(content)){
								try {
									ShangpinMail shangpinMail = this.getEmailMessage();
									if(null!=shangpinMail){
                                        shangpinMail.setText(content);
                                        shangpinMailSender.sendShangpinMail(shangpinMail);
                                    }
								} catch (Exception e) {

								}
							}

						} else {
							//推送订单失败
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
							orderDTO.setDescription("下单失败：" + returnData);
						}
					}else{
//						orderDTO.setConfirmTime(new Date());
						orderDTO.setPushStatus(PushStatus.NO_STOCK);
//						sendMail(item_id+" 该产品库存不足!采购单号是："+orderDTO.getSpPurchaseNo());
					}
				}else{
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
					orderDTO.setDescription("查询对方库存接口失败,对方返回的信息是："+stockData);
//					sendMail("订单 "+orderDTO.getSpPurchaseNo()+" spuid等于 "+item_id+" 查询对方库存接口 GetItemStockBySizeMarketPlace 失败,对方返回的信息是："+stockData+",请与供应商联系。2分钟后会再推一次。 ");
				}
			}else{
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
				orderDTO.setDescription("查询数据库失败,未找到该商品 "+skuId);
				orderDTO.setLogContent("查询数据库失败,未找到该商品=========== "+skuId);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
//				sendMail("订单 "+orderDTO.getSpPurchaseNo()+" 查询数据库失败,未找到该商品=========== "+skuId);
			}
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		
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
	private String getItemStockBySizeMarketPlace(String item_id) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ITEM_ID", item_id);		
		String returnData = atelierPost(getApiUrl()+getGetItemStockInterface(), param, getStockOutTimeConf, getUserName(), getPassword());
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
		param.put("ID_ORDER_MRKP", String.valueOf(id_order_mrkp));
		param.put("BARCODE", barcode);
		param.put("QTY", String.valueOf(qty));
		orderDTO.setLogContent("下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = atelierPost(getApiUrl()+getCreateOrderInterface(), param, createOrderOutTimeConf, getUserName(), getPassword());
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
		String returnData = atelierPost(getApiUrl()+getSetStatusInterface(), param, getStockOutTimeConf, getUserName(), getPassword());
		orderDTO.setLogContent("设置订单状态返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		return returnData;
	}
}