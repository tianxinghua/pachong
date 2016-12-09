package com.shangpin.ep.order.module.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.enumeration.ErrorStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.mapping.bean.HubSkuRelation;
import com.shangpin.ep.order.module.mapping.service.impl.HubSkuRelationService;
import com.shangpin.ep.order.module.order.bean.HubOrder;
import com.shangpin.ep.order.module.order.bean.HubOrderCriteria;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.HubOrderDetailCriteria;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.bean.SkuCountDTO;
import com.shangpin.ep.order.module.order.mapper.HubOrderDetailMapper;
import com.shangpin.ep.order.module.order.mapper.HubOrderMapper;
import com.shangpin.ep.order.module.order.service.IHubOrderService;

/**
 * <p>Title:HubOrderService.java </p>
 * 所有日志，调用通用的日志格式
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:20:11
 */
@Service

public class HubOrderService implements IHubOrderService {


    @Autowired
    HubOrderDetailMapper orderDetailDao;

    @Autowired
    HubOrderMapper orderDao;

    @Autowired
    SupplierProperties supplierProperties;

    @Autowired
    IShangpinRedis shangpinRedis;

    @Autowired
    HubSkuRelationService hubSkuRelationService;

    @Autowired
    ShangpinMailSender shangpinMailSender;

    @Autowired
    IHubOrderLogService hubOrderLogService;

    @Override
    public Map<String, Integer> getNoShippedSkuCount(String supplierId,boolean isOrderApi,boolean isSpSku, List<String> skus) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        Map<String,Integer> skuCountMap = new HashMap<>();
        try {
            if(isOrderApi){//订单对接
                List<SkuCountDTO>  skuCountDTOs = null;
                if(null==skus){//查询所有未发货的商品的数量
                    skuCountDTOs =  orderDetailDao.getSkuCountOfNoOrderApiBySupplierId(supplierId) ;

                }else{  //查询多个商品包含单个商品
                    if(isSpSku){//  按尚品的SKU查询
                        skuCountDTOs = orderDetailDao.getSkuCountOfOrderApiBySupplierIdAndSpSkuNo(supplierId,skus);
                    }else{  //按供货商的SKU查询
                        skuCountDTOs = orderDetailDao.getSkuCountOfOrderApiBySupplierIdAndSupplierSkuNo(supplierId,skus);
                    }
                }
                if(null!=skuCountDTOs&&skuCountDTOs.size()>0){
                    for(SkuCountDTO dto:skuCountDTOs){
                        skuCountMap.put(dto.getSkuNo(),dto.getQuantity());
                    }
                }

            }else{//非订单对接
                if(null==skus){//查询所有未发货的商品的数量
                   List<SkuCountDTO>  skuCountDTOs =  orderDetailDao.getSkuCountOfNoOrderApiBySupplierId(supplierId) ;
                    if(null!=skuCountDTOs&&skuCountDTOs.size()>0){
                        for(SkuCountDTO dto:skuCountDTOs){
                            skuCountMap.put(dto.getSkuNo(),dto.getQuantity());
                        }
                    }
                }else{ //
                    List<Integer> orderStatusList = new ArrayList<>();
                    orderStatusList.add(0);orderStatusList.add(2);
                    HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
                    criterion.andSupplierIdEqualTo(supplierId).andOrderStatusIn(orderStatusList);
                    //判断是否是尚品的SKUNO 还是供货商的SKUNO
                    if(isSpSku){//尚品的SKUNO
                        criterion.andSpSkuNoIn(skus);
                    }else{
                        criterion.andSupplierSkuNoIn(skus);
                    }
                    List<HubOrderDetail>  details = orderDetailDao.selectByExample(criteria);
                    if(null!=details&&details.size()>0){
                        for(HubOrderDetail dto:details){
                            skuCountMap.put(dto.getSpSkuNo(),dto.getQuantity());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogCommon.recordLog("查询供货商：" + supplierId + " 尚未发货的商品数量时失败"  ,e);
        }

        return skuCountMap;
    }

    @Override
    public List<OrderDTO> getOrderMsgBySupplierIdAndSpMasterOrderNoAndSpSkuNo(String supplierId, String spMasterOrderNo, String spSkuNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId);
        criterion.andSpMasterOrderNoEqualTo(spMasterOrderNo);
        criterion.andSpSkuNoEqualTo(spSkuNo);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
            orderDTOs.add(this.changeHubOrderDetailToOrderDTO(orderDetail));
        }
        return   orderDTOs;

    }

    @Override
    public List<HubOrderDetail> getOrderDetailBySupplierIdAndSpMasterOrderNoAndSpSkuNo(String supplierId, String spMasterOrderNo, String spSkuNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId);
        criterion.andSpMasterOrderNoEqualTo(spMasterOrderNo);
        criterion.andSpSkuNoEqualTo(spSkuNo);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        return  orderDetails;
    }


