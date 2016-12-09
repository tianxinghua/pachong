package com.shangpin.ep.order.push.repurchase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.bean.HubOrderLog;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.HubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;
import com.shangpin.ep.order.push.common.AbstractPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;
import com.shangpin.ep.order.push.common.HandleMessageException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2016/11/21.
 */
@Service
@Slf4j
public class RePurchaseOrderPusher extends AbstractPusher {

    @Autowired
    OrderCommonUtil  orderCommonUtil;

   @Autowired
    IHubOrderLogService orderLogService;
    
    @Autowired
    OrderHandleSearch orderHandleSearch;

    @Autowired
    HubOrderService hubOrderService;



    @Autowired
    HandleMessageException handleMessageException;


    @Autowired
    OpenApiService openApiService;

    @Autowired
    SupplierProperties supplierProperties;
    /**
     * 重新采购
     * 重新采购分为两种情况：
     * 1、订单对接
     *    订单对接 无库存 设置采购异常 触发重新采购 此时原单号的订单状态已经是采购异常了 不需要再做处理
     *    只需要处理新单
     * 2、非订单对接
     *     修改原来的订单状态为采购异常，处理新的单子
     *
     * @param message
     * @param headers
     */
    public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception{
        //记录日志
        LogCommon.recordLog(" repurchase order message origin content :"+message.toString()+
                "　message-header content :" +headers.toString(), LogLeve.INFO);
        try {
            handleRePurchase(message, headers);
        } catch (Exception e) {
            handleMessageException.handleException(message,headers,e);
            //内部延迟短些 推送的延迟产些 以秒为单位（1000代表一秒）
            int delay = supplierProperties.getSupplier().getDelayTime();
            this.reTry(message,delay*60,(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY),(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY),
                    (null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)),null);

        }
    }

    /**
     * 重采的处理逻辑
     * @param message
     * @param headers

     */
    private void handleRePurchase(SupplierOrderSync message, Map<String, Object> headers) throws Exception{


        Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
        if(null!=supplierOrderListMap){
            Set<String> supplierIdSet = supplierOrderListMap.keySet();
            for(String supplieId:supplierIdSet){//循环处理订单
                List<OrderDTO> orderDTOs= supplierOrderListMap.get(supplieId);

                for(OrderDTO orderDTO:orderDTOs) {
                    //先设置订单状态
                    orderCommonUtil.setStatusIntoHeader(headers,orderDTO);

                    if(orderDTO.getBusinessOperate().getIndex()==OrderBusinessOperateType.OPERATE_PURCHASE_EXCEPTION.getIndex()){ //老单子处理
                        handleOldOrderStatus(headers,orderDTO);
                        //采购异常修改推送状态
                        handlePurchaseExceptionPush(orderDTO,headers);
                    }else{ //新单子
                        handleNewOrderStatus(orderDTO);
                        //获取供货商处理的实现类
                        IOrderService iOrderService = orderHandleSearch.getHander(orderDTO.getSupplierId());
                        if(null!=iOrderService){
                            this.handlePush(iOrderService,orderDTO,headers);

                        }
                    }

                }
            }

        }else{ //重新处理  认为是新数据
           throw new ServiceMessageException("需要重新处理");
        }

    }




    /**
     * 处理推送
     * @param iOrderService   :具体推送的实现类
     * @param orderDTO
     * @param headers
     * @throws ServiceException
     */
    private void handlePush(IOrderService iOrderService, OrderDTO orderDTO, Map<String, Object> headers) throws  Exception{

        orderDTO.setErrorType(null);
        orderDTO.setDescription("");

        if(isNeedHandle(orderDTO,HandleStep.HANDLE_LOCK_PUSH)){
            iOrderService.handleSupplierOrder(orderDTO);
            this.handlePushStauts(orderDTO, headers);
        }else{
            if(isNeedHandleLockStatus(orderDTO)){
                this.handlePushStauts(orderDTO, headers);

            }
        }

        if(isNeedHandle(orderDTO,HandleStep.HANDLE_CONFIRM_PUSH)){
            iOrderService.handleConfirmOrder(orderDTO);
            this.handlePushStauts(orderDTO, headers);

        }else{
            if(isNeedHandleConfrimStatus(orderDTO)){
                this.handlePushStauts(orderDTO, headers);
            }
        }

        //推送发生错误 需要重新发送
        if(null!=orderDTO.getErrorType()){
            throw new Exception("重采推送出错，需要重试.错误原因: " + orderDTO.getLogContent());
        }
        //是否需要设置采购异常
        if(isNeedHandle(orderDTO,HandleStep.HANDLE_PURCHASE_EXCEPTION)){
            setPurchaseException(headers,orderDTO);

        }


    }

    /**
     * 采购异常推送
     * 采购异常 说明供货商没货 不需要退货 只需把采购单状态设置成NO_STOCK

     * @param orderDTO
     * @param headers
     * @throws Exception
     */
    private void handlePurchaseExceptionPush( OrderDTO orderDTO, Map<String, Object> headers) throws  Exception{
        IOrderService iOrderService = orderHandleSearch.getHander(orderDTO.getSupplierId());
        if(null!=iOrderService) {
            orderDTO.setPushStatus(PushStatus.NO_STOCK);
        }
        hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
        orderCommonUtil.setStatusIntoHeader(headers,orderDTO);

    }


    private void handlePushStauts(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {

        hubOrderService.updateHubOrderDetailPushStatus(orderDTO);

        //推送发生错误 需要重新发送
        if(null!=orderDTO.getErrorType()){
            orderCommonUtil.setStatusIntoHeader(headers, orderDTO);
            throw new Exception("");
        }
    }

    /**
     * 更新旧的订单的状态
     * @param headers
     * @param orderDTO   订单
     */
    private void handleOldOrderStatus(Map<String, Object> headers,  OrderDTO orderDTO) throws Exception {

        if(orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION.getIndex()||
                orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION_FAKE.getIndex()){

        }else { //如果状态不是采购异常的 需要更新
            try {
                //重采的一定是已经采购异常了 EP库只需要把状态设置成采购异常
                orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION);
                hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);
            } catch (Exception e) {
                orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
                throw e;
            }
            //记录日志
            orderLogService.saveOrderLog(orderDTO);
        }

    }

    /**
     * 更新旧的订单的状态 保存新的订单
     * @param orderDTO   订单
     */
    private void handleNewOrderStatus(OrderDTO orderDTO) throws Exception {
        //处理业务
        try {
            Date date = new Date();


            orderDTO.setOrderStatus(OrderStatus.PAYED);
            orderDTO.setPayTime(date);
            hubOrderService.saveOrderDetail(orderDTO);
            //记录日志
            orderLogService.saveOrderLog(orderDTO);

        } catch (Exception e) {
            e.printStackTrace();
            LogCommon.recordLog(e.getMessage());
            if(e instanceof ServiceException){
                throw new ServiceMessageException(ExceptionType.DATABASE_HANDLE_EXCEPTION.toString(),"数据库操作失败");
            }else{
                throw e;
            }


        }
    }


    /**
     * 单独更新订单的锁库推送状态 说明有异常发生 只能是数据库更新时挂了
     * @param orderDTO
     * @return
     */
    private boolean isNeedHandleLockStatus(OrderDTO orderDTO){
        Boolean isNeed = false;
        if(null==orderDTO.getPushStatus()){ //无状态
            if(null!=orderDTO.getExceptionPushStatus()) { //有异常
                orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
                isNeed = true;
            }
        }else{//订单推送有状态
            if(null!=orderDTO.getExceptionPushStatus()) {//
                if (orderDTO.getPushStatus().getIndex() != orderDTO.getExceptionPushStatus().getIndex()) {//状态不相等
                    if(orderDTO.getExceptionPushStatus().getIndex()==PushStatus.LOCK_PLACED_ERROR.getIndex()||
                            orderDTO.getExceptionPushStatus().getIndex()==PushStatus.LOCK_PLACED.getIndex()){
                        orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
                        isNeed = true;
                    }
                }
            }
        }

        return isNeed;
    }

    /**
     * 获取订单日志对象
     * @param orderDetail
     * @return
     */
    private HubOrderLog getHubOrderLog(HubOrderDetail orderDetail) {
        HubOrderLog hubOrderLog = new HubOrderLog();
        hubOrderLog.setOrderDetailId(orderDetail.getId().intValue());
        hubOrderLog.setOrderStatus(orderDetail.getOrderStatus());
        hubOrderLog.setPushStatus(orderDetail.getPushStatus());
        hubOrderLog.setOperateType(OrderBusinessType.SYNC_TYPE_REPURCHASE_ORDER.getIndex());
        hubOrderLog.setCreateTime(new Date());
        return hubOrderLog;
    }






    /**
     * 判断是否需要操作下一步 ture :需要
     * @param orderDTO
     * @param handleStep
     * @return
     * @throws Exception
     */
    private boolean isNeedHandle(OrderDTO orderDTO, HandleStep handleStep) throws Exception {
        Boolean isNeed = true;
        if(null==orderDTO.getExceptionOrderStatus()) {//新的消息 需要处理
            if(handleStep.getIndex()!=HandleStep.HANDLE_PURCHASE_EXCEPTION.getIndex()){
                return isNeed;
            }
        }

        if(handleStep.getIndex()==HandleStep.HANDLE_ORDER.getIndex()){ //订单处理
            isNeed = judageOrderStep(orderDTO, isNeed);
        }else if((handleStep.getIndex()==HandleStep.HANDLE_LOCK_PUSH.getIndex())){//锁库推送处理
            isNeed = judgeLockPush(orderDTO);
        }else if((handleStep.getIndex()==HandleStep.HANDLE_CONFIRM_PUSH.getIndex())){//推送推送处理
            isNeed = judgeConfrimPush(orderDTO);
        }else if((handleStep.getIndex()==HandleStep.HANDLE_PURCHASE_EXCEPTION.getIndex())) {//采购异常
            if(orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()){ //已支付
                if(null==orderDTO.getPushStatus()){ //推送状态没有 说明是重采的 或者是下单流程尚未处理 支付流程先处理
                    isNeed = false;
                }else{
                    if(orderDTO.getPushStatus().getIndex()!=PushStatus.NO_STOCK.getIndex()) {
                        isNeed = false;
                    }
                }
            }
        }else{
            if(orderDTO.getPushStatus().getIndex()==orderDTO.getExceptionPushStatus().getIndex()) { //推送状态不等
                isNeed = false;
            }
        }
        return isNeed;
    }


    private boolean judageOrderStep(OrderDTO orderDTO, boolean isNeed) throws ServiceMessageException {
        if(orderDTO.getOrderStatus().getIndex()!=orderDTO.getExceptionOrderStatus().getIndex()){// 修改订单状态时失败
            if(orderDTO.getOrderStatus().getIndex()!= OrderStatus.NO_PAY.getIndex()&&
                    orderDTO.getOrderStatus().getIndex()!=OrderStatus.PAYED.getIndex()){
                //不能再做支付
                throw new ServiceMessageException("业务发生混乱,不能做处理");
            }

        }else{
            if(orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()){ //已支付 不做处理
                isNeed = false;
            }
        }
        return isNeed;
    }



    private boolean judgeLockPush(OrderDTO orderDTO) {
        Boolean isNeed = false;
        if(null==orderDTO.getPushStatus()){ //需要锁库  (下单消息还未处理 支付消息已经来了 )
            if(null==orderDTO.getExceptionPushStatus()) {//新的消息 需要处理
                isNeed = true;
            }else {
                if (orderDTO.getExceptionPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {//锁库失败了  保存数据库时失败  继续锁库
                    isNeed = true;
                }
            }
        }else{//订单推送有状态
            if(null==orderDTO.getExceptionPushStatus()) {//正常逻辑
                // 订单取消后，再次支付   或者支付时 发现原来的锁库异常 需要继续推送
                if (orderDTO.getPushStatus().getIndex() == PushStatus.LOCK_CANCELLED.getIndex() ||
                        orderDTO.getPushStatus().getIndex() == PushStatus.NO_LOCK_CANCELLED_API.getIndex()||
                        orderDTO.getPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {
                    isNeed = true;
                }
            }else{ //有异常
                if (orderDTO.getExceptionPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {//锁库失败 继续锁库
                    isNeed = true;
                }

            }
        }

        return isNeed;
    }
    private boolean judgeConfrimPush(OrderDTO orderDTO) {
        boolean isNeed = false;
        if(null==orderDTO.getExceptionPushStatus()) {//正常逻辑
            isNeed = true;
        }else{ //有异常
            if(orderDTO.getPushStatus().getIndex()==orderDTO.getExceptionPushStatus().getIndex()) {
                if (orderDTO.getPushStatus().getIndex() == PushStatus.ORDER_CONFIRMED_ERROR.getIndex()) { //推送异常 重新推送
                    isNeed = true;
                }
            }else{
                //更新库存失败 不需要做处理

            }

        }
        return isNeed;
    }

    /*** 单独更新订单的锁库推送状态 说明有异常发生 只能是数据库更新时挂了
     * @param orderDTO
     * @return
     */
    private boolean isNeedHandleConfrimStatus(OrderDTO orderDTO){
        Boolean isNeed = false;
        if(null!=orderDTO.getExceptionPushStatus()) {//
            if (orderDTO.getPushStatus().getIndex() != orderDTO.getExceptionPushStatus().getIndex()) {//状态不相等
                orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
                isNeed = true;

            }
        }
        return isNeed;
    }
    /**
     * 设置采购异常 返回true 不在继续执行
     * @param orderDTO
     * @return
     */
    private boolean setPurchaseException(Map<String, Object> headers,OrderDTO orderDTO) throws Exception{
        boolean result = false;

        //如果推送状态是NO_STOCK 需要采购异常
        Date date = new Date();
        orderDTO.setUpdateTime(date);
        if(orderDTO.getPushStatus().getIndex()==PushStatus.NO_STOCK.getIndex()){

            orderDTO.setPayTime(date);
            //设置库存为0
            SupplierDTO supplierDTO=  orderCommonUtil.getSupplier(orderDTO.getSupplierNo());

            openApiService.setSkuQuantity(supplierDTO.getOpenApiKey(),supplierDTO.getOpenApiSecret(),orderDTO.getSpSkuNo(),0);
            //设置采购异常
            if(null!=supplierDTO.getIsPurchaseException()&&supplierDTO.getIsPurchaseException()){
                try {
                    openApiService.setPurchaseException(supplierDTO.getOpenApiKey(),supplierDTO.getOpenApiSecret(),orderDTO.getPurchaseNo());
                } catch (Exception e) {
                    if(e instanceof  ServiceMessageException){//采购单尚未生成
                        orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION_ERROR);
                        orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
                        throw new Exception(e.getMessage());

                    }else{
                        throw e;
                    }
                }
                orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION);
            }else{
                orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION_FAKE);
            }

            //设置订单及推送的状态
            orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
            //更新状态
            hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);

            //增加订单状发生变化的日志
            orderLogService.saveOrderLog(orderDTO);
            result = true;
        }
        return result;

    }

}
