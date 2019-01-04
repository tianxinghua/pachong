package com.shangpin.api.airshop.dto.direct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 18:52 2018/9/28
 * @Description: 直发模式中 国外 发货页面数据实体
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirtectDeliveryPurchaseInfoForSendDto {


    /// 采购单编号
    //@JSONField(name = "SopPurchaseOrderNo")
    private String sopPurchaseOrderNo ;

    /// 收货人姓名
   // @JSONField(name = "ConsigneeName")
    private String consigneeName;

    /// 收货人电话
    //@JSONField(name = "ConsigneePhone")
    private String consigneePhone ;

    /// 收货人手机
    //@JSONField(name = "ConsigneeMobile")
    private String consigneeMobile ;

    /// 收货地址-省
    //@JSONField(name = "ConsigneeProvinceName")
    private String consigneeProvinceName ;

    /// 收货地址-市
   // @JSONField(name = "ConsigneeCityName")
    private String consigneeCityName ;

    /// 收货地址-区
    //@JSONField(name = "ConsigneeAreaName")
    private String consigneeAreaName ;

    /// 收货地址-乡镇
    //@JSONField(name = "ConsigneeTownName")
    private String consigneeTownName ;

    /// <summary>
    /// 收货地址
    //@JSONField(name = "ConsigneeAddress")
    private String consigneeAddress ;

    /// 发货人
    //@JSONField(name = "ConnectionUser")
    private String connectionUser ;

    /// 发货人联系方式
    //@JSONField(name = "ConnectionPhone")
    private String connectionPhone ;

    /// 发货地址
    //@JSONField(name = "DeliverAddress")
    private String deliverAddress ;

    /// 可选物流公司
    //@JSONField(name = "LogisticsCompany")
    private List<LogisticsCompanyDto> logisticsCompany ;

    //发货商品图片
    private String picUrl;

    //商品的尚品Sku编码
    private String skuNo;

    //身份证号
    private String identityCard;

    private String supplierSkuNo;

}
