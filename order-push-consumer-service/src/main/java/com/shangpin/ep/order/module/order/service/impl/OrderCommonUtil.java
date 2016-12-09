package com.shangpin.ep.order.module.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderDetailSync;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.conf.supplier.SupplierCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.*;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.mapping.bean.HubSkuRelation;
import com.shangpin.ep.order.module.mapping.service.impl.HubSkuRelationService;
import com.shangpin.ep.order.module.order.bean.*;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;
import com.shangpin.ep.order.module.supplier.bean.SupplierMsg;
import com.shangpin.ep.order.push.common.GlobalConstant;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.utils.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Component
public class OrderCommonUtil {

    /**
     * 从库对象转化为处理对象
     * @param hubOrderDetail
     * @return
     */
    @Autowired
    IShangpinRedis shangpinRedis;

    @Autowired
    HubSkuRelationService hubSkuRelationService;

    @Autowired
    SupplierProperties supplierProperties;

    @Autowired
    IHubOrderService hubOrderService;

    @Autowired
    ShangpinMailSender shangpinMailSender;

    @Autowired
    OrderHandleSearch orderHandleSearch;


    private static Map<String,String> orderSupplierMap =new HashMap<>();
    private static Map<String,String> stockSupplierMap =new HashMap<>();

    @PostConstruct
    public void init(){
        //获取映射关系
        String orderSupplier = supplierProperties.getSupplier().getOrderSupplierMapping();
        String[] orderSupplierArray = orderSupplier.split(",");
        if(null!=orderSupplierArray){
            for(String supplier:orderSupplierArray){
                if(supplier.indexOf(":")>0){

                    orderSupplierMap.put(supplier.substring(0,supplier.indexOf(":")),
                            supplier.substring(supplier.indexOf(":")+1,supplier.length()));
                }
            }
        }
//        String stockSupplier = supplierProperties.getSupplier().getStockSupplierMapping();
//        String[] stockSupplierArray = orderSupplier.split(",");
//        if(null!=stockSupplierArray){
//            for(String supplier:stockSupplierArray){
//                stockSupplierMap.put(supplier.substring(0,supplier.indexOf(":")),
//                        supplier.substring(supplier.indexOf(":")+1,supplier.length()));
//            }
//        }
    }

    /**
     * 从订单明细到DTO
     * @param hubOrderDetail
     * @return
     */
    public  OrderDTO changeHubOrderDetailToOrderDTO(HubOrderDetail hubOrderDetail){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(hubOrderDetail,orderDTO);
        orderDTO.setOrderStatus(null!=hubOrderDetail.getOrderStatus()?OrderStatus.getOrderStatus(hubOrderDetail.getOrderStatus()):null);
        orderDTO.setPushStatus(null!=hubOrderDetail.getPushStatus()?PushStatus.getPushStatus(hubOrderDetail.getPushStatus()):null);
        orderDTO.setSpOrderId(hubOrderDetail.getOrderNo());
        orderDTO.setDetail(hubOrderDetail.getSupplierSkuNo()+":1");
        orderDTO.setPurchasePriceDetail(null==hubOrderDetail.getPurchasePrice()?null:hubOrderDetail.getPurchasePrice().toString());
        return orderDTO;
    }

    /**
     * 从  OrderDTO 到   HubOrderDetail
     * @param orderDTO
     * @return
     */
    public  HubOrderDetail changeOrderDTOToHubOrderDetail(OrderDTO orderDTO){
        HubOrderDetail hubOrderDetail = new  HubOrderDetail();
        BeanUtils.copyProperties(orderDTO,hubOrderDetail);
        hubOrderDetail.setOrderNo(orderDTO.getSpOrderId());
        if(StringUtils.isNotBlank(orderDTO.getPurchasePriceDetail())){
            hubOrderDetail.setPurchasePrice(new BigDecimal(orderDTO.getPurchasePriceDetail()));
        }
        if(null!=orderDTO.getOrderStatus()){
            hubOrderDetail.setOrderStatus(orderDTO.getOrderStatus().getIndex());
        }
        if(null!=orderDTO.getPushStatus()){
            hubOrderDetail.setPushStatus(orderDTO.getPushStatus().getIndex());
        }


        return hubOrderDetail;
    }

