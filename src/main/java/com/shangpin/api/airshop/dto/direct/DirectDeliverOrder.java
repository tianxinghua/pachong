package com.shangpin.api.airshop.dto.direct;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 18:14 2018/10/8
 * @Description:
 */
@Setter
@Getter
@ToString
public class DirectDeliverOrder {

    
    // 采购单编号
    ////@JSONField(name = "SopPurchaseOrderNo")
    private String sopPurchaseOrderNo ;
    
    // 发货单号
    //@JSONField(name = "SopDeliveryOrderNo")
    private long sopDeliveryOrderNo ;
    
    // 商品名称
    //@JSONField(name = "SkuName")
    private String skuName ;
    
    // 国内物流公司名称
    //@JSONField(name = "LogisticsName")
    private String logisticsName ;
    
    // 国内物流单号
    //@JSONField(name = "LogisticsOrderNo")
    private String logisticsOrderNo ;
    
    // 国际段物流单号
    //@JSONField(name = "OverseasLogisticsNO")
    private String overseasLogisticsNO ;
    
    // 国际段物流公司
    //@JSONField(name = "OverseasLogiticsCompanyNo")
    private String overseasLogiticsCompanyNo ;
    
    // 发货日期
    //@JSONField(name = "DateDeliver")
    private String dateDeliver ;
    
    // 到达日期
    //@JSONField(name = "DateArrival")
    private String dateArrival ;
    
    // 预计到达日期
    //@JSONField(name = "EstimateArrivedTime")
    private byte estimateArrivedTime ;
    
    // 发货状态
    //@JSONField(name = "DeliveryStatus")
    private byte deliveryStatus ;
    
    // 发货联系人
    //@JSONField(name = "DeliveryContacts")
    private String deliveryContacts ;
    
    // 发货联系电话
    //@JSONField(name = "DeliveryContactsPhone")
    private String deliveryContactsPhone ;

    // 发货地址
    //@JSONField(name = "DeliveryAddress")
    private String deliveryAddress ;
    
    // 发货备注
    //@JSONField(name = "DeliveryMemo")
    private String deliveryMemo ;
    
    // 发货数量
    // @JSONField(name = "DeliveryCount")
    private int deliveryCount ;

    //尚品子订单号
    private String supplierOrderNo;

    /// 品牌名称
    public String brandName ;
    
    /// 材质
    public String material ;
    
    /// 品类中文名称
    public String categoryName ;
    
    /// 品类英文名称
    public String categoryNameEn ;
    
    /// Price
    public double skuPrice ;
    
    /// 供应商Sku编号
    public String supplierSkuNo ;

    //商品图片链接
    private String picUrl;

    //商品原始链接
    private String productUrl;

    //商品的尚品Sku编码
    private String skuNo;

}