    @Override
    public HubOrderDetail getOrderDetailBySupplierIdAndSpOrderDetailNo(String supplierId, String spOrderDetailNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId);
        criterion.andSpOrderDetailNoEqualTo(spOrderDetailNo);

        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
           return orderDetail;
        } else{
            return null;
        }
    }

    @Override
    public HubOrderDetail getOrderDetailBySpOrderDetailNo(String spOrderDetailNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSpOrderDetailNoEqualTo(spOrderDetailNo);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
            return orderDetail;
        } else{
            return null;
        }
    }


    @Override
    public HubOrderDetail getOrderDetailByPurchaseNo(String purchaseNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andPurchaseNoEqualTo(purchaseNo);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
            return orderDetail;
        } else{
            return null;
        }
    }

    @Override
    public HubOrderDetail getOrderDetailBySupplierIdAndOrderNo(String supplierId,String orderNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andOrderNoEqualTo(orderNo).andSupplierIdEqualTo(supplierId);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
            return orderDetail;
        } else{
            return null;
        }
    }

    @Override
    public HubOrderDetail getOrderDetailBySupplierNoAndOrderNo(String suppliernNo,String orderNo) {
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andOrderNoEqualTo(orderNo).andSupplierNoEqualTo(suppliernNo);
        List<HubOrderDetail> orderDetails =  orderDetailDao.selectByExample(criteria);
        if(null!=orderDetails&&orderDetails.size()>0){
            HubOrderDetail orderDetail  =   orderDetails.get(0);
            return orderDetail;
        } else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public boolean saveOrderAndOrderDetails( Map<String, List<OrderDTO>> supplierOrderMsg) throws Exception {
        Set<String>  keySet =  supplierOrderMsg.keySet();
        Date date = new Date();
        String supplierNo="";
        for(String supplierId:keySet){
            List<OrderDTO>  orderDTOs = supplierOrderMsg.get(supplierId);
            if(null==orderDTOs||orderDTOs.isEmpty()){
                throw new ServiceMessageException("无消息体内容");
            }
            //转化临时对象为数据库对象
            String spMasterOrderNo ="";
            for(OrderDTO orderDTO:orderDTOs) {
                HubOrderDetail hubOrderDetail = this.changeOrderDTOToHubOrderDetail(orderDTO);
                supplierNo = hubOrderDetail.getSupplierNo();

                spMasterOrderNo = orderDTO.getSpMasterOrderNo();
                hubOrderDetail.setOrderStatus(OrderStatus.NO_PAY.getIndex());
                hubOrderDetail.setCreateTime(date);
                hubOrderDetail.setUpdateTime(date);
                orderDetailDao.insert(hubOrderDetail);
                orderDTO.setId(hubOrderDetail.getId());
                //记录订单状态的变化日志
                orderDTO.setOrderStatus(OrderStatus.NO_PAY);
                orderDTO.setUpdateTime(date);
                hubOrderLogService.saveOrderLog(orderDTO);


                //现只有一个
                HubOrder order = new HubOrder();
                order.setCreateTime(date);
                order.setSupplierId(supplierId);
                order.setSupplierNo(supplierNo);
                order.setSpMasterOrderNo(spMasterOrderNo);
                try {
                    orderDao.insert(order);//保存主表
                } catch (Exception e) {
                    //现因拆单保存 会出现重复的插入  发生错误不做处理
//                    LogCommon.recordLog("exceptionLog :{ messageId: " + orderDTO.getMessageId() + "} ，reason：" +e.getMessage(), LogLeve.ERROR);
                }
            }


        }
        return true;
    }

    @Override
    public boolean saveOrderAndOrderDetails(OrderDTO orderDTO) throws Exception {
        HubOrderDetail hubOrderDetail = this.changeOrderDTOToHubOrderDetail(orderDTO);
        String  supplierId=hubOrderDetail.getSupplierId();
        String supplierNo = hubOrderDetail.getSupplierNo();
        String spMasterOrderNo ="";
        Date date = new Date();

        spMasterOrderNo = orderDTO.getSpMasterOrderNo();
        hubOrderDetail.setOrderStatus(orderDTO.getOrderStatus().getIndex());
        hubOrderDetail.setCreateTime(date);
        hubOrderDetail.setUpdateTime(date);
        orderDetailDao.insert(hubOrderDetail);
        orderDTO.setId(hubOrderDetail.getId());
        //记录订单状态的变化日志
        orderDTO.setOrderStatus(orderDTO.getOrderStatus());
        orderDTO.setUpdateTime(date);
        hubOrderLogService.saveOrderLog(orderDTO);


        //现只有一个
        HubOrder order = new HubOrder();
        order.setCreateTime(date);
        order.setSupplierId(supplierId);
        order.setSupplierNo(supplierNo);
        order.setSpMasterOrderNo(spMasterOrderNo);
        try {
            orderDao.insert(order);//保存主表
        } catch (Exception e) {
            //现因拆单保存 会出现主单重复的插入  发生错误不做处理
        }

        return false;
    }

    @Override
    public void saveOrderDetail(OrderDTO orderDTO) throws ServiceException {
        HubOrderDetail hubOrderDetail = this.changeOrderDTOToHubOrderDetail(orderDTO);

        Date date = new Date();
        try {

            hubOrderDetail.setOrderStatus(orderDTO.getOrderStatus().getIndex());
            hubOrderDetail.setCreateTime(date);
            hubOrderDetail.setUpdateTime(date);
            orderDetailDao.insert(hubOrderDetail);
            orderDTO.setId(hubOrderDetail.getId());
            //记录订单状态的变化日志
            orderDTO.setOrderStatus(orderDTO.getOrderStatus());
            orderDTO.setUpdateTime(date);
            hubOrderLogService.saveOrderLog(orderDTO);

        } catch (Exception e) {
            LogCommon.recordLog("exceptionLog :{ messageId: " + orderDTO.getMessageId() + "}" +e.getMessage(), LogLeve.ERROR);
            throw  e;//ServiceMessageException(e.getMessage());
        }

    }


    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public void updateOrderDetail(Map<String, List<OrderDTO>> supplierOrderMsg) throws ServiceException {
        Set<String>  keySet =  supplierOrderMsg.keySet();
        Date date = new Date();
        String supplierNo="";
        for(String supplierId:keySet){
            List<OrderDTO>  orderDTOs = supplierOrderMsg.get(supplierId);
            if(null==orderDTOs||orderDTOs.isEmpty()){
                throw new ServiceMessageException("");
            }
            //转化临时对象为数据库对象
            for(OrderDTO orderDTO:orderDTOs) {
                HubOrderDetail hubOrderDetail = new HubOrderDetail();
                HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
                HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
                criterion.andIdEqualTo(orderDTO.getId());
                try {
                    hubOrderDetail.setOrderStatus(OrderStatus.PAYED.getIndex());
                    hubOrderDetail.setSpOrderDetailNo(orderDTO.getSpOrderDetailNo());
                    hubOrderDetail.setPurchaseNo(orderDTO.getPurchaseNo());
                    hubOrderDetail.setPayTime(date);
                    orderDetailDao.updateByExampleSelective(hubOrderDetail,criteria);
                } catch (Exception e) {
                    //TODO 记录日志
                    LogCommon.recordLog("",e);
                    throw new ServiceMessageException("","",e);
                }
            }

        }
    }


    @Override
    public void updateOrderDetailOrderStatusBySpMasterOrderNo(String spMasterOrderNo) throws ServiceException {
        HubOrderDetail hubOrderDetail = new HubOrderDetail();
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSpMasterOrderNoEqualTo(spMasterOrderNo);
        try {
            hubOrderDetail.setOrderStatus(OrderStatus.PAYED.getIndex());
            orderDetailDao.updateByExampleSelective(hubOrderDetail,criteria);


        } catch (Exception e) {
            //TODO 记录日志
            LogCommon.recordLog("",e);
            throw new ServiceMessageException("","",e);
        }
    }


    @Override
    public void updateHubOrderDetailOrderStatus(OrderDTO orderDTO) throws Exception {
        HubOrderDetail hubOrderDetail = new HubOrderDetail();//this.changeOrderDTOToHubOrderDetail(orderDTO);
        hubOrderDetail.setOrderStatus(orderDTO.getOrderStatus().getIndex());

        if(null!=orderDTO.getLockStockTime()){
            hubOrderDetail.setLockStockTime(orderDTO.getLockStockTime());
        }
        if(null!=orderDTO.getConfirmTime()){
            hubOrderDetail.setConfirmTime(orderDTO.getConfirmTime());
        }
        if(null!=orderDTO.getPayTime()){
        	hubOrderDetail.setPayTime(orderDTO.getPayTime());
        }
        hubOrderDetail.setUpdateTime(new Date());
        if(null!=orderDTO.getRefundTime()){
            hubOrderDetail.setRefundTime(orderDTO.getRefundTime());
        }
        if(null!=orderDTO.getCancelTime()){
            hubOrderDetail.setCancelTime(orderDTO.getCancelTime());
        }
        if(null!=orderDTO.getPurchaseNo()){
            hubOrderDetail.setPurchaseNo(orderDTO.getPurchaseNo());
        }
        hubOrderDetail.setUpdateTime(new Date());

        if(null!=orderDTO.getSpOrderDetailNo()){
            hubOrderDetail.setSpOrderDetailNo(orderDTO.getSpOrderDetailNo());
        }
        if(null!=orderDTO.getShipTime()){
        	hubOrderDetail.setDeliveryTime(orderDTO.getShipTime());
        }
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andIdEqualTo(orderDTO.getId());
        orderDetailDao.updateByExampleSelective(hubOrderDetail,criteria);
    }

    @Override
    public void updateHubOrderDetailPushStatus(OrderDTO orderDTO) throws Exception {
        HubOrderDetail hubOrderDetail = new HubOrderDetail();//this.changeOrderDTOToHubOrderDetail(orderDTO);
        if(null!=orderDTO.getPushStatus()){
            hubOrderDetail.setPushStatus(orderDTO.getPushStatus().getIndex());
        }
        if(null!=orderDTO.getErrorType()){
            hubOrderDetail.setErrorType(orderDTO.getErrorType().getIndex());
        }

        hubOrderDetail.setDescription(orderDTO.getDescription());

        if(null!=orderDTO.getLockStockTime()){
            hubOrderDetail.setLockStockTime(orderDTO.getLockStockTime());
        }
        if(null!=orderDTO.getConfirmTime()){
            hubOrderDetail.setConfirmTime(orderDTO.getConfirmTime());
        }
        if(null!=orderDTO.getUpdateTime()){
            hubOrderDetail.setUpdateTime(orderDTO.getUpdateTime());
        }
        if(null!=orderDTO.getRefundTime()){
            hubOrderDetail.setRefundTime(orderDTO.getRefundTime());
        }
        if(null!=orderDTO.getCancelTime()){
            hubOrderDetail.setCancelTime(orderDTO.getCancelTime());
        }
        if(null!=orderDTO.getSupplierOrderNo()){
            hubOrderDetail.setSupplierOrderNo(orderDTO.getSupplierOrderNo());
        }
        HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
        HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andIdEqualTo(orderDTO.getId());

        orderDetailDao.updateByExampleSelective(hubOrderDetail,criteria);

    }


    /**
     * 从  OrderDTO 到   HubOrderDetail
     * @param orderDTO
     * @return
     */
    private   HubOrderDetail changeOrderDTOToHubOrderDetail(OrderDTO orderDTO){
        HubOrderDetail hubOrderDetail = new  HubOrderDetail();
        BeanUtils.copyProperties(orderDTO,hubOrderDetail);
        hubOrderDetail.setOrderNo(orderDTO.getSpOrderId());
        hubOrderDetail.setPurchasePrice(new BigDecimal(orderDTO.getPurchasePriceDetail()));
        return hubOrderDetail;
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public void handleRePurchase(HubOrderDetail oldHubOrderDetail, HubOrderDetail newHubOrderDetail) throws ServiceException{
        //老的订单需要设置采购异常
        try {
            if(null!=oldHubOrderDetail){
                orderDetailDao.updateByPrimaryKey(oldHubOrderDetail);
            }
            orderDetailDao.insert(newHubOrderDetail);
        } catch (Exception e) {
            throw new ServiceMessageException("数据库操作失败");
        }

    }




    @Override
    public HubOrder getHubOrderBySpMasterOrderNo(String spMasterOrderNo) {
        HubOrderCriteria criteria = new HubOrderCriteria();
        HubOrderCriteria.Criteria criterion =criteria.createCriteria();
        criterion.andSpMasterOrderNoEqualTo(spMasterOrderNo);
        List<HubOrder> orders =  orderDao.selectByExample(criteria);
        if(null!=orders&&orders.size()>0){
            return orders.get(0);
        }
        return null;
    }


    private  OrderDTO changeHubOrderDetailToOrderDTO(HubOrderDetail hubOrderDetail){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(hubOrderDetail,orderDTO);
        orderDTO.setDetail(hubOrderDetail.getSupplierSkuNo()+":1");
        orderDTO.setPurchasePriceDetail(hubOrderDetail.getPurchasePrice().toString());
        return orderDTO;
    }

    /**
     * 获取供货商的SKU
     * 先从缓存取 如果没有再从数据库中取
     * @param supplierId
     * @param spSkuNo
     * @return
     * @throws ServiceException
     */
    private String getSupplierSkuNo(String supplierId,String spSkuNo) throws ServiceException{
        //先获取缓存
        String supplierSkuNo =  shangpinRedis.get(supplierId+"_"+spSkuNo);
        if(StringUtils.isBlank(supplierSkuNo)){
            HubSkuRelation dto = hubSkuRelationService.getSkuRelationBySupplierIdAndSpSkuNo(supplierId,spSkuNo);
            if(null==dto){
                throw new ServiceMessageException("not find " + supplierId + "_" + spSkuNo +   " mapping ");
            }else{
                supplierSkuNo = dto.getSupplierSkuId();
                shangpinRedis.set(supplierId+"_"+spSkuNo,supplierSkuNo);
            }
        }
        return supplierSkuNo;
    }
    
    @Override
    public void updateOrderToShipped(String purchaseNo) throws ServiceException{
    	 HubOrderDetailCriteria criteria = new HubOrderDetailCriteria();
         HubOrderDetailCriteria.Criteria criterion =criteria.createCriteria();
         criterion.andPurchaseNoEqualTo(purchaseNo);
         HubOrderDetail orderDetail = new HubOrderDetail();
         orderDetail.setOrderStatus(OrderStatus.SHIPPED.getIndex());
         int result = orderDetailDao.updateByExampleSelective(orderDetail, criteria);
         if(1 != result){
        	 LogCommon.recordLog("采购单 "+purchaseNo+"更新SHIPPED状态失败");
        	 throw new ServiceMessageException(purchaseNo+" 更新SHIPPED状态值失败");
         }else{
        	 LogCommon.recordLog("采购单 "+purchaseNo+"更新SHIPPED状态成功");
         }
    }
}
