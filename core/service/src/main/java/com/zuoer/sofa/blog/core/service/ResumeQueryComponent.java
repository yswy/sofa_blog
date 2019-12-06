/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service;

import java.util.List;

import com.zuoer.sofa.blog.core.model.Resume;

/**
 * 管理员用户查询组件
 * @author zuoer
 *
 * @version $Id: AdminUserQueryComponent.java, v 0.1 2019年11月28日 下午5:47:02 zuoer Exp $
 */
public interface ResumeQueryComponent {

	/**
	 * 获得最近简历
	 * @return
	 */
	public Resume getLastOne();
	
}
