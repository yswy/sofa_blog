/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import com.zuoer.sofa.blog.core.model.Tag;
import com.zuoer.sofa.blog.core.service.TagQueryComponent;
import com.zuoer.sofa.blog.core.service.converter.TagConverter;
import com.zuoer.sofa.blog.dal.dao.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author zuoer
 *
 * @version $Id: TagQueryComponentImpl.java, v 0.1 2019年12月6日 下午3:19:41 zuoer Exp $
 */
@Component
public class TagQueryComponentImpl implements TagQueryComponent {
	
	@Autowired
	private TagMapper tagMapper;
	
	@Autowired
	private TagConverter tagConverter;

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.TagQueryComponent#getAll()
	 */
	@Override
	public List<Tag> getAll() {
		// TODO Auto-generated method stub
		return tagConverter.convertMany(tagMapper.selectAll());
	}

}
