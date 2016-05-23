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

public interface _OpenApiServantOperationsNC
{
    ShangPin.SOP.Entity.Api.Product.SopSkuPriceApplyIce[] FindSupplyInfo(String supplierId, ShangPin.SOP.Entity.Api.Product.Supply supply)
        throws ShangPin.SOP.Api.ApiException;

    boolean UpdateSupplyPrice(String supplierId, ShangPin.SOP.Entity.Api.Product.SupplyPriceInfo price)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce[] FindStockInfo(String supplierId, java.util.List<java.lang.String> SkuNos)
        throws ShangPin.SOP.Api.ApiException;

    boolean UpdateStock(String supplierId, String SkuNo, int InventoryQuantity)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopProductSkuIce[] FindCommodityInfo(String supplierId, String Starttime, String Endtime, java.util.List<java.lang.String> ProductNos, java.util.List<java.lang.String> ProductModels)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopProductSkuPage FindCommodityInfoPage(String supplierId, ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery query)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.BaseDatas.SopCurrencyIce[] FindMoneyInfo()
        throws ShangPin.SOP.Api.ApiException;

    java.util.Map<java.lang.String, java.lang.String> FindSuppliersById(String Id)
        throws ShangPin.SOP.Api.ApiException;

    java.util.Map<java.lang.String, java.lang.String> FindSuppliersByName(String Name)
        throws ShangPin.SOP.Api.ApiException;

    String CreateDeliveryOrder(String supplierId, ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd deliverOrder)
        throws ShangPin.SOP.Api.ApiException;

    boolean AddPurchaseDetailToDeliveryOrder(String supplierId, String deliveryOrderNo, java.util.List<java.lang.String> purchaseOrderDetailNos)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage FindPurchaseOrderDetailPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage FindPurchaseOrderDetail(String supplierId, String purchaseOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.ReturnOrderPage FindReturnOrderPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.ReturnOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.ReturnOrderPage FindReturnOrder(String supplierId, String returnOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderPage FindDeliveryOrderPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.DeliveryOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderPage FindDeliveryOrder(String supplierId, String deliveryOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    boolean FindDeliveryOrderSend(String supplierId, ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderSend deliveryOrderSend)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpCategoryPage FindSpCategoryPage(String supplierId, int pageIndex, int pageSize)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpBrandPage FindSpBrandPage(String supplierId, int pageIndex, int pageSize)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpSizeTmpIce FindSpSizeTmp(String supplierId, String SizeTmpNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpfProductAttributeInfoIce[] FindSpSpfProductAttributeInfo(String supplierId, String CategoryNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopAreaPage FindAreaPage(String supplierId, int pageIndex, int pageSize)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpCategorySizeStandard[] FindSpCategorySizeStandard(String supplierId, String CategoryNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SpfProductMainColor[] FindSpfProductMainColor(String supplierId)
        throws ShangPin.SOP.Api.ApiException;

    int AddCommodity(String supplierId, ShangPin.SOP.Entity.Api.Product.ProductAddIce productAdd)
        throws ShangPin.SOP.Api.ApiException;

    boolean AddCommodityPic(String supplierId, ShangPin.SOP.Entity.Api.Product.ProductPicIce ProductPicIce)
        throws ShangPin.SOP.Api.ApiException;

    String PurchaseDetailEx(ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx purchaseOrderEx, String supplierId)
        throws ShangPin.SOP.Api.ApiException;

    String FindCategoryBrandAgreement(String supplierId, String categoryNo, String brandNo)
        throws ShangPin.SOP.Api.ApiException;

    boolean UpdateSupplyPriceSpecial(String supplierId, ShangPin.SOP.Entity.Api.Product.SupplyPriceInfo price)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailSpecialPage FindPurchaseOrderDetailSpecial(String supplierId, String purchaseOrderNo, String orderNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DirectoryPurchaseOrderDetailPage FindDirectoryPurchaseOrderDetailPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DirectPurchaseOrder FindDirectoryPurchaseOrderDetail(String supplierId, String purchaseOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    String SendDirectoryDeliveryOrder(String supplierId, ShangPin.SOP.Entity.Api.Purchase.SendDeliveryOrder sendDeliveryOrder)
        throws ShangPin.SOP.Api.ApiException;

    String SendDirectoryDeliveryOrderError(String supplierId, ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx deliveryOrderEx)
        throws ShangPin.SOP.Api.ApiException;

    String SendArrivalAbnormalProcess(String supplierId, ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderAbnormal purchaseOrderAbnormal)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopProductPricePage FindProductPrice(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Product.ProductPriceQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    boolean UpdateProductPrice(String supplierId, ShangPin.SOP.Entity.Api.Product.SopProductPriceEditIce productPrice)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.SopSecondReturnOrderPage FindSecondReturnOrderList(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.SecondReturnOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.SopSecondReturnOrder FindSecondReturnOrderByReturnNo(String supplierId, String returnOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    boolean ReceiveSecondReturnOrder(String supplierId, String returnOrderNo, String returnOrderDetailId)
        throws ShangPin.SOP.Api.ApiException;

    boolean SubmitSecondReturnOrderAbnormal(String supplierId, ShangPin.SOP.Entity.Api.Purchase.SopSecondReturnOrderSupply secondReturnOrderDetail)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopSkuPriceApplyPage FindSupplyInfoPage(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Product.ProductPriceQueryDto supply)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopSupplierProductInventoryPage FindSopProductInventoryList(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Product.SopSkuInventoryQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    boolean ModifySkuInventoryQuantity(String supplierId, ShangPin.SOP.Entity.Api.Product.SopSupplierProductInventoryEditIce productInventory)
        throws ShangPin.SOP.Api.ApiException;

    boolean AddSecondReturnOrderAbnormalPic(String supplierId, int detailId, byte[] sbnormalpic)
        throws ShangPin.SOP.Api.ApiException;

    String PurchaseDetailError(ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx purchaseOrderEx, String supplierId)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.BaseDatas.SopCategoryBrandAgreementPage FindCategoryBrandAgreementPage(String supplierId, ShangPin.SOP.Entity.Api.BaseDatas.SopCategoryBrandAgreementQuery query)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DirectoryPurchaseOrderPage FindDirectoryPurchaseOrderPage(String supplierId, ShangPin.SOP.Entity.Api.Purchase.DirectoryPurchaseOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;

    java.util.List<java.lang.String> GetLogisticsCompany(String supplierId, String logisticsName)
        throws ShangPin.SOP.Api.ApiException;

    boolean DirectUpdateStock(String supplierId, String SkuNo, String SupplierSkuNo, int InventoryQuantity, String PrewarningQuantity, String Freight)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Purchase.DirectPurchaseOrderApi FindDirectoryPurchaseOrder(String supplierId, String purchaseOrderNo)
        throws ShangPin.SOP.Api.ApiException;

    boolean UpdateStockNew(String supplierId, String SkuNo, String SupplierSkuNo, int InventoryQuantity)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce[] FindDirectStockInfo(String supplierId, java.util.List<java.lang.String> SkuNos, java.util.List<java.lang.String> SupplierSkuNos)
        throws ShangPin.SOP.Api.ApiException;

    String DeleteProduct(String supplierId, String SkuNo)
        throws ShangPin.SOP.Api.ApiException;

    ShangPin.SOP.Entity.DTO.PurchaseOrderInfoApiDto FindPurchaseOrderDetailCountPaged(String supplierId, ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto queryDto)
        throws ShangPin.SOP.Api.ApiException;
}
