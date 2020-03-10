package com.zuoer.sofa.blog.base.datasource.impl;

import com.zuoer.sofa.blog.base.config.BaseConfiguration;
import com.zuoer.sofa.blog.base.datasource.BaseDataSource;
import com.zuoer.sofa.blog.base.datasource.BaseDataSourceComponent;
import com.zuoer.sofa.blog.base.datasource.DataSourceConfig;
import com.zuoer.sofa.blog.base.datasource.DatabaseTypeEnum;
import com.zuoer.sofa.blog.base.proxy.DataSourceProxy;
import com.zuoer.sofa.blog.base.runtime.initializer.RuntimeInitializer;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author zuoer
 * @version BaseDataSourceComponentImpl, v 0.1 2020/3/10 11:16 zuoer Exp $
 */
public class BaseDataSourceComponentImpl implements BaseDataSourceComponent, RuntimeInitializer {

    private BaseDataSource baseDataSource;

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void initialize() {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("开始数据源初始化");
        this.beanFactory = beanFactory;
        refreshDataSourceConfig();
        registerDataSourceBean();
    }

    private void refreshDataSourceConfig() {
        //此处先写死，目前作为个人应用不需要多数据源,同时先写死只支持oracle

        DataSourceConfig dataSourceConfig = BaseConfiguration.getInstance().getDataSourceConfig();

        BaseDataSource newDS = new BaseDataSource("SOFA_BLOG_DATASOURCE");
        newDS.setDatabaseType(DatabaseTypeEnum.ORACLE);
        newDS.setDatabaseCode("SOFA_BLOG_DATASOURCE");
        /**
         * 别注册，会报错javax.management.NotCompliantMBeanException: Class does not expose a management interface:
         * java.lang.Object,jboss的MbeanServer引发，待升级到新版tomcat后可解决此问题
         */
        // ds.setRegisterMbeans(true);
        newDS.setDriverClassName(dataSourceConfig.getDriverClassName());
        newDS.setUrl(dataSourceConfig.getUrl());
        newDS.setUsername(dataSourceConfig.getUsername());
        newDS.setPassword(dataSourceConfig.getPassword());
        // 最大连接数
        newDS.setMaxTotal(50);
        // 初始连接数
        newDS.setInitialSize(10);
        // 最大空闲
        newDS.setMaxIdle(10);
        // 最小空闲数
        newDS.setMinIdle(2);
        // 等待时间
        newDS.setMaxWaitMillis(2 * 1000);
        // 自动回收超时连接
        newDS.setRemoveAbandonedOnBorrow(true);
        // 超过时间限制，回收没有用(废弃)的连接（默认为 300秒，调整为180）
        newDS.setRemoveAbandonedTimeout(180);
        // 是否删除超过removeAbandonedTimout所指定时间的被遗弃的连接。如果设置为true，则一个连接在超过removeAbandonedTimeout所设定的时间未使用即被认为是应该被抛弃并应该被移除的。
        newDS.setRemoveAbandonedOnMaintenance(false);
        newDS.setTestWhileIdle(false);
        newDS.setTestOnBorrow(true);
        newDS.setTestOnReturn(false);
        newDS.setValidationQuery("select 1 from dual;");
        newDS.setValidationQueryTimeout(2);
        newDS.setTimeBetweenEvictionRunsMillis(5 * 1000);
        newDS.setNumTestsPerEvictionRun(5);
        newDS.setMinEvictableIdleTimeMillis(
                BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        baseDataSource = newDS;

    }

    /**
     * 注册数据源为SpringBean
     */
    private void registerDataSourceBean() {
        beanFactory.registerSingleton(baseDataSource.getDataSourceName(), new DataSourceProxy(this));
    }

    @Override
    public BaseDataSource getBaseDataSource() {
        return baseDataSource;
    }
}
