package com.shangpin.iog.ice.dto;

/**
 * Created by lizhongren on 2015/10/4.
 * 订单状态
 */
public class OrderStatus {
    public static String PLACED="placed";//下订单成功（未支付）
    public static String PAYED="payed";//支付
    public static String CONFIRMED="confirmed";//支付成功
    public static String CANCELLED="cancelled";//取消成功
    public static String PROCESSING="processing";
    public static String SHIPPED="shipped";
    public static String NOHANDLE="nohandle";//订单超过一定时间不处理，下订单没有库存
    public static String WAITCANCEL="waitcancel";
    public static String WAITPLACED="waitplaced";

    public static String WAITREFUND="waitrefund";
    public static String REFUNDED="refunded";//退款成功

    public static String PURCHASE_EXP_SUCCESS="purExpSuc";//采购单异常成功
    public static String PURCHASE_EXP_ERROR="purExpErr";
    
    public static String SHOULD_PURCHASE_EXP = "SHpurExp";
    
    public static String YUSHOUCONFIRM = "yuShou";


}
