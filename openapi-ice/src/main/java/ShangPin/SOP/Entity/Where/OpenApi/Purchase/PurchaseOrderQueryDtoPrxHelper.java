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
// Generated from file `PurchaseOrderQueryDto.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Where.OpenApi.Purchase;

public final class PurchaseOrderQueryDtoPrxHelper extends Ice.ObjectPrxHelperBase implements PurchaseOrderQueryDtoPrx
{
    public static PurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof PurchaseOrderQueryDtoPrx)
            {
                __d = (PurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof PurchaseOrderQueryDtoPrx)
            {
                __d = (PurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
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

    public static PurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
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

    public static PurchaseOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof PurchaseOrderQueryDtoPrx)
            {
                __d = (PurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PurchaseOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PurchaseOrderQueryDtoPrxHelper __h = new PurchaseOrderQueryDtoPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Where::OpenApi::Purchase::PurchaseOrderQueryDto"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _PurchaseOrderQueryDtoDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _PurchaseOrderQueryDtoDelD();
    }

    public static void __write(IceInternal.BasicStream __os, PurchaseOrderQueryDtoPrx v)
    {
        __os.writeProxy(v);
    }

    public static PurchaseOrderQueryDtoPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PurchaseOrderQueryDtoPrxHelper result = new PurchaseOrderQueryDtoPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
