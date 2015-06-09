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
// Generated from file `OpenApiServant.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Servant;

public abstract class Callback_OpenApiServant_FindMoneyInfo extends Ice.TwowayCallback
{
    public abstract void response(ShangPin.SOP.Entity.Api.BaseDatas.SopCurrencyIce[] __ret);
    public abstract void exception(Ice.UserException __ex);

    public final void __completed(Ice.AsyncResult __result)
    {
        OpenApiServantPrx __proxy = (OpenApiServantPrx)__result.getProxy();
        ShangPin.SOP.Entity.Api.BaseDatas.SopCurrencyIce[] __ret = null;
        try
        {
            __ret = __proxy.end_FindMoneyInfo(__result);
        }
        catch(Ice.UserException __ex)
        {
            exception(__ex);
            return;
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
