package com.shangpin.iog.facade.security;

import com.shangpin.iog.facade.comm.ApiContext;
import com.shangpin.iog.facade.comm.MD5;


import com.shangpin.iog.facade.comm.ToolsUtils;

import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.*;

/**
 * http请求的入口
 */
public class ApiRequestControlFilter implements ContainerRequestFilter {

    /**
     * 日志
     */
    static Logger logger_info = Logger.getLogger("info");
    static Logger logger_error = Logger.getLogger("error");

    public void filter(ContainerRequestContext request)
            throws IOException {


        String charset = request.getMediaType().getParameters().get("charset");
        charset = charset == null ? "UTF-8" : charset;

        //获取输入流，并截取请求对象重新定义输入流
        ByteArrayOutputStream outs = ToolsUtils.InputStreamToOutputStream(request.getEntityStream());
        String req_body = outs.toString();
        String[] params = req_body.split("&");
        Map<String, String> form_params = new LinkedHashMap<>();
        for (String param : params) {
            String[] key_value = param.split("=");
            String value = null;
            try {
                value = key_value.length == 1 ? "" : ToolsUtils.urlDecode(key_value[1], charset);
            } catch (Exception e) {
                throw new IOException("6|||***|||get the parameters exception");
            }
            form_params.put(key_value[0], value);
            System.out.println(key_value[0] + " : " + value);
        }


        //重新定义输入流
        if (form_params.containsKey("request") && form_params.get("request") != null)
            request.setEntityStream(new ByteArrayInputStream(form_params.get("request").getBytes("UTF-8")));
        else
            request.setEntityStream(new ByteArrayInputStream("null".getBytes("UTF-8")));
    }

}
