package com.zuoer.sofa.blog.base.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseDataSource extends BasicDataSource {

	/**
	 * 数据库代码
	 */
	private String databaseCode;

	/**
	 * 数据源名称
	 */
	private String dataSourceName;

	/**
	 * 连接URL
	 */
	private String url;

	/**
	 * 驱动名称
	 */
	private String driverClassName;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 最大使用数，即使用峰值
	 */
	private int maxUsingCount;

	/**
	 * 数据库类型
	 */
	private DatabaseTypeEnum databaseType;

	/**
	 * 返回等待链接的数量
	 *
	 * @return
	 */
	public int getWaitCount() {
		GenericObjectPool<PoolableConnection> pool = super.getConnectionPool();
		if (pool != null) {
			return pool.getNumWaiters();
		}
		return 0;
	}

	/**
	 * 返回已创建的数量
	 *
	 * @return
	 */
	public long getCreatedCount() {
		GenericObjectPool<PoolableConnection> pool = super.getConnectionPool();
		if (pool != null) {
			return pool.getCreatedCount();
		}
		return 0;
	}

	/**
	 * 返回已借的数量
	 *
	 * @return
	 */
	public long getBorrowedCount() {
		GenericObjectPool<PoolableConnection> pool = super.getConnectionPool();
		if (pool != null) {
			return pool.getBorrowedCount();
		}
		return 0;
	}

	public int getMaxUsingCount() {
		return this.maxUsingCount;
	}

	/**
	 * 返回已销毁的数量
	 *
	 * @return
	 */
	public long getDestroyedCount() {
		GenericObjectPool<PoolableConnection> pool = super.getConnectionPool();
		if (pool != null) {
			return pool.getDestroyedCount();
		}
		return 0;
	}

	/**
	 * 返回已归还的数量
	 *
	 * @return
	 */
	public long getReturnedCount() {
		GenericObjectPool<PoolableConnection> pool = super.getConnectionPool();
		if (pool != null) {
			return pool.getReturnedCount();
		}
		return 0;
	}

	public BaseDataSource(String dataSourceName) {
		super();
		this.dataSourceName = dataSourceName;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		super.setUrl(url);
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
		super.setDriverClassName(driverClassName);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		super.setUsername(username);
	}

	public String getDatabaseCode() {
		return databaseCode;
	}

	public void setDatabaseCode(String databaseCode) {
		this.databaseCode = databaseCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.dbcp2.BasicDataSource#getConnectionPool()
	 */
	@Override
	public GenericObjectPool<PoolableConnection> getConnectionPool() {
		// TODO Auto-generated method stub
		return super.getConnectionPool();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.dbcp2.BasicDataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = super.getConnection();
		// 更新峰值
		int numberActive = this.getNumActive();
		if (maxUsingCount < numberActive) {
			maxUsingCount = numberActive;
		}
		return connection;
	}

	public DatabaseTypeEnum getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DatabaseTypeEnum databaseType) {
		this.databaseType = databaseType;
	}

}
