package com.shangpin.iog.atelier.purchase.order;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.atelier.common.MyStringUtil;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("atelierOrder")
public class OrderServiceImpl extends AbsOrderService {

    private String barCodeAll;
    private  String soapRequestData;
    private String opName;
    private String skuNo;
    private String excCode;
    private String excDes;
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    /**
     * main
     */
    public void start(){
        //获取条形码
        this.barCodeAll = new WS_Sito_P15().getAllAvailabilityStr();
        //通过采购单处理下单 包括下单和退单
        this.confirmOrder(supplierId);
    }



    /**
     * 在线推送订单：已支付
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {

        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
                barCodeAll.indexOf(orderDTO.getSupplierOrderNo()),barCodeAll.indexOf(orderDTO.getSupplierOrderNo())+50));
        //推送订单
        this.soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
        this.skuNo = orderDTO.getSupplierOrderNo();
        this.opName = "NewOrder";
        sendOrder();
        orderDTO.setExcState(this.excCode);
        orderDTO.setExcDesc(this.excDes);
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
        this.soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
        this.skuNo = deleteOrder.getSupplierOrderNo();
        this.opName = "OrderAmendment";
        sendOrder();
        deleteOrder.setExcState(this.excCode);
        deleteOrder.setExcDesc(this.excDes);
    }


    /**
     * 在线推送订单:未支付
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
orderDTO.setStatus(OrderStatus.PLACED);
    }

    /**
     * 在线推送订单/取消订单
     */
    private void sendOrder(){
        //获取条形码
        WS_Sito_P15 atelier = new WS_Sito_P15();
        String barCodeAll = atelier.getAllAvailabilityStr();
        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
                barCodeAll.indexOf(skuNo),barCodeAll.indexOf(skuNo)+50));
        System.out.println("soapRequestData=="+soapRequestData);
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op="+opName;
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/"+opName);
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            logger.info("returnCode=="+returnCode+","+skuNo);
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
            loggerError.error(skuNo+":"+e.getMessage());
            excCode = "1";
            excDes = e.getMessage();
        } catch (IOException e) {
            loggerError.error(skuNo + ":" + e.getMessage());
            excCode = "1";
            excDes = e.getMessage();
        } catch (Exception e) {
            loggerError.error(skuNo + ":" + e.getMessage());
            excCode = "1";
            excDes = e.getMessage();
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
