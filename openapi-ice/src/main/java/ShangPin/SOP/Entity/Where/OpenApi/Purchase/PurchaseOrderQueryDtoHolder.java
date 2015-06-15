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

public final class PurchaseOrderQueryDtoHolder extends Ice.ObjectHolderBase<PurchaseOrderQueryDto>
{
    public
    PurchaseOrderQueryDtoHolder()
    {
    }

    public
    PurchaseOrderQueryDtoHolder(PurchaseOrderQueryDto value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof PurchaseOrderQueryDto)
        {
            value = (PurchaseOrderQueryDto)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return PurchaseOrderQueryDto.ice_staticId();
    }
}
