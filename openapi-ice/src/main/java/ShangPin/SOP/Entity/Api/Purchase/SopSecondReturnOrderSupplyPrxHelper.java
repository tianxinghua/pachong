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
// Generated from file `SopSecondReturnOrderSupply.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class SopSecondReturnOrderSupplyPrxHelper extends Ice.ObjectPrxHelperBase implements SopSecondReturnOrderSupplyPrx
{
    public static SopSecondReturnOrderSupplyPrx checkedCast(Ice.ObjectPrx __obj)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderSupplyPrx)
            {
                __d = (SopSecondReturnOrderSupplyPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderSupplyPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderSupplyPrx)
            {
                __d = (SopSecondReturnOrderSupplyPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderSupplyPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
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

    public static SopSecondReturnOrderSupplyPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
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

    public static SopSecondReturnOrderSupplyPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderSupplyPrx)
            {
                __d = (SopSecondReturnOrderSupplyPrx)__obj;
            }
            else
            {
                SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderSupplyPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSecondReturnOrderSupplyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SopSecondReturnOrderSupplyPrxHelper __h = new SopSecondReturnOrderSupplyPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::SopSecondReturnOrderSupply"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _SopSecondReturnOrderSupplyDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _SopSecondReturnOrderSupplyDelD();
    }

    public static void __write(IceInternal.BasicStream __os, SopSecondReturnOrderSupplyPrx v)
    {
        __os.writeProxy(v);
    }

    public static SopSecondReturnOrderSupplyPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SopSecondReturnOrderSupplyPrxHelper result = new SopSecondReturnOrderSupplyPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}