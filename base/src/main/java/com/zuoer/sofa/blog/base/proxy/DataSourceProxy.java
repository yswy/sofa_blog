package com.zuoer.sofa.blog.base.proxy;

import com.zuoer.sofa.blog.base.datasource.BaseDataSourceComponent;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 数据源代理
 * 
 * @author chenbug
 *
 * @version $Id: DataSourceProxy.java, v 0.1 2018年3月29日 下午6:09:00 chenbug Exp $
 */
public class DataSourceProxy implements DataSource {

	private BaseDataSourceComponent baseDataSourceComponent;

	public DataSourceProxy(BaseDataSourceComponent baseDataSourceComponent) {
		super();
		this.baseDataSourceComponent = baseDataSourceComponent;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().isWrapperFor(iface);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		baseDataSourceComponent.getBaseDataSource().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		baseDataSourceComponent.getBaseDataSource().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().getParentLogger();
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return baseDataSourceComponent.getBaseDataSource().getConnection();
	}
}
