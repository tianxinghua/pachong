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
 * @author njt
 * @date 2018年9月25日 下午6:14:55
 * @desc 用于封装类中的字段注释
 * FieldNotes
 */
public @interface FieldNotes {
	String name() default ""; 
}
