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
// Generated from file `SkuInfoIce.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public final class MaterialListHelper
{
    public static void
    write(IceInternal.BasicStream __os, java.util.List<java.lang.String> __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.size());
            for(String __elem : __v)
            {
                __os.writeString(__elem);
            }
        }
    }

    public static java.util.List<java.lang.String>
    read(IceInternal.BasicStream __is)
    {
        java.util.List<java.lang.String> __v;
        __v = new java.util.ArrayList();
        final int __len0 = __is.readAndCheckSeqSize(1);
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            String __elem;
            __elem = __is.readString();
            __v.add(__elem);
        }
        return __v;
    }
}
