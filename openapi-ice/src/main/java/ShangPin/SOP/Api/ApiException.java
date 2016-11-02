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
// Generated from file `ApiException.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package ShangPin.SOP.Api;

public class ApiException extends Ice.UserException
{
    public ApiException()
    {
    }

    public ApiException(Throwable __cause)
    {
        super(__cause);
    }

    public ApiException(String ErrorCode, String Message)
    {
        this.ErrorCode = ErrorCode;
        this.Message = Message;
    }

    public ApiException(String ErrorCode, String Message, Throwable __cause)
    {
        super(__cause);
        this.ErrorCode = ErrorCode;
        this.Message = Message;
    }

    public String
    ice_name()
    {
        return "ShangPin::SOP::Api::ApiException";
    }

    public String ErrorCode;

    public String Message;

    protected void
    __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice("::ShangPin::SOP::Api::ApiException", -1, true);
        __os.writeString(ErrorCode);
        __os.writeString(Message);
        __os.endWriteSlice();
    }

    protected void
    __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        ErrorCode = __is.readString();
        Message = __is.readString();
        __is.endReadSlice();
    }

    public static final long serialVersionUID = 1589533002L;
}
