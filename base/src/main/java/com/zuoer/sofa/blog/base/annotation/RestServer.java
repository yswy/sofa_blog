/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.annotation;

import com.zuoer.sofa.blog.base.nill.Null;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * webservice服务端注解，将一个类实例定义为spring bean后发布成webservice服务
 * 
 * @author chenbug
 *
 * @version $Id: RestServer.java, v 0.1 2016年6月7日 下午4:54:17 chenbug Exp $
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RestServer {

	/**
	 * 服务接口类
	 *
	 * @return
	 */
	Class<?> serviceInterface() default Null.class;

}
