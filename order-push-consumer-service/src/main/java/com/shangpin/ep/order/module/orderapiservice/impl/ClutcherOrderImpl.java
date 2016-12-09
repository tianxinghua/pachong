package com.shangpin.ep.order.module.orderapiservice.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther.ItemDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther.MD5;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther.PushOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther.ResponseObject;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.utils.UUIDGenerator;

/**
 * Created by wangyuzhi on 15/10/9.
 */
@Component("clutcherOrderImpl")
public class ClutcherOrderImpl implements IOrderService {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    private  String merchantId = null;
    private  String token = null;
    private  String url = null;
    
    @PostConstruct
    public void init(){
    	url = supplierProperties.getTonyConf().getUrl();
    	token = supplierProperties.getTonyConf().getToken();
    	merchantId = supplierProperties.getTonyConf().getMerchantId();
    }
    
    public static String CANCELED = "CANCELED";
    public static String PENDING = "PENDING";
    public static String CONFIRMED = "CONFIRMED";
    /**
     * 锁库存
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        //在线推送订单
        createOrder(orderDTO,"put","holdorders","lock");
        //设置异常信息
    }	
    /**
     * 推送订单
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        //在线推送订单
    	createOrder(orderDTO,"put","orders","confirm");
    }

    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(OrderDTO orderDTO) {
    	createOrder(orderDTO,"delete","holdorders","cancel");
    }
   
	@Override
	public void handleRefundlOrder(OrderDTO orderDTO) {
		createOrder(orderDTO,"delete","orders","refund");
		
	}
	 
    /**
     * 在线推送订单:
     * status未支付：锁库存						
     * status已支付：推单
     */
    
