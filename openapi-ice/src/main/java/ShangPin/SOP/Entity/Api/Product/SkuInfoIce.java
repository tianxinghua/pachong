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

public class SkuInfoIce extends Ice.ObjectImpl
{
    public SkuInfoIce()
    {
    }

    public SkuInfoIce(String ProductColor, String ColourScheme, String ProductSizeText, String ScreenSize, String BarCode, String SupplierSkuNo, java.util.List<java.lang.String> Materials, int[] PlaceOrginIds, SkuTempValueData[] SkuTempValueDatas)
    {
        this.ProductColor = ProductColor;
        this.ColourScheme = ColourScheme;
        this.ProductSizeText = ProductSizeText;
        this.ScreenSize = ScreenSize;
        this.BarCode = BarCode;
        this.SupplierSkuNo = SupplierSkuNo;
        this.Materials = Materials;
        this.PlaceOrginIds = PlaceOrginIds;
        this.SkuTempValueDatas = SkuTempValueDatas;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SkuInfoIce();
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
        "::ShangPin::SOP::Entity::Api::Product::SkuInfoIce"
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
        __os.writeString(ProductColor);
        __os.writeString(ColourScheme);
        __os.writeString(ProductSizeText);
        __os.writeString(ScreenSize);
        __os.writeString(BarCode);
        __os.writeString(SupplierSkuNo);
        MaterialListHelper.write(__os, Materials);
        PlaceOrginIdListHelper.write(__os, PlaceOrginIds);
        SkuTempValueDataListHelper.write(__os, SkuTempValueDatas);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        ProductColor = __is.readString();
        ColourScheme = __is.readString();
        ProductSizeText = __is.readString();
        ScreenSize = __is.readString();
        BarCode = __is.readString();
        SupplierSkuNo = __is.readString();
        Materials = MaterialListHelper.read(__is);
        PlaceOrginIds = PlaceOrginIdListHelper.read(__is);
        SkuTempValueDatas = SkuTempValueDataListHelper.read(__is);
        __is.endReadSlice();
    }

    public String ProductColor;

    public String ColourScheme;

    public String ProductSizeText;

    public String ScreenSize;

    public String BarCode;

    public String SupplierSkuNo;

    public java.util.List<java.lang.String> Materials;

    public int[] PlaceOrginIds;

    public SkuTempValueData[] SkuTempValueDatas;

    public static final long serialVersionUID = 2103896582L;
}
