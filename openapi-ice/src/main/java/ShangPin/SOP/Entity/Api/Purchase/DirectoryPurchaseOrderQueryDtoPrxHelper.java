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

public final class DirectoryPurchaseOrderQueryDtoPrxHelper extends Ice.ObjectPrxHelperBase implements DirectoryPurchaseOrderQueryDtoPrx
{
    public static DirectoryPurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectoryPurchaseOrderQueryDtoPrx)
            {
                __d = (DirectoryPurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectoryPurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectoryPurchaseOrderQueryDtoPrx)
            {
                __d = (DirectoryPurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DirectoryPurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
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

    public static DirectoryPurchaseOrderQueryDtoPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
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

    public static DirectoryPurchaseOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DirectoryPurchaseOrderQueryDtoPrx)
            {
                __d = (DirectoryPurchaseOrderQueryDtoPrx)__obj;
            }
            else
            {
                DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DirectoryPurchaseOrderQueryDtoPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DirectoryPurchaseOrderQueryDtoPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DirectoryPurchaseOrderQueryDtoPrxHelper __h = new DirectoryPurchaseOrderQueryDtoPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Api::Purchase::DirectoryPurchaseOrderQueryDto"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DirectoryPurchaseOrderQueryDtoDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DirectoryPurchaseOrderQueryDtoDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DirectoryPurchaseOrderQueryDtoPrx v)
    {
        __os.writeProxy(v);
    }

    public static DirectoryPurchaseOrderQueryDtoPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DirectoryPurchaseOrderQueryDtoPrxHelper result = new DirectoryPurchaseOrderQueryDtoPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
