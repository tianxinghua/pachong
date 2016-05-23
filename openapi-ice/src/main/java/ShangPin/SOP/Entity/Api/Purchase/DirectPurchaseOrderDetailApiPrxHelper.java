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
// Generated from file `DirectoryPurchaseOrderPage.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class DirectPurchaseOrderDetailApiPrxHelper extends Ice.ObjectPrxHelperBase implements DirectPurchaseOrderDetailApiPrx
{
    public static DirectPurchaseOrderDetailApiPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderDetailApiPrx)
            {
                __d = (DirectPurchaseOrderDetailApiPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderDetailApiPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderDetailApiPrx)
            {
                __d = (DirectPurchaseOrderDetailApiPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderDetailApiPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
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

    public static DirectPurchaseOrderDetailApiPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
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

    public static DirectPurchaseOrderDetailApiPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderDetailApiPrx)
            {
                __d = (DirectPurchaseOrderDetailApiPrx)__obj;
            }
            else
            {
                DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderDetailApiPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectPurchaseOrderDetailApiPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DirectPurchaseOrderDetailApiPrxHelper __h = new DirectPurchaseOrderDetailApiPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DirectPurchaseOrderDetailApi"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DirectPurchaseOrderDetailApiDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DirectPurchaseOrderDetailApiDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DirectPurchaseOrderDetailApiPrx v)
    {
        __os.writeProxy(v);
    }

    public static DirectPurchaseOrderDetailApiPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DirectPurchaseOrderDetailApiPrxHelper result = new DirectPurchaseOrderDetailApiPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
