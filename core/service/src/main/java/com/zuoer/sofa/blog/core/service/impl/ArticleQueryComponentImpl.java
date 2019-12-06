/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.zuoer.sofa.blog.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zuoer.sofa.blog.core.model.Article;
import com.zuoer.sofa.blog.core.model.request.ArticleSearchRequest;
import com.zuoer.sofa.blog.core.service.ArticleQueryComponent;
import com.zuoer.sofa.blog.core.service.converter.ArticleConverter;
import com.zuoer.sofa.blog.dal.mapper.ArticleMapper;

/**
 * 
 * @author zuoer
 *
 * @version $Id: ArticleQueryComponentImpl.java, v 0.1 2019年12月5日 下午6:11:12 zuoer Exp $
 */
@Component
public class ArticleQueryComponentImpl implements ArticleQueryComponent {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ArticleConverter articleConverter;

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ArticleQueryComponent#getById(int)
	 */
	@Override
	public Article getById(int id) {
		// TODO Auto-generated method stub
		return articleConverter.convertOne(articleMapper.selectById(id));
	}

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ArticleQueryComponent#getAll()
	 */
	@Override
	public List<Article> getAll() {
		// TODO Auto-generated method stub
		return articleConverter.convertMany(articleMapper.selectAll());
	}

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ArticleQueryComponent#search(com.zuoer.sofa.blog.core.model.request.ArticleSearchRequest)
	 */
	@Override
	public List<Article> search(ArticleSearchRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zuoer.sofa.blog.core.service.ArticleQueryComponent#getByTagId(int)
	 */
	@Override
	public List<Article> getByTagId(int tagId) {
		// TODO Auto-generated method stub
		return null;
	}

}
