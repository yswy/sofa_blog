/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.dal.mapper;

import com.zuoer.sofa.blog.dal.entity.WebAccessLogDealDO;

/**
 * 
 * @author zuoer
 *
 * @version $Id: WebAccessLogDealMapper.java, v 0.1 2019年11月28日 上午10:08:30 zuoer Exp $
 */
public interface WebAccessLogDealMapper {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public WebAccessLogDealDO selectById(String id);
}
