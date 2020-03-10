/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.dal.autoconfig;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring配置
 *
 * @author chenbug
 *
 * @version $Id: BenchPlatformWebServiceAutoConfig.java, v 0.1 2018年2月23日 下午12:25:24 chenbug Exp $
 */
@Configuration
//@MapperScan(basePackages = "com.zuoer.sofa.blog.dal", sqlSessionFactoryRef = "DBDataSqlSessionFactory")
@MapperScan(basePackages = "com.zuoer.sofa.blog.dal")
public class SofaBlogDalAutoConfig {

//
//	@Bean(name = "DBDataSqlSessionFactory")
//	public SqlSessionFactory sqlSessionFactory(@Qualifier("SOFA_BLOG_DATASOURCE") DataSource dataSource) throws Exception {
//		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//		bean.setDataSource(dataSource);
//		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
//		return bean.getObject();
//	}
//
//	@Bean(name = "DBDataTransactionManager")
//	public DataSourceTransactionManager transactionManager(@Qualifier("SOFA_BLOG_DATASOURCE") DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}
//
//	@Bean(name = "DBDataSqlSessionTemplate")
//	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("SOFA_BLOG_DATASOURCE") SqlSessionFactory sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
}
