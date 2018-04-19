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
// Generated from file `SopSecondReturnOrder.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public class SopSecondReturnOrder extends Ice.ObjectImpl
{
    public SopSecondReturnOrder()
    {
    }

    public SopSecondReturnOrder(String SopSecondReturnOrderId, String SopSecondReturnOrderNo, String SupplierNo, String SopUserNo, int ReturnPurchaseType, int ReturnPurchaseMode, int ReturnChannel, String WarehouseNo, int TotalQuantity, String TotalAmount, String Currency, int ReturnStatus, String LogisticsName, String LogisticsOrderNo, String SendOutTime, String ReceiveTime, String DeliveryContacts, String DeliveryContactsPhone, String DeliveryAddress, String Remark, java.util.List<SopSecondReturnOrderDetail> Details, String WarehouseName, String Consignee, String ConsigneeMobile, String ReturnCode, String ReturnAddress, String ConsigneeName, String LinkPhone, String AreaNo, String AreaPath, String SecondReturnReasonName, String SecondReturnReasonDetailName, String DeliveryMemo, String CreateTime)
    {
        this.SopSecondReturnOrderId = SopSecondReturnOrderId;
        this.SopSecondReturnOrderNo = SopSecondReturnOrderNo;
        this.SupplierNo = SupplierNo;
        this.SopUserNo = SopUserNo;
        this.ReturnPurchaseType = ReturnPurchaseType;
        this.ReturnPurchaseMode = ReturnPurchaseMode;
        this.ReturnChannel = ReturnChannel;
        this.WarehouseNo = WarehouseNo;
        this.TotalQuantity = TotalQuantity;
        this.TotalAmount = TotalAmount;
        this.Currency = Currency;
        this.ReturnStatus = ReturnStatus;
        this.LogisticsName = LogisticsName;
        this.LogisticsOrderNo = LogisticsOrderNo;
        this.SendOutTime = SendOutTime;
        this.ReceiveTime = ReceiveTime;
        this.DeliveryContacts = DeliveryContacts;
        this.DeliveryContactsPhone = DeliveryContactsPhone;
        this.DeliveryAddress = DeliveryAddress;
        this.Remark = Remark;
        this.Details = Details;
        this.WarehouseName = WarehouseName;
        this.Consignee = Consignee;
        this.ConsigneeMobile = ConsigneeMobile;
        this.ReturnCode = ReturnCode;
        this.ReturnAddress = ReturnAddress;
        this.ConsigneeName = ConsigneeName;
        this.LinkPhone = LinkPhone;
        this.AreaNo = AreaNo;
        this.AreaPath = AreaPath;
        this.SecondReturnReasonName = SecondReturnReasonName;
        this.SecondReturnReasonDetailName = SecondReturnReasonDetailName;
        this.DeliveryMemo = DeliveryMemo;
        this.CreateTime = CreateTime;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SopSecondReturnOrder();
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
        "::ShangPin::SOP::Entity::Api::Purchase::SopSecondReturnOrder"
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
        __os.writeString(SopSecondReturnOrderId);
        __os.writeString(SopSecondReturnOrderNo);
        __os.writeString(SupplierNo);
        __os.writeString(SopUserNo);
        __os.writeInt(ReturnPurchaseType);
        __os.writeInt(ReturnPurchaseMode);
        __os.writeInt(ReturnChannel);
        __os.writeString(WarehouseNo);
        __os.writeInt(TotalQuantity);
        __os.writeString(TotalAmount);
        __os.writeString(Currency);
        __os.writeInt(ReturnStatus);
        __os.writeString(LogisticsName);
        __os.writeString(LogisticsOrderNo);
        __os.writeString(SendOutTime);
        __os.writeString(ReceiveTime);
        __os.writeString(DeliveryContacts);
        __os.writeString(DeliveryContactsPhone);
        __os.writeString(DeliveryAddress);
        __os.writeString(Remark);
        SopSecondReturnOrderDetailListHelper.write(__os, Details);
        __os.writeString(WarehouseName);
        __os.writeString(Consignee);
        __os.writeString(ConsigneeMobile);
        __os.writeString(ReturnCode);
        __os.writeString(ReturnAddress);
        __os.writeString(ConsigneeName);
        __os.writeString(LinkPhone);
        __os.writeString(AreaNo);
        __os.writeString(AreaPath);
        __os.writeString(SecondReturnReasonName);
        __os.writeString(SecondReturnReasonDetailName);
        __os.writeString(DeliveryMemo);
        __os.writeString(CreateTime);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopSecondReturnOrderId = __is.readString();
        SopSecondReturnOrderNo = __is.readString();
        SupplierNo = __is.readString();
        SopUserNo = __is.readString();
        ReturnPurchaseType = __is.readInt();
        ReturnPurchaseMode = __is.readInt();
        ReturnChannel = __is.readInt();
        WarehouseNo = __is.readString();
        TotalQuantity = __is.readInt();
        TotalAmount = __is.readString();
        Currency = __is.readString();
        ReturnStatus = __is.readInt();
        LogisticsName = __is.readString();
        LogisticsOrderNo = __is.readString();
        SendOutTime = __is.readString();
        ReceiveTime = __is.readString();
        DeliveryContacts = __is.readString();
        DeliveryContactsPhone = __is.readString();
        DeliveryAddress = __is.readString();
        Remark = __is.readString();
        Details = SopSecondReturnOrderDetailListHelper.read(__is);
        WarehouseName = __is.readString();
        Consignee = __is.readString();
        ConsigneeMobile = __is.readString();
        ReturnCode = __is.readString();
        ReturnAddress = __is.readString();
        ConsigneeName = __is.readString();
        LinkPhone = __is.readString();
        AreaNo = __is.readString();
        AreaPath = __is.readString();
        SecondReturnReasonName = __is.readString();
        SecondReturnReasonDetailName = __is.readString();
        DeliveryMemo = __is.readString();
        CreateTime = __is.readString();
        __is.endReadSlice();
    }

    public String SopSecondReturnOrderId;

    public String SopSecondReturnOrderNo;

    public String SupplierNo;

    public String SopUserNo;

    public int ReturnPurchaseType;

    public int ReturnPurchaseMode;

    public int ReturnChannel;

    public String WarehouseNo;

    public int TotalQuantity;

    public String TotalAmount;

    public String Currency;

    public int ReturnStatus;

    public String LogisticsName;

    public String LogisticsOrderNo;

    public String SendOutTime;

    public String ReceiveTime;

    public String DeliveryContacts;

    public String DeliveryContactsPhone;

    public String DeliveryAddress;

    public String Remark;

    public java.util.List<SopSecondReturnOrderDetail> Details;

    public String WarehouseName;

    public String Consignee;

    public String ConsigneeMobile;

    public String ReturnCode;

    public String ReturnAddress;

    public String ConsigneeName;

    public String LinkPhone;

    public String AreaNo;

    public String AreaPath;

    public String SecondReturnReasonName;

    public String SecondReturnReasonDetailName;

    public String DeliveryMemo;

    public String CreateTime;

    public static final long serialVersionUID = 1397321434L;
}