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
// Generated from file `ProductAttributeDataIce.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public final class ProductAttributeDataIcePrxHelper extends Ice.ObjectPrxHelperBase implements ProductAttributeDataIcePrx
{
    public static ProductAttributeDataIcePrx checkedCast(Ice.ObjectPrx __obj)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ProductAttributeDataIcePrx)
            {
                __d = (ProductAttributeDataIcePrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ProductAttributeDataIcePrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ProductAttributeDataIcePrx)
            {
                __d = (ProductAttributeDataIcePrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ProductAttributeDataIcePrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
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

    public static ProductAttributeDataIcePrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
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

    public static ProductAttributeDataIcePrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof ProductAttributeDataIcePrx)
            {
                __d = (ProductAttributeDataIcePrx)__obj;
            }
            else
            {
                ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ProductAttributeDataIcePrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ProductAttributeDataIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ProductAttributeDataIcePrxHelper __h = new ProductAttributeDataIcePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Product::ProductAttributeDataIce"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _ProductAttributeDataIceDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _ProductAttributeDataIceDelD();
    }

    public static void __write(IceInternal.BasicStream __os, ProductAttributeDataIcePrx v)
    {
        __os.writeProxy(v);
    }

    public static ProductAttributeDataIcePrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ProductAttributeDataIcePrxHelper result = new ProductAttributeDataIcePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
