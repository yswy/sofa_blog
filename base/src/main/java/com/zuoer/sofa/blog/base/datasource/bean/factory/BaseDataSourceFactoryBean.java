/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.base.datasource.bean.factory;

import com.zuoer.sofa.blog.base.datasource.BaseDataSourceComponent;
import com.zuoer.sofa.blog.base.proxy.DataSourceProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * 
 * @author chenbug
 *
 * @version $Id: DataSourceFactoryBean.java, v 0.1 2018年3月6日 下午5:07:52 chenbug
 *          Exp $
 */

public class BaseDataSourceFactoryBean implements FactoryBean<DataSource> {

	@Autowired
	private BaseDataSourceComponent baseDataSourceComponent;

	private String dataSourceName;

	@Override
	public DataSource getObject() throws Exception {
		// TODO Auto-generated method stub
		return new DataSourceProxy(baseDataSourceComponent);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return DataSource.class;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

}
