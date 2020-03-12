package com.zuoer.sofa.blog.base.annotation.bean.instance;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将类中的staic getInstance方法返回的对象注册为Spring的Bean
 * 
 * @author chenbug
 *
 * @version $Id: InstanceBean.java, v 0.1 2018年6月6日 下午6:42:53 chenbug Exp $
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InstanceBean {

	String value() default "";
}
