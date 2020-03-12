//package com.zuoer.sofa.blog.base.datasource;
//
//import com.zuoer.sofa.blog.base.autoconfig.BaseConfiguration;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.factory.autoconfig.BeanDefinition;
//import org.springframework.beans.factory.autoconfig.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import java.io.IOException;
//
///**
// * 数据源注册进程
// * @author zuoer
// * @version $Id: DataSourceRegistryPostProcessor.java, v 0.1 2019/12/17 18:21 zuoer Exp $$
// */
//@Configuration
//public class DataSourceRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        //TODO 后续替换成日志
//        System.out.println("初始化dataSource");
//        BeanDefinition dataSource = registry.getBeanDefinition("dataSource");
//        MutablePropertyValues dataSourceProperty = dataSource.getPropertyValues();
//        DataSourceConfig dataSourceConfig= BaseConfiguration.getInstance().getDataSourceConfig();
//        dataSourceProperty.addPropertyValue("driverClassName", dataSourceConfig.getDriverClassName());
//        dataSourceProperty.addPropertyValue("url", dataSourceConfig.getUrl());
//        dataSourceProperty.addPropertyValue("username", dataSourceConfig.getUsername());
//        dataSourceProperty.addPropertyValue("password", dataSourceConfig.getPassword());
//
//        BeanDefinition dataSqlSessionFactory = registry.getBeanDefinition("dataSqlSessionFactory");
//        MutablePropertyValues dataSqlSessionFactoryProperty = dataSqlSessionFactory.getPropertyValues();
//        dataSqlSessionFactoryProperty.addPropertyValue("dataSource", dataSource);
//        try {
//            dataSqlSessionFactoryProperty.addPropertyValue("mapperLocations", new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        BeanDefinition dataTransactionManager = registry.getBeanDefinition("dataTransactionManager");
//        MutablePropertyValues dataTransactionManagerProperty = dataTransactionManager.getPropertyValues();
//        dataTransactionManagerProperty.addPropertyValue("dataSource", dataSource);
//
//        BeanDefinition dataSqlSessionTemplate = registry.getBeanDefinition("dataSqlSessionTemplate");
//        MutablePropertyValues dataSqlSessionTemplateProperty = dataSqlSessionTemplate.getPropertyValues();
//        dataSqlSessionTemplateProperty.addPropertyValue("sqlSessionFactory", dataSqlSessionFactory);
//
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//    }
//}
