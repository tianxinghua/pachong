package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderDTO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huxia on 2015/9/10.
 */
@Mapper
public interface OrderMapper extends IBaseDao<OrderDTO> {


    public List<OrderDTO> findBySupplierIdAndStatus(@Param("supplierId") String supplierId,
                                                    @Param("status") String status) ;

    public List<OrderDTO> findBySupplierIdAndStatusAndDate(@Param("supplierId") String supplierId,
            @Param("status") String status,@Param("date") String date) ;
    
    /**
     * 查询订单
     * @param supplierId
     * @param status 订单状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<OrderDTO> findBySupplierIdAndStatusAndTime(@Param("supplierId") String supplierId,
            @Param("status") String status,@Param("startTime")String startTime,@Param("endTime")String endTime) ;



    /**
     * 更新订单状态
     * @param paraMap
     * @return
     */
    public int updateOrderStatus(Map<String,String> paraMap);

    /**
     * 增加异常信息
     * @param paraMap
     * @return
     */
    public int updateOrderExceptionMsg(Map<String,String> paraMap);

    /**
     * 更新订单发货单信息
     * map 包含
     * @param paraMap
     * @return
     */
    public int updateDeliveryNo(Map<String,String> paraMap);

    /**
     * 更新订单信息 任意信息
     * @param paraMap
     * @return
     */
    public int updateOrderMsg(Map<String,String> paraMap);


    /**
     * 根据订单编号获取信息
     * @param spOrderId 订单编号
     * @return
     */
    public OrderDTO findBySpOrderId(@Param("spOrderId")String spOrderId);


    /**
     * 根据订单唯一编号获取信息
     * @param uuId 订单唯一编号
     * @return
     */
    public OrderDTO findByUuId(@Param("uuId")String uuId);


    /**
     * 根据采购单编号获取信息
     * @param purchaseNo  采购单
     * @return
     */
    public OrderDTO findByPurchaseNo(@Param("purchaseNo")String purchaseNo);

    /**
     * 获取异常订单信息
     * @return
     */
    public List<OrderDTO> findExceptionOrder() ;
    
    public List<OrderDTO> getOrderBySupplierIdAndTime(@Param("supplierId")String supplier, @Param("startDate")Date startDate,
    		@Param("endDate")Date endDate,RowBounds rowBounds);
    
    
    public List<OrderDTO> getOrderBySupplierIdAndTime(@Param("supplierId")String supplier, @Param("startDate")Date startDate, @Param("endDate")Date endDate);

	public OrderDTO checkOrderByOrderIdSupplier(@Param("supplierId")String supplier, @Param("orderId")String string);
}
