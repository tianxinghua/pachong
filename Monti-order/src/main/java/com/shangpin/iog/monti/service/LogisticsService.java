package com.shangpin.iog.monti.service;

import com.google.gson.Gson;
import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.monti.dto.ResponseObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class LogisticsService extends AbsDeliverService{

    private static Logger logger = Logger.getLogger("info");
    private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60, 1000 * 60);

    private static ResourceBundle bdl = null;
    private static String supplierId = null;
    private static String supplierNo = null;
    private static String queryOrderUrl = null;
    private static String dBContext = null;
    private static String key = null;
    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("param");
        }
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        queryOrderUrl = bdl.getString("queryOrderUrl");
        dBContext = bdl.getString("dBContext");
        key = bdl.getString("key");
    }

    @Override
    protected void handleConfirmShippedOrder(OrderDTO orderDTO) {
    	String rtnData = null;
    	Gson gson = new Gson();
        
        try {
        	Map<String, String> map =new HashMap<String, String>();
			 //String[] barcode = orderDTO.getDetail().split(":");
			 map.put("DBContext", dBContext);
			 map.put("Purchase_No", orderDTO.getSpPurchaseNo());
			 map.put("Order_No", orderDTO.getSupplierOrderNo());
			 map.put("Key", key);
			 //map.put("sellPrice", order.getSellPrice());
			 rtnData = HttpUtil45.get(queryOrderUrl, defaultConfig , map);
			 logger.info("发货验证返回信息==="+rtnData); 
        }catch (Exception e) {
        	e.printStackTrace();
        	logger.info("发货验证异常==="+e); 
        }
        
        ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
        if("SH".equals(responseObject.getStatus())){
	        //String deliverNo =  "DHL" + ";" + "0123456789" + ";" + "2016-01-13 12:00:00";
	        String deliverNo =  responseObject.getLogistics_company() +";"+ responseObject.getTrk_Number() +";"+ responseObject.getDate_Order();
	        orderDTO.setStatus(OrderStatus.SHIPPED);
	        orderDTO.setDeliveryNo(deliverNo);
        }
    }

    /**
     * 处理供货商已发货的单子
     */
    public void handleShippedOrder(){
        this.confirmShippedOrder(supplierId);
    }

    /**
     * 设置尚品的发货单
     */
    public void handleInvoice(){
        this.deliveryOrder(supplierId);

    }

}
