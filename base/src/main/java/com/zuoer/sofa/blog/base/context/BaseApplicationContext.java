package com.zuoer.sofa.blog.base.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zuoer
 * @version $Id: BaseApplicationContext.java, v 0.1 2019/12/11 18:29 zuoer Exp $$
 */
@Component
public class BaseApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 服务器启动，Spring容器初始化时，当加载了当前类为bean组件后，
     * 将会调用下面方法注入ApplicationContext实例
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        System.out.println("初始化了");
        BaseApplicationContext.applicationContext = arg0;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 外部调用这个getBean方法就可以手动获取到bean
     * 用bean组件的name来获取bean
     * @param beanName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T>T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }


}