    /**
     * 按供货商级别获取中间对象 包含数据库里的订单状态 和 异常时的状态
     * @param message  消息体
     * @param headers 消息头
     * @return  Map<String, List<OrderDTO>>  key:supplierId 非supplierNo
     */
    public Map<String, List<OrderDTO>>  getOrderDTO(SupplierOrderSync message, Map<String, Object> headers) throws Exception{
        Map<String, List<OrderDetailMq>> supplierOrderListMap = this.getSplitOrderBySupplier(message);
        Set<String> supplierNoSet = supplierOrderListMap.keySet();
        Map<String, List<OrderDTO>>  result = new   HashMap<String, List<OrderDTO>> ();
        for(String supplierNo:supplierNoSet){
            List<OrderDetailMq> detailMqs = supplierOrderListMap.get(supplierNo);
            result.put(this.getSopUserNo(supplierNo),this.getSplitOrder(message.getSyncType(),supplierNo,message.getOrderNo(),
                    detailMqs,headers));
        }
        return result;
    }

    /**
     * 按供货商级别拆单
     * @param message
     * @return
     */
    public  Map<String, List<OrderDetailMq>> getSplitOrderBySupplier(SupplierOrderSync message) {

        List<SupplierOrderDetailSync> orderDetails = message.getSyncDetailDto();
        Map<String, List<OrderDetailMq>>  result = new HashMap<>();
        for(SupplierOrderDetailSync orderDetail:orderDetails){

            OrderDetailMq iceOrderDTO = new OrderDetailMq();
            iceOrderDTO.setPurchaseOrderNo(orderDetail.getPurchaseOrderNo());
            iceOrderDTO.setSupplierNo(orderDetail.getSupplierNo());
            iceOrderDTO.setQuantity(orderDetail.getQuantity());
            iceOrderDTO.setSkuNo(orderDetail.getSkuNo());
            iceOrderDTO.setSupplierOrderNo(orderDetail.getSupplierOrderNo());
            iceOrderDTO.setSpMasterOrderNo(message.getOrderNo());
            iceOrderDTO.setOrderNo(orderDetail.getOrderNo());
            iceOrderDTO.setMessageId(message.getMessageId());
            iceOrderDTO.setParentMessageId(message.getParentMessageId());
            iceOrderDTO.setSyncType(message.getSyncType());
            iceOrderDTO.setMessageDate(message.getMessageDate());
            iceOrderDTO.setOriginalSupplierOrderNo(orderDetail.getOriginalSupplierOrderNo());
            if(result.containsKey(orderDetail.getSupplierNo())){
                result.get(orderDetail.getSupplierNo()).add(iceOrderDTO);
            }else{
                List<OrderDetailMq> hubOrderDetails = new ArrayList<>();
                hubOrderDetails.add(iceOrderDTO);
                result.put(orderDetail.getSupplierNo(),hubOrderDetails);
            }
        }
        return result;
    }



