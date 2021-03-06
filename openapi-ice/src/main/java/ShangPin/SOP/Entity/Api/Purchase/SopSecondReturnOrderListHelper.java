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
// Generated from file `SopSecondReturnOrderPage.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class SopSecondReturnOrderListHelper
{
    public static void
    write(IceInternal.BasicStream __os, java.util.List<SopSecondReturnOrder> __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.size());
            for(SopSecondReturnOrder __elem : __v)
            {
                __os.writeObject(__elem);
            }
        }
    }

    public static java.util.List<SopSecondReturnOrder>
    read(IceInternal.BasicStream __is)
    {
        java.util.List<SopSecondReturnOrder> __v;
        __v = new java.util.LinkedList();
        final int __len0 = __is.readAndCheckSeqSize(1);
        final String __type0 = SopSecondReturnOrder.ice_staticId();
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v.add(null);
            __is.readObject(new IceInternal.ListPatcher<SopSecondReturnOrder>(__v, SopSecondReturnOrder.class, __type0, __i0));
        }
        return __v;
    }
}
