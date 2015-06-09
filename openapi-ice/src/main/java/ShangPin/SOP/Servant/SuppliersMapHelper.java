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

public final class SuppliersMapHelper
{
    public static void
    write(IceInternal.BasicStream __os, java.util.Map<String, String> __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.size());
            for(java.util.Map.Entry<String, String> __e : __v.entrySet())
            {
                __os.writeString(__e.getKey());
                __os.writeString(__e.getValue());
            }
        }
    }

    public static java.util.Map<String, String>
    read(IceInternal.BasicStream __is)
    {
        java.util.Map<String, String> __v;
        __v = new java.util.HashMap<String, String>();
        int __sz0 = __is.readSize();
        for(int __i0 = 0; __i0 < __sz0; __i0++)
        {
            String __key;
            __key = __is.readString();
            String __value;
            __value = __is.readString();
            __v.put(__key, __value);
        }
        return __v;
    }
}
