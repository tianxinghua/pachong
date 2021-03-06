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
// Generated from file `SopSkuInventoryQueryDto.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Where.OpenApi.Product;

public final class SopSkuInventoryQueryDtoPrxHelper extends Ice.ObjectPrxHelperBase implements SopSkuInventoryQueryDtoPrx
{
    public static SopSkuInventoryQueryDtoPrx checkedCast(Ice.ObjectPrx __obj)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSkuInventoryQueryDtoPrx)
            {
                __d = (SopSkuInventoryQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSkuInventoryQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSkuInventoryQueryDtoPrx)
            {
                __d = (SopSkuInventoryQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSkuInventoryQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static SopSkuInventoryQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static SopSkuInventoryQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSkuInventoryQueryDtoPrx)
            {
                __d = (SopSkuInventoryQueryDtoPrx)__obj;
            }
            else
            {
                SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SopSkuInventoryQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSkuInventoryQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SopSkuInventoryQueryDtoPrxHelper __h = new SopSkuInventoryQueryDtoPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Where::OpenApi::Product::SopSkuInventoryQueryDto"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _SopSkuInventoryQueryDtoDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _SopSkuInventoryQueryDtoDelD();
    }

    public static void __write(IceInternal.BasicStream __os, SopSkuInventoryQueryDtoPrx v)
    {
        __os.writeProxy(v);
    }

    public static SopSkuInventoryQueryDtoPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SopSkuInventoryQueryDtoPrxHelper result = new SopSkuInventoryQueryDtoPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
