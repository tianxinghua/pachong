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
// Generated from file `SopProductSkuIce.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public class SopProductSkuIce extends Ice.ObjectImpl
{
    public SopProductSkuIce()
    {
    }

    public SopProductSkuIce(String BrandENName, String CategoryName, String GrossWeight, String Heigth, String Length, String MarketSeason, String MarketTime, String PackingList, String ProductModel, String ProductNo, String ProductSex, String ProductSlogan, String ProductUnit, String SopProductName, String UpdateTime, String Width, java.util.List<SopSkuIce> SopSkuIces)
    {
        this.BrandENName = BrandENName;
        this.CategoryName = CategoryName;
        this.GrossWeight = GrossWeight;
        this.Heigth = Heigth;
        this.Length = Length;
        this.MarketSeason = MarketSeason;
        this.MarketTime = MarketTime;
        this.PackingList = PackingList;
        this.ProductModel = ProductModel;
        this.ProductNo = ProductNo;
        this.ProductSex = ProductSex;
        this.ProductSlogan = ProductSlogan;
        this.ProductUnit = ProductUnit;
        this.SopProductName = SopProductName;
        this.UpdateTime = UpdateTime;
        this.Width = Width;
        this.SopSkuIces = SopSkuIces;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SopProductSkuIce();
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
        "::ShangPin::SOP::Entity::Api::Product::SopProductSkuIce"
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
        __os.writeString(BrandENName);
        __os.writeString(CategoryName);
        __os.writeString(GrossWeight);
        __os.writeString(Heigth);
        __os.writeString(Length);
        __os.writeString(MarketSeason);
        __os.writeString(MarketTime);
        __os.writeString(PackingList);
        __os.writeString(ProductModel);
        __os.writeString(ProductNo);
        __os.writeString(ProductSex);
        __os.writeString(ProductSlogan);
        __os.writeString(ProductUnit);
        __os.writeString(SopProductName);
        __os.writeString(UpdateTime);
        __os.writeString(Width);
        SopSkuIceListHelper.write(__os, SopSkuIces);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        BrandENName = __is.readString();
        CategoryName = __is.readString();
        GrossWeight = __is.readString();
        Heigth = __is.readString();
        Length = __is.readString();
        MarketSeason = __is.readString();
        MarketTime = __is.readString();
        PackingList = __is.readString();
        ProductModel = __is.readString();
        ProductNo = __is.readString();
        ProductSex = __is.readString();
        ProductSlogan = __is.readString();
        ProductUnit = __is.readString();
        SopProductName = __is.readString();
        UpdateTime = __is.readString();
        Width = __is.readString();
        SopSkuIces = SopSkuIceListHelper.read(__is);
        __is.endReadSlice();
    }

    public String BrandENName;

    public String CategoryName;

    public String GrossWeight;

    public String Heigth;

    public String Length;

    public String MarketSeason;

    public String MarketTime;

    public String PackingList;

    public String ProductModel;

    public String ProductNo;

    public String ProductSex;

    public String ProductSlogan;

    public String ProductUnit;

    public String SopProductName;

    public String UpdateTime;

    public String Width;

    public java.util.List<SopSkuIce> SopSkuIces;

    public static final long serialVersionUID = 1328908427L;
}
