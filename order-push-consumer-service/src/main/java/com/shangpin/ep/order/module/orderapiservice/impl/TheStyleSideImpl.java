package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.order.service.impl.PriceService;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 11:00 2018/4/19
 * @Description:
 */
@Component("theStyleSideImpl")
public class TheStyleSideImpl implements IOrderService{

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    LogCommon logCommon;
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;

    private  String usr = null;
    private  String pwd = null;

    @Autowired
    OpenApiService openApiService;

    @Autowired
    PriceService priceService;

    @PostConstruct
    public void init(){
        usr = supplierProperties.getTheStyleSide().getUsr();
        pwd = supplierProperties.getTheStyleSide().getPwd();
    }

    public static String CANCELED = "CANCELED";
    public static String PENDING = "PENDING";
    public static String CONFIRMED = "CONFIRMED";

    /**
     * 处理供货商订单信息(锁库存)
     * @param orderDTO
     * 订单状态需求参是 orderDTO 里的状态
     * @throws
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        logger.info("===theStyleSide 开始锁库操作========");
        orderDTO.setLockStockTime(new Date());
        orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
        orderDTO.setLogContent("------锁库结束-------");
        logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
        logger.info("=== theStyleSide 锁库结束 ========");
    }

    /**
     * 订单从下单到支付后的处理
     * @param orderDTO  订单信息
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        logger.info("=== theStyleSide 开始推送订单 ========");
        logger.info("=== orderDTO 信息如下："+orderDTO.toString());
        theStyleSidePushOrder(orderDTO);
    }

    /**
     * 取消订单 （未支付）
     * @throws
     */
    @Override
    public void handleCancelOrder(OrderDTO deleteOrder) {
        deleteOrder.setCancelTime(new Date());
        deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
        deleteOrder.setLogContent("------取消订单(未支付)结束,取消锁库-------");
        logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
    }

