package com.shangpin.iog.spinnaker.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.spinnaker.dto.OrderInfoDTO;
import com.shangpin.iog.spinnaker.dto.Parameters2;
import com.shangpin.iog.spinnaker.dto.ResponseObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private static Logger loggerError = Logger.getLogger("error");
    private static LoggerUtil loggerError = LoggerUtil.getLogger("error");
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

    @Autowired
    OrderService orderService;
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
//                else if("CL".equals(responseObject.getStatus())){ //无货，需要设置采购异常  （暂时屏蔽 因为对方的数据需要修改 但看状况没有修改 ）
//                    try {
//                        String result = orderService.setPurchaseOrderExc(orderDTO);
//                        if("-1".equals(result)){
//                            orderDTO.setStatus(OrderStatus.NOHANDLE);
//                        }else if("1".equals(result)){
//                            orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
//                        }else if("0".equals(result)){
//                            orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
//                        }
//                    } catch (Exception e) {
//                        loggerError.error("供货商无货，设置采购单"+ orderDTO.getSpPurchaseNo()+"失败。"+e.getMessage());
//                    }
//
//
//                }


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
