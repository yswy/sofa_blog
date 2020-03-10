package com.zuoer.sofa.blog.base.datasource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author zuoer
 * @version BaseDataSourceComponent, v 0.1 2020/3/10 11:16 zuoer Exp $
 */
public interface BaseDataSourceComponent extends BeanFactoryPostProcessor {

    /**
     * 获取数据源
     * @return
     */
    public BaseDataSource getBaseDataSource();
}
