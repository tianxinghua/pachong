package com.shangpin.iog.spinnaker.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.spinnaker.dto.Parameters2;
import com.shangpin.iog.spinnaker.dto.ResponseObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lizhongren on 2016/1/13.
 */
@Component
public class LogisticsService extends AbsDeliverService{

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
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
			 map.put("purchase_no", orderDTO.getSpPurchaseNo());
			 map.put("order_no", orderDTO.getSupplierOrderNo());
			 map.put("key", key);
			 //map.put("sellPrice", order.getSellPrice());
			 rtnData = HttpUtil45.get(queryOrderUrl, defaultConfig , map);
            logger.info("查询订单状态返回信息："+rtnData);
        }catch (Exception e) {
        	e.printStackTrace();
        }

        try {
            ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
            if("SH".equals(responseObject.getStatus())){
                //String deliverNo =  "DHL" + ";" + "0123456789" + ";" + "2016-01-13 12:00:00";
                String deliverNo =  responseObject.getLogistics_company() +";"+ responseObject.getTrk_Number() +";"+ responseObject.getDate_Order();
                orderDTO.setStatus(OrderStatus.SHIPPED);
                orderDTO.setDeliveryNo(deliverNo);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
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
