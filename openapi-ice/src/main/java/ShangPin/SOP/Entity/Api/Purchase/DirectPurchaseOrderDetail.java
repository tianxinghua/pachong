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
// Generated from file `DirectPurchaseOrderDetail.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public class DirectPurchaseOrderDetail extends Ice.ObjectImpl
{
    public DirectPurchaseOrderDetail()
    {
    }

    public DirectPurchaseOrderDetail(String SopPurchaseOrderDetailNo, String SopPurchaseOrderNo, String SupplierSkuNo, int DetailStatus, String ProductModel, String SopProductName, String BarCode, String SkuName, int SopProductNo, int PurchaseType, String WarehouseNo, String Material, String PlaceOriginText, String ProductColor, String ProductSizeValue, String DateStart, String DateEnd, int IsReSend, String SkuPrice, String SkuPriceCurrency, String ConsigneeName, String ConsigneeAddress, String ConsigneeMobile, String TariffPrice, String TariffPriceCurrencyName, String Freight, String FreightCurrencyName, String DeliveryBefore, String SkuNo)
    {
        this.SopPurchaseOrderDetailNo = SopPurchaseOrderDetailNo;
        this.SopPurchaseOrderNo = SopPurchaseOrderNo;
        this.SupplierSkuNo = SupplierSkuNo;
        this.DetailStatus = DetailStatus;
        this.ProductModel = ProductModel;
        this.SopProductName = SopProductName;
        this.BarCode = BarCode;
        this.SkuName = SkuName;
        this.SopProductNo = SopProductNo;
        this.PurchaseType = PurchaseType;
        this.WarehouseNo = WarehouseNo;
        this.Material = Material;
        this.PlaceOriginText = PlaceOriginText;
        this.ProductColor = ProductColor;
        this.ProductSizeValue = ProductSizeValue;
        this.DateStart = DateStart;
        this.DateEnd = DateEnd;
        this.IsReSend = IsReSend;
        this.SkuPrice = SkuPrice;
        this.SkuPriceCurrency = SkuPriceCurrency;
        this.ConsigneeName = ConsigneeName;
        this.ConsigneeAddress = ConsigneeAddress;
        this.ConsigneeMobile = ConsigneeMobile;
        this.TariffPrice = TariffPrice;
        this.TariffPriceCurrencyName = TariffPriceCurrencyName;
        this.Freight = Freight;
        this.FreightCurrencyName = FreightCurrencyName;
        this.DeliveryBefore = DeliveryBefore;
        this.SkuNo = SkuNo;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new DirectPurchaseOrderDetail();
        }

        public void destroy()
        {
        }
    }
    private static Ice.ObjectFactory _factory = new __F();

    public static Ice.ObjectFactory
    ice_factory()
    {
        return _factory;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DirectPurchaseOrderDetail"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.writeString(SopPurchaseOrderDetailNo);
        __os.writeString(SopPurchaseOrderNo);
        __os.writeString(SupplierSkuNo);
        __os.writeInt(DetailStatus);
        __os.writeString(ProductModel);
        __os.writeString(SopProductName);
        __os.writeString(BarCode);
        __os.writeString(SkuName);
        __os.writeInt(SopProductNo);
        __os.writeInt(PurchaseType);
        __os.writeString(WarehouseNo);
        __os.writeString(Material);
        __os.writeString(PlaceOriginText);
        __os.writeString(ProductColor);
        __os.writeString(ProductSizeValue);
        __os.writeString(DateStart);
        __os.writeString(DateEnd);
        __os.writeInt(IsReSend);
        __os.writeString(SkuPrice);
        __os.writeString(SkuPriceCurrency);
        __os.writeString(ConsigneeName);
        __os.writeString(ConsigneeAddress);
        __os.writeString(ConsigneeMobile);
        __os.writeString(TariffPrice);
        __os.writeString(TariffPriceCurrencyName);
        __os.writeString(Freight);
        __os.writeString(FreightCurrencyName);
        __os.writeString(DeliveryBefore);
        __os.writeString(SkuNo);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopPurchaseOrderDetailNo = __is.readString();
        SopPurchaseOrderNo = __is.readString();
        SupplierSkuNo = __is.readString();
        DetailStatus = __is.readInt();
        ProductModel = __is.readString();
        SopProductName = __is.readString();
        BarCode = __is.readString();
        SkuName = __is.readString();
        SopProductNo = __is.readInt();
        PurchaseType = __is.readInt();
        WarehouseNo = __is.readString();
        Material = __is.readString();
        PlaceOriginText = __is.readString();
        ProductColor = __is.readString();
        ProductSizeValue = __is.readString();
        DateStart = __is.readString();
        DateEnd = __is.readString();
        IsReSend = __is.readInt();
        SkuPrice = __is.readString();
        SkuPriceCurrency = __is.readString();
        ConsigneeName = __is.readString();
        ConsigneeAddress = __is.readString();
        ConsigneeMobile = __is.readString();
        TariffPrice = __is.readString();
        TariffPriceCurrencyName = __is.readString();
        Freight = __is.readString();
        FreightCurrencyName = __is.readString();
        DeliveryBefore = __is.readString();
        SkuNo = __is.readString();
        __is.endReadSlice();
    }

    public String SopPurchaseOrderDetailNo;

    public String SopPurchaseOrderNo;

    public String SupplierSkuNo;

    public int DetailStatus;

    public String ProductModel;

    public String SopProductName;

    public String BarCode;

    public String SkuName;

    public int SopProductNo;

    public int PurchaseType;

    public String WarehouseNo;

    public String Material;

    public String PlaceOriginText;

    public String ProductColor;

    public String ProductSizeValue;

    public String DateStart;

    public String DateEnd;

    public int IsReSend;

    public String SkuPrice;

    public String SkuPriceCurrency;

    public String ConsigneeName;

    public String ConsigneeAddress;

    public String ConsigneeMobile;

    public String TariffPrice;

    public String TariffPriceCurrencyName;

    public String Freight;

    public String FreightCurrencyName;

    public String DeliveryBefore;

    public String SkuNo;

    public static final long serialVersionUID = -2116684358L;
}
