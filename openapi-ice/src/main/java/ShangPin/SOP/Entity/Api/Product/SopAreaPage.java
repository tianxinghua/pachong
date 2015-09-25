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
// Generated from file `SopAreaPage.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public class SopAreaPage extends Ice.ObjectImpl
{
    public SopAreaPage()
    {
    }

    public SopAreaPage(int Total, java.util.List<SopAreaIce> SopAreaIces)
    {
        this.Total = Total;
        this.SopAreaIces = SopAreaIces;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SopAreaPage();
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
        "::ShangPin::SOP::Entity::Api::Product::SopAreaPage"
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
        __os.writeInt(Total);
        SopAreaIceListHelper.write(__os, SopAreaIces);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        Total = __is.readInt();
        SopAreaIces = SopAreaIceListHelper.read(__is);
        __is.endReadSlice();
    }

    public int Total;

    public java.util.List<SopAreaIce> SopAreaIces;

    public static final long serialVersionUID = 1661542748L;
}
