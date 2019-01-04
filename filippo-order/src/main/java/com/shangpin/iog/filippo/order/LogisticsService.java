package com.shangpin.iog.filippo.order;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ice.ice.AbsDeliverService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lizhongren on 2016/1/13.
 */
@Component
public class LogisticsService extends AbsDeliverService{

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private OutTimeConfig outTimeConfig = new OutTimeConfig(1000 * 2, 1000 * 60, 1000 * 60);

    private static ResourceBundle bdl = null;
    private static String supplierId = null;
    private static String supplierNo = null;
    private static String orderurl = null;

    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        orderurl = bdl.getString("orderurl");
    }


    @Override
    protected void handleConfirmShippedOrder(OrderDTO orderDTO) {
    	String rtnData = null;

        try {

			 rtnData = this.queryOrder(orderDTO);
            logger.info("purchase_no=" + orderDTO.getSpPurchaseNo()+";order_no="+ orderDTO.getSpOrderId() + " 查询订单状态返回信息："+rtnData);
        }catch (Exception e) {
        	e.printStackTrace();
        }

        try {
            if (StringUtils.isNotBlank(rtnData)) {
                //数据推送成功，filippo系统返回信息
                String[] split = rtnData.split("\\|");
                if(split.length==10){
                    if(split[5].equals("2")){
                        orderDTO.setStatus(OrderStatus.SHIPPED);
                        String deliverNo = "";
                        String tmp = split[8];
                        if(tmp.startsWith("UPS")){

                            deliverNo =  tmp.substring(0,3) +";"+ tmp.substring(3) +";"+ split[9];
                        }else{
                            deliverNo =  tmp +";"+ tmp +";"+ split[9];
                        }

                        orderDTO.setDeliveryNo(deliverNo);
                    }
                }

            }else{
                //数据推送失败
                logger.info("网络原因推送订单失败op=o,orderNo:"+orderDTO.getSpOrderId());
            }



        } catch (Exception e) {
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


    private String queryOrder(OrderDTO orderDTO){

        Map<String, String> param = new HashMap<String, String>();



        param.put("o", "shangG");
        param.put("p", "aW5102cn6");
        param.put("w", "ha");
        param.put("q", "ordlst");
        param.put("poc",orderDTO.getSupplierOrderNo());


        String result = HttpUtil45.get(orderurl, outTimeConfig, param);
        return result;
    }

    public static void main(String[] argus){



    }

}
