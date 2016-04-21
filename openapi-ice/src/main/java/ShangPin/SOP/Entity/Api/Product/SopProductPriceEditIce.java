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
// Generated from file `SopProductPriceEditIce.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public class SopProductPriceEditIce extends Ice.ObjectImpl
{
    public SopProductPriceEditIce()
    {
    }

    public SopProductPriceEditIce(String SopSkuPriceApplyId, int SopSkuPriceApplyNo, int SopSkuNo, String MarketCurrency, String MarketPrice, String SupplyCurrency, String SupplyPrice, String StagePrice, String StagePriceEffectDate, String StagePriceFailureDate, String TraiffPrice)
    {
        this.SopSkuPriceApplyId = SopSkuPriceApplyId;
        this.SopSkuPriceApplyNo = SopSkuPriceApplyNo;
        this.SopSkuNo = SopSkuNo;
        this.MarketCurrency = MarketCurrency;
        this.MarketPrice = MarketPrice;
        this.SupplyCurrency = SupplyCurrency;
        this.SupplyPrice = SupplyPrice;
        this.StagePrice = StagePrice;
        this.StagePriceEffectDate = StagePriceEffectDate;
        this.StagePriceFailureDate = StagePriceFailureDate;
        this.TraiffPrice = TraiffPrice;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SopProductPriceEditIce();
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
        "::ShangPin::SOP::Entity::Api::Product::SopProductPriceEditIce"
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
        __os.writeString(SopSkuPriceApplyId);
        __os.writeInt(SopSkuPriceApplyNo);
        __os.writeInt(SopSkuNo);
        __os.writeString(MarketCurrency);
        __os.writeString(MarketPrice);
        __os.writeString(SupplyCurrency);
        __os.writeString(SupplyPrice);
        __os.writeString(StagePrice);
        __os.writeString(StagePriceEffectDate);
        __os.writeString(StagePriceFailureDate);
        __os.writeString(TraiffPrice);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopSkuPriceApplyId = __is.readString();
        SopSkuPriceApplyNo = __is.readInt();
        SopSkuNo = __is.readInt();
        MarketCurrency = __is.readString();
        MarketPrice = __is.readString();
        SupplyCurrency = __is.readString();
        SupplyPrice = __is.readString();
        StagePrice = __is.readString();
        StagePriceEffectDate = __is.readString();
        StagePriceFailureDate = __is.readString();
        TraiffPrice = __is.readString();
        __is.endReadSlice();
    }

    public String SopSkuPriceApplyId;

    public int SopSkuPriceApplyNo;

    public int SopSkuNo;

    public String MarketCurrency;

    public String MarketPrice;

    public String SupplyCurrency;

    public String SupplyPrice;

    public String StagePrice;

    public String StagePriceEffectDate;

    public String StagePriceFailureDate;

    public String TraiffPrice;

    public static final long serialVersionUID = 712316478L;
}
