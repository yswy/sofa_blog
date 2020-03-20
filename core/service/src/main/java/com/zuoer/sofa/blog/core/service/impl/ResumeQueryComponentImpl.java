/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import com.zuoer.sofa.blog.core.model.Resume;
import com.zuoer.sofa.blog.core.service.ResumeQueryComponent;
import com.zuoer.sofa.blog.core.service.converter.ResumeConverter;
import com.zuoer.sofa.blog.dal.dao.ResumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zuoer
 *
 * @version $Id: ResumeQueryComponentImpl.java, v 0.1 2019年12月6日 下午3:18:42 zuoer Exp $
 */
@Component
public class ResumeQueryComponentImpl implements ResumeQueryComponent {
	
	@Autowired
	private ResumeMapper resumeMapper;
	
	@Autowired
	private ResumeConverter resumeConverter;

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ResumeQueryComponent#getLastOne()
	 */
	@Override
	public Resume getLastOne() {
		// TODO Auto-generated method stub
		return resumeConverter.convertOne(resumeMapper.selectById(1l));
	}

}
