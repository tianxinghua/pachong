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
// Generated from file `DeliveryOrder.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class DeliveryOrderPrxHelper extends Ice.ObjectPrxHelperBase implements DeliveryOrderPrx
{
    public static DeliveryOrderPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderPrx)
            {
                __d = (DeliveryOrderPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderPrx)
            {
                __d = (DeliveryOrderPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
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

    public static DeliveryOrderPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
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

    public static DeliveryOrderPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderPrx)
            {
                __d = (DeliveryOrderPrx)__obj;
            }
            else
            {
                DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DeliveryOrderPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DeliveryOrderPrxHelper __h = new DeliveryOrderPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DeliveryOrder"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DeliveryOrderDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DeliveryOrderDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DeliveryOrderPrx v)
    {
        __os.writeProxy(v);
    }

    public static DeliveryOrderPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DeliveryOrderPrxHelper result = new DeliveryOrderPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
