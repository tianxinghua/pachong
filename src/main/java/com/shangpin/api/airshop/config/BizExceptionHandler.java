package com.shangpin.api.airshop.config;

import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.exception.ReminderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 全局异常处理逻辑<br/>
 * 返回客户端对应的错误结果
 * Date:     2015年12月20日 <br/> 
 * @author 陈小峰
 * @since JDK7
 */
@ControllerAdvice
@Component
public class BizExceptionHandler {

    protected static Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);
    
    /**
     * request参数缺失
     * @param e 异常堆栈信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseContentOne<String> hanleRequestParameterException(MissingServletRequestParameterException e) throws Exception {
        logger.error("请求参数错误:" + e.getMessage());
        return ResponseContentOne.errorResp("2", "请求参数错误");
    }

    /**
     * 自定义提示异常
     * @param e 异常堆栈信息
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=ReminderException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseContentOne<String> hanleReminderException(ReminderException e) throws Exception {
        logger.error("错误信息:" + e.getMessage());
        return formatException(e);
    }

    /**
     * request body不存在
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseContentOne<String> hanleNoMethodContentException(HttpMessageNotReadableException e) throws Exception {
    	logger.error("请求body为空:",e);
    	return ResponseContentOne.errorResp("2", "请求body为空");
    }
    /**
     * 请求方法post,put,get错误
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseContentOne<String> hanleRequestMethodException(HttpRequestMethodNotSupportedException e) throws Exception {
        logger.error("服务请求方式错误:" + e.getMessage());
        return ResponseContentOne.errorResp("2", "请求不支持");
    }
    /**
     * 请求的参数转换错误
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=HttpMessageConversionException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseContentOne<String> hanleConversionException(HttpMessageConversionException e) throws Exception {
    	logger.error("请求数据转换异常",e);
    	return ResponseContentOne.errorResp("2", "数据转换异常");
    }

    /**
     * 处理系统异常
     *
     * @param e
     * @return
     * @throws Throwable
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseContentOne<String> exception(HttpServletResponse response,Exception e) throws Throwable {
    	logger.error("未知异常:",e);
    	return formatException(e);//String.newFailAck(PayErrorCode.SYSTEM_EXCEPTION);
    }
    /**
     * 处理系统异常
     *
     * @param e
     * @return
     * @throws Throwable
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseContentOne<String> throwable(HttpServletResponse response,Throwable e) throws Throwable {
    	logger.error("未知异常:",e);
        return formatException(e);//String.newFailAck(PayErrorCode.SYSTEM_EXCEPTION);
    }
    
    
    private ResponseContentOne<String> formatException(Throwable throwable) {
        if (throwable == null) return ResponseContentOne.errorResp("2", "未知错误");
        return ResponseContentOne.errorResp("2", throwable.getMessage()==null?"系统异常":throwable.getLocalizedMessage());
    }
    
}