    /**
     * 根据业务类型获取转化后的信息
     * 如果返回的对象列表是空 说明消息队列的处理速度有问题 需要推入异常的消息队列
     * @param orderBusinessType  ：业务类型
     * @param supplierNo  :供货商编号
     * @param spMasterOrderNo :主订单编号
     * @param orderDetailMqs  ：消息体内订单信息
     * @param headers
     * @return
     */
    private   List<OrderDTO> getSplitOrder(String   orderBusinessType , String supplierNo,String spMasterOrderNo, List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers ) throws Exception{

        //设置供货商的的对应关系
        List<OrderDTO>  result  = new ArrayList<>();
        SupplierDTO supplierDTO= this.getSupplier(supplierNo);
        if(null==supplierDTO){
            return  result;
        }
        String supplierId = supplierDTO.getSupplierId();

        Date date = new Date();
        //创建订单
        //保存订单状态时 一定要同时保存，不论是否多个供货商
        if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_CREATE_ORDER)){   //下单
            handlePlace(spMasterOrderNo, orderDetailMqs, headers, result, date);
        }else if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_CANCEL_ORDER)){   //取消
            if (handleCancel(spMasterOrderNo, orderDetailMqs, headers, result)) return result;

        }else if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_PAYED_ORDER)){ //支付

            handlePay(orderDetailMqs, headers, result, date);

        }else if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_REFUNDED)){ //退款
            if (handleRefunded(spMasterOrderNo, orderDetailMqs, headers, result, supplierId)) return result;

        }else if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_REPURCHASE_ORDER)){ //重采
            if (handleRePurchase(spMasterOrderNo, orderDetailMqs, headers, result, supplierId, date)) return result;

        }else if(orderBusinessType.equals(GlobalConstant.SYNC_TYPE_SHIPPED)){  //发货
            handleShipped(orderDetailMqs, headers, result);
        }
        return result;
    }

    private void handlePlace(String spMasterOrderNo, List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result, Date date) throws Exception{
        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else{
             //创建
            createOrderDTO(orderDetailMqs, result, date,headers);
        }
    }

    private void handleShipped(List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result) throws Exception {
        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else {
            for (OrderDetailMq orderDetailMq : orderDetailMqs) { //发货一件一件的发
                HubOrderDetail orderDetail = hubOrderService.getOrderDetailByPurchaseNo(orderDetailMq.getPurchaseOrderNo());
                if (null != orderDetail) {
                    OrderDTO orderDTO = this.changeHubOrderDetailToOrderDTO(orderDetail);
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_SHIPPED);
                    result.add(orderDTO);
                }
            }
        }
    }

    private boolean handleRePurchase(String spMasterOrderNo, List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result, String supplierId, Date date) throws Exception {
        if (isExistOfOrder(spMasterOrderNo)) return true;
        //重采包含一个信息 处理两条订单信息

        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else{
            for(OrderDetailMq orderDetailMq:orderDetailMqs){ //仅有一个
                //获取原来的订单信息
                HubOrderDetail orderDetail = hubOrderService.getOrderDetailBySpOrderDetailNo(orderDetailMq.getOriginalSupplierOrderNo());
                if(null!=orderDetail) {
                    OrderDTO orderDTO = this.changeHubOrderDetailToOrderDTO(orderDetail);
                    orderDTO.setBusinessOperate(OrderBusinessOperateType.OPERATE_PURCHASE_EXCEPTION);
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_REPURCHASE_ORDER);
                    result.add(orderDTO);
                }

                //插入新的订单明细
                OrderDTO newOrderDTO = this.getNewOrderDTO(date,orderDetailMq,headers);
                newOrderDTO.setSpOrderId(orderDetailMq.getSupplierOrderNo());//重采的订单编号 直接使用新的子订单编号
                newOrderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_REPURCHASE_ORDER);
                newOrderDTO.setBusinessOperate(OrderBusinessOperateType.OPERATE_CREATE_NEW_ORDER);
                newOrderDTO.setSpOrderDetailNo(orderDetailMq.getSupplierOrderNo());
                newOrderDTO.setPurchaseNo(orderDetailMq.getPurchaseOrderNo());
                result.add(newOrderDTO);
            }
        }
        return true;
    }

    private boolean handleRefunded(String spMasterOrderNo, List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result, String supplierId) throws Exception{
        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else{
            for(OrderDetailMq orderDetailMq:orderDetailMqs){
                OrderDTO orderDTO = null;
                HubOrderDetail orderDetail = hubOrderService.getOrderDetailBySupplierIdAndSpOrderDetailNo(supplierId,
                        orderDetailMq.getSupplierOrderNo());
                if(null==orderDetail){
                    //插入新的订单明细

                    orderDTO = this.getNewOrderDTO(new Date(),orderDetailMq,headers);
                    orderDTO.setSpOrderId(orderDetailMq.getOrderNo());
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_REFUND_ORDER);
                    orderDTO.setBusinessOperate(OrderBusinessOperateType.OPERATE_CREATE_NEW_ORDER);
                    orderDTO.setSpOrderDetailNo(orderDetailMq.getSupplierOrderNo());
                    orderDTO.setPurchaseNo(orderDetailMq.getPurchaseOrderNo());

                }else{
                    orderDTO = this.changeHubOrderDetailToOrderDTO(orderDetail);
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_REFUND_ORDER);
                }

                result.add(orderDTO);
            }
        }
        return false;
    }

    private boolean handleCancel(String spMasterOrderNo, List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result) throws Exception {
        if (isExistOfOrder(spMasterOrderNo)) return true;
        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else{ //未有异常
            for(OrderDetailMq orderDetailMq:orderDetailMqs){ //获取数据库的订单信息
//                List<OrderDTO> orderDetails = hubOrderService.getOrderMsgBySupplierIdAndSpMasterOrderNoAndSpSkuNo(this.getSopUserNo(orderDetailMq.getSupplierNo()),
//                        spMasterOrderNo,orderDetailMq.getSkuNo());
                HubOrderDetail hubOrderDetail = hubOrderService.getOrderDetailBySupplierNoAndOrderNo(orderDetailMq.getSupplierNo(),orderDetailMq.getOrderNo());
                if(null!=hubOrderDetail){
                    OrderDTO orderDTO = this.changeHubOrderDetailToOrderDTO(hubOrderDetail);
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_CANCEL_ORDER);
                    result.add(orderDTO);
                }

            }
        }
        return false;
    }

    private void handlePay(List<OrderDetailMq> orderDetailMqs, Map<String, Object> headers, List<OrderDTO> result, Date date) throws Exception {
        if(isExceptionMq(headers)){//有异常
            setOrderDTOPropertyForException(orderDetailMqs, result,headers);
        }else{
            //按商品的SKU合并信息 ，以便处理同一个订单中购买多个相同的产品
            Map<String,List<OrderDetailMq>>  orderDetailMap = new HashMap<>();
            for(OrderDetailMq orderDetailMq:orderDetailMqs) {
                if(orderDetailMap.containsKey(orderDetailMq.getSkuNo())){
                    orderDetailMap.get(orderDetailMq.getSkuNo()).add(orderDetailMq);
                }else{
                    List<OrderDetailMq>  orderDetailMqList = new ArrayList<>();
                    orderDetailMqList.add(orderDetailMq);
                    orderDetailMap.put(orderDetailMq.getSkuNo(),orderDetailMqList);
                }
            }

            Set<String> skuSet = orderDetailMap.keySet();
            for(String skuNo:skuSet){
                List<OrderDetailMq>  orderDetailOfSku =  orderDetailMap.get(skuNo);//获取消息队列中的订单信息
                for(OrderDetailMq orderDetailMq:orderDetailOfSku){
                    HubOrderDetail orderDetail = hubOrderService.getOrderDetailBySupplierNoAndOrderNo(orderDetailMq.getSupplierNo(),orderDetailMq.getOrderNo());
                    OrderDTO orderDTO = null;
                    if(null==orderDetail){//下单的消息尚未过来 ，创建新的单子
                        orderDTO = getNewOrderDTO( date, orderDetailMq,headers);
                    } else{ //获取老的单子
                        orderDTO =this.changeHubOrderDetailToOrderDTO(orderDetail);
                    }
                    orderDTO.setPurchaseNo(orderDetailMq.getPurchaseOrderNo());
                    orderDTO.setSpOrderDetailNo(orderDetailMq.getSupplierOrderNo());//尚品的订单明细编号
                    orderDTO.setMessageId(orderDetailMq.getMessageId());
                    orderDTO.setParentMessageId(orderDetailMq.getParentMessageId());
                    orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_PAYED_ORDER);
                    result.add(orderDTO);
                }

                //按尚品的SKU 把此供货商下的所有订单均查询出来
                //保存订单状态时 一定要同时保存，不论是否多个供货商 ，然后单独去处理推送 否则会发生混乱
//                    List<HubOrderDetail> orderDetails = hubOrderService.getOrderDetailBySupplierIdAndSpMasterOrderNoAndSpSkuNo(supplierId,spMasterOrderNo,skuNo);
//                    for(HubOrderDetail orderDetail:orderDetails){
//                        OrderDTO orderDTO = this.changeHubOrderDetailToOrderDTO(orderDetail);
//                        if(StringUtils.isBlank(orderDetail.getPurchaseNo())){ //尚未赋值
//                            OrderDetailMq orderDetailMq =  orderDetailOfSku.get(0);
//                            orderDTO.setPurchaseNo(orderDetailMq.getPurchaseOrderNo());
//                            orderDTO.setSpOrderDetailNo(orderDetailMq.getSupplierOrderNo());//尚品的订单明细编号
//                            orderDetailOfSku.remove(0);
//                        }
//                        result.add(orderDTO);
//                    }
            }
        }
    }

    /**
     * 创建新的订单信息
     * @param date
     * @param orderDetailMq
     * @return
     */
    private OrderDTO getNewOrderDTO( Date date, OrderDetailMq orderDetailMq,Map<String, Object> headers) throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUuid(UUIDGenerator.getUUID());
        orderDTO.setSupplierNo(orderDetailMq.getSupplierNo());
        orderDTO.setSupplierId(this.getSopUserNo(orderDetailMq.getSupplierNo()));
        orderDTO.setSpMasterOrderNo(orderDetailMq.getSpMasterOrderNo());
        orderDTO.setQuantity(1);
        orderDTO.setSpSkuNo(orderDetailMq.getSkuNo());
        orderDTO.setSupplierSkuNo(this.getSupplierSkuNo(orderDTO.getSupplierId(),orderDTO.getSpSkuNo(),headers));
        orderDTO.setDetail(orderDTO.getSupplierSkuNo()+":1");
        orderDTO.setCreateTime(date);
        orderDTO.setPurchasePriceDetail("10");//无法获取 现临时保存1
        orderDTO.setSpOrderId(orderDetailMq.getOrderNo());
        orderDTO.setOrderStatus(OrderStatus.NO_PAY);
        orderDTO.setMessageId(orderDetailMq.getMessageId());
        orderDTO.setParentMessageId(orderDetailMq.getParentMessageId());

        return orderDTO;
    }

    /**
     * 创建新的DTO
     * @param orderDetailMqs
     * @param result
     * @param date
     */
    private void createOrderDTO(List<OrderDetailMq> orderDetailMqs, List<OrderDTO> result, Date date,Map<String, Object> headers) throws Exception {
        int num = 1;
        for(OrderDetailMq orderDetailMq:orderDetailMqs){                   ;
            for(int i =0;i< orderDetailMq.getQuantity();i++){
                OrderDTO orderDTO = this.getNewOrderDTO(date,orderDetailMq,headers);

                if(1==orderDetailMqs.size()&&1==orderDetailMq.getQuantity()){//仅有一个订单且只购买了一件
                    orderDTO.setSpOrderId(orderDetailMq.getOrderNo());
                }else{
                    orderDTO.setSpOrderId(orderDetailMq.getSpMasterOrderNo()+num);
                    num++;
                }
                orderDTO.setBusinessType(OrderBusinessType.SYNC_TYPE_CREATE_ORDER);
                result.add(orderDTO);
            }
        }
    }

    /**
     * 判断订单是否存在
     * @param spMasterOrderNo
     * @return
     */
    private boolean isExistOfOrder(String spMasterOrderNo) {
        HubOrder order = hubOrderService.getHubOrderBySpMasterOrderNo(spMasterOrderNo);
        if(null==order){//消息队列处理速度混乱 返回空值
            return true;
        }
        return false;
    }


    /**
     * 判断订单是否存在
     * @param orderNo
     * @return
     */
    private boolean isExistOfOrderDetail(String supplierNo,String orderNo) {
        HubOrderDetail  order = hubOrderService.getOrderDetailBySupplierNoAndOrderNo(supplierNo,orderNo);
        if(null==order){//消息队列处理速度混乱 返回空值
            return true;
        }
        return false;
    }



    private void setOrderDTOPropertyForException(List<OrderDetailMq> orderDetailMqs, List<OrderDTO> result,Map<String, Object> headers) throws Exception {
        for(OrderDetailMq orderDetailMq:orderDetailMqs) { //获取数据库的订单信息
            boolean isCreateOrderException = true;
            // 发生了错误  如果是重采的业务，需要知道是老订单出现问题 还是新单子出现了问题
           if(StringUtils.isNotBlank(orderDetailMq.getOriginalSupplierOrderNo())){
               HubOrderDetail oldOOrderDetail = hubOrderService.getOrderDetailBySpOrderDetailNo(orderDetailMq.getOriginalSupplierOrderNo());
               if(null!=oldOOrderDetail){
                   OrderDTO oldOrderDTO  = this.changeHubOrderDetailToOrderDTO(oldOOrderDetail);
                   oldOrderDTO.setBusinessOperate(OrderBusinessOperateType.OPERATE_PURCHASE_EXCEPTION);

                   //判断是否是采购异常
                   if(((Integer)headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)==OrderStatus.PURCHASE_EXCEPTION.getIndex()||
                           (Integer)headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)==OrderStatus.PURCHASE_EXCEPTION_FAKE.getIndex())){
                       oldOrderDTO.setExceptionOrderStatus(OrderStatus.getOrderStatus((Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)));
                       oldOrderDTO.setExceptionPushStatus(PushStatus.getPushStatus((Integer)headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY)));
                       isCreateOrderException = false;
                   }
                   result.add(oldOrderDTO);
               }

           }


            String orderNo =  orderDetailMq.getOrderNo();//新的单子
            HubOrderDetail orderDetail = hubOrderService.getOrderDetailBySupplierNoAndOrderNo(orderDetailMq.getSupplierNo(),orderNo);
            OrderDTO newOrderDTO = null;
            if(null!=orderDetail){
                newOrderDTO = this.changeHubOrderDetailToOrderDTO(orderDetail);
                newOrderDTO.setMessageId(orderDetailMq.getMessageId());
                newOrderDTO.setParentMessageId(orderDetailMq.getParentMessageId());

            }else{
                Date date = new Date();
                newOrderDTO = getNewOrderDTO( date, orderDetailMq,headers);
                newOrderDTO.setBusinessOperate(OrderBusinessOperateType.OPERATE_CREATE_NEW_ORDER);
            }
            if(isCreateOrderException){
                if(null!=headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)){
                    newOrderDTO.setExceptionOrderStatus(OrderStatus.getOrderStatus((Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)));
                }
                if(null!=headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY)) {
                    newOrderDTO.setExceptionPushStatus(PushStatus.getPushStatus((Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY)));
                }
            }

            result.add(newOrderDTO);

        }
    }

    /**
     * 判断是否是异常的消息体
     * @param headers : true 异常消息体
     * @return
     */
    public  boolean isExceptionMq(Map<String, Object> headers){
        if(null==headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY)&&null==headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY)){
            return  false;
        }else{
            return true;
        }
    }


    /**
     * 获取尚品门户编号
     * @param supplierNo   ：供货商编号
     * @return
     */
    public String getSopUserNo(String supplierNo){

        SupplierDTO supplierDTO = this.getSupplier(supplierNo);


        if(null!=supplierDTO){
//            //非对接的的直接返回空对象 ，不做处理
//            if(SupplierAPIType.STOCK_API.getIndex()!=supplierDTO.getInterfaceType()&&
//                    SupplierAPIType.ORDER_LOCK_API.getIndex()!=supplierDTO.getInterfaceType()&&
//                    SupplierAPIType.ORDER_NO_LOCK_API.getIndex()!=supplierDTO.getInterfaceType()){
//                return "";
//            }
            return supplierDTO.getSupplierId();
        }else{
            return "";
        }







    }

    /**
     * 通过供货商门户编号查询供货商对象
     * @param supplierNo
     * @return
     */
    public  SupplierDTO getSupplier(String supplierNo) {
        SupplierDTO dto = null;
        //先获取缓存中的数据
        String supplierMsg = shangpinRedis.get(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo);
        ObjectMapper om = new ObjectMapper();
        if(StringUtils.isNotBlank(supplierMsg)){
            try {
                dto = om.readValue(supplierMsg, SupplierDTO.class);
                SupplierCommon supplierCommon = orderHandleSearch.getSupplierProperty(dto.getSupplierId());
                if(null!=supplierCommon){
                    dto.setOpenApiKey(supplierCommon.getOpenApiKey());
                    dto.setOpenApiSecret(supplierCommon.getOpenApiSecret());
                    dto.setIsPurchaseException("true".equals(supplierCommon.getIsPurchaseExp())?true:false);
                    dto.setInterfaceType(3);
                }
                return dto;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //调用接口获取供货商信息
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("supplierNo", supplierNo);

        try {
            supplierMsg = HttpUtil45.get(supplierProperties.getSupplier().getServiceUrl()+supplierNo,new OutTimeConfig(1000*3,1000*5,1000*5),null);
            if(StringUtils.isNotBlank(supplierMsg)){

                dto = om.readValue(supplierMsg, SupplierDTO.class);
                if(null!=dto){
                    if(null!=dto.getSopUserNo()){
                        dto.setSupplierId(dto.getSopUserNo().toString());
                        dto.setSupplierNo(supplierNo);
                    }
                }
            }else{
                //如果接口挂掉 启用备用的方案 走配置文件
                if (null == dto) {//
                    dto = new SupplierDTO();
                    if (orderSupplierMap.containsKey(supplierNo)) {
                        dto.setSupplierId(orderSupplierMap.get(supplierNo));
                        dto.setInterfaceType(3);
                    }
                }

            }
        } catch (Exception e) {
            LogCommon.recordLog("获取供货商信息失败:"+e.getMessage(),LogLeve.ERROR);
            if (null == dto) {//
                dto = new SupplierDTO();
                if (orderSupplierMap.containsKey(supplierNo)) {
                    dto.setSupplierId(orderSupplierMap.get(supplierNo));
                    dto.setInterfaceType(3);
                }
            }
            e.printStackTrace();
        }




        SupplierCommon supplierCommon = orderHandleSearch.getSupplierProperty(dto.getSupplierId());
        if(null!=supplierCommon){
            dto.setOpenApiKey(supplierCommon.getOpenApiKey());
            dto.setOpenApiSecret(supplierCommon.getOpenApiSecret());
            dto.setIsPurchaseException("true".equals(supplierCommon.getIsPurchaseExp())?true:false);
            dto.setInterfaceType(3);
        }

        //记录到REDIS缓存中

        try {
            shangpinRedis.setex(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo,1000*60*5,om.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  dto;
    }


    /**
     * 获取供货商的SKU
     * 先从缓存取 如果没有再从数据库中取
     * @param supplierId
     * @param spSkuNo
     * @return
     * @throws ServiceException
     */
    public String getSupplierSkuNo(String supplierId,String spSkuNo,Map<String, Object> headers) throws Exception {
        //先获取缓存
        String supplierSkuNo =  shangpinRedis.get(supplierId+"_"+spSkuNo);
        if(StringUtils.isBlank(supplierSkuNo)){
            HubSkuRelation dto = hubSkuRelationService.getSkuRelationBySupplierIdAndSpSkuNo(supplierId,spSkuNo);
            if(null==dto){
                IOrderService iOrderService = orderHandleSearch.getHander(supplierId);
                if(null==iOrderService){//无订单对接的忽略对应关系
                    return "";
                }else{
                    throw  new ServiceMessageException("　getSupplierSkuNo: " +supplierId+"_"+spSkuNo +"未找到对应供货商的SKU编号");
                }
            }else{
                supplierSkuNo = dto.getSupplierSkuId();
                shangpinRedis.setex(supplierId+"_"+spSkuNo,1000*60*60,supplierSkuNo);
            }
        }
        return supplierSkuNo;
    }

    /**
     * 发送邮件 默认
     * @param title 标题
     * @param text  内容
     */
    public void sendMail(String title,String text){
        ShangpinMail shangpinMail = new ShangpinMail();
        shangpinMail.setSubject(title);
        shangpinMail.setText(text);
        try {
            shangpinMailSender.sendShangpinMail(shangpinMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置异常到消息头中
     * @param headers
     * @param orderDTO
     */
    public  void setStatusIntoHeader(Map<String, Object> headers, OrderDTO orderDTO) {
        if(null!=orderDTO.getPushStatus()){

            headers.put(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY,orderDTO.getPushStatus().getIndex());
        }
        if(null!=orderDTO.getOrderStatus()){

            headers.put(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY,orderDTO.getOrderStatus().getIndex());
        }
    }
}