    /**
     * 退款
     * @param deleteOrder
     */
    @Override
    public void handleRefundlOrder(OrderDTO deleteOrder) {
        deleteOrder.setRefundTime(new Date());
        deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API);
        deleteOrder.setLogContent("------订单退款结束-------");
        logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
    }

    /**
     * theStyleSide 订单推送 wsdl-java客户端发送方式
     * @param orderDTO
     * @return
     */
    public String theStyleSidePushOrder(OrderDTO orderDTO) {

        ObjectFactory objFac=new ObjectFactory();
        //定义订单数据
        DocumentoVO order = new DocumentoVO();
        /**
         * 设置订单的用户信息
         */
        AnagraficaVO anagraficaVO = new AnagraficaVO();
        //?
        anagraficaVO.setID(objFac.createAnagraficaVOID("GENER"));
        JAXBElement<AnagraficaVO> anagraficaVOJaxbe = objFac.createDocumentoVOCustomer(anagraficaVO);
        order.setCustomer(anagraficaVOJaxbe);
        /**
         *  设置订单时间
         */
        Calendar cal = Calendar.getInstance();
        XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
        xmlGregorianCalendar.setTimezone(8);
        xmlGregorianCalendar.setYear(cal.get(Calendar.YEAR));
        xmlGregorianCalendar.setMonth(cal.get(Calendar.MONTH));
        xmlGregorianCalendar.setDay(cal.get(Calendar.DATE));
        xmlGregorianCalendar.setHour(cal.get(Calendar.HOUR));
        xmlGregorianCalendar.setMinute(cal.get(Calendar.MINUTE));
        xmlGregorianCalendar.setSecond(cal.get(Calendar.SECOND));
        xmlGregorianCalendar.setMillisecond(cal.get(Calendar.MILLISECOND));
        order.setDate(xmlGregorianCalendar);
        order.setOrderDate(xmlGregorianCalendar);
        //?
        order.setIdTipoDocumento(objFac.createDocumentoVOIdTipoDocumento(new Short("54")));

        //订单号
        order.setOrderNumber(objFac.createDocumentoVOOrderNumber(orderDTO.getSpOrderId()));
        //?
        order.setPriceListCode(objFac.createDocumentoVOPriceListCode("PUBBL"));

        /**
         * 设置订单的商品的 barCode|size、qty折扣disCount
         * 可以添加多个商品信息 注意格式： 货号|尺码
         */
        ArrayOfDocumentoRigaVO arrayOfDocumentoRigaVO = new ArrayOfDocumentoRigaVO();
        List<DocumentoRigaVO> documentoRigaVOs = arrayOfDocumentoRigaVO.getDocumentoRigaVO();
        if(documentoRigaVOs==null){
            documentoRigaVOs = new ArrayList<>();
        }
        DocumentoRigaVO documentoRigaVO = new DocumentoRigaVO();
        //注意格式： 货号|尺码 00004F|27
        String detail = orderDTO.getDetail();
        logger.info("== 推送订单 orderDTO.getDetail():"+detail);
        String[] details = detail.split(":");
        String skuId = details[0];
        skuId = skuId.replace("-","|");
        String qty = details[1];
        //sku|size   documentoRigaVO.setBarCode(objFac.createDocumentoRigaVOBarCode(""));
        documentoRigaVO.setBarCode(objFac.createDocumentoRigaVOBarCode(skuId));
        //数量qty
        documentoRigaVO.setQty(Integer.parseInt(qty));
        //折扣
        documentoRigaVO.setDiscount((float)0);
        //?
        documentoRigaVO.setIDDocumentoContatore(objFac.createDocumentoRigaVOIDDocumentoContatore("1"));
        //订单价格
        try {
            String price = getpriceDetail(orderDTO);
            if(price!=null){
                documentoRigaVO.setPrice(Float.parseFloat(price));
            }else{
                logger.info("=== theStyleSide 获取订单价格失败 价格结果为空 ");
                orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
                orderDTO.setDescription("=== theStyleSide 获取订单价格失败 价格结果为空 ");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("==获取订单价格失败===");
            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
            orderDTO.setDescription("===系统内部获取 theStyleSide 订单价格失败===== e.getMessage():"+e.getMessage());
            return "";
        }

        documentoRigaVOs.add(documentoRigaVO);
        JAXBElement<ArrayOfDocumentoRigaVO> righe = objFac.createDocumentoVORighe(arrayOfDocumentoRigaVO);
        order.setRighe(righe);

        /**
         * 设置支付方式  ArrayOfTipoPagamentoVO
         */
        ArrayOfTipoPagamentoVO arrayOfTipoPagamentoVO = new ArrayOfTipoPagamentoVO();
        List<TipoPagamentoVO> tipoPagamentoVOs = arrayOfTipoPagamentoVO.getTipoPagamentoVO();
        if(tipoPagamentoVOs==null){
            tipoPagamentoVOs = new ArrayList<>();
        }
        TipoPagamentoVO tipoPagamentoVO = new TipoPagamentoVO();
        tipoPagamentoVO.setDescrizione(objFac.createTagliaVODescrizione("Fattura Fine Mese"));
        tipoPagamentoVOs.add(tipoPagamentoVO);
        JAXBElement<ArrayOfTipoPagamentoVO> jaArrayOfTipoPagamentoVO = objFac.createDocumentoVOTipiPagamento(arrayOfTipoPagamentoVO);
        order.setTipiPagamento(jaArrayOfTipoPagamentoVO);

        String result = null;
        try {
            IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
            result = http.insertOrderItalist(usr, pwd, order);
            if(null==result){
                logger.info("=== theStyleSide 推送订单失败 结果为空 result： "+result);
                orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                orderDTO.setErrorType(ErrorStatus.API_ERROR);
                orderDTO.setDescription("=== theStyleSide 推送订单失败 结果为空 result： "+result);
            } else {
                logger.info("=== theStyleSide 推送订单成功 result： "+result);
                orderDTO.setSupplierOrderNo(result);
                orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
                orderDTO.setConfirmTime(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            orderDTO.setDescription(" ==调用供应商接口异常== ");
            orderDTO.setLogContent(e.getMessage());
            handleException.handleException(orderDTO,e);
        }
        return result;
    }


    /**
     * 从sop获取价格
     * @param orderDTO
     * @return
     */
    @SuppressWarnings("static-access")
    private String getpriceDetail(OrderDTO orderDTO) throws Exception{
        BigDecimal priceInt = priceService.getPurchasePrice(orderDTO.getSupplierId(),"",orderDTO.getSpSkuNo());
        orderDTO.setLogContent("【theStyleSide 在推送订单时获取采购价："+priceInt.toString()+"】");
        logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
        String price = priceInt.divide(new BigDecimal(1.05), 2)
                .setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return price;
    }

}
