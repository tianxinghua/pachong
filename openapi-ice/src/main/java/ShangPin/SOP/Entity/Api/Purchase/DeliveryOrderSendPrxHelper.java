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
// Generated from file `DeliveryOrderSend.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class DeliveryOrderSendPrxHelper extends Ice.ObjectPrxHelperBase implements DeliveryOrderSendPrx
{
    public static DeliveryOrderSendPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderSendPrx)
            {
                __d = (DeliveryOrderSendPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderSendPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderSendPrx)
            {
                __d = (DeliveryOrderSendPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderSendPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
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

    public static DeliveryOrderSendPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
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

    public static DeliveryOrderSendPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderSendPrx)
            {
                __d = (DeliveryOrderSendPrx)__obj;
            }
            else
            {
                DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DeliveryOrderSendPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderSendPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DeliveryOrderSendPrxHelper __h = new DeliveryOrderSendPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DeliveryOrderSend"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DeliveryOrderSendDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DeliveryOrderSendDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DeliveryOrderSendPrx v)
    {
        __os.writeProxy(v);
    }

    public static DeliveryOrderSendPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DeliveryOrderSendPrxHelper result = new DeliveryOrderSendPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
