package com.shangpin.iog.facade.security;

import com.shangpin.openapi.api.comm.ApiContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.*;

/**
 * Created by Administrator on 2015/3/4.
 */
public class ApiReaderInterceptor implements ReaderInterceptor {

    public Object aroundReadFrom(ReaderInterceptorContext context)
            throws IOException, WebApplicationException {
        //System.out.println("ApiReaderInterceptor");
        //System.out.println("当前线程id：" + Thread.currentThread().getId());
        //System.out.println("=========ApiReaderInterceptor中获取的上下文信息============" + ApiContext.getContext().getAttachment("app_key"));
        String charset = context.getMediaType().getParameters().get("charset");
        charset = charset == null ? "UTF-8" : charset;
        context.setMediaType(new MediaType("application", "json", charset));
        Object entity = context.proceed();
        return entity;
    }


}
