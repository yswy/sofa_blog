/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.annotation.bean.instance;

import com.zuoer.sofa.blog.base.error.ErrorCode;
import com.zuoer.sofa.blog.base.exception.BaseRuntimeException;
import com.zuoer.sofa.blog.base.scanner.SpringClassScanner;
import com.zuoer.sofa.blog.base.spring.bean.name.BenchBeanNameGenerator;
import com.zuoer.sofa.blog.base.utils.ClassUtils;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 
 * @author chenbug
 *
 * @version $Id: InstanceBeanPostProcessor.java, v 0.1 2018年6月6日 下午6:48:54 chenbug Exp $
 */
@Component
public class InstanceBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub
		// 无法注入SpringClassScanner，这个时候spring还未准备好
		SpringClassScanner scanner = new SpringClassScanner();
		Set<Class<?>> classes = scanner.scan("com.zuoer.", ClassUtils.ACCEPT_ALL_CLASS_FILTER);
		for (Class<?> clasz : classes) {
			InstanceBean instanceBeanAnnotation = clasz.getAnnotation(InstanceBean.class);
			if (instanceBeanAnnotation == null) {
				continue;
			}
			String beanName = instanceBeanAnnotation.value();

			Object object = ClassUtils.getInstance(clasz, true);
			// 如果仍旧为空，则报错
			if (object == null) {
				throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "无法创建标注为InstanceBean的bean，根据static getInstance方法获取的值为空，class=" + clasz);
			}
			if (!object.getClass().isAssignableFrom(clasz)) {
				throw new BaseRuntimeException(new ErrorCode("SYSTEM_ERROR","系统错误"), "无法创建标注为InstanceBean的bean，instance的值类型不为" + clasz + "，class=" + clasz);
			}
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition((Class<Object>) object.getClass(), () -> object);
			AbstractBeanDefinition beanDefinition = builder.getRawBeanDefinition();
			if (StringUtils.isEmpty(beanName)) {
				beanName = BenchBeanNameGenerator.INSTANCE.generateBeanName(beanDefinition, registry);
			}
			registry.registerBeanDefinition(beanName, beanDefinition);
		}
	}

}
