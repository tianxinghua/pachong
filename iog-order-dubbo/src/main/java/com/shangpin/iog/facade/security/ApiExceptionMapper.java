package com.shangpin.iog.facade.security;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.shangpin.openapi.api.contract.model.ApiException;
import com.shangpin.openapi.api.contract.model.ExceptionResponse;
import org.codehaus.jackson.map.JsonMappingException;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.IOException;

/**
 * Created by Administrator on 2015/3/4.
 */
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception e) {
        //String url = ((HttpServletRequest)RpcContext.getContext().getRequest()).getRequestURI();
        //System.out.println("请求地址：" + url);
        //System.out.println("ApiExceptionMapper");
        //System.out.println(e.getClass().getName());
        //System.out.println("当前线程id：" + Thread.currentThread().getId());

        //异常处理
        ExceptionResponse response = new ExceptionResponse();
        String e_msg = "";
        //1.IOException(request过滤器中出现的权限等，通过message传递)
        // 2.ApiException
        // 3.JsonMappingException(json参数等反序列化失败)
        //4.NotAllowedException (No resource method found for GET, return 405 with Allow header)
        if (e instanceof JsonMappingException) {
            response.setResponseCode(4);
        } else if (e instanceof ApiException) {
            response.setResponseCode(((ApiException) e).getErrorCode());
        } else if (e instanceof IOException) {
            String[] responses = e.getMessage().split("\\|\\|\\|\\*\\*\\*\\|\\|\\|");
            if (responses.length > 1 && !responses[0].isEmpty()) {
                response.setResponseCode(Integer.parseInt(responses[0]));
                e_msg = responses[1];
            } else {
                response.setResponseCode(1);
            }
        } else if (e instanceof NotAllowedException) {
            response.setResponseCode(405);
        } else {
            response.setResponseCode(1);
        }
        response.setResponseMsg(e_msg.isEmpty() ? e.getMessage() : e_msg);

        return Response.status(Response.Status.OK)
                .entity(response)
                .type(ContentType.APPLICATION_JSON_UTF_8)
                .build();
    }

}
