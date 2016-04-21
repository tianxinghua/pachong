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
// Generated from file `SecondReturnOrderQueryDto.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Entity.Where.OpenApi.Purchase;

public final class SecondReturnOrderQueryDtoHolder extends Ice.ObjectHolderBase<SecondReturnOrderQueryDto>
{
    public
    SecondReturnOrderQueryDtoHolder()
    {
    }

    public
    SecondReturnOrderQueryDtoHolder(SecondReturnOrderQueryDto value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof SecondReturnOrderQueryDto)
        {
            value = (SecondReturnOrderQueryDto)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return SecondReturnOrderQueryDto.ice_staticId();
    }
}
