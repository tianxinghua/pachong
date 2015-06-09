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
// Generated from file `DeliveryOrderAdd.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public class DeliveryOrderAdd extends Ice.ObjectImpl
{
    public DeliveryOrderAdd()
    {
    }

    public DeliveryOrderAdd(String LogisticsName, String LogisticsOrderNo, String DateDeliver, int EstimateArrivedTime, String DeliveryContacts, String DeliveryContactsPhone, String DeliveryAddress, String DeliveryMemo, String WarehouseNo, String WarehouseName, java.util.List<String> SopPurchaseOrderDetailNo, int PrintStatus)
    {
        this.LogisticsName = LogisticsName;
        this.LogisticsOrderNo = LogisticsOrderNo;
        this.DateDeliver = DateDeliver;
        this.EstimateArrivedTime = EstimateArrivedTime;
        this.DeliveryContacts = DeliveryContacts;
        this.DeliveryContactsPhone = DeliveryContactsPhone;
        this.DeliveryAddress = DeliveryAddress;
        this.DeliveryMemo = DeliveryMemo;
        this.WarehouseNo = WarehouseNo;
        this.WarehouseName = WarehouseName;
        this.SopPurchaseOrderDetailNo = SopPurchaseOrderDetailNo;
        this.PrintStatus = PrintStatus;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new DeliveryOrderAdd();
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
        "::ShangPin::SOP::Entity::Api::Purchase::DeliveryOrderAdd"
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
        __os.writeString(LogisticsName);
        __os.writeString(LogisticsOrderNo);
        __os.writeString(DateDeliver);
        __os.writeInt(EstimateArrivedTime);
        __os.writeString(DeliveryContacts);
        __os.writeString(DeliveryContactsPhone);
        __os.writeString(DeliveryAddress);
        __os.writeString(DeliveryMemo);
        __os.writeString(WarehouseNo);
        __os.writeString(WarehouseName);
        StringArrayHelper.write(__os, SopPurchaseOrderDetailNo);
        __os.writeInt(PrintStatus);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        LogisticsName = __is.readString();
        LogisticsOrderNo = __is.readString();
        DateDeliver = __is.readString();
        EstimateArrivedTime = __is.readInt();
        DeliveryContacts = __is.readString();
        DeliveryContactsPhone = __is.readString();
        DeliveryAddress = __is.readString();
        DeliveryMemo = __is.readString();
        WarehouseNo = __is.readString();
        WarehouseName = __is.readString();
        SopPurchaseOrderDetailNo = StringArrayHelper.read(__is);
        PrintStatus = __is.readInt();
        __is.endReadSlice();
    }

    public String LogisticsName;

    public String LogisticsOrderNo;

    public String DateDeliver;

    public int EstimateArrivedTime;

    public String DeliveryContacts;

    public String DeliveryContactsPhone;

    public String DeliveryAddress;

    public String DeliveryMemo;

    public String WarehouseNo;

    public String WarehouseName;

    public java.util.List<String> SopPurchaseOrderDetailNo;

    public int PrintStatus;

    public static final long serialVersionUID = 1568491229L;
}
