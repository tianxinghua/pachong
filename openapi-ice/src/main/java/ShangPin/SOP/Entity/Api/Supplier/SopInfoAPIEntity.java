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
// Generated from file `SopInfoAPIEntity.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Supplier;

public class SopInfoAPIEntity extends Ice.ObjectImpl
{
    public SopInfoAPIEntity()
    {
    }

    public SopInfoAPIEntity(long SopInfoId, long SopUserNo, String SupplierNo, String SupplierName, short Language, int SupplyCycle, String SettlementSubject, String WarehouseNo, String Memo, String BelongMerchantNo, String Currency, String ServiceRate, String PurchaseType, String QuoteMode, String DeliverMode, String DirectFlag)
    {
        this.SopInfoId = SopInfoId;
        this.SopUserNo = SopUserNo;
        this.SupplierNo = SupplierNo;
        this.SupplierName = SupplierName;
        this.Language = Language;
        this.SupplyCycle = SupplyCycle;
        this.SettlementSubject = SettlementSubject;
        this.WarehouseNo = WarehouseNo;
        this.Memo = Memo;
        this.BelongMerchantNo = BelongMerchantNo;
        this.Currency = Currency;
        this.ServiceRate = ServiceRate;
        this.PurchaseType = PurchaseType;
        this.QuoteMode = QuoteMode;
        this.DeliverMode = DeliverMode;
        this.DirectFlag = DirectFlag;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SopInfoAPIEntity();
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
        "::ShangPin::SOP::Entity::Api::Supplier::SopInfoAPIEntity"
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
        __os.writeLong(SopInfoId);
        __os.writeLong(SopUserNo);
        __os.writeString(SupplierNo);
        __os.writeString(SupplierName);
        __os.writeShort(Language);
        __os.writeInt(SupplyCycle);
        __os.writeString(SettlementSubject);
        __os.writeString(WarehouseNo);
        __os.writeString(Memo);
        __os.writeString(BelongMerchantNo);
        __os.writeString(Currency);
        __os.writeString(ServiceRate);
        __os.writeString(PurchaseType);
        __os.writeString(QuoteMode);
        __os.writeString(DeliverMode);
        __os.writeString(DirectFlag);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopInfoId = __is.readLong();
        SopUserNo = __is.readLong();
        SupplierNo = __is.readString();
        SupplierName = __is.readString();
        Language = __is.readShort();
        SupplyCycle = __is.readInt();
        SettlementSubject = __is.readString();
        WarehouseNo = __is.readString();
        Memo = __is.readString();
        BelongMerchantNo = __is.readString();
        Currency = __is.readString();
        ServiceRate = __is.readString();
        PurchaseType = __is.readString();
        QuoteMode = __is.readString();
        DeliverMode = __is.readString();
        DirectFlag = __is.readString();
        __is.endReadSlice();
    }

    public long SopInfoId;

    public long SopUserNo;

    public String SupplierNo;

    public String SupplierName;

    public short Language;

    public int SupplyCycle;

    public String SettlementSubject;

    public String WarehouseNo;

    public String Memo;

    public String BelongMerchantNo;

    public String Currency;

    public String ServiceRate;

    public String PurchaseType;

    public String QuoteMode;

    public String DeliverMode;

    public String DirectFlag;

    public static final long serialVersionUID = -1316466617L;
}
