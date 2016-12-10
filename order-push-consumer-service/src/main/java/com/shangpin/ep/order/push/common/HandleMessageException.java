package com.shangpin.ep.order.push.common;

import java.util.List;
import java.util.Map;

import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderDetailSync;
import com.shangpin.ep.order.conf.supplier.Supplier;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.exception.ServiceMessageException;

/**
 * Created by lizhongren on 2016/11/26.
 * 统一处理抛出的异常
 *
 */
@Component
public class HandleMessageException extends AbstractPusher{

    @Autowired
    ShangpinMailSender mailSender;

    @Autowired
    SupplierProperties supplierProperties;

    @Autowired
    OrderCommonUtil orderCommonUtil;

    public void handleException(SupplierOrderSync message, Map<String, Object> headers,Exception e) throws  Exception{
        //记录日志
        LogCommon.recordLog(" exceptionLog :" +message.toString()  +  "  message-header: " + headers.toString() + " reason:" + e.getMessage(), LogLeve.ERROR);

        handleSendMailByAPIError(message,headers);
        //此类异常  直接抛出 人工干预
        handleException(message, e);

    }


    /**
     * 处理异常
     * @param message
     * @param e
     * @throws Exception
     */
    private void handleException(SupplierOrderSync message, Exception e) throws Exception {
        if(e instanceof ServiceMessageException){

            List<SupplierOrderDetailSync> orderDetailSyncs = message.getSyncDetailDto();
            String supplierNo="",orderNo="",supplierName="";
            orderNo = message.getOrderNo();
            if(null!=orderDetailSyncs){
                for(SupplierOrderDetailSync orderDetailSync :orderDetailSyncs){
                    supplierNo = orderDetailSync.getSupplierNo();

                    SupplierDTO supplierDTO = orderCommonUtil.getSupplier(supplierNo);
                    if(null!=supplierDTO){
                        supplierName = supplierDTO.getSupplierName();
                    }
                }
            }
            //发邮件
            ShangpinMail shangpinMail = new ShangpinMail();

            shangpinMail.setSubject("紧急！供货商"+ (StringUtils.isBlank(supplierName)?supplierNo:supplierName) +"订单"+ orderNo  +"需要人工处理.");

            shangpinMail.setText("紧急！订单信息需要人工处理. 具体订单信息：" + message.toString() +"\r\n"
                    +" 原因： " + e.getMessage());
            mailSender.sendShangpinMail(shangpinMail );
            //扔入错误消息队列 人工处理
            throw e;
        }
    }

    /**
     * 对方接口挂掉需要发邮件通知  第一发 和 最后一次发送 其它时间不发送
     * @param message
     * @param headers
     */
    public void handleSendMailByAPIError(SupplierOrderSync message, Map<String, Object> headers){
       if(headers.containsKey(GlobalConstant.MESSAGE_HEADER_ORDER_PUSH_ERROR_TYPE)){
            if((Integer)headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_PUSH_ERROR_TYPE)== ErrorStatus.API_ERROR.getIndex()){
                //发邮件
                Supplier supplier = supplierProperties.getSupplier();
                int retryNum = (null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY));
                if(1==retryNum||retryNum==supplier.getRetry()){
                    List<SupplierOrderDetailSync> orderDetailSyncs = message.getSyncDetailDto();
                    String supplierNo="",supplierName="";
                    if(null!=orderDetailSyncs){
                        for(SupplierOrderDetailSync orderDetailSync :orderDetailSyncs){
                            supplierNo = orderDetailSync.getSupplierNo();
                            SupplierDTO supplierDTO = orderCommonUtil.getSupplier(supplierNo);
                            if(null!=supplierDTO){
                                supplierName = supplierDTO.getSupplierName();
                            }
                        }
                    }

                    ShangpinMail shangpinMail = new ShangpinMail();

                    shangpinMail.setSubject("紧急！供货商"+ (StringUtils.isBlank(supplierName)?supplierNo:supplierName) +"的接口挂掉");

                    shangpinMail.setText("紧急！供货商的接口挂掉. 具体订单信息：" + message.toString() );
                    try {
                        mailSender.sendShangpinMail(shangpinMail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
       }

    }

}
