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

public class SecondReturnOrderQueryDto extends Ice.ObjectImpl
{
    public SecondReturnOrderQueryDto()
    {
    }

    public SecondReturnOrderQueryDto(String SopSecondReturnOrderNo, int ReturnStatus, String LogisticsName, String SendOutTimeBegin, String SendOutTimeEnd, String LogisticsOrderNo, int PageIndex, int PageSize)
    {
        this.SopSecondReturnOrderNo = SopSecondReturnOrderNo;
        this.ReturnStatus = ReturnStatus;
        this.LogisticsName = LogisticsName;
        this.SendOutTimeBegin = SendOutTimeBegin;
        this.SendOutTimeEnd = SendOutTimeEnd;
        this.LogisticsOrderNo = LogisticsOrderNo;
        this.PageIndex = PageIndex;
        this.PageSize = PageSize;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new SecondReturnOrderQueryDto();
        }

        public void destroy()
        {
        }
    }
    private static Ice.ObjectFactory _factory = new __F();

    public static Ice.ObjectFactory
    ice_factory()
    {
        return _factory;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ShangPin::SOP::Entity::Where::OpenApi::Purchase::SecondReturnOrderQueryDto"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.writeString(SopSecondReturnOrderNo);
        __os.writeInt(ReturnStatus);
        __os.writeString(LogisticsName);
        __os.writeString(SendOutTimeBegin);
        __os.writeString(SendOutTimeEnd);
        __os.writeString(LogisticsOrderNo);
        __os.writeInt(PageIndex);
        __os.writeInt(PageSize);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        SopSecondReturnOrderNo = __is.readString();
        ReturnStatus = __is.readInt();
        LogisticsName = __is.readString();
        SendOutTimeBegin = __is.readString();
        SendOutTimeEnd = __is.readString();
        LogisticsOrderNo = __is.readString();
        PageIndex = __is.readInt();
        PageSize = __is.readInt();
        __is.endReadSlice();
    }

    public String SopSecondReturnOrderNo;

    public int ReturnStatus;

    public String LogisticsName;

    public String SendOutTimeBegin;

    public String SendOutTimeEnd;

    public String LogisticsOrderNo;

    public int PageIndex;

    public int PageSize;

    public static final long serialVersionUID = 1514836309L;
}