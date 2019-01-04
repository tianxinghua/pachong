package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 销售月统计
 * Created by ZRS on 2016/1/28.
 */
@Setter
@Getter
public class SalesMonth implements Serializable{

    @JSONField(name = "YearMonth")
    private String yearMonth;
    @JSONField(name = "DataFlag")
    private String dataFlag;
    @JSONField(name = "SupplierNo")
    private String supplierNo;
    @JSONField(name = "BrandNo")
    private String brandNo;
    @JSONField(name = "BrandNameEn")
    private String brandNameEn;
    @JSONField(name = "SecondaryCategory")
    private String secondaryCategory;
    @JSONField(name = "week_sort_sp2")
    private String week_sort_sp2;
    @JSONField(name = "TotalRevenue")
    private String totalRevenue;
    @JSONField(name = "TotalSalesOrders")
    private String totalSalesOrders;
    @JSONField(name = "ShippedToMilanOrders")
    private String shippedToMilanOrders;
    @JSONField(name = "ShippedToCustomerOrders")
    private String shippedToCustomerOrders;
    @JSONField(name = "ReturnedBeforeShipmentOrders")
    private String returnedBeforeShipmentOrders;
    @JSONField(name = "TotalSalesBooking")
    private String totalSalesBooking;
    @JSONField(name = "TotalSalesQuantityBooking")
    private String totalSalesQuantityBooking;
    @JSONField(name = "TotalCost")
    private String totalCost;
    @JSONField(name = "ItemsSold")
    private String itemsSold;
    @JSONField(name = "ResponseTime")
    private String responseTime;
    @JSONField(name = "ValidOrders")
    private String validOrders;
    @JSONField(name = "NoOfCustomers")
    private String noOfCustomers;
    @JSONField(name = "UniqueVisitors")
    private String uniqueVisitors;
    @JSONField(name = "PageViews")
    private String pageViews;
    @JSONField(name = "SaleCitys")
    private String saleCitys;
    @JSONField(name = "MoblieSalesPercentage")
    private String moblieSalesPercentage;
    @JSONField(name = "SalesRanking")
    private String salesRanking;
    @JSONField(name = "ValidItemsRanking")
    private String validItemsRanking;
    @JSONField(name = "FulfilmentRateRanking")
    private String fulfilmentRateRanking;
    @JSONField(name = "ReturnRateRanking")
    private String returnRateRanking;
    @JSONField(name = "Sales")
    private String sales;
    @JSONField(name = "SalesQuantity")
    private String salesQuantity;
    @JSONField(name = "OnSpuNum")
    private String onSpuNum;
    @JSONField(name = "SaleSpuNum")
    private String saleSpuNum;
    @JSONField(name = "CurrencyNO")
    private String currencyNO;
    @JSONField(name = "CurrencySymbol")
    private String currencySymbol;

    private List<SalesMonthCategory> salesMonthCategoryList;
    private SalesMonthPerformance salesMonthPerformance;
    private List<SalesMonthWeek> salesMonthWeekList;
    private List<SalesMonthBrand> salesMonthBrand;

    private String yesterTotalRevenue;
    private String revenueRate;
}
