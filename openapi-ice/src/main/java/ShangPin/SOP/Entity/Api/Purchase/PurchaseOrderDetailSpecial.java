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
// Generated from file `PurchaseOrderDetailSpecial.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public class PurchaseOrderDetailSpecial extends Ice.ObjectImpl
{
    public PurchaseOrderDetailSpecial()
    {
    }

    public PurchaseOrderDetailSpecial(String SopPurchaseOrderNo, String SopPurchaseOrderDetailNo, String SkuNo, String SupplierSkuNo, int DetailStatus, String WarehouseNo, String WarehouseName, String DateStart, String DateEnd, String SkuPrice, String SkuPriceCurrency, String WarehouseAddress, String WarehousePost, String WarehouseContactPerson, String WarehouseContactMobile, String OrderNo, String OrderDetailNo)
    {
        this.SopPurchaseOrderNo = SopPurchaseOrderNo;
        this.SopPurchaseOrderDetailNo = SopPurchaseOrderDetailNo;
        this.SkuNo = SkuNo;
        this.SupplierSkuNo = SupplierSkuNo;
        this.DetailStatus = DetailStatus;
        this.WarehouseNo = WarehouseNo;
        this.WarehouseName = WarehouseName;
        this.DateStart = DateStart;
        this.DateEnd = DateEnd;
        this.SkuPrice = SkuPrice;
        this.SkuPriceCurrency = SkuPriceCurrency;
        this.WarehouseAddress = WarehouseAddress;
        this.WarehousePost = WarehousePost;
        this.WarehouseContactPerson = WarehouseContactPerson;
        this.WarehouseContactMobile = WarehouseContactMobile;
        this.OrderNo = OrderNo;
        this.OrderDetailNo = OrderDetailNo;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new PurchaseOrderDetailSpecial();
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
        "::ShangPin::SOP::Entity::Api::Purchase::PurchaseOrderDetailSpecial"
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
        __os.writeString(SopPurchaseOrderNo);
        __os.writeString(SopPurchaseOrderDetailNo);
        __os.writeString(SkuNo);
        __os.writeString(SupplierSkuNo);
        __os.writeInt(DetailStatus);
        __os.writeString(WarehouseNo);
        __os.writeString(WarehouseName);
        __os.writeString(DateStart);
        __os.writeString(DateEnd);
        __os.writeString(SkuPrice);
        __os.writeString(SkuPriceCurrency);
        __os.writeString(WarehouseAddress);
        __os.writeString(WarehousePost);
        __os.writeString(WarehouseContactPerson);
        __os.writeString(WarehouseContactMobile);
        __os.writeString(OrderNo);
        __os.writeString(OrderDetailNo);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopPurchaseOrderNo = __is.readString();
        SopPurchaseOrderDetailNo = __is.readString();
        SkuNo = __is.readString();
        SupplierSkuNo = __is.readString();
        DetailStatus = __is.readInt();
        WarehouseNo = __is.readString();
        WarehouseName = __is.readString();
        DateStart = __is.readString();
        DateEnd = __is.readString();
        SkuPrice = __is.readString();
        SkuPriceCurrency = __is.readString();
        WarehouseAddress = __is.readString();
        WarehousePost = __is.readString();
        WarehouseContactPerson = __is.readString();
        WarehouseContactMobile = __is.readString();
        OrderNo = __is.readString();
        OrderDetailNo = __is.readString();
        __is.endReadSlice();
    }

    public String SopPurchaseOrderNo;

    public String SopPurchaseOrderDetailNo;

    public String SkuNo;

    public String SupplierSkuNo;

    public int DetailStatus;

    public String WarehouseNo;

    public String WarehouseName;

    public String DateStart;

    public String DateEnd;

    public String SkuPrice;

    public String SkuPriceCurrency;

    public String WarehouseAddress;

    public String WarehousePost;

    public String WarehouseContactPerson;

    public String WarehouseContactMobile;

    public String OrderNo;

    public String OrderDetailNo;

    public static final long serialVersionUID = 1282085735L;
}
