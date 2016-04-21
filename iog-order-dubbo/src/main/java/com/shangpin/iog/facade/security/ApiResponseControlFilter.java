package com.shangpin.iog.facade.security;


import com.shangpin.iog.facade.comm.ApiContext;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * Created by Administrator on 2015/3/4.
 */
public class ApiResponseControlFilter implements ContainerResponseFilter {

    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) throws IOException {
        System.out.println("ApiResponseControlFilter");
        System.out.println("当前线程id：" + Thread.currentThread().getId());
        System.out.println("=========ApiResponseControlFilter中获取的上下文信息============" + ApiContext.getContext().getAttachment("app_key"));
        String url = request.getUriInfo().getAbsolutePath().getPath();
    }

}
