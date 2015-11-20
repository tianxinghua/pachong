package com.shangpin.iog.facade.security;

import com.alibaba.fastjson.JSON;
import com.shangpin.openapi.api.comm.ApiContext;
import com.shangpin.openapi.api.contract.model.ExceptionResponse;
import com.shangpin.openapi.service.cache.CachedService;
import org.apache.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/4.
 */
public class ApiWriterInterceptor implements WriterInterceptor {

    /**
     * 日志
     */
    static Logger logger_info = Logger.getLogger("info");
    static Logger logger_error = Logger.getLogger("error");
    static Logger logger_mongo = Logger.getLogger("mongodb");

    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        //System.out.println("ApiWriterInterceptor");
        //System.out.println("当前线程id：" + Thread.currentThread().getId());
        //System.out.println("=========ApiWriterInterceptor中获取的上下文信息============" + ApiContext.getContext().getAttachment("app_key"));
        //String url = ((HttpServletRequest)RpcContext.getContext().getRequest()).getRequestURI();

        //从上下文信息中获取信息
        Map<String, String> map = ApiContext.getContext().getAttachments();
        String app_key = map.get("app_key");
        String messageid = map.get("messageid");
        String RestUrl = map.get("RestUrl");
        Map mongodb_log_base = new HashMap();
        mongodb_log_base.put("app_key", app_key);
        mongodb_log_base.put("messageid", messageid);
        mongodb_log_base.put("resturl", RestUrl);
        Object obj = context.getEntity();
        String message = JSON.toJSONString(obj);
        Map mongodb_log = new HashMap();
        mongodb_log.putAll(mongodb_log_base);
        mongodb_log.put("message", message);
        //6.after：后期拦截记录响应日志
        if (RestUrl != null ) {
            if (obj instanceof ExceptionResponse) {
                logger_error.error((messageid == null ? "" : messageid + "\r\n") + RestUrl + "\r\n" + app_key + "\r\n" + message);
                try {
                    logger_mongo.error(mongodb_log);
                } catch (Exception e) {

                }
            } else {
                logger_info.info((messageid == null ? "" : messageid + "\r\n") + RestUrl + "\r\n" + app_key + "\r\n" + message);
                try {
                    logger_mongo.info(mongodb_log);
                } catch (Exception e) {

                }
            }
        }
        //System.out.println("writer拦截器响应数据：" + JSON.toJSON(obj));

        if (map.containsKey("SupplierID")
                && map.containsKey("RestUrlID")
                && map.containsKey("Day")
                && map.containsKey("Hour")
                && map.containsKey("Minute")) {
            String SupplierID = map.get("SupplierID");
            Integer RestUrlID = Integer.valueOf(map.get("RestUrlID"));
            String Day = map.get("Day");
            String Hour = map.get("Hour");
            String Minute = map.get("Minute");

            //缓存类操作，加入计数，需要加入异常处理
            CachedService cachedservice = ApiStart.getInstance().getBean("CachedService", CachedService.class);
            try {
                Map mongodb_log_redis = new HashMap();
                mongodb_log_redis.putAll(mongodb_log_base);
                long starttime = new Date().getTime();
                cachedservice.SetCachedRequestNumsByM(SupplierID, RestUrlID, Day, Hour, Minute);
                cachedservice.SetCachedRequestNumsByH(SupplierID, RestUrlID, Day, Hour);
                cachedservice.SetCachedRequestNumsByD(SupplierID, RestUrlID, Day);
                long endtime = new Date().getTime();
                mongodb_log_redis.put("message", "add call nums " + (endtime - starttime) + "ms");
                logger_mongo.debug(mongodb_log_redis);
            } catch (Exception e) {

            }
        }

        //清除上下文信息
        ApiContext.removeContext();
        context.proceed();
        //System.out.println("=================End=====================");
    }


}
