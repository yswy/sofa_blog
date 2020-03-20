/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import com.zuoer.sofa.blog.core.model.AdminUser;
import com.zuoer.sofa.blog.core.service.AdminUserQueryComponent;
import com.zuoer.sofa.blog.core.service.converter.AdminUserConverter;
import com.zuoer.sofa.blog.dal.dao.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author zuoer
 *
 * @version $Id: AdminUserQueryComponentImpl.java, v 0.1 2019年12月5日 下午12:24:19 zuoer Exp $
 */
@Component
public class AdminUserQueryComponentImpl implements AdminUserQueryComponent {
	
	@Autowired
	private AdminUserMapper adminUserMapper;

	@Autowired
	private AdminUserConverter adminUserConverter;
	
	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.AdminUserQueryComponent#getAll()
	 */
	@Override
	public List<AdminUser> getAll() {
		// TODO Auto-generated method stub
		return adminUserConverter.convertMany(adminUserMapper.selectAll());
	}

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.AdminUserQueryComponent#getByUserName(java.lang.String)
	 */
	@Override
	public AdminUser getByUserName(String userName) {
		// TODO Auto-generated method stub
		return adminUserConverter.convertOne(adminUserMapper.selectByUserName(userName));
	}

}
