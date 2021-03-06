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
// Generated from file `SopSecondReturnOrderDetail.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Purchase;

public final class SopSecondReturnOrderDetailPrxHelper extends Ice.ObjectPrxHelperBase implements SopSecondReturnOrderDetailPrx
{
    public static SopSecondReturnOrderDetailPrx checkedCast(Ice.ObjectPrx __obj)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderDetailPrx)
            {
                __d = (SopSecondReturnOrderDetailPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderDetailPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderDetailPrx)
            {
                __d = (SopSecondReturnOrderDetailPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderDetailPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
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

    public static SopSecondReturnOrderDetailPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
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

    public static SopSecondReturnOrderDetailPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof SopSecondReturnOrderDetailPrx)
            {
                __d = (SopSecondReturnOrderDetailPrx)__obj;
            }
            else
            {
                SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SopSecondReturnOrderDetailPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SopSecondReturnOrderDetailPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SopSecondReturnOrderDetailPrxHelper __h = new SopSecondReturnOrderDetailPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::SopSecondReturnOrderDetail"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _SopSecondReturnOrderDetailDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _SopSecondReturnOrderDetailDelD();
    }

    public static void __write(IceInternal.BasicStream __os, SopSecondReturnOrderDetailPrx v)
    {
        __os.writeProxy(v);
    }

    public static SopSecondReturnOrderDetailPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SopSecondReturnOrderDetailPrxHelper result = new SopSecondReturnOrderDetailPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
