package com.shangpin.iog.atelier.purchase.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.atelier.common.MyStringUtil;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import com.shangpin.iog.atelier.purchase.dto.*;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.service.ReturnOrderService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("atelierOrder")
public class OrderServiceImpl extends AbsOrderService {

    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;
    private static String orderFile ;
    private static int period ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
        orderFile = bdl.getString("newOrder");
        period = Integer.parseInt(bdl.getString("period"));
    }

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    /**
     * 在线推送订单:未支付
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {

    }
    /**
     * 在线推送订单：已支付
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        //订单支付确认
        confirmOrder(supplierId);
        //获取条形码
        WS_Sito_P15 atelier = new WS_Sito_P15();
        String barCodeAll = atelier.getAllAvailabilityStr();
        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
                barCodeAll.indexOf(orderDTO.getSupplierOrderNo()),barCodeAll.indexOf(orderDTO.getSupplierOrderNo())+50));
        //推送订单
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <NewOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + orderDTO.getSupplierOrderNo() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + "1234" + "</ID_CLIENTE_WEB>\n" +
                "      <DESTINATIONROW1>" + "" + "</DESTINATIONROW1>\n" +
                "      <DESTINATIONROW2>" + "" + "</DESTINATIONROW2>\n" +
                "      <DESTINATIONROW3>" + "" + "</DESTINATIONROW3>\n" +
                "      <BARCODE>" + barCode + "</BARCODE>\n" +
                "      <QTY>" + orderDTO.getDetail().split(":")[1] + "</QTY>\n" +
                "      <PRICE>" + "111" + "</PRICE>\n" +
                "    </NewOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=NewOrder";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/NewOrder");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
/*            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(orderFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();*/
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
        //获取条形码
        WS_Sito_P15 atelier = new WS_Sito_P15();
        String barCodeAll = atelier.getAllAvailabilityStr();
        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
                barCodeAll.indexOf(deleteOrder.getSupplierOrderNo()),barCodeAll.indexOf(deleteOrder.getSupplierOrderNo())+50));
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <OrderAmendment xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + deleteOrder.getSupplierOrderNo() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + "" + "</ID_CLIENTE_WEB>\n" +
                "      <BARCODE>" + barCode + "</BARCODE>\n" +
                "      <QTY>0</QTY>\n" +
                "    </OrderAmendment>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=OrderAmendment";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/OrderAmendment");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
/*            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(orderFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();*/
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * String:尚品skuNo
     * String:组装后的skuNo
     */
    @Override
    public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {

    }

    public static void main(String[] args) {
        OrderServiceImpl  orderService = new OrderServiceImpl();

//        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
//        purchaseOrderDetails.add(purchaseOrderDetail);
//        orderMap.put("",purchaseOrderDetails);
//
//        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",orderMap);


    }
}
