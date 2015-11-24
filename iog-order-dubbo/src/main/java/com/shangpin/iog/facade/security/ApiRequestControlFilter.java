package com.shangpin.iog.facade.security;

import com.shangpin.openapi.api.comm.ApiContext;
import com.shangpin.openapi.api.security.MD5;
import com.shangpin.openapi.api.comm.ToolsUtils;

import com.shangpin.openapi.model.Calllimittimes;
import com.shangpin.openapi.model.Resturl;
import com.shangpin.openapi.model.Supplier;
import com.shangpin.openapi.service.cache.CachedService;
import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    static Logger logger_mongo = Logger.getLogger("mongodb");

    public void filter(ContainerRequestContext request)
            throws IOException {
        //System.out.println("=================Start====================");
        //System.out.println("ApiRequestControlFilter");
        //System.out.println("当前线程id：" + Thread.currentThread().getId());

        String charset = request.getMediaType().getParameters().get("charset");
        charset = charset == null ? "UTF-8" : charset;

        //获取输入流，并截取请求对象重新定义输入流
        ByteArrayOutputStream outs = ToolsUtils.InputStreamToOutputStream(request.getEntityStream());
        String req_body = outs.toString();
        String[] params = req_body.split("&");
        Map<String, String> form_params = new LinkedHashMap<>();
        Map<String, String> md5_params = new LinkedHashMap<>();
        for (String param : params) {
            String[] key_value = param.split("=");
            String value = null;
            String md5_value = null;
            try {
                value = key_value.length == 1 ? "" : ToolsUtils.urlDecode(key_value[1], charset);
                md5_value = key_value.length == 1 ? "" : key_value[1];
            } catch (Exception e) {
                throw new IOException("6|||***|||get the parameters exception");
            }
            form_params.put(key_value[0], value);
            md5_params.put(key_value[0], md5_value);
            //System.out.println(key_value[0] + " : " + value);
        }

        String url = request.getUriInfo().getAbsolutePath().getPath();//获取请求的url
        //System.out.println(url);
        String app_key = form_params.get("app_key");//对应供应商UserName
        String timestamp = form_params.get("timestamp");//时间戳，防钓鱼
        String sign = form_params.get("sign");//签名

        //参数为空时直接拒绝请求
        if(null == app_key || "".equals(app_key) || app_key.length() != 32){
            throw new IOException("10|||***|||no user");
        }
        if (null == timestamp || "".equals(timestamp) || timestamp.length() != 16){
            throw new IOException("7|||***|||timestamp format is incorrent");
        }
        if (null == sign || "".equals(sign) || sign.length() != 32){
            throw new IOException("2|||***|||sign error");
        }

        //before：前期拦截记录请求日志
        String messageid = UUID.randomUUID().toString();
        logger_info.info(messageid + "\r\n" + url + "\r\n" + app_key + "\r\n" + req_body + "\r\n" + form_params.get("request"));
        Map mongodb_log_base = new HashMap();
        mongodb_log_base.put("app_key", app_key);
        mongodb_log_base.put("messageid", messageid);
        mongodb_log_base.put("resturl", url);
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            mongodb_log.put("message", req_body + " | " + form_params.get("request"));
            logger_mongo.info(mongodb_log);
        }catch (Exception e){

        }

        //把供应商信息写入上下文信息，供后面使用
        ApiContext.getContext().setAttachment("app_key", app_key);
        ApiContext.getContext().setAttachment("messageid", messageid);
        ApiContext.getContext().setAttachment("RestUrl", url);

        //首先判断时间是否在规定的10分钟内，防钓鱼
        //统一用北京时间
        Date now_date = new Date();
        long now = now_date.getTime();
        long request_time = 0;
        try {
            request_time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timestamp).getTime();
        } catch (ParseException e) {
            throw new IOException("7|||***|||timestamp format is incorrent");
        }
        long minute = Math.abs((now - request_time) / (60 * 1000));
        if (minute > 10) {
            throw new IOException("8|||***|||timestamp over 10minutes");
        }

        //缓存类操作
        CachedService cachedservice = ApiStart.getInstance().getBean("CachedService", CachedService.class);
        //1.是否有接口访问权限
        //先判断此供应商是否启用
        Supplier supplier = null;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            supplier = cachedservice.GetCachedSupplier(app_key);
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached user info " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (SQLException e) {
            throw new IOException("9|||***|||gets the user info failed");
        }
        if (supplier == null) {
            throw new IOException("10|||***|||no user");
        }
        if (!supplier.getIsValid().equals(1)) {
            throw new IOException("11|||***|||user permissions is not opened");
        }

        //再判断此接口是否启用
        Resturl resturl = null;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            resturl = cachedservice.GetCachedResturl(url);
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached url info " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (SQLException e) {
            throw new IOException("12|||***|||gets the url info failed");
        }
        if (resturl == null) {
            throw new IOException("13|||***|||no url");
        }
        if (!resturl.getIsUsed().equals(1)) {
            throw new IOException("14|||***|||url is not opened");
        }

        //最后获取供应商接口权限表，判断是否启用，在redis中取
        Calllimittimes calllimittimes = null;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            calllimittimes = cachedservice.GetCachedCalllimittimes(supplier.getSupplierID(), resturl.getRestUrlID());
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached user's url info " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (SQLException e) {
            throw new IOException("15|||***|||gets the user's url info failed");
        }
        if (calllimittimes == null) {
            throw new IOException("16|||***|||no user's url");
        }
        if (!calllimittimes.getIsUsed().equals(1)) {
            throw new IOException("17|||***|||user's url is not opened");
        }

        //2.访问次数限制判断
        //先获取供应商接口调用次数，在redis中取
        String Day = new SimpleDateFormat("yyyy-MM-dd").format(now_date);
        String Hour = new SimpleDateFormat("HH").format(now_date);
        String Minute = new SimpleDateFormat("mm").format(now_date);

        //需做异常处理
        int request_m_nums = 0;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            request_m_nums = cachedservice.GetCachedRequestNumsByM(supplier.getSupplierID(), resturl.getRestUrlID(), Day, Hour, Minute);
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached redis minute call nums " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (Exception e) {
            throw new IOException("22|||***|||get redis minute call nums error");
        }
        int request_h_nums = 0;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            request_h_nums = cachedservice.GetCachedRequestNumsByH(supplier.getSupplierID(), resturl.getRestUrlID(), Day, Hour);
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached redis hour call nums " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (Exception e) {
            throw new IOException("23|||***|||get redis hour call nums error");
        }
        int request_d_nums = 0;
        try {
            Map mongodb_log = new HashMap();
            mongodb_log.putAll(mongodb_log_base);
            long starttime = new Date().getTime();
            request_d_nums = cachedservice.GetCachedRequestNumsByD(supplier.getSupplierID(), resturl.getRestUrlID(), Day);
            long endtime = new Date().getTime();
            mongodb_log.put("message", "get cached redis day call nums " + (endtime - starttime) + "ms");
            logger_mongo.debug(mongodb_log);
        } catch (Exception e) {
            throw new IOException("24|||***|||get redis day call nums error");
        }

        //再根据上一步获取的供应商权限表中缓存的调用次数限制做一次匹配，看是否超出调用限制
        //判断每分钟限制
        if (request_m_nums >= calllimittimes.getMtimes()) {
            throw new IOException("18|||***|||over per minute " + calllimittimes.getMtimes());
        }
        //判断每小时限制
        if (request_h_nums >= calllimittimes.getHtimes()) {
            throw new IOException("19|||***|||over per hour " + calllimittimes.getHtimes());
        }
        //判断每天限制
        if (request_d_nums >= calllimittimes.getDtimes()) {
            throw new IOException("20|||***|||over per day " + calllimittimes.getDtimes());
        }

        //3.签名验证
        //sign=Md5(app_key=***&request=***&timestamp=***_app_secret)
        //request=UrlEncode({"id":"232","name":"asdsd"})
        String md5_sign = null;
        try {
            md5_sign = "app_key=" + md5_params.get("app_key")
                    + (md5_params.containsKey("request") ? "&request=" + md5_params.get("request") : "")
                    + "&timestamp=" + md5_params.get("timestamp")
                    + "_" + supplier.getPrivateKey();
        } catch (Exception e) {
            throw new IOException("21|||***|||md5 error1");
        }
        String md5_result = null;
        try {
            md5_result = MD5.encrypt32(md5_sign);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("22|||***|||md5 error2");
        }
        if (!md5_result.equals(sign)) {
            throw new IOException("2|||***|||sign error");
        }

        //如果没有超过使用次数则记录到上下文中传递到ApiWriterInterceptor中计数
        //计数为成功的调用次数，失败的不算
        ApiContext.getContext().setAttachment("SupplierID", supplier.getSupplierID());
        ApiContext.getContext().setAttachment("RestUrlID", resturl.getRestUrlID().toString());
        ApiContext.getContext().setAttachment("Day", Day);
        ApiContext.getContext().setAttachment("Hour", Hour);
        ApiContext.getContext().setAttachment("Minute", Minute);

        //重新定义输入流
        if (form_params.containsKey("request") && form_params.get("request") != null)
            request.setEntityStream(new ByteArrayInputStream(form_params.get("request").getBytes("UTF-8")));
        else
            request.setEntityStream(new ByteArrayInputStream("null".getBytes("UTF-8")));
    }

}
