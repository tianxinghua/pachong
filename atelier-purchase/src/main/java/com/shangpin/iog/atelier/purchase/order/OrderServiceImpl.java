package com.shangpin.iog.atelier.purchase.order;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import com.shangpin.iog.atelier.purchase.dto.AtelierOrder;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.ice.dto.ICEWMSOrderRequestDTO;
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
 * Created by wangyuzhi on 15/10/08.
 */
@Component("atelierOrder")
public class OrderServiceImpl extends AbsOrderService {

    private AtelierOrder atelierOrder = new AtelierOrder();
/*    private String barCodeAll;
    private  String soapRequestData;
    private String opName;
    private String skuNo;
    private String excCode;
    private String excDes;*/
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
    public void startWMS(){
        //获取条形码
        atelierOrder.setBarCodeAll(new WS_Sito_P15().getAllAvailabilityStr());
        //通过采购单处理下单 包括下单和退单
        this.checkoutOrderFromWMS(supplierId, "", true);
    }
    public void startSOP(){
        //获取条形码
        atelierOrder.setBarCodeAll(new WS_Sito_P15().getAllAvailabilityStr());
        //通过采购单处理下单 包括下单和退单
        this.checkoutOrderFromSOP(supplierId, "", true);
    }



    /**
     * 在线推送订单：已支付
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {

//        String barCodeAll = atelierOrder.getBarCodeAll();
//        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
//                barCodeAll.indexOf(orderDTO.getSupplierOrderNo()),barCodeAll.indexOf(orderDTO.getSupplierOrderNo())+50));
        //得到barcode
        String barCode = orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[1];
        //推送订单
        atelierOrder.setSoapRequestData( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <NewOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + orderDTO.getSupplierOrderNo() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + "1234" + "</ID_CLIENTE_WEB>\n" +
                "      <DESTINATIONROW1>" + "" + "</DESTINATIONROW1>\n" +
                "      <DESTINATIONROW2>" + "" + "</DESTINATIONROW2>\n" +
                "      <DESTINATIONROW3>" + "" + "</DESTINATIONROW3>\n" +
                "      <BARCODE>" + barCode + "</BARCODE>\n" +
                "      <QTY>" + orderDTO.getDetail().split(",")[0].split(":")[1] + "</QTY>\n" +
                "      <PRICE>" + "111" + "</PRICE>\n" +
                "    </NewOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>");
        System.out.println("soapRequestData=="+atelierOrder.getSoapRequestData());
        atelierOrder.setSkuNo(orderDTO.getSupplierOrderNo());
        atelierOrder.setOpName("NewOrder");
        sendOrder();
        orderDTO.setExcState(atelierOrder.getExcCode());
        orderDTO.setExcDesc(atelierOrder.getExcDes());
    }
    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
        //获取条形码
//        String barCodeAll = atelierOrder.getBarCodeAll();
//        String barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
//                barCodeAll.indexOf(deleteOrder.getSupplierOrderNo()),barCodeAll.indexOf(deleteOrder.getSupplierOrderNo())+50));
      //得到barcode
        String barCode = deleteOrder.getDetail().split(",")[0].split(":")[0].split("-")[1];
        atelierOrder.setSoapRequestData( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <OrderAmendment xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + deleteOrder.getSupplierOrderNo() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + "" + "</ID_CLIENTE_WEB>\n" +
                "      <BARCODE>" + barCode + "</BARCODE>\n" +
                "      <QTY>0</QTY>\n" +
                "    </OrderAmendment>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>");
        System.out.println("soapRequestData=="+atelierOrder.getSoapRequestData());
        atelierOrder.setSkuNo(deleteOrder.getSupplierOrderNo());
        atelierOrder.setOpName("OrderAmendment");
        sendOrder();
        deleteOrder.setExcState(atelierOrder.getExcCode());
        deleteOrder.setExcDesc(atelierOrder.getExcDes());
    }


    /**
     * 在线推送订单:未支付
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        atelierOrder.setSoapRequestData( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <SetStatusOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <CODICE>"+orderDTO.getSupplierOrderNo()+"</CODICE>\n" +
                "      <ID_CLIENTE>12345</ID_CLIENTE>\n" +
                "      <ID_STATUS>1</ID_STATUS>\n" +
                "    </SetStatusOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>");
        System.out.println("soapRequestData=="+atelierOrder.getSoapRequestData());
        atelierOrder.setSkuNo(orderDTO.getSupplierOrderNo());
        atelierOrder.setOpName("SetStatusOrder");
        sendOrder();
        orderDTO.setExcState(atelierOrder.getExcCode());
        orderDTO.setExcDesc(atelierOrder.getExcDes());
        orderDTO.setStatus(atelierOrder.getStatus());
    }

    /**
     * 在线推送订单/取消订单
     */
    private void sendOrder(){
        System.out.println("soapRequestData=="+atelierOrder.getSoapRequestData());
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op="+atelierOrder.getOpName();
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/"+atelierOrder.getOpName());
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(atelierOrder.getSoapRequestData());
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        String rtnData = "";
        try {
            returnCode = httpClient.executeMethod(postMethod);
            rtnData = postMethod.getResponseBodyAsString();
            System.out.println("returnCode=="+returnCode);
            logger.info("returnCode=="+returnCode+","+atelierOrder.getSkuNo());
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
            loggerError.error(atelierOrder.getSkuNo()+":"+e.getMessage());
            atelierOrder.setExcCode("1");
            atelierOrder.setExcDes(e.getMessage());
        } catch (IOException e) {
            loggerError.error(atelierOrder.getSkuNo() + ":" + e.getMessage());
            atelierOrder.setExcCode("1");
            atelierOrder.setExcDes(e.getMessage());
        } catch (Exception e) {
            loggerError.error(atelierOrder.getSkuNo() + ":" + e.getMessage());
            atelierOrder.setExcCode("1");
            atelierOrder.setExcDes(e.getMessage());
        } finally {
            if (!"200".equals(returnCode)){
                atelierOrder.setExcCode("1");
                atelierOrder.setExcDes("returnCode is" + returnCode);
            }else if (!rtnData.contains("OK")){
                atelierOrder.setExcCode("1");
                atelierOrder.setExcDes(atelierOrder.getOpName() +" is ER!");
            } else if("NewOrder".equals(atelierOrder.getOpName())){
                atelierOrder.setStatus(OrderStatus.PAYED);
            } else if("OrderAmendment".equals(atelierOrder.getOpName())){
                atelierOrder.setStatus(OrderStatus.CANCELLED);
            } else if("SetStatusOrder".equals(atelierOrder.getOpName())){
                atelierOrder.setStatus(OrderStatus.PLACED);
            }
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

        Gson gson = new Gson();
        ICEWMSOrderRequestDTO dto = new ICEWMSOrderRequestDTO();
        dto.setBeginTime("2015-12-23 11:11:11");
        dto.setEndTime("2015-09-23 11:11:11");
        dto.setSupplierNo("200520155");
        String jsonParameter= "="+ gson.toJson(dto);
        try {
            String   result =  HttpUtil45.operateData("post", "form", "http://wmsinventory.liantiao.com/Api/StockQuery/SupplierInventoryLogQuery", new OutTimeConfig(1000 * 5, 1000 * 5, 1000 * 5), null,
                    jsonParameter, "", "");
            System.out.println(result);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}
}
