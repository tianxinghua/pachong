// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `OpenApiServant.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Servant;

public interface _OpenApiServantDel extends Ice._ObjectDel
{
    ShangPin.SOP.Entity.Api.Product.SopSkuPriceApplyIce[] FindSupplyInfo(String supplierId, ShangPin.SOP.Entity.Api.Product.Supply supply, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    boolean UpdateSupplyPrice(String supplierId, ShangPin.SOP.Entity.Api.Product.SupplyPriceInfo price, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce[] FindStockInfo(String supplierId, java.util.List<java.lang.String> SkuNos, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    boolean UpdateStock(String supplierId, String SkuNo, int InventoryQuantity, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopProductSkuIce[] FindCommodityInfo(String supplierId, String Starttime, String Endtime, java.util.List<java.lang.String> ProductNos, java.util.List<java.lang.String> ProductModels, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopProductSkuPage FindCommodityInfoPage(String supplierId, ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery query, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.BaseDatas.SopCurrencyIce[] FindMoneyInfo(java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    java.util.Map<java.lang.String, java.lang.String> FindSuppliersById(String Id, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    java.util.Map<java.lang.String, java.lang.String> FindSuppliersByName(String Name, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    String CreateDeliveryOrder(String supplierId, ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd deliverOrder, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    boolean AddPurchaseDetailToDeliveryOrder(String supplierId, String deliveryOrderNo, java.util.List<java.lang.String> purchaseOrderDetailNos, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage FindPurchaseOrderDetailPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto queryDto, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage FindPurchaseOrderDetail(String supplierId, String purchaseOrderNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.ReturnOrderPage FindReturnOrderPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.ReturnOrderQueryDto queryDto, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.ReturnOrderPage FindReturnOrder(String supplierId, String returnOrderNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderPage FindDeliveryOrderPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.DeliveryOrderQueryDto queryDto, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderPage FindDeliveryOrder(String supplierId, String deliveryOrderNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    boolean FindDeliveryOrderSend(String supplierId, ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderSend deliveryOrderSend, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpCategoryPage FindSpCategoryPage(String supplierId, int pageIndex, int pageSize, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpBrandPage FindSpBrandPage(String supplierId, int pageIndex, int pageSize, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpSizeTmpIce FindSpSizeTmp(String supplierId, String SizeTmpNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpfProductAttributeInfoIce[] FindSpSpfProductAttributeInfo(String supplierId, String CategoryNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopAreaPage FindAreaPage(String supplierId, int pageIndex, int pageSize, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpCategorySizeStandard[] FindSpCategorySizeStandard(String supplierId, String CategoryNo, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpfProductMainColor[] FindSpfProductMainColor(String supplierId, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    int AddCommodity(String supplierId, ShangPin.SOP.Entity.Api.Product.ProductAddIce productAdd, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;

    boolean AddCommodityPic(String supplierId, ShangPin.SOP.Entity.Api.Product.ProductPicIce ProductPicIce, java.util.Map<String, String> __ctx, Ice.Instrumentation.InvocationObserver __obsv)
        throws IceInternal.LocalExceptionWrapper,
               ShangPin.SOP.Api.ApiException;
}
