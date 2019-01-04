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

public final class SopProductPriceEditIcePrxHelper extends Ice.ObjectPrxHelperBase implements SopProductPriceEditIcePrx
{
    public static SopProductPriceEditIcePrx checkedCast(Ice.ObjectPrx __obj)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopProductPriceEditIcePrx)
            {
                __d = (SopProductPriceEditIcePrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopProductPriceEditIcePrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopProductPriceEditIcePrx)
            {
                __d = (SopProductPriceEditIcePrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopProductPriceEditIcePrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
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

    public static SopProductPriceEditIcePrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
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

    public static SopProductPriceEditIcePrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopProductPriceEditIcePrx)
            {
                __d = (SopProductPriceEditIcePrx)__obj;
            }
            else
            {
                SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SopProductPriceEditIcePrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopProductPriceEditIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SopProductPriceEditIcePrxHelper __h = new SopProductPriceEditIcePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Product::SopProductPriceEditIce"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _SopProductPriceEditIceDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _SopProductPriceEditIceDelD();
    }

    public static void __write(IceInternal.BasicStream __os, SopProductPriceEditIcePrx v)
    {
        __os.writeProxy(v);
    }

    public static SopProductPriceEditIcePrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SopProductPriceEditIcePrxHelper result = new SopProductPriceEditIcePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
