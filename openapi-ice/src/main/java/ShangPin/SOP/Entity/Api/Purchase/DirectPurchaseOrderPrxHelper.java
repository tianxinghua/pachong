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
// Generated from file `DirectPurchaseOrder.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class DirectPurchaseOrderPrxHelper extends Ice.ObjectPrxHelperBase implements DirectPurchaseOrderPrx
{
    public static DirectPurchaseOrderPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderPrx)
            {
                __d = (DirectPurchaseOrderPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderPrx)
            {
                __d = (DirectPurchaseOrderPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
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

    public static DirectPurchaseOrderPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
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

    public static DirectPurchaseOrderPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectPurchaseOrderPrx)
            {
                __d = (DirectPurchaseOrderPrx)__obj;
            }
            else
            {
                DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DirectPurchaseOrderPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectPurchaseOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DirectPurchaseOrderPrxHelper __h = new DirectPurchaseOrderPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DirectPurchaseOrder"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DirectPurchaseOrderDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DirectPurchaseOrderDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DirectPurchaseOrderPrx v)
    {
        __os.writeProxy(v);
    }

    public static DirectPurchaseOrderPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DirectPurchaseOrderPrxHelper result = new DirectPurchaseOrderPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
