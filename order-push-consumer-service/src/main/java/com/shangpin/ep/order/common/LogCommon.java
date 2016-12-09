package com.shangpin.ep.order.common;

import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.OrderBusinessType;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by lizhongren on 2016/11/18.
 * 日志格式汇总    异常日志不在考虑范围
 */
@Component
@Slf4j
public class LogCommon {


    private static String logFormat ="{\"messageId\": {},\"parentMessageId\":{},\"businessType\":{},\"supplierId\":{}," +
            "\"supplierNo\":{},\"skuNo:\":{},\"supplierSkuNo:\":{},\"orderStatus\":{},\"pushStatus\":{},\"logContent\":{},\"handleTime\":{}}";
    /**
     * 记录订单相关的错误日志
     * @param orderDTO
     * @param logTypeStatus
     */
    public static void loggerOrder(OrderDTO orderDTO, LogTypeStatus logTypeStatus){
        recodeLog(LogLeve.INFO,logFormat,getLogContent(orderDTO));

    }

    /**
     * 记录订单相关的错误日志
     * @param orderDTO

     */
    public static void loggerOrder(OrderDTO orderDTO, LogLeve logLeve){

           recodeLog(logLeve,logFormat,getLogContent(orderDTO));


    }



    public static void recordLog(Map<String,String> msgMap){

    }

    public static void recordLog(String content){
        recodeLog(LogLeve.INFO,content);
    }

    /**
     * 记录日志
     * @param content   ：日志内容
     * @param logLeve   ：日志级别
     */
    public static void recordLog(String content, LogLeve logLeve){
        recodeLog(logLeve,content);
    }

    public static void recordLog(String content,Exception e){

    }

    public static void recordLog(String content, LogLeve logLeve,Exception e){
        recodeLog(logLeve,content + " exceptionLog:" + e.getMessage() );
    }

    private static void recodeLog(LogLeve logLeve,String format,Object... obj){
         if("TRACK".equals(logLeve.getDescription())){
             log.trace(format,obj);
         }else  if("DEBUG".equals(logLeve.getDescription())) {
             log.debug(format,obj);
         }else  if("INFO".equals(logLeve.getDescription())){
             log.info(format,obj);
         } else  if("WARN".equals(logLeve.getDescription())){
             log.warn(format,obj);
         }else  if("ERROR".equals(logLeve.getDescription())){
             log.error(format,obj);
         }
    }

    private static void recodeLog(LogLeve logLeve,String content){
        if("TRACK".equals(logLeve.getDescription())){
            log.trace(content);
        }else  if("DEBUG".equals(logLeve.getDescription())) {
            log.debug(content);
        }else  if("INFO".equals(logLeve.getDescription())){
            log.info(content);
        } else  if("WARN".equals(logLeve.getDescription())){
            log.warn(content);
        }else  if("ERROR".equals(logLeve.getDescription())){
            log.error(content);
        }
    }

    /**
     * 获取日志内容
     * @return
     */
    private static String[]  getLogContent(OrderDTO orderDTO){
        //"{\"messageId\": %s,\"parentMessageId\":%s,\"businessType\":%s,\"supplierId\":%s," +
       // "\"supplierNo\":%s,\"skuNo:\":%s,\"supplierSkuNo:\":%s,\"orderStatus\":%s,\"pushStatus\":%s,\"handleTime\":%s}";
        String[] contentArray = new  String[11];
        contentArray[0] = orderDTO.getMessageId();
        contentArray[1] = orderDTO.getParentMessageId();
        contentArray[2] = null==orderDTO.getBusinessType()?"":orderDTO.getBusinessType().getDescription();
        contentArray[3] = orderDTO.getSupplierId();
        contentArray[4] = orderDTO.getSupplierNo();
        contentArray[5] = orderDTO.getSpSkuNo();
        contentArray[6] = orderDTO.getSupplierSkuNo();
        contentArray[7] = null==orderDTO.getOrderStatus()?"":orderDTO.getOrderStatus().getDescription();
        contentArray[8] = null==orderDTO.getPushStatus()?"":orderDTO.getPushStatus().getDescription();
        contentArray[9] = orderDTO.getLogContent();
        if(null!=orderDTO.getUpdateTime()){
            contentArray[10] =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDTO.getUpdateTime()) ;
        }else{
            contentArray[10] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        return contentArray;
    }
}
