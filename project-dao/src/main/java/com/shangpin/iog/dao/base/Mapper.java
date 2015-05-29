/**
 * <pre>
 * Copyright:		Copyright(C) 2011-2012, feinno.com
 * Filename:		Mapper.java 
 * Date:			2014-1-13 
 * Author:			<a href="mailto:sundful@gmail.com">sundful</a>
 * Version          2.0.0
 * Description:		
 *
 * </pre>
 **/
package com.shangpin.iog.dao.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 	
 * @author 	<a href="mailto:sundful@gmail.com">sundful</a>
 * @version  2.0.0
 * @since   2014-1-13 下午1:58:18 
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE) 
public @interface Mapper {

}
