
package com.shangpin.iog.web.conf.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date:     2015-2-5 下午10:19:09 <br/>
 * Description: 防止重复提交的token注解<br/>
 * 需要对action进行重复提交验证时添加此注解，和对应的拦截器就行<br/>
 * 在页面设置对应的表单参数键值对: <br/>
 * name:_token_,value:${_token_}
 * @author   陈小峰
 * @version  
 * @since    JDK 1.6	 
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	/**
	 * token产生着，生成token做为消费者验证是否重复提交的凭据<br/>
	 * 在页面设置对应的表单参数键值对: <br/>
	 * name:_token_,value:${_token_}
	 * */
	boolean producer() default false;
	/**token消费者，若action注解设置true，则会验证是否重复提交，默认false*/
	boolean consumer() default false;
}

