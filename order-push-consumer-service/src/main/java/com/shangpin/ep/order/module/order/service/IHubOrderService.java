package com.shangpin.ep.order.module.order.service;

import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.HubOrder;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:IHubOrderService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:19:35
 */

public interface IHubOrderService {
    /**
     * 获取未发货的sku数量
     * 当skus为空时 返回此供货商下所有的未发货商品的数量
     * @param supplierId ：供货商编号
     * @param isOrderApi : 是否有订单对接   true ：是
     * @param isSpSku    :  是否是尚品的SKUNO true :是
     * @param skus     sku列表   可为空
     * @return
     */
     public Map<String,Integer> getNoShippedSkuCount(String supplierId,boolean isOrderApi,boolean isSpSku, List<String> skus);

    /**
     * 获取订单明细信息
     * @param supplierId  : 供货商编号
     * @param spMasterOrderNo   ：尚品主订单号
     * @param spSkuNo            ：尚品SKUNO
     * @return
     */
    public List<OrderDTO> getOrderMsgBySupplierIdAndSpMasterOrderNoAndSpSkuNo(String supplierId,String spMasterOrderNo,String spSkuNo);

    /**
     * 获取订单明细信息
     * @param supplierId  : 供货商编号
     * @param spMasterOrderNo   ：尚品主订单号
     * @param spSkuNo            ：尚品SKUNO
     * @return
     */
    public List<HubOrderDetail> getOrderDetailBySupplierIdAndSpMasterOrderNoAndSpSkuNo(String supplierId,String spMasterOrderNo,String spSkuNo);
    /**
     * 根据供货商的子订单和门户编号查询订单明细信息
     * @param supplierId  : 供货商门户编号
     * @param spOrderDetailNo  ：尚品子订单号
     * @return
     */
    public HubOrderDetail getOrderDetailBySupplierIdAndSpOrderDetailNo(String supplierId,String spOrderDetailNo);


    /**
     * 根据供货商的子订单查询订单明细信息
     * @param spOrderDetailNo  ：尚品子订单号
     * @return
     */
    public HubOrderDetail getOrderDetailBySpOrderDetailNo(String spOrderDetailNo);

    /**
     * 根据采购单编号获取订单信息
     * @param purchaseNo ：采购单编号
     * @return
     */
    public HubOrderDetail getOrderDetailByPurchaseNo(String purchaseNo);

    /**
     * 根据推送给供货商的订单编号 和供货商的门户ID  查询订单明细
     * @param supplierId 供货商ID
     * @param orderNo   推送给供货商的订单编号
     * @return
     */
    public HubOrderDetail  getOrderDetailBySupplierIdAndOrderNo(String supplierId,String  orderNo);

    /**
     * 根据推送给供货商的订单编号  和供货商的编号 查询订单明细
     * @param supplierNo
     * @param orderNo
     * @return
     */
    public HubOrderDetail  getOrderDetailBySupplierNoAndOrderNo(String supplierNo,String  orderNo);

    /**
     * 保存各个供货商的订单和订单明细信息
     * @param supplierOrderMsg :key :供货商Id List 订单明细  拆单后的
     * @return
     */
    public boolean saveOrderAndOrderDetails(Map<String,List<OrderDTO>> supplierOrderMsg) throws Exception;

    /***
     * 保存供货商的订单和订单明细
     * @param orderDTO
     * @return
     * @throws ServiceException
     */
    public boolean saveOrderAndOrderDetails(OrderDTO orderDTO) throws Exception;


    /***
     * 保存供货商的订单明细
     * @param orderDTO
     * @return
     * @throws ServiceException
     */
    public void saveOrderDetail(OrderDTO orderDTO) throws ServiceException;

    /**
     * 以供货商的维度更新订单明细
     * @param supplierOrderMsg
     * @throws ServiceException
     */
    public void updateOrderDetail(Map<String,List<OrderDTO>> supplierOrderMsg) throws ServiceException;



    /**
     * 更新订单明细的支付状态
     * @param spMasterOrderNo  :主单号
     * @throws ServiceException
     */
    public void updateOrderDetailOrderStatusBySpMasterOrderNo(String spMasterOrderNo) throws ServiceException;

    /**
     * 更新订单的状态
     * @param orderDTO
     * @throws ServiceException
     */
    public void updateHubOrderDetailOrderStatus(OrderDTO orderDTO) throws  Exception;

    /**
     * 更新订单明细的推送状态
     * @param orderDTO
     * @throws ServiceException
     */
    public void  updateHubOrderDetailPushStatus(OrderDTO orderDTO) throws  Exception;

    /**
     * 处理重采业务（把原采购单设置成采购异常，插入新的采购单）
     * @param oldHubOrderDetail ：原采购单
     * @param newHubOrderDetail ：新采购单
     * @throws ServiceException
     */
    public void handleRePurchase(HubOrderDetail oldHubOrderDetail,HubOrderDetail newHubOrderDetail) throws ServiceException;



    /**
     * 查询订单主表信息
     * @param spMasterOrderNo 订单信息
     * @return
     */
    public HubOrder getHubOrderBySpMasterOrderNo(String spMasterOrderNo);


//    public HubOrder


    /**
     * 更新订单状态为已发货
     * @param purchaseNo
     */
    public void updateOrderToShipped(String purchaseNo) throws ServiceException;
}
