package com.shangpin.iog.sanremo.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.sanremo.dto.OrderInfoDTO;
import com.shangpin.iog.sanremo.dto.Parameters2;
import com.shangpin.iog.sanremo.dto.ResponseObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
			 map.put("order_no", orderDTO.getSpOrderId());
			 map.put("key", key);
            if(orderDTO.getSpOrderId().compareTo("201602161167427")<0){  //程序遗留问题
                map.put("order_no", "null");
            }
			 //map.put("sellPrice", order.getSellPrice());
			 rtnData = HttpUtil45.get(queryOrderUrl, defaultConfig , map);
            logger.info("purchase_no=" + orderDTO.getSpPurchaseNo()+";order_no="+ orderDTO.getSpOrderId() + " 查询订单状态返回信息："+rtnData);
        }catch (Exception e) {
        	e.printStackTrace();
        }

        try {
            List<OrderInfoDTO> responseObjectList = gson.fromJson(rtnData, new TypeToken<List<OrderInfoDTO>>() {}.getType());
            if(responseObjectList.size()>0){
                OrderInfoDTO responseObject = responseObjectList.get(0);
                if("SH".equals(responseObject.getStatus())||"FA".equals(responseObject.getStatus())){
                    //String deliverNo =  "DHL" + ";" + "0123456789" + ";" + "2016-01-13 12:00:00";
                    String deliverNo =  responseObject.getLogistics_company() +";"+ responseObject.getTrk_Number() +";"+ responseObject.getDate_Shipped();
                    orderDTO.setStatus(OrderStatus.SHIPPED);
                    orderDTO.setDeliveryNo(deliverNo);
                }
            }



        } catch (JsonSyntaxException e) {
            loggerError.error("采购单："+ orderDTO.getSpPurchaseNo()+ "转化错误."+e.getMessage());
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
