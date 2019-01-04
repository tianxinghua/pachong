package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**采购单实体类
 * @author qinyingchun
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sopPurchaseOrderNo; //该商品所在采购单的采购单号
	private String sopPurchaseOrderDetailNo; //该商品的唯一标识
	private String skuNo; //商品的尚品Sku编码
	private String productModel; //货号
	private String supplierSkuNo; //商品的供应商Sku编码
	private String barCode; //条形码
	private String detailStatus; //该商品状态，状态值描述：1=待处理，2=待发货，3=已发货，待收货，4=待补发，5=已取消，6=已完成
	private String detailStatusName; //状态值描述
	private String isStock; //库存检查标记 0=未标记 1 = 有货 2 = 缺货
	private String dateStart; //商品采购有效期的开始时间
	private String dateEnd; //商品采购有效期的结束时间
	private String skuPrice; //商品此次采购的价格
	private String skuPriceCurrency; //商品此次采购的价格币种名称
	private String warehouseNo; //商品需发往仓库的仓库编号
	private String warehouseName; //商品需发往仓库的仓库名称
	private String warehouseAddress; //商品需发往仓库的仓库地址
	private String warehousePost; //商品需发往仓库的仓库所在地邮编
	private String warehouseContactPerson; //商品需发往仓库的仓库联系人
	private String warehouseContactMobile; //商品需发往仓库的仓库联系方式
	private String currencyName;//货币币种名称
	private String brandNo; //品牌编号
	private String brandName; //品牌
	private String size; //尺码
	private String qty; //数量
	private String color; //颜色
	private String createTime; //采购单生成时间
	private String dateArrival; //采购单收货时间
	private String dateCanceled; //采购单取消时间
	private String orderNo; //尚品订单号
	private String supplierOrderNo; //尚品子订单号
	private String isPrint;
	private String picUrl; //图片地址
	private String sopDeliverOrderNo; //发货单号（包裹单ID）
	private String logisticsName; //物流公司
	private String logisticsOrderNo; //物流单号
	private String productName;
	private String latestConfirmTime;
	private String deliveryBefore;  //采购时限，超时未发货会有相应的动作
	private String importType;// 1跨境电商  2一般贸易
	private String tmsUrl;

	//供应商 原始链接 商品url
	private String productUrl;

	//香港供应商 增加 每一个商品的唯一编号    20180823
	private String pid;

	//订单来源  DBuy  KL
	private String orderFromNo;

	//mvp 标志位 1:mvp订单  0:默认值  2:不是mvp订单（只有 1 为 MVP订单）
	private String mvpFlag;

}
