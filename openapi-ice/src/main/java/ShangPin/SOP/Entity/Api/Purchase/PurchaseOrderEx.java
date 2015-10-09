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
// Generated from file `PurchaseOrderEx.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public class PurchaseOrderEx extends Ice.ObjectImpl
{
    public PurchaseOrderEx()
    {
    }

    public PurchaseOrderEx(java.util.List<java.lang.Long> SopPurchaseOrderDetailNos, String Memo)
    {
        this.SopPurchaseOrderDetailNos = SopPurchaseOrderDetailNos;
        this.Memo = Memo;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new PurchaseOrderEx();
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
        "::ShangPin::SOP::Entity::Api::Purchase::PurchaseOrderEx"
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
        LongArrayHelper.write(__os, SopPurchaseOrderDetailNos);
        __os.writeString(Memo);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopPurchaseOrderDetailNos = LongArrayHelper.read(__is);
        Memo = __is.readString();
        __is.endReadSlice();
    }

    public java.util.List<java.lang.Long> SopPurchaseOrderDetailNos;

    public String Memo;

    public static final long serialVersionUID = 983225225L;
}
