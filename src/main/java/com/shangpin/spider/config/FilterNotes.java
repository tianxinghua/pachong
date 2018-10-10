package com.shangpin.spider.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 
 * @author njt
 * @date 2018年10月10日 下午8:42:01
 * @desc 用于过滤无需反射赋值的字段
 * FilterNotes
 */
public @interface FilterNotes {
	String name() default ""; 
}
