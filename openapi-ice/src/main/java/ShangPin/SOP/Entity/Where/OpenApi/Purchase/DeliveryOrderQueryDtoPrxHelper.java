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
// Generated from file `DeliveryOrderQueryDto.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Where.OpenApi.Purchase;

public final class DeliveryOrderQueryDtoPrxHelper extends Ice.ObjectPrxHelperBase implements DeliveryOrderQueryDtoPrx
{
    public static DeliveryOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderQueryDtoPrx)
            {
                __d = (DeliveryOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderQueryDtoPrx)
            {
                __d = (DeliveryOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DeliveryOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
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

    public static DeliveryOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
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

    public static DeliveryOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DeliveryOrderQueryDtoPrx)
            {
                __d = (DeliveryOrderQueryDtoPrx)__obj;
            }
            else
            {
                DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DeliveryOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DeliveryOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DeliveryOrderQueryDtoPrxHelper __h = new DeliveryOrderQueryDtoPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Where::OpenApi::Purchase::DeliveryOrderQueryDto"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DeliveryOrderQueryDtoDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DeliveryOrderQueryDtoDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DeliveryOrderQueryDtoPrx v)
    {
        __os.writeProxy(v);
    }

    public static DeliveryOrderQueryDtoPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DeliveryOrderQueryDtoPrxHelper result = new DeliveryOrderQueryDtoPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
