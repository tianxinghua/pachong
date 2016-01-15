package com.shangpin.iog.spinnaker.service;

import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * Created by lizhongren on 2016/1/13.
 */
@Component
public class LogisticsService extends AbsDeliverService{

    private static Logger logger = Logger.getLogger("info");

    private static ResourceBundle bdl = null;
    private static String supplierId = null;
    private static String supplierNo = null;
    private static String url = null;
    private static String dBContext = null;
    private static String key = null;
    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("param");
        }
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        url = bdl.getString("url");
        dBContext = bdl.getString("dBContext");
        key = bdl.getString("key");
    }

    @Override
    protected void handleConfirmShippedOrder(OrderDTO orderDTO) {
        orderDTO.setStatus(OrderStatus.SHIPPED);
        String deliverNo =  "DHL" + ";" + "0123456789" + ";" + "2016-01-13 12:00:00";
        orderDTO.setDeliveryNo(deliverNo);
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