    /**
     * 在线推送订单:
     * status未支付：锁库存						
     * status已支付：推单
     */
    private void createOrder(OrderDTO orderDTO,String method,String uri,String type){
        //获取订单信息
        PushOrderDTO order = getOrder(orderDTO);
        Gson gson = new Gson();
        String json = gson.toJson(order,PushOrderDTO.class);
        String rtnData = null;
		String uuid = null;
    	if(orderDTO.getSupplierOrderNo()==null){
    		uuid = UUIDGenerator.getUUID();
    	}else{
    		uuid = orderDTO.getSupplierOrderNo();
    	}
        try {
            rtnData = getResponseJson(orderDTO,json,method,uri,uuid);
            logger.info(uri+","+type+"推送订单返回结果=="+rtnData+",推送的数据："+json);
        	orderDTO.setLogContent(uri+","+type+"推送订单返回结果=="+rtnData);
    		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	setType(orderDTO,type,"ko",uuid);
            	return ;
            }
            ResponseObject respon = gson.fromJson(rtnData, ResponseObject.class);
            if("KO".equals(respon.getStatus())){
            	setType(orderDTO,type,"ko",uuid);
            }else if("OK".equals(respon.getStatus())){
            	setType(orderDTO,type,"ok",uuid);
            }else{
            	if("cancel".equals(type)||"refund".equals(type)){
            		setType(orderDTO,type,"ok",uuid);
            	}else{
            		throw new ServiceException(uri+","+type+"推送订单返回结果="+rtnData+"，未找到相应订单，请检查");	
            	}
            }
        } catch (Exception e) {
        	setType(orderDTO,type,"ko",uuid);
        } 
    }
    
    private void setType(OrderDTO orderDTO,String type,String flag,String id){
    	if("refund".equals(type)){
    		if("ko".equals(flag)){
				orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
                orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			}else if("ok".equals(flag)){
				orderDTO.setRefundTime(new Date());
				orderDTO.setPushStatus(PushStatus.REFUNDED);
			}
    	}
		if("cancel".equals(type)){
			if("ko".equals(flag)){
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED);
//				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription(orderDTO.getLogContent());
			}else if("ok".equals(flag)){
				orderDTO.setCancelTime(new Date());
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED);
			}    		
		}
		if("confirm".equals(type)){
			if("ko".equals(flag)){
				orderDTO.setPushStatus(PushStatus.NO_STOCK);
				orderDTO.setDescription(orderDTO.getLogContent());
			}else if("ok".equals(flag)){
				orderDTO.setConfirmTime(new Date());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			}
		}
		if("lock".equals(type)){
			orderDTO.setSupplierOrderNo(id);
			if("ko".equals(flag)){
				orderDTO.setPushStatus(PushStatus.NO_STOCK);
                orderDTO.setDescription(orderDTO.getLogContent());
			}else if("ok".equals(flag)){
				orderDTO.setLockStockTime(new Date());
				orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
			}
		}
    }
    /**
     * 获取订单信息
     */
	private static PushOrderDTO getOrder(OrderDTO orderDTO){
		PushOrderDTO order = new PushOrderDTO();
		String markPrice = null;
		List<ItemDTO> list = new ArrayList<ItemDTO>();
		try {
		     String detail = orderDTO.getDetail();
				int num = 0;
				String skuNo = null;
				num = Integer.parseInt(detail.split(":")[1]);
				skuNo = detail.split(":")[0];
				ItemDTO item = new ItemDTO();
				item.setSkuID(skuNo);
				item.setQty(num);
				list.add(item);
				order.setOrderItems(list);
				order.setOrderNo(orderDTO.getSpOrderId());
				order.setPurchaseNo(orderDTO.getPurchaseNo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return  order;
	}
	private String clutherPushOrder(OrderDTO orderDTO, String url, String type)  throws Exception{
		return HttpUtil45.operateData(type, "json", url, new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2), null, null,null, null, null);
	}
	public String handleException(OrderDTO orderDTO, String url, String type,Throwable e){		
		handleException.handleException(orderDTO, e); 
		return null;
	}
	public  String getResponseJson(OrderDTO orderDTO,String p,String type,String uri,String uuid) {
		String app_secret = "hM&L1dqGA5YGjGK%fU8*715D$g&z$B";
		Map<String, String> params = new LinkedHashMap<String, String>();
		String app_key = "SHNGPN-001";
		params.put("app_key", app_key);		
		params.put("request", p);
		String time = getUTCTime().getTime()+"";
		time = time.substring(0,10);
		params.put("time_stamp",time );
		String charset = "UTF-8";
		String md5_sign;
		String json = null;
		try {
			md5_sign ="app_key=" + app_key
				        + (params.containsKey("request") ? "&request=" + params.get("request") : "")
				               + "&time_stamp=" +params.get("time_stamp")
				                                             + "_" + app_secret;
			String md5_result = MD5.encrypt32(md5_sign);
			params.put("sign", md5_result);
			Map<String, String> headerMap = new LinkedHashMap<String, String>();
			headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			String url = "http://ws.theclutcher.com/shangpin.svc/"+uri+"/"+uuid+"?app_key=SHNGPN-001&request="+URLEncoder.encode(p)+"&time_stamp="+time+"&sign="+md5_result;
			json = clutherPushOrder(orderDTO,url,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
   private static Date getUTCTime(){
	   final java.util.Calendar cal = java.util.Calendar.getInstance(); 
	   cal.setTimeZone(TimeZone.getTimeZone("GMT"));
	    //2、取得时间偏移量：    
	    final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);   
	    //3、取得夏令时差：    
	    final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);    
	   
	    //4、从本地时间里扣除这些差量，即可以取得UTC时间：    
	    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));    
        return cal.getTime();
    }
   public static long GetTicks() 
   { 
	  SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss"); 
      String date =s.format(getUTCTime()); 
      String[] ds=date.split("/"); 
     Calendar calStart=Calendar.getInstance(); 
     calStart.set(1, 1, 3, 0, 0, 0); 
      
     Calendar calEnd=Calendar.getInstance(); 
     calEnd.set(Integer.parseInt(ds[0]) ,Integer.parseInt(ds[1]),Integer.parseInt(ds[2]),Integer.parseInt(ds[3]),Integer.parseInt(ds[4]),Integer.parseInt(ds[5]) ); 
      
     long epochStart=calStart.getTime().getTime(); 
     //epoch time of the target time 
     long epochEnd=calEnd.getTime().getTime(); 
      
     //get the sum of epoch time, from the target time to the ticks-start time 
      long all=epochEnd-epochStart;    
      //convert epoch time to ticks time 
         long ticks=( (all/1000) * 1000000) * 10; 
         
         return ticks; 
   }

}
