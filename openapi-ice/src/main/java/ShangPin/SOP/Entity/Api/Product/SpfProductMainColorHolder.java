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
// Generated from file `SpfProductMainColor.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Api.Product;

public final class SpfProductMainColorHolder extends Ice.ObjectHolderBase<SpfProductMainColor>
{
    public
    SpfProductMainColorHolder()
    {
    }

    public
    SpfProductMainColorHolder(SpfProductMainColor value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof SpfProductMainColor)
        {
            value = (SpfProductMainColor)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return SpfProductMainColor.ice_staticId();
    }
}
